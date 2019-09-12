package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;

public interface IAdditionalBankDetailsDao extends JpaRepository<AdditionalBankDetailsViewx, Serializable> {

	@Query("select a from AdditionalBankDetailsViewx a where a.currencyId=?1 and a.bankId=?2 "
			+ " and a.remittanceId=?3 and a.deliveryId=?4 and a.countryId=?5 "
			+ " and a.flexField=?6")
	public List<AdditionalBankDetailsViewx> getAdditionalBankDetails(BigDecimal currencyId, BigDecimal bankId,
			BigDecimal remittanceModeId, BigDecimal deleveryModeId, BigDecimal countryId, String flexiField);

	@Query("select a from AdditionalBankDetailsViewx a where a.srlId=:srlId and a.currencyId=:currencyId and a.bankId=:bankId "
			+ " and a.remittanceId=:remittanceModeId and a.deliveryId=:deleveryModeId ")
	public List<AdditionalBankDetailsViewx> getAdditionalBankDetails(@Param("srlId") BigDecimal srlId,
			@Param("currencyId") BigDecimal currencyId, @Param("bankId") BigDecimal bankId,
			@Param("remittanceModeId") BigDecimal remittanceModeId, @Param("deleveryModeId") BigDecimal deleveryModeId);


	public AdditionalBankDetailsViewx findByCountryIdAndFlexFieldAndCurrencyIdAndBankIdAndRemittanceIdAndDeliveryIdAndAmiecCode(BigDecimal countryId,String flexField,BigDecimal currencyId, BigDecimal bankId,
			BigDecimal remittanceModeId, BigDecimal deleveryModeId, String amiecCode);

}

