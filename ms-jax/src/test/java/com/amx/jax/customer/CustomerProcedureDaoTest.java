package com.amx.jax.customer;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dal.ImageCheckDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerProcedureDaoTest {

	@Autowired
	ImageCheckDao imageCheckDao;
	
	@Test
	public void testGenerateBlobId() {
		BigDecimal output = imageCheckDao.callTogenerateBlobID(new BigDecimal(2019));
		assertNotNull(output);
	}
}
