package cuj.ejabberd.connector;
/*
*
* project_name: DemoOfSmack
* class_name: 
* class_description: 
* author: cujamin
* create_time: 2016年7月13日
* modifier: 
* modify_time: 
* modify_description: 
* @version
*
 */

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import cuj.ejabberd.config.EjabberdConfig;
import cuj.ejabberd.ssl.MemorizingTrustManager;
import cuj.ejabberd.support.handler.ChatHandler;
import cuj.ejabberd.support.handler.DeliveryReceiptHandler;
import cuj.ejabberd.support.handler.GroupChatHandler;
import cuj.ejabberd.support.handler.RosterHandler;
import cuj.ejabberd.support.listener.EjabberdConnectionListener;
import cuj.ejabberd.support.thread.*;
import cuj.ejabberd.support.thread.manager.ChatManagerThread;
import cuj.ejabberd.support.thread.manager.GroupChatManagerThread;
import cuj.ejabberd.support.thread.manager.PubSubManagerThread;
import cuj.ejabberd.support.thread.manager.RosterManagerThread;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;


public class EjabbberdConnectorImpl implements EjabberdConnector{
	static final private Logger log = Logger.getLogger(EjabbberdConnectorImpl.class);
	static private EjabbberdConnectorImpl ejabberdConnector = null;
	private EjabberdConfig ejabberdConfig= null;
	private XMPPTCPConnection connection = null;
	private Presence presence = null;//状态
	private DeliveryReceiptManager deliveryReceiptManager;//回执
	public FileTransferManager fileTransferManager;

	private ChatManagerThread chatManager;
	private GroupChatManagerThread groupChatManager;
	private RosterManagerThread rosterThread;
	private PubSubManagerThread pubSubManager = null;
	private LiveThread liveThread;


	/**
	 *
	 * @param ejabberdConfig
	 * @return
	 */
	static public EjabbberdConnectorImpl getInstance(EjabberdConfig ejabberdConfig)
	{
		if(ejabberdConnector == null)
		{
			ejabberdConnector = new EjabbberdConnectorImpl(ejabberdConfig);
		}
		return ejabberdConnector;
	}

	/**
	 *
	 * @return
	 */
	static public EjabbberdConnectorImpl getInstance()
	{
		return ejabberdConnector;
	}

	/**
	 *
	 * @param ejabberdConfig
	 */
	private EjabbberdConnectorImpl(EjabberdConfig ejabberdConfig)
	{
		if(ejabberdConfig == null)
		{
			log.info("ejabberdConfig is null");
		}
		else this.ejabberdConfig=ejabberdConfig;
	}

	/**
	 *
	 * @return
	 */
	public XMPPTCPConnection getXMPPTCPConnection()
	{
		return connection;
	}

	/**
	 *
	 */
	public void connect()
	{
		XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
		configBuilder.setServiceName( ejabberdConfig.getServiceName() );
		configBuilder.setHost( ejabberdConfig.getHost() );
		configBuilder.setPort( ejabberdConfig.getPort() );
		configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
		configBuilder.setCompressionEnabled(false);//压缩？
		configBuilder.setKeystoreType("JKS");

//************************************************************************************
		//加入SSL方法
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MemorizingTrustManager mtm = new MemorizingTrustManager();
		try {
			sc.init(null, new X509TrustManager[] { mtm }, new java.security.SecureRandom());
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		configBuilder.setCustomSSLContext(sc);
		System.out.println(" ");
		configBuilder.setHostnameVerifier(
				mtm.wrapHostnameVerifier(new org.apache.http.conn.ssl.StrictHostnameVerifier()));

		try {
			TLSUtils.acceptAllCertificates(configBuilder);//接受所有证书
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

//****************************************************************************
		XMPPTCPConnectionConfiguration config = configBuilder.build();
		connection =new XMPPTCPConnection(config);
//		ProviderManager pm = new ProviderManager();
//		pm.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
//		pm.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());

		try {
			//连接服务器
			connection.connect();
			System.out.println("connect is success");
			//登录服务器(却输入账号密码)****************************
		} catch (SmackException e) {
			System.out.println("************1con");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("************2con");
			e.printStackTrace();
		} catch (XMPPException e) {
			System.out.println("************3con");
			e.printStackTrace();
		}
	}

	/**
	 * 登录:用户名user，密码password
	 * @return
	 */
	public boolean logIn()
	{
		try {
			connection.login(ejabberdConfig.getUserName() , ejabberdConfig.getPassWord());
			System.out.println("LOG IN");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//自身状态
//		setPresence();

		//花名册
		setRosterManager(new RosterHandler());

		//聊天
		setChatManager(new ChatHandler());

		//群聊
		setGroupChatManager(new GroupChatHandler());

		setLiveThread();

		EjabberdConnectionListener connectionListener = new EjabberdConnectionListener();
		connection.addConnectionListener(connectionListener);

		return true;
	}
	//
	public void closeConnection()
	{
		connection.disconnect();
		log.info("disconnect");
	}

//	private void setMessageRecvThread()
//	{
//		messageRecvThread = new MessageRecvThread(new ChatHandler());
//		messageRecvThread.start();
//	}
	/**
	 * 设置当前状态
	 */
	public void setPresence()
	{
		presence = new Presence(Presence.Type.available);
		presence.setStatus("X");
		try {
			connection.sendPacket(presence);
		} catch (NotConnectedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 设置花名册(好友列表)
	 */
	private void setRosterManager(RosterHandler rosterHandler)
	{
		rosterThread = new RosterManagerThread(connection,rosterHandler);
	}
	public void addFriends(String friendjid,String name)
	{
		rosterThread.addFriends(friendjid,name);
	}

	public void delFriends(String friendjid)
	{
		rosterThread.delFriends(friendjid);
	}
	public HashMap showFriendsList()
	{
		return rosterThread.getFriendMap();
	}

	/**
	 * 设置回执
	 * @param deliveryReceiptHandler
	 */
	public void setDeliveryReceipt(final DeliveryReceiptHandler deliveryReceiptHandler)
	{
		deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(connection);
		deliveryReceiptManager.autoAddDeliveryReceiptRequests();
		deliveryReceiptManager.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
		deliveryReceiptManager.addReceiptReceivedListener(
				new ReceiptReceivedListener()
				{
					public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt)
					{
						deliveryReceiptHandler.handle( fromJid,  toJid,  receiptId,  receipt);
					}
				});
	}

	public void setLiveThread()
	{
		liveThread = new LiveThread();
		liveThread.start();
	}


	public void registerUser(String username , String password )
	{
		try {

			AccountManager.sensitiveOperationOverInsecureConnectionDefault(true);
			AccountManager.getInstance(connection).createAccount(username, password);
			System.out.println("注册成功!");
		} catch (NoResponseException n) {
			// TODO Auto-generated catch block
			n.printStackTrace();
			System.out.println("注册失败");
		} catch (XMPPErrorException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}catch (NotConnectedException n)
		{

		}
	}

	//CHAT********************************************************************************

	/**
	 *
	 * @param chatHandler
	 */
	private void setChatManager(ChatHandler chatHandler)
	{
		chatManager = ChatManagerThread.getInstance(connection,chatHandler);
		chatManager.start();
	}

	/**
	 *
	 * @param chatHandler
	 */
	public void setChatHandler(ChatHandler chatHandler)
	{
		chatManager.setChatHander(chatHandler);
	}
	/**
	 *
	 * @param toUserJID
	 * @param chatMessage
	 */
	public void chat(String toUserJID,String chatMessage)
	{
		chatManager.chat(toUserJID,chatMessage);
	}


	//GROUPCHAT****************************************************************************
	/**
	 * @param groupChatHandler
	 */
	private void setGroupChatManager(GroupChatHandler groupChatHandler)
	{
		groupChatManager = GroupChatManagerThread.getInstance(ejabberdConfig.getUserName()+"@"+ejabberdConfig.getServiceName(),connection,groupChatHandler);
		groupChatManager.start();
	}
	public void setGroupChatHandler(GroupChatHandler groupChatHandler)
	{

	}
	public void groupChat(String[] infos)
	{
		groupChatManager.command(infos);
	}

	//PUBSUB*********************************************************************************
	private void setPubSubManager() {
		pubSubManager = PubSubManagerThread.getInstance(connection);
	}
	public void pubsub(String[] infos)
	{
		if(null==pubSubManager)
		{
			pubSubManager = PubSubManagerThread.getInstance(connection);
			pubSubManager.start();
		}
		pubSubManager.sendMessageOnNode(infos);
	}
	public boolean sub(String nodeName)
	{
		if(null==pubSubManager)
		{
			pubSubManager = PubSubManagerThread.getInstance(connection);
			pubSubManager.start();
		}
		if(pubSubManager.sub(nodeName))return true;
		return false;
	}


	//file transfer**************************************************************************

//	/**
//	 * @param fromJID
//	 * @param toJID
//	 * @param fileDir
//	 * @return
//	 */
//	public boolean fileSend(String fromJID, String toJID, String fileDir)
//	{
//		log.info("now is to send file ");
//		OutgoingFileTransfer transfer = null;
//		try
//		{
//			Presence presence = roster.getPresence(toJID);
//			if(presence!=null)
//			{
//				log.info(toJID+":"+presence);
//				toJID=presence.getFrom();
//				log.info(toJID+":"+presence);
//			}
//			else log.info("the "+toJID+"is not friend");
//			fileTransferManager.createOutgoingFileTransfer(toJID);
//			File file = new File(fileDir);
//			transfer = fileTransferManager.createOutgoingFileTransfer(toJID);
//			transfer.sendFile(file, "Sending");
//
//			return true;
//		}
//		catch (Exception e)
//		{
//			log.info(" XFileSend Error Delivering block :"+e);
//		}
//		finally
//		{
//			transfer = null;
//		}
//		return false;
//	}
}

