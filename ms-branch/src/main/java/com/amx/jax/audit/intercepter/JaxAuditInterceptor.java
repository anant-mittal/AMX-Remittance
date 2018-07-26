package com.amx.jax.audit.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.amx.jax.auditlog.handlers.AbstractAuditHanlder;
import com.amx.jax.utils.JaxContextUtil;

@Component
public class JaxAuditInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	ApplicationContext applicationContext;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (JaxContextUtil.getJaxEvent() != null) {
			Class<? extends AbstractAuditHanlder> auditHanlder = JaxContextUtil.getJaxEvent().getAuditHanlder();
			if (auditHanlder != null) {
				applicationContext.getBean(auditHanlder).log();
			}
		}
	}

}
