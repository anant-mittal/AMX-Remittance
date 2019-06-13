package com.amx.jax.customer;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dict.Tenant;
import com.amx.jax.repository.customer.CustomerDocumentTypeMasterRepo;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.userservice.dao.CustomerDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDaoTest {

	@Autowired
	CustomerDao custDao;
	@Autowired
	CustomerDocumentTypeMasterRepo repo;

	public void testGetActiveCustomers() {
		List<Customer> customers = custDao.findActiveCustomers("284052306594", null);
		assertNotNull(customers);
	}

	public void testPopulateCusmas() {
		TenantContextHolder.setCurrent(Tenant.KWT2);
		Map<String, Object> output = custDao.callProcedurePopulateCusmas(new BigDecimal(1320544));
		assertNotNull(output);
	}
	
	@Test
	public void testCustomerSave() {
		Customer customer = custDao.getCustById(new BigDecimal(5218));
		customer.getComplianceBlockedDocuments().add(repo.findOne(BigDecimal.ONE));
		custDao.saveCustomer(customer);
	}

}
