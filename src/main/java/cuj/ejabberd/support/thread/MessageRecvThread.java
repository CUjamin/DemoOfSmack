//package cuj.ejabberd.support.thread;
//
//import cuj.ejabberd.connector.EjabbberdConnectorImpl;
//import cuj.ejabberd.support.handler.ChatHandler;
//import org.jivesoftware.smack.chat.ChatManager;
//import org.jivesoftware.smack.chat.ChatManagerListener;
//import org.jivesoftware.smack.chat.ChatMessageListener;
//import org.jivesoftware.smack.packet.Message;
//
///**
// * Created by cujamin on 2017/2/9.
// */
//public class MessageRecvThread extends Thread{
//    private ChatHandler chatHander;
//    private ChatManager chatRecvManager;
//    public MessageRecvThread(ChatHandler chatHander)
//    {
//        this.chatHander = chatHander;
//    }
//    public void run()
//    {
//        EjabbberdConnectorImpl ejabbberdConnector = EjabbberdConnectorImpl.getInstance();
//        chatRecvManager = ChatManager.getInstanceFor(ejabbberdConnector.getXMPPTCPConnection());
//        if(ejabbberdConnector ==null)
//        {
//            return;
//        }
//
//        chatRecvManager.addChatListener(
//                new ChatManagerListener() {
//                    @Override
//                    public void chatCreated(org.jivesoftware.smack.chat.Chat chat, boolean createdLocally)
//                    {
//                        if (!createdLocally)
//                        {
//                            chat.addMessageListener(new ChatMessageListener()
//                            {
//                                @Override
//                                public void processMessage(org.jivesoftware.smack.chat.Chat chat, Message message)
//                                {
//                                    System.out.println("MessageRecvThread");
//                                    chatHander.handle(message);
////	        						if(message.getBody()!=null)
////	        						{
////	        							handleData(message);
//////	        							try {
//////											chat.sendMessage("**********************mgw01  Receivered  message");
//////										} catch (NotConnectedException e) {
//////											// TODO Auto-generated catch block
//////											e.printStackTrace();
//////										}
////	        						}
//                                }
//                            });
//                        }
//                    }
//                });
//    }
//}
