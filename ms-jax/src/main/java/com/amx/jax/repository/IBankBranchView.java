package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.BankBranchView;

public interface IBankBranchView extends JpaRepository<BankBranchView, Serializable>{
	
	@Query("select bb from BankBranchView bb where bb.bankId=:bankid and bb.bankBranchId=:branchid")
	public List<BankBranchView> getBankBranch(@Param("bankid") BigDecimal bankid,@Param("branchid") BigDecimal branchid);

}
