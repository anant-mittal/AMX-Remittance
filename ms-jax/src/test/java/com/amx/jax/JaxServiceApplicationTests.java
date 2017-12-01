package com.amx.jax;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.dal.CryptoDao;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.WebUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxServiceApplicationTests {

	@Autowired
	CryptoDao cryptdao;

	@Autowired
	UserService userService;

	// @Test
	public void decrypt() {
		String output = cryptdao.decrypt("123123", "15BBA6D8EF5C4D660F57CB9E91C7B9D5");
		assertTrue(output.equals("amx@123"));
	}

	// @Test
	public void encrypt() {
		String output = cryptdao.encrypt("123123", "amx@123");
		assertTrue(output.equals("15BBA6D8EF5C4D660F57CB9E91C7B9D5"));
	}

	// @Test
	public void sendOtpForCivilidCheck() {
		ApiResponse apiResp = userService.sendOtpForCivilId("284052306594");
		assertTrue(apiResp.getError() == null);
	}

	@Bean
	public MetaData meta() {
		MetaData metad = new MetaData();
		metad.setCountryId(new BigDecimal(91));
		return metad;
	}

	@Autowired
	WebUtils webutil;

	@Test
	public void testwebutil() {

		webutil.getClientIp();
	}
}
