package com.amx.jax.proto.tpc.api;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CtxCacheBox;
import com.amx.jax.proto.tpc.api.TPCSession.TPCSessionModel;

@Component
public class TPCSession extends CtxCacheBox<TPCSessionModel> {

	public static class TPCSessionModel implements Serializable {
		private static final long serialVersionUID = 4246127002367512855L;
		String sessionToken;
		String sessionSalt;
		BigDecimal branchId;

		public String getSessionToken() {
			return sessionToken;
		}

		public void setSessionToken(String sessionToken) {
			this.sessionToken = sessionToken;
		}

		public String getSessionSalt() {
			return sessionSalt;
		}

		public void setSessionSalt(String sessionSalt) {
			this.sessionSalt = sessionSalt;
		}

		public BigDecimal getBranchId() {
			return branchId;
		}

		public void setBranchId(BigDecimal branchId) {
			this.branchId = branchId;
		}

	}

	public TPCSessionModel getDefault() {
		return new TPCSessionModel();
	}

}
