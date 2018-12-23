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

		DataBaseUtil.loadTable();	// ����ʵ���
		new Thread(new RegisterServerListener(12000)).start();	// ����ע�����
		new Thread(new LoginServerListener(13000)).start();	// ������¼����
		new Thread(new AddFriendServerListener(14000)).start();	// ������Ӻ��ѷ���
		new Thread(new DeleteFriendServerListener(15000)).start();	// ����ɾ�����ѷ���
		new Thread(new ChatServerListener(16000)).start();	// �����������
	}
}
