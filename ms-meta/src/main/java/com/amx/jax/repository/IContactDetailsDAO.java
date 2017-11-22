package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.model.ContactDetail;

public interface IContactDetailsDAO  extends JpaRepository<ContactDetail, Serializable>{
	
	@Query("select cd from ContactDetail cd where customerId =?1 and activeStatus='Y'")
	public List<ContactDetail> getContactDetails(BigDecimal customerId);
	
	@Query("select cd from ContactDetail cd where customerId =?1 and contactTypeId=?2 and activeStatus='Y'")
	public List<ContactDetail> getContactDetailByCotactId(BigDecimal customerId,BigDecimal contactTypeId);
	
	
}
