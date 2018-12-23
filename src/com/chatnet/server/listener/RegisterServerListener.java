package com.chatnet.server.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import com.chatnet.entity.User;
import com.chatnet.service.UserService;
import com.chatnet.service.imp.UserServiceIMP;
import com.chatnet.util.UserUtil;

public class RegisterServerListener implements Runnable{
	private ServerSocket serverSocket;
	
	public RegisterServerListener(int port) {
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
				
				User user = (User)ois.readObject();
				String userID = UserUtil.createUserID();
				
				UserService service = new UserServiceIMP();
				while(service.findById(User.class, userID) != null) {
					userID = UserUtil.createUserID();
				}
				
				user.setUserID(userID);
				service.insert(user);
				
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(user);
			} catch (IOException | ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
