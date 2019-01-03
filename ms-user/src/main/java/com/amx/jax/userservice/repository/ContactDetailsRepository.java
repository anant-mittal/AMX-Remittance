package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.Customer;

public interface ContactDetailsRepository extends JpaRepository<ContactDetail, Serializable> {

	@Query("select cd from ContactDetail cd where cd.fsCustomer =?1 and activeStatus='Y'")
	public List<ContactDetail> getContactDetails(Customer customerId);

	@Query("select cd from ContactDetail cd where cd.fsCustomer =?1 and cd.fsBizComponentDataByContactTypeId=?2 and activeStatus='Y'")
	public List<ContactDetail> getContactDetailByCotactId(Customer customerId, BizComponentData bizComponentData);
	
}
