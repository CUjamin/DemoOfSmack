package cuj.ejabberd;

import cuj.ejabberd.controller.EjabberdController;
import cuj.ejabberd.controller.EjabberdControllerImpl;

/*
 *
 * project_name: DemoOfSmack
 * class_name: 
 * class_description: 
 * author: cujamin
 * create_time: 2016年7月28日
 * modifier: 
 * modify_time: 
 * modify_description: 
 * @version
 *
 */

public class Demo {
	private static EjabberdController ejabberdController;

	public static void main(String[]args)
	{
		ejabberdController = new EjabberdControllerImpl();
		ejabberdController.start();

		//连接并登录		cnctli:1,1234
		//登出				lo
		//聊天				ct:mgw02@cujamin-pc,hello
//		EjabberdLogin ejabberdLogin = new EjabberdLoginImpl();
//		ejabberdLogin.setServerInfo("cujamin-pc","127.0.0.1",5222);
//		ejabberdLogin.setUserInfo("1","1234");
//		ejabberdLogin.start();
//		ejabberdLogin.startChat("mgw02@cujamin-pc");
//		ejabberdLogin.createChatRoom("chat1@conference.cujamin-pc");
//		ejabberdLogin.startGroupChat("chat1@conference.cujamin-pc");
//		new PingThread().start();

	}
}

