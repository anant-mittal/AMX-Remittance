package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;

@Transactional
public interface ViewExRoutingMatrixRepository extends CrudRepository<ViewExRoutingMatrix, String> {

	@Query("select matrix from ViewExRoutingMatrix matrix where matrix.applicationCountryId=?1 and matrix.beneCountryId=?2"
			+ " and matrix.beneBankId=?3 and matrix.beneBankBranchId=?4 and matrix.currencyId=?5"
			+ " and matrix.serviceGroupCode=?6")
	public List<ViewExRoutingMatrix> findRoutingMatrix(BigDecimal applicationCountryId, BigDecimal beneCountryId,
			BigDecimal beneBankId, BigDecimal beneBankBranchId, BigDecimal currencyId, String serviceGroupCode);

	@Query("select matrix from ViewExRoutingMatrix matrix where matrix.applicationCountryId=?1 and matrix.beneCountryId=?2"
			+ " and matrix.beneBankId=?3 and matrix.beneBankBranchId=?4 and matrix.currencyId=?5"
			+ " and matrix.serviceGroupCode=?6 and matrix.routingBankId=?7"
			+ " and ( matrix.bankBranchId=?8 or matrix.branchApplicability='ALL' )")
	public List<ViewExRoutingMatrix> findRoutingMatrix(BigDecimal applicationCountryId, BigDecimal beneCountryId,
			BigDecimal beneBankId, BigDecimal beneBankBranchId, BigDecimal currencyId, String serviceGroupCode,
			BigDecimal routingBankId, BigDecimal bankBranchId);

}
