package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ITransactionHistroyDAO;

@Service
@SuppressWarnings("rawtypes")
public class TransactionHistroyService extends AbstractService {
	
	
	
	@Autowired
	ITransactionHistroyDAO transactionHistroyDao;
	
	
	public ApiResponse getTransactionHistroy(BigDecimal cutomerReference,BigDecimal docfyr){ 
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao.getTransactionHistroy(cutomerReference); 
		ApiResponse response = getBlackApiResponse();
		if(trnxHisList.isEmpty()) {
			throw new GlobalException("Transaction histroy not found");
		}else {
		response.getData().getValues().addAll(convert(trnxHisList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("trnxHist");
		return response;
	}

	
	public ApiResponse getTransactionHistroyByDocumentNumber(BigDecimal cutomerReference,BigDecimal docfyr, BigDecimal docNumber){ //, String fromDate,String  toDate
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao.getTransactionHistroyByDocumnet(cutomerReference, docfyr, docNumber); //, fromDate, toDate
		ApiResponse response = getBlackApiResponse();
		if(trnxHisList.isEmpty()) {
			throw new GlobalException("Transaction histroy not found");
		}else {
		response.getData().getValues().addAll(convert(trnxHisList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("trnxHist");
		return response;
	}
	
	
	
	
	public ApiResponse getTransactionHistroyDateWise(BigDecimal cutomerReference,BigDecimal docfyr,String fromDate,String  toDate) {
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao.getTransactionHistroyDateWise(cutomerReference, docfyr,fromDate, toDate);
		ApiResponse response = getBlackApiResponse();
		if(trnxHisList.isEmpty()) {
			throw new GlobalException("Transaction histroy not found");
		}else {
		response.getData().getValues().addAll(convert(trnxHisList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("trnxHist");
		return response;
	}
	

	
	
	private List<TransactionHistroyDTO> convert(List<CustomerRemittanceTransactionView> trnxHist) {
		List<TransactionHistroyDTO> list = new ArrayList<>();
		for (CustomerRemittanceTransactionView hist : trnxHist) {
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
			model.setTrnxIdNumber(hist.getDocumentFinanceYear()+"/"+hist.getDocumentNumber());
			model.setCustomerId(hist.getCustomerId());
			
			list.add(model);
		}
		return list;
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
