package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsView;

public interface IAdditionalBankDetailsDao extends JpaRepository<AdditionalBankDetailsView, Serializable> {

	@Query("select a from AdditionalBankDetailsView a where a.currencyId=:currencyId and a.bankId=:bankId "
			+ " and a.remittanceId=:remittanceModeId and a.deliveryId=:deleveryModeId and a.countryId=:countryId "
			+ " and a.flexField=:flexiField")
	public List<AdditionalBankDetailsView> getAdditionalBankDetails(@Param("currencyId") BigDecimal currencyId,
			@Param("bankId") BigDecimal bankId, @Param("remittanceModeId") BigDecimal remittanceModeId,
			@Param("deleveryModeId") BigDecimal deleveryModeId, @Param("countryId") BigDecimal countryId,
			@Param("flexiField") String flexiField);

	@Query("select a from AdditionalBankDetailsView a where a.srlId=:srlId and a.currencyId=:currencyId and a.bankId=:bankId "
			+ " and a.remittanceId=:remittanceModeId and a.deliveryId=:deleveryModeId and a.countryId=:countryId "
			+ " and a.flexField=:flexiField")
	public List<AdditionalBankDetailsView> getAdditionalBankDetails(@Param("srlId") BigDecimal srlId,
			@Param("currencyId") BigDecimal currencyId, @Param("bankId") BigDecimal bankId,
			@Param("remittanceModeId") BigDecimal remittanceModeId, @Param("deleveryModeId") BigDecimal deleveryModeId);

}
