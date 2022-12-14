package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.amxlib.model.placeorder.PlaceOrderCustomer;
import com.amx.jax.dbmodel.Customer;

public interface CustomerRepository extends CrudRepository<Customer, BigDecimal> {

	@Query("select c from Customer c where countryId=?1 and  identityInt =?2 and isActive='Y'")
	public List<Customer> getCustomerbyuser(BigDecimal countryId, String userId);

	@Query("select c from Customer c where mobile=?1 and isActive='Y'")
	public List<Customer> getCustomerByMobile(String mobile);

	@Query("select c from Customer c where email=?1")
	public List<Customer> getCustomerByEmailId(String emailId);

	@Query("select c from Customer c where identityInt =?1 and isActive ='Y'")
	public List<Customer> findActiveCustomers(String identityInt);

	@Query("select c from Customer c where identityInt =?1")
	public List<Customer> getCustomerByIdentityInt(String identityInt);

	@Query("select c from Customer c where identityInt =?1")
	public Customer getCustomerOneByIdentityInt(String identityInt);

	@Query("select new com.amx.amxlib.model.placeorder.PlaceOrderCustomer(c.customerId, c.firstName, c.middleName, c.lastName, c.email)  from Customer c where c.customerId in (?1)")
	public List<PlaceOrderCustomer> findPOCustomersByIds(List<BigDecimal> customerIds);

	@Query("select c from Customer c where identityInt =?1 and countryId=?2 and identityTypeId = ?3 and   isActive in ('Y','N')")
	public List<Customer> getCustomerByCivilIdAndIsActive(String identityInt, BigDecimal countryId,
			BigDecimal identityTypeId);

	public Customer getCustomerByCustomerIdAndIsActive(BigDecimal customerId, String isActive);
	
	public Customer getCustomerByCustomerId(BigDecimal customerId);

	/// GET CUSTOMER BY CONTACT -- START
	@Query("select c from Customer c where mobile=?1 ")
	public List<Customer> getCustomerByMobileCheck(String mobile);

	@Query("select c from Customer c where whatsappPrefix=?1 and whatsapp=?2")
	public Customer getCustomerByWhatsApp(String whatsappPrefix, String whatsapp);

	@Query("select c from Customer c where whatsappPrefix=?1 and whatsapp=?2")
	public List<Customer> getCustomersByWhatsApp(String whatsappPrefix, String whatsapp);

	@Query("select c from Customer c where prefixCodeMobile=?1 and mobile=?2")
	public List<Customer> getCustomersByMobile(String prefixCodeMobile, String mobile);

	@Query("select c from Customer c where email=?1")
	public List<Customer> getCustomersByEmail(String email);

	/// GET CUSTOMER BY CONTACT -- ENDS

	@Query("select c from Customer c where identityInt=?1 and isActive='N'")
	public Customer getCustomerDetails(String loginId);
	
	@Query("select c from Customer c where identityInt=?1 and isActive='Y'")
	public Customer getActiveCustomerDetails(String loginId);

	@Query("select c from Customer c where identityInt=?1 and identityTypeId = ?2 and isActive='N'")
	public Customer getCustomerData(String identityInt, BigDecimal identityTypeId);

	@Query("select c from Customer c where identityInt=?1 and identityTypeId = ?2 and isActive='Y'")
	public Customer getActiveCustomerByIndentityIntAndType(String identityInt, BigDecimal identityTypeId);

	//
	@Query("select c from Customer c where customerId=?1 and isActive='N'")
	public Customer getCustomerDetailsByCustomerId(BigDecimal customerId);

	@Query("select c from Customer c where identityInt =?1 and identityTypeId = ?2")
	public List<Customer> getCustomerByIdentityInt(String identityInt, BigDecimal identityType);

	@Query("select c from Customer c where identityInt=?1 and identityTypeId = ?2 and countryId =?3")
	public Customer getCustomerDetails(String identityInt, BigDecimal identityTypeId, BigDecimal countryId);

	@Query("select c from Customer c where identityInt=?1  and countryId =?2")
	public List<Customer> getCustomerDetails(String identityInt, BigDecimal countryId);


	@Query("select c from Customer c where identityInt =?1 and isActive ='Y' and identityTypeId = ?2")
	public List<Customer> findActiveCustomers(String identityInt, BigDecimal identityType);

	@Query("select c from Customer c where identityInt=?1 and identityTypeId = ?2 and isActive in ?3 order by lastUpdated desc")
	public List<Customer> getCustomerByIndentityIntAndTypeAndIsactive(String identityInt, BigDecimal identityTypeId,
			List<String> status);

	@Query("select c from Customer c where identityInt =?1 and isActive in ?2 order by lastUpdated desc")
	public List<Customer> getCustomerByIdentityIntAndIsActive(String identityInt, List<String> isActive);

	@Query("select c from Customer c where customerId =?1")
	public Customer getNationalityValue(BigDecimal customerId);

	@Query(value = "select * from fs_customer where nationality=?1 and mobile=?2 and email = ?3 and first_name = ?4  and isactive='Y' order by last_updated", nativeQuery = true)
	public List<Customer> getCustomerForDuplicateCheck(BigDecimal nationality, String mobile, String email,
			String firstName);

	
	@Query("select c from Customer c where identityInt=?1")
	public Customer getCustomerEmailDetails(String identityInt);
	
	@Query("select c from Customer c where customerId=?1 and isActive='Y'")
	public Customer getActiveCustomerDetailsByCustomerId(BigDecimal customerId);
	
}
