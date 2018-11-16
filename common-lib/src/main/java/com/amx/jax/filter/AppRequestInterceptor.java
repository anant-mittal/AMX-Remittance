package com.amx.jax.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.amx.jax.rest.RestService;

@Component
public class AppRequestInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	RestService restService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		restService.importMetaFromStatic(request);
		return super.preHandle(request, response, handler);
	}

}
