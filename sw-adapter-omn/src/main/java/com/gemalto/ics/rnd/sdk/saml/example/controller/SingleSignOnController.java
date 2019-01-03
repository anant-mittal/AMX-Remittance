/*
 *  (c) Copyright Gemalto, 2017
 *  ALL RIGHTS RESERVED UNDER COPYRIGHT LAWS.
 *  CONTAINS CONFIDENTIAL AND TRADE SECRET INFORMATION.
 *  GEMALTO MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GEMALTO SHALL NOT BE
 *  LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 *  MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *  THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
 *  CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
 *  PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
 *  NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
 *  SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
 *  SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
 *  PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). GEMALTO
 *  SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
 *  HIGH RISK ACTIVITIES.
 */
package com.gemalto.ics.rnd.sdk.saml.example.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opensaml.messaging.context.MessageContext;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gemalto.ics.dc.oids.sdk.saml.OmanSingleSignOnClient;
import com.gemalto.ics.dc.oids.sdk.saml.model.OmanAuthenticationMethod;
import com.gemalto.ics.dc.oids.sdk.saml.model.OmanAuthnRequestInput;
import com.gemalto.ics.rnd.sdk.saml.binding.Binding;
import com.gemalto.ics.rnd.sdk.saml.example.i18n.I18n;
import com.gemalto.ics.rnd.sdk.saml.example.i18n.MessageSourceFacade;
import com.gemalto.ics.rnd.sdk.saml.example.model.AttributeDto;
import com.gemalto.ics.rnd.sdk.saml.example.model.LoginRequest;
import com.gemalto.ics.rnd.sdk.saml.exception.AuthenticationCancelledException;
import com.gemalto.ics.rnd.sdk.saml.exception.SAMLException;
import com.gemalto.ics.rnd.sdk.saml.model.AuthenticationMethod;
import com.gemalto.ics.rnd.sdk.saml.model.AuthnRequestInput;
import com.gemalto.ics.rnd.sdk.saml.model.SAMLCredential;
import com.gemalto.ics.rnd.sdk.saml.util.SAMLUtils;

/**
 * The {@code SingleSignOnController} class implements a controller performing
 * single sign-on.
 */
@Controller
public class SingleSignOnController {

	protected static final String VIEW_NAME = "single-sign-on";

	private static final Logger LOGGER = LoggerFactory.getLogger(SingleSignOnController.class);

	@Autowired
	private OmanSingleSignOnClient singleSignOnClient;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/single-sign-on", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest httpRequest, Locale locale) {
		Map<String, Object> model = new LinkedHashMap<>(getCommonModel(locale));
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("test username");
		loginRequest.setMachineName("test machine name");
		loginRequest.setLocation("test location");
		model.put("loginRequest", loginRequest);

		HttpSession session = httpRequest.getSession(false);
		if (session != null) {
			SAMLCredential samlCredential = (SAMLCredential) session.getAttribute(SessionAttribute.SAML_CREDENTIAL);
			if (samlCredential != null) {
				model.putAll(getUserModel(samlCredential));
			}
		}

		return new ModelAndView(VIEW_NAME, model);
	}

	private Map<String, Object> getCommonModel(Locale locale) {
		Map<String, Object> model = new LinkedHashMap<>();

		model.put("bindings", I18n.enumAsMap(Binding.class, messageSource, locale));

		Map<String, String> authMethods = new LinkedHashMap<>();
		final MessageSourceFacade authMethodI18N = new MessageSourceFacade(messageSource, locale, "AuthenticationMethod");
		authMethods.put(OmanAuthenticationMethod.OMAN_ID_CARD.getUri(), authMethodI18N.getMessage("OMAN_ID_CARD"));
		authMethods.put(OmanAuthenticationMethod.OMAN_ID_CARD_AUTHENTICATION.getUri(), authMethodI18N.getMessage("OMAN_ID_CARD_AUTHENTICATION"));
		model.put("authMethods", authMethods);

		return model;
	}

	private Map<String, Object> getUserModel(SAMLCredential samlCredential) {
		Map<String, Object> model = new LinkedHashMap<>();

		model.put("logged", true);
		Map<String, List<String>> attributes = samlCredential.getAttributes();
		model.put("attributes", toAttributeDtoList(attributes));
		model.put("username", getUsername(samlCredential));

		return model;
	}

	private String getUsername(SAMLCredential samlCredential) {
		List<String> values = samlCredential.getAttributes().get("name");
		if (values != null && !values.isEmpty()) {
			return values.get(0);
		} else {
			return samlCredential.getNameID().getValue();
		}
	}

	private List<AttributeDto> toAttributeDtoList(Map<String, List<String>> attributeMap) {
		List<AttributeDto> attributeDtoList = new LinkedList<>();

		for (Map.Entry<String, List<String>> entry : attributeMap.entrySet()) {
			List<String> values = entry.getValue();
			for (String value : values) {
				attributeDtoList.add(new AttributeDto(entry.getKey(), value));
			}
		}

		return attributeDtoList;
	}

	@RequestMapping(value = "/single-sign-on", method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("loginRequest") LoginRequest loginRequest, BindingResult bindingResult, Locale locale,
			HttpSession session, HttpServletResponse response) {
		String requestId = SAMLUtils.generateRequestId();
		List<AuthenticationMethod> authenticationMethods = null;
		if (loginRequest.getAuthenticationMethod() != null) {
			authenticationMethods = Collections.singletonList(new AuthenticationMethod(loginRequest.getAuthenticationMethod()));
		}
		OmanAuthnRequestInput authnRequestInput = new OmanAuthnRequestInput(requestId, authenticationMethods, loginRequest.getRelayState());

		LOGGER.debug("SP attribute [Username] value: " + loginRequest.getUsername());
		LOGGER.debug("SP attribute [UserCivilNumber] value: " + loginRequest.getUserCivilNumber());
		LOGGER.debug("SP attribute [MachineName] value: " + loginRequest.getMachineName());
		LOGGER.debug("SP attribute [Location] value: " + loginRequest.getLocation());
		LOGGER.debug("SP attribute [TransactionType] value: " + loginRequest.getTransactionType());
		LOGGER.debug("SP attribute [TransactionDetails] value: " + loginRequest.getTransactionDetails());
		LOGGER.debug("SP attribute [ApplicantMobile] value: " + loginRequest.getApplicantMobile());
		authnRequestInput.setOmanSpFields(loginRequest.getUsername(), loginRequest.getUserCivilNumber(), loginRequest.getMachineName(),
				loginRequest.getLocation(), loginRequest.getTransactionType(), loginRequest.getTransactionDetails(),
				loginRequest.getApplicantMobile());

		session.setAttribute(SessionAttribute.LAST_AUTHN_REQUEST_INPUT, authnRequestInput);
		try {
			AuthnRequest authnRequest = singleSignOnClient.createAuthnRequest(authnRequestInput);

			singleSignOnClient.sendAuthnRequest(authnRequest, loginRequest.getBinding(), authnRequestInput.getRelayState(), response);
		} catch (SAMLException e) {
			LOGGER.warn("Error sending authentication request", e);
			Map<String, Object> model = new LinkedHashMap<>(getCommonModel(locale));
			model.put("errorMessage", messageSource.getMessage("error.authnRequest", new Object[]{e.getMessage()}, locale));
			return new ModelAndView(VIEW_NAME, model);
		}
		return null;
	}

	@RequestMapping(value = "/saml/acs", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView acs(HttpSession session, HttpServletRequest httpRequest, RedirectAttributes redirectAttributes, Locale locale) {
		AuthnRequestInput requestInput = (AuthnRequestInput) session.getAttribute(SessionAttribute.LAST_AUTHN_REQUEST_INPUT);
		SAMLCredential samlCredential = null;
		try {
			MessageContext<Response> messageContext = singleSignOnClient.createMessageContext(httpRequest);
			samlCredential = singleSignOnClient.processAuthnResponse(messageContext, requestInput);
		} catch (AuthenticationCancelledException e) {
			redirectAttributes.addFlashAttribute("infoMessage", messageSource.getMessage("authn.cancelled", null, locale));
		} catch (SAMLException e) {
			LOGGER.warn("Error consuming assertion", e);
			redirectAttributes.addFlashAttribute("errorMessage",
					messageSource.getMessage("error.authnResponse", new Object[]{e.getMessage()}, locale));
		}
		if (samlCredential == null) {
			session.removeAttribute(SessionAttribute.SAML_CREDENTIAL);
		} else {
			session.setAttribute(SessionAttribute.SAML_CREDENTIAL, samlCredential);
		}
		return new ModelAndView(new RedirectView("/single-sign-on", true));
	}

}
