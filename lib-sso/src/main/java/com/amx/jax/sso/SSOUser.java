package com.amx.jax.sso;

import java.io.Serializable;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.rbaac.dto.UserClientDto;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SSOUser implements Serializable {

	private static final long serialVersionUID = 5846957265338654300L;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	AppConfig appConfig;

	@Autowired
	SSOConfig ssoConfig;

	private String terminalId;

	private String terminalIp;

	private boolean authDone = false;
	private String tranxId;
	private Long loginTime;
	private EmployeeDetailsDTO userDetails = null;

	private UserClientDto userClient;

	UUID outlookState;
	UUID outlookNonce;
	String outlookAuthCode;
	String outlookIdToken;

	public String getUserId() {
		if (userDetails == null) {
			return null;
		}
		return ArgUtil.parseAsString(userDetails.getEmployeeId());
	}

	public UserClientDto getUserClient() {
		return userClient;
	}

	public void setUserClient(UserClientDto userClient) {
		this.userClient = userClient;
	}

	private String selfSAC;
	private String partnerSAC;

	public boolean isAuthDone() {
		return authDone;
	}

	public void setAuthDone(boolean authDone) {
		if (authDone) {
			this.loginTime = System.currentTimeMillis();
			Cookie kooky = new Cookie("AMXSESSION", "ok");
			kooky.setMaxAge(15);
			kooky.setHttpOnly(false);
			kooky.setSecure(false);
			kooky.setPath("/");
			commonHttpRequest.setCookie(kooky);
		} else {
			this.loginTime = 0L;
		}
		this.authDone = authDone;
	}

	public boolean isAMXCookieValid() {
		if (ssoConfig.isBrowserSessionPersist()) {
			return true;
		}
		Cookie kooky = commonHttpRequest.getCookie("AMXSESSION");
		if (kooky == null) {
			return false;
		}
		return true;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}

	public String ssoTranxId() {
		String tranxId = AppContextUtil.getTranxId();
		if (ArgUtil.isEmpty(tranxId) && !ArgUtil.isEmpty(this.getTranxId())) {
			tranxId = this.getTranxId();
			AppContextUtil.setTranxId(tranxId);
		} else {
			tranxId = AppContextUtil.getTranxId(true);
			this.setTranxId(tranxId);
		}
		return tranxId;
	}

	public void setUserDetails(EmployeeDetailsDTO userDetails) {
		this.userDetails = userDetails;
	}

	public EmployeeDetailsDTO getUserDetails() {
		return userDetails;
	}

	public String getSelfSAC() {
		return selfSAC;
	}

	public void setSelfSAC(String selfSAC) {
		this.selfSAC = selfSAC;
	}

	public String getPartnerSAC() {
		return partnerSAC;
	}

	public void setPartnerSAC(String partnerSAC) {
		this.partnerSAC = partnerSAC;
	}

	public void generateSAC() {
		this.selfSAC = Random.randomAlphaNumeric(6);
		this.partnerSAC = Random.randomAlphaNumeric(6);
	}

	public Long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Long loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * Logged In terminal
	 * 
	 * @return
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * Logged In terminal
	 * 
	 * @param terminalId
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalIp() {
		return terminalIp;
	}

	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp + ",0:0:0:0:0:0:0:1";
	}

	public void setTerminalIp(String terminalIp, String terminalIp2) {
		this.terminalIp = terminalIp + ",0:0:0:0:0:0:0:1," + terminalIp2;
	}

	public void setTerminalIp(String terminalIp, String terminalIp2, String terminalIp3) {
		this.terminalIp = terminalIp + "," + terminalIp2 + ",0:0:0:0:0:0:0:1," + terminalIp3;
	}

	public UUID getOutlookState() {
		return outlookState;
	}

	public void setOutlookState(UUID outlookState) {
		this.outlookState = outlookState;
	}

	public UUID getOutlookNonce() {
		return outlookNonce;
	}

	public void setOutlookNonce(UUID outlookNonce) {
		this.outlookNonce = outlookNonce;
	}

	public String getOutlookAuthCode() {
		return outlookAuthCode;
	}

	public void setOutlookAuthCode(String outlookAuthCode) {
		this.outlookAuthCode = outlookAuthCode;
	}

	public String getOutlookIdToken() {
		return outlookIdToken;
	}

	public void setOutlookIdToken(String outlookIdToken) {
		this.outlookIdToken = outlookIdToken;
	}

}
