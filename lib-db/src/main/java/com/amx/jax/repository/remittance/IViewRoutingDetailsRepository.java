/*package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.ViewRoutingDetails;

public interface IViewRoutingDetailsRepository extends CrudRepository<ViewRoutingDetails, Serializable>{
	
	@Query("select distinct r.serviceMasterId, r.serviceCode,r.serviceDescription from ViewRoutingDetails r where r.beneBankId=:beneBankId and  r.beneBankBranchId=:beneBankBranchId and r.countryId =:countryId "
			+ " and r.currencyId=:currencyId and r.applicationCountryId=:applicationCountryId and r.serviceGroupCode=:serviceGroupCode")
	List<ViewRoutingDetails> getBankServiceList(@Param("beneBankId") BigDecimal  beneBankId,
			@Param("beneBankBranchId") BigDecimal  beneBankBranchId,
			@Param("countryId") BigDecimal  countryId,
			@Param("currencyId") BigDecimal  currencyId,
			@Param("applicationCountryId") BigDecimal  applicationCountryId,
			@Param("serviceGroupCode") String  serviceGroupCode);
	
	
	@Query("select distinct r.routingCountryId,r.rowId ,r.countryCode ,r.CountryName from ViewRoutingDetails r where r.applicationCountryId=:applicationCountryId and r.beneBankId=:beneBankId and  r.beneBankBranchId=:beneBankBranchId and r.countryId =:countryId "
			+ " and r.currencyId=:currencyId and  and r.serviceMasterId=:serviceMasterId and r.routingCountryId=:routingCountryId and f.routingBankId=DECODE(:serviceMasterId,101,:beneBankId,r.routingBankId) ")
	List<ViewRoutingDetails> getRoutingCountryList(@Param("beneBankId") BigDecimal  beneBankId,
			@Param("beneBankBranchId") BigDecimal  beneBankBranchId,
			@Param("countryId") BigDecimal  countryId,
			@Param("currencyId") BigDecimal  currencyId,
			@Param("applicationCountryId") BigDecimal  applicationCountryId,
			@Param("serviceMasterId") BigDecimal  serviceMasterId);
	
	
	
	
	@Query("select distinct r.routingBankId,r.rowId ,r.routingBankCode,r.routingBankName from ViewRoutingDetails r where r.applicationCountryId=:applicationCountryId and r.beneBankId=:beneBankId and  r.beneBankBranchId=:beneBankBranchId and r.countryId =:countryId "
			+ " and r.currencyId=:currencyId and  and r.serviceMasterId=:serviceMasterId and r.routingCountryId=:routingCountryId and f.routingBankId=DECODE(:serviceMasterId,101,:beneBankId,r.routingBankId) ")
	List<ViewRoutingDetails> getRoutingBankList(@Param("beneBankId") BigDecimal  beneBankId,
			@Param("beneBankBranchId") BigDecimal  beneBankBranchId,
			@Param("countryId") BigDecimal  countryId,
			@Param("currencyId") BigDecimal  currencyId,
			@Param("applicationCountryId") BigDecimal  applicationCountryId,
			@Param("serviceMasterId") BigDecimal  serviceMasterId,
			@Param("routingCountryId") BigDecimal  routingCountryId);
	
	
	
	
	
	
	
	@Query("select distinct r.remittanceModeId,r.rowId ,r.remittancCode,r.remittanceDescription from ViewRoutingDetails r where r.applicationCountryId=:applicationCountryId and r.beneBankId=:beneBankId and  r.beneBankBranchId=:beneBankBranchId and r.countryId =:countryId "
			+ " and r.currencyId=:currencyId and  and r.serviceMasterId=:serviceMasterId and r.routingCountryId=:routingCountryId and f.routingBankId=DECODE(:serviceMasterId,101,:beneBankId,r.routingBankId) and r.routingBankId=:routingBankId order by r.remittanceModeId")
	List<ViewRoutingDetails> getRemittanceListByCountryBank(@Param("beneBankId") BigDecimal  beneBankId,
			@Param("beneBankBranchId") BigDecimal  beneBankBranchId,
			@Param("countryId") BigDecimal  countryId,
			@Param("currencyId") BigDecimal  currencyId,
			@Param("applicationCountryId") BigDecimal  applicationCountryId,
			@Param("serviceMasterId") BigDecimal  serviceMasterId,
			@Param("routingCountryId") BigDecimal  routingCountryId,
			@Param("routingBankId") BigDecimal  routingBankId);
	
	
	@Query("select distinct r.deliveryModeId,r.rowId ,r.deliveryCode,r.deliveryDescription from ViewRoutingDetails r where r.applicationCountryId=:applicationCountryId and r.beneBankId=:beneBankId and  r.beneBankBranchId=:beneBankBranchId and r.countryId =:countryId "
			+ " and r.currencyId=:currencyId and  and r.serviceMasterId=:serviceMasterId and r.routingCountryId=:routingCountryId and f.routingBankId=DECODE(:serviceMasterId,101,:beneBankId,r.routingBankId) "
			+ " and r.routingBankId=:routingBankId and r.remittanceModeId =:remittanceModeId order by r.remittanceModeId")
	List<ViewRoutingDetails> getDeliveryListByCountryBankRemit(@Param("beneBankId") BigDecimal  beneBankId,
			@Param("beneBankBranchId") BigDecimal  beneBankBranchId,
			@Param("countryId") BigDecimal  countryId,
			@Param("currencyId") BigDecimal  currencyId,
			@Param("applicationCountryId") BigDecimal  applicationCountryId,
			@Param("serviceMasterId") BigDecimal  serviceMasterId,
			@Param("routingCountryId") BigDecimal  routingCountryId,
			@Param("routingBankId") BigDecimal  routingBankId,
			@Param("remittanceModeId") BigDecimal  remittanceModeId);
	
			
	@Query("select distinct r.bankBranchId,r.rowId ,r.branchCode,r.branchFullName from ViewRoutingDetails r where r.applicationCountryId=:applicationCountryId and r.beneBankId=:beneBankId and  r.beneBankBranchId=:beneBankBranchId and r.countryId =:countryId "
			+ " and r.currencyId=:currencyId and  and r.serviceMasterId=:serviceMasterId and r.routingCountryId=:routingCountryId and f.routingBankId=DECODE(:serviceMasterId,101,:beneBankId,r.routingBankId) "
			+ "and r.routingBankId=:routingBankId and r.remittanceModeId=:remittanceModeId and r.deliveryModeId =:deliveryModeId")
	List<ViewRoutingDetails> getRoutingBankBranchList(@Param("beneBankId") BigDecimal  beneBankId,
			@Param("beneBankBranchId") BigDecimal  beneBankBranchId,
			@Param("countryId") BigDecimal  countryId,
			@Param("currencyId") BigDecimal  currencyId,
			@Param("applicationCountryId") BigDecimal  applicationCountryId,
			@Param("serviceMasterId") BigDecimal  serviceMasterId,
			@Param("routingCountryId") BigDecimal  routingCountryId,
			@Param("routingBankId") BigDecimal  routingBankId,
			@Param("remittanceModeId") BigDecimal  remittanceModeId,
			@Param("deliveryModeId") BigDecimal  deliveryModeId);
	
	
	
	private BigDecimal countryId;
	private String countryCode;
	private String CountryName;
	private BigDecimal routingCountryId;
	private BigDecimal routingBankId;
	private String routingBankCode;
	private String routingBankName;
	private BigDecimal currencyId;
	private String currencyCode;
	private String currencyName;
	private String quoteName;	
	private BigDecimal serviceMasterId;
	private String serviceCode;
	private String serviceDescription;
	private BigDecimal remittanceModeId;
	private String remittancCode;
	private String remittanceDescription;
	private BigDecimal deliveryModeId;
	private String deliveryCode;
	private String deliveryDescription;
	private BigDecimal bankBranchId;
	private BigDecimal branchCode;
	private String branchFullName;
	private BigDecimal beneBankId;
	private BigDecimal beneBankBranchId;
	private String serviceGroupCode;

}


*/