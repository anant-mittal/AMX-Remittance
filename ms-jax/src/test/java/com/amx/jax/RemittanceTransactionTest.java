package com.amx.jax;

import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemittanceTransactionTest {

	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;

	@Test
	public void testTrnxNullDoc() {
		RemittanceTransaction remitTrnx = remittanceApplicationDao
				.getRemittanceTransactionByRemitDocNo(new BigDecimal(10), null);
		assertNull("trnx shoudl be null", remitTrnx);
	}
}
