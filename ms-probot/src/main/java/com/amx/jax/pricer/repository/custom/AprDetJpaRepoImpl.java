package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;

@Repository
public class AprDetJpaRepoImpl implements AprDetJpaRepoCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<ExchangeRateMasterApprovalDet> findByPredicateIn(List<BigDecimal> predicateIn) {

		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
		CriteriaQuery<ExchangeRateMasterApprovalDet> cQuery = cBuilder.createQuery(ExchangeRateMasterApprovalDet.class);

		Root<ExchangeRateMasterApprovalDet> root = cQuery.from(ExchangeRateMasterApprovalDet.class);

		In<BigDecimal> inClause = cBuilder.in(root.get("currencyId"));

		for (BigDecimal pred : predicateIn) {
			inClause.value(pred);
		}

		Predicate cBranch = cBuilder.equal(root.get("countryBranchId"), new BigDecimal(56));

		cQuery.select(root).where(cBranch, inClause);

		return em.createQuery(cQuery).getResultList();
	}

	@Override
	public List<ExchangeRateMasterApprovalDet> getExchangeRatesForPredicates(BigDecimal countryId, BigDecimal currencyId,
			BigDecimal bankId, BigDecimal serviceIndId, BigDecimal countryBranchId, Pageable pageable) {

		CriteriaBuilder cBuilder = em.getCriteriaBuilder();
		CriteriaQuery<ExchangeRateMasterApprovalDet> cQuery = cBuilder.createQuery(ExchangeRateMasterApprovalDet.class);

		Root<ExchangeRateMasterApprovalDet> root = cQuery.from(ExchangeRateMasterApprovalDet.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (countryId != null) {
			predicates.add(cBuilder.equal(root.get("countryId"), countryId));
		}

		if (currencyId != null) {
			predicates.add(cBuilder.equal(root.get("currencyId"), currencyId));
		}

		if (bankId != null) {
			predicates.add(cBuilder.equal(root.get("bankMaster"), bankId));
		}

		if (serviceIndId != null) {
			predicates.add(cBuilder.equal(root.get("serviceId"), serviceIndId));
		}

		if (countryBranchId != null) {
			predicates.add(cBuilder.equal(root.get("countryBranchId"), countryBranchId));
		}

		cQuery.select(root).where(predicates.toArray(new Predicate[predicates.size()]));

		return em.createQuery(cQuery).setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize())
				.getResultList();
	}

}
