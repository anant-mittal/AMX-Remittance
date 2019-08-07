package com.amx.jax.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.repository.IExEmailNotificationDao;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class JaxEmailNotificationService {

	@Autowired
	IExEmailNotificationDao iExEmailNotificationDao;

	public String[] getBeneCreationErrorEmailList() {
		List<ExEmailNotification> beneCreationErrorList = iExEmailNotificationDao
				.getBeneCreationErrorEmailNotification();
		if (CollectionUtils.isNotEmpty(beneCreationErrorList)) {
			Set<String> beneCreationErrorSet = beneCreationErrorList.stream().map(i -> i.getEmailId())
					.collect(Collectors.toSet());
			return beneCreationErrorSet.toArray(new String[beneCreationErrorSet.size()]);
		}
		return null;
	}
}
