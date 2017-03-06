//package cuj.ejabberd.support.thread;
//
//import com.alibaba.fastjson.JSON;
//import cuj.ejabberd.common.type.ChatType;
//import cuj.ejabberd.connector.EjabbberdConnectorImpl;
//import cuj.ejabberd.domain.Txt;
//import org.apache.log4j.Logger;
//import org.jivesoftware.smack.SmackException;
//import org.jivesoftware.smack.chat.Chat;
//import org.jivesoftware.smack.chat.ChatManager;
//import java.util.Scanner;
//
///**
// * Created by cujamin on 2017/2/9.
// */
//public class SingleChat {
//    static final Logger log = Logger.getLogger(SingleChat.class);
//
//    private ChatManager chatManager;
//    private Chat newChat;
//    private String toJID;
//
//    public SingleChat(String toJID)
//    {
//        this.toJID = toJID;
//    }
//    public void start()
//    {
//        try
//        {
//            EjabbberdConnectorImpl ejabbberdConnector = EjabbberdConnectorImpl.getInstance();
//            if(ejabbberdConnector ==null)
//            {
//                return;
//            }
//            chatManager = ChatManager.getInstanceFor(ejabbberdConnector.getXMPPTCPConnection());
//            System.out.println("ChatManagerThread is started");
//            Scanner input = new Scanner(System.in);
//            while (true) {
//                String message = input.nextLine();
//                //chat close
//                if(message.equals("0"))
//                {
//                    log.info("chat to :" + toJID + " ; close !");
//                    break;
//                }
//                //send message
//                else
//                {
//                    postData( toJID , message , chatManager);
//                }
//            }
//        }catch (Exception e)
//        {
//            log.error("Single Chat Error : " + e);
//        }finally {
//            chatManager = null;
//            newChat = null;
//            toJID = null;
//        }
//    }
//    private void postData(String to, String data ,ChatManager chatManager)
//    {
//        newChat = chatManager.createChat(to , null);
//        try {
//            Txt txt = new Txt();
//            txt.setType(ChatType.chat);
//            txt.setData(data);
//            newChat.sendMessage(JSON.toJSONString(txt));
//
//        } catch (SmackException.NotConnectedException e) {
//            log.error("Error Delivering block : "+ e);
//        }
//    }
//}
