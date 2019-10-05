package com.amx.jax.def;

import java.lang.annotation.Annotation;
import java.util.List;

import com.amx.common.ScopedBeanFactory;
import com.amx.jax.def.AbstarctQueryFactory.IQueryTemplate;
import com.amx.jax.def.AbstarctQueryFactory.QueryProcessor;

public abstract class AbstarctQueryFactory<QT extends IQueryTemplate, QA extends Annotation>
		extends ScopedBeanFactory<QT, QueryProcessor<?>> {

	private static final long serialVersionUID = -2475686410285813714L;

	public static interface IQueryTemplate {
	}

	public static interface QueryProcessor<B> {
		public List<B> process();
	}

	public AbstarctQueryFactory(List<QueryProcessor<?>> libs) {
		super(libs);
	}

	public abstract Class<QA> getAnnotionClass();

	public abstract QT[] getValues(QA annotation);

	@Override
	public QT[] getKeys(QueryProcessor<?> lib) {
		QA annotation = lib.getClass().getAnnotation(getAnnotionClass());
		return getValues(annotation);
	}

}
