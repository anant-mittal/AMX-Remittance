package com.amx.jax.branchremittance.manager;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author rabil
 * 
 */
import org.springframework.stereotype.Component;

import com.amx.jax.branchremittance.dao.BranchRemittanceDao;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.model.request.remittance.RemittanceSignatureDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.IRemittanceBenificiaryRepository;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.JsonUtil;

@Component
public class RemittanceSignatureManager {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	IRemittanceTransactionDao remittanceTransactionDao;
	
	@Autowired
	IRemittanceBenificiaryRepository remitBeneRepository;
	@Autowired
	BranchRemittanceDao branchRemittanceDao;
	
	@Autowired
	PKCS7Signer pKCS7Signer;
	
	
	public void updateSignatureHash(PaymentResponseDto pDto) {
		try {
		List<RemittanceTransactionView> remitTrnxList =  remittanceTransactionDao.findByCustomerIdAndCollectionDocumentNoAndCollectionDocFinanceYearAndCollectionDocCode(pDto.getCustomerId(),pDto.getCollectionDocumentNumber(),pDto.getCollectionFinanceYear(),pDto.getCollectionDocumentCode());
		if(remitTrnxList!=null && !remitTrnxList.isEmpty()) {
			for(RemittanceTransactionView trnxDetails :remitTrnxList ) {
				RemittanceTransaction remitTrnx = new RemittanceTransaction();
				remitTrnx.setRemittanceTransactionId(trnxDetails.getRemittanceTransactionId());
				RemittanceBenificiary remitBene = remitBeneRepository.findByExRemittancefromBenfi(remitTrnx);
				RemittanceSignatureDto sigDto = createSignature(trnxDetails,remitBene);
				logger.info("Signature JSON ----- :"+JsonUtil.toJson(sigDto));
				String signature =getTrnxSignature(sigDto);
				branchRemittanceDao.updateSignatureHash(trnxDetails,signature);
				logger.info("Signature value ----- :"+signature);
			}
		}
		}catch(Exception e) {
			logger.info("updateSignatureHash JSON ----- :"+JsonUtil.toJson(pDto));
			e.printStackTrace();
		}
	}
	
	
	
	private RemittanceSignatureDto createSignature(RemittanceTransactionView trnxDetails,RemittanceBenificiary remitBene) {
		RemittanceSignatureDto dto = new RemittanceSignatureDto();
		
		dto.setCustomerId(trnxDetails.getCustomerId());
		dto.setDocumentFYear(trnxDetails.getDocumentFinancialYear());
		dto.setDocumentNumber(trnxDetails.getDocumentNo());
		dto.setForignAmount(trnxDetails.getForeignTransactionAmount());
		dto.setForeignCurrencyIso3Code(trnxDetails.getCurrencyQuoteName());
		dto.setBeneRelationId(remitBene.getBeneficiaryRelationShipSeqId());
		if(remitBene!=null && !StringUtils.isBlank(remitBene.getBeneficiaryAccountNo())) {
			dto.setAccountNumber(remitBene.getBeneficiaryAccountNo());
		}
		String dateStr = DateUtil.convertDatetostringWithddMmYyyywithHMinute(trnxDetails.getDocumentDate());		
		dto.setDateStr(dateStr);
		dto.setBeneName(remitBene.getBeneficiaryName());
		
		
		return dto;
		
	}
	
	private  String getTrnxSignature(RemittanceSignatureDto sigDto) {
		String signature =null;
	
		if(sigDto!=null) {
			StringBuffer sb = new StringBuffer();
			if(JaxUtil.isNullZeroBigDecimalCheck(sigDto.getCustomerId())){
				sb.append(sigDto.getCustomerId());
			}if(JaxUtil.isNullZeroBigDecimalCheck(sigDto.getDocumentFYear())){
				sb.append(sigDto.getDocumentFYear());
			}
			
			if(JaxUtil.isNullZeroBigDecimalCheck(sigDto.getDocumentNumber())){
				sb.append(sigDto.getDocumentNumber());
			}
			
			if(JaxUtil.isNullZeroBigDecimalCheck(sigDto.getForignAmount())){
				sb.append(sigDto.getForignAmount());
			}
			
			/*if(JaxUtil.isNullZeroBigDecimalCheck(sigDto.getForeignCurrencyId())){
				sb.append(sigDto.getForeignCurrencyId());
			}*/
			
			if(!StringUtils.isBlank(sigDto.getForeignCurrencyIso3Code())) {
				sb.append(sigDto.getForeignCurrencyIso3Code());
			}
			
			if(JaxUtil.isNullZeroBigDecimalCheck(sigDto.getBeneRelationId())){
				sb.append(sigDto.getBeneRelationId());
			}
			
			if(!StringUtils.isBlank(sigDto.getAccountNumber())){
				sb.append(sigDto.getAccountNumber());
			}
			if(!StringUtils.isBlank(sigDto.getBeneName())) {
				sb.append(sigDto.getBeneName());
			}
			
			if(!StringUtils.isBlank(sigDto.getDateStr())) {
				sb.append(sigDto.getDateStr());
			}
			
			if(sb!=null) {
				signature =sb.toString();
				System.out.println("signature -->"+signature);
				signature =pKCS7Signer.getSignature(signature);
				System.out.println("hash signature -->"+signature);
			}
		}
		return signature;
	}
	
	private String getSignature(String str) {
		return null;
	}
}
