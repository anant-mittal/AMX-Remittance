package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.bene.BankBlWorld;

public interface IBeneBankBlackCheckDao extends JpaRepository<BankBlWorld, Serializable>{
	
	@Query(value="SELECT * FROM   BNKBLWORD WHERE  blword  like '%?1%'" ,nativeQuery=true)
	public List<BankBlWorld> getCheckBankBanned(String inputString);
	
	
	@Query(value="SELECT * FROM   BNKBLWORD WHERE  BLWORD  like %?1% AND NVL(RECSTS,' ') <> 'D'" ,nativeQuery=true)
	 public List<BankBlWorld> getCheckBnakBeneBanned(String inputString); //
	
	
	//@Query(value="SELECT * FROM   BNKBLWORD WHERE  BLWORD  like '%?1%' AND NVL(RECSTS,' ') <> 'D'" ,nativeQuery=true)
		//@Query(value="SELECT * FROM   BankBlWorld WHERE  blword  like '%?1%' AND NVL(RECSTS,' ') <> 'D'" ,nativeQuery=true)
		//public List<BankBlWorld> getCheckBnakBeneBanned(@Param("inputString") String inputString); //
		
	
	/** added by Rabil  3rd Oct **/
	 @Query(value="SELECT * FROM   BNKBLWORD WHERE  BNKCOD=?1  AND NVL(RECSTS,' ') <> 'D'" ,nativeQuery=true)
	 public List<BankBlWorld> getbnkWordList(String inputString); //
}
