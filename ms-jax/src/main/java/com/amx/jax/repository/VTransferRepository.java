package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewTransfer;

@Transactional
public interface VTransferRepository extends CrudRepository<ViewTransfer, String> {

	@Query(nativeQuery = true, value = "select * from v_TRANSFER WHERE  CUSREF = ?1"
			+ "            AND    ACYYMM = TRUNC(SYSDATE,'MONTH')" + "            AND    NVL(CANSTS,' ') = ' '")
	public List<ViewTransfer> findBycusRef(BigDecimal cusRef);
}
