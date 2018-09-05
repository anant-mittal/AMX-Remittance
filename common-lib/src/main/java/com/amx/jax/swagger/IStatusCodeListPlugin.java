package com.amx.jax.swagger;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.amx.jax.exception.IExceptionEnum;
import com.amx.jax.logger.LoggerService;
import com.google.common.base.Optional;

import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

public abstract class IStatusCodeListPlugin<S extends IExceptionEnum, A extends Annotation>
		implements OperationBuilderPlugin {

	Logger logger = LoggerService.getLogger(getClass());

	@Override
	public boolean supports(DocumentationType documentationType) {
		return true;
	}

	public abstract Class<? extends Annotation> getAnnotionClass();

	public abstract S[] getValues(Optional<A> annotation);

	@Override
	public void apply(OperationContext context) {

		@SuppressWarnings("unchecked")
		Optional<A> annotation = (Optional<A>) context.findAnnotation(getAnnotionClass());
		if (annotation.isPresent() && getValues(annotation) != null) {
			S[] value = getValues(annotation);
			Set<ResponseMessage> respSt = new HashSet<ResponseMessage>();
			for (S amxError : value) {
				respSt.add(new ResponseMessageBuilder().code(amxError.getStatusCode()).message(amxError.getStatusKey())
						.build());
			}
			context.operationBuilder().responseMessages(respSt);
		}
	}

}
