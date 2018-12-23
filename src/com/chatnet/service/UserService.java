package com.chatnet.service;

import java.util.List;

import com.chatnet.entity.User;

public interface UserService extends BaseService<User> {
	public List<User> getFriends(User user);

	public User addFriend(User user, String friendID);

	public boolean deleteFriend(User user, String friendID);
	
	public User login(User verifyUser);
}
