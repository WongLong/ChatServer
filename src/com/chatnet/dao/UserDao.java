package com.chatnet.dao;

import java.util.List;

import com.chatnet.entity.User;

public interface UserDao extends BaseDao<User>{
	public List<User> getFriends(User user);
	
	public User addFriend(User user, String friendID);
	
	public boolean deleteFriend(User user, String friendID);
	
	public User login(User verifyUser);
	
	public boolean isFriend(User user, String friend);
}
