package com.amx.jax.sso;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.sso.SSOTranx.SSOModel;

@Component
public class SSOTranx extends TransactionModel<SSOModel> {

	public static class SSOModel implements Serializable {
		private static final long serialVersionUID = -2178734153442648084L;

		private String landingUrl = null;
		private String returnUrl = null;
		private String sotp = null;

		public String getReturnUrl() {
			return returnUrl;
		}

		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}

		public String getLandingUrl() {
			return landingUrl;
		}

		public void setLandingUrl(String landingUrl) {
			this.landingUrl = landingUrl;
		}

		public String getSotp() {
			return sotp;
		}

		public void setSotp(String sotp) {
			this.sotp = sotp;
		}

	}

	@Override
	public SSOModel init() {
		return this.save(new SSOModel());
	}

	public SSOModel setReturnUrl(String returnUrl) {
		SSOModel msg = this.save(new SSOModel());
		msg.setReturnUrl(returnUrl);
		return msg;
	}

	public SSOModel setLandingUrl(String landingUrl, String sotp) {
		SSOModel msg = this.get();
		msg.setLandingUrl(landingUrl);
		msg.setSotp(sotp);
		this.save(msg);
		return msg;
	}

	@Override
	public SSOModel commit() {
		return this.get();
	}

}
