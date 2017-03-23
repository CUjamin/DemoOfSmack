package cuj.ejabberd.connector;

import cuj.ejabberd.support.handler.ChatHandler;
import cuj.ejabberd.support.handler.DeliveryReceiptHandler;
import cuj.ejabberd.support.handler.GroupChatHandler;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.HashMap;

/**
 * Created by cujamin on 2016/12/28.
 */
public interface EjabberdConnector {
    XMPPTCPConnection getXMPPTCPConnection();
    //登录
    boolean connectAndLogin();
    void closeConnection();
    //注册
    void registerUser(String username , String password );

    //状态
    void setPresence();

    //回执
//    void setDeliveryReceipt(final DeliveryReceiptHandler deliveryReceiptHandler);

    //花名册
//    void setRoster();
    void addFriends(String friendjid,String name);
    void delFriends(String friendjid);
    HashMap showFriendsList();

    //聊天
//    void setChat();
    void setChatHandler(ChatHandler chatHandler);
    void chat(String toUserJID,String chatMessage);

    //群聊
//    void setGroupChat();
    void setGroupChatHandler(GroupChatHandler groupChatHandler);
    void groupChat(String[] infos);

    //订阅/推送
    void pubsub(String[] infos);
    boolean sub(String nodeName);
}
