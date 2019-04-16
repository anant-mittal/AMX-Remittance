package com.amx.jax;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.JaxAuthCache.JaxAuthMeta;
import com.amx.jax.cache.CacheBox;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
public class JaxAuthCache extends CacheBox<JaxAuthMeta> {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class JaxAuthMeta implements Serializable {

		private static final long serialVersionUID = 4710795819445446940L;

		public JaxAuthMeta(String mOtp, String eOtp, String secAns) {
			super();
			this.mOtp = mOtp;
			this.eOtp = eOtp;
			this.secAns = secAns;
		}

		public JaxAuthMeta(String mOtp, String eOtp) {
			super();
			this.mOtp = mOtp;
			this.eOtp = eOtp;
		}

		public JaxAuthMeta() {
			// TODO Auto-generated constructor stub
		}

		@ApiMockModelProperty(example = "123456")
		String otp;

		@ApiMockModelProperty(example = "123456")
		String mOtp;
		@ApiMockModelProperty(example = "234567")
		String eOtp;
		@ApiMockModelProperty(example = "123456")
		String wOtp;
		@ApiMockModelProperty(example = "Q1")
		BigDecimal questId;
		@ApiMockModelProperty(example = "black")
		String secAns;

		/**
		 * @return the questId
		 */
		public BigDecimal getQuestId() {
			return questId;
		}

		/**
		 * @param questId the questId to set
		 */
		public void setQuestId(BigDecimal questId) {
			this.questId = questId;
		}

		public String getSecAns() {
			return secAns;
		}

		public void setSecAns(String secAns) {
			this.secAns = secAns;
		}

		public String getmOtp() {
			return mOtp;
		}

		public void setmOtp(String mOtp) {
			this.mOtp = mOtp;
		}

		public String geteOtp() {
			return eOtp;
		}

		public void seteOtp(String eOtp) {
			this.eOtp = eOtp;
		}

		public String getOtp() {
			return otp;
		}

		public void setOtp(String otp) {
			this.otp = otp;
		}

		public String getwOtp() {
			return wOtp;
		}

		public void setwOtp(String wOtp) {
			this.wOtp = wOtp;
		}
	}

	public JaxAuthMeta getAuthMeta(BigDecimal customerId) {
		return this.get(ArgUtil.parseAsString(customerId));
	}
}
