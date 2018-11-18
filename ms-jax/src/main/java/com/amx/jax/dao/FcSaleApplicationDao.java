package com.amx.jax.dao;
/**
 * Author : Rabil
 * Date   : 08/11/2018
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.ReceiptPaymentAppRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FcSaleApplicationDao {

	
	private Logger logger = Logger.getLogger(FcSaleApplicationDao.class);
	
	
	@Autowired
	ReceiptPaymentAppRepository receiptPaymentApplRespo;
	
	
	@Transactional
	public void saveAllApplicationData(HashMap<String, Object> mapAllDetailApplSave){
		ReceiptPaymentApp receiptAppl= (ReceiptPaymentApp)mapAllDetailApplSave.get("EX_APPL_RECEIPT");
		if(receiptPaymentApplRespo!=null){
			receiptPaymentApplRespo.save(receiptAppl);
		}
	}
	
	
	public void deActiveUnUsedapplication(BigDecimal customerId,BigDecimal foeignCurrencyId){
		try {
		List<ReceiptPaymentApp> listOfAppl =  receiptPaymentApplRespo.deActivateNotUsedApplication(customerId, foeignCurrencyId);
		for(ReceiptPaymentApp appl : listOfAppl){
			ReceiptPaymentApp applDeac = receiptPaymentApplRespo.findOne(appl.getReceiptId());
			applDeac.setIsActive(ConstantDocument.Deleted);
			receiptPaymentApplRespo.save(applDeac);
		}
		}catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("deActivateApplication faliled for custoemr:"+customerId);
		}
	}
	
	
	public void removeItemFromCart(BigDecimal applId){
	try {
			ReceiptPaymentApp applDeac = receiptPaymentApplRespo.findOne(applId);
			applDeac.setIsActive(ConstantDocument.Deleted);
			applDeac.setModifiedDate(new Date());
			receiptPaymentApplRespo.save(applDeac);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Remove item from cart faliled for custoemr:"+applId);
		}
		
	}
	
}
