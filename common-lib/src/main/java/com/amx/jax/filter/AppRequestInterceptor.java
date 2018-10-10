package com.amx.jax.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.jax.rest.RestMetaRequestInFilter;
import com.amx.jax.rest.RestService;

@Component
public class AppRequestInterceptor extends HandlerInterceptorAdapter {

	@Autowired(required = false)
	RestMetaRequestInFilter<?> restMetaFilter;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		RestService.importMetaFromStatic(restMetaFilter, request);
		return super.preHandle(request, response, handler);
	}

}
