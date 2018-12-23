package com.chatnet.bootstrap;

import com.chatnet.server.listener.AddFriendServerListener;
import com.chatnet.server.listener.ChatServerListener;
import com.chatnet.server.listener.DeleteFriendServerListener;
import com.chatnet.server.listener.LoginServerListener;
import com.chatnet.server.listener.RegisterServerListener;
import com.chatnet.util.DataBaseUtil;

public class ServerBootstrap {
	public static void main(String[] args) {
		System.out.println("hello");

		DataBaseUtil.loadTable();	// 创建实体表
		new Thread(new RegisterServerListener(12000)).start();	// 开启注册服务
		new Thread(new LoginServerListener(13000)).start();	// 开启登录服务
		new Thread(new AddFriendServerListener(14000)).start();	// 开启添加好友服务
		new Thread(new DeleteFriendServerListener(15000)).start();	// 开启删除好友服务
		new Thread(new ChatServerListener(16000)).start();	// 开启聊天服务
	}
}
