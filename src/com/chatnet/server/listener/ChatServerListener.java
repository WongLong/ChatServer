package com.chatnet.server.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.chatnet.entity.User;
import com.chatnet.entity.UserSocket;
import com.chatnet.server.manage.UserManage;

public class ChatServerListener implements Runnable {
	private ServerSocket server;

	public ChatServerListener(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ������¼�ɹ����û����û���¼�ɹ��󣬱�����ܵ���socket���û���󶨣�����ӵ��û���������
	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = server.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				User loginedUser = (User)ois.readObject();
				
				UserManage manage = UserManage.newInstance();
				manage.addClient(new UserSocket(loginedUser, socket));
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
