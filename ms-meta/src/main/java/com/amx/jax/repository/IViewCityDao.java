package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.ViewCity;

public interface IViewCityDao extends JpaRepository<ViewCity, Serializable>{

	@Query("select cl from ViewCity cl where cl.districtId=?1 and cl.languageId=?2")
	public List<ViewCity> getCityByDistrictId(BigDecimal districtId,BigDecimal languageId);
	
	@Query("select cld from ViewCity cld where cld.districtId=:distId and cld.cityMasterId =:cityid  and cld.languageId=:languageid")
	public List<ViewCity> getCityDescription(@Param("distId") BigDecimal distId,
			@Param("cityid") BigDecimal cityid,
			@Param("languageid") BigDecimal languageid);
	


	public List<ViewCity> findByCityMasterId(BigDecimal cityid);
}
