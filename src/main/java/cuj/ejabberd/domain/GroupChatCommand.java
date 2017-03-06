package cuj.ejabberd.domain;

import cuj.ejabberd.common.type.command.GroupChatCommandType;

/**
 * Created by cujamin on 2017/2/13.
 */
public class GroupChatCommand {
    public String command = GroupChatCommandType.Send_Message_To_Chat_Room;
    public String groupRoomJID = "chat1@conference.cujamin-pc";
    public String toUserJID = "mgw02@cujamin-pc/spark";
    public String message ="hello welcom chat room";//reason
    public GroupChatCommand(String[] infos)
    {
        parameter(infos);
    }
    private void parameter(String[] infos)
    {
        if(null==infos)return;
        int num = infos.length;
        switch (num)
        {
            case 1:
                command = infos[0];
                message = infos[0];
                break;
            case 2:
                command = infos[0];
                groupRoomJID = infos[1];
                message = infos[0];
                break;
            case 3:
                command = infos[0];
                groupRoomJID = infos[1];
                toUserJID = infos[2];
                message = infos[0];
                break;
            case 4:
                command = infos[0];
                groupRoomJID = infos[1];
                toUserJID = infos[2];
                message = infos[3];
                break;
        }
    }
}
