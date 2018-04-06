package com.amx.jax.amxlib.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RoutingBankMasterParam {

	@JsonDeserialize(as = RoutingBankMasterServiceImpl.class)
	public interface RoutingBankMasterServiceProviderParam {

		BigDecimal getRoutingCountryId();

		void setServiceGroupId(BigDecimal serviceGroupId);

		BigDecimal getServiceGroupId();

		void setRoutingCountryId(BigDecimal routingCountryId);

	}

	@JsonDeserialize(as = RoutingBankMasterServiceImpl.class)
	public interface RoutingBankMasterAgentParam extends RoutingBankMasterServiceProviderParam {

		BigDecimal getRoutingBankId();

		void setCurrencyId(BigDecimal currencyId);

		BigDecimal getCurrencyId();

		void setRoutingBankId(BigDecimal routingBankId);

	}

	@JsonDeserialize(as = RoutingBankMasterServiceImpl.class)
	public interface RoutingBankMasterAgentBranchParam extends RoutingBankMasterAgentParam {

		BigDecimal getAgentBankId();

		void setAgentBankId(BigDecimal agentBankId);

	}

	public static class RoutingBankMasterServiceImpl implements RoutingBankMasterAgentBranchParam {

		public RoutingBankMasterServiceImpl() {

		}

		public RoutingBankMasterServiceImpl(BigDecimal applicationCountryId, BigDecimal routingCountryId,
				BigDecimal serviceGroupId) {
			this.applicationCountryId = applicationCountryId;
			this.routingCountryId = routingCountryId;
			this.serviceGroupId = serviceGroupId;
		}

		public RoutingBankMasterServiceImpl(BigDecimal applicationCountryId, BigDecimal routingCountryId,
				BigDecimal serviceGroupId, BigDecimal routingBankId, BigDecimal currencyId) {
			this.applicationCountryId = applicationCountryId;
			this.routingCountryId = routingCountryId;
			this.serviceGroupId = serviceGroupId;
			this.routingBankId = routingBankId;
			this.currencyId = currencyId;
		}

		public RoutingBankMasterServiceImpl(BigDecimal applicationCountryId, BigDecimal routingCountryId,
				BigDecimal serviceGroupId, BigDecimal routingBankId, BigDecimal currencyId, BigDecimal agentBankId) {
			this.applicationCountryId = applicationCountryId;
			this.routingCountryId = routingCountryId;
			this.serviceGroupId = serviceGroupId;
			this.routingBankId = routingBankId;
			this.currencyId = currencyId;
			this.agentBankId = agentBankId;
		}

		private BigDecimal applicationCountryId;
		private BigDecimal routingCountryId;
		private BigDecimal serviceGroupId;
		private BigDecimal routingBankId;
		private String routingBankCode;
		private String routingBankName;
		private BigDecimal currencyId;
		private BigDecimal agentBankId;

		public BigDecimal getApplicationCountryId() {
			return applicationCountryId;
		}

		public void setApplicationCountryId(BigDecimal applicationCountryId) {
			this.applicationCountryId = applicationCountryId;
		}

		@Override
		public BigDecimal getRoutingCountryId() {
			return routingCountryId;
		}

		@Override
		public void setRoutingCountryId(BigDecimal routingCountryId) {
			this.routingCountryId = routingCountryId;
		}

		@Override
		public BigDecimal getServiceGroupId() {
			return serviceGroupId;
		}

		@Override
		public void setServiceGroupId(BigDecimal serviceGroupId) {
			this.serviceGroupId = serviceGroupId;
		}

		@Override
		public BigDecimal getRoutingBankId() {
			return routingBankId;
		}

		@Override
		public void setRoutingBankId(BigDecimal routingBankId) {
			this.routingBankId = routingBankId;
		}

		public String getRoutingBankCode() {
			return routingBankCode;
		}

		public void setRoutingBankCode(String routingBankCode) {
			this.routingBankCode = routingBankCode;
		}

		public String getRoutingBankName() {
			return routingBankName;
		}

		public void setRoutingBankName(String routingBankName) {
			this.routingBankName = routingBankName;
		}

		@Override
		public BigDecimal getCurrencyId() {
			return currencyId;
		}

		@Override
		public void setCurrencyId(BigDecimal currencyId) {
			this.currencyId = currencyId;
		}

		@Override
		public BigDecimal getAgentBankId() {
			return agentBankId;
		}

		@Override
		public void setAgentBankId(BigDecimal agentBankId) {
			this.agentBankId = agentBankId;
		}

		@Override
		public String toString() {
			return "RoutingBankMasterParam [applicationCountryId=" + applicationCountryId + ", routingCountryId="
					+ routingCountryId + ", serviceGroupId=" + serviceGroupId + ", routingBankId=" + routingBankId
					+ ", routingBankCode=" + routingBankCode + ", routingBankName=" + routingBankName + ", currencyId="
					+ currencyId + ", agentBankId=" + agentBankId + "]";
		}
	}

}
