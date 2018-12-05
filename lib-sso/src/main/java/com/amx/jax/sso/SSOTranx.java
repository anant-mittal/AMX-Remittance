package com.amx.jax.sso;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.rbaac.dto.UserClientDto;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.sso.SSOTranx.SSOModel;

@Component
public class SSOTranx extends TransactionModel<SSOModel> {

	public static class SSOModel implements Serializable {
		private static final long serialVersionUID = -2178734153442648084L;

		private String appUrl = SSOConstants.APP_LOGIN_URL_DONE;
		private String returnUrl = SSOConstants.APP_LOGGEDIN_URL;
		private String appToken = null;
		private String motp = null;
		private UserClientDto userClient;
		private String branchAdapterId = null;

		private EmployeeDetailsDTO userDetails = null;

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

		public String getAppToken() {
			return appToken;
		}

		public void setAppToken(String appToken) {
			this.appToken = appToken;
		}

		public String getMotp() {
			return motp;
		}

		public void setMotp(String motp) {
			this.motp = motp;
		}

		public EmployeeDetailsDTO getUserDetails() {
			return userDetails;
		}

		public void setUserDetails(EmployeeDetailsDTO userDetails) {
			this.userDetails = userDetails;
		}

		public UserClientDto getUserClient() {
			return userClient;
		}

		public void setUserClient(UserClientDto userClient) {
			this.userClient = userClient;
		}

		public String getBranchAdapterId() {
			return branchAdapterId;
		}

		public void setBranchAdapterId(String branchAdapterId) {
			this.branchAdapterId = branchAdapterId;
		}

	}

	@Override
	public SSOModel init() {
		return this.save(getDefault());
	}

	@Override
	public SSOModel getDefault() {
		SSOModel sSOModel = new SSOModel();
		sSOModel.setUserClient(new UserClientDto());
		return sSOModel;
	}

	public SSOModel setReturnUrl(String returnUrl) {
		SSOModel msg = this.get();
		msg.setReturnUrl(returnUrl);
		this.save(msg);
		return msg;
	}

	public SSOModel setUserDetails(EmployeeDetailsDTO userDetail) {
		SSOModel msg = this.get();
		msg.setUserDetails(userDetail);
		this.save(msg);
		return msg;
	}

	@Deprecated
	public SSOModel setMOtp(String motp) {
		SSOModel msg = this.get();
		msg.setMotp(motp);
		this.save(msg);
		return msg;
	}

}
