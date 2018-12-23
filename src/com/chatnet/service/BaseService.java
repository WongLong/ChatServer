package com.chatnet.service;

import java.sql.SQLException;
import java.util.List;

public interface BaseService<T> {
	public boolean insert(T t) throws SQLException;

	public boolean delete(Class<?> clazz, String id);

	public boolean update(T t, String id);

	public T findById(Class<?> clazz, String id);

	public List<T> findAll(Class<?> clazz);
}
