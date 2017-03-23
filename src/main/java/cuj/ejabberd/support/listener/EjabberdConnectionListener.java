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

import cuj.ejabberd.connector.EjabberdLoginImpl;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;


/**
 * 连接监听类
 *
 * @author Administrator
 *
 */
public class EjabberdConnectionListener implements ConnectionListener {
    static final private Logger log = Logger.getLogger(EjabberdConnectionListener.class);

    @Override
    public void connectionClosed() {
        log.info("connectionClosed : 主动断开连接1_________________________________________________________ !");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        log.info("connectionClosedOnError ： 连接异常关闭2___________________________________________________________");
        log.info(""+e);
//        EjabberdLoginImpl.chatConnector.closeConnection();
        reconnectingIn(10000);
    }

    @Override
    public void connected(XMPPConnection connection) {
        log.info("connected ：连接成功3___________________________________________________________");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        log.info("authenticated ：认证成功4___________________________________________________________");
//        log.info("authenticated ： " + resumed+"__________________________________________");
    }



    @Override
    public void reconnectingIn(int seconds) {
        log.info("reconnectingIn  ：重——新——连——接5__________________________________________________");
        while(true)
        {
            try
            {
                Thread.sleep(Long.valueOf(seconds));
            }catch (InterruptedException e)
            {
                log.info("wait: " + e);
            }
            if (EjabberdLoginImpl.chatConnector.reConnectAndLogin()) {
                break;
            } else {
//                reconnectionFailed(null);
            }

        }
    }
    @Override
    public void reconnectionSuccessful() {
        log.info("reconnectionSuccessful  ：重连成功_6B__________________________________________________");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        //TODO
        log.info("reconnectionFailed  ：重连失败7__________________________________________________");
        log.info(""+e);
    }
}
