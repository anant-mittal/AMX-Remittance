package com.amx.jax.branch.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.fx.FxOrderBranchClient;
import com.amx.jax.client.fx.IFxBranchOrderService.Path;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.model.request.fx.FcDeliveryBranchOrderSearchRequest;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.request.fx.FcSaleOrderManagementDatesRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@PreAuthorize("hasPermission('ORDER_MGMT.FXORDER', 'VIEW')")
@RestController
@Api(value = "Order Management APIs")
public class FxOrderBranchController {
	@Autowired
	private FxOrderBranchClient fxOrderBranchClient;

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
		return fxOrderBranchClient.acceptOrderLock(orderNumber, orderYear);
	}
	
	@RequestMapping(value = "/api/fxo/order/release", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> releaseOrderLock(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		return fxOrderBranchClient.releaseOrderLock(orderNumber, orderYear);
	}
	
	@PreAuthorize("hasPermission('ORDER_MGMT.FCINQUIRY', 'VIEW') or hasPermission('ORDER_MGMT.FXORDER', 'VIEW')")
	@RequestMapping(value = "/api/fxo/order/details",  method = { RequestMethod.POST })
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> getOrderDetails(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		return fxOrderBranchClient.fetchBranchOrderDetails(orderNumber,orderYear);
	}
	
	@PreAuthorize("hasPermission('ORDER_MGMT.FCINQUIRY', 'VIEW') or hasPermission('ORDER_MGMT.FXORDER', 'VIEW')")
	@RequestMapping(value = "/api/fxo/currency/stock",  method = { RequestMethod.POST })
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(@RequestParam(value = "currencyId", required = true) BigDecimal foreignCurrencyId){
		return fxOrderBranchClient.fetchBranchStockDetailsByCurrency(foreignCurrencyId);
	}
	
	@PreAuthorize("hasPermission('ORDER_MGMT.FCINQUIRY', 'VIEW') or hasPermission('ORDER_MGMT.FXORDER', 'VIEW')")
	@RequestMapping(value = "/api/fxo/currency/stock",  method = { RequestMethod.GET })
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails(){
		return fxOrderBranchClient.fetchBranchStockDetails();
	}
	
	@RequestMapping(value = "/api/fxo/order/print",  method = { RequestMethod.POST }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE,
			CommonMediaType.APPLICATION_PDF_VALUE, CommonMediaType.TEXT_HTML_VALUE })
	public ResponseEntity<byte[]> getFxOrderTransactionReport(
			@RequestParam("ext") File.Type ext,
			@RequestParam(required = false) Boolean duplicate,
			@RequestBody FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest,
			HttpServletResponse response) {

		duplicate = ArgUtil.parseAsBoolean(duplicate, false);
		BigDecimal documentNo = fcSaleBranchDispatchRequest.getCollectionDocumentNo();
		BigDecimal documentYear = fcSaleBranchDispatchRequest.getCollectionDocumentYear();
		
		AmxApiResponse<FxOrderReportResponseDto, Object> wrapper = duplicate ? 
				fxOrderBranchClient.reprintOrder(documentNo, documentYear) : 
				fxOrderBranchClient.printOrderSave(fcSaleBranchDispatchRequest);
				
		if (File.Type.PDF.equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.FXO_RECEIPT_BRANCH : TemplatesMX.FXO_RECEIPT_BRANCH,
							wrapper, File.Type.PDF).lang(AppContextUtil.getTenant().defaultLang()))
					.getResult();
			// file.create(response, false);
			// return null;
			file.setName(file.getITemplate().getFileName() + '_' + 
					documentNo.toString() + '_' + 
					documentYear.toString() + ".pdf");
			return PostManUtil.download(file);

		} else if (File.Type.HTML.equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.FXO_RECEIPT_BRANCH : TemplatesMX.FXO_RECEIPT_BRANCH,
							wrapper, File.Type.HTML).lang(AppContextUtil.getTenant().defaultLang()))
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
		return fxOrderBranchClient.printOrderSave(fcSaleBranchDispatchRequest);
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

		return fxOrderBranchClient.assignDriver(orderNumber, orderYear,driverId);
	}
	
	@RequestMapping(value = "/api/fxo/order/dispatch",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> dispatchOrder(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		return fxOrderBranchClient.dispatchOrder(orderNumber, orderYear);
	}
	
	@RequestMapping(value = "/api/fxo/order/acknowledge/return",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> acknowledgeReturn(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear) {
		return fxOrderBranchClient.returnAcknowledge(orderNumber, orderYear);
	}
	
	@RequestMapping(value = "/api/fxo/order/acknowledge/cancel",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> acknowledgeCancel(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear) {
		return fxOrderBranchClient.acceptCancellation(orderNumber, orderYear);
	}
	
	@PreAuthorize("hasPermission('ORDER_MGMT.FCINQUIRY', 'VIEW')")
	@RequestMapping(value = "/api/fxo/order/inquiry", method = { RequestMethod.POST })
	public AmxApiResponse<FxOrderTransactionHistroyDto,Object> getPastOrdersList(@RequestBody FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest){
		return fxOrderBranchClient.searchOrder(fcDeliveryBranchOrderSearchRequest);
	}
	
	@RequestMapping(value = "/api/fxo/order/search", method = RequestMethod.POST)
	public AmxApiResponse<FcSaleOrderManagementDTO, Object> searchOrderByDates(
			@RequestBody FcSaleOrderManagementDatesRequest fcSaleDates) {
		/*java.util.Date utilFromDate =  new java.util.Date(fromDate.getTime());
		java.util.Date utilToDate =  new java.util.Date(toDate.getTime());*/
		return fxOrderBranchClient.searchOrderByDates(fcSaleDates);

	}
}
