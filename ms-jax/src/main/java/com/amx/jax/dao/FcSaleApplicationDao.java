package com.amx.jax.dao;
/**
 * Author : Rabil
 * Date   : 08/11/2018
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.ReceiptPayment;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.response.fx.ShoppingCartDetailsDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.FxDeliveryDetailsRepository;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICollectionRepository;
import com.amx.jax.repository.PaygDetailsRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.ReceiptPaymentRespository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FcSaleApplicationDao {

	
	private Logger logger = Logger.getLogger(FcSaleApplicationDao.class);
	
	
	@Autowired
	ReceiptPaymentAppRepository receiptPaymentApplRespo;
	
	@Autowired
	PaygDetailsRepository pgRepository;
	
	@Autowired
	FxDeliveryDetailsRepository fxDeliveryDetailsRepository;
	
	@Autowired
	ReceiptPaymentRespository receiptPaymentRespository;
	
	@Autowired
	ICollectionRepository collectionRepository;
	
	@Autowired
	ICollectionDetailRepository collectionDetailRepository;
	
	
	@Transactional
	public void saveAllApplicationData(HashMap<String, Object> mapAllDetailApplSave){
		ReceiptPaymentApp receiptAppl= (ReceiptPaymentApp)mapAllDetailApplSave.get("EX_APPL_RECEIPT");
		if(receiptPaymentApplRespo!=null){
			receiptPaymentApplRespo.save(receiptAppl);
		}
	}
	
	@Transactional
	public void saveAllAppDetails(HashMap<String, Object> mapAllDetailApplSave){
		
		PaygDetailsModel paygDetailsModel = (PaygDetailsModel)mapAllDetailApplSave.get("EX_PAYG_DETAILS");
		FxDeliveryDetailsModel fxDeliveryModel =(FxDeliveryDetailsModel)mapAllDetailApplSave.get("EX_DELIVERY_DETAILS");
		FcSaleOrderPaynowRequestModel requestmodel =(FcSaleOrderPaynowRequestModel)mapAllDetailApplSave.get("requestmodel");
		
		if(paygDetailsModel!=null){
			pgRepository.save(paygDetailsModel);
		}
		if(fxDeliveryModel!=null){
			fxDeliveryDetailsRepository.save(fxDeliveryModel);
		}
		
		if(paygDetailsModel !=null && fxDeliveryModel!= null && requestmodel!=null){
			for(ShoppingCartDetailsDto dto :requestmodel.getCartDetailList()){
				ReceiptPaymentApp applDeac = receiptPaymentApplRespo.findOne(dto.getApplicationId());
				if(applDeac!=null && applDeac.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)){
				applDeac.setApplicationStatus(ConstantDocument.S);
				applDeac.setPgPaymentSeqDtlId(paygDetailsModel.getPaygTrnxSeqId());
				applDeac.setDeliveryDetSeqId(fxDeliveryModel.getDeleviryDelSeqId());
				receiptPaymentApplRespo.save(applDeac);
				}
			}
		}
	}
	
	
	public void deActiveUnUsedapplication(BigDecimal customerId,BigDecimal foeignCurrencyId){
		try {
		List<ReceiptPaymentApp> listOfAppl =  receiptPaymentApplRespo.deActivateNotUsedApplication(customerId, foeignCurrencyId);
		for(ReceiptPaymentApp appl : listOfAppl){
			ReceiptPaymentApp applDeac = receiptPaymentApplRespo.findOne(appl.getReceiptId());
			applDeac.setIsActive(ConstantDocument.Deleted);
			applDeac.setApplicationStatus(null);
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
			applDeac.setApplicationStatus(null);
			applDeac.setModifiedDate(new Date());
			receiptPaymentApplRespo.save(applDeac);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Remove item from cart faliled for custoemr:"+applId);
		}
		
	}
	
	public void savePaygDetails(PaygDetailsModel pgModel){
		try {
			pgRepository.save(pgModel);
		}catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("saving pg details :"+pgModel);
		}
	}
	/**
	 * 
	 * @param :update shippiong address, time slot.
	 */
	public void updateCartDetails(ReceiptPaymentApp rcpt){
		if(rcpt!=null){
			receiptPaymentApplRespo.save(rcpt);
		}
		
	}
	
	public void updatePaygDetails(List<ReceiptPaymentApp> listOfRecAppl,PaymentResponseDto paymentResponse){
		try{
			if(paymentResponse!= null && paymentResponse.getUdf3()!=null){
				PaygDetailsModel pgModel =pgRepository.findOne(new BigDecimal(paymentResponse.getUdf3()));
				pgModel.setResultCode(paymentResponse.getResultCode());
				pgModel.setModifiedDate(new Date());
				pgModel.setPgAuthCode(paymentResponse.getAuth_appNo());
				pgModel.setPgErrorText(paymentResponse.getErrorText());
				pgModel.setPgPaymentId(paymentResponse.getPaymentId());
				pgModel.setPgReceiptDate(paymentResponse.getPostDate());
				pgModel.setPgTransactionId(paymentResponse.getTransactionId());
				pgModel.setPgReferenceId(paymentResponse.getReferenceId());
				pgRepository.save(pgModel);
			}else{
				throw new GlobalException("Update after PG details Payment Id :"+paymentResponse.getPaymentId()+"\t Udf 3--Pg trnx seq Id :"+paymentResponse.getUdf3()+"Result code :"+paymentResponse.getResultCode());
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Update after PG details Payment Id :"+paymentResponse.getPaymentId()+"\t Udf 3--Pg trnx seq Id :"+paymentResponse.getUdf3()+"Result code :"+paymentResponse.getResultCode());
		}
		
	}
	
	@Transactional
	public Map<String, Object> saveAll(HashMap<String,Object> hashMapToSaveAllInput){
		Map<String, Object> output = new HashMap<>();
		logger.info("Input value : "+hashMapToSaveAllInput.toString());
		try{
			BigDecimal collectionId= BigDecimal.ZERO;
			 List<ReceiptPayment> receiptPaymentList=(List<ReceiptPayment>)hashMapToSaveAllInput.get("RCPT_PAY");
			 CollectionModel collection =(CollectionModel)hashMapToSaveAllInput.get("COLLECTION");
			 CollectDetailModel collectDetail =(CollectDetailModel)hashMapToSaveAllInput.get("COLL_DETAILS");
			 List<ReceiptPaymentApp> listOfRecAppl = (List<ReceiptPaymentApp>)hashMapToSaveAllInput.get("LIST_RCPT_APPL");
			 PaymentResponseDto	pgResponse =(PaymentResponseDto)hashMapToSaveAllInput.get("PG_RESP_DETAILS");
			 
			 if(receiptPaymentList.isEmpty()){
				 for(ReceiptPayment rcpt : receiptPaymentList){
					 receiptPaymentRespository.save(rcpt);
				 }
			 }
			 
			/* if(collection!=null){
				 collectionId = collectionRepository.save(collection);
			 }
			 if(collection.getCollectionId()=)*/
			 
			
		/*	BigDecimal collectionFinanceYear = cs.getBigDecimal(9);
			BigDecimal collectionDocumentNumber = cs.getBigDecimal(10);
			BigDecimal collectionDocumentCode = cs.getBigDecimal(11);
			String outMessage = cs.getString(12);

			output.put("P_COLLECT_FINYR", collectionFinanceYear);
			output.put("P_COLLECTION_NO", collectionDocumentNumber);
			output.put("P_COLLECTION_DOCUMENT_CODE", collectionDocumentCode);
			output.put("P_ERROR_MESG", outMessage);
			*/
			logger.info("output value : "+output.toString());
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveAll of FX Order :"+e.getMessage());
		}
		return output;
	}
	
}
