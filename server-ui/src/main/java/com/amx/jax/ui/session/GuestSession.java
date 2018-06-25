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
import com.amx.jax.dict.Language;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.auth.AuthState.AuthFlow;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.jax.ui.auth.CAuthEvent;
import com.amx.jax.ui.config.HttpUnauthorizedException;
import com.amx.utils.Random;
import com.amx.utils.TimeUtils;

/**
 * To Save Values to Session Use this class, only if these values are not
 * related to Valid user Request.
 *
 * @author lalittanwar
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GuestSession implements Serializable {

	private static final long serialVersionUID = -8825493107883952226L;

	private static final Logger log = LoggerFactory.getLogger(GuestSession.class);

	@Autowired
	TenantContext<AuthLib> tenantContext;

	@Autowired
	AuditService auditService;

	Language language = Language.DEFAULT;

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * Sets the lang.
	 *
	 * @param lang
	 *            the new lang
	 */
	public void setLanguage(Language lang) {
		this.language = lang;
	}

	/** The state. */
	public AuthState state = new AuthState();

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public AuthState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state
	 *            the new state
	 */
	public void setState(AuthState state) {
		this.state = state;
	}

	/** The identity. */
	String identity = null;

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Sets the identity.
	 *
	 * @param identiy
	 *            the new identity
	 */
	public void setIdentity(String identiy) {
		this.identity = identiy;
	}

	/** The return url. */
	String returnUrl = null;

	/**
	 * Sets the return url.
	 *
	 * @param returnUrl
	 *            the new return url
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	/**
	 * Inits the flow.
	 *
	 * @param flow
	 *            the flow
	 */
	public void initFlow(AuthFlow flow) {
		state.flow = flow;
		state.cStep = null;
		state.timestamp = System.currentTimeMillis();
		state.nStep = tenantContext.get().getNextAuthStep(state);
	}

	/**
	 * Inits the step.
	 *
	 * @param step
	 *            the step
	 */
	public void initStep(AuthStep step) {
		// AuthStep nStep = tenantContext.get().getNextAuthStep(state);
		if (step != state.nStep) {
			auditService.log(new CAuthEvent(state, CAuthEvent.Result.FAIL, HttpUnauthorizedException.UN_SEQUENCE,
					TimeUtils.timeSince(state.timestamp)));
			// throw new HttpUnauthorizedException(HttpUnauthorizedException.UN_SEQUENCE);
		}
	}

	/**
	 * End step.
	 *
	 * @param step
	 *            the step
	 * @return the auth state
	 */
	public AuthState endStep(AuthStep step) {
		auditService.log(new CAuthEvent(state));
		state.cStep = step;
		state.nStep = tenantContext.get().getNextAuthStep(state);
		if (state.nStep == AuthStep.COMPLETED) {
			auditService.log(new CAuthEvent(state, CAuthEvent.Result.PASS, TimeUtils.timeSince(state.timestamp)));
			state.flow = null;
		}
		return state;
	}

	/** The response. */
	@Autowired
	private HttpServletResponse response;

	/** The next token map. */
	private Map<String, String> nextTokenMap = new HashMap<String, String>();

	/**
	 * Gets the next token.
	 *
	 * @param key
	 *            the key
	 * @return the next token
	 */
	public String getNextToken(String key) {
		String nextToken = Random.randomAlpha(6);
		nextTokenMap.put(key, nextToken);
		log.info("Created {} = {}", key, nextToken);
		return nextToken;
	}

	/**
	 * Validate.
	 *
	 * @param curEnd
	 *            the cur end
	 * @param validEnds
	 *            the valid ends
	 */
	public void validate(String curEnd, String[] validEnds) {
		Cookie kooky = new Cookie(UIConstants.SEQ_KEY, this.getNextToken(curEnd));
		kooky.setMaxAge(300);
		// kooky.setPath("/");
		response.addCookie(kooky);
	}

	/** The hits. */
	private Integer hits = 0;

	/**
	 * Gets the hits.
	 *
	 * @return the hits
	 */
	public Integer getHits() {
		return hits;
	}

	/**
	 * Sets the hits.
	 *
	 * @param hits
	 *            the new hits
	 */
	public void setHits(Integer hits) {
		this.hits = hits;
	}

	/**
	 * Hit counter.
	 *
	 * @return the integer
	 */
	public Integer hitCounter() {
		return this.hits++;
	}

	/** The customer model. */
	private CustomerModel customerModel = null;

	/**
	 * Gets the customer model.
	 *
	 * @return the customer model
	 */
	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	/**
	 * Sets the customer model.
	 *
	 * @param customerModel
	 *            the new customer model
	 */
	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	/** The ques index. */
	public Integer quesIndex = 0;

	/**
	 * Gets the ques index.
	 *
	 * @return the ques index
	 */
	public Integer getQuesIndex() {
		return quesIndex;
	}

	/**
	 * Sets the ques index.
	 *
	 * @param quesIndex
	 *            the new ques index
	 */
	public void setQuesIndex(Integer quesIndex) {
		this.quesIndex = quesIndex;
	}

	/**
	 * Next ques index.
	 */
	public void nextQuesIndex() {
		quesIndex++;
	}

	/**
	 * Started.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@PostConstruct
	public void started() throws Exception {
		SessionEvent evt = new SessionEvent();
		evt.setType(SessionEvent.Type.SESSION_STARTED);
		auditService.log(evt);
	}

	/**
	 * Ended.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@PreDestroy
	public void ended() throws Exception {
		SessionEvent evt = new SessionEvent();
		evt.setType(SessionEvent.Type.SESSION_ENDED);
		auditService.log(evt);
	}

	/**
	 * Gets the return url.
	 *
	 * @return the return url
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

}