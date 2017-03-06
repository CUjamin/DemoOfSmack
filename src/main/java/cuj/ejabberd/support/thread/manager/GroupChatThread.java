package cuj.ejabberd.support.thread.manager;

import cuj.ejabberd.support.handler.GroupChatHandler;
import cuj.ejabberd.support.listener.group.GroupChatMessageListener;
import cuj.ejabberd.support.listener.group.GroupChatPresenceListener;
import cuj.ejabberd.support.listener.group.GroupChatUserStatusListener;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.muc.*;

/**
 * Created by cujamin on 2016/12/16.
 */
public class GroupChatThread implements Runnable
{
    static final Logger log = Logger.getLogger(GroupChatThread.class);
    private MultiUserChat multiUserChat;
    private GroupChatMessageListener groupChatMessageListener;
    private GroupChatUserStatusListener groupChatUserStatusListener;
    private GroupChatPresenceListener groupChatPresenceListener;
    private Roster roster;
    public GroupChatThread(MultiUserChat multiUserChat ,GroupChatHandler groupChatHandler)
    {
        groupChatMessageListener = new GroupChatMessageListener(groupChatHandler);
        groupChatPresenceListener = new GroupChatPresenceListener();
        groupChatUserStatusListener = new GroupChatUserStatusListener();

        this.multiUserChat = multiUserChat;
        log.info("Enter The Group Chat Room ：" + multiUserChat.getRoom());
    }
    public void run()
    {
        multiUserChat.addMessageListener(groupChatMessageListener);
        multiUserChat.addPresenceInterceptor(groupChatPresenceListener);
        multiUserChat.addUserStatusListener(groupChatUserStatusListener);
//        roster = Roster.getInstanceFor(multiUserChat);
        log.info("GroupChatThread is started");
    }
    public MultiUserChat getGroupChat()
    {
        return multiUserChat;
    }
}
