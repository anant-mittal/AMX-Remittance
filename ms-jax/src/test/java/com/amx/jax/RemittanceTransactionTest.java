package com.amx.jax;

import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemittanceTransactionTest {

	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	@Autowired
	com.amx.jax.repository.IBeneficiaryOnlineDao iBeneficiaryOnlineDao;

	@Test
	public void testTrnxNullDoc() {
		List<BenificiaryListView> remitTrnx = iBeneficiaryOnlineDao.getBeneficiaryRelationShipSeqIds(new BigDecimal(5218), Arrays.asList(new BigDecimal(1)));
		assertNull("trnx shoudl be null", remitTrnx);
	}
}
