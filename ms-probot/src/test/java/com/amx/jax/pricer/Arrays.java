package com.amx.jax.pricer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arrays {

	public static String maxSubArray(int[] array) {
		// 1. null / empty
		// 2. All +ve / -ve

		// [-2, -5, 6, -2, -3, 1, 5, -6]

		if (array == null || array.length == 0)
			return null;

		int curSum = 0;
		int bestSum = 0;

		int cfrom = 0, cto = 0, bfrom = 0, bto = 0;
		int bestNeg = Integer.MIN_VALUE, negI = 0;

		for (int i = 0; i < array.length; i++) {

			if (curSum + array[i] > 0) {
				curSum = curSum + array[i];
				cto = i;
			} else {
				curSum = 0;
				cfrom = i + 1;
			}

			// curSum = Math.max(0, );

			if (curSum > bestSum) {
				bestSum = curSum;
				bfrom = cfrom;
				bto = cto;
				System.out.println(" Itr: " + bfrom + " # " + bto + " # " + bestSum);

			}

			if (array[i] < 0 && array[i] > bestNeg) {
				bestNeg = array[i];
				negI = i;
			}

		}

		if (bfrom < bto) {
			return "" + bfrom + " # " + bto + " # " + bestSum;
		} else {
			return "" + negI + " # " + negI + " # " + bestNeg;
		}

	}

	public static void main(String[] args) {

		int min = -100;
		int max = -50;

		Random random = new Random();

		List<Integer> intList = new ArrayList<Integer>();

		for (int i = 0; i < max - min; i++) {
			intList.add(random.nextInt(max - min) + min);
		}

		// int[] arr = intList.stream().mapToInt(i -> i.intValue()).toArray();

		// int[] arr = { -2, -5, 6, -2, -3, 1, 5, -6 };

		int[] arr = {};

		System.out.println("Array ==>" + java.util.Arrays.toString(arr));

		System.out.println(" Max Sum ==>" + maxSubArray(arr));

	}

}
