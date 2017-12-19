package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.SwiftMasterView;

public interface ISwiftMasterDao extends JpaRepository<SwiftMasterView, Serializable> {

	@Query("select smv from SwiftMasterView smv where smv.swiftBIC=:swiftbic")
	public List<SwiftMasterView> getSwiftMasterDetails(@Param("swiftbic") String swiftbic);
	
	
	@Query("select smv from SwiftMasterView smv where smv.countryId=:countryid")
	public List<SwiftMasterView> getSwiftMasterDetailsByBeneCountryId(@Param("countryid") BigDecimal countryid);
	
}
