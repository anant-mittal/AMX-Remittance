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
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerDocumentManagerTest {

	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	CustomerDocMasterManager docMngr;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	UserService userservice;

//	@Test
	public void testGetCustomerDocument() {
		CustomerDocumentTypeMaster docTypeMaster = docMngr.getDocTypeMaster("ADDRESS_PROOF", "DRIVING_LICENSE");
		List<CustomerDocumentUploadReference> uploads = customerDocumentManager.getCustomerUploads(new BigDecimal(932353));
		uploads.forEach(i -> {
			System.out.println(i.getDbScanRef());
		});
		assertNotNull(docTypeMaster);
	}
	
	@Test
	public void testGetDmsMapping() {
		Customer customer = userservice.getCustById(new BigDecimal(1054));
		DmsApplMapping mapping = customerIdProofManager.getDmsMapping(customer);
		assertNotNull(mapping);
	}
	
}
