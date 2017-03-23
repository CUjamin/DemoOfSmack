package cuj.ejabberd.ssl;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * A X509 trust manager implementation which asks the user about invalid
 * certificates and memorizes their decision.
 * <p>
 * The certificate validity is checked using the system default X509
 * TrustManager, creating a query Dialog if the check fails.
 * <p>
 * <b>WARNING:</b> This only works if a dedicated thread is used for
 * opening sockets!
 */
public class MemorizingTrustManager implements X509TrustManager {
    final static String DECISION_INTENT = "de.duenndns.ssl.DECISION";
    final static String DECISION_INTENT_ID     = DECISION_INTENT + ".decisionId";
    final static String DECISION_INTENT_CERT   = DECISION_INTENT + ".cert";
    final static String DECISION_INTENT_CHOICE = DECISION_INTENT + ".decisionChoice";

    private final static Logger LOGGER = Logger.getLogger(MemorizingTrustManager.class.getName());
    final static String DECISION_TITLE_ID      = DECISION_INTENT + ".titleId";
    private final static int NOTIFICATION_ID = 100509;

    static String KEYSTORE_DIR = "KeyStore";
    static String KEYSTORE_FILE = "KeyStore.p12";

    private static int decisionId = 0;

    private File keyStoreFile;
    private KeyStore appKeyStore;
    private X509TrustManager defaultTrustManager;
    private X509TrustManager appTrustManager;

    /** Creates an instance of the MemorizingTrustManager class that falls back to a custom TrustManager.
     *
     * You need to supply the application context. This has to be one of:
     *    - Application
     *    - Activity
     *    - Service
     *
     * The context is used for file management, to display the dialog /
     * notification and for obtaining translated strings.
     *
     * @param defaultTrustManager Delegate trust management to this TM. If null, the user must accept every certificate.
     */
    public MemorizingTrustManager(X509TrustManager defaultTrustManager) {
        init();
        this.appTrustManager = getTrustManager(appKeyStore);
        this.defaultTrustManager = defaultTrustManager;
    }

    /** Creates an instance of the MemorizingTrustManager class using the system X509TrustManager.
     *
     * You need to supply the application context. This has to be one of:
     *    - Application
     *    - Activity
     *    - Service
     *
     * The context is used for file management, to display the dialog /
     * notification and for obtaining translated strings.
     *
     */
    public MemorizingTrustManager() {
    	init();
        this.appTrustManager = getTrustManager(appKeyStore);
        this.defaultTrustManager = getTrustManager(null);
    }

    void init() {
    	LOGGER.info("init()");
        keyStoreFile = new File(KEYSTORE_FILE);

        if (!keyStoreFile.exists()){
            try {
                keyStoreFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        appKeyStore = loadAppKeyStore();
    }


    /**
     * Returns a X509TrustManager list containing a new instance of
     * TrustManagerFactory.
     *
     * This function is meant for convenience only. You can use it
     * as follows to integrate TrustManagerFactory for HTTPS sockets:
     *
     * <pre>
     *     SSLContext sc = SSLContext.getInstance("TLS");
     *     sc.init(null, MemorizingTrustManager.getInstanceList(this),
     *         new java.security.SecureRandom());
     *     HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
     * </pre>
     */
    public static X509TrustManager[] getInstanceList() {
    	LOGGER.info("getInstanceList()");
        return new X509TrustManager[] { new MemorizingTrustManager() };
    }

    /**
     * Changes the path for the KeyStore file.
     *
     * The actual filename relative to the app's directory will be
     * <code>app_<i>dirname</i>/<i>filename</i></code>.
     *
     * @param dirname directory to store the KeyStore.
     * @param filename file name for the KeyStore.
     */
    public static void setKeyStoreFile(String dirname, String filename) {
    	LOGGER.info("setKeyStoreFile(String dirname, String filename)");
        KEYSTORE_DIR = dirname;
        KEYSTORE_FILE = filename;
    }

    /**
     * Get a list of all certificate aliases stored in MTM.
     *
     * @return an {@link Enumeration} of all certificates
     */
    public Enumeration<String> getCertificates() {
    	LOGGER.info("getCertificates()");
        try {
            return appKeyStore.aliases();
        } catch (KeyStoreException e) {
            // this should never happen, however...
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a certificate for a given alias.
     *
     * @param alias the certificate's alias as returned by {@link #getCertificates()}.
     *
     * @return the certificate associated with the alias or <tt>null</tt> if none found.
     */
    public Certificate getCertificate(String alias) {
    	LOGGER.info("getCertificate(String alias)");
        try {
            return appKeyStore.getCertificate(alias);
        } catch (KeyStoreException e) {
            // this should never happen, however...
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes the given certificate from MTMs key store.
     *
     * <p>
     * <b>WARNING</b>: this does not immediately invalidate the certificate. It is
     * well possible that (a) data is transmitted over still existing connections or
     * (b) new connections are created using TLS renegotiation, without a new cert
     * check.
     * </p>
     * @param alias the certificate's alias as returned by {@link #getCertificates()}.
     *
     * @throws KeyStoreException if the certificate could not be deleted.
     */
    public void deleteCertificate(String alias) throws KeyStoreException {
    	LOGGER.info("deleteCertificate(String alias)");
        appKeyStore.deleteEntry(alias);
        keyStoreUpdated();
    }

    /**
     * Creates a new hostname verifier supporting user interaction.
     *
     * <p>This method creates a new {@link HostnameVerifier} that is bound to
     * the given instance of {@link MemorizingTrustManager}, and leverages an
     * existing {@link HostnameVerifier}. The returned verifier performs the
     * following steps, returning as soon as one of them succeeds:
     *  </p>
     *  <ol>
     *  <li>Success, if the wrapped defaultVerifier accepts the certificate.</li>
     *  <li>Success, if the server certificate is stored in the keystore under the given hostname.</li>
     *  <li>Ask the user and return accordingly.</li>
     *  <li>Failure on exception.</li>
     *  </ol>
     *
     * @param defaultVerifier the {@link HostnameVerifier} that should perform the actual check
     * @return a new hostname verifier using the MTM's key store
     *
     * @throws IllegalArgumentException if the defaultVerifier parameter is null
     */
    public HostnameVerifier wrapHostnameVerifier(final HostnameVerifier defaultVerifier) {
    	LOGGER.info("wrapHostnameVerifier(final HostnameVerifier defaultVerifier)");
        if (defaultVerifier == null)
            throw new IllegalArgumentException("The default verifier may not be null");

        return new MemorizingHostnameVerifier(defaultVerifier);
    }

    X509TrustManager getTrustManager(KeyStore ks) {
    	LOGGER.info("getTrustManager(KeyStore ks)");
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);
            for (TrustManager t : tmf.getTrustManagers()) {
                if (t instanceof X509TrustManager) {
                    return (X509TrustManager)t;
                }
            }
        } catch (Exception e) {
            // Here, we are covering up errors. It might be more useful
            // however to throw them out of the constructor so the
            // embedding app knows something went wrong.
            LOGGER.log(Level.SEVERE, "getTrustManager(" + ks + ")", e);
        }
        return null;
    }

    KeyStore loadAppKeyStore() {
    	LOGGER.info("loadAppKeyStore()");
        KeyStore ks;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            LOGGER.log(Level.SEVERE, "getAppKeyStore()", e);
            return null;
        }
        try {
            ks.load(null, null);
        } catch (NoSuchAlgorithmException ne) {
            LOGGER.log(Level.SEVERE, "getAppKeyStore(" + keyStoreFile + ")", ne);
        }catch (CertificateException ce)
        {

        }catch (IOException ie)
        {

        }
        InputStream is = null;
        try {
            is = new java.io.FileInputStream(keyStoreFile);
            ks.load(is, "MTM".toCharArray());
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            LOGGER.log(Level.INFO, "getAppKeyStore(" + keyStoreFile + ") - exception loading file key store", e);//报错
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.log(Level.FINE, "getAppKeyStore(" + keyStoreFile + ") - exception closing file key store input stream", e);
                }
            }
        }
        return ks;
    }

    void storeCert(String alias, Certificate cert) {
    	LOGGER.info("storeCert(String alias, Certificate cert)");
        try {
        	LOGGER.log(Level.SEVERE, "storeCert");
            appKeyStore.setCertificateEntry(alias, cert);
        } catch (KeyStoreException e) {
            LOGGER.log(Level.SEVERE, "storeCert(" + cert + ")", e);
            return;
        }
        keyStoreUpdated();
    }

    void storeCert(X509Certificate cert) {
    	LOGGER.info("storeCert(X509Certificate cert)");
        storeCert(cert.getSubjectDN().toString(), cert);
    }

    void keyStoreUpdated() {
    	LOGGER.info("keyStoreUpdated()");
        // reload appTrustManager
        appTrustManager = getTrustManager(appKeyStore);

        // store KeyStore to file
        java.io.FileOutputStream fos = null;
        try {
        	LOGGER.log(Level.SEVERE,"write in keystore" );
            fos = new java.io.FileOutputStream(keyStoreFile);
            appKeyStore.store(fos, "MTM".toCharArray());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "storeCert(" + keyStoreFile + ")", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "storeCert(" + keyStoreFile + ")", e);
                }
            }
        }
    }

    // if the certificate is stored in the app key store, it is considered "known"
    private boolean isCertKnown(X509Certificate cert) {
    	LOGGER.info("isCertKnown(X509Certificate cert) ");
        try {
            return appKeyStore.getCertificateAlias(cert) != null;
        } catch (KeyStoreException e) {
            return false;
        }
    }

    private static boolean isExpiredException(Throwable e) {
    	LOGGER.info("isExpiredException(Throwable e) ");
        do {
            if (e instanceof CertificateExpiredException)
                return true;
            e = e.getCause();
        } while (e != null);
        return false;
    }

    private static boolean isPathException(Throwable e) {
    	LOGGER.info("isPathException(Throwable e)");
        do {
            if (e instanceof CertPathValidatorException)
                return true;
            e = e.getCause();
        } while (e != null);
        return false;
    }

    public void checkCertTrusted(X509Certificate[] chain, String authType, boolean isServer)
            throws CertificateException
    {
        LOGGER.log(Level.FINE, "checkCertTrusted(" + chain + ", " + authType + ", " + isServer + ")");
        try {
            LOGGER.log(Level.FINE, "checkCertTrusted: trying appTrustManager");
            if (isServer)
                appTrustManager.checkServerTrusted(chain, authType);
            else
                appTrustManager.checkClientTrusted(chain, authType);
        } catch (CertificateException ae) {
            LOGGER.log(Level.FINER, "checkCertTrusted: appTrustManager did not verify certificate. Will fall back to secondary verification mechanisms (if any).", ae);
            // if the cert is stored in our appTrustManager, we ignore expiredness
            if (isExpiredException(ae)) {
                LOGGER.log(Level.INFO, "checkCertTrusted: accepting expired certificate from keystore");
                return;
            }
            if (isCertKnown(chain[0])) {
                LOGGER.log(Level.INFO, "checkCertTrusted: accepting cert already stored in keystore");
                return;
            }
            try {
                if (defaultTrustManager == null) {
                    LOGGER.fine("No defaultTrustManager set. Verification failed, throwing " + ae);
                    throw ae;
                }
                LOGGER.log(Level.FINE, "checkCertTrusted: trying defaultTrustManager");
                if (isServer)
                    defaultTrustManager.checkServerTrusted(chain, authType);
                else
                    defaultTrustManager.checkClientTrusted(chain, authType);
            } catch (CertificateException e) {
                LOGGER.log(Level.FINER, "checkCertTrusted: defaultTrustManager failed", e);
                interactCert(chain, authType, e);
            }
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
    {
        checkCertTrusted(chain, authType, false);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
    {
        checkCertTrusted(chain, authType, true);
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        LOGGER.log(Level.FINE, "getAcceptedIssuers()");
        return defaultTrustManager.getAcceptedIssuers();
    }

  

    private static String hexString(byte[] data) {
    	LOGGER.info("hexString(byte[] data)");
        StringBuilder si = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            si.append(String.format("%02x", data[i]));
            if (i < data.length - 1)
                si.append(":");
        }
        return si.toString();
    }

    private static String certHash(final X509Certificate cert, String digest) {
    	LOGGER.info("certHash(final X509Certificate cert, String digest)");
        try {
            MessageDigest md = MessageDigest.getInstance(digest);
            md.update(cert.getEncoded());
            return hexString(md.digest());
        } catch (java.security.cert.CertificateEncodingException e) {
            return e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            return e.getMessage();
        }
    }

    private static void certDetails(StringBuilder si, X509Certificate c) {
    	LOGGER.info("certDetails(StringBuilder si, X509Certificate c)");
        SimpleDateFormat validityDateFormater = new SimpleDateFormat("yyyy-MM-dd");
        si.append("\n");
        si.append(c.getSubjectDN().toString());
        si.append("\n");
        si.append(validityDateFormater.format(c.getNotBefore()));
        si.append(" - ");
        si.append(validityDateFormater.format(c.getNotAfter()));
        si.append("\nSHA-256: ");
        si.append(certHash(c, "SHA-256"));
        si.append("\nSHA-1: ");
        si.append(certHash(c, "SHA-1"));
        si.append("\nSigned by: ");
        si.append(c.getIssuerDN().toString());
        si.append("\n");
    }

    int interact() {
    	LOGGER.info("interact()");
		/* prepare the MTMDecision blocker object */
//        MTMDecision choice = new MTMDecision();
//        final int myId = createDecisionId(choice);
//
//        masterHandler.post(new Runnable() {
//            public void run() {
//                Intent ni = new Intent(master, MemorizingActivity.class);
//                ni.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ni.setData(Uri.parse(MemorizingTrustManager.class.getName() + "/" + myId));
//                ni.putExtra(DECISION_INTENT_ID, myId);
//                ni.putExtra(DECISION_INTENT_CERT, message);
//                ni.putExtra(DECISION_TITLE_ID, titleId);
//
//                // we try to directly start the activity and fall back to
//                // making a notification
//                try {
//                    getUI().startActivity(ni);
//                } catch (Exception e) {
//                    LOGGER.log(Level.FINE, "startActivity(MemorizingActivity)", e);
//                    startActivityNotification(ni, myId, message);
//                }
//            }
//        });
//
//        LOGGER.log(Level.FINE, "openDecisions: " + openDecisions + ", waiting on " + myId);
//        try {
//            synchronized(choice) { choice.wait(); }
//        } catch (InterruptedException e) {
//            LOGGER.log(Level.FINER, "InterruptedException", e);
//        }
//        LOGGER.log(Level.FINE, "finished wait on " + myId + ": " + choice.state);
//        return choice.state;
        return MTMDecision.DECISION_ALWAYS;
    }

    void interactCert(final X509Certificate[] chain, String authType, CertificateException cause)
            throws CertificateException
    {
    	LOGGER.info("interactCert(final X509Certificate[] chain, String authType, CertificateException cause)");
        switch (interact()) {
            case MTMDecision.DECISION_ALWAYS:
                storeCert(chain[0]); // only store the server cert, not the whole chain
            case MTMDecision.DECISION_ONCE:
                break;
            default:
                throw (cause);
        }
    }

    boolean interactHostname(X509Certificate cert, String hostname)
    {
    	LOGGER.info("interactHostname(X509Certificate cert, String hostname)");
        switch (interact()) {
            case MTMDecision.DECISION_ALWAYS:
                storeCert(hostname, cert);
            case MTMDecision.DECISION_ONCE:
                return true;
            default:
                return false;
        }
    }

    class MemorizingHostnameVerifier implements HostnameVerifier {
        private HostnameVerifier defaultVerifier;

        public MemorizingHostnameVerifier(HostnameVerifier wrapped) {
        	LOGGER.info("MemorizingHostnameVerifier(HostnameVerifier wrapped)");
            defaultVerifier = wrapped;
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
        	LOGGER.info("verify(String " + hostname + ", SSLSession " + session);
        	LOGGER.info("SSLSession: session-PeerHost = " + session.getPeerHost());
        	LOGGER.info("SSLSession: session-CipherSuite = " + session.getCipherSuite());
            LOGGER.log(Level.FINE, "hostname verifier for " + hostname + ", trying default verifier first");
            // if the default verifier accepts the hostname, we are done
            if (defaultVerifier.verify(hostname, session)) {
                LOGGER.log(Level.FINE, "default verifier accepted " + hostname);
                LOGGER.info("1");
                return true;
            }
            // otherwise, we check if the hostname is an alias for this cert in our keystore
            try {
                X509Certificate cert = (X509Certificate)session.getPeerCertificates()[0];
                //Log.d(TAG, "cert: " + cert);
                if (cert.equals(appKeyStore.getCertificate(hostname.toLowerCase(Locale.US)))) {
                    LOGGER.log(Level.FINE, "certificate for " + hostname + " is in our keystore. accepting.");
                    LOGGER.info("2");
                    return true;
                } else {
                    LOGGER.log(Level.FINE, "server " + hostname + " provided wrong certificate, asking user.");
                    LOGGER.info("right!!");
                    return interactHostname(cert, hostname);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("3");
                return false;
            }
        }
    }
}
