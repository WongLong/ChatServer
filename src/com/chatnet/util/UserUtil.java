package com.chatnet.util;

public class UserUtil {
	public static String createUserID() {
		StringBuilder userID = new StringBuilder();
			
		if((int)(Math.random() * 5) == 0) {
			for(int i = 0; i < 9; i++) {
				if(i != 0) {
					userID.append((int)(Math.random() * 10));
				}else {
					userID.append((int)(Math.random() * 9 + 1));
				}
			}
		}else {
			for(int i = 0; i < 10; i++) {
				if(i != 0) {
					userID.append((int)(Math.random() * 10));
				}else {
					userID.append((int)(Math.random() * 2 + 1));
				}
			}
		}
		
		return userID.toString();
	}
}
