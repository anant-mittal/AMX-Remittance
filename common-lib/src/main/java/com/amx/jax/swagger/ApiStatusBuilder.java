package com.amx.jax.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.swagger.ApiStatusBuilder.ApiStatus;
import com.google.common.base.Optional;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class ApiStatusBuilder extends IStatusCodeListPlugin<ApiStatusCodes, ApiStatus> {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ApiStatus {
		ApiStatusCodes[] value() default { ApiStatusCodes.SUCCESS };

		Class<? extends IExceptionEnum> enumClass() default ApiStatusCodes.class;
	}

	@Override
	public Class<ApiStatus> getAnnotionClass() {
		return ApiStatus.class;
	}

	@Override
	public ApiStatusCodes[] getValues(ApiStatus annotation) {
		return annotation.value();
	}

	@Override
	public void apply(OperationContext context) {
		super.apply(context);

		List<Parameter> paramsList = new ArrayList<Parameter>();
		Optional<ApiMockParams> classAnnotation = (Optional<ApiMockParams>) context
				.findControllerAnnotation(ApiMockParams.class);

		if (classAnnotation.isPresent()) {
			ApiMockParam[] params = classAnnotation.get().value();

			for (ApiMockParam apiImplicitParam : params) {
				Parameter parameter = new ParameterBuilder().name(apiImplicitParam.name())
						.modelRef(new ModelRef(DefaultSwaggerConfig.PARAM_STRING)).description(apiImplicitParam.value())
						.defaultValue(apiImplicitParam.defaultValue())
						.parameterType(apiImplicitParam.paramType().toString().toLowerCase())
						.required(apiImplicitParam.required()).build();
				paramsList.add(parameter);
			}

		}

		Optional<ApiMockParams> annotation = (Optional<ApiMockParams>) context.findAnnotation(ApiMockParams.class);
		if (annotation.isPresent()) {
			ApiMockParam[] params = annotation.get().value();
			for (ApiMockParam apiImplicitParam : params) {
				Parameter parameter = new ParameterBuilder().name(apiImplicitParam.name())
						.modelRef(new ModelRef(DefaultSwaggerConfig.PARAM_STRING)).description(apiImplicitParam.value())
						.defaultValue(apiImplicitParam.defaultValue())
						.parameterType(apiImplicitParam.paramType().toString().toLowerCase())
						.required(apiImplicitParam.required()).build();
				paramsList.add(parameter);
			}
		}

		context.operationBuilder().parameters(paramsList);
	}
}
