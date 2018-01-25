package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ITransactionHistroyDAO;

@Service
@SuppressWarnings("rawtypes")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TransactionHistroyService extends AbstractService {
	
	

	@Autowired
	ITransactionHistroyDAO transactionHistroyDao;
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;
	
	@Autowired
	BeneficiaryCheckService beneCheckService;
	
	
	@Autowired
	MetaData metaData;

	public ApiResponse getTransactionHistroy(BigDecimal cutomerReference, BigDecimal docfyr) {
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao.getTransactionHistroy(cutomerReference);
		ApiResponse response = getBlackApiResponse();
		if (trnxHisList.isEmpty()) {
			throw new GlobalException("Transaction histroy not found",JaxError.TRANSACTION_HISTORY_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convert(trnxHisList));
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("trnxHist");
		return response;
	}

	public ApiResponse getTransactionHistroyByDocumentNumber(BigDecimal cutomerReference, BigDecimal docfyr,
			BigDecimal docNumber) { // , String fromDate,String toDate
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao
				.getTransactionHistroyByDocumnet(cutomerReference, docfyr, docNumber); // , fromDate, toDate
		ApiResponse response = getBlackApiResponse();
		if (trnxHisList.isEmpty()) {
			throw new GlobalException("Transaction histroy not found",JaxError.TRANSACTION_HISTORY_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convert(trnxHisList));
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("trnxHist");
		return response;
	}

	public TransactionHistroyDTO getTransactionHistoryDto(BigDecimal cutomerReference, BigDecimal remittanceDocfyr,
			BigDecimal remittancedocNumber) {
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao
				.getTransactionHistroyByDocumnet(cutomerReference, remittanceDocfyr, remittancedocNumber); // ,
																											// fromDate,
																											// toDate
		return convert(trnxHisList).get(0);
	}

	public ApiResponse getTransactionHistroyDateWise(BigDecimal cutomerReference, BigDecimal docfyr, String fromDate,
			String toDate) {
		List<CustomerRemittanceTransactionView> trnxHisList;
		if (docfyr != null) {
			trnxHisList = transactionHistroyDao.getTransactionHistroyDateWise(cutomerReference, docfyr, fromDate,
					toDate);
		} else {
			trnxHisList = transactionHistroyDao.getTransactionHistroyDateWise(cutomerReference, fromDate, toDate);
		}
		ApiResponse response = getBlackApiResponse();
		if (trnxHisList.isEmpty()) {
			throw new GlobalException("Transaction histroy not found",JaxError.TRANSACTION_HISTORY_NOT_FOUND);
		} else {
			response.getData().getValues().addAll(convert(trnxHisList));
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("trnxHist");
		return response;
	}

	private List<TransactionHistroyDTO> convert(List<CustomerRemittanceTransactionView> trnxHist) {
		System.out.println("Application country Id :"+metaData.getCountryId());
		List<TransactionHistroyDTO> list = new ArrayList<>();
		for (CustomerRemittanceTransactionView hist : trnxHist) {
			BeneficiaryListDTO beneDtoCheck = null; 
			TransactionHistroyDTO model = new TransactionHistroyDTO();
			model.setBeneficaryAccountNumber(hist.getBeneficaryAccountNumber());
			model.setForeignTransactionAmount(hist.getForeignTransactionAmount());
			model.setTransactionStatusDesc(hist.getTransactionStatusDesc());
			model.setTransactionTypeDesc(hist.getTransactionTypeDesc());

			model.setBeneficaryBankName(hist.getBeneficaryBankName());
			model.setBeneficaryBranchName(hist.getBeneficaryBranchName());
			model.setBeneficaryCorespondingBankName(hist.getBeneficaryCorespondingBankName());
			model.setBeneficaryName(hist.getBeneficaryName());

			model.setCollectionDocumentCode(hist.getCollectionDocumentCode());
			model.setCollectionDocumentFinYear(hist.getCollectionDocumentFinYear());
			model.setCollectionDocumentNo(hist.getCollectionDocumentNo());
			model.setCurrencyQuoteName(hist.getCurrencyQuoteName());
			model.setCustomerReference(hist.getCustomerReference());
			model.setForeignCurrencyCode(hist.getForeignCurrencyCode());

			model.setDocumentDate(hist.getDocumentDate());
			model.setDocumentFinanceYear(hist.getDocumentFinanceYear());
			model.setDocumentNumber(hist.getDocumentNumber());
			model.setBranchDesc(hist.getBranchDesc());
			model.setServiceDescription(hist.getServiceDescription());
			model.setForeignTransactionAmount(hist.getForeignTransactionAmount());
			model.setIdno(hist.getIdno());
			model.setTrnxIdNumber(hist.getDocumentFinanceYear() + "/" + hist.getDocumentNumber());
			model.setCustomerId(hist.getCustomerId());
			model.setBeneficiaryRelationSeqId(hist.getBeneficiaryRelationSeqId());
			model.setLocalTrnxAmount(hist.getLocalTrnxAmount());
			model.setSourceOfIncomeId(hist.getSourceOfIncomeId());
			BenificiaryListView beneViewModel = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(hist.getCustomerId(),metaData.getCountryId(),hist.getBeneficiaryRelationSeqId());
			if(beneViewModel!=null){
				 beneDtoCheck=beneCheckService.beneCheck(convertBeneModelToDto(beneViewModel));
			}
			if(beneDtoCheck != null){
				model.setBeneficiaryErrorStatus(beneDtoCheck.getBeneficiaryErrorStatus());
			}
			if (!StringUtils.isBlank(hist.getBeneficaryCorespondingBankName())
					&& !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.WU)) {
				list.add(model);
			}

		}
		return list;
	}
	
	
	
	private BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			System.out.println("Exception e:"+e.getMessage());
		}
		return dto;
	}
	
	
	
	
	
	

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
