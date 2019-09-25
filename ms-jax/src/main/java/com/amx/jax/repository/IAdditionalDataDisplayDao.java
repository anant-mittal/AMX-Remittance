package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;

public interface IAdditionalDataDisplayDao  extends JpaRepository<AdditionalDataDisplayView, Serializable>{


	
	public static String[] flexiFieldIn = {ConstantDocument.INDIC1,ConstantDocument.INDIC2,ConstantDocument.INDIC3,ConstantDocument.INDIC4,ConstantDocument.INDIC5};
	
	@Query("select av from AdditionalDataDisplayView av where av.applicationCountryId=:applicationCountryId "
			+ " and av.routingCountryId=:countryId "
			+ " and av.routingCurrencyId=:currencyId and av.remittanceModeId=:remittanceModeId "
			+ " and av.deliveryModeId =:deliveryModeId and av.isActive='Y' and av.isRendered='Y' "
			+ " and av.flexField  in :flexiFieldIn")
	public List<AdditionalDataDisplayView> getAdditionalDataFromServiceApplicability(
			@Param("applicationCountryId") BigDecimal applicationCountryId, 
			@Param("countryId") BigDecimal countryId, 
			@Param("currencyId") BigDecimal currencyId, 
			@Param("remittanceModeId") BigDecimal remittanceModeId,
			@Param("deliveryModeId") BigDecimal deliveryModeId,
			@Param("flexiFieldIn") String[] flexiFieldIn );
	
	@Query("select av from AdditionalDataDisplayView av where av.applicationCountryId=:applicationCountryId "
			+ " and av.routingCountryId=:countryId "
			+ " and av.routingCurrencyId=:currencyId and av.remittanceModeId=:remittanceModeId "
			+ " and av.deliveryModeId =:deliveryModeId and av.isActive='Y' and av.isRendered='Y' "
			+ " and av.flexField  in :flexiFieldIn and av.bankId = :bankId")
	public List<AdditionalDataDisplayView> getAdditionalDataFromServiceApplicabilityForBank(
			@Param("applicationCountryId") BigDecimal applicationCountryId, 
			@Param("countryId") BigDecimal countryId, 
			@Param("currencyId") BigDecimal currencyId, 
			@Param("remittanceModeId") BigDecimal remittanceModeId,
			@Param("deliveryModeId") BigDecimal deliveryModeId,
			@Param("flexiFieldIn") String[] flexiFieldIn ,
			@Param("bankId") BigDecimal bankId);
}
