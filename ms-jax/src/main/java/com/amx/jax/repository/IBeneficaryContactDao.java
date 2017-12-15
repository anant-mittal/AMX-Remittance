package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.bene.BeneficaryContact;

public interface IBeneficaryContactDao extends JpaRepository<BeneficaryContact, Serializable>{

	@Query("select bc from BeneficaryContact bc where bc.beneficaryMasterId =?1 and isActive='Y'")
	public List<BeneficaryContact> getBeneContact(BigDecimal beneMasterId);
}
