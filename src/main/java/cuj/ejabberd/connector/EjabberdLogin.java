package cuj.ejabberd.connector;

/**
 * Created by cujamin on 2017/2/7.
 */
public interface EjabberdLogin {
    void setServerInfo(String serviceName , String host , int port);
    void setUserInfo(String username,String password);
    boolean logIn();
    void logOut();
    void chat(String toUserJID,String message);
    void groupChat(String[] infos);
//    void stopChat(String toUserJID);
//    void createChatRoom(String roomJID);
//    void startGroupChat(String toRoomJID);
//    void stopGroupChat(String toUserJID);
    void pubsub(String[] infos);
}
