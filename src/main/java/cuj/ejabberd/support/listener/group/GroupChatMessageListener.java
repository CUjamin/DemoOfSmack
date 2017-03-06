package cuj.ejabberd.support.listener.group;

import cuj.ejabberd.support.handler.GroupChatHandler;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by cujamin on 2017/2/14.
 */
public class GroupChatMessageListener implements MessageListener {
    private GroupChatHandler groupChatHandler;
    public GroupChatMessageListener(GroupChatHandler groupChatHandler)
    {
        this.groupChatHandler = groupChatHandler;
    }
    public void processMessage(Message message)
    {
        groupChatHandler.handle(message);
    }

    public GroupChatHandler getGroupChatHandler() {
        return groupChatHandler;
    }

    public void setGroupChatHandler(GroupChatHandler groupChatHandler) {
        this.groupChatHandler = groupChatHandler;
    }
}
