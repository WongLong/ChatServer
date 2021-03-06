package com.chatnet.server.listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.chatnet.entity.User;
import com.chatnet.service.UserService;
import com.chatnet.service.imp.UserServiceIMP;

public class LoginServerListener implements Runnable {
	private ServerSocket serverSocket;

	public LoginServerListener(int port) {
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

				User user = (User) ois.readObject();
				UserService service = new UserServiceIMP();
				user = service.login(user);

				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(user);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
