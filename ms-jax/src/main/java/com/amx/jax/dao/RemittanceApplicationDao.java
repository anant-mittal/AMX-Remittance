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

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.controller.RemittanceController;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
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

	@Transactional
	public void saveAllApplicationData(RemittanceApplication app, RemittanceAppBenificiary appBene,
			List<AdditionalInstructionData> additionalInstrumentData) {

		appRepo.save(app);
		appBeneRepo.save(appBene);
		addlInstDataRepo.save(additionalInstrumentData);
		logger.info("Application saved in the database, docNo: " + app.getDocumentNo());
	}

	public RemittanceTransactionView getRemittanceTransactionView(BigDecimal documentNumber, BigDecimal finYear) {
		RemittanceTransactionView remittanceTransactionView = appRepo.fetchRemitApplTrnxView(documentNumber, finYear,
				ConstantDocument.REMITTANCE_DOCUMENT_ID);
		return remittanceTransactionView;

	}

	public RemittanceApplication getApplication(BigDecimal documentNumber, BigDecimal finYear) {
		RemittanceApplication remittanceTransactionView = appRepo.fetchRemitApplTrnx(documentNumber, finYear);
		return remittanceTransactionView;

	}
}
