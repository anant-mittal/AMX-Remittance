package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.WhyDoAskInformation;

public interface IWhyDoAskInformationRepository extends JpaRepository<WhyDoAskInformation, BigDecimal>{
	
	@Query("Select t from WhyDoAskInformation t where languageId=?1 and countryId=?2")
public List<WhyDoAskInformation> getwhyDoAskInformation(BigDecimal languageId,BigDecimal countryId);

}