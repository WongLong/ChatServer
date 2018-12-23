package com.chatnet.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.chatnet.util.ConPool.Con;


public class DataBaseUtil {
	private static ConPool pool = ConPool.getIntance();
	private static Con con = pool.getCon();
	
	public static void loadTable() {
		TableProcessor.process(System.getProperty("user.dir"));
	}
	
	public static void createDataBaseTable(TableInfo table) {
		if (!isPersisted(table)) {
			try {
				Statement state = con.getCon().createStatement();
				state.executeUpdate(table.toString());
				System.err.println("已创建实体表格：" + table.getTableName());
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isPersisted(TableInfo table) {
		// 判断是否存在该表
		try {
			DatabaseMetaData meta = con.getCon().getMetaData();

			ResultSet rs = meta.getTables(null, "mydb", table.getTableName(), null);
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
