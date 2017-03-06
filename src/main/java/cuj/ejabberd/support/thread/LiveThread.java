package cuj.ejabberd.support.thread;

/**
 * Created by cujamin on 2017/2/7.
 */

import cuj.ejabberd.connector.EjabbberdConnectorImpl;
import org.apache.log4j.Logger;
import org.jivesoftware.smackx.ping.PingManager;

/**
 * 保持在线
 */
public class LiveThread extends Thread{
    static final Logger log = Logger.getLogger(LiveThread.class);
    public void run()
    {
        while(true);
    }
}
