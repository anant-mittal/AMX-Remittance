package com.amx.jax.swagger;

import static com.amx.jax.swagger.ApiMockModelProperties.findApiModePropertyAnnotation;
import static com.amx.jax.swagger.ApiMockModelProperties.toAllowableValues;
import static com.amx.jax.swagger.ApiMockModelProperties.toDescription;
import static com.amx.jax.swagger.ApiMockModelProperties.toExample;
import static com.amx.jax.swagger.ApiMockModelProperties.toHidden;
import static com.amx.jax.swagger.ApiMockModelProperties.toIsReadOnly;
import static com.amx.jax.swagger.ApiMockModelProperties.toIsRequired;
import static com.amx.jax.swagger.ApiMockModelProperties.toPosition;
import static com.amx.jax.swagger.ApiMockModelProperties.toType;
import static springfox.documentation.schema.Annotations.findPropertyAnnotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ParameterNotNullAnnotationPlugin implements ModelPropertyBuilderPlugin {
	@Override
	public void apply(ModelPropertyContext context) {
		Optional<ApiMockModelProperty> annotation = Optional.absent();

		if (context.getAnnotatedElement().isPresent()) {
			annotation = annotation.or(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
		}
		if (context.getBeanPropertyDefinition().isPresent()) {
			annotation = annotation.or(findPropertyAnnotation(
					context.getBeanPropertyDefinition().get(), ApiMockModelProperty.class));
		}
		if (annotation.isPresent()) {
			context.getBuilder()
					.allowableValues(annotation.transform(toAllowableValues()).orNull())
					.required(annotation.transform(toIsRequired()).or(true))
					// .required(true)
					.readOnly(annotation.transform(toIsReadOnly()).or(false))
					.description(annotation.transform(toDescription()).orNull())
					.isHidden(annotation.transform(toHidden()).or(false))
					.type(annotation.transform(toType(context.getResolver())).orNull())
					.position(annotation.transform(toPosition()).or(0))
					.example(annotation.transform(toExample()).orNull());
		} else {
			context.getBuilder()
					// .allowableValues(annotation.transform(toAllowableValues()).orNull())
					.required(annotation.transform(toIsRequired()).or(true))
					.isHidden(annotation.transform(toHidden()).or(false))
			// .required(true)
			// .readOnly(annotation.transform(toIsReadOnly()).or(false))
			// .description(annotation.transform(toDescription()).orNull())
			// .isHidden(annotation.transform(toHidden()).or(false))
			// .type(annotation.transform(toType(context.getResolver())).orNull())
			// .position(annotation.transform(toPosition()).or(0))
			// .example(annotation.transform(toExample()).orNull())
			;
		}
	}

	@Override
	public boolean supports(DocumentationType delimiter) {
		return SwaggerPluginSupport.pluginDoesApply(delimiter);
	}
}
