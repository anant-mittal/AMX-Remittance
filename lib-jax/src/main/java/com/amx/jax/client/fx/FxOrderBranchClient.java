package com.amx.jax.client.fx;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
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
		try {
			LOGGER.debug("in fetchBranchOrderManagement :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_PENDING_ORDER_MANAGEMENT).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderManagementDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchBranchOrderManagement : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}
	
	/**
	 * 
	 * @return : To get the fx pending order management list
	 */
	@Override
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderDetails(BigDecimal orderNumber) {
		try {
			LOGGER.debug("in fetchBranchOrderDetails :"+orderNumber);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_FETCH_ORDER_MANAGEMENT).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderManagementDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchBranchOrderDetails : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}
	
	/**
	 * 
	 * @return : To get the fx fetch user stock by currency list
	 */
	@Override
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(BigDecimal foreignCurrencyId) {
		try {
			LOGGER.debug("in fetchBranchStockDetailsByCurrency :"+foreignCurrencyId);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_FETCH_STOCK_CURRENCY).meta(new JaxMetaInfo())
					.queryParam(Params.FX_CURRENCY_ID, foreignCurrencyId)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchBranchStockDetailsByCurrency : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}
	
	/**
	 * 
	 * @return : To get the fx fetch user stock list
	 */
	@Override
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails() {
		try {
			LOGGER.debug("in fetchBranchOrderManagement :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_FETCH_STOCK).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserStockDto,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchBranchOrderManagement : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}
	
	/**
	 * 
	 * @return : To get the fx fetch driver list
	 */
	@Override
	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchBranchEmployee() {
		try {
			LOGGER.debug("in fetchBranchEmployee :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_EMLOYEE_DRIVERS).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcEmployeeDetailsDto,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in fetchBranchEmployee : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}
	
	/**
	 * 
	 * @return : To get the save assign driver
	 */
	@Override
	public AmxApiResponse<Boolean,Object> assignDriver(BigDecimal orderNumber,BigDecimal driverId) {
		try {
			LOGGER.debug("in assignDriver :"+orderNumber +" "+driverId);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_ASSIGN_DRIVER).meta(new JaxMetaInfo())
					.queryParam(Params.FX_ORDER_NUMBER, orderNumber).meta(new JaxMetaInfo())
					.queryParam(Params.FX_DRIVER_ID, driverId)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<Boolean,Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in assignDriver : ", e);
			return JaxSystemError.evaluate(e);
		} // end of try-catch
	}

}
