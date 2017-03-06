package cuj.ejabberd.ui.windows;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cujamin on 2017/2/15.
 */
public class GroupChatWindow extends Thread{
    private JFrame jFrame;
    private JPanel recvPanel;
    private JPanel rosterPanel;
    private JPanel scanfPanel;
    private String roomJID;
    private JTextArea rosterlist;
    private JTextArea recvText;
    private JTextArea scanfText;

    public GroupChatWindow(String roomJID)
    {
        this.roomJID = roomJID;
    }
    public void run()
    {
        init();
    }
    private void init()
    {
        jFrame = new JFrame(roomJID);
        jFrame.setSize(600,450);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        recvPanel = new JPanel();
        rosterPanel = new JPanel();
        scanfPanel = new JPanel();

        recvPanel.setBackground(Color.lightGray);
        recvPanel.setBounds(10,10,300,200);
        recvText = new JTextArea("聊天",10,20);
        recvPanel.add(recvText,new BorderLayout(0,0));


        rosterPanel.setBackground(Color.blue);
        rosterPanel.setBounds(320,10,300,200);
        rosterlist = new JTextArea("群成员",10,20);
        rosterPanel.add(rosterlist,new BorderLayout(0,0));

        scanfPanel.setBackground(Color.yellow);
        scanfPanel.setBounds(100,600,100,100);
        scanfText = new JTextArea("输入",10,20);
        scanfPanel.add(scanfText,BorderLayout.SOUTH);


        jFrame.add(recvPanel);
        jFrame.add(rosterPanel);
        jFrame.add(scanfPanel);

        jFrame.setVisible(true);
    }

}
