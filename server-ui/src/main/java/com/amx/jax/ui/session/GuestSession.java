package com.amx.jax.ui.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.auth.AuthEvent;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.jax.ui.config.HttpUnauthorizedException;
import com.bootloaderjs.Random;

/**
 * To Save Values to Session Use this class, only if these values are not
 * related to Valid user Request
 * 
 * @author lalittanwar
 *
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GuestSession implements Serializable {

	private static final long serialVersionUID = -8825493107883952226L;
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	TenantContext<AuthLib> tenantContext;

	@Autowired
	AuditService auditService;

	public AuthState state = new AuthState();

	public AuthState getState() {
		return state;
	}

	public void setState(AuthState state) {
		this.state = state;
	}

	String identity = null;

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identiy) {
		this.identity = identiy;
	}

	public void initStep(AuthStep step) {
		// AuthStep nStep = tenantContext.get().getNextAuthStep(state);
		if (step != state.cStep) {
			auditService.log(new AuthEvent(AuthEvent.Type.AUTH_FAIL, state, HttpUnauthorizedException.UN_SEQUENCE));
			// throw new HttpUnauthorizedException(HttpUnauthorizedException.UN_SEQUENCE);
		}
	}

	public AuthState endStep(AuthStep step) {
		auditService.log(new AuthEvent(state));
		state.cStep = step;
		state.nStep = tenantContext.get().getNextAuthStep(state);
		if (state.nStep == AuthStep.COMPLETED) {
			state.flow = null;
		}
		return state;
	}

	@Autowired
	private HttpServletResponse response;

	private Map<String, String> nextTokenMap = new HashMap<String, String>();

	public String getNextToken(String key) {
		String nextToken = Random.randomAlpha(6);
		nextTokenMap.put(key, nextToken);
		log.info("Created {} = {}", key, nextToken);
		return nextToken;
	}

	public void validate(String curEnd, String[] validEnds) {
		Cookie kooky = new Cookie(UIConstants.SEQ_KEY, this.getNextToken(curEnd));
		kooky.setMaxAge(300);
		// kooky.setPath("/");
		response.addCookie(kooky);
	}

	private Integer hits = 0;

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Integer hitCounter() {
		return this.hits++;
	}

	private CustomerModel customerModel = null;

	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	public Integer quesIndex = 0;

	public Integer getQuesIndex() {
		return quesIndex;
	}

	public void setQuesIndex(Integer quesIndex) {
		this.quesIndex = quesIndex;
	}

	public void nextQuesIndex() {
		quesIndex++;
	}

	@PostConstruct
	public void started() throws Exception {
		SessionEvent evt = new SessionEvent();
		evt.setType(SessionEvent.Type.SESSION_STARTED);
		auditService.log(evt);
	}

	@PreDestroy
	public void ended() throws Exception {
		SessionEvent evt = new SessionEvent();
		evt.setType(SessionEvent.Type.SESSION_ENDED);
		auditService.log(evt);
	}

}