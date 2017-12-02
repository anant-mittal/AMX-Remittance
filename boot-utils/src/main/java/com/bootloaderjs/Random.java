package com.bootloaderjs;

public class Random {

	public static int getInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}
	
}
