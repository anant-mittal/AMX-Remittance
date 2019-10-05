package com.amx.jax.radar.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.def.AbstarctQueryFactory;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQuery;

@Component
public class SnapQueryFactory extends AbstarctQueryFactory<SnapQueryTemplate, SnapQuery> {

	private static final long serialVersionUID = 4887835524923451254L;

	public SnapQueryFactory(@Autowired(required = false) List<QueryProcessor<?>> libs) {
		super(libs);
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface SnapQuery {
		SnapQueryTemplate[] value();
	}

	@Override
	public Class<SnapQuery> getAnnotionClass() {
		return SnapQuery.class;
	}

	@Override
	public SnapQueryTemplate[] getValues(SnapQuery annotation) {
		return annotation.value();
	}
}
