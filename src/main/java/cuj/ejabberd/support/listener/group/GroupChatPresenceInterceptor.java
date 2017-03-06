package cuj.ejabberd.support.listener.group;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by cujamin on 2017/2/15.
 */
public class GroupChatPresenceInterceptor implements PresenceListener {
    private static final Logger log = Logger.getLogger(GroupChatPresenceInterceptor.class);

    public void processPresence(Presence presence)
    {
        log.info("GroupChatPresenceInterceptor : "+presence);
    }
}
