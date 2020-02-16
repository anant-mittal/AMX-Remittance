package com.amx.jax.cache.box;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.jax.cache.box.CustomerOnCall.CustomerCall;

/**
 * This cache box has all the custoemrs connected to branch user on-call
 * 
 * @author lalittanwar
 *
 */
@Component
public class CustomerOnCall extends CacheBox<CustomerCall> {

	public static class CustomerCall implements Serializable {

		private static final long serialVersionUID = -8788305096383678577L;
		private BigDecimal customerid;
		private BigDecimal leadId;
		private String sessionId;

		public BigDecimal getCustomerid() {
			return customerid;
		}

		public void setCustomerid(BigDecimal customerid) {
			this.customerid = customerid;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public BigDecimal getLeadId() {
			return leadId;
		}

		public void setLeadId(BigDecimal leadId) {
			this.leadId = leadId;
		}

	}

	public CustomerOnCall() {
		super("CustomerOnCall");
	}

}
