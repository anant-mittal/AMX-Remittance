package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;

@Transactional
public interface CustomerContactVerificationRepository extends CrudRepository<CustomerContactVerification, BigDecimal> {

	// @Query("select c from CustomerContactVerification c where id=?1")
	CustomerContactVerification findById(BigDecimal id);

	@Query("select c from CustomerContactVerification cv where cv.customerId=?1 and cv.contactType=?2 and cv.contactValue=?2  and cv.isActive='Y' and cv.createdDate > :createdDate")
	public List<CustomerContactVerification> getByContact(BigDecimal customerId,
			ContactType contactType, String contactValue, @Param("createdDate") java.sql.Date date);

}
