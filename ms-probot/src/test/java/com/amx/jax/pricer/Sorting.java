package com.amx.jax.pricer;

import java.util.Arrays;

public class Sorting {

	// vector = [5,2,4,6,1,3,8,0,4,5]

	public static int[] bubbleSort(int[] vector) {

		if (vector == null)
			return null;
		else if (vector.length <= 1)
			return vector;

		int cnt = 0;

		for (int i = 1; i < vector.length; i++) {
			for (int j = i - 1; j >= 0; j--) {
				if (vector[j + 1] < vector[j]) {
					int swap = vector[j + 1];
					vector[j + 1] = vector[j];
					vector[j] = swap;
				} else {
					break;
				}
				cnt++;
			}

		}

		System.out.println("Length ==>" + vector.length + " Count ==>" + cnt);

		return vector;
	}

	public static void main(String[] args) {
		//int[] vector = { 5, 2, 4, 6, 1, 3, 8, 0, 4, 5, -1, -10, 20, 100, 50, -20 };
		int[] vector = { 100, 80, 50, 20, 10, 8, 0, -10, -20, -25, -30, -50, -100 };
		
		// int[] vector = null;
		// int[] vector = {};

		System.out.println(Arrays.toString(vector));

		System.out.println(Arrays.toString(bubbleSort(vector)));

	}

}
