package com.chatnet.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.chatnet.annotation.Column;
import com.chatnet.annotation.Foreign;
import com.chatnet.annotation.ID;

public class ColumnInfo {
	private Field field;
	private String columnName;
	private Class<?> type;
	private boolean isID = false;
	private boolean nullable = true;
	private boolean isUnique = false;
	private int length = 32;
	private boolean needPersist = false;
	private boolean needForeign = false;
	private String referenceTableName = "";
	private String referenceColumnName = "";

	public ColumnInfo parse(Field field) {
		this.field = field;
		this.columnName = field.getName();
		this.type = field.getType();
		Annotation[] annotations = field.getAnnotations();

		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(Column.class)) {
				this.needPersist = true;

				Column column = (Column) annotation;
				if (!column.columnName().equals("")) {
					this.columnName = column.columnName();
				}

				this.nullable = column.nullable();
				this.isUnique = column.isUnique();

				if (column.length() != -1) {
					this.length = column.length();
				}
			} else if (annotation.annotationType().equals(ID.class)) {
				ID id = (ID) annotation;
				this.isID = true;
				if (!id.value().equals("")) {
					this.columnName = id.value();
				}
			} else if (annotation.annotationType().equals(Foreign.class)) {
				this.needForeign = true;

				Foreign f = (Foreign) annotation;
				if (!f.value().equals("")) {
					this.columnName = f.value();
				}

				this.referenceTableName = f.referenceTableName();
				this.referenceColumnName = f.referenceColumnName();
			}
		}

		if (this.needPersist) {
			return this;
		} else {
			return null;
		}
	}

	public String toString() {
		StringBuilder sql = new StringBuilder(this.columnName);

		if (this.type.equals(String.class)) {
			sql.append(Symbol.BLANK + "VARCHAR(" + this.length + ")");
		} else if (this.type.equals(int.class)) {
			sql.append(Symbol.BLANK + "INT");
		} else if (this.type.equals(java.util.Date.class)) {
			sql.append(Symbol.BLANK + "DATE");
		}

		if (this.isID) {
			sql.append(Symbol.BLANK + "PRIMARY KEY");
		} else {
			if (!this.nullable) {
				sql.append(Symbol.BLANK + "NOT NULL");
			}
			if (this.isUnique) {
				sql.append(Symbol.BLANK + "UNIQUE ");
			}
			if(this.needForeign) {
				sql.append(",");
				sql.append(Symbol.LINE);
				sql.append(Symbol.BLANK + "FOREIGN KEY (");
				sql.append(this.columnName);
				sql.append(") REFERENCES ");
				sql.append(this.referenceTableName);
				sql.append("(");
				sql.append(this.referenceColumnName);
				sql.append(")");
			}
		}
		sql.append(",");

		return sql.toString();
	}

	public Class<?> getType() {
		return type;
	}

	public boolean isID() {
		return isID;
	}

	public String getColumnName() {
		return columnName;
	}

	public Field getField() {
		return field;
	}
}
