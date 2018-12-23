package com.chatnet.service.imp;

import java.util.List;

import com.chatnet.dao.imp.UserDaoIMP;
import com.chatnet.entity.User;
import com.chatnet.service.UserService;

public class UserServiceIMP extends BaseServiceIMP<User> implements UserService{
	
	public UserServiceIMP() {
		this.dao = new UserDaoIMP();
	}
	
	@Override
	public List<User> getFriends(User user) {
		return ((UserDaoIMP)this.dao).getFriends(user);
	}

	@Override
	public User addFriend(User user, String friendID) {		
		return ((UserDaoIMP)this.dao).addFriend(user, friendID);
	}

	@Override
	public boolean deleteFriend(User user, String friendID) {		
		return ((UserDaoIMP)this.dao).deleteFriend(user, friendID);
	}

	@Override
	public User login(User verifyUser) {
		return ((UserDaoIMP)this.dao).login(verifyUser);
	}

}
