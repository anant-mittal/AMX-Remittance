package com.amx.jax;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.JaxAuthCache.JaxAuthMeta;
import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

@Component
public class JaxAuthCache extends CacheBox<JaxAuthMeta> {
	public static class JaxAuthMeta {
		String mOtp;
		String eOtp;
		String secAns;

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
	}

	public JaxAuthMeta getAuthMeta(BigDecimal customerId) {
		return this.get(ArgUtil.parseAsString(customerId));
	}
}
