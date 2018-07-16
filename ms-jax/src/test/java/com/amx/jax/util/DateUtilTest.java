package com.amx.jax.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DateUtilTest {

	@Autowired
	DateUtil dateUtil;

	@Test
	public void validateDateTest() {
		LocalDate date = dateUtil.validateDate("11/1/2018", "MM/d/yyyy");
		assertThat(date != null);
	}
}
