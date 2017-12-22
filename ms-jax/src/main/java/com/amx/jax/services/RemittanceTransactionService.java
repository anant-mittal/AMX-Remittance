package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.SourceOfIncomeDto;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.SourceOfIncomeView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.repository.ISourceOfIncomeDao;

@Service
@SuppressWarnings("rawtypes")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceTransactionService extends AbstractService {

	@Autowired
	IRemittanceTransactionDao remittanceTransactionDao;

	@Autowired
	RemittanceTransactionManager remittanceTxnManger;
	
	@Autowired
	ISourceOfIncomeDao sourceOfIncomeDao;
	

	public ApiResponse getRemittanceTransactionDetails(BigDecimal collectionDocumentNo, BigDecimal fYear,
			BigDecimal collectionDocumentCode) {

		List<RemittanceTransactionView> transctionDetail = remittanceTransactionDao
				.getRemittanceTransaction(collectionDocumentNo, fYear, collectionDocumentCode);
		ApiResponse response = getBlackApiResponse();
		if (transctionDetail.isEmpty()) {
			throw new GlobalException("Transaction details not avaliable");
		} else {
			response.getData().getValues().addAll(transctionDetail);
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("remittance_transaction");
		return response;

	}
	
	
	
	
	public ApiResponse getSourceOfIncome(BigDecimal languageId) {
		
		List<SourceOfIncomeView> sourceOfIncomeList  = sourceOfIncomeDao.getSourceofIncome(languageId);
		ApiResponse response = getBlackApiResponse();
		if(sourceOfIncomeList.isEmpty()) {
			throw new GlobalException("No data found");
		}else {
			response.getData().getValues().addAll(convertSourceOfIncome(sourceOfIncomeList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("sourceofincome");
		return response;
	}
	

	/*
	 * public ApiResponse convert(List<RemittanceTransactionView> transctionDetail){
	 * List<RemittanceReportBean> reportBean = new ArrayList<>();
	 * 
	 * }
	 */

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

	public ApiResponse validateRemittanceTransaction(RemittanceTransactionRequestModel model) {
		ApiResponse response = getBlackApiResponse();
		RemittanceTransactionResponsetModel responseModel = remittanceTxnManger.validateTransactionData(model);
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(model.getModelType());
		return response;

	}
	
	
	public List<SourceOfIncomeDto> convertSourceOfIncome(List<SourceOfIncomeView> sourceOfIncomeList){
		List<SourceOfIncomeDto> list = new ArrayList<>();
		for(SourceOfIncomeView model:sourceOfIncomeList) {
			SourceOfIncomeDto dto = new SourceOfIncomeDto();
			dto.setSourceofIncomeId(model.getSourceofIncomeId());
			dto.setShortDesc(model.getShortDesc());
			dto.setLanguageId(model.getLanguageId());
			dto.setDescription(model.getDescription());
			list.add(dto);
		}
		return list;
		
	}

	public ApiResponse saveApplication(RemittanceTransactionRequestModel model) {
		ApiResponse response = getBlackApiResponse();
		RemittanceApplicationResponseModel responseModel = remittanceTxnManger.saveApplication(model);
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(model.getModelType());
		return response;
	}

}
