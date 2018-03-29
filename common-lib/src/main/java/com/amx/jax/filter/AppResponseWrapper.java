package com.amx.jax.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.util.StringUtils;

import com.amx.jax.AppConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public class AppResponseWrapper extends HttpServletResponseWrapper {

	private boolean isheaderSet = false;

	public AppResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void setStatus(int sc) {
		super.setStatus(sc);
		handleStatus(sc);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, sm);
		handleStatus(sc);
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		super.sendError(sc, msg);
		handleStatus(sc);
	}

	@Override
	public void sendError(int sc) throws IOException {
		super.sendError(sc);
		handleStatus(sc);
	}

	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, value);
		handleStatus(this.getStatus());
	}

	@Override
	public void addHeader(String name, String value) {
		super.addHeader(name, value);
		handleStatus(this.getStatus());
	}

	private void handleStatus(int code) {
		if (!isheaderSet) {
			String tranxId = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
			String traceId = ContextUtil.getTraceId();
			if (!StringUtils.isEmpty(tranxId)) {
				super.addHeader(AppConstants.TRANX_ID_XKEY, tranxId);
			}
			if (!StringUtils.isEmpty(traceId)) {
				super.addHeader(AppConstants.TRACE_ID_XKEY, traceId);
			}
			isheaderSet = false;
		}
	}

}
