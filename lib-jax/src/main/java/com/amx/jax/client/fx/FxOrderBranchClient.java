package com.amx.jax.client.fx;



import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.customer.CustomerRatingDTO;
import com.amx.jax.model.request.fx.FcDeliveryBranchOrderSearchRequest;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.request.fx.FcSaleOrderManagementDatesRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.FxDeliveryTimeSlotDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.rest.RestService;

@Component
public class FxOrderBranchClient implements IFxBranchOrderService {
	
	private static final Logger LOGGER = Logger.getLogger(FxOrderBranchClient.class);
	
	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;
	
	
	
	/**
	 * 
	 * @return : To get the fx pending order management list
	 */
	@Override
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderManagement() {
		
			LOGGER.debug("in fetchBranchOrderManagement :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_PENDING_ORDER_MANAGEMENT).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderManagementDTO, Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the fx pending order management list
	 */
	@Override
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderDetails(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in fetchBranchOrderDetails :"+orderNumber+ " "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_FETCH_ORDER_MANAGEMENT).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderManagementDTO, Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the fx fetch user stock by currency list
	 */
	@Override
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(BigDecimal foreignCurrencyId) {
		
			LOGGER.debug("in fetchBranchStockDetailsByCurrency :"+foreignCurrencyId);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_FETCH_STOCK_CURRENCY).meta(new JaxMetaInfo())
					.queryParam(Params.FX_CURRENCY_ID, foreignCurrencyId)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto, Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the fx fetch user stock list
	 */
	@Override
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails() {
		
			LOGGER.debug("in fetchBranchStockDetails :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_FETCH_STOCK).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the fx fetch driver list
	 */
	@Override
	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchBranchEmployee() {
		
			LOGGER.debug("in fetchBranchEmployee :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_EMLOYEE_DRIVERS).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcEmployeeDetailsDto,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the save assign driver
	 */
	@Override
	public AmxApiResponse<BoolRespModel,Object> assignDriver(BigDecimal orderNumber,BigDecimal orderYear,BigDecimal driverId) {
		
			LOGGER.debug("in assignDriver :"+orderNumber +" "+driverId);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_ASSIGN_DRIVER).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear).meta(new JaxMetaInfo())
					.queryParam(Params.FX_DRIVER_ID, driverId)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the save print order save
	 */
	@Override
	public AmxApiResponse<FxOrderReportResponseDto,Object> printOrderSave(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest) {
		
			LOGGER.debug("in printOrderSave :"+fcSaleBranchDispatchRequest);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_PRINT_ORDER_SAVE).meta(new JaxMetaInfo())
					.post(fcSaleBranchDispatchRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<FxOrderReportResponseDto,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the accept order lock
	 */
	@Override
	public AmxApiResponse<BoolRespModel,Object> acceptOrderLock(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in acceptOrderLock :"+orderNumber +" "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_ACCEPT_ORDER_LOCK).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the release order lock
	 */
	@Override
	public AmxApiResponse<BoolRespModel,Object> releaseOrderLock(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in releaseOrderLock :"+orderNumber +" "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_RELEASE_ORDER_LOCK).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the dispatch order
	 */
	@Override
	public AmxApiResponse<BoolRespModel,Object> dispatchOrder(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in dispatchOrder :"+orderNumber +" "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_DISPATCH_ORDER).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the acknowledgement Driver
	 */
	@Override
	public AmxApiResponse<BoolRespModel,Object> acknowledgeDrive(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in acknowledgeDrive :"+orderNumber +" "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_ACKNOWLEDGE_DRIVE).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the return Acknowledge
	 */
	@Override
	public AmxApiResponse<BoolRespModel,Object> returnAcknowledge(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in returnAcknowledge :"+orderNumber +" "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_RETURN_ACKNOWLEDGE).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the accept Cancellation
	 */
	@Override
	public AmxApiResponse<BoolRespModel,Object> acceptCancellation(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in acceptCancellation :"+orderNumber +" "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_ACCEPT_CANCELLATION).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		
	}
	
	/**
	 * 
	 * @return : To get the re-print order
	 */
	@Override
	public AmxApiResponse<FxOrderReportResponseDto,Object> reprintOrder(BigDecimal orderNumber,BigDecimal orderYear) {
		
			LOGGER.debug("in reprintOrder :"+orderNumber +" "+orderYear);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_REPRINT_ORDER).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_YEAR, orderYear)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxOrderReportResponseDto,Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<FxOrderTransactionHistroyDto, Object> searchOrder(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest) {
		
			LOGGER.debug("in searchOrder :"+fcDeliveryBranchOrderSearchRequest);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SEARCH_ORDER).meta(new JaxMetaInfo())
					.post(fcDeliveryBranchOrderSearchRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<FxOrderTransactionHistroyDto,Object>>() {
					});
		
	}
	
	
	@Override
	public AmxApiResponse<BoolRespModel,Object> saveFcDeliveryTimeSlot(FxDeliveryTimeSlotDto fxDeliveryTimeSlotDto) {
		try {
			LOGGER.debug("in saveFcDeliveryTimeSlot :"+fxDeliveryTimeSlotDto);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_ORDER_DELIVERY_TIME_SETUP).meta(new JaxMetaInfo())
					.post(fxDeliveryTimeSlotDto)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in acceptOrderLock : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

	@Override
	public AmxApiResponse<FxDeliveryTimeSlotDto,Object> fetchFcDeliveryTiming() {
		try {
			LOGGER.debug("in saveFcDeliveryTimeSlot :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_ORDER_DELIVERY_TIME_SETUP_FETCH).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxDeliveryTimeSlotDto,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in acceptOrderLock : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}


	
	public AmxApiResponse<CustomerRatingDTO, ?> inquirefxOrderCustomerRating(BigDecimal deliveryDetailSeqId, String product) 
	{
	
			return restService.ajax(appConfig.getJaxURL()).path(Path.FC_CUSTOMER_RATING).meta(new JaxMetaInfo()).post()
					.queryParam(Params.FX_DELIVERY_SEQ_ID, deliveryDetailSeqId).queryParam(Params.FX_PRODUCT, product)
					.post().as(new ParameterizedTypeReference<AmxApiResponse<CustomerRatingDTO, ?>>() {
					});
		
	}
	

	@Override
	public AmxApiResponse<FcSaleOrderManagementDTO, Object> searchOrderByDates(FcSaleOrderManagementDatesRequest fcSaleDates) {
		
			LOGGER.debug("in searchOrderByDates :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SEARCH_ORDER_BY_DATES).meta(new JaxMetaInfo())
					.post(fcSaleDates)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderManagementDTO, Object>>() {
					});
		
	}

}

