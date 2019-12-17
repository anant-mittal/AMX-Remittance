package com.amx.jax.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.swagger.IStatusCodeListPlugin;

import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class ApiJaxStatusBuilder extends IStatusCodeListPlugin<JaxError, ApiJaxStatus> {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ApiJaxStatus {
		JaxError[] value() default { JaxError.USER_NOT_FOUND };
	}

	@Override
	public Class<ApiJaxStatus> getAnnotionClass() {
		return ApiJaxStatus.class;
	}

	@Override
	public JaxError[] getValues(ApiJaxStatus annotation) {
		return annotation.value();
	}

}
