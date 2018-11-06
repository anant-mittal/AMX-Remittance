package com.amx.jax.pricer.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.exception.PricerApiStatusBuilder.PricerApiStatus;
import com.amx.jax.swagger.IStatusCodeListPlugin;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 800)
public class PricerApiStatusBuilder extends IStatusCodeListPlugin<PricerServiceError, PricerApiStatus> {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface PricerApiStatus {
		PricerServiceError[] value() default { PricerServiceError.UNKNOWN_EXCEPTION };
	}

	@Override
	public Class<PricerApiStatus> getAnnotionClass() {
		return PricerApiStatus.class;
	}

	@Override
	public PricerServiceError[] getValues(PricerApiStatus annotation) {
		return annotation.value();
	}

}
