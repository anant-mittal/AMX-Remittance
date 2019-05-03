package com.amx.jax.dbmodel.fx.predicate;

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;
import com.amx.jax.dbmodel.fx.QFxOrderTransactionModel;
import com.amx.jax.model.request.fx.FcDeliveryBranchOrderSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;


@Component
public class FxOrderTransactionModelPredicateCreator {

	public Predicate searchOrderPredicate(
			FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		QFxOrderTransactionModel qFxOrderTransactionModel = QFxOrderTransactionModel.fxOrderTransactionModel;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		if (fcDeliveryBranchOrderSearchRequest.getCustomerId() != null) {
			booleanBuilder
					.and(qFxOrderTransactionModel.customerId.eq(fcDeliveryBranchOrderSearchRequest.getCustomerId()));
		}
		if (fcDeliveryBranchOrderSearchRequest.getCountryBranchName() != null) {
			booleanBuilder
					.and(qFxOrderTransactionModel.branchDesc.eq(fcDeliveryBranchOrderSearchRequest.getCountryBranchName()));
		}
		if (fcDeliveryBranchOrderSearchRequest.getOrderStatusCode() != null) {
			booleanBuilder
					.and(qFxOrderTransactionModel.orderStatusCode.eq(fcDeliveryBranchOrderSearchRequest.getOrderStatusCode()));
		}
		if (fcDeliveryBranchOrderSearchRequest.getOrderId() != null) {
			booleanBuilder
					.and(qFxOrderTransactionModel.transactionReferenceNo.eq(fcDeliveryBranchOrderSearchRequest.getOrderId()));
		}
		
		Date toDate = Calendar.getInstance().getTime();  
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date fromDate = cal.getTime();    
         
        booleanBuilder.and(qFxOrderTransactionModel.createdDateAlt.between(fromDate, toDate));

		return booleanBuilder.getValue();
	}
	
}
