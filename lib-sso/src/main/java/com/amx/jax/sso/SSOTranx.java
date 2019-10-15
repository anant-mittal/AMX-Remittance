package com.amx.jax.sso;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.TxCacheBox;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.dto.UserClientDto;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.sso.SSOTranx.SSOModel;

@Component
public class SSOTranx extends TxCacheBox<SSOModel> {

	public static class SSOModel implements Serializable {
		private static final long serialVersionUID = -2178734153442648084L;

		private String appUrl = null;
		private String returnUrl = SSOConstants.APP_LOGGEDIN_URL;

		private String appToken = null;
		private String motp = null;
		/**
		 * @deprecated - use direct values
		 */
		@Deprecated
		private UserClientDto userClient;
		private String branchAdapterId = null;

		private ClientType clientType;
		private BigDecimal terminalId;
		private Long createdStamp;

		private EmployeeDetailsDTO userDetails = null;

		public SSOModel() {
			this.userClient = new UserClientDto();
			this.createdStamp = System.currentTimeMillis();
		}

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

		@Deprecated
		public UserClientDto getUserClient() {
			return userClient;
		}

		@Deprecated
		public void setUserClient(UserClientDto userClient) {
			this.userClient = userClient;
		}

		public String getBranchAdapterId() {
			return branchAdapterId;
		}

		public void setBranchAdapterId(String branchAdapterId) {
			this.branchAdapterId = branchAdapterId;
		}

		public ClientType getClientType() {
			return clientType;
		}

		public void setClientType(ClientType clientType) {
			this.clientType = clientType;
		}

		public BigDecimal getTerminalId() {
			return this.terminalId;
		}

		public void setTerminalId(BigDecimal terminalId) {
			this.terminalId = terminalId;
		}

		public Long getCreatedStamp() {
			return createdStamp;
		}

		public void setCreatedStamp(Long createdStamp) {
			this.createdStamp = createdStamp;
		}

	}

	public SSOModel setReturnUrl(String returnUrl) {
		SSOModel msg = this.get();
		msg.setReturnUrl(returnUrl);
		this.put(msg);
		return msg;
	}

	public SSOModel setUserDetails(EmployeeDetailsDTO userDetail) {
		SSOModel msg = this.get();
		msg.setUserDetails(userDetail);
		this.put(msg);
		return msg;
	}

	@Deprecated
	public SSOModel setMOtp(String motp) {
		SSOModel msg = this.get();
		msg.setMotp(motp);
		this.put(msg);
		return msg;
	}

	@Override
	public SSOModel getDefault() {
		SSOModel sSOModel = new SSOModel();
		sSOModel.setUserClient(new UserClientDto());
		return sSOModel;
	}

}
