package cuj.ejabberd.controller;

import cuj.ejabberd.common.type.command.CommandType;
import cuj.ejabberd.connector.EjabberdLogin;
import cuj.ejabberd.connector.EjabberdLoginImpl;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Created by cujamin on 2017/2/9.
 */
public class EjabberdControllerImpl implements EjabberdController
{
    private static final Logger log = Logger.getLogger(EjabberdControllerImpl.class);
//    private static final String defult_servername = "egoo2.com";
//    private static final String defult_host = "192.168.1.179";
    private static final String defult_servername = "cujamin-pc";
    private static final String defult_host = "127.0.0.1";
    private static final int defult_port = 5222;
    private static final String defult_username = "1000";
    private static final String defult_password = "1234";
    private static final String defult_toJID = "mgw02@cujamin-pc";
    private static final String defult_groupRoomJID = "chat1@conference.cujamin-pc";

    private String servername = null;
    private String host = null;
    private int port = 0;
    private String username = null;
    private String password = null;
    private String toJID = null;
    private String groupRoomJID = null;

    private boolean status = false;
    private EjabberdLogin ejabberdLogin =null ;
    public void start()
    {
        EjabberdLogin ejabberdLogin = new EjabberdLoginImpl();
        Scanner input = new Scanner(System.in);
        while (true) {
            String message = input.nextLine();
            String command = null;
            String[] rests = null;
            try
            {
                command = message.split(":")[0].trim();
                rests = message.split(":")[1].trim().split(",");
            }catch (Exception e)
            {

            }
            switch (command)
            {
                case CommandType.Set_Server_Info:
                    setServerInfo(rests);
                    break;
                case CommandType.Connect_To_Server_And_Log_In:
                    logIn(rests);
                    break;
                case CommandType.Log_Out:
                    logOut();
                    break;
                case CommandType.Connect_To_Server_And_Regiater:
                    break;
                case CommandType.Chat:
                    chat(rests);
                    break;
//                case CommandType.Chat_Close:
//                    break;
                case CommandType.Group_Chat:
                    groupChat(rests);
                    break;
//                case CommandType.Group_Chat_Chose:
//                    break;
                case CommandType.Roster_List:
                    break;
                case CommandType.PubSub:
                    pubsub(rests);
                    break;
            }
            System.out.println("once");
        }

    }
    private void setServerInfo(String[] serverinfos)
    {
        try
        {
            servername = serverinfos[0].trim();
            host = serverinfos[1].trim();
            port = Integer.parseInt(serverinfos[2].trim());
        }catch (Exception e)
        {

        }
        if(null==servername)
        {
            servername = defult_servername;
            log.info("servername is null , now use the defult servername!");
        }
        if(null==host)
        {
            host = defult_host;
            log.info("host is null , now use the defult host!");
        }
        if(0==port)
        {
            port=defult_port;
            log.info("port is null , now use the defult port!");
        }

    }

    private void logIn(String[] userinfos)
    {
        setServerInfo(null);
        try
        {
            username = userinfos[0].trim();
            password = userinfos[1].trim();
        }catch (Exception e)
        {

        }
        if(null==ejabberdLogin)
        {
            ejabberdLogin = new EjabberdLoginImpl();
        }
        if(null==username)
        {
            username=defult_username;
            log.info("username is null , now use the defult username!");
        }
        if(null==password)
        {
            password=defult_password;
            log.info("password is null , now use the defult password!");
        }


        log.info("log in :\n" +
                "Server Name : " + servername+ "\n" +
                "host : " + host+ "\n" +
                "port : " + port+ "\n" +
                "username : " + username+ "\n" +
                "password : " + password+ "\n"
        );

        ejabberdLogin.setServerInfo(servername,host,port);
        ejabberdLogin.setUserInfo(username,password);
        ejabberdLogin.logIn();
    }
    private void logOut()
    {
        ejabberdLogin.logOut();
        ejabberdLogin=null;
    }
    private void chat(String[] infos)
    {
        String message = null;
        String chat_0 = null;
        String chat_1 = null;
        try
        {
            chat_0 = infos[0];
            chat_1 = infos[1];
        }catch (Exception e)
        {

        }
        if(null==chat_0)
        {
            toJID = defult_toJID;
            message = "hello world !";
        }
        else if(null==chat_1)
        {
            toJID = defult_toJID;
            message = chat_0;
        }
        else
        {
            toJID=chat_0;
            message = chat_1;
        }
        log.info("start to chat :\n" +
                "tojid : " + toJID+ "\n" +
                "message : " + message+ "\n"
        );
        ejabberdLogin.chat(toJID,message);
    }
    private void chatClose(String[] infos)
    {

    }
    private void groupChat(String[] infos)
    {
        ejabberdLogin.groupChat(infos);
    }
    private void groupChatClose(String[] infos)
    {

    }
    public void stop()
    {

    }
    public void pubsub(String[] infos)
    {
        ejabberdLogin.pubsub(infos);
    }
}
