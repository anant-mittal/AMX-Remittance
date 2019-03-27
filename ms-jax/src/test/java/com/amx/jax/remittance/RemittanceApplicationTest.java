package com.amx.jax.remittance;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.AbstractTest;
import com.amx.jax.branchremittance.dao.BranchRemittanceDao;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;

public class RemittanceApplicationTest extends AbstractTest {

	@Autowired
	RemittanceApplicationRepository repo;
	@Autowired
	RemittanceTransactionRepository trnxRepo;
	@Autowired
	BranchRemittanceDao brRemittanceDao;
	

	/*@Test
	public void testSaveAplication() {
		RemittanceApplication appl = new RemittanceApplication();
		appl.setRemittanceApplicationId(new BigDecimal(2946240));
		//brRemittanceDao.deleteFromCart(appl, ConstantDocument.Deleted);
	}*/
}
