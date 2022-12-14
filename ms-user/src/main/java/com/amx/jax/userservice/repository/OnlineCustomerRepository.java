package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CustomerOnlineRegistration;

public interface OnlineCustomerRepository extends JpaRepository<CustomerOnlineRegistration, Serializable> {

	@Query("select c from CustomerOnlineRegistration c where countryId=?1 and userName=?2")
	public List<CustomerOnlineRegistration> getOnlineCustomerList(BigDecimal countryId, String userName);

	@Query("select c from CustomerOnlineRegistration c where customerId=?1")
	public List<CustomerOnlineRegistration> getOnlineCustomersById(BigDecimal customerId);

	@Query("select c from CustomerOnlineRegistration c where loginId=?1 and status='Y'")
	public CustomerOnlineRegistration getOnlineCustomersByLoginId(String loginId);

	@Query("select c from CustomerOnlineRegistration c where (loginId=?1 or userName=?1) and status='Y' ")
	public CustomerOnlineRegistration getOnlineCustomerByLoginIdOrUserName(String value);
	
	@Query("select c from CustomerOnlineRegistration c where (loginId=?1 or userName=?1)")
	public List<CustomerOnlineRegistration> getOnlineCustomerWithStatusByLoginIdOrUserName(String value);
	
	@Query("select c from CustomerOnlineRegistration c where loginId=?1")
	public CustomerOnlineRegistration getLoginCustomersById(String userName);
	
	//
	@Query("select c from CustomerOnlineRegistration c where loginId=?1")
	public CustomerOnlineRegistration getCustomerIDByuserId(String userName);
	
	
	@Query("select c from CustomerOnlineRegistration c where userName=?1")
	public CustomerOnlineRegistration getLoginCustomersDeatilsById(String userName);
	
	public CustomerOnlineRegistration findByCustomerId(BigDecimal customerId);
	

}
