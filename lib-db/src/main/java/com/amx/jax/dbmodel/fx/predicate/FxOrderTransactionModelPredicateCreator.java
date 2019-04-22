package com.amx.jax.dbmodel.fx.predicate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
		
		
		Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
        String toDate = dateFormat.format(date);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date todate1 = cal.getTime();    
        String fromdate = dateFormat.format(todate1);
        
        System.out.println("fromdate" +fromdate);
          
		
		booleanBuilder.and(qFxOrderTransactionModel.createdDate.between(fromdate, toDate));

		return booleanBuilder.getValue();
	}
	
}
