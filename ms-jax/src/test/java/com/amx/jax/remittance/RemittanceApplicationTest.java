package com.amx.jax.remittance;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.AbstractTest;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.manager.RemittancePaymentManager;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest

public class RemittanceApplicationTest extends AbstractTest {

	@Autowired
	RemittanceApplicationRepository repo;
	@Autowired
	RemittanceTransactionRepository trnxRepo;
	@Autowired
	RemittancePaymentManager remittancePaymentManager;

	@Test
	@Transactional
	public void testAmountMismatch() {
		RemittanceApplication remittanceApplication = repo.findOne(new BigDecimal(2947458));
		PaymentResponseDto dto = new PaymentResponseDto();
		dto.setAmount("3");

	}
}
