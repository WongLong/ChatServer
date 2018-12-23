package com.chatnet.entity;

import java.io.Serializable;
import java.util.List;

import com.chatnet.annotation.Column;
import com.chatnet.annotation.Entity;
import com.chatnet.annotation.ID;

@Entity
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ID
	@Column(length = 10)
	private String userID;
	@Column(nullable = false, isUnique = true, length = 20)
	private String userName;
	@Column(length = 16, nullable=false)
	private String password;
	private List<User> friends;

	public User() {
		
	}
	
	public User(String userID) {
		this.userID = userID;
	}
	
	public User(String userID, String userName) {
		this.userID = userID;
		this.userName = userName;
	}

	public User(String userID, String userName, String password) {
		this.userID = userID;
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
}
