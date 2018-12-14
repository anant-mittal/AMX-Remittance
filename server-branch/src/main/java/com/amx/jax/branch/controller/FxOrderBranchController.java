package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.JaxStompClient;
import com.amx.jax.client.fx.FxOrderBranchClient;
import com.amx.jax.dict.AmxEnums.FxOrderStatus;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.utils.PostManUtil;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@PreAuthorize("hasPermission('CUSTOMER_MGMT.FXORDER', 'VIEW')")
@RestController
@Api(value = "Order Management APIs")
public class FxOrderBranchController {
	@Autowired
	private FxOrderBranchClient fxOrderBranchClient;

	@Autowired
	private JaxStompClient jaxStompClient;

	@Autowired
	private PostManService postManService;

	@RequestMapping(value = "/api/fxo/order/list", method = { RequestMethod.GET })
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> getOrderList(){
		return fxOrderBranchClient.fetchBranchOrderManagement();
	}
	
	@RequestMapping(value = "/api/fxo/order/accept", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> acceptOrder(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		AmxApiResponse<BoolRespModel, Object> response = fxOrderBranchClient.acceptOrderLock(orderNumber, orderYear);
		jaxStompClient.publishFxOrderStatusChange(orderNumber, orderYear, FxOrderStatus.ACP);
		return response;
	}
	
	@RequestMapping(value = "/api/fxo/order/release", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> releaseOrderLock(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		AmxApiResponse<BoolRespModel, Object> response =  fxOrderBranchClient.releaseOrderLock(orderNumber, orderYear);
		jaxStompClient.publishFxOrderStatusChange(orderNumber, orderYear, FxOrderStatus.ORD);
		return response;
	}
	
	@RequestMapping(value = "/api/fxo/order/details",  method = { RequestMethod.POST })
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> getOrderDetails(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		AmxApiResponse<FcSaleOrderManagementDTO, Object> orderDetails = fxOrderBranchClient.fetchBranchOrderDetails(orderNumber,
				orderYear);
		return orderDetails;
	}
	
	@RequestMapping(value = "/api/fxo/currency/stock",  method = { RequestMethod.POST })
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(@RequestParam(value = "currencyId", required = true) BigDecimal foreignCurrencyId){
		return fxOrderBranchClient.fetchBranchStockDetailsByCurrency(foreignCurrencyId);
	}
	
	@RequestMapping(value = "/api/fxo/currency/stock",  method = { RequestMethod.GET })
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails(){
		return fxOrderBranchClient.fetchBranchStockDetails();
	}
	
	@RequestMapping(value = "/api/fxo/order/print.{ext}",  method = { RequestMethod.POST }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE,
			CommonMediaType.APPLICATION_PDF_VALUE, CommonMediaType.TEXT_HTML_VALUE })
	public ResponseEntity<byte[]> getFxOrderTransactionReport(
			@PathVariable("ext") File.Type ext,
			@RequestParam(required = false) Boolean duplicate,
			@RequestBody FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest,
			HttpServletResponse response) {

		duplicate = ArgUtil.parseAsBoolean(duplicate, false);
		BigDecimal documentNo = fcSaleBranchDispatchRequest.getCollectionDocumentNo();
		BigDecimal documentYear = fcSaleBranchDispatchRequest.getCollectionDocumentYear();
		
		AmxApiResponse<FxOrderReportResponseDto, Object> wrapper = duplicate ? 
				fxOrderBranchClient.reprintOrder(documentNo, documentYear) : 
				fxOrderBranchClient.printOrderSave(fcSaleBranchDispatchRequest);
				
		jaxStompClient.publishFxOrderStatusChange(documentNo, documentYear, FxOrderStatus.PCK);

		if (File.Type.PDF.equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.FXO_RECEIPT : TemplatesMX.FXO_RECEIPT,
							wrapper, File.Type.PDF))
					.getResult();
			// file.create(response, false);
			// return null;
			file.setName(file.getITemplate().getFileName() + '_' + 
					documentNo.toString() + '_' + 
					documentYear.toString() + ".pdf");
			return PostManUtil.download(file);

		} else if (File.Type.HTML.equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.FXO_RECEIPT : TemplatesMX.FXO_RECEIPT,
							wrapper, File.Type.HTML))
					.getResult();
			
			file.setName(file.getITemplate().getFileName() + '_' +
					documentNo.toString() + '_' + 
					documentYear.toString() + ".html");
			// return file.getContent();
			return PostManUtil.download(file);

		} else {
			// return JsonUtil.toJson(wrapper);
			String json = JsonUtil.toJson(AmxApiResponse.build(wrapper));
			return ResponseEntity.ok().contentLength(json.length())
					.contentType(MediaType.valueOf(File.Type.JSON.getContentType())).body(json.getBytes());
		}

	}
	
	@RequestMapping(value = "/api/fxo/order/pack",  method = { RequestMethod.POST })
	public AmxApiResponse<FxOrderReportResponseDto,Object> saveAndPrintReceipt(@RequestBody FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest){
		BigDecimal documentNo = fcSaleBranchDispatchRequest.getCollectionDocumentNo();
		BigDecimal documentYear = fcSaleBranchDispatchRequest.getCollectionDocumentYear();
		AmxApiResponse<FxOrderReportResponseDto, Object> response = fxOrderBranchClient.printOrderSave(fcSaleBranchDispatchRequest);
		jaxStompClient.publishFxOrderStatusChange(documentNo, documentYear, FxOrderStatus.PCK);
		return response;
	}
	
	@RequestMapping(value = "/api/fxo/drivers",  method = { RequestMethod.GET })
	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchDriverList(){
		return fxOrderBranchClient.fetchBranchEmployee();
	}
	
	@RequestMapping(value = "/api/fxo/driver/assign",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> assignDriver(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "driverId", required = true) BigDecimal driverId,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){

		AmxApiResponse<BoolRespModel, Object> response = fxOrderBranchClient.assignDriver(orderNumber, orderYear,driverId);
		jaxStompClient.publishFxOrderStatusChange(orderNumber, orderYear, FxOrderStatus.OFD_ACK);
		return response;
	}
	
	@RequestMapping(value = "/api/fxo/order/dispatch",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> dispatchOrder(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		AmxApiResponse<BoolRespModel, Object> response = fxOrderBranchClient.dispatchOrder(orderNumber, orderYear);
		jaxStompClient.publishFxOrderStatusChange(orderNumber, orderYear, FxOrderStatus.OFD);
		return response;
	}
	
	@RequestMapping(value = "/api/fxo/order/acknowledge/return",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> acknowledgeReturn(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear) {
		AmxApiResponse<BoolRespModel, Object> response = fxOrderBranchClient.returnAcknowledge(orderNumber, orderYear);
		jaxStompClient.publishFxOrderStatusChange(orderNumber, orderYear, FxOrderStatus.RTD);
		return response;
	}
	
	@RequestMapping(value = "/api/fxo/order/acknowledge/cancel",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> acknowledgeCancel(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear) {
		AmxApiResponse<BoolRespModel, Object> response = fxOrderBranchClient.acceptCancellation(orderNumber, orderYear);
		jaxStompClient.publishFxOrderStatusChange(orderNumber, orderYear, FxOrderStatus.CND);
		return response;
	}
	
}
