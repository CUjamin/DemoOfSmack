package cuj.ejabberd.support.thread.manager;

import com.alibaba.fastjson.JSON;
import cuj.ejabberd.domain.GroupChatCommand;
import cuj.ejabberd.common.type.command.GroupChatCommandType;
import cuj.ejabberd.common.type.ChatType;
import cuj.ejabberd.domain.Txt;
import cuj.ejabberd.support.handler.GroupChatHandler;
import cuj.ejabberd.support.listener.group.GroupChatMessageListener;
import cuj.ejabberd.support.listener.group.GroupChatPresenceInterceptor;
import cuj.ejabberd.support.listener.group.GroupChatPresenceListener;
import cuj.ejabberd.support.listener.group.GroupChatUserStatusListener;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.*;
import java.util.*;

/**
 * Created by cujamin on 2017/2/10.
 */
public class GroupChatManagerThread extends Thread{
    private static final Logger log = Logger.getLogger(GroupChatManagerThread.class);
    private static GroupChatManagerThread groupChatManagerThread = null;
    private String servername = "cujamin-pc";
    private String userJID = null;
    private MultiUserChatManager groupChatManager;

    private GroupChatMessageListener groupChatMessageListener;
    private GroupChatUserStatusListener groupChatUserStatusListener;
    private GroupChatPresenceListener groupChatPresenceListener;
    private GroupChatPresenceInterceptor groupChatPresenceInterceptor;

    private GroupChatManagerThread(String userJID ,XMPPConnection xmppConnection , GroupChatHandler groupChatHandler)
    {
        this.userJID = userJID;
        groupChatMessageListener = new GroupChatMessageListener(groupChatHandler);
        groupChatUserStatusListener = new GroupChatUserStatusListener();
        groupChatPresenceListener = new GroupChatPresenceListener();
        groupChatPresenceInterceptor = new GroupChatPresenceInterceptor();
        if(null==xmppConnection)
        {
            return;
        }
        groupChatManager = MultiUserChatManager.getInstanceFor(xmppConnection);
        log.info("GroupChatManagerThread is inited");
    }
    public static GroupChatManagerThread getInstance(String userJID ,XMPPConnection xmppConnection , GroupChatHandler groupChatHandler)
    {
        if(null==groupChatManagerThread)
        {
            groupChatManagerThread = new GroupChatManagerThread(userJID,xmppConnection,groupChatHandler);
        }
        return groupChatManagerThread;
    }
    public static GroupChatManagerThread getInstance(String userJID ,XMPPConnection xmppConnection )
    {
        return getInstance(userJID,xmppConnection,new GroupChatHandler());
    }
    public void run()
    {
        groupChatManager.addInvitationListener(new InvitationListener()
        {
            @Override
            public void invitationReceived(final XMPPConnection conn, MultiUserChat room, String inviter, String reason,
                                                    String password, Message message)
            {
                log.info("GroupChatThread has been invited , room jid : "+ room.getRoom());
                try
                {
                    jionGroupChatRoom(room);
                }catch (Exception e)
                {
                    log.error("Exception " +e);
                }
            }
        });
        log.info("Group Chat manager Thread is OK");
    }

    /**
     * 创建并加入聊天室
     * @param roomJID
     */
    public MultiUserChat jionGroupChatRoom(String roomJID)
    {
        MultiUserChat groupChat = groupChatManager.getMultiUserChat(roomJID);
        return jionGroupChatRoom(groupChat);
    }
    private MultiUserChat jionGroupChatRoom(MultiUserChat groupChat)
    {
        try
        {
            if(groupChat.createOrJoin(userJID.split("@")[0].trim()))
            {
                log.info("Join the chat room : "+groupChat.getRoom());
            }

            if(groupChat.addUserStatusListener(groupChatUserStatusListener))
            {
                log.info("Group chat room : user status listener success");
            }
            else
            {
                log.info("Group chat room : user status listener has been added");
            }
            if(groupChat.addMessageListener(groupChatMessageListener))
            {
                log.info("Group chat room : message listener success");
            }
            else
            {
                log.info("Group chat room : message listener has been added");

            }
            groupChat.addParticipantListener(groupChatPresenceListener);
            groupChat.addPresenceInterceptor(groupChatPresenceInterceptor);
        }catch (Exception e)
        {

        }finally {
            return groupChat;
        }

    }
    /**
     *  send message to chat room
     * @param roomJID
     * @param data
     */
    public void sendMessageToChatRoom(String roomJID , String data)
    {
        MultiUserChat groupChat = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the chat room : "+roomJID+" ; can not send message !");
                return;
            }
            log.info("send message to chat room : " + roomJID + " ; message : "+data);
            Txt txt = new Txt();
            txt.setType(ChatType.groupchat);
            txt.setData(data);
            Message message = new Message();
            message.setBody(JSON.toJSONString(txt));
            groupChat.sendMessage(message);
        }catch (SmackException.NotConnectedException n)
        {

        }finally {
            groupChat = null;
        }
    }

    /**
     * 待测试
     */
    public void getHostedRooms()
    {
        List<HostedRoom> hostedRooms = new ArrayList<>();
        try{
            List<String> servers = groupChatManager.getServiceNames();
            for(String server : servers)
            {
                hostedRooms = groupChatManager.getHostedRooms(server);
                log.info("the server : "+ server +" has " + hostedRooms.size() + " Rooms : ");
                for(HostedRoom room:hostedRooms)
                {
                    log.info("\troomJID: " + room.getJid()+" ; name is " +room.getName());
                }
            }
        }catch (Exception e)
        {

        }finally {
            hostedRooms = null;
        }
    }
    /**
     * 获取加入的房间JID
     * get joined rooms
     */
    public void getJoinedRooms()
    {
        Set<String> joinedRooms = new HashSet<>();
        try{
            joinedRooms = groupChatManager.getJoinedRooms();
            if(joinedRooms.size()==0)
            {
                log.info("not joined any rooms: ");
            }
            for (Iterator<String>room=joinedRooms.iterator();room.hasNext();)
            {
                log.info("get joined rooms: " + room.next());
            }
        }catch (Exception e)
        {

        }finally {
            joinedRooms = null;
        }
    }

    /**
     * 离开房间
     * @param roomJID
     */
    public void leaveFromChatRoom(String roomJID)
    {
        MultiUserChat groupChat = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return;
            }
            groupChat.leave();
            log.info("leave the room :"+roomJID);
        }catch (Exception e)
        {

        }finally {
            groupChat = null;
        }

    }

    /**
     * 邀请某人进某个房间
     * @param roomJID
     * @param userJID
     * @param reason
     */
    public void initUserToChatRoom(String roomJID,String userJID , String reason)
    {
        MultiUserChat groupChat = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return;
            }
            groupChat.invite(userJID , reason);
            log.info("init user:"+userJID+" to chat room:"+roomJID);
        }catch (Exception e)
        {

        }finally {
            groupChat = null;
        }
    }

    /**
     * 从某个房间里BAN出某个用户
     * @param roomJID
     * @param userJID
     * @param reason
     */
    public void banUserFromChatRoom(String roomJID,String userJID , String reason)
    {
        MultiUserChat groupChat = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return;
            }
            groupChat.banUser(userJID , reason);
            log.info("ban user :"+userJID+" ; from chat room : "+roomJID);
        }catch (Exception e)
        {

        }finally {
            groupChat = null;
        }
    }
    public void grantMembership(String roomJID,String userJID)
    {
        MultiUserChat groupChat = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return;
            }
            groupChat.grantMembership(userJID);
            log.info("grant member ship :"+userJID+" ; from chat room : "+roomJID);
        }catch (Exception e)
        {

        }finally {
            groupChat = null;
        }
    }

    /**
     * 获取房间的用户名单
     * @param roomJID
     */
    public List<String> getMembersInChatRoom(String roomJID)
    {
        MultiUserChat groupChat = null;
        List<String> members  = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return null;
            }
            members = groupChat.getOccupants();
            if(null==members)
            {
                log.info("no member in the room :"+roomJID);
            }
            else
            {
                log.info(members.size()+" member in the room :" +roomJID);
                for(String member:members)
                {
                    log.info("Member : "+member);
                }
            }
        }catch (Exception e)
        {
            log.error("Exception : "+e);

        }finally
        {
            groupChat = null;
            return members;
        }
    }


    /**
     * 6
     * @param roomJID
     */
    public void getRoomInfo(String roomJID)
    {
        RoomInfo roomInfo;
        try
        {
            roomInfo = groupChatManager.getRoomInfo(roomJID);
            log.info("room : " + roomJID +" information :\n"+
                    roomInfo.getName()+"\n"+
                    roomInfo.getLang()+"\n"+
                    roomInfo.getLdapGroup()+"\n"+
                    roomInfo.getPubSub()+"\n"+
                    roomInfo.getRoom()+"\n"+
                    roomInfo.getSubject()+"\n"+
                    roomInfo.getMaxHistoryFetch()+"\n"+
                    roomInfo.getOccupantsCount()+"\n"
            );
        }catch (Exception e)
        {

        }finally
        {
            roomInfo=null;
        }
    }

    /**
     *
     * @param roomJID
     */
    public void getAdmins(String roomJID)
    {
        MultiUserChat groupChat = null;
        List<Affiliate> affiliates = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return ;
            }
            affiliates = groupChat.getAdmins();
            log.info("admin size : "+affiliates.size());
            for(Affiliate affiliate:affiliates)
            {
                log.info("admin jid: " + affiliate.getJid()+
                        "           admin nick: " + affiliate.getNick()
                );
            }
        }catch (Exception e)
        {

        }finally {
            groupChat = null;
        }
    }
    /**
     * @param roomJID
     * @param userJID
     */
    public void setAdmin(String roomJID,String userJID)
    {
        MultiUserChat groupChat = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return ;
            }
            groupChat.grantAdmin(userJID);
            log.info("set user : "+userJID+"  as admin of room :"+roomJID);
        }catch (Exception e)
        {

        }finally {
            groupChat = null;
        }
    }

    /**
     *
     * @param roomJID
     * @param userJID
     */
    public void revokeAdmin(String roomJID,String userJID)
    {
        MultiUserChat groupChat = null;
        try
        {
            groupChat = groupChatManager.getMultiUserChat(roomJID);
            if(!groupChat.isJoined())
            {
                log.info("not in the room :"+roomJID);
                return ;
            }
            groupChat.revokeAdmin(userJID);
            log.info("revoke admin : "+userJID+"  of room :"+roomJID);
        }catch (Exception e)
        {

        }finally {
            groupChat = null;
        }
    }
    public void command(String[] infos)
    {
        GroupChatCommand groupChatCommand = new GroupChatCommand(infos);
        switch (groupChatCommand.command)
        {
            case GroupChatCommandType.Join_Chat_Room:
                jionGroupChatRoom(groupChatCommand.groupRoomJID);
                break;
            case GroupChatCommandType.Leave_From_Chat_Room:
                leaveFromChatRoom(groupChatCommand.groupRoomJID);
                break;
            case GroupChatCommandType.Send_Message_To_Chat_Room:
                sendMessageToChatRoom(groupChatCommand.groupRoomJID,
                        groupChatCommand.message);
                break;
            
            //// TODO: 2017/2/13
            case GroupChatCommandType.Get_Hosted_Rooms:
                getHostedRooms();
                break;
            case GroupChatCommandType.Get_Joined_Rooms:
                getJoinedRooms();
                break;
            case GroupChatCommandType.Get_Members_In_Chat_Room:
                getMembersInChatRoom(groupChatCommand.groupRoomJID);
                break;
            case GroupChatCommandType.Get_Room_Info:
                getRoomInfo(groupChatCommand.groupRoomJID);
                break;
            case GroupChatCommandType.Init_User_To_Chat_Room:
                initUserToChatRoom(groupChatCommand.groupRoomJID,
                        groupChatCommand.toUserJID,
                        groupChatCommand.message);
                break;
            case GroupChatCommandType.Ban_User_From_Chat_Room:
                banUserFromChatRoom(groupChatCommand.groupRoomJID,
                        groupChatCommand.toUserJID,
                        groupChatCommand.message);
                break;
            case GroupChatCommandType.Grant_Member_ship:
                grantMembership(groupChatCommand.groupRoomJID,
                        groupChatCommand.toUserJID);
                break;
            case GroupChatCommandType.Get_Admin:
                getAdmins(groupChatCommand.groupRoomJID);
                break;
            case GroupChatCommandType.Set_Admin:
                setAdmin(groupChatCommand.groupRoomJID,
                        groupChatCommand.toUserJID);
                break;
            case  GroupChatCommandType.Revoke_Admin:
                revokeAdmin(groupChatCommand.groupRoomJID,
                        groupChatCommand.toUserJID);
                break;
            default:
                sendMessageToChatRoom(groupChatCommand.groupRoomJID,
                        groupChatCommand.message);
                break;
        }
    }
}
