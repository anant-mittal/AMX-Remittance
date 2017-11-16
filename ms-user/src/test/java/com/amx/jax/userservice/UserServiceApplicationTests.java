package com.amx.jax.userservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.userservice.service.KwUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceApplicationTests {

	@Autowired
	KwUserService kwUserService;

	@Test
	public void contextLoads() {
		assertTrue(kwUserService.validateCivilId("288122507112"));
		assertFalse(kwUserService.validateCivilId("1234"));
	}

}
