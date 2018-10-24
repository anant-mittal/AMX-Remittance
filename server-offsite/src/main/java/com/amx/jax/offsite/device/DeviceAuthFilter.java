package com.amx.jax.offsite.device;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceBox;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceData;
import com.amx.utils.ArgUtil;

/**
 * The Class WebAuthFilter.
 */
@Component
public class DeviceAuthFilter implements Filter {

	/** The session service. */
	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	DeviceBox deviceBox;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		String deviceKey = commonHttpRequest.getDeviceRegKey();
		String requestURI = request.getRequestURI();
		if (!ArgUtil.isEmpty(deviceKey) && !requestURI.startsWith(DeviceConstants.DEVICE_PAIR)) {
			AppContextUtil.setDeviceRegKey(deviceKey);
			DeviceData deviceData = deviceBox.get(deviceKey);
			if (deviceData == null) {
				HttpServletResponse response = ((HttpServletResponse) resp);
				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", DeviceConstants.DEVICE_PAIR);
			} else {
				chain.doFilter(req, resp);
			}
		} else {
			chain.doFilter(req, resp);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// empty
	}
}
