package com.amx.jax.meta.api;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AmxSharedConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CommunicationPrefsModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.ICommunicationPrefsRepository;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;

/**
 * 
 * @author lalittanwar
 *
 */
@RestController
public class MetaConfigController implements AmxSharedConfig {

	private static final Logger LOGGER = LoggerService.getLogger(MetaConfigController.class);

	@Autowired
	private AmxSharedConfigDB amxSharedConfigDB;

	@RequestMapping(value = AmxSharedConfigApi.Path.COMM_PREFS, method = RequestMethod.GET)
	public AmxApiResponse<CommunicationPrefs, Object> getCommunicationPrefs() {
		return amxSharedConfigDB.getCommunicationPrefs();
	}

	@Autowired
	ICommunicationPrefsRepository communicationPrefsRepository;

	@RequestMapping(value = AmxSharedConfigApi.Path.COMM_PREFS, method = RequestMethod.POST)
	public AmxApiResponse<CommunicationPrefs, Object> getCommunicationPrefs(
			@RequestParam CommunicationEvents event,
			@RequestBody CommunicationPrefsModel prefs) {
		CommunicationPrefsModel x = communicationPrefsRepository
				.findByEvent(ArgUtil.ifNotEmpty(event, prefs.getEvent()));

		if (ArgUtil.isEmpty(x)) {
			x = new CommunicationPrefsModel();
		}
		x.setEvent(event);
		x.setEmailPrefs(prefs.getEmailPrefs());
		x.setPushPrefs(prefs.getPushPrefs());
		x.setSmsPrefs(prefs.getSmsPrefs());
		x.setWaPrefs(prefs.getWaPrefs());

		communicationPrefsRepository.save(x);
		return amxSharedConfigDB.getCommunicationPrefs();
	}

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private CommunicationPrefsUtil communicationPrefsUtil;

	@RequestMapping(value = "/test/meta/config/customer", method = RequestMethod.GET)
	public CommunicationPrefsResult getCommunicationPrefsTest(
			@RequestParam CommunicationEvents event,
			@RequestParam String identityInt) {
		Customer x = customerRepository.getCustomerOneByIdentityInt(identityInt);
		return communicationPrefsUtil.forCustomer(event, x);

	}

}
