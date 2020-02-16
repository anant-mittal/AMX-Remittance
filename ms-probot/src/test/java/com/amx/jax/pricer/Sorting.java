package com.amx.jax.pricer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Sorting {

	// vector = [5,2,4,6,1,3,8,0,4,5]

	public static void bubbleSort(int[] vector) {

		if (vector == null)
			return;
		else if (vector.length <= 1)
			return;

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

		return;
	}

	public static void merge(int[] vector, int from, int mid, int to) {
		if (from >= to)
			return;

		int[] left = Arrays.copyOfRange(vector, from, mid + 1);
		int[] right = Arrays.copyOfRange(vector, mid + 1, to + 1);

		int l = 0;
		int r = 0;
		int i = from;

		for (; i <= to && l < left.length && r < right.length; i++) {
			if (left[l] <= right[r]) {
				vector[i] = left[l];
				l++;
			} else {
				vector[i] = right[r];
				r++;
			}
		}

		if (l == left.length) {
			for (; i <= to; i++) {
				vector[i] = right[r];
				r++;
			}
		} else {
			for (; i <= to; i++) {
				vector[i] = left[l];
				l++;
			}
		}
	}

	public static void mergeSort(int[] vector, int from, int to) {

		if (vector == null || from >= to) {
			return;
		}

		int mid = (to + from) / 2;

		mergeSort(vector, from, mid);
		mergeSort(vector, mid + 1, to);

		merge(vector, from, mid, to);

	}

	public static void main(String[] args) {

		int min = -1000;
		int max = 1000;

		Random random = new Random();

		// int[] vector = random.ints(-1000, 1000).toArray();

		List<Integer> intList = new ArrayList<Integer>();

		for (int i = 0; i < max - min; i++) {
			intList.add(random.nextInt(max - min) + min);
		}

		int[] vector = intList.stream().mapToInt(i -> i.intValue()).toArray();

		// int[] vector = { 5, 2, 4, 6, 1, 3, 8, 0, 4, 5, -1, -10, 20, 100, 50, -20 };
		// int[] vector = { 100, 80, 50, 20, 10, 8, 0, -10, -20, -25, -30, -50, -100 };

		// int[] vector = null;
		// int[] vector = {};

		System.out.println("Unsorted ==>" + Arrays.toString(vector));

		// bubbleSort(vector);

		mergeSort(vector, 0, vector.length - 1);

		System.out.println("Sorted ==>" + Arrays.toString(vector));

		// System.out.println(Arrays.toString(bubbleSort(vector)));

	}

}
