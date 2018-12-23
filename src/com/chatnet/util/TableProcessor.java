package com.chatnet.util;

import java.io.File;
import java.util.List;

public class TableProcessor {
	public static void process(String url) {
		url += "\\src\\com\\chatnet\\entity";
		List<File> classFiles = Scanner.getClassFiles(url);

		for (File file : classFiles) {
			try {
				Class<?> clazz = ClassFileLoader.loadClass(file);
				TableInfo table = new TableInfo().parse(clazz);
				if (table != null) {
					DataBaseUtil.createDataBaseTable(table);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}
}
