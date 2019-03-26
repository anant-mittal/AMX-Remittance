package com.amx.jax.notification.fx;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.JaxStompClient;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dict.AmxEnums.FxOrderStatus;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;

@Component
public class FcSaleEventManager {

	@Autowired
	FcSaleApplicationDao fcSaleApplicationDao;
	@Autowired
	JaxStompClient jaxStompClient;
	@Autowired
	AuditService auditService;

	public void logStatusChangeAuditEvent(BigDecimal deliveryDetailSeqId, String oldOrderStatus) {

		FxDeliveryDetailsModel deliveryDetailModel = fcSaleApplicationDao.getDeliveryDetailModel(deliveryDetailSeqId);

		jaxStompClient.publishFxOrderStatusChange(deliveryDetailModel.getColDocNo(), deliveryDetailModel.getColDocFyr(),
				FxOrderStatus.valueOf(deliveryDetailModel.getOrderStatus()));

		CActivityEvent audit = new CActivityEvent(Type.FC_UPDATE, deliveryDetailModel.getDeleviryDelSeqId())
				.field("STATUS").from(oldOrderStatus).to(deliveryDetailModel.getOrderStatus());
		auditService.log(audit);

	}
}
