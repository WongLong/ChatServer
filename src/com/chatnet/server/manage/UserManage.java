package com.chatnet.server.manage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.chatnet.entity.Message;
import com.chatnet.entity.User;
import com.chatnet.entity.UserSocket;

public class UserManage {
	private Set<UserSocket> loginedUsers = new HashSet<>();
	private static UserManage manage;

	private UserManage() {

	}

	public static UserManage newInstance() {
		if (manage == null) {
			manage = new UserManage();
			return manage;
		} else {
			return manage;
		}
	}

	public void addClient(UserSocket socket) {
		loginedUsers.add(socket);
		
		new Thread(() -> {
			Socket s = socket.getSocket();
			try {
				Message obj = null;
				while ((obj = (Message) (new ObjectInputStream(s.getInputStream())).readObject()) != null) {
					
					while(!publish(obj)) {
						
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}).start();

	}

	public synchronized boolean publish(Message message) {
		User receiver = message.getReceiver();

		for (UserSocket userSocket : this.loginedUsers) {
			if (userSocket.getUser().getUserID().equals(receiver.getUserID())) {
				try {
					Socket socket = userSocket.getSocket();
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

					oos.writeObject(message);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		return false;
	}
		

}
