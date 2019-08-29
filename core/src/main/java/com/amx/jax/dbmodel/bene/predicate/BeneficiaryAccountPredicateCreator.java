package com.amx.jax.dbmodel.bene.predicate;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.bene.QBeneficaryAccount;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Component
public class BeneficiaryAccountPredicateCreator {

	public Predicate createBeneSearchPredicate(BeneAccountModel beneAccountModel, boolean isBangladeshBene) {
		QBeneficaryAccount qDataEntity = QBeneficaryAccount.beneficaryAccount;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if (beneAccountModel.getBankBranchId() != null && isBangladeshBene) {
			booleanBuilder.and(qDataEntity.bankBranchId.eq(beneAccountModel.getBankBranchId()));
		}
		if (beneAccountModel.getServiceProviderBranchId() != null) {
			booleanBuilder.and(qDataEntity.serviceProviderBranchId.eq(beneAccountModel.getServiceProviderBranchId()));
		}
		if (beneAccountModel.getServiceProviderId() != null) {
			booleanBuilder.and(qDataEntity.serviceProviderId.eq(beneAccountModel.getServiceProviderId()));
		}
		booleanBuilder.and(qDataEntity.beneficaryCountryId.eq(beneAccountModel.getBeneficaryCountryId()));
		booleanBuilder.and(qDataEntity.bankId.eq(beneAccountModel.getBankId()));
		booleanBuilder.and(qDataEntity.currencyId.eq(beneAccountModel.getCurrencyId()));
		if (beneAccountModel.getBankAccountNumber() != null) {
			booleanBuilder.and(qDataEntity.bankAccountNumber.eq(beneAccountModel.getBankAccountNumber()));
		}
		booleanBuilder.and(qDataEntity.isActive.eq(ConstantDocument.Yes));
		booleanBuilder.and(qDataEntity.serviceGroupId.eq(beneAccountModel.getServiceGroupId()));
		return booleanBuilder.getValue();
	}
	
	public Predicate createBeneSearchPredicateCash(BeneAccountModel beneAccountModel, boolean isBangladeshBene, 
			BigDecimal beneMasterSeqId) {
		QBeneficaryAccount qDataEntity = QBeneficaryAccount.beneficaryAccount;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if (beneAccountModel.getBankBranchId() != null && isBangladeshBene) {
			booleanBuilder.and(qDataEntity.bankBranchId.eq(beneAccountModel.getBankBranchId()));
		}
		if (beneAccountModel.getServiceProviderBranchId() != null) {
			booleanBuilder.and(qDataEntity.serviceProviderBranchId.eq(beneAccountModel.getServiceProviderBranchId()));
		}
		if (beneAccountModel.getServiceProviderId() != null) {
			booleanBuilder.and(qDataEntity.serviceProviderId.eq(beneAccountModel.getServiceProviderId()));
		}
		booleanBuilder.and(qDataEntity.beneficaryCountryId.eq(beneAccountModel.getBeneficaryCountryId()));
		booleanBuilder.and(qDataEntity.bankId.eq(beneAccountModel.getBankId()));
		booleanBuilder.and(qDataEntity.currencyId.eq(beneAccountModel.getCurrencyId()));
		if (beneAccountModel.getBankAccountNumber() != null) {
			booleanBuilder.and(qDataEntity.bankAccountNumber.eq(beneAccountModel.getBankAccountNumber()));
		}
		booleanBuilder.and(qDataEntity.isActive.eq(ConstantDocument.Yes));
		booleanBuilder.and(qDataEntity.serviceGroupId.eq(beneAccountModel.getServiceGroupId()));
		if (beneMasterSeqId != null) {
			booleanBuilder.and(qDataEntity.beneficaryMasterId.eq(beneMasterSeqId));
		} else {
			booleanBuilder.and(qDataEntity.beneficaryMasterId.isNull());
		}
		return booleanBuilder.getValue();
	}
}
