package com.amx.jax.userservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.userservice.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=UserServiceApplicationTests.class)
public class UserServiceApplicationTests {

	@Autowired
	UserService kwUserService;

	@Test
	public void contextLoads() {
//		assertTrue(kwUserService.validateCivilId("288122507112", "KW"));
//		assertFalse(kwUserService.validateCivilId("1234", "BH"));
	}

}
