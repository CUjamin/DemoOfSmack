package cuj.ejabberd.connector;

import cuj.ejabberd.config.EjabberdConfig;
import cuj.ejabberd.support.handler.GroupChatHandler;

/**
 * Created by cujamin on 2017/2/7.
 */
public class EjabberdLoginImpl implements EjabberdLogin {
    private EjabberdConfig chatConfig;
    public static EjabbberdConnectorImpl chatConnector;

    /**
     *
     * @param serviceName
     * @param host
     * @param port
     */
    public void setServerInfo(String serviceName , String host , int port)
    {
        chatConfig = EjabberdConfig.getInstance(serviceName,host,port);
    }

    /**
     *
     * @param username
     * @param password
     */
    public void setUserInfo(String username,String password)
    {
        chatConfig.setUserName(username);
        chatConfig.setPassWord(password);
    }

    public boolean logIn()
    {
        chatConnector = EjabbberdConnectorImpl.getInstance(chatConfig);
        chatConnector.connect();
        boolean result = chatConnector.logIn();
        return result;
    }
    public void logOut()
    {
        chatConnector.closeConnection();
    }

    public void chat(String toUserJID,String message)
    {
        chatConnector.chat(toUserJID,message);
    }

    public void groupChat( String[] infos)
    {
        chatConnector.groupChat(infos);
    }

    public void pubsub(String[] infos)
    {
        chatConnector.pubsub(infos);
    }

    public EjabbberdConnectorImpl getChatConnector() {
        return chatConnector;
    }

    public void setChatConnector(EjabbberdConnectorImpl chatConnector) {
        this.chatConnector = chatConnector;
    }
}
