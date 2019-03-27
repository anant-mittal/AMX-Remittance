package com.amx.jax.customer;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.userservice.dao.CustomerDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDaoTest {

	@Autowired
	CustomerDao custDao;

	@Test
	public void testGetActiveCustomers() {
		List<Customer> customers = custDao.findActiveCustomers("284052306594", null);
		assertNotNull(customers);
	}
	
}
