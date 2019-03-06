package com.amx.jax.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dbmodel.Device;
import com.amx.jax.rbaac.RbaacServiceApplication;
import com.amx.jax.rbaac.dao.DeviceDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RbaacServiceApplication.class)
public class DeviceTest {

	@Autowired
	DeviceDao deviceDao;

	@Test
	public void testDeviceSecret() {
		Device device = deviceDao.findDevice(new BigDecimal(75));
		assertTrue("secret mistmatch", device.getClientSecreteKey()
				.equals("74afbe2d5b7d77964e4dda557e3e52a5fbb2c9ff9db647acfaad33fab9f24920"));
	}
}
