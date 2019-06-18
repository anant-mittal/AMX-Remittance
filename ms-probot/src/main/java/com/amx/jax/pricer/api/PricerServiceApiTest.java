package com.amx.jax.pricer.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.PartnerDataService;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.ProbotDataService;
import com.amx.jax.pricer.ProbotExchangeRateService;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.service.PricerTestService;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;
import com.amx.utils.ArgUtil;

/**
 * The Class PricerServiceApiTest.
 */
@RestController
@RequestMapping("test/")
public class PricerServiceApiTest implements ProbotExchangeRateService, ProbotDataService, PartnerDataService {

	/** The rbaac service client. */
	@Autowired
	PricerServiceClient pricerServiceClient;

	@Autowired
	PricerTestService pricerTestService;

	// @Autowired
	// HolidayListService holidayListService;

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_PRICE_CUSTOMER, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchPriceForCustomer(PricingRequestDTO pricingRequestDTO) {
		return pricerServiceClient.fetchPriceForCustomer(pricingRequestDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_BASE_PRICE, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchBasePrice(PricingRequestDTO pricingRequestDTO) {
		return pricerServiceClient.fetchBasePrice(pricingRequestDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_DISCOUNTED_RATES, method = RequestMethod.POST)
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO) {
		return pricerServiceClient.fetchDiscountedRates(pricingRequestDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.FETCH_REMIT_ROUTES_PRICES, method = RequestMethod.POST)
	public AmxApiResponse<ExchangeRateAndRoutingResponse, Object> fetchRemitRoutesAndPrices(
			ExchangeRateAndRoutingRequest dprRequestDTO) {
		return pricerServiceClient.fetchRemitRoutesAndPrices(dprRequestDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_HOLIDAYS_DATE_RANGE, method = RequestMethod.POST)
	public AmxApiResponse<HolidayResponseDTO, Object> fetchHolidayList(
			@RequestParam(required = true) BigDecimal countryId,
			@RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

		return pricerServiceClient.fetchHolidayList(countryId, fromDate, toDate);

	}

	@RequestMapping(value = ApiEndPoints.PRICE_TEST, method = RequestMethod.POST)
	public ResponseEntity<byte[]> pricingLoadTest(@RequestParam("file") MultipartFile uploadfile)
			throws IOException, InterruptedException, ExecutionException {

		List<BigDecimal> customerList = new ArrayList<BigDecimal>();

		customerList.add(new BigDecimal(54));
		customerList.add(new BigDecimal(74));
		customerList.add(new BigDecimal(96));
		customerList.add(new BigDecimal(21));

		List<Channel> channelList = new ArrayList<>();

		channelList.add(Channel.KIOSK);
		channelList.add(Channel.ONLINE);
		channelList.add(Channel.BRANCH);

		List<BigDecimal> localAmtList = new ArrayList<>();
		localAmtList.add(new BigDecimal(1000));
		localAmtList.add(new BigDecimal(5000));
		localAmtList.add(new BigDecimal(10000));

		Map<String, PricingRequestDTO> pricingReqMap = new TreeMap<>();
		BufferedReader countryCurrencyReader = new BufferedReader(new InputStreamReader(uploadfile.getInputStream()));

		// FileWriter outFileWriter = new
		// FileWriter("/home/abhijeet/Desktop/results.json");

		// FileWriter errorFileWriter = new
		// FileWriter("/home/abhijeet/Desktop/error.txt");

		String line;
		while ((line = countryCurrencyReader.readLine()) != null) {
			line = line.trim();

			if (line.startsWith("#") || StringUtils.isBlank(line)) {
				continue;
			}

			String[] tokens = line.split("\t");

			PricingRequestDTO pricingRequestDTO = new PricingRequestDTO();

			pricingRequestDTO.setLocalCountryId(new BigDecimal(91));
			pricingRequestDTO.setForeignCountryId(new BigDecimal(tokens[1]));

			// Multiple
			// pricingRequestDTO.setLocalAmount(new BigDecimal(1000));

			pricingRequestDTO.setLocalCurrencyId(new BigDecimal(1));
			pricingRequestDTO.setForeignCurrencyId(new BigDecimal(tokens[3]));

			pricingRequestDTO.setPricingLevel(PRICE_BY.COUNTRY);

			pricingReqMap.put(pricingRequestDTO.getForeignCountryId() + "-" + pricingRequestDTO.getForeignCurrencyId(),
					pricingRequestDTO);
		}

		List<BigDecimal> branchIdList = new ArrayList<>();
		branchIdList.add(new BigDecimal(1));
		branchIdList.add(new BigDecimal(54));
		branchIdList.add(new BigDecimal(64));
		branchIdList.add(new BigDecimal(70));
		branchIdList.add(new BigDecimal(71));
		branchIdList.add(new BigDecimal(72));
		branchIdList.add(new BigDecimal(82));
		branchIdList.add(new BigDecimal(83));
		branchIdList.add(new BigDecimal(84));
		branchIdList.add(new BigDecimal(85));

		// PrintWriter outPrintWriter = new PrintWriter(outFileWriter);

		// PrintWriter errorPrintWriter = new PrintWriter(errorFileWriter);

		StringBuilder strBuilder = new StringBuilder();

		Map<Integer, PricingRequestDTO> requestMap = new HashMap<Integer, PricingRequestDTO>();

		long i = 1;

		Queue<Future<AmxApiResponse<PricingResponseDTO, Object>>> allFutureQueue = new ConcurrentLinkedQueue<>();

		StopWatch watch = new StopWatch();
		watch.start();

		for (BigDecimal customerId : customerList) {

			for (BigDecimal localAmt : localAmtList) {

				for (Channel channel : channelList) {

					for (PricingRequestDTO request : pricingReqMap.values()) {

						PricingRequestDTO pricingRequestDTO = new PricingRequestDTO();

						pricingRequestDTO.setLocalCountryId(request.getLocalCountryId());
						pricingRequestDTO.setForeignCountryId(request.getForeignCountryId());

						// Multiple
						// pricingRequestDTO.setLocalAmount(new BigDecimal(1000));

						pricingRequestDTO.setLocalCurrencyId(request.getLocalCurrencyId());
						pricingRequestDTO.setForeignCurrencyId(request.getForeignCurrencyId());

						pricingRequestDTO.setPricingLevel(PRICE_BY.COUNTRY);

						if (channel.equals(Channel.ONLINE)) {
							pricingRequestDTO.setCountryBranchId(new BigDecimal(78));
						} else if (channel.equals(Channel.KIOSK)) {
							pricingRequestDTO.setCountryBranchId(new BigDecimal(54));
						} else if (channel.equals(Channel.BRANCH)) {
							pricingRequestDTO.setCountryBranchId(new BigDecimal(64));
						}

						pricingRequestDTO.setCustomerId(customerId);

						pricingRequestDTO.setLocalAmount(localAmt);

						pricingRequestDTO.setChannel(channel);

						try {

							pricingRequestDTO.setInfo(new HashMap<String, Object>());
							// pricingRequestDTO.getInfo().put("thread", i++);

							Future<AmxApiResponse<PricingResponseDTO, Object>> amxFutureResp = pricerTestService
									.fetchPriceForCustomerAsynch(pricingRequestDTO);

							// errorPrintWriter.println(" Future Hash ==> " + amxFutureResp.hashCode());

							requestMap.put(amxFutureResp.hashCode(), pricingRequestDTO);

							allFutureQueue.add(amxFutureResp);

						} catch (Exception e) {
							// TODO: handle exception
							// Ignore
						}

					} // for

				} // ch

			} // local Amt

		} // cust

		// Write to the file

		boolean allDone = false;

		strBuilder.append("\n Result Size ==>  " + allFutureQueue.size() + " Count Size ==> " + i + " Hash Size ==>"
				+ requestMap.size());

		/**
		 * Query Params
		 */
		strBuilder.append("\nSr. No.");
		strBuilder.append(", ID");
		strBuilder.append(", CustomerId");
		strBuilder.append(", ForeignCountryId");
		strBuilder.append(", ForeignCurrencyId");
		strBuilder.append(", LocalAmount");

		strBuilder.append(", CountryBranchId");
		strBuilder.append(", PricingLevel");
		strBuilder.append(", Channel");

		/**
		 * Result Params
		 */
		strBuilder.append(", BankId");
		strBuilder.append(", BankCode");
		strBuilder.append(", BankCountryId");
		strBuilder.append(", ServiceIndicatorId");
		strBuilder.append(", Rate");
		strBuilder.append(", InverseRate");
		strBuilder.append(", ConvertedFCAmount");
		strBuilder.append(", ConvertedLCAmount");
		strBuilder.append(", TimeToExec");
		// strBuilder.append(", TRATE_IDS");
		// strBuilder.append(", RoutingHeaderIds");

		long srNo = 0;

		while (!allDone) {

			allDone = Boolean.TRUE;

			for (Future<AmxApiResponse<PricingResponseDTO, Object>> future : allFutureQueue) {

				if (future.isDone()) {

					srNo++;

					PricingRequestDTO requestParam = requestMap.get(future.hashCode());

					try {
						AmxApiResponse<PricingResponseDTO, Object> amxResp = future.get();

						PricingResponseDTO response = amxResp.getResult();

						// PricingRequestDTO requestParam = response.getRequest();

						long tte = ArgUtil.parseAsLong(response.getInfo().get(PricerServiceConstants.TTE));

						for (ExchangeRateDetails bankRate : response.getSellRateDetails()) {

							BankDetailsDTO bankDetails = response.getBankDetails().get(bankRate.getBankId());

							/**
							 * Query Params
							 */
							strBuilder.append("\n" + srNo);
							strBuilder.append(", " + future.hashCode());
							strBuilder.append(", " + requestParam.getCustomerId());
							strBuilder.append(", " + requestParam.getForeignCountryId());
							strBuilder.append(", " + requestParam.getForeignCurrencyId());
							strBuilder.append(", " + requestParam.getLocalAmount());
							strBuilder.append(", " + requestParam.getCountryBranchId());
							strBuilder.append(", " + requestParam.getPricingLevel());
							strBuilder.append(", " + requestParam.getChannel());

							/**
							 * Result Params
							 */
							strBuilder.append(", " + bankRate.getBankId());
							strBuilder.append(", " + bankDetails.getBankCode());
							strBuilder.append(", " + bankDetails.getBankCountryId());
							strBuilder.append(", " + bankRate.getServiceIndicatorId());
							strBuilder.append(", " + bankRate.getSellRateNet().getRate());
							strBuilder.append(", " + bankRate.getSellRateNet().getInverseRate());
							strBuilder.append(", " + bankRate.getSellRateNet().getConvertedFCAmount());
							strBuilder.append(", " + bankRate.getSellRateNet().getConvertedLCAmount());
							strBuilder.append(", " + tte);
							// strBuilder.append(", " + response.getInfo().get("TRATE_IDS"));
							// strBuilder.append(", " + response.getInfo().get("RH_IDS"));

						}

						// strBuilder.append("\n" + JsonUtil.toJson(amxResp.getResult()));

						// errorPrintWriter.println(" Response Future Hash ==> " + future.hashCode());

					} catch (Exception e) {

						Throwable cause = e.getCause().getCause();

						// errorPrintWriter.println(" Error Future Hash ==> " + future.hashCode());

						if ((cause instanceof PricerServiceException)) {

							PricerServiceException prCause = (PricerServiceException) cause;

							// PricingRequestDTO requestParam = prCause.getRequest();

							/**
							 * Query Params
							 */
							strBuilder.append("\n" + srNo);
							strBuilder.append(", " + future.hashCode());
							strBuilder.append(", " + requestParam.getCustomerId());
							strBuilder.append(", " + requestParam.getForeignCountryId());
							strBuilder.append(", " + requestParam.getForeignCurrencyId());
							strBuilder.append(", " + requestParam.getLocalAmount());
							strBuilder.append(", " + requestParam.getCountryBranchId());
							strBuilder.append(", " + requestParam.getPricingLevel());
							strBuilder.append(", " + requestParam.getChannel());

							strBuilder.append(", Pricing Error ");
							strBuilder.append(", " + prCause.getErrorKey());
							strBuilder.append(", " + prCause.getErrorMessage());

							// e.printStackTrace(errorPrintWriter);
						} else {

							// errorPrintWriter.println(" UNKNOWN_Error Future Hash ==> " +
							// future.hashCode());

							strBuilder.append("\n" + srNo);
							strBuilder.append(", " + future.hashCode());
							strBuilder.append(", " + requestParam.getCustomerId());
							strBuilder.append(", " + requestParam.getForeignCountryId());
							strBuilder.append(", " + requestParam.getForeignCurrencyId());
							strBuilder.append(", " + requestParam.getLocalAmount());
							strBuilder.append(", " + requestParam.getCountryBranchId());
							strBuilder.append(", " + requestParam.getPricingLevel());
							strBuilder.append(", " + requestParam.getChannel());

							strBuilder.append(", UnKnown Error: " + e.getMessage());

						}
					}

					allFutureQueue.remove(future);

				} else {
					allDone = Boolean.FALSE;
				}

			}

			Thread.sleep(10 * 1000);

			System.out.println(" <== Sleeping Now ==> ");
		}

		System.out.println(" ======= Done Now ======= ");

		// BufferedReader countryBranchReader = new BufferedReader(
		// new InputStreamReader(countryBranchFile.getInputStream()));

		// System.out.println("File 2 ==> " + countryCurrencyFile.getFilename());

		// outPrintWriter.close();
		// outFileWriter.close();

		// errorPrintWriter.close();
		// errorFileWriter.close();

		watch.stop();
		long timetaken = watch.getLastTaskTimeMillis();
		System.out.println("Total time taken to Complete the Process with size: " + allFutureQueue.size() + " : "
				+ timetaken / 1000 + " seconds OR : " + timetaken / (1000 * 60) + " Mins");

		HttpHeaders headers = new HttpHeaders();
		byte[] media = strBuilder.toString().getBytes();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "Results.csv" + "\"").body(media);

	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_ROUTBANKS_AND_SEVICES, method = RequestMethod.POST)
	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(
			@RequestParam(required = true) BigDecimal countryId, @RequestParam(required = true) BigDecimal currencyId) {
		// TODO Subodh To Fix This
		return pricerServiceClient.getRbanksAndServices(countryId, currencyId);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.SAVE_DISCOUNT_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(
			DiscountDetailsReqRespDTO discountMgmtReqDTO) {
		// TODO Subodh To Fix This
		return pricerServiceClient.saveDiscountDetails(discountMgmtReqDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_DISCOUNT_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemet(
			DiscountMgmtReqDTO discountMgmtReqDTO) {
		// TODO Subodh To Fix This
		return pricerServiceClient.getDiscountManagemet(discountMgmtReqDTO);
	}

	@RequestMapping(value = ApiEndPoints.SERVICE_TEST, method = RequestMethod.GET)
	public AmxApiResponse<Map<String, String>, Object> testService() {

		Map<String, String> testResp = new HashMap<String, String>();

		testResp.put("status", "OK");

		AmxApiResponse<Map<String, String>, Object> amxApiResponse = AmxApiResponse.build(testResp);

		return amxApiResponse;
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_CUR_GROUPING_DATA, method = RequestMethod.POST)
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData() {
		// TODO Subodh To Fix This
		return pricerServiceClient.getCurrencyGroupingData();
	}

	@Override
	@RequestMapping(value = ApiEndPoints.UPDATE_CUR_GROUP_ID, method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(
			@RequestParam(required = true) BigDecimal groupId, @RequestParam(required = true) BigDecimal currencyId) {
		// TODO Subodh To Fix This
		return pricerServiceClient.updateCurrencyGroupId(groupId, currencyId);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_CUR_BY_GROUP_ID, method = RequestMethod.POST)
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(
			@RequestParam(required = true) BigDecimal groupId) {
		// TODO Subodh To Fix This
		return pricerServiceClient.getCurrencyByGroupId(groupId);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_SERVICE_PROVIDER_QUOTE, method = RequestMethod.POST)
	public AmxApiResponse<SrvPrvFeeInqResDTO, Object> getServiceProviderQuotation(
			SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		return pricerServiceClient.getServiceProviderQuotation(srvPrvFeeInqReqDTO);
	}

}
