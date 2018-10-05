package com.amx.jax.sso;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.sso.SSOTranx.SSOModel;

@Component
public class SSOTranx extends TransactionModel<SSOModel> {

	public static class SSOModel implements Serializable {
		private static final long serialVersionUID = -2178734153442648084L;

		private String appUrl = null;
		private String returnUrl = SSOConstants.APP_LOGGEDIN_URL;
		private String sotp = null;
		private String motp = null;

		public String getReturnUrl() {
			return returnUrl;
		}

		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}

		public String getAppUrl() {
			return appUrl;
		}

		public void setAppUrl(String appUrl) {
			this.appUrl = appUrl;
		}

		public String getSotp() {
			return sotp;
		}

		public void setSotp(String sotp) {
			this.sotp = sotp;
		}

		public String getMotp() {
			return motp;
		}

		public void setMotp(String motp) {
			this.motp = motp;
		}

	}

	@Override
	public SSOModel init() {
		return this.save(new SSOModel());
	}

	@Override
	public SSOModel getDefault() {
		return new SSOModel();
	}

	public SSOModel setReturnUrl(String returnUrl) {
		SSOModel msg = this.get();
		msg.setReturnUrl(returnUrl);
		return msg;
	}

	public SSOModel setAppUrl(String landingUrl, String sotp) {
		SSOModel msg = this.get();
		msg.setAppUrl(landingUrl);
		msg.setSotp(sotp);
		this.save(msg);
		return msg;
	}

	public SSOModel setMOtp(String motp) {
		SSOModel msg = this.get();
		msg.setMotp(motp);
		this.save(msg);
		return msg;
	}

	@Override
	public SSOModel commit() {
		return this.get();
	}

}
