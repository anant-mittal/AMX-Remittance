package com.amx.jax.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.constants.CommunicationChannel;

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
		assertThat(date != null);
	}
	
	@Test
	public void testArrayUtils() {
		CommunicationChannel c = null;
		List<CommunicationChannel> channel = Arrays.asList(c);
		assertNotNull(channel);
	}
}
