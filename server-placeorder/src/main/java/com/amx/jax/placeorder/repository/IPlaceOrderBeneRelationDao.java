package com.amx.jax.placeorder.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.PlaceOrder;

public interface IPlaceOrderBeneRelationDao extends JpaRepository<BenificiaryListView, Serializable>{

	/*@Query("select p from  BenificiaryListView b  inner join  PlaceOrder p on b.beneficiaryRelationShipSeqId =p.beneficiaryRelationshipSeqId where b.countryId =:countryId and b.bankId =:bankId and  b.currencyId =:currencyId and p.targetExchangeRate ='200' ")
	public List<BenificiaryListView> getPlaceOrderBeneRelation(@Param("countryId") BigDecimal countryId,@Param("currencyId") BigDecimal currencyId, @Param("bankId") BigDecimal bankId);
*/}
