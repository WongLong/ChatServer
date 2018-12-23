package com.chatnet.dao.imp;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chatnet.dao.BaseDao;
import com.chatnet.util.ColumnInfo;
import com.chatnet.util.ConPool;
import com.chatnet.util.ConPool.Con;
import com.chatnet.util.TableInfo;

public class BaseDaoIMP<T> implements BaseDao<T> {
	protected ConPool pool = ConPool.getIntance();

	@Override
	public boolean insert(T t) throws SQLException {
		Con con = pool.getCon();
		TableInfo table = new TableInfo().parse(t.getClass());

		if (table != null) {
			String tableName = table.getTableName();
			Set<ColumnInfo> columns = table.getColumns();

			StringBuilder columnNames = new StringBuilder().append("(");
			StringBuilder values = new StringBuilder().append("(");
			for (ColumnInfo column : columns) {
				columnNames.append(column.getColumnName());
				columnNames.append(",");

				try {
					Class<?> clazz = column.getType();
					Field f = column.getField();
					f.setAccessible(true);

					if (clazz.equals(java.util.Date.class)) {
						java.util.Date date = (java.util.Date) f.get(t);
						java.sql.Date sqlDate = new java.sql.Date(date.getTime());
						values.append("'");
						values.append(sqlDate);
						values.append("',");
					} else {
						values.append("'");
						values.append(f.get(t));
						values.append("',");
					}

				} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}

			}

			columnNames.setCharAt(columnNames.length() - 1, ')');
			values.setCharAt(values.length() - 1, ')');

			StringBuilder sql = new StringBuilder();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append(columnNames);
			sql.append(" values ");
			sql.append(values);

			Statement state = con.getCon().createStatement();
			state.executeUpdate(sql.toString());
			state.close();
			pool.setFree(con);
			System.out.println("已在表: " + tableName + " 中插入数据！");

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(Class<?> clazz, String id) {
		Con con = pool.getCon();
		TableInfo table = new TableInfo().parse(clazz);

		if (table != null) {
			String tableName = table.getTableName();
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM ");
			sql.append(tableName);
			sql.append(" WHERE ");

			if (!table.hasID()) {
				sql.append("primaryID ");
			} else {
				for (ColumnInfo column : table.getColumns()) {
					if (column.isID()) {
						sql.append(column.getColumnName());

						break;
					}
				}
			}

			sql.append(" = ");
			sql.append(id);

			try {
				Statement state = con.getCon().createStatement();
				state.executeUpdate(sql.toString());
				state.close();

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

				pool.setFree(con);
				System.out.println("已更新表: " + tableName);
			}
		}

		return false;
	}

	@Override
	public boolean update(T t, String id) {
		Con con = pool.getCon();
		TableInfo table = new TableInfo().parse(t.getClass());

		if (table != null) {
			String tableName = table.getTableName();
			Set<ColumnInfo> columns = table.getColumns();

			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ");
			sql.append(tableName);
			sql.append(" SET ");

			StringBuilder set = new StringBuilder();
			StringBuilder condition = new StringBuilder();
			if (!table.hasID()) {
				condition.append("primaryID = '");
				condition.append(id);
				condition.append("'");
			}
			try {
				for (ColumnInfo column : columns) {
					Field field = column.getField();
					field.setAccessible(true);

					if (column.isID()) {
						condition.append(column.getColumnName());
						condition.append(" = '");
						condition.append(id);
						condition.append("'");
					} else {
						if (column.getType().equals(java.util.Date.class)) {
							java.util.Date date = (java.util.Date) field.get(t);
							java.sql.Date sqlDate = new java.sql.Date(date.getTime());
							set.append(column.getColumnName());
							set.append(" =' ");
							set.append(sqlDate);
							set.append("',");
						} else {
							set.append(column.getColumnName());
							set.append(" = '");
							set.append(field.get(t));
							set.append("',");
						}
					}
				}

				set.setCharAt(set.length() - 1, ' ');
				sql.append(set);
				sql.append("WHERE ");
				sql.append(condition);

				Statement state = con.getCon().createStatement();
				state.executeUpdate(sql.toString());
				state.close();

				return true;
			} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} finally {

				pool.setFree(con);
				System.out.println("已更新表: " + tableName);
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findById(Class<?> clazz, String id) {
		Con con = pool.getCon();
		TableInfo table = new TableInfo().parse(clazz);

		if (table != null) {
			Set<ColumnInfo> columns = table.getColumns();

			StringBuilder condition = new StringBuilder();
			if (!table.hasID()) {
				condition.append("primaryID = ");
				condition.append(id);
			} else {
				for (ColumnInfo column : columns) {
					if (column.isID()) {
						condition.append(column.getColumnName());
						condition.append(" = ");
						condition.append(id);
						break;
					}
				}
			}

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ");
			sql.append(table.getTableName());
			sql.append(" WHERE ");
			sql.append(condition);

			try {
				Statement statement = con.getCon().createStatement();
				ResultSet rs = statement.executeQuery(sql.toString());

				if (rs.next()) {
					T t = (T) clazz.newInstance();

					for (ColumnInfo column : columns) {
						Field field = column.getField();
						field.setAccessible(true);
						field.set(t, rs.getObject(column.getColumnName()));
					}

					rs.close();
					statement.close();
					return t;
				} else {
					return null;
				}
			} catch (InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			} finally {

				pool.setFree(con);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(Class<?> clazz) {
		Con con = pool.getCon();
		TableInfo table = new TableInfo().parse(clazz);

		if (table != null) {
			List<T> objs = new ArrayList<>();
			Set<ColumnInfo> columns = table.getColumns();

			String tableName = table.getTableName();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM ");
			sql.append(tableName);

			try {
				Statement state = con.getCon().createStatement();
				ResultSet rs = state.executeQuery(sql.toString());

				while (rs.next()) {
					T t = (T) clazz.newInstance();

					for (ColumnInfo column : columns) {
						Field field = column.getField();
						field.setAccessible(true);

						if (column.getType().equals(java.util.Date.class)) {
							field.set(t, (java.util.Date) rs.getObject(column.getColumnName()));
						} else {
							field.set(t, rs.getObject(column.getColumnName()));
						}
					}

					objs.add(t);
				}

				rs.close();
				state.close();
				return objs;
			} catch (InstantiationException | IllegalAccessException | SQLException e) {
				e.printStackTrace();
			} finally {

				pool.setFree(con);
			}
		}

		return null;
	}

}
