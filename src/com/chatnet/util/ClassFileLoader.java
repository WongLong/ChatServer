package com.chatnet.util;

import java.io.File;

public class ClassFileLoader {
	public static Class<?> loadClass(File file) throws ClassNotFoundException{
		String className = file.toString();
		className = className.replace(".java", "");
		int index = className.lastIndexOf("src\\");
		className = className.substring(index + 4);
		className = className.replace("\\", ".");
		
		return Class.forName(className);
	}
}
