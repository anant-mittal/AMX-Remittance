package com.amx.jax.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Util {

	public String createRandomPassword(int length) {
		String validChars = "1234567890";
		String password = "";
		for (int i = 0; i < length; i++) {
			password = password + String.valueOf(validChars.charAt((int) (Math.random() * validChars.length())));
		}
		return password;
	}

	public List<Integer> getRandomIntegersFromList(List<Integer> input, int size) {
		int totalSize = input.size();
		List<Integer> output = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			Random rand = new Random();
			int randIndex = rand.nextInt(totalSize);
			output.add(input.get(randIndex));
			System.out.println(input.get(randIndex));
			input.remove(randIndex);
			totalSize--;
		}
		return output;
	}

	public static void main(String[] args) {
		Util util = new Util();
		List<Integer> input = new ArrayList<>();
		input.add(3);
		input.add(233);
		input.add(33);
		input.add(31);

		int size = 3;
		util.getRandomIntegersFromList(input, size);
	}

	public List<BigDecimal> getRandomIntegersFromList(List<BigDecimal> questions, Integer size) {
		List<Integer> questionsInt = new ArrayList<>();
		List<BigDecimal> result = new ArrayList<>();
		questions.forEach(q -> questionsInt.add(q.intValue()));
		List<Integer> output = getRandomIntegersFromList(questionsInt, size);
		output.forEach(o -> result.add(new BigDecimal(o)));
		return result;
	}
	
	public static boolean isNullZeroBigDecimalCheck(BigDecimal value) {
		if(value != null && value.compareTo(BigDecimal.ZERO)!=0) {
			return true;
		}else {
			return false;
		}
	}
}
