package com.amx.jax.rates.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

public class AmanKuwaitModels {

	@JacksonXmlRootElement(localName = "Rates")
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Rates implements Serializable {

		private static final long serialVersionUID = 3954809608842883968L;

		@JacksonXmlProperty(localName = "CurRates")
		private List<CurRates> curRates;

		@JacksonXmlProperty(localName = "UpdtDate")
		private UpdtDate updtDate;

		public List<CurRates> getCurRates() {
			return curRates;
		}

		public void setCurRates(List<CurRates> curRates) {
			this.curRates = curRates;
		}

		public UpdtDate getUpdtDate() {
			return updtDate;
		}

		public void setUpdtDate(UpdtDate updtDate) {
			this.updtDate = updtDate;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CurRates implements Serializable {

		private static final long serialVersionUID = 8632681886190975280L;

		@JacksonXmlProperty(localName = "SortOrder")
		private String sortOrder;
		@JacksonXmlProperty(localName = "Name")
		private String name;
		@JacksonXmlProperty(localName = "Code")
		private String code;
		@JacksonXmlProperty(localName = "BRate")
		private String buyrate;
		@JacksonXmlProperty(localName = "SRate")
		private String sellrate;
		@JacksonXmlProperty(localName = "KDRate")
		private String kdrate;

		public String getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(String sortOrder) {
			this.sortOrder = sortOrder;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getBuyrate() {
			return buyrate;
		}

		public void setBuyrate(String buyrate) {
			this.buyrate = buyrate;
		}

		public String getSellrate() {
			return sellrate;
		}

		public void setSellrate(String sellrate) {
			this.sellrate = sellrate;
		}

		public String getKdrate() {
			return kdrate;
		}

		public void setKdrate(String kdrate) {
			this.kdrate = kdrate;
		}

	}

	@JsonIgnoreProperties
	public static class UpdtDate implements Serializable {
		private static final long serialVersionUID = -699453964265130543L;
		@JacksonXmlProperty(localName = "RateDate")
		private String ratedate;
	}
}
