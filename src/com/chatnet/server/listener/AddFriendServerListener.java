package com.chatnet.server.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.chatnet.entity.User;
import com.chatnet.service.UserService;
import com.chatnet.service.imp.UserServiceIMP;

public class AddFriendServerListener implements Runnable {
	private ServerSocket serverSocket;

	public AddFriendServerListener(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				
				String data = (String)ois.readObject();
				String[] datas = data.split(",");
				String userID = datas[0];
				String friendID = datas[1];
	
				UserService service = new UserServiceIMP();
				User friend = service.addFriend(new User(userID), friendID);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(friend);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
