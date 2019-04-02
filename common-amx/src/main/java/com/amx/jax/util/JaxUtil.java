package com.amx.jax.util;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.ExceptionMessageKey;

/**
 * The Class JaxUtil.
 */
@Component
public class JaxUtil {

	/** The logger. */
	Logger logger = LoggerFactory.getLogger(getClass());
	BeanUtilsBean notNullBeanUtilsBean = new NullAwareBeanUtilsBean();


	/**
	 * Use {@link com.amx.utils.Random#randomNumeric(int)}
	 *
	 * @param length the length
	 * @return the string
	 */
	@Deprecated
	public String createRandomPassword(int length) {
		String validChars = "1234567890";
		String password = "";
		for (int i = 0; i < length; i++) {
			password = password + String.valueOf(validChars.charAt((int) (Math.random() * validChars.length())));
		}
		return password;
	}

	/**
	 * Builds the error expressions.
	 *
	 * @deprecated use
	 *             {@link ExceptionMessageKey#build(com.amx.jax.exception.IExceptionEnum, Object...)}
	 * @param respCode the resp code
	 * @param values   the values
	 * @return the string
	 */
	@Deprecated
	public String buildErrorExpressions(String respCode, List<String> values) {
		final String DELIM = ":";
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(respCode).append(DELIM);
		for (int i = 0; i < values.size(); i++) {
			if (i == (values.size() - 1)) {
				sbuf.append(values.get(i));
			} else {
				sbuf.append(values.get(i)).append(DELIM);
			}
		}
		return sbuf.toString();
	}

	/**
	 * Builds the error expression.
	 *
	 * @param respCode the resp code
	 * @param value    the value
	 * @return the string
	 */
	public String buildErrorExpression(String respCode, Object value) {
		final String DELIM = ":";
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(respCode).append(DELIM);
		sbuf.append(value.toString());
		return sbuf.toString();
	}

	/**
	 * Gets the random integers from list.
	 *
	 * @param input the input
	 * @param size  the size
	 * @return the random integers from list
	 */
	public List<Integer> getRandomIntegersFromList(List<Integer> input, int size) {
		int totalSize = input.size();
		List<Integer> output = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			Random rand = new Random();
			int randIndex = rand.nextInt(totalSize);
			output.add(input.get(randIndex));
			input.remove(randIndex);
			totalSize--;
		}
		return output;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		JaxUtil util = new JaxUtil();
		List<Integer> input = new ArrayList<>();
		input.add(3);
		input.add(233);
		input.add(33);
		input.add(31);

		int size = 3;
		util.getRandomIntegersFromList(input, size);
	}

	/**
	 * Gets the random integers from list.
	 *
	 * @param questions the questions
	 * @param size      the size
	 * @return the random integers from list
	 */
	public List<BigDecimal> getRandomIntegersFromList(List<BigDecimal> questions, Integer size) {
		List<Integer> questionsInt = new ArrayList<>();
		List<BigDecimal> result = new ArrayList<>();
		questions.forEach(q -> questionsInt.add(q.intValue()));
		List<Integer> output = getRandomIntegersFromList(questionsInt, size);
		output.forEach(o -> result.add(new BigDecimal(o)));
		return result;
	}

	/**
	 * Checks if is null zero big decimal check.
	 *
	 * @param value the value
	 * @return true, if is null zero big decimal check
	 */
	public static boolean isNullZeroBigDecimalCheck(BigDecimal value) {
		if (value != null && value.compareTo(BigDecimal.ZERO) != 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Find element.
	 *
	 * @param         <T> the generic type
	 * @param set     the set
	 * @param element the element
	 * @return the t
	 */
	public <T> T findElement(Set<T> set, T element) {
		T output = null;
		if (set.contains(element)) {
			for (T t : set) {
				if (t.equals(element)) {
					output = t;
					break;
				}
			}
		}
		return output;
	}

	/**
	 * Gets the months list.
	 *
	 * @return the months list
	 */
	public Map<String, String> getMonthsList() {

		String[] monthsDesc = new DateFormatSymbols().getMonths();
		String[] monthCodes = new DateFormatSymbols().getShortMonths();
		Map<String, String> monthMap = new LinkedHashMap<>();
		for (int i = 0; i < monthsDesc.length; i++) {
			monthMap.put(monthCodes[i], monthsDesc[i]);
		}
		return monthMap;
	}

	/**
	 * Gets the short month.
	 *
	 * @param index the index
	 * @return the short month
	 */
	public String getShortMonth(int index) {

		String[] monthCodes = new DateFormatSymbols().getShortMonths();
		return monthCodes[index];
	}

	/**
	 * Convert.
	 *
	 * @param            <T> the generic type
	 * @param            <E> the element type
	 * @param fromObject the from object
	 * @param toObject   the to object
	 */
	public <T, E> void convert(T fromObject, E toObject) {
		try {
			BeanUtils.copyProperties(toObject, fromObject);
		} catch (Exception e) {
			logger.error("error in convert", e);
		}
	}

	/**
	 * Converts only non null properties of bean
	 *
	 * @param            <T> the generic type
	 * @param            <E> the element type
	 * @param fromObject the from object
	 * @param toObject   the to object
	 */
	public <T, E> void convertNotNull(T fromObject, E toObject) {
		try {
			notNullBeanUtilsBean.copyProperties(toObject, fromObject);
		} catch (Exception e) {
			logger.error("error in convert", e);
		}
	}

}
