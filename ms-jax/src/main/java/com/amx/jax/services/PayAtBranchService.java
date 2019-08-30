package com.amx.jax.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.branchremittance.manager.BranchRemittanceApplManager;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.PayAtBranchDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.PayAtBranchTrnxModel;
import com.amx.jax.dbmodel.PaymentModesModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.postman.PostManService;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.response.payatbranch.PayAtBranchTrnxListDTO;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.userservice.dao.CustomerDao;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class PayAtBranchService {
	@Autowired
	PayAtBranchDao payAtBranchDao;

	@Autowired
	MetaData metaData;

	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;

	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;

	@Autowired
	BranchRemittanceApplManager branchRemitApplManager;

	@Autowired
	CustomerDao custDao;

	@Autowired
	PostManService postManService;

	@Autowired
	CurrencyMasterService currencyMasterService;

	@Autowired
	JaxTenantProperties jaxTenantProperties;

	Logger logger = Logger.getLogger(PayAtBranchService.class);

	public List<ResourceDTO> getPaymentModes() {
		List<PaymentModesModel> paymentModesList = payAtBranchDao.getPaymentModes();
		List<ResourceDTO> resourceDtoList = ResourceDTO.createList(paymentModesList);
		return resourceDtoList;
	}

	public List<PayAtBranchTrnxListDTO> getPbTrnxList() {
		List<PayAtBranchTrnxModel> pbTrnxList = payAtBranchDao.getPbTrnxList();
		if (pbTrnxList.isEmpty()) {
			throw new GlobalException("No wire transfer transaction found");
		}

		return convertPbTrnxList(pbTrnxList);
	}

	public static List<PayAtBranchTrnxListDTO> convertPbTrnxList(List<PayAtBranchTrnxModel> pbTrnxList) {
		List<PayAtBranchTrnxListDTO> output = new ArrayList<>();
		for (PayAtBranchTrnxModel payAtBranchTrnxModel : pbTrnxList) {
			PayAtBranchTrnxListDTO pbTrnxListDTO = new PayAtBranchTrnxListDTO();
			pbTrnxListDTO.setTransactionId(payAtBranchTrnxModel.getTransactionId());
			pbTrnxListDTO.setStatus(payAtBranchTrnxModel.getWireTransferStatus());
			pbTrnxListDTO.setStatusDesc(payAtBranchTrnxModel.getStatusDescription());
			pbTrnxListDTO.setBank(payAtBranchTrnxModel.getBankName());
			pbTrnxListDTO.setAccountNo(payAtBranchTrnxModel.getBankAccountNo());
			pbTrnxListDTO.setAmount(payAtBranchTrnxModel.getNetAmount());
			pbTrnxListDTO.setDocumentDate(payAtBranchTrnxModel.getDocumentDate());
			pbTrnxListDTO.setApplIsActive(payAtBranchTrnxModel.getApplIsActive());
			pbTrnxListDTO.setForeignCurrencyDescription(payAtBranchTrnxModel.getForeignCurrencyDescription());
			if (payAtBranchTrnxModel.getWireTransferStatus().equalsIgnoreCase(ConstantDocument.WT_STATUS_PAID)) {
				pbTrnxListDTO.setTransactionDocumentNo(payAtBranchTrnxModel.getTransactionDocumentNo());
			}
			output.add(pbTrnxListDTO);

		}
		return output;

	}

	public List<PayAtBranchTrnxListDTO> getPbTrnxListBranch() {
		List<PayAtBranchTrnxModel> pbTrnxList = payAtBranchDao.getPbTrnxListBranch();

		return convertPbTrnxListBranch(pbTrnxList);
	}

	public static List<PayAtBranchTrnxListDTO> convertPbTrnxListBranch(List<PayAtBranchTrnxModel> pbTrnxList) {
		List<PayAtBranchTrnxListDTO> output = new ArrayList<>();
		for (PayAtBranchTrnxModel payAtBranchTrnxModel : pbTrnxList) {
			PayAtBranchTrnxListDTO pbTrnxListDTO = new PayAtBranchTrnxListDTO();
			pbTrnxListDTO.setTransactionId(payAtBranchTrnxModel.getTransactionId());
			// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy
			// hh:mm:ss");
			// String confDate = simpleDateFormat.format();
			pbTrnxListDTO.setConfirmDate(payAtBranchTrnxModel.getConfirmDate());

			pbTrnxListDTO.setStatus(payAtBranchTrnxModel.getWireTransferStatus());
			pbTrnxListDTO.setStatusDesc(payAtBranchTrnxModel.getStatusDescription());
			pbTrnxListDTO.setBank(payAtBranchTrnxModel.getBankName());
			pbTrnxListDTO.setAccountNo(payAtBranchTrnxModel.getBankAccountNo());
			pbTrnxListDTO.setAmount(payAtBranchTrnxModel.getNetAmount());
			pbTrnxListDTO.setCustomerName(payAtBranchTrnxModel.getCustomerName());
			pbTrnxListDTO.setExchangeRate(payAtBranchTrnxModel.getExchangeRate());
			pbTrnxListDTO.setForeignAmount(payAtBranchTrnxModel.getForeignTransactionAmount());
			pbTrnxListDTO.setIdentityInt(payAtBranchTrnxModel.getIdentityInt());
			pbTrnxListDTO.setApplIsActive(payAtBranchTrnxModel.getApplIsActive());
			pbTrnxListDTO.setIdentityTypeId(payAtBranchTrnxModel.getIdentityTypeId());
			pbTrnxListDTO.setForeignCurrencyDescription(payAtBranchTrnxModel.getForeignCurrencyDescription());
			if (payAtBranchTrnxModel.getWireTransferStatus().equalsIgnoreCase(ConstantDocument.WT_STATUS_PAID)) {
				pbTrnxListDTO.setTransactionDocumentNo(payAtBranchTrnxModel.getTransactionDocumentNo());
			}

			output.add(pbTrnxListDTO);

		}
		return output;

	}

}
