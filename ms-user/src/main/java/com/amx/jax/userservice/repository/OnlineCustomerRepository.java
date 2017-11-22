package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CustomerOnlineRegistration;

@Transactional
public interface OnlineCustomerRepository extends JpaRepository<CustomerOnlineRegistration, Serializable> {

	@Query("select c from CustomerOnlineRegistration c where countryId=?1 and userName=?2")
	public List<CustomerOnlineRegistration> getOnlineCustomerList(BigDecimal countryId, String userName);

	@Query("select c from CustomerOnlineRegistration c where customerId=?1")
	public List<CustomerOnlineRegistration> getOnlineCustomersById(BigDecimal customerId);

}
