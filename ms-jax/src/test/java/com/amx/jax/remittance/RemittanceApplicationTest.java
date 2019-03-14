package com.amx.jax.remittance;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.AbstractTest;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;

public class RemittanceApplicationTest extends AbstractTest {

	@Autowired
	RemittanceApplicationRepository repo;
	@Autowired
	RemittanceTransactionRepository trnxRepo;

	@Test
	@Transactional
	public void testSaveAplication() {
		RemittanceApplication app = repo.findOne(new BigDecimal(2210));
		RemittanceTransaction trnx = trnxRepo.findOne(new BigDecimal(670));
		app.setCustomerSignatureClob(trnx.getCustomerSignatureClob());
		repo.save(app);
	}
}
