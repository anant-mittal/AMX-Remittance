package com.amx.jax.customer;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.model.customer.CustomerDocumentInfo;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerDocumentManagerTest {

	@Autowired
	CustomerDocumentManager customerDocumentManager;

	@Test
	public void testGetCustomerDocument() {
		List<CustomerDocumentInfo> output = customerDocumentManager.getCustomerImages(new BigDecimal(5218));
		assertNotNull(output);
	}
}
