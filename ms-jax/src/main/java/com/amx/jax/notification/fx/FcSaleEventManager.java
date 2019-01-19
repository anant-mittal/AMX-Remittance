package com.amx.jax.notification.fx;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.auditlog.FcSaleOrderStatusChangeAuditEvent;
import com.amx.jax.auditlog.JaxAuditEvent;
import com.amx.jax.client.JaxStompClient;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dict.AmxEnums.FxOrderStatus;
import com.amx.jax.logger.AuditService;

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
		FcSaleOrderStatusChangeAuditEvent event = new FcSaleOrderStatusChangeAuditEvent(deliveryDetailModel,
				oldOrderStatus, JaxAuditEvent.Type.FC_SALE_UPDATE_ORDER_STATUS);
		auditService.log(event);
	}
}
