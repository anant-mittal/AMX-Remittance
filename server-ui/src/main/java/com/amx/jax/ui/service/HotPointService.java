package com.amx.jax.ui.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.postman.FBPushService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.ui.WebAppConfig;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class HotPointService.
 */
@Component
public class HotPointService {

	/**
	 * The Enum HotPoints.
	 */
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	@JsonPropertyOrder({ "latitude", "longitude" })
	public enum HotPoints {

		SALMIYA2(" 29.331993", "48.061422"), MURGAB3("29.369429", "47.978551"), SALMIYA4(" 29.325602", "48.058039");

		/** The latitude. */
		private String latitude;

		/** The longitude. */
		private String longitude;

		/** The id. */
		private String id;

		/**
		 * Instantiates a new hot points.
		 *
		 * @param latitude
		 *            the latitude
		 * @param longitude
		 *            the longitude
		 */
		HotPoints(String latitude, String longitude) {
			this.id = this.name();
			this.latitude = latitude;
			this.longitude = longitude;
		}

		/**
		 * Gets the latitude.
		 *
		 * @return the latitude
		 */
		public String getLatitude() {
			return latitude;
		}

		/**
		 * Gets the longitude.
		 *
		 * @return the longitude
		 */
		public String getLongitude() {
			return longitude;
		}

		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public String getId() {
			return id;
		}

	}

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/** The b push service. */
	@Autowired
	FBPushService fBPushService;

	@Autowired
	AuditService auditService;

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	/**
	 * Notify.
	 *
	 * @param customerId
	 *            the customer id
	 * @param hotpoint
	 * @param token
	 * @return the list
	 * @throws PostManException
	 *             the post man exception
	 */
	// @Async
	public List<String> notify(BigDecimal customerId, String token, HotPoints hotpoint) throws PostManException {

		List<String> messages = new ArrayList<>();
		List<MinMaxExRateDTO> rates = jaxService.setDefaults(customerId).getxRateClient().getMinMaxExchangeRate()
				.getResults();
		List<BeneficiaryListDTO> benes = jaxService.setDefaults(customerId).getBeneClient()
				.getBeneficiaryList(new BigDecimal(0)).getResults();

		PushMessage pushMessage = new PushMessage();
		for (MinMaxExRateDTO minMaxExRateDTO : rates) {
			boolean toAdd = false;
			for (BeneficiaryListDTO beneficiaryListDTO : benes) {
				if (minMaxExRateDTO.getToCurrency().getCurrencyId()
						.compareTo(beneficiaryListDTO.getCurrencyId()) == 0) {
					toAdd = true;
					continue;
				}
			}
			if (toAdd) {
				messages.add(String.format(
						"Get more %s for your %s at %s. %s-%s Special rate in the "
								+ "range of %.4f – %.4f for %s online and App users.",
						minMaxExRateDTO.getToCurrency().getCurrencyName(),
						minMaxExRateDTO.getFromCurrency().getCurrencyName(), webAppConfig.getAppTitle(),
						minMaxExRateDTO.getFromCurrency().getQuoteName(),
						minMaxExRateDTO.getToCurrency().getQuoteName(), minMaxExRateDTO.getMinExrate(),
						minMaxExRateDTO.getMaxExrate(), webAppConfig.getAppTitle()));
			}
		}
		CActivityEvent event = new CActivityEvent(CActivityEvent.Type.GEO_LOCATION);
		event.setCustomer(ArgUtil.parseAsString(customerId));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("hotpoint", hotpoint);
		data.put("messages", messages);
		event.setData(data);
		//auditService.log(event);

		pushMessage.setSubject(String.format("Spceial rate @ %s", webAppConfig.getAppTitle()));
		pushMessage.setLines(messages);
		pushMessage.addToUser(customerId);
		//fBPushService.sendDirect(pushMessage);
		return messages;
	}

}
