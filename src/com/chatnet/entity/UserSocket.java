package com.chatnet.entity;

import java.net.Socket;

public class UserSocket {
	private User user;
	private Socket socket;
	
	public UserSocket() {
		
	}
	
	public UserSocket(User user) {
		this.user = user;
	}
	
	public UserSocket(User user, Socket socket) {
		this.user = user;
		this.socket = socket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
