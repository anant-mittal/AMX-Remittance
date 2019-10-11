package com.amx.jax.remittance;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.repository.routing.RoutingDetailRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoutingTest {

	@Autowired
	RoutingDetailRepository repo;

	@Test
	public void testRoutingDetailRepository() {
		List<BigDecimal> list = repo.getCashRoutingBanks(new CurrencyMasterMdlv1(new BigDecimal(4)),
				new CountryMaster(new BigDecimal(94)));
		assertNotNull(list);
	}
}
