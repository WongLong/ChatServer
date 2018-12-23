package com.chatnet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 廖卧龙
 *
 */
public class ConPool {
	private List<Con> freeCons = new ArrayList<>();
	private List<Con> buzyCons = new ArrayList<>();
	private int max = 10;
	private int min = 2;
	private int current = 0;
	private static ConPool instance;

	private ConPool() {
		while (current < min) {
			this.freeCons.add(this.createCon());
		}
	}

	public static ConPool getIntance() {
		if (instance == null)
			instance = new ConPool();
		return instance;
	}

	public Con getCon() {
		Con con = this.getFreeCon();
		if (con != null)
			return con;
		else
			return this.getNewCon();
	}

	private Con getFreeCon() {
		if (freeCons.size() > 0) {
			Con con = freeCons.remove(0);
			con.setState(Con.BUZY);
			this.buzyCons.add(con);
			return con;
		} else {
			return null;
		}
	}

	private Con getNewCon() {
		if (this.current < this.max) {
			Con con = this.createCon();
			con.setState(Con.BUZY);
			this.buzyCons.add(con);
			return con;
		} else {
			return null;
		}
	}

	private Con createCon() {
		try {
			Connection con = MySqlDAO.getConnection();
			Con myCon = new Con(con);
			this.current++;
			return myCon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setFree(Con con) {
		this.buzyCons.remove(con);
		con.setState(Con.FREE);
		this.freeCons.add(con);
	}
	
	public void setClose(Con con) {
		this.buzyCons.remove(con);
		this.freeCons.remove(con);
		con.setState(Con.CLOSED);
		
		try {
			con.getCon().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return "当前连接数:" + this.current + "空闲连接数:" + this.freeCons.size() + "繁忙连接数:" + this.buzyCons.size();
	}

	/**
	 * 
	 * @author 廖卧龙
	 *
	 */
	public class Con {
		public static final int FREE = 100;
		public static final int BUZY = 101;
		public static final int CLOSED = 102;
		private Connection con;
		private int state = FREE;

		public Con(Connection con) {
			this.con = con;
		}

		public Connection getCon() {
			return this.con;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}
	}

	/**
	 * 
	 * @author 廖卧龙
	 *
	 */
	static class MySqlDAO {
		public static Connection getConnection() throws Exception {
			String driverName = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/chat";
			String userName = "root";
			String password = "root";
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(url, userName, password);
			
			return con;
		}
	}
}
