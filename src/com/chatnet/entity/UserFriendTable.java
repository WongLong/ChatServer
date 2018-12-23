package com.chatnet.entity;

import com.chatnet.annotation.Column;
import com.chatnet.annotation.Entity;
import com.chatnet.annotation.Foreign;

@Entity
public class UserFriendTable {
	@Foreign(referenceColumnName="userID", referenceTableName="user")
	@Column(nullable=false,length=10)
	private String userID;
	@Foreign(referenceColumnName="userID", referenceTableName="user")
	@Column(nullable=false,length=10)
	private String friendID;
	
	public UserFriendTable() {
		
	}
	
	public UserFriendTable(String userID, String friendID) {
		this.userID = userID;
		this.friendID = friendID;
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getFriendID() {
		return friendID;
	}
	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}
}
