package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

		// List<Predicate> predicates = new ArrayList<>();

		// predicates.add(cBuilder.in(root.get("SLNO")));
		
		In<BigDecimal> inClause = cBuilder.in(root.get("currencyId"));

		for (BigDecimal pred : predicateIn) {
			inClause.value(pred);
		}
		
		Predicate cBranch = cBuilder.equal(root.get("countryBranchId"), new BigDecimal(56));

		//cQuery.select(root).where(root.in(predicateIn));

		cQuery.select(root).where(cBranch, inClause);
		
		// cq.where(predicates.toArray(new Predicate[0]));

		return em.createQuery(cQuery).getResultList();
	}

}
