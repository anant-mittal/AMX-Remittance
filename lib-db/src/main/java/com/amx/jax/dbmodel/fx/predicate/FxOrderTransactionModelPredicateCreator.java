package com.amx.jax.dbmodel.fx.predicate;

import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.fx.QFxOrderTransactionModel;
import com.amx.jax.model.request.fx.FcDeliveryBranchOrderSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Component
public class FxOrderTransactionModelPredicateCreator {

	public Predicate createFxOrderTransactionSearchPredicate(
			FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		QFxOrderTransactionModel qFxOrderTransactionModel = QFxOrderTransactionModel.fxOrderTransactionModel;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if (fcDeliveryBranchOrderSearchRequest.getCustomerId() != null) {
			booleanBuilder
					.and(qFxOrderTransactionModel.customerId.eq(fcDeliveryBranchOrderSearchRequest.getCustomerId()));
		}
		return booleanBuilder.getValue();
	}

}
