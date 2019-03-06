package com.amx.jax.radar.jobs.scrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.dict.Currency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

public class AmanKuwaitModels {

	@JacksonXmlRootElement(localName = "Rates")
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Rates implements Serializable {

		private static final long serialVersionUID = 3954809608842883968L;

		@JacksonXmlProperty(localName = "CurRates")
		@JacksonXmlElementWrapper(useWrapping = false)
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
		private Currency code;
		@JacksonXmlProperty(localName = "BRate")
		private BigDecimal buyrate;
		@JacksonXmlProperty(localName = "SRate")
		private BigDecimal sellrate;
		@JacksonXmlProperty(localName = "KDRate")
		private BigDecimal kdrate;

		public CurRates() {
//			this.sortOrder = "1";
//			this.name = "NAME";
//			this.code = "CODE";
//			this.buyrate = "BUYRATE";
//			this.sellrate = "SELLRATE";
//			this.kdrate = "KDRATE";
		}

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

		public Currency getCode() {
			return code;
		}

		public void setCode(Currency code) {
			this.code = code;
		}

		public BigDecimal getBuyrate() {
			return buyrate;
		}

		public void setBuyrate(BigDecimal buyrate) {
			this.buyrate = buyrate;
		}

		public BigDecimal getSellrate() {
			return sellrate;
		}

		public void setSellrate(BigDecimal sellrate) {
			this.sellrate = sellrate;
		}

		public BigDecimal getKdrate() {
			return kdrate;
		}

		public void setKdrate(BigDecimal kdrate) {
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
