package com.amx.jax.pricer;

public class Arrays {

	public static String maxSubArray(int[] array) {
		//1. null / empty
		//2. All +ve / -ve
		
		// [-2, -5, 6, -2, -3, 1, 5, -6]
		
		int curSum = Integer.MIN_VALUE;
		int bestSum = Integer.MIN_VALUE;
		
		int from = 0,to = 0;
		
		for(int i=0;i<array.length;i++) {
			curSum = curSum + array[i];
			to = i;
			if(curSum > bestSum) {
				bestSum = curSum;
			}
			
		}
		
		return  ""+ from + "#" + to + "#" + bestSum ;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
