//package cuj.ejabberd.support.thread;
//
//import cuj.ejabberd.connector.EjabbberdConnectorImpl;
//import org.apache.log4j.Logger;
//import org.jivesoftware.smackx.ping.PingManager;
//
///**
// * Created by cujamin on 2017/2/9.
// */
//public class PingThread extends Thread{
//    static final Logger log = Logger.getLogger(PingThread.class);
//    PingManager pingManager ;
//    public void run()
//    {
//        EjabbberdConnectorImpl ejabbberdConnector = EjabbberdConnectorImpl.getInstance();
//        if(ejabbberdConnector ==null)
//        {
//            return;
//        }
//        pingManager = PingManager.getInstanceFor(ejabbberdConnector.getXMPPTCPConnection());
//        try
//        {
//            log.info("ping mgw02 is "+pingManager.ping("mgw02@cujamin-pc",10));
//            log.info("XMPP0199:"+pingManager.isPingSupported("cujamin-pc"));
//        }catch (Exception e)
//        {
//            log.error("error");
//        }
//    }
//}
