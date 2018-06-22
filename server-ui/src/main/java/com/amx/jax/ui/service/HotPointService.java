package com.amx.jax.ui.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.postman.FBPushService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.ui.WebAppConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Component
public class HotPointService {

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	@JsonPropertyOrder({ "latitude", "longitude" })
	public static enum HotPoints {

		SALMIYA2(" 29.331993", "48.061422"), MURGAB3("29.369429", "47.978551"), SALMIYA4(" 29.325602", "48.058039");

		private String latitude;
		private String longitude;
		private String id;

		HotPoints(String latitude, String longitude) {
			this.id = this.name();
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	@Autowired
	private JaxService jaxService;

	@Autowired
	FBPushService fBPushService;

	@Autowired
	private WebAppConfig webAppConfig;

	// @Async
	public List<String> notify(BigDecimal customerId) throws PostManException {
		List<String> messages = new ArrayList<String>();
		List<MinMaxExRateDTO> rates = jaxService.setDefaults(customerId).getxRateClient().getMinMaxExchangeRate()
				.getResults();

		PushMessage pushMessage = new PushMessage();
		for (MinMaxExRateDTO minMaxExRateDTO : rates) {
			messages.add(String.format(
					"Get more %s for your %s at %s. %s-%s Special rate in the "
							+ "range of %.4f – %.4f for %s online and App users.",
					minMaxExRateDTO.getToCurrency().getCurrencyName(),
					minMaxExRateDTO.getFromCurrency().getCurrencyName(), webAppConfig.getAppTitle(),
					minMaxExRateDTO.getFromCurrency().getQuoteName(), minMaxExRateDTO.getToCurrency().getQuoteName(),
					minMaxExRateDTO.getMinExrate(), minMaxExRateDTO.getMaxExrate(), webAppConfig.getAppTitle()));
		}

		pushMessage.setSubject(String.format("Spceial rate @ %s", webAppConfig.getAppTitle()));

		pushMessage.setLines(messages);
		pushMessage.addToUser(customerId);
		fBPushService.sendDirect(pushMessage);
		return messages;
	}

}
