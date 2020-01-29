package com.amx.jax.dbmodel.bene.predicate;

import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.bene.QBeneficaryMaster;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Component
public class BeneficiaryPersonalDetailPredicateCreator {

	public Predicate createBeneSearchPredicate(BenePersonalDetailModel benePersonalDetailModel) {
		QBeneficaryMaster qDataEntity = QBeneficaryMaster.beneficaryMaster;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if (StringUtils.isNotBlank(benePersonalDetailModel.getFirstName())) {
			booleanBuilder.and(qDataEntity.firstName.eq(benePersonalDetailModel.getFirstName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getFifthName())) {
			booleanBuilder.and(qDataEntity.fifthName.eq(benePersonalDetailModel.getFifthName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getFourthName())) {
			booleanBuilder.and(qDataEntity.fourthName.eq(benePersonalDetailModel.getFourthName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalFifthName())) {
			booleanBuilder.and(qDataEntity.localFifthName.eq(benePersonalDetailModel.getLocalFifthName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalFirstName())) {
			booleanBuilder.and(qDataEntity.localFirstName.eq(benePersonalDetailModel.getLocalFirstName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalFourthName())) {
			booleanBuilder.and(qDataEntity.localFourthName.eq(benePersonalDetailModel.getLocalFourthName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalSecondName())) {
			booleanBuilder.and(qDataEntity.localSecondName.eq(benePersonalDetailModel.getLocalSecondName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalThirdName())) {
			booleanBuilder.and(qDataEntity.localThirdName.eq(benePersonalDetailModel.getLocalThirdName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getSecondName())) {
			booleanBuilder.and(qDataEntity.secondName.eq(benePersonalDetailModel.getSecondName()));
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getThirdName())) {
			booleanBuilder.and(qDataEntity.thirdName.eq(benePersonalDetailModel.getThirdName()));
		}

		booleanBuilder.and(qDataEntity.fsCountryMaster.eq(benePersonalDetailModel.getCountryId()));

		return booleanBuilder.getValue();
	}
}
