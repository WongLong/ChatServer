package com.chatnet.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
	public static List<File> getClassFiles(String url){
		File file = new File(url);
		List<File> classFile = new ArrayList<>();
		
		scanner(classFile, file);
		return classFile;
	}
	
	private static void scanner(List<File> classFile, File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			
			for(File f : files) {
				scanner(classFile, f);
			}
		}else {
			if(file.getName().endsWith(".java")) {
				classFile.add(file);
			}
		}
	}
	
}
