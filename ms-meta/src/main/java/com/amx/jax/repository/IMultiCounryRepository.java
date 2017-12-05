package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.JAXDbCredentailsModel;

public interface IMultiCounryRepository  extends JpaRepository<JAXDbCredentailsModel, Serializable>{

	@Query("select mc from JAXDbCredentailsModel mc where mc.isActive ='Y'")	
	public List<JAXDbCredentailsModel> getMultiCountryList();
	
	
	@Query("select mc from JAXDbCredentailsModel mc where mc.applicationCountryId =?1 and mc.isActive ='Y'")	
	public List<JAXDbCredentailsModel> getMultiCountryListByCountryId(BigDecimal countryId);
	

}
