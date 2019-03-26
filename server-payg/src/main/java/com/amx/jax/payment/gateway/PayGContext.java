package com.amx.jax.payment.gateway;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.common.ScopedBeanFactory;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.utils.ContextUtil;

@Service
public class PayGContext extends ScopedBeanFactory<PayGServiceCode, PayGClient> {

	private static final String PAYG_SERVICE_CODE = "PAYG_SERVICE_CODE";
	private static final long serialVersionUID = 4007091611441725719L;

	@Retention(RetentionPolicy.RUNTIME)
	@Lazy
	@Component
	public @interface PayGSpecific {
		PayGServiceCode[] value() default PayGServiceCode.KNET;
	}

	public PayGContext(List<PayGClient> libs) {
		super(libs);
	}

	@Override
	public PayGServiceCode[] getKeys(PayGClient lib) {
		PayGSpecific annotation = lib.getClass().getAnnotation(PayGSpecific.class);
		if (annotation != null) {
			return annotation.value();
		}
		return null;
	}

	@Override
	public PayGServiceCode getKey() {
		return (PayGServiceCode) ContextUtil.map().get(PAYG_SERVICE_CODE);
	}

	@Override
	public void setKey(PayGServiceCode serviceCode) {
		ContextUtil.map().put(PAYG_SERVICE_CODE, serviceCode);
	}
}
