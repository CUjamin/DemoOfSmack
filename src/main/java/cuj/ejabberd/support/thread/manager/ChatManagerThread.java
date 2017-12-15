package cuj.ejabberd.support.thread.manager;

import com.alibaba.fastjson.JSON;
import cuj.ejabberd.common.type.ChatType;
import cuj.ejabberd.connector.EjabbberdConnectorImpl;
import cuj.ejabberd.domain.Txt;
import cuj.ejabberd.support.handler.ChatHandler;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cujamin on 2016/12/16.
 */ //发送消息线程
public class ChatManagerThread extends Thread
{
	private static final Logger log = Logger.getLogger(ChatManagerThread.class);
	private static ChatManagerThread chatManagerThread = null;
	//	ProviderManager providerManager;
	private ChatManager chatManager = null;
	//<toJID,Chat>
	private Map<String,Chat> chatMap = new HashMap<String,Chat>();
	private ChatHandler chatHander;

	public ChatManagerThread(XMPPConnection connection , ChatHandler chatHander)
	{
		if(null==connection||connection.isConnected()==false)
		{
			return;
		}
		chatManager = ChatManager.getInstanceFor(connection);
		this.chatHander = chatHander;
	}

	public void run()
	{
		log.info(" [ Chat manager Thread init ... ... ] ");
		EjabbberdConnectorImpl ejabbberdConnector = EjabbberdConnectorImpl.getInstance();
		if(ejabbberdConnector ==null)
		{
			return;
		}
		chatManager.addChatListener(
				new ChatManagerListener() {
					@Override
					public void chatCreated(Chat chat, boolean createdLocally)
					{
						if (!createdLocally)
						{
							chat.addMessageListener(new ChatMessageListener()
							{
								@Override
								public void processMessage(Chat chat, Message message)
								{
//									log.info("ChatManagerThread\n"+message);
									if(message.getType().equals(Message.Type.chat))
									{
										chatHander.handle(message);
									}
									else
									{
										log.info("type : "+message.getType() + " ; body : "+message.getBody());
									}
								}
							});
						}
					}
				});
		log.info(" [ Chat manager Thread init OK ] ");
	}
	public void setChatHander(ChatHandler chatHander)
	{
		this.chatHander = chatHander;
	}
	public void chat(String toJID, String data)
	{
		Chat newChat;
		if(chatMap.containsKey(toJID))
		{
			newChat = chatMap.get(toJID);
		}
		else
		{
			newChat = chatManager.createChat(toJID , null);
			chatMap.put(toJID,newChat);
		}
		try {
			Txt txt = new Txt();
			txt.setType(ChatType.chat);
			txt.setData(data);
			newChat.sendMessage(JSON.toJSONString(txt));
		} catch (SmackException.NotConnectedException e) {
			log.error("Error Delivering block",e);
		}
	}
	public void closeChat(String toJID)
	{
		if(chatMap.containsKey(toJID))
		{
			chatMap.remove(toJID);
		}
		log.info("close chat : "+toJID);
	}
}
