package com.amx.jax.repository;
/**
 * @author rabil 
 * @date 10/29/2019
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RatePlaceOrder;

public interface IRatePlaceOrderRepository  extends CrudRepository<RatePlaceOrder, Serializable>{

	
	@Query("select rv from RatePlaceOrder rv where rv.customerId=?1 and trunc(createdDate)=trunc(sysdate) and rv.isActive='Y' order by createdDate desc")
	public List<RatePlaceOrder> fetchPlaceOrderForCustomer(BigDecimal customerId);
	
	@Query("select rv from RatePlaceOrder rv where rv.beneficiaryCountryId =?1 and trunc(valueDate)=trunc(sysdate) and NVL(rv.isActive,' ') <>'D' "
			+ "and applDocumentNumber is null and applDocumentFinanceYear is null and approvedBy is null and approvedDate is null "
			+ "and branchSupportIndicator is null order by createdDate desc")
	public List<RatePlaceOrder> fetchByBeneficiaryCountryId(BigDecimal beneficiaryCountryId);
	
	
	@Query("select rv from RatePlaceOrder rv where rv.customerId=?1 and trunc(createdDate)=trunc(sysdate)  and rv.isActive='U' "
			+ "and rv.beneficiaryRelationId=?2 and rv.transactionAmount=?3 and remitType=?4 order by createdDate desc")
	public List<RatePlaceOrder> sameBeneTrnxAmtCheck(BigDecimal customerId,BigDecimal beneRelationId,BigDecimal trnxAmount,BigDecimal remitType);
	
	
	
	@Query("select rv from RatePlaceOrder rv where rv.customerId=?1 and trunc(createdDate)=trunc(sysdate)  and rv.isActive='U' "
			+ "and rv.beneficiaryRelationId=?2 and remitType=?3 order by createdDate desc")
	public List<RatePlaceOrder> sameBeneTrnxButDiffAmtCheck(BigDecimal customerId,BigDecimal beneRelationId,BigDecimal remitType);
	
	
	
	@Query(value =  "Select\n" + 
			"    Beneficiary_Country_Id,(select COUNTRY_NAME FROM FS_COUNTRY_MASTER_DESC where COUNTRY_ID =Beneficiary_Country_Id AND LANGUAGE_ID=1) COUNTRY_NAME, \n" + 
			"    count(Beneficiary_Country_Id) \n" + 
			"from\n" + 
			"    EX_RATE_PLACE_ORDER p\n" + 
			"Where\n" + 
			"    trunc(Value_Date) = trunc(sysdate) \n" + 
			"    and Isactive = 'U' \n" + 
			"    and Approved_By is null \n" + 
			"    and Approved_Date is null \n" + 
			"Group By\n" + 
			"    Beneficiary_Country_Id",nativeQuery=true)
	public List<Object[]> getPlaceOrderCountryWiseCoount();
	
	
	@Query("select rv from RatePlaceOrder rv where rv.customerId=?1 and ratePlaceOrderId = ?2 and trunc(createdDate)=trunc(sysdate)  "
			+ " and rv.isActive='Y' and applDocumentNumber is null and applDocumentFinanceYear is null and NVL(approvedBy,' ') <> ' '  order by createdDate desc")
	public RatePlaceOrder fetchApprovedPlaceOrder(BigDecimal customerId,BigDecimal placeOrderId);
	
}
