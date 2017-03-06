package cuj.ejabberd.support.handler;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by cujamin on 2016/12/28.
 */
public class GroupChatHandler {
    private static final Logger log = Logger.getLogger(GroupChatHandler.class);
    public void handle(Message message)
    {
        log.info("\nGroupChatHandler not be Override\nreceive message from room : " + message.getFrom() +" ; message body : "+ message.getBody()+" ; _>"+message+"\n");
    }
}
