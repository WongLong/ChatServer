package com.chatnet.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.chatnet.dao.UserDao;
import com.chatnet.entity.User;
import com.chatnet.entity.UserFriendTable;
import com.chatnet.service.BaseService;
import com.chatnet.service.imp.BaseServiceIMP;
import com.chatnet.util.ConPool.Con;

public class UserDaoIMP extends BaseDaoIMP<User> implements UserDao {
	@Override
	public List<User> getFriends(User user) {
		StringBuilder sql = new StringBuilder();
		List<User> friends = new ArrayList<>();

		sql.append("SELECT friendID FROM UserFriendTable WHERE userID = '");
		sql.append(user.getUserID());
		sql.append("'");

		try {
			Con con = this.pool.getCon();
			Statement state = con.getCon().createStatement();
			ResultSet rs = state.executeQuery(sql.toString());

			while (rs.next()) {
				String friendID = rs.getString("friendID");
				sql = new StringBuilder();
				sql.append("SELECT * FROM User WHERE userID = '");
				sql.append(friendID);
				sql.append("'");

				User friend = new User();
				Statement newState = con.getCon().createStatement();
				ResultSet friendSet = newState.executeQuery(sql.toString());
				if (friendSet.next()) {
					friend.setUserName(friendSet.getString("userName"));
					friend.setUserID(friendSet.getString("userID"));

					friends.add(friend);
				}
				
				friendSet.close();
				newState.close();
			}

			rs.close();
			state.close();
			this.pool.setFree(con);

		
			return friends;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public User addFriend(User user, String friendID) {
		User friend = super.findById(User.class, friendID);
		if(friend != null) {
			BaseService<UserFriendTable> service = new BaseServiceIMP<>(); 
			try {
				if(this.isFriend(user, friendID)) {
					return null;
				}
				
				service.insert(new UserFriendTable(user.getUserID(), friendID));
				return friend;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean deleteFriend(User user, String friendID) {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM UserFriendTable WHERE userID = '");
		sql.append(user.getUserID());
		sql.append("' AND friendID = '");
		sql.append(friendID);
		sql.append("'");
		
		try {
			Con con = this.pool.getCon();
			Statement state = con.getCon().createStatement();
			state.executeUpdate(sql.toString());
			
			state.close();
			this.pool.setFree(con);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return false;
	}

	@Override
	public User login(User verifyUser) {
		User findUser = this.findById(User.class, verifyUser.getUserID());
		
		if(findUser == null || !findUser.getPassword().equals(verifyUser.getPassword())) {
			return null;
		}
		
		findUser.setFriends(this.getFriends(findUser));;
		
		return findUser;
	}
	
	@Override
	public boolean isFriend(User user, String friend) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * FROM UserFriendTable WHERE userID = '");
		sql.append(user.getUserID());
		sql.append("' AND friendID = '");
		sql.append(friend);
		sql.append("'");
		
		try {
			Con con = this.pool.getCon();
			Statement state = con.getCon().createStatement();
			ResultSet rs = state.executeQuery(sql.toString());
			
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
