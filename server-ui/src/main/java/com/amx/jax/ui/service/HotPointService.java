package com.amx.jax.ui.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.amxlib.model.response.ApiResponse;
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

	// @Async
	public List<MinMaxExRateDTO> notify(BigDecimal customerId) {
		return jaxService.setDefaults(customerId).getxRateClient().getMinMaxExchangeRate().getResults();
	}

}
