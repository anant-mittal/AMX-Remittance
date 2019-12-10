package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
	
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.CustomerBank;

public interface CustomerBankRepository extends CrudRepository<CustomerBank, Serializable>{

	@Query("select c from CustomerBank c where c.customerId=?1 and c.bankId=?2 and isActive='Y'")
	List<CustomerBank> getCustomerBanks(BigDecimal customerId, BigDecimal bankId);

}
