package com.amx.jax.payment.gateway;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.cache.TxCacheBox;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payment.gateway.PayGSession.PayGModels;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PayGSession extends TxCacheBox<PayGModels> {

	public static class PayGModels implements Serializable {

		private static final long serialVersionUID = -505348790426926238L;

		PayGParams params;

		PaymentGateWayResponse response;

		private UserDeviceClient client;

		String callback = null;

		public PaymentGateWayResponse getResponse() {
			return response;
		}

		public void setResponse(PaymentGateWayResponse response) {
			this.response = response;
		}

		public PayGParams getParams() {
			return params;
		}

		public void setParams(PayGParams params) {
			this.params = params;
		}

		public String getCallback() {
			return callback;
		}

		public void setCallback(String callback) {
			this.callback = callback;
		}

		public UserDeviceClient getClient() {
			return client;
		}

		public void setClient(UserDeviceClient client) {
			this.client = client;
		}
	}

	String uuid;

	String callback = null;

	public String getCallback() {
		if (ArgUtil.isEmpty(callback)) {
			callback = get().getCallback();
		}
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
		get().setCallback(callback);
	}

	public PayGModels getDefault() {
		return new PayGModels();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 
	 * @param generateIfNotExists
	 * @return
	 */
	public String uuid(boolean generateIfNotExists) {
		if (generateIfNotExists) {
			return this.uuid(AppContextUtil.getTraceId(true));
		} else {
			return this.uuid(Constants.BLANK);
		}
	}

	public String uuid(String newuuid) {
		if (!ArgUtil.isEmpty(newuuid)) {
			this.uuid = newuuid; // Set new UUID
		} else if (ArgUtil.isEmpty(this.uuid)) {
			this.uuid = AppContextUtil.getTraceId(true);
		}
		AppContextUtil.setTranxId(this.uuid);
		return this.uuid;
	}

}
