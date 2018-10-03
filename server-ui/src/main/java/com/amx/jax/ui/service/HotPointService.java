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
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.ui.WebAppConfig;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.amx.jax.ui.service.GeoHotPoints;

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

		ABDULLAH_AL_MUBARAK_CO_OPERATIVE("29.2439764","47.8926305"),
		AIRPORT_BRANCH_("29.2407624","47.9690786"),
		AL_DHAHER_CO_OPERATIVE_("29.16818"," 48.06258"),
		SHAMIYA_CO_OPERATIVE("29.355601","47.966485"),
		AVENUES_MALL_BRANCH("29.302358","47.933889"),
		AL_RAWDHA_CO_OPERATIVE_("29.328565","47.999201"),
		ZAHRA_CO_OPERATIVE_("29.2784541","48.0033514"),
		BAYAN_CO_OPERATIVE("29.3048426","48.0441923"),
		B_NEID_AL_GAR("29.3721568","48.004709"),
		DAIYA_CO_OPERATIVE_("29.3576334","48.0130213"),
		ESHBILIYA_CO_OPERATIVE_("29.273735","47.9363374"),
		FAIHA_CO_OPERATIVE_("29.3388931","47.9799896"),
		HATEEN_CO_OPERATIVE("29.2804592","48.0200451"),
		KAIFAN_CO_OPERATIVE("29.338821","47.9620374"),
		KHALDIYA_CO_OPERATIVE("29.3285952","47.9605936"),
		MANSOURIA_CO_OPERATIVE("29.3595447","47.9970901"),
		QASR_CO__OPERATIVE("29.3069263","47.7154283"),
		ALI_SABAH_SALEM_BRANCH("28.9625507","48.1594903"),
		SABAHIYA_BRANCH("29.1070459","48.1041769"),
		SABAH_AL_AHMED_AERA("28.8012537","48.0597201"),
		AVENUES_MALL_BRANCH2("29.3029167","47.9279524"),
		YARMOUK_CO_OPERATIVE("29.3092174","47.9685186"),
		RIQQA_CO_OPERATIVE("29.1430092","48.1008009"),
		RUMAITHIYA_CO_OPERATIVE("29.3171205","48.0786327"),
		AMGHARA_BRANCH("29.320955","47.751742"),
		AVENUE_MALL_BRANCH("29.304236","47.937305"),
		ARDIYA_BRANCH("29.2813344","47.8960289"),
		QURTUBA_CO_OPERATIVE_BRANCH("29.313026","47.984813"),
		AL_RAI_BRANCH("29.3150164","47.9589301"),
		DAJEEJ_BRANCH("29.2644626","47.9635623"),
		NEW_KHAITAN("29.2762099","47.9725432"),
		WAFRA_BRANCH("28.5631419","48.0628015"),
		SURRA_BRANCH("29.3138254","48.0022465"),
		HASSAWI_BRANCH("29.2662336","47.9239427"),

		AWFIS_CHEMTEX_LANE("19.119084", "72.913620"), POWAI_PLAZA("19.123392", "72.913109"), D_MART_POWAI("19.116531",
				"72.910423");

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
	PushNotifyClient pushNotifyClient;

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
	public List<String> notify(BigDecimal customerId, String token, GeoHotPoints hotpoint) throws PostManException {

		List<String> messages = new ArrayList<>();
		List<MinMaxExRateDTO> rates = jaxService.setDefaults(customerId).getxRateClient().getMinMaxExchangeRate()
				.getResults();
		List<BeneficiaryListDTO> benes = jaxService.setDefaults(customerId).getBeneClient()
				.getBeneficiaryList(new BigDecimal(0)).getResults();

		PushMessage pushMessage = new PushMessage();
		for (MinMaxExRateDTO minMaxExRateDTO : rates) {
			boolean toAdd = false;
			for (BeneficiaryListDTO beneficiaryListDTO : benes) {
				if (ArgUtil.areEqual(minMaxExRateDTO.getToCurrency().getCurrencyId(),
						beneficiaryListDTO.getCurrencyId())) {
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

		pushMessage.setSubject(String.format("Spceial rate @ %s", webAppConfig.getAppTitle()));
		pushMessage.setLines(messages);
		pushMessage.addToUser(customerId);

		if (webAppConfig.isNotifyGeoEnabled()) {
			auditService.log(event);
			pushNotifyClient.sendDirect(pushMessage);
		}
		return messages;
	}

}
