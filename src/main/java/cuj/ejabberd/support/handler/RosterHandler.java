package cuj.ejabberd.support.handler;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by cujamin on 2016/12/28.
 */
public class RosterHandler {
    public void entriesAddedHandler(Collection<String> addresses)
    {
//
//        //添加好友
//			else if(message.equals("2"))
//    {
//        sslc.addFriends(toJID,toJID.split("@")[0]);
//    }
//    //删除好友
//    else if(message.equals("3"))
//    {
//        int a = 0;
//        Collection<RosterEntry> rosters = Roster.getInstanceFor(sslc.getXMPPTCPConnection()).getEntries();
//        for(RosterEntry  rosterEntry : rosters)
//        {
//
//            if(rosterEntry.equals(sslc.getRoster().getEntry(toJID)))
//            {
//                a = 1;
//                sslc.delFriends(toJID);
//                break;
//            }
//        }
//        if(a == 0)
//        {
//            log.info("没有找到该好友："+toJID);
//        }
//    }
//    //显示所有好友的状态
//    else if(message.equals("4"))
//    {
//        Roster roster = Roster.getInstanceFor( sslc.getXMPPTCPConnection() );
//        Collection<RosterEntry> rosters = Roster.getInstanceFor(sslc.getXMPPTCPConnection()).getEntries();
//        for(RosterEntry  rosterEntry : rosters)
//        {
//            Presence presence = roster.getPresence(rosterEntry.getName());
//            log.info(presence);
//            log.info( rosterEntry.getName()+ " presence is " + presence.getType());
//        }
//    }
//    //显示所有好友状态以及可用id
//    else if(message.equals("5"))
//    {
//        HashMap map = sslc.getFriendMap();
//        for(Object key : map.keySet())
//        {
//            log.info(key + " 的状态是 " + map.get(key));
//        }
//        log.info("以下为可用id");
//        for(Object key : map.keySet())
//        {
//            if(map.get(key).equals(Presence.Type.unavailable))
//            {
//                log.info(key + " 的状态是 " + map.get(key));
//                //发给MGW后，即可break
//            }
//        }
//    }

    }
}
