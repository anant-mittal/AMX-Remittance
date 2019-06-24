package com.amx.jax.customer;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.customer.document.manager.CustomerDocMasterManager;
import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.model.customer.CustomerDocumentInfo;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerDocumentManagerTest {

	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	CustomerDocMasterManager docMngr;

	@Test
	public void testGetCustomerDocument() {
		CustomerDocumentTypeMaster docTypeMaster = docMngr.getDocTypeMaster("ADDRESS_PROOF", "DRIVING_LICENSE");
		customerDocumentManager.checkAndRemoveBlockedDocuments(new BigDecimal(5218), docTypeMaster);
		assertNotNull(docTypeMaster);
	}
}
