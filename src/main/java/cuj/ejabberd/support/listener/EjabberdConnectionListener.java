package cuj.ejabberd.support.listener;
/*
*
* project_name: DemoOfSmack
* class_name:
* class_description:
* author: cujamin
* create_time: 2016年9月22日
* modifier:
* modify_time:
* modify_description:
* @version
*
 */
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import cuj.ejabberd.Demo;
import cuj.ejabberd.config.EjabberdConfig;
import cuj.ejabberd.connector.EjabbberdConnectorImpl;
import cuj.ejabberd.connector.EjabberdLoginImpl;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;


/**
 * 连接监听类
 *
 * @author Administrator
 *
 */
public class EjabberdConnectionListener implements ConnectionListener {
    static final private Logger log = Logger.getLogger(EjabberdConnectionListener.class.getName());
    private String username;
    private String password;

    @Override
    public void connectionClosed() {
        log.info("Ejabberd Connection Listener : 连接_____________________________________________断开 !");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        log.info("EjabberdConnectionListener"+"连接________________________________________________关闭异常");

        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {
            EjabbberdConnectorImpl.getInstance().getXMPPTCPConnection().disconnect();
            reconnectingIn(20000);
        }
    }

    @Override
    public void connected(XMPPConnection connection) {
        log.info("connected" + "登陆___________________________________________________________成功");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        log.info("authenticated ： " + resumed+"__________________________________________");
    }

    @Override
    public void reconnectionSuccessful() {
        log.info("reconnectionSuccessful" + "登陆____________________________________________成功");
    }

    @Override
    public void reconnectingIn(int seconds) {
        username = EjabberdConfig.getInstance().getUserName();
        password = EjabberdConfig.getInstance().getPassWord();
        while(seconds<900000)
        {
            log.info("reconnectingIn" + "尝试+++++++++++++++++++++++++++++++++++++++++++++++++++登陆 : "+seconds/1000+" s");
            try
            {
                Thread.sleep(Long.valueOf(seconds));
            }catch (InterruptedException e)
            {
                log.info("wait: " + e);
            }
            if (username != null && password != null) {
                EjabberdLoginImpl.chatConnector.connect();
                // 连接服务器
                if (EjabberdLoginImpl.chatConnector.logIn()) {
                    log.info("reconnectingIn" + "登陆成功");
                    break;
                } else {
                    log.info("reconnectingIn" + "重新失败");
                }
            }
            seconds+=10000;
        }
    }

    @Override
    public void reconnectionFailed(Exception e) {
        log.info("reconnectionFailed" + "重新_______________________失败");
    }
}
