package com.amx.jax.dao;
/**
 * Author : Rabil
 * Date   : 08/11/2018
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.ReceiptPayment;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.dbmodel.fx.FxDeliveryRemark;
import com.amx.jax.dbmodel.fx.StatusMaster;
import com.amx.jax.dbmodel.fx.VwFxDeliveryDetailsModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.response.fx.FxApplicationDto;
import com.amx.jax.payg.PayGModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICollectionRepository;
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.PaygDetailsRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.ReceiptPaymentRespository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.repository.fx.FxDeliveryRemarkRepository;
import com.amx.jax.repository.fx.StatusMasterRepository;
import com.amx.jax.repository.fx.VwFxDeliveryDetailsRepository;
import com.amx.jax.util.JaxUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FcSaleApplicationDao {

	
	private Logger logger = Logger.getLogger(FcSaleApplicationDao.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ReceiptPaymentAppRepository receiptPaymentApplRespo;
	
	@Autowired
	PaygDetailsRepository pgRepository;
	
	@Autowired
	FxDeliveryDetailsRepository fxDeliveryDetailsRepository;
	
	@Autowired
	IShippingAddressRepository shippingAddressDao;
	
	@Autowired
	ReceiptPaymentRespository receiptPaymentRespository;
	
	@Autowired
	ICollectionRepository collectionRepository;
	
	@Autowired
	ICollectionDetailRepository collectionDetailRepository;
	
	@Autowired
	VwFxDeliveryDetailsRepository vwFxDeliveryDetailsRepository;
	
	@Autowired
	FxDeliveryRemarkRepository fxDeliveryRemarkRepository ;
	
	@Autowired
	StatusMasterRepository statusMasterRepository;
	
	
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
			for(FxApplicationDto dto :requestmodel.getCartDetailList()){
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
			
			if(!listOfRecAppl.isEmpty()){
				for(ReceiptPaymentApp appl :listOfRecAppl){
					appl.setIsActive(ConstantDocument.Deleted);
					appl.setApplicationStatus(null);
					appl.setModifiedDate(new Date());
					updateCartDetails(appl);
				}
			}
			
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
				logger.error("Update after PG details Payment Id :"+paymentResponse.getPaymentId()+"\t Udf 3--Pg trnx seq Id :"+paymentResponse.getUdf3()+"Result code :"+paymentResponse.getResultCode());
				throw new GlobalException("PG updatio failed",JaxError.PAYMENT_UPDATION_FAILED);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("catch Update after PG details Payment Id :"+paymentResponse.getPaymentId()+"\t Udf 3--Pg trnx seq Id :"+paymentResponse.getUdf3()+"Result code :"+paymentResponse.getResultCode());
			throw new GlobalException("PG updatio failed",JaxError.PAYMENT_UPDATION_FAILED);
		}
		
	}
	
	@Transactional
	public Map<String, Object> finalSaveAll(HashMap<String,Object> hashMapToSaveAllInput){
		Map<String, Object> output = new HashMap<>();
		logger.info("Input value : "+hashMapToSaveAllInput.toString());
		String outMessage = "";
		try{
			BigDecimal collectionId= BigDecimal.ZERO;
			 List<ReceiptPayment> receiptPaymentList=(List<ReceiptPayment>)hashMapToSaveAllInput.get("RCPT_PAY");
			 CollectionModel collection =(CollectionModel)hashMapToSaveAllInput.get("COLLECTION");
			 CollectDetailModel collectDetail =(CollectDetailModel)hashMapToSaveAllInput.get("COLL_DETAILS");
			 List<ReceiptPaymentApp> listOfRecAppl = (List<ReceiptPaymentApp>)hashMapToSaveAllInput.get("LIST_RCPT_APPL");
			 PayGModel	pgResponse =(PayGModel)hashMapToSaveAllInput.get("PG_RESP_DETAILS");
			 
			 if(collection!=null){
				 collectionRepository.save(collection);
			 } else{
				 output.put("P_ERROR_MESG", "ERROR_WHILE_SAVING_COLLECTION");
			 }
			 if(collection!= null && collection.getCollectionId()!=null)
			 {
				 collectionDetailRepository.save(collectDetail);
			 }else{
				 output.put("P_ERROR_MESG", "ERROR_WHILE_SAVING_COLLECTION_DETAILS");
			 }
			 
			 if(receiptPaymentList !=null && !receiptPaymentList.isEmpty()){
				 for(ReceiptPayment rcpt : receiptPaymentList){
					 rcpt.setColDocNo(collection.getDocumentNo());
					 rcpt.setColDocFyr(collection.getDocumentFinanceYear());
					 rcpt.setColDocCode(collection.getDocumentCode());
					 receiptPaymentRespository.save(rcpt);
					 //Update the Application Receipt
					 updateAppicationReceiptPayment(rcpt);
					 updateDeliveryDetails(rcpt.getDeliveryDetSeqId());
				 }//end of for loop.
			 }else{
				 output.put("P_ERROR_MESG", "ERROR_RCPT_APPLICATION_SAVE");
			 }
			 
			
			 
		    BigDecimal collectionFinanceYear = collection.getDocumentFinanceYear();
			BigDecimal collectionDocumentNumber = collection.getDocumentNo();
			BigDecimal collectionDocumentCode = collection.getDocumentCode();
			 
			 if(pgResponse!=null && JaxUtil.isNullZeroBigDecimalCheck(new BigDecimal(pgResponse.getUdf3()))){
				 PaygDetailsModel pgModel =pgRepository.findOne(new BigDecimal(pgResponse.getUdf3()));
				 pgModel.setCollDocNumber(collectionDocumentNumber);
				 pgModel.setCollDocFYear(collectionFinanceYear);
				 savePaygDetails(pgModel);
			 }else{
				 output.put("P_ERROR_MESG", "RCPT_APPLICATION_NOT_FOUND");
			 }
			 
			 
			output.put("P_COLLECT_FINYR", collectionFinanceYear);
			output.put("P_COLLECTION_NO", collectionDocumentNumber);
			output.put("P_COLLECTION_DOCUMENT_CODE", collectionDocumentCode);
		
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("saveAll of FX Order :"+e.getMessage());
			outMessage = e.getMessage();
			output.put("P_ERROR_MESG", outMessage);
		}
		logger.info("output value : "+output.toString());
		return output;
	}
	
	public void updateAppicationReceiptPayment(ReceiptPayment rcpt){
		if(rcpt!=null){
			BigDecimal applicationDocumentNo = rcpt.getApplicationDocumentNo();
			BigDecimal applicationFinanceYear= rcpt.getApplicationFinanceYear();
			BigDecimal customerId            = rcpt.getFsCustomer().getCustomerId();  
			if(JaxUtil.isNullZeroBigDecimalCheck(applicationDocumentNo) && JaxUtil.isNullZeroBigDecimalCheck(applicationFinanceYear)){
				ReceiptPaymentApp rcptAppl = receiptPaymentApplRespo.getApplciationDetailsByDocNoFYear(customerId,applicationDocumentNo,applicationFinanceYear);
				if(rcptAppl!=null){
					rcptAppl.setReceiptId(rcptAppl.getReceiptId());
					rcptAppl.setApplicationStatus(ConstantDocument.T);
					rcptAppl.setTransactionRefNo(rcpt.getDocumentNo());
					rcptAppl.setTransactionFinanceYear(rcpt.getDocumentFinanceYear());
					rcptAppl.setColDocNo(rcpt.getColDocNo());
					rcptAppl.setColDocFyr(rcpt.getColDocFyr());
					rcptAppl.setColDocCode(rcpt.getColDocCode());
					receiptPaymentApplRespo.save(rcptAppl);
				}
			}
					
		}	
	}
	

	public List<VwFxDeliveryDetailsModel> listOrders(BigDecimal driverEmployeeId) {
		return vwFxDeliveryDetailsRepository.findDriverOrders(driverEmployeeId,  new Date());
	}
	
	public FxDeliveryRemark getDeliveryRemarkById(BigDecimal deliveryRemarkId) {
		FxDeliveryRemark fxDeliveryRemark = null;
		if (deliveryRemarkId != null) {
			fxDeliveryRemark = fxDeliveryRemarkRepository.findOne(deliveryRemarkId);
		}
		return fxDeliveryRemark;
	}
	
	public ShippingAddressDetail getShippingAddressById(BigDecimal shippingAddressId) {
		return shippingAddressDao.findOne(shippingAddressId);
	}

	public VwFxDeliveryDetailsModel getDeliveryDetail(BigDecimal deliveryDetailSeqId) {
		return vwFxDeliveryDetailsRepository.findOne(deliveryDetailSeqId);
	}
	
	public VwFxDeliveryDetailsModel getDeliveryDetail(BigDecimal deliveryDetailSeqId, BigDecimal driverEmployeeId) {
		return vwFxDeliveryDetailsRepository.findByDeleviryDelSeqIdAndDriverEmployeeId(deliveryDetailSeqId,
				driverEmployeeId);
	}
	
	public void saveDeliveryDetail(FxDeliveryDetailsModel model) {
		fxDeliveryDetailsRepository.save(model);
	}
	
	public FxDeliveryDetailsModel getDeliveryDetailModel(BigDecimal deliveryDetailSeqId) {
		return fxDeliveryDetailsRepository.findOne(deliveryDetailSeqId);
	}
	
	public void updateDeliveryDetails(BigDecimal delDelSeqId){
		FxDeliveryDetailsModel deliveryDetails = fxDeliveryDetailsRepository.findOne(delDelSeqId);
		if(deliveryDetails!=null){
			deliveryDetails.setOrderStatus(ConstantDocument.ORD);
		}
	}
	
	public StatusMaster getStatusMaster(String statusCode) {
		return statusMasterRepository.findByStatusCode(statusCode);
	}

	public List<FxDeliveryRemark> listDeliveryRemark() {
		return fxDeliveryRemarkRepository.findByIsActive(ConstantDocument.Yes);
	}
	
	
/*	public BigDecimal getFxTrnxLimit(Map<String, Object> inputValues) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal fcAmount = (BigDecimal)inputValues.get("FC_AMOUNT");
		BigDecimal customerId = (BigDecimal)inputValues.get("CUSTOMER_ID");
		logger.info("inputValues :" +inputValues.toString());
		String sql =" select sum(amt)  totalamount from ("
					+" select sum(A.LOCAL_NET_AMOUNT) amt from EX_APPL_RECEIPT_PAYMENT A where  A.customer_id ="+customerId+" "
					+" and   trunc(A.CREATED_DATE)=trunc(sysdate)  and a.isactive='Y' union all "
					+" select sum(A.LOCAL_NET_AMOUNT) amt from EX_RECEIPT_PAYMENT A where  A.customer_id ="+customerId+" "
					+ " and   trunc(A.CREATED_DATE)=trunc(sysdate)  and a.isactive='Y' ) t2 ";

		List<Object> inputList = new ArrayList<>();
		inputList.add(inputValues.get("CUSTOMER_ID"));
		List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
		logger.info("Output : "+outputList.toString());
		Iterator<Map<String, Object>> itr = outputList.iterator();
		while (itr.hasNext()) {
			totalAmount = (BigDecimal) itr.next().get("totalamount");
		}
		
		totalAmount = totalAmount.add(fcAmount);
		return totalAmount;

	}
	*/
	
}
