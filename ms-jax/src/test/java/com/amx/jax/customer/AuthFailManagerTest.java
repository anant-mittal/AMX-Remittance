package com.amx.jax.customer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.auth.AuthFailureLogManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthFailManagerTest {

	@Autowired
	AuthFailureLogManager authfailmanager;
	
	@Test
	public void testgetLastUnblockDate() {
		authfailmanager.getLastUnblockDate("115.111.75.48");
	}
}
