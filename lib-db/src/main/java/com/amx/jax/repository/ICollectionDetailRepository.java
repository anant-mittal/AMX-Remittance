package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.CollectDetailMdlv1;

public interface ICollectionDetailRepository extends CrudRepository<CollectDetailMdlv1, Serializable>{

	public List<CollectDetailMdlv1> findByDocumentNoAndDocumentCodeAndDocumentFinanceYear(BigDecimal documentNo,BigDecimal documentCode,BigDecimal documentFinanceYear);

	@Query(value= "select * from EX_COLLECT_DETAIL where ACYYMM =to_date(:accMyear,'dd/MM/yyyy') and country_branch_id =:countrybranchId and created_by=:username and trunc(CREATED_DATE)=trunc(sysdate)", nativeQuery=true)
	public List<CollectDetailMdlv1> getCollectionDetailsForUser(@Param("username") String username,@Param("accMyear") String accMyear,@Param("countrybranchId") BigDecimal countrybranchId);

}
