package cuj.ejabberd.common.type.command;

/**
 * Created by cujamin on 2017/2/13.
 */
public class GroupChatCommandType {
    public static final String Join_Chat_Room = "j";//ok
    public static final String Leave_From_Chat_Room = "l";//ok

    public static final String Send_Message_To_Chat_Room = "c";//ok

    public static final String Get_Hosted_Rooms = "h";//?????// TODO: 2017/2/13  
    public static final String Get_Joined_Rooms = "jd";//????// ok
    public static final String Get_Members_In_Chat_Room = "m";//ok
    public static final String Get_Room_Info = "in";//ok

    public static final String Init_User_To_Chat_Room = "i";//ok
    public static final String Grant_Member_ship = "g";//okgrantMembership
    public static final String Ban_User_From_Chat_Room = "b";//ok

    public static final String Get_Admin = "ga";
    public static final String Set_Admin = "sa";
    public static final String Revoke_Admin = "ra";


}
