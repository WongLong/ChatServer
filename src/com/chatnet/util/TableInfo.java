package com.chatnet.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.chatnet.annotation.Entity;

public class TableInfo {
	private String tableName;
	private Class<?> clazz;
	private boolean needPersist = false;
	private boolean hasID = false;
	private Set<ColumnInfo> columns = new HashSet<>();
	
	public TableInfo parse(Class<?> clazz) {
		this.clazz = clazz;
		this.tableName = this.clazz.getSimpleName();
		
//		// 判断是否存在该表
//		try {
//			DatabaseMetaData meta = ConPool.getIntance().getCon().getCon().getMetaData();
//			
//			ResultSet rs = meta.getTables(null, "mydb", this.tableName, null);
//			if(rs.next()) {
//				return null;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		// 扫描注解
		Annotation[] annotations = this.clazz.getAnnotations();
		
		for(Annotation annotation : annotations) {
			if(annotation.annotationType().equals(Entity.class)) {
				this.needPersist = true;
				Entity entity = (Entity) annotation;
				
				if(!entity.entityName().equals("")) {
					this.tableName = entity.entityName();
				}
				
				break;
			}
		}
		
		if(this.needPersist) {
			Field[] fields = this.clazz.getDeclaredFields();
			for(Field field : fields) {
				ColumnInfo column = new ColumnInfo();
				column = column.parse(field);
				if(column != null) {
					if(column.isID()) {
						this.hasID = true;
					}
					
					this.columns.add(column);
				}
			}
			
			return this;
		}else {
			return null;
		}
	}
	
	public String toString() {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(this.tableName + Symbol.BLANK);
		sql.append("(");
		for(ColumnInfo column : columns) {
			sql.append(Symbol.LINE);
			sql.append(Symbol.TAB);
			sql.append(column.toString());
		}
		
		if(!this.hasID) {
			sql.append(Symbol.LINE);
			sql.append(Symbol.TAB);
			sql.append("primaryID INT PRIMARY KEY AUTO_INCREMENT,");
		}
		
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(Symbol.LINE);
		sql.append(")");
		sql.append(Symbol.LINE);
		
		return sql.toString();
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public Set<ColumnInfo> getColumns(){
		return columns;
	}
	
	public boolean hasID() {
		return hasID;
	}
}
