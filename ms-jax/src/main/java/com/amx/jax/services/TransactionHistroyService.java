package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.TransactionHistoryDto;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionHistoryView;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.TransactionHistoryDAO;

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
	TransactionHistoryDAO transactionHistroyDAO;


	@Autowired
	MetaData metaData;
	
	Logger logger = LoggerFactory.getLogger(TransactionHistroyDTO.class);

	public ApiResponse getTransactionHistroy(BigDecimal cutomerReference, BigDecimal docfyr) {
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao
				.getTransactionHistroy(cutomerReference);
		ApiResponse response = getBlackApiResponse();
		if (trnxHisList.isEmpty()) {
			throw new GlobalException(JaxError.TRANSACTION_HISTORY_NOT_FOUND,"Transaction histroy not found");
		} else {
		    
			Set<BigDecimal> beneRelSeqSet = trnxHisList.stream().map(emp -> emp.getBeneficiaryRelationSeqId())
					.collect(Collectors.toSet());
			List<BenificiaryListView> beneList = beneficiaryOnlineDao.getBeneficiaryRelationShipSeqIds(
					metaData.getCustomerId(), new ArrayList<BigDecimal>(beneRelSeqSet));
			Map<BigDecimal, BenificiaryListView> beneMap = beneList.stream()
					.collect(Collectors.toMap(BenificiaryListView::getBeneficiaryRelationShipSeqId, x -> x));
		    
		    response.getData().getValues().addAll(convert(trnxHisList,beneMap));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("trnxHist");
		return response;
	}


    public ApiResponse getTransactionHistroyByDocumentNumber(BigDecimal cutomerReference, BigDecimal docfyr,
			BigDecimal docNumber) { 
		List<CustomerRemittanceTransactionView> trnxList = transactionHistroyDao
				.getTransactionHistroyByDocumnet(cutomerReference, docfyr, docNumber); 
		ApiResponse response = getBlackApiResponse();
		if (trnxList.isEmpty()) {
			logger.debug("values passing in second query:" +docfyr+ "docfyr" +docNumber+ "docnumber" +cutomerReference+ "cutomerReference");
			List<CustomerRemittanceTransactionHistoryView> trnxHistList = transactionHistroyDAO
					.getTransactionHistroyByDocumnet(cutomerReference, docfyr, docNumber);			
			response.getData().getValues().addAll(convertv2(trnxHistList));
			response.setResponseStatus(ResponseStatus.OK);
			if(trnxHistList.isEmpty()) {
				throw new GlobalException(JaxError.TRANSACTION_HISTORY_NOT_FOUND,"Transaction histroy not found");
			}
				
		} else {
			response.getData().getValues().addAll(convert(trnxList));
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("trnxHist");
		return response;
	}

	public TransactionHistroyDTO getTransactionHistoryDto(BigDecimal cutomerReference, BigDecimal remittanceDocfyr,
			BigDecimal remittancedocNumber) {
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao
				.getTransactionHistroyByDocumnet(cutomerReference, remittanceDocfyr, remittancedocNumber); 
		if(trnxHisList.isEmpty()) {
			getTransactionHistoryDTO(cutomerReference, remittanceDocfyr, remittancedocNumber);
		}
		return convert(trnxHisList).get(0);
	}

	public TransactionHistoryDto getTransactionHistoryDTO(BigDecimal cutomerReference, BigDecimal remittanceDocfyr,
			BigDecimal remittancedocNumber) {
		
		List<CustomerRemittanceTransactionHistoryView> trnxHistList = transactionHistroyDAO.getTransactionHistroyByDocumnet(cutomerReference, remittanceDocfyr, remittancedocNumber);
				
		return convertv2(trnxHistList).get(0);
	}
		
	public ApiResponse getTransactionHistroyDateWise(BigDecimal cutomerReference, BigDecimal docfyr, String fromDate,
			String toDate) {
		List<CustomerRemittanceTransactionView> trnxList;
		List<CustomerRemittanceTransactionHistoryView> trnxHistoryList = new ArrayList<CustomerRemittanceTransactionHistoryView>();
		ApiResponse response = getBlackApiResponse();
		if (docfyr != null) {
			trnxList = transactionHistroyDao.getTransactionHistroyDocfyrDateWise(cutomerReference, docfyr, fromDate,
					toDate);
			if (!trnxList.isEmpty()) {
				response.getData().getValues().addAll(convert(trnxList));
				response.setResponseStatus(ResponseStatus.OK);
			}
			trnxHistoryList = transactionHistroyDAO.getTransactionHistroyDocfyrAndDateWise(cutomerReference, docfyr, fromDate,
					toDate);
			if (!trnxHistoryList.isEmpty()) {

				response.getData().getValues().addAll(convertv2(trnxHistoryList));
				response.setResponseStatus(ResponseStatus.OK);

			}

		} else {
			trnxList = transactionHistroyDao.getTransactionHistroyDateWise(cutomerReference, fromDate, toDate);

			if (!trnxList.isEmpty()) {
				response.getData().getValues().addAll(convert(trnxList));
				response.setResponseStatus(ResponseStatus.OK);
			}
			trnxHistoryList = transactionHistroyDAO.getTransactionHistoryDateWise(cutomerReference, fromDate, toDate);
			if (!trnxHistoryList.isEmpty()) {
				response.getData().getValues().addAll(convertv2(trnxHistoryList));
				response.setResponseStatus(ResponseStatus.OK);
			}
		}
		if (trnxList.isEmpty() && trnxHistoryList.isEmpty()) {
			throw new GlobalException(JaxError.TRANSACTION_HISTORY_NOT_FOUND, "Transaction histroy not found");
		} else {
			response.getData().setType("trnxHist");
			return response;
		}
	}

	   private List<TransactionHistroyDTO> convert(List<CustomerRemittanceTransactionView> trnxHist) {
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
	            model.setTransactionReference(getTransactionReferece(hist));
	            
			BenificiaryListView beneViewModel = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(
					hist.getCustomerId(), metaData.getCountryId(), hist.getBeneficiaryRelationSeqId());
	            if(beneViewModel!=null){
	                 beneDtoCheck=beneCheckService.beneCheck(convertBeneModelToDto(beneViewModel));
	            }
	            if(beneDtoCheck != null){
	                model.setBeneficiaryErrorStatus(beneDtoCheck.getBeneficiaryErrorStatus());
	            }
	            if (!StringUtils.isBlank(hist.getBeneficaryCorespondingBankName()) 
	                && !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.WU) 
	                && !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.MONEY)) {
	                list.add(model);
	            }

	        }
	        return list;
	    }
	   
	   private List<TransactionHistoryDto> convertv2(List<CustomerRemittanceTransactionHistoryView> trnxHist) {
	        List<TransactionHistoryDto> list = new ArrayList<>();
	        for (CustomerRemittanceTransactionHistoryView hist : trnxHist) {
	            BeneficiaryListDTO beneDtoCheck = null; 
	            TransactionHistoryDto model = new TransactionHistoryDto();
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
	            model.setTransactionReference(getTransactionReference(hist));
	            
			BenificiaryListView beneViewModel = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(
					hist.getCustomerId(), metaData.getCountryId(), hist.getBeneficiaryRelationSeqId());
	            if(beneViewModel!=null){
	                 beneDtoCheck=beneCheckService.beneCheck(convertBeneModelToDto(beneViewModel));
	            }
	            if(beneDtoCheck != null){
	                model.setBeneficiaryErrorStatus(beneDtoCheck.getBeneficiaryErrorStatus());
	            }
	            if (!StringUtils.isBlank(hist.getBeneficaryCorespondingBankName()) 
	                && !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.WU) 
	                && !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.MONEY)) {
	                list.add(model);
	            }

	        }
	        return list;
	    }

	private List<TransactionHistroyDTO> convert(List<CustomerRemittanceTransactionView> trnxHist,
			Map<BigDecimal, BenificiaryListView> beneMap) {
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
			model.setTransactionReference(getTransactionReferece(hist));

			if (beneMap!=null  && model.getBeneficiaryRelationSeqId()!=null) {
				Boolean status = Boolean.FALSE;
				if(beneMap.get(model.getBeneficiaryRelationSeqId()) != null) {
				if (beneMap.get(model.getBeneficiaryRelationSeqId()).getIsActive()!= null)
					status = beneMap.get(model.getBeneficiaryRelationSeqId()).getIsActive().equalsIgnoreCase("Y")?Boolean.TRUE:Boolean.FALSE;
				 } 			  
				model.setBeneIsActive(status);
			}	
			
			BenificiaryListView beneViewModel = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(hist.getCustomerId(),metaData.getCountryId(),hist.getBeneficiaryRelationSeqId());
			if(beneViewModel!=null){
				 beneDtoCheck=beneCheckService.beneCheck(convertBeneModelToDto(beneViewModel));
			}
			if(beneDtoCheck != null){
				model.setBeneficiaryErrorStatus(beneDtoCheck.getBeneficiaryErrorStatus());
			}
			if (!StringUtils.isBlank(hist.getBeneficaryCorespondingBankName()) 
				&& !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.WU) 
				&& !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.MONEY)) {
				list.add(model);
			}

		}
		return list;
	}
	
	private List<TransactionHistoryDto> convert_v2(List<CustomerRemittanceTransactionHistoryView> trnxHist,
			Map<BigDecimal, BenificiaryListView> beneMap) {
		List<TransactionHistoryDto> list = new ArrayList<>();
		for (CustomerRemittanceTransactionHistoryView hist : trnxHist) {
			BeneficiaryListDTO beneDtoCheck = null; 
			TransactionHistoryDto model = new TransactionHistoryDto();
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
			model.setTransactionReference(getTransactionReference(hist));

			if (beneMap!=null  && model.getBeneficiaryRelationSeqId()!=null) {
				Boolean status = Boolean.FALSE;
				if(beneMap.get(model.getBeneficiaryRelationSeqId()) != null) {
				if (beneMap.get(model.getBeneficiaryRelationSeqId()).getIsActive()!= null)
					status = beneMap.get(model.getBeneficiaryRelationSeqId()).getIsActive().equalsIgnoreCase("Y")?Boolean.TRUE:Boolean.FALSE;
				 } 			  
				model.setBeneIsActive(status);
			}	
			
			BenificiaryListView beneViewModel = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(hist.getCustomerId(),metaData.getCountryId(),hist.getBeneficiaryRelationSeqId());
			if(beneViewModel!=null){
				 beneDtoCheck=beneCheckService.beneCheck(convertBeneModelToDto(beneViewModel));
			}
			if(beneDtoCheck != null){
				model.setBeneficiaryErrorStatus(beneDtoCheck.getBeneficiaryErrorStatus());
			}
			if (!StringUtils.isBlank(hist.getBeneficaryCorespondingBankName()) 
				&& !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.WU) 
				&& !hist.getBeneficaryCorespondingBankName().equalsIgnoreCase(ConstantDocument.MONEY)) {
				list.add(model);
			}

		}
		return list;
	}
	
	private String getTransactionReferece(CustomerRemittanceTransactionView hist) {
		return hist.getDocumentNumber().toString() + hist.getDocumentFinanceYear().toString();
	}
	

	private String getTransactionReference(CustomerRemittanceTransactionHistoryView hist) {
		return hist.getDocumentNumber().toString() + hist.getDocumentFinanceYear().toString();
	}

	public BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("error occured in convertBeneModelToDto", e);
		}
		return dto;
	}
	
	public CustomerRemittanceTransactionView getLastTransaction(BigDecimal custId) {
		CustomerRemittanceTransactionView output = null;
		List<CustomerRemittanceTransactionView> list = transactionHistroyDao.getLastTransaction(custId,
				ConstantDocument.REMITTANCE_DOCUMENT_CODE, new PageRequest(0, 1));
		if (list != null && !list.isEmpty()) {
			output = list.get(0);
		}
		return output;
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
