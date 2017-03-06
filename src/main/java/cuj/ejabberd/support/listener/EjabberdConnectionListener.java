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
    private Timer tExit;
    private String username;
    private String password;
    private int logintime = 5000;

    @Override
    public void connectionClosed() {
        log.info("Ejabberd Connection Listener : disconnect !");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
    	log.info("EjabberdConnectionListener"+"连接关闭异常");

        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {

        	EjabbberdConnectorImpl.getInstance().getXMPPTCPConnection().disconnect();

            tExit = new Timer();
            tExit.schedule(new timetask(), logintime);
        }
    }

    class timetask extends TimerTask {
        @Override
        public void run() {
            username = EjabberdConfig.getInstance().getUserName();
            password = EjabberdConfig.getInstance().getPassWord();
            if (username != null && password != null) {
                EjabberdLoginImpl.chatConnector.connect();
            	log.info("ConnectionListener"+"尝试登陆");
                // 连接服务器
                if (EjabberdLoginImpl.chatConnector.logIn()) {
                	log.info("ConnectionListener"+"登陆成功");
                } else {
                	log.info("ConnectionListener"+"重新登录");
                    tExit.schedule(new timetask(), logintime);
                }
            }
        }
    }

	@Override
	public void connected(XMPPConnection connection) {
		// TODO Auto-generated method stub
		/*
		 *cujamin
		 *2016年9月22日
		 */

	}

	@Override
	public void authenticated(XMPPConnection connection, boolean resumed) {
		// TODO Auto-generated method stub
		/*
		 *cujamin
		 *2016年9月22日
		 */

	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		/*
		 *cujamin
		 *2016年9月22日
		 */

	}

	@Override
	public void reconnectingIn(int seconds) {
		// TODO Auto-generated method stub
		/*
		 *cujamin
		 *2016年9月22日
		 */

	}

	@Override
	public void reconnectionFailed(Exception e) {
		// TODO Auto-generated method stub
		/*
		 *cujamin
		 *2016年9月22日
		 */

	}
}
