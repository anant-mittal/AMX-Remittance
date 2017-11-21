package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.model.WhyDoAskInformation;

public interface IWhyDoAskInformationRepository extends JpaRepository<WhyDoAskInformation, BigDecimal>{
	
	@Query("Select t from WhyDoAskInformation t where languageId=? and countryId=?")
public List<WhyDoAskInformation> getwhyDoAskInformation(BigDecimal languageId,BigDecimal countryId);

}