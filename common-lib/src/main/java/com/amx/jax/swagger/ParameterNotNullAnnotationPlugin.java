package com.amx.jax.swagger;

import java.lang.annotation.Annotation;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

//@Component
//@Order(Ordered.LOWEST_PRECEDENCE)
public class ParameterNotNullAnnotationPlugin implements ParameterBuilderPlugin {

	private static final Logger LOG = LoggerFactory
			.getLogger(ParameterNotNullAnnotationPlugin.class);

	@Override
	public boolean supports(DocumentationType delimiter) {
		// we simply support all documentationTypes!
		return true;
	}

	@Override
	public void apply(ParameterContext context) {

		Optional<RequestParam> requestParam = extractRequestParamAnnotation(context);

		if (requestParam.isPresent()) {
			RequestParam requestParamValue = requestParam.get();

			if (requestParamValue.required() == true) {
				LOG.info("@RequestParam.required=true: setting parameter as required");
				context.parameterBuilder().required(true);
			}

		} else {
			Optional<PathVariable> pathVariableParam = extractPathVariableAnnotation(context);
			if (pathVariableParam.isPresent()) {
				LOG.info("@PathVariable present: setting parameter as required");
				context.parameterBuilder().required(true);

			} else {
				Optional<NotNull> notNull = extractAnnotation(context);

				if (notNull.isPresent()) {
					LOG.info("@NotNull present: setting parameter as required");
					context.parameterBuilder().required(true);
				}
			}

		}

	}

	@VisibleForTesting
	Optional<NotNull> extractAnnotation(ParameterContext context) {
		return validatorFromField(context, NotNull.class);
	}

	@VisibleForTesting
	Optional<RequestParam> extractRequestParamAnnotation(ParameterContext context) {
		return validatorFromField(context, RequestParam.class);
	}

	@VisibleForTesting
	Optional<PathVariable> extractPathVariableAnnotation(ParameterContext context) {
		return validatorFromField(context, PathVariable.class);
	}

	public static <T extends Annotation> Optional<T> validatorFromField(
			ParameterContext context, Class<T> annotationType) {

		MethodParameter methodParam = context.methodParameter();

		T annotatedElement = methodParam.getParameterAnnotation(annotationType);
		Optional<T> annotationValue = Optional.absent();
		if (annotatedElement != null) {
			annotationValue = Optional.fromNullable(annotatedElement);
		}
		return annotationValue;
	}

}