package com.chatnet.server.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.chatnet.entity.User;
import com.chatnet.service.UserService;
import com.chatnet.service.imp.UserServiceIMP;

public class DeleteFriendServerListener implements Runnable {
	private ServerSocket serverSocket;
	
	public DeleteFriendServerListener(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				
				String data = (String)ois.readObject();
				String[] datas = data.split(",");
				String userID = datas[0];
				String friendID = datas[1];
	
				UserService service = new UserServiceIMP();
				service.deleteFriend(new User(userID), friendID);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
