package cuj.ejabberd.support.listener.group;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by cujamin on 2017/2/14.
 */
public class GroupChatPresenceListener implements PresenceListener {
    private static final Logger log = Logger.getLogger(GroupChatPresenceListener.class);
    @Override
    public void processPresence(Presence presence) {
        log.info(presence.getFrom().split("/")[1]+" status is "+presence.getType());
    }
}
