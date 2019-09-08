package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CustomerRating;
import com.amx.jax.dbmodel.ReceiptPayment;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dict.AmxEnums;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CustomerRatingDTO;
import com.amx.jax.model.response.remittance.CustomerBankRelationNameDto;
import com.amx.jax.partner.repository.ReceiptPaymenttRepository;
import com.amx.jax.repository.ICustomerRatingDao;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerRatingService {

	private Logger logger = Logger.getLogger(CustomerRatingService.class);

	@Autowired
	ICustomerRatingDao customerRatingdao;

	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;

	@Autowired
	RemittanceApplicationRepository remittanceApplicationRepository;

	@Autowired
	ReceiptPaymenttRepository receiptPaymenttRepository;

	@Autowired
	MetaData metaData;

	/**
	 * Saved customer rating
	 * 
	 * @param dto
	 * @return
	 */
	public AmxApiResponse<CustomerRating, ?> saveCustomerRating(CustomerRatingDTO dto) {
		CustomerRating customerRating = new CustomerRating();
		try {
			if (dto.getProdType().equals(AmxEnums.Products.REMIT)) {
				
				BigDecimal applicationCountryId = metaData.getCountryId();
				BigDecimal remittancetrnxId = dto.getRemittanceTransactionId();

				if (remittancetrnxId != null) {

					CustomerRating customerRatingvalue = customerRatingdao
							.getCustomerRatingDataByRemittanceTransactionId(remittancetrnxId);

					if (customerRatingvalue != null) {

						logger.info("Transaction Details are already Rated for the Remittance transaction ID"
								+ remittancetrnxId);
						throw new GlobalException(JaxError.TRANSACTION_ALREADY_RATED.getStatusKey(),
								"Transaction Details are already Rated for the Remittance transaction ID");

					} else {
						RemittanceTransaction remittanceApplicationTxnxId = remittanceTransactionRepository
								.findByRemittanceTransactionId(dto.getRemittanceTransactionId());
						if (remittanceApplicationTxnxId != null) {
							RemittanceApplication remitAPPLTrnx = remittanceApplicationRepository
									.getRemittanceApplicationId(remittanceApplicationTxnxId.getApplicationDocumentNo(),
											remittanceApplicationTxnxId.getDocumentFinanceYear());

							if (remitAPPLTrnx != null) {

								customerRating.setRating(dto.getRating());
								customerRating.setRatingRemark(dto.getRatingRemark());
								customerRating.setRemittanceApplicationId(remitAPPLTrnx.getRemittanceApplicationId());
								customerRating.setRemittanceTransactionId(dto.getRemittanceTransactionId());
								customerRating.setCustomerId(remitAPPLTrnx.getFsCustomer().getCustomerId());
								customerRating.setApplicationCountryId(applicationCountryId);
								customerRating.setCreatedDate(new Date());
								customerRating.setFeedbackType(AmxEnums.Products.REMIT.toString());
								customerRatingdao.save(customerRating);
							} else {
								throw new GlobalException(JaxError.INVALID_TRANSACTION_ID.getStatusKey(),
										"Invalid transaction ID");
							}
						} else {
							throw new GlobalException(JaxError.INVALID_TRANSACTION_ID.getStatusKey(),
									"Invalid transaction ID");
						}
					}
				}

			} else {

				BigDecimal applicationCountryId = metaData.getCountryId();
				BigDecimal fxOrdertrnxId = dto.getRemittanceTransactionId();

				if (fxOrdertrnxId != null) {

					CustomerRating customerRatingvalue = customerRatingdao
							.getCustomerRatingDataBycollectionDocNo(fxOrdertrnxId);

					if (customerRatingvalue != null) {

						logger.info("Transaction Details are already Rated for the Remittance transaction ID"
								+ fxOrdertrnxId);
						throw new GlobalException(JaxError.TRANSACTION_ALREADY_RATED.getStatusKey(),
								"Transaction Details are already Rated for the Remittance transaction ID");

					} else {
						BigDecimal docno = BigDecimal.ZERO;
						BigDecimal docfyr = BigDecimal.ZERO;

						List<Object[]> colldocyear = receiptPaymenttRepository.findByDeliveryDetSeqId(fxOrdertrnxId);
						if (colldocyear != null && colldocyear.size() != 0) {
							for (Object object : colldocyear) {
								Object[] coldet = (Object[]) object;
								if (coldet[0] != null) {
									docno = new BigDecimal(coldet[0].toString());
								}
								if (coldet[1] != null) {
									docfyr = new BigDecimal(coldet[1].toString());
								}
							}
						}

						if (docno != null && docfyr != null) {
							List<ReceiptPayment> receiptPayment = receiptPaymenttRepository
									.findDeliveryDetSeqId(fxOrdertrnxId);

							customerRating.setRating(dto.getRating());
							customerRating.setRatingRemark(dto.getRatingRemark());
							customerRating.setCollectionDocNo(docno);
							customerRating.setCollectionDocFyr(docfyr);
							customerRating.setCustomerId(receiptPayment.get(0).getFsCustomer().getCustomerId());
							customerRating.setApplicationCountryId(applicationCountryId);
							customerRating.setCreatedDate(new Date());
							customerRating.setFeedbackType(AmxEnums.Products.FXORDER.toString());
							customerRating.setDelvSeqId(fxOrdertrnxId);
							customerRatingdao.save(customerRating);
						} else {
							throw new GlobalException(JaxError.INVALID_TRANSACTION_ID.getStatusKey(),
									"Invalid transaction ID");
						}
					}

				}

			}
		}

		catch (GlobalException e) {
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}
		
		return AmxApiResponse.build();
	}

	/**
	 * Inquire customer rating
	 * 
	 */

	public AmxApiResponse<CustomerRating, ?> inquireCustomerRating(BigDecimal remittanceTrnxId, String product) {

		CustomerRating customerRating = new CustomerRating();
		try {

			if (remittanceTrnxId != null) {

				CustomerRating customerRatingvalue = customerRatingdao
						.getCustomerRatingDataByRemittanceTransactionId(remittanceTrnxId);
				if (customerRatingvalue != null) {

					customerRating.setRating(customerRatingvalue.getRating());
					customerRating.setApplicationCountryId(customerRatingvalue.getApplicationCountryId());
					customerRating.setCreatedDate(customerRatingvalue.getCreatedDate());
					customerRating.setCustomerId(customerRatingvalue.getCustomerId());
					customerRating.setRemittanceTransactionId(customerRatingvalue.getRemittanceTransactionId());
					customerRating.setRatingId(customerRatingvalue.getRatingId());
					customerRating.setRatingRemark(customerRatingvalue.getRatingRemark());
					customerRating.setRemittanceApplicationId(customerRatingvalue.getRemittanceApplicationId());
					customerRating.setFeedbackType(AmxEnums.Products.REMIT.toString());

				} else {

					// do nothing
				}
			}

		} catch (GlobalException e) {
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}
		return AmxApiResponse.build(customerRating);
	}

	/**
	 * Inquire fx-order customer rating
	 * 
	 */
	public AmxApiResponse<CustomerRating, ?> fxOrderinquireCustomerRating(BigDecimal fxOrdertrnxId, String product) {

		CustomerRating customerRating = new CustomerRating();

		try {

				if (fxOrdertrnxId != null) {

				
						CustomerRating customerRatingvalue = customerRatingdao
								.getCustomerRatingDataBydelvSeqId(fxOrdertrnxId);
						if (customerRatingvalue != null) {

							customerRating.setRating(customerRatingvalue.getRating());
							customerRating.setApplicationCountryId(customerRatingvalue.getApplicationCountryId());
							customerRating.setCreatedDate(customerRatingvalue.getCreatedDate());
							customerRating.setCustomerId(customerRatingvalue.getCustomerId());
							customerRating.setCollectionDocNo(customerRatingvalue.getCollectionDocNo());
							customerRating.setRatingId(customerRatingvalue.getRatingId());
							customerRating.setRatingRemark(customerRating.getRatingRemark());
							customerRating.setCollectionDocFyr(customerRatingvalue.getCollectionDocFyr());
							customerRating.setFeedbackType(AmxEnums.Products.FXORDER.toString());
							customerRating.setDelvSeqId(fxOrdertrnxId);
						
						} else {

							// DO NOTHING

						}
					}
				
			

		} catch (GlobalException e) {
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}

		return AmxApiResponse.build(customerRating);
	}
}
