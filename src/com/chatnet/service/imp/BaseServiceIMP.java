package com.chatnet.service.imp;

import java.sql.SQLException;
import java.util.List;

import com.chatnet.dao.BaseDao;
import com.chatnet.dao.imp.BaseDaoIMP;
import com.chatnet.service.BaseService;

public class BaseServiceIMP<T> implements BaseService<T>{
	BaseDao<T> dao = new BaseDaoIMP<T>();

	@Override
	public boolean insert(T t) throws SQLException {
		return dao.insert(t);
	}

	@Override
	public boolean delete(Class<?> clazz, String id) {
		return dao.delete(clazz, id);
	}

	@Override
	public boolean update(T t, String id) {
		return dao.update(t, id);
	}

	@Override
	public T findById(Class<?> clazz, String id) {
		return dao.findById(clazz, id);
	}

	@Override
	public List<T> findAll(Class<?> clazz) {
		return dao.findAll(clazz);
	}
	

}
