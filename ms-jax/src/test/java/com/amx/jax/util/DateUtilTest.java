package com.amx.jax.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class DateUtilTest {

	DateUtil dateUtil = new DateUtil();

	@Test
	public void validateDateTest() {
		LocalDate date = dateUtil.validateDate("11/1/2018", "MM/d/yyyy");
		assertThat(date != null);
	}
	
	@Test
	public void formatDateTest() {
		String date = dateUtil.format(LocalDate.now(), "MM/d/YYYY");
		System.out.println(date);
		assertThat(date != null);
	}
}
