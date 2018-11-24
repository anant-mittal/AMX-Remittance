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
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryRemark;
import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.response.fx.FxDeliveryDetailDto;
import com.amx.jax.model.response.fx.ShoppingCartDetailsDto;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.PaygDetailsRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.repository.fx.FxDeliveryRemarkRepository;
import com.amx.jax.repository.fx.VwFxDeliveryDetailsRepository;

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
	VwFxDeliveryDetailsRepository vwFxDeliveryDetailsRepository;
	@Autowired
	FxDeliveryRemarkRepository fxDeliveryRemarkRepository ; 
	@Autowired
	IShippingAddressRepository shippingAddressDao;
	
	
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

	public List<VwFxDeliveryDetailsModel> listOrders(BigDecimal driverEmployeeId) {
		return vwFxDeliveryDetailsRepository.findByDriverEmployeeIdAndDeliveryDate(driverEmployeeId, new Date());
	}
	
	public FxDeliveryRemark getDeliveryRemarkById(BigDecimal deliveryRemarkId) {
		return fxDeliveryRemarkRepository.findOne(deliveryRemarkId);
	}
	
	public ShippingAddressDetail getShippingAddressById(BigDecimal shippingAddressId) {
		return shippingAddressDao.findOne(shippingAddressId);
	}

	public VwFxDeliveryDetailsModel getDeliveryDetail(BigDecimal deliveryDetailSeqId) {
		return vwFxDeliveryDetailsRepository.findOne(deliveryDetailSeqId);
	}
	
}
