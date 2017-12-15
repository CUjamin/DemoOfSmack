package cuj.ejabberd.support.handler;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by cujamin on 2016/12/28.
 */
public class ChatHandler {
    private static final Logger log = Logger.getLogger(ChatHandler.class);
    public void handle(Message message)
    {
        log.info("ChatHandler not be Override\n收到消息:"+message.toXML()+"\n");
        if(message.getBody()!=null)
        {
            try {
//                log.info("Recv Chat Message : "+message.getBody()+"\n");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
//    private void handleData(Message message)
//    {
//
//        ++Demo.no;
//        System.out.println("收到消息:"+message.toXML());
//        System.out.println("Demo.no: "+Demo.no+" ; message :"+message.getBody());
////		new ChatManagerThread(message).start();
//    }
}
