package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.Customer;

public interface IContactDetailDao extends JpaRepository<ContactDetail, Serializable>{

	@Query("select cd from ContactDetail cd where cd.fsCustomer=?1 and cd.fsBizComponentDataByContactTypeId =49 and cd.activeStatus='Y'")
	public List<ContactDetail> getContactDetailForLocal(Customer customerId);
	
	@Query("select cd from ContactDetail cd where cd.fsCustomer.customerId=?1 and cd.fsBizComponentDataByContactTypeId =49 and cd.activeStatus='Y'")
	public ContactDetail getContactDetailForLocal(BigDecimal customerId);
}
