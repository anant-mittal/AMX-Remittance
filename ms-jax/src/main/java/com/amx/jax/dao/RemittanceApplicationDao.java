package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.IFlexFiledView;
import com.amx.jax.repository.IPlaceOrderDao;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;
import com.amx.jax.service.FinancialService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceApplicationDao {

	private Logger logger = Logger.getLogger(RemittanceApplicationDao.class);

	@Autowired
	RemittanceApplicationRepository appRepo;

	@Autowired
	RemittanceApplicationBeneRepository appBeneRepo;

	@Autowired
	AdditionalInstructionDataRepository addlInstDataRepo;

	@Autowired
	RemittanceApplicationManager remitApplManager;

	@Autowired
	FinancialService finanacialService;

	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;
	@Autowired
	IFlexFiledView IFlexFiledView;
    @Autowired
    IPlaceOrderDao placeOrderdao;
    
	@Transactional
	public void saveAllApplicationData(RemittanceApplication app, RemittanceAppBenificiary appBene,
			List<AdditionalInstructionData> additionalInstrumentData) {

		appRepo.save(app);
		appBeneRepo.save(appBene);
		addlInstDataRepo.save(additionalInstrumentData);
		logger.info("Application saved in the database, docNo: " + app.getDocumentNo());
	}

	public RemittanceTransactionView getRemittanceTransactionView(BigDecimal documentNumber, BigDecimal finYear) {
		RemittanceTransactionView remittanceTransactionView = appRepo.fetchRemitApplTrnxView(documentNumber, finYear);
		return remittanceTransactionView;

	}

	public RemittanceApplication getApplication(BigDecimal documentNumber, BigDecimal finYear) {
		RemittanceApplication remittanceTransactionView = appRepo.fetchRemitApplTrnx(documentNumber, finYear);
		return remittanceTransactionView;
	}

	public RemittanceTransaction getRemittanceTransaction(BigDecimal applicationDocumentNumber,
			BigDecimal applicationfinYear) {
		RemittanceTransaction remittanceTransactionView = null;
		if (applicationDocumentNumber != null && applicationfinYear != null) {
			remittanceTransactionView = remittanceTransactionRepository
					.findByapplicationDocumentNoAndApplicationdocumentFinancialyear(applicationDocumentNumber,
							applicationfinYear);
		}
		return remittanceTransactionView;
	}
	
	public RemittanceTransaction getRemittanceTransactionByRemitDocNo(BigDecimal remittanceDocumentNumber,
			BigDecimal remittanceFinYear) {
		RemittanceTransaction remittanceTransactionView = null;
		if (remittanceDocumentNumber != null && remittanceFinYear != null) {
			remittanceTransactionView = remittanceTransactionRepository
					.findByDocumentNoAndDocumentFinancialyear(remittanceDocumentNumber, remittanceFinYear);
		}
		return remittanceTransactionView;
	}
	
	public List<FlexFiledView> getFlexFields() {
		return (List<FlexFiledView>) IFlexFiledView.findAll();
	}

	public void updatePlaceOrder(RemittanceTransactionRequestModel model, RemittanceApplication remittanceApplication) {

		// to update place order status we update applicaiton id in placeorder table
		if (model.getPlaceOrderId() != null) {
			List<PlaceOrder> poList = placeOrderdao.getPlaceOrderForId(model.getPlaceOrderId());
			PlaceOrder po = null;
			if (poList != null && poList.size() != 0) {
				po = poList.get(0);
				po.setRemittanceApplicationId(remittanceApplication.getRemittanceApplicationId());
				// po.setIsActive("C");
				placeOrderdao.save(po);
			} else {
				logger.info("Place Order not found for place_order_id: " + model.getPlaceOrderId());
				throw new GlobalException(JaxError.PLACE_ORDER_NOT_ACTIVE_OR_EXPIRED, "The order is not available");
			}
			logger.info("Place Order updated for place_order_id: " + model.getPlaceOrderId());
		}

	}
	
	public RemittanceTransaction getRemittanceTransactionById(BigDecimal remittanceTransactionId) {
		return remittanceTransactionRepository.findOne(remittanceTransactionId);
	}

	public RemittanceApplication getApplication(BigDecimal remittanceApplicationId) {
		return appRepo.findOne(remittanceApplicationId);
	}
}
