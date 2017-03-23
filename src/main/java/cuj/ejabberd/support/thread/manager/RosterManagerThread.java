package cuj.ejabberd.support.thread.manager;

import cuj.ejabberd.support.handler.RosterHandler;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by cujamin on 2017/2/10.
 */
public class RosterManagerThread extends Thread{
    private static final Logger log = Logger.getLogger(RosterManagerThread.class);
    private Roster roster = null;//花名册
    private HashMap friendMap = new HashMap();
    private RosterHandler rosterHandler;
    public RosterManagerThread(XMPPConnection connection , RosterHandler rosterHandler)
    {
        this.rosterHandler = rosterHandler;
        if(null==connection||connection.isConnected()==false)
        {
            return;
        }
        roster = Roster.getInstanceFor(connection);
    }
    public void run()
    {
        log.info(" [ Roster manager Thread init... ... ] ");
        roster.addRosterListener(new RosterListener(){
            int a=0;//登录时和有加好友请求时，都会调用entriesAdded，用a在区分”登录“和”有加好友请求“两种情况
            @Override
            public void entriesAdded(Collection<String> addresses) {
                if(a==0)
                {
                    for(String friend : addresses)
                    {
                        log.info("好友 : "+friend);
                    }
                    a=1;
                }
                else
                {
                    String name = (String) addresses.toString().subSequence(1, addresses.toString().length()-1);
                    log.info("添加好友 ："+name);
                    addFriends(name,name);
                }
            }
            @Override
            public void entriesUpdated(Collection<String> addresses) {
                log.info("更新好友 ： "+addresses);
            }
            @Override
            public void entriesDeleted(Collection<String> addresses) {
                log.info("删除好友： "+addresses);
            }
            @Override
            public void presenceChanged(Presence presence) {

                log.info(presence);
                log.info(presence.getTo()+"  的好友:  "+presence.getFrom() +"   状态为 ：  "+ presence.getType());
                getFriendMap().put(presence.getFrom().split("@")[0], presence.getType());
                if(presence.getType().equals(Presence.Type.unavailable))
                {
                    //TODO
                    log.info("好友"+presence.getFrom()+"掉线");
                }
            }
        });
        log.info(" [ Roster manager Thread init OK ] ");
    }
    //添加好友
    public void addFriends(String friendjid,String name)
    {
        try {
            roster.createEntry(friendjid, name, new String[]{"Friends"});
        } catch (SmackException.NotLoggedInException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * @param friendjid
     */
    public void delFriends(String friendjid)
    {
        try {
            roster.removeEntry(roster.getEntry(friendjid));
        } catch (SmackException.NotLoggedInException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public HashMap getFriendMap() {
        return friendMap;
    }
}
