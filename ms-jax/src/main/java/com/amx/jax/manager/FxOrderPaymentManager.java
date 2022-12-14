package com.amx.jax.manager;

/**
 * @author rabil 
 * @date 25/11/2018
 * @purpose : save all details	
 */
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.FcSaleApplicationDao;
import com.amx.jax.dao.FcSaleExchangeRateDao;
import com.amx.jax.dbmodel.CollectDetailMdlv1;
import com.amx.jax.dbmodel.CollectionMdlv1;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.PaymentModeModel;
import com.amx.jax.dbmodel.PurposeOfTransaction;
import com.amx.jax.dbmodel.ReceiptPayment;
import com.amx.jax.dbmodel.ReceiptPaymentApp;
import com.amx.jax.dbmodel.SourceOfIncome;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.fx.ShoppingCartDetailsDto;
import com.amx.jax.payg.PayGModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.PaymentModeRepository;
import com.amx.jax.repository.ReceiptPaymentAppRepository;
import com.amx.jax.repository.fx.FxDeliveryDetailsRepository;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FxOrderPaymentManager {

	private static final Logger logger = LoggerFactory.getLogger(FxOrderPaymentManager.class);

	@Autowired
	CustomerDao customerDao;

	@Autowired
	private CustomerRepository custRepo;

	@Autowired
	FinancialService finanacialService;

	@Autowired
	ReceiptPaymentAppRepository receiptAppRepository;

	@Autowired
	FcSaleApplicationDao rcptApplPaydao;

	@Autowired
	MetaData metaData;

	@Autowired
	CompanyService companyService;

	@Autowired
	CountryBranchRepository countryBranchRepository;

	@Autowired
	IDocumentDao documentDao;

	@Autowired
	FcSaleApplicationTransactionManager applTrnxManager;

	@Autowired
	FxDeliveryDetailsRepository deliveryDetailRepos;

	@Autowired
	FcSaleExchangeRateDao fcSaleExchangeRateDao;

	@Autowired
	ApplicationProcedureDao applicationProcedureDao;

	@Autowired
	PaymentModeRepository payModeRepositoy;
	

	@Autowired
	AuditService auditService;
	
	@Autowired
	FcSaleApplicationTransactionManager fcSaleApplicationTransactionManager;
	@Autowired
	JaxNotificationService notificationService;
	@Autowired
	CurrencyMasterService currencyMasterService;
	
	
	public PaymentResponseDto paymentCapture(PaymentResponseDto paymentResponse) {
		logger.debug("paymment capture :" + paymentResponse.toString());
		logger.debug("Customer Id :" + paymentResponse.getCustomerId());
		logger.debug(
				"Result code :" + paymentResponse.getResultCode() + "\t Auth Code :" + paymentResponse.getAuth_appNo());
		logger.debug("paymment capture Payment ID :" + paymentResponse.getPaymentId() + "\t Merchant Track Id :"
				+ paymentResponse.getTrackId() + "\t UDF 3 :" + paymentResponse.getUdf3() + "\t Udf 2 :"
				+ paymentResponse.getUdf2());

		HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		Map<String, Object> mapResopnseObject = new HashMap<String, Object>();
		List<ShoppingCartDetailsDto> fxOrdershoppingCartList = new ArrayList<>();
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		List<ReceiptPaymentApp> listOfRecAppl = new ArrayList<>();
		BigDecimal customerId = metaData.getCustomerId();
		try {

			if (!StringUtils.isBlank(paymentResponse.getPaymentId())
					&& !StringUtils.isBlank(paymentResponse.getResultCode())
					&& (paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.CAPTURED)
							|| paymentResponse.getResultCode().equalsIgnoreCase(ConstantDocument.APPROVED))) {
				/** update the payg details **/
				rcptApplPaydao.updatePaygDetails(listOfRecAppl, paymentResponse);
				/** To fetch the applciation **/

				if (JaxUtil.isNullZeroBigDecimalCheck(paymentResponse.getCustomerId())) {
					customerId = customerId;
				}

				listOfRecAppl = receiptAppRepository.fetchreceiptPaymentAppl(customerId,new BigDecimal(paymentResponse.getUdf3()));
				/** aded new code for appl amount and knet amount cehck  on 30 may 2019 by rabil**/
				validateAmountMismatch(listOfRecAppl, paymentResponse);
				/** end **/		
				 
				List<ReceiptPayment> receiptPayment = saveReceiptPayment(listOfRecAppl, paymentResponse);
				CollectionMdlv1 collection = saveCollection(listOfRecAppl, paymentResponse);
				CollectDetailMdlv1 collectDetail = saveCollectDetail(listOfRecAppl, paymentResponse, collection);
				// adding into map
				mapAllDetailApplSave.put("RCPT_PAY", receiptPayment);
				mapAllDetailApplSave.put("COLLECTION", collection);
				mapAllDetailApplSave.put("COLL_DETAILS", collectDetail);
				mapAllDetailApplSave.put("LIST_RCPT_APPL", listOfRecAppl);
				mapAllDetailApplSave.put("PG_RESP_DETAILS", paymentResponse);

				mapResopnseObject = rcptApplPaydao.finalSaveAll(mapAllDetailApplSave);

				logger.debug("mapResopnseObject :" + mapResopnseObject.toString());
				if (mapResopnseObject != null && mapResopnseObject.get("P_ERROR_MESG") == null) {
					paymentResponse.setCollectionDocumentNumber((BigDecimal) mapResopnseObject.get("P_COLLECTION_NO"));
					paymentResponse.setCollectionFinanceYear((BigDecimal) mapResopnseObject.get("P_COLLECT_FINYR"));
					paymentResponse.setCollectionDocumentCode(
							(BigDecimal) mapResopnseObject.get("P_COLLECTION_DOCUMENT_CODE"));

					if (listOfRecAppl != null && !listOfRecAppl.isEmpty()) {
						logStatusChangeAuditEvent(listOfRecAppl.get(0).getDeliveryDetSeqId(), ConstantDocument.ORD);
					}

				} else {
					logger.error("paymentCapture final save  finalSaveAll method :" + mapResopnseObject.toString());
					throw new GlobalException(JaxError.PAYMENT_UPDATION_FAILED,
							"Invalid collection document number /year");
				}

			} else {
				listOfRecAppl = receiptAppRepository.fetchreceiptPaymentAppl(paymentResponse.getCustomerId(),
						new BigDecimal(paymentResponse.getUdf3()));
				if (listOfRecAppl != null && !listOfRecAppl.isEmpty()) {
					rcptApplPaydao.updatePaygDetails(listOfRecAppl, paymentResponse);
				}

			}
		} catch (GlobalException e) {
			logger.error("createFcSaleReceiptApplication", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		} catch (Exception e) {
			logger.error("try--catch block paymentCapture :" + e.getMessage());
			throw new GlobalException(JaxError.PAYMENT_UPDATION_FAILED, "catch Payment capture failed");
		}
		return paymentResponse;
	}

	/**
	 * Save ReceiptPayment
	 * 
	 * @param localFsCountryMaster
	 **/
	public List<ReceiptPayment> saveReceiptPayment(List<ReceiptPaymentApp> listOfRecAppl, PayGModel paymentResponse) {
		List<ReceiptPayment> receiptPaymentList = new ArrayList<>();
		if (!listOfRecAppl.isEmpty()) {
			for (ReceiptPaymentApp applreceipt : listOfRecAppl) {

				ReceiptPayment receiptPayment = new ReceiptPayment();

				receiptPayment.setApplicationDocumentNo(applreceipt.getDocumentNo());
				receiptPayment.setApplicationFinanceYear(applreceipt.getDocumentFinanceYear());

				CompanyMaster comp = new CompanyMaster();
				comp.setCompanyId(applreceipt.getCompanyId());
				receiptPayment.setFsCompanyMaster(comp);
				ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(applreceipt.getCompanyId());
				receiptPayment.setCompanyCode(companyDetails.getCompanyCode());

				CountryMaster countryMas = new CountryMaster();
				countryMas.setCountryId(applreceipt.getCountryId());
				receiptPayment.setFsCountryMaster(countryMas);

				CurrencyMasterMdlv1 localCurrency = new CurrencyMasterMdlv1();
				localCurrency.setCurrencyId(applreceipt.getLocalCurrencyId());
				receiptPayment.setLocalFsCountryMaster(localCurrency);

				CurrencyMasterMdlv1 foreignCurrency = new CurrencyMasterMdlv1();
				foreignCurrency.setCurrencyId(applreceipt.getForeignCurrencyId());
				receiptPayment.setForeignFsCountryMaster(foreignCurrency);

				receiptPayment.setAccountMMYYYY(applreceipt.getAccountMMYYYY());
				receiptPayment.setCreatedDate(new Date());
				receiptPayment.setDocumentDate(new Date());

				Customer customer = new Customer();
				customer.setCustomerId(applreceipt.getCustomerId());
				receiptPayment.setFsCustomer(customer);
				receiptPayment.setCustomerName(applreceipt.getCustomerName());
				receiptPayment
						.setCustomerReference(custRepo.findOne(applreceipt.getCustomerId()).getCustomerReference());

				CountryBranchMdlv1 countryBranch = countryBranchRepository.findByCountryBranchId(applreceipt.getBranchId());
				if (countryBranch != null) {
					receiptPayment.setCountryBranch(countryBranch);
					receiptPayment.setLocCode(countryBranch.getBranchId());
				}

				UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
				if (userFinancialYear != null) {
					receiptPayment.setDocumentFinanceYear(userFinancialYear.getFinancialYear());
					receiptPayment.setDocumentFinanceYearId(userFinancialYear.getFinancialYearID());
				}

				receiptPayment.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);

				BigDecimal documentNo = generateDocumentNumber(countryBranch, countryMas.getCountryId(),
						companyDetails.getCompanyId(), ConstantDocument.Update, receiptPayment.getDocumentFinanceYear(),
						ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
				if (documentNo != null && documentNo.compareTo(BigDecimal.ZERO) != 0) {
					receiptPayment.setDocumentNo(documentNo);
				} else {
					throw new GlobalException(JaxError.INVALID_RECEIPT_PAYMNET_DOCUMENT_NO,
							"Receipt  document should not be blank.");
				}

				receiptPayment.setReceiptType(ConstantDocument.FC_SALE_RECEIPT_TYPE);
				receiptPayment.setDocumentId(documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_FCSALE)
						.get(0).getDocumentID());

				receiptPayment.setLocalTrnxAmount(applreceipt.getLocalTrnxAmount());
				receiptPayment.setLocalNetAmount(applreceipt.getLocalNetAmount());
				receiptPayment.setForignTrnxAmount(applreceipt.getForignTrnxAmount());
				receiptPayment.setTransactionIPAddress(metaData.getDeviceIp());
				receiptPayment.setTransactionType(applreceipt.getTransactionType());
				receiptPayment.setTransactionActualRate(applreceipt.getTransactionActualRate());
				receiptPayment.setDocumentStatus(applreceipt.getDocumentStatus());

				PurposeOfTransaction purpose = new PurposeOfTransaction();
				purpose.setPurposeId(applreceipt.getPurposeofTransactionId());
				receiptPayment.setPurposeOfTransaction(purpose);

				SourceOfIncome sourceOfIncome = new SourceOfIncome();
				sourceOfIncome.setSourceId(applreceipt.getSourceofIncomeId());
				receiptPayment.setSourceOfIncome(sourceOfIncome);
				receiptPayment.setTravelStartDate(applreceipt.getTravelStartDate());
				receiptPayment.setTravelEndDate(applreceipt.getTravelEndDate());
				receiptPayment.setTravelCountryId(applreceipt.getTravelCountryId());
				receiptPayment.setSourceofIncomeId(applreceipt.getSourceofIncomeId());
				receiptPayment.setDenominationType(applreceipt.getDenominationType());

				receiptPayment.setDeliveryDetSeqId(applreceipt.getDeliveryDetSeqId());
				receiptPayment.setPgPaymentSeqDtlId(applreceipt.getPgPaymentSeqDtlId());

				receiptPayment.setIsActive(ConstantDocument.Yes);
				if (!StringUtils.isBlank(metaData.getReferrer())) {
					receiptPayment.setCreatedBy(metaData.getReferrer());
				} else {
					if (!StringUtils.isBlank(metaData.getAppType())) {
						receiptPayment.setCreatedBy(metaData.getAppType());
					} else {
						receiptPayment.setCreatedBy("WEB");
					}
				}

				receiptPaymentList.add(receiptPayment);
			} // End of foor loop

		}
		return receiptPaymentList;
	}

	/** Save collection **/
	public CollectionMdlv1 saveCollection(List<ReceiptPaymentApp> listOfRecAppl, PayGModel paymentResponse) {
		CollectionMdlv1 collection = new CollectionMdlv1();
		BigDecimal totalcollectiontAmount = BigDecimal.ZERO;
		BigDecimal deliveryCharges = BigDecimal.ZERO;
		try {
			if (listOfRecAppl != null && !listOfRecAppl.isEmpty()) {
				ReceiptPaymentApp appl = listOfRecAppl.get(0);
				FxDeliveryDetailsModel delDetail = deliveryDetailRepos.findOne(appl.getDeliveryDetSeqId());
				if (delDetail != null) {
					deliveryCharges = delDetail.getDeliveryCharges();
				} else {
					deliveryCharges = fcSaleExchangeRateDao
							.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes).get(0) == null
									? BigDecimal.ZERO
									: fcSaleExchangeRateDao
											.getParameterDetails(ConstantDocument.FX_DC, ConstantDocument.Yes).get(0)
											.getNumericField1();
				}
				for (ReceiptPaymentApp appl1 : listOfRecAppl) {
					totalcollectiontAmount = totalcollectiontAmount.add(appl1.getLocalNetAmount());
				}
				if (deliveryCharges != null && totalcollectiontAmount != null) {
					totalcollectiontAmount = totalcollectiontAmount.add(deliveryCharges);
				}
				collection.setDeliveryCharges(deliveryCharges);
				collection.setReceiptType(ConstantDocument.COLLECTION_RECEIPT_TYPE);
				collection.setApplicationCountryId(appl.getCountryId());
				collection.setAccountMMYYYY(appl.getAccountMMYYYY());
				collection.setCollectDate(new Date());
				ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(appl.getCompanyId());
				if (companyDetails != null) {
					collection.setCompanyCode(companyDetails.getCompanyCode());

				} else {
					throw new GlobalException(JaxError.INVALID_COMPANY_ID, "Invalid company code.");
				}
				collection.setFsCompanyMaster(new CompanyMaster(appl.getCompanyId()));
				CountryBranchMdlv1 countryBranch = countryBranchRepository.findByCountryBranchId(appl.getBranchId());
				if (countryBranch != null) {
					collection.setExBankBranch(countryBranch);
					collection.setLocCode(countryBranch.getBranchId());
				}

				collection.setCreatedDate(new Date());
				CurrencyMasterMdlv1 localCurrency = new CurrencyMasterMdlv1();
				localCurrency.setCurrencyId(appl.getLocalCurrencyId());
				collection.setExCurrencyMaster(localCurrency);
				collection.setIsActive(ConstantDocument.Yes);
				collection.setDocumentFinanceYear(appl.getDocumentFinanceYear());
				collection.setDocumentCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
				collection.setDocumentId(
						documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION).get(0)
								.getDocumentID());
				BigDecimal documentNo = generateDocumentNumber(countryBranch, appl.getCountryId(),
						companyDetails.getCompanyId(), ConstantDocument.A, appl.getDocumentFinanceYear(),
						ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
				if (documentNo != null && documentNo.compareTo(BigDecimal.ZERO) != 0) {
					collection.setDocumentNo(documentNo);
				} else {
					throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO,
							"Collection document should not be blank.");
				}

				Customer customer = new Customer();
				customer.setCustomerId(appl.getCustomerId());
				collection.setFsCustomer(customer);

				collection.setNetAmount(totalcollectiontAmount);
				collection.setPaidAmount(totalcollectiontAmount);

				if (!StringUtils.isBlank(metaData.getReferrer())) {
					collection.setCreatedBy(metaData.getReferrer());
				} else {
					if (!StringUtils.isBlank(metaData.getAppType())) {
						collection.setCreatedBy(metaData.getAppType());
					} else {
						collection.setCreatedBy("WEB");
					}
				}

			} else {
				logger.error("save saveCollection listOfRecAppl is empty :");
				throw new GlobalException(JaxError.NO_RECORD_FOUND, "NO record found");
			}
		} catch (GlobalException e) {
			logger.error("createFcSaleReceiptApplication", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save collection :" + e.getMessage());
		}

		return collection;

	}

	/** Save CollectDetailModel **/
	public CollectDetailMdlv1 saveCollectDetail(List<ReceiptPaymentApp> listOfRecAppl,
			PaymentResponseDto paymentResponse, CollectionMdlv1 collection) {
		CollectDetailMdlv1 collectDetail = new CollectDetailMdlv1();
		try {

			collectDetail.setFsCompanyMaster(collection.getFsCompanyMaster());
			collectDetail.setFsCustomer(collection.getFsCustomer());
			collectDetail.setExBankBranch(collection.getExBankBranch());
			collectDetail.setLocCode(collection.getLocCode());
			collectDetail.setFsCountryMaster(new CountryMaster(collection.getApplicationCountryId()));
			collectDetail.setAcyymm(collection.getAccountMMYYYY());
			collectDetail.setCompanyCode(collection.getCompanyCode());

			collectDetail.setCreatedDate(new Date());

			if (!StringUtils.isBlank(metaData.getReferrer())) {
				collectDetail.setCreatedBy(metaData.getReferrer());
			} else {
				if (!StringUtils.isBlank(metaData.getAppType())) {
					collectDetail.setCreatedBy(metaData.getAppType());
				} else {
					collectDetail.setCreatedBy("WEB");
				}
			}

			collectDetail.setDocumentCode(collection.getDocumentCode());
			collectDetail.setDocumentDate(new Date());
			collectDetail.setDocumentFinanceYear(collection.getDocumentFinanceYear());
			collectDetail.setDocumentId(collection.getDocumentId());
			collectDetail.setDocumentNo(collection.getDocumentNo());
			collectDetail.setExCurrencyMaster(collection.getExCurrencyMaster());
			collectDetail.setFsCustomer(collection.getFsCustomer());
			collectDetail.setCashCollectionId(collection);
			collectDetail.setCollAmt(collection.getNetAmount());

			collectDetail.setIsActive(ConstantDocument.Yes);
			collectDetail.setCollectionMode(ConstantDocument.KNET_CODE);
			PaymentModeModel payModeModel = payModeRepositoy.getPaymentModeDetails(ConstantDocument.KNET_CODE);
			if (payModeModel != null) {
				collectDetail.setPaymentModeId(payModeModel.getPaymentModeId());
			} else {
				throw new GlobalException(JaxError.INVALID_PAYMENT_MODE, "Paymnet mode is not found.");
			}

			collectDetail.setApprovalNo(paymentResponse.getAuth_appNo());
			collectDetail.setRefId(paymentResponse.getReferenceId());
			collectDetail.setTransId(paymentResponse.getTransactionId());
			collectDetail.setKnetReceipt(paymentResponse.getPostDate());
			collectDetail.setDocumentLineNo(new BigDecimal(1));
			collectDetail.setAuthdate(new Date());
			collectDetail.setPayId(paymentResponse.getPaymentId());
			collectDetail.setKnetReceiptDateTime(new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(new Date()));
			
		} catch (GlobalException e) {
			logger.error("createFcSaleReceiptApplication", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("save collection details :" + e.getMessage());
		}
		return collectDetail;
	}

	public BigDecimal generateDocumentNumber(CountryBranchMdlv1 countryBranch, BigDecimal appCountryId, BigDecimal companyId,
			String processInd, BigDecimal finYear, BigDecimal documentId) {
		BigDecimal branchId = countryBranch.getBranchId() == null ? ConstantDocument.ONLINE_BRANCH_LOC_CODE
				: countryBranch.getBranchId();
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,
				finYear, processInd, branchId);
		return (BigDecimal) output.get("P_DOC_NO");
	}

	private void logStatusChangeAuditEvent(BigDecimal deliveryDetailSeqId, String oldOrderStatus) {
		FxDeliveryDetailsModel deliveryDetailModel = rcptApplPaydao.getDeliveryDetailModel(deliveryDetailSeqId);
		CActivityEvent audit = new CActivityEvent(Type.FC_UPDATE, deliveryDetailModel.getDeleviryDelSeqId())
				.field("STATUS").from(oldOrderStatus).to(deliveryDetailModel.getOrderStatus());
		auditService.log(audit);
	}

	private void validateAmountMismatch(List<ReceiptPaymentApp> listOfRecAppl, PaymentResponseDto paymentResponse) {
		BigDecimal localCurrencyId = listOfRecAppl.get(0).getLocalCurrencyId();
		BigDecimal decimalNumber = currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber();
		BigDecimal totalApplAmount = listOfRecAppl.stream().map(ReceiptPaymentApp::getLocalNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal delCharges = fcSaleApplicationTransactionManager.getDeliveryChargesFromParameter();
		BigDecimal totalAmountWithCharges =totalApplAmount.add(delCharges); 
		BigDecimal paidAmount = new BigDecimal(paymentResponse.getAmount());
		totalAmountWithCharges = RoundUtil.roundBigDecimal(totalAmountWithCharges, decimalNumber.intValue());
		paidAmount = RoundUtil.roundBigDecimal(paidAmount, decimalNumber.intValue());
		if(JaxUtil.isNullZeroBigDecimalCheck(totalAmountWithCharges) && JaxUtil.isNullZeroBigDecimalCheck(paidAmount)) {
			if (!totalAmountWithCharges.equals(paidAmount)) {
				String errorMessage = String.format("paidAmount: %s and payableAmount: %s mismatch for customerId: %s", paidAmount,
						totalAmountWithCharges, metaData.getCustomerId());
				logger.info(errorMessage);
				notificationService.sendTransactionErrorAlertEmail(errorMessage, "Fxorder Amount mismatch", paymentResponse);
				throw new GlobalException("paid and payable amount mismatch");
			}
		}
	}
	
}
