package com.amx.jax.pricer.api;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.pricer.PricerService;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.service.PricerTestService;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;
import com.amx.utils.JsonUtil;

/**
 * The Class PricerServiceApiTest.
 */
@RestController
@RequestMapping("test/")
public class PricerServiceApiTest implements PricerService {

	/** The rbaac service client. */
	@Autowired
	PricerServiceClient pricerServiceClient;

	@Autowired
	PricerTestService pricerTestService;

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

	@RequestMapping(value = ApiEndPoints.PRICE_TEST, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> pricingLoadTest(@RequestParam("file") MultipartFile uploadfile)
			throws IOException, InterruptedException, ExecutionException {

		System.out.println(" File Name ==> " + uploadfile.getName());

		List<BigDecimal> customerList = new ArrayList<BigDecimal>();
		customerList.add(new BigDecimal(21));
		customerList.add(new BigDecimal(54));
		customerList.add(new BigDecimal(74));
		customerList.add(new BigDecimal(96));

		List<Channel> channelList = new ArrayList<>();
		channelList.add(Channel.BRANCH);
		channelList.add(Channel.KIOSK);
		channelList.add(Channel.ONLINE);

		List<BigDecimal> localAmtList = new ArrayList<>();
		localAmtList.add(new BigDecimal(1000));
		localAmtList.add(new BigDecimal(5000));
		localAmtList.add(new BigDecimal(10000));

		Map<String, PricingRequestDTO> pricingReqMap = new TreeMap<>();
		BufferedReader countryCurrencyReader = new BufferedReader(new InputStreamReader(uploadfile.getInputStream()));

		FileWriter fileWriter = new FileWriter("/home/abhijeet/Desktop/results.json");

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
			pricingRequestDTO.setLocalAmount(new BigDecimal(1000));

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

		PrintWriter printWriter = new PrintWriter(fileWriter);

		long i = 1;

		List<Future<AmxApiResponse<PricingResponseDTO, Object>>> allFutureList = new ArrayList<>();

		for (BigDecimal customerId : customerList) {

			for (BigDecimal localAmt : localAmtList) {

				for (Channel channel : channelList) {

					for (PricingRequestDTO pricingRequestDTO : pricingReqMap.values()) {
						if (channel.equals(Channel.ONLINE)) {
							pricingRequestDTO.setCountryBranchId(new BigDecimal(78));
						} else if (channel.equals(Channel.KIOSK)) {
							pricingRequestDTO.setCountryBranchId(new BigDecimal(54));
						} else if (channel.equals(Channel.BRANCH)) {
							pricingRequestDTO.setCountryBranchId(new BigDecimal(72));
						}

						pricingRequestDTO.setCustomerId(customerId);

						pricingRequestDTO.setLocalAmount(localAmt);

						pricingRequestDTO.setChannel(channel);

						System.out.println(" Pricing Request Dto ==> " + JsonUtil.toJson(pricingRequestDTO));

						try {

							System.out.println(" @#@#$@#$#%#% PRE THREAD ==> " + i + "\n\n");

							pricingRequestDTO.setInfo(new HashMap<String, Object>());
							pricingRequestDTO.getInfo().put("thread", i);

							Future<AmxApiResponse<PricingResponseDTO, Object>> amxFutureResp = pricerTestService
									.fetchPriceForCustomerAsynch(pricingRequestDTO);

							allFutureList.add(amxFutureResp);

							System.out.println(" @#@#$@#$#%#%POST THREAD ==> " + i++ + "\n\n");

						} catch (Exception e) {
							// TODO: handle exception
						}

					} // for

				} // ch

			} // local Amt

		} // cust

		// Write to the file

		boolean allDone = false;

		while (!allDone) {

			for (Future<AmxApiResponse<PricingResponseDTO, Object>> future : allFutureList) {
				allDone = Boolean.TRUE;

				if (future.isDone()) {

					try {
						AmxApiResponse<PricingResponseDTO, Object> amxResp = future.get();
						printWriter.println(JsonUtil.toJson(amxResp.getResult()));
					} catch (Exception e) {
						// TODO: handle exception
					}
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

		printWriter.close();

		return AmxApiResponse.build("Success");

	}

}
