package com.amx.jax.scope;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.amx.jax.AppConstants;

public class TenantBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
		factory.registerScope("tenant", new TenantScope());
		factory.registerScope("thread", new ThreadScope());
		factory.registerScope(AppConstants.Scopes.VENDOR, new VendorScope());
	}
}