//package cuj.ejabberd.support.listener.group;
//
//import org.apache.log4j.Logger;
//import org.jivesoftware.smackx.muc.UserStatusListener;
//
///**
// * Created by cujamin on 2017/2/14.
// */
//public class GroupChatUserStatusListener implements UserStatusListener {
//    static final Logger log = Logger.getLogger(GroupChatUserStatusListener.class);
//
//    /**
//     * Called when a moderator kicked your user from the room. This means that you are no longer
//     * participanting in the room.
//     *
//     * @param actor the moderator that kicked your user from the room (e.g. user@host.org).
//     * @param reason the reason provided by the actor to kick you from the room.
//     */
//    public void kicked(String actor, String reason)
//    {
//        log.info("kicked actor: "+actor+" ; reason : "+reason);
//    }
//
//    /**
//     * Called when a moderator grants voice to your user. This means that you were a visitor in
//     * the moderated room before and now you can participate in the room by sending messages to
//     * all occupants.
//     *
//     */
//    public void voiceGranted()
//    {
//        log.info("voiceGranted");
//    }
//
//    /**
//     * Called when a moderator revokes voice from your user. This means that you were a
//     * participant in the room able to speak and now you are a visitor that can't send
//     * messages to the room occupants.
//     *
//     */
//    public void voiceRevoked()
//    {
//        log.info("voiceRevoked");
//    }
//
//    /**
//     * Called when an administrator or owner banned your user from the room. This means that you
//     * will no longer be able to join the room unless the ban has been removed.
//     *
//     * @param actor the administrator that banned your user (e.g. user@host.org).
//     * @param reason the reason provided by the administrator to banned you.
//     */
//    public void banned(String actor, String reason)
//    {
//        log.info("banned actor : "+actor+" ; reason : "+reason);
//    }
//
//
//
//    /**
//     * Called when an administrator grants your user membership to the room. This means that you
//     * will be able to join the members-only room.
//     *
//     */
//    public void membershipGranted()
//    {
//        log.info("membershipGranted");
//    }
//
//    /**
//     * Called when an administrator revokes your user membership to the room. This means that you
//     * will not be able to join the members-only room.
//     *
//     */
//    public void membershipRevoked()
//    {
//        log.info("membershipRevoked");
//    }
//
//    /**
//     * Called when an administrator grants moderator privileges to your user. This means that you
//     * will be able to kick users, grant and revoke voice, invite other users, modify room's
//     * subject plus all the partcipants privileges.
//     *
//     */
//    public void moderatorGranted()
//    {
//        log.info("moderatorGranted");
//    }
//
//    /**
//     * Called when an administrator revokes moderator privileges from your user. This means that
//     * you will no longer be able to kick users, grant and revoke voice, invite other users,
//     * modify room's subject plus all the partcipants privileges.
//     *
//     */
//    public void moderatorRevoked()
//    {
//        log.info("moderatorRevoked");
//    }
//
//    /**
//     * Called when an owner grants to your user ownership on the room. This means that you
//     * will be able to change defining room features as well as perform all administrative
//     * functions.
//     *
//     */
//    public void ownershipGranted()
//    {
//        log.info("ownershipGranted");
//    }
//
//    /**
//     * Called when an owner revokes from your user ownership on the room. This means that you
//     * will no longer be able to change defining room features as well as perform all
//     * administrative functions.
//     *
//     */
//    public void ownershipRevoked()
//    {
//        log.info("ownershipRevoked");
//    }
//
//    /**
//     * Called when an owner grants administrator privileges to your user. This means that you
//     * will be able to perform administrative functions such as banning users and edit moderator
//     * list.
//     *
//     */
//    public void adminGranted()
//    {
//        log.info("adminGranted");
//    }
//
//    /**
//     * Called when an owner revokes administrator privileges from your user. This means that you
//     * will no longer be able to perform administrative functions such as banning users and edit
//     * moderator list.
//     *
//     */
//    public void adminRevoked()
//    {
//        log.info("adminRevoked");
//    }
//}
