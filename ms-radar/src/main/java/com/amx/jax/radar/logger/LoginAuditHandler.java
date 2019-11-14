package com.amx.jax.radar.logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.common.HandlerBeanFactory.HandlerMapping;
import com.amx.jax.AppContextUtil;
import com.amx.jax.client.snap.SnapConstants;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.logger.AuditHandler;
import com.amx.jax.logger.AuditMapModel;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQueryParams;
import com.amx.jax.radar.snap.SnapQueryService;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

@Component
@HandlerMapping("LOGIN:COMPLETED:DONE")
public class LoginAuditHandler implements AuditHandler {

	@Autowired
	private SnapQueryService snapQueryService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;

	@Autowired
	PostManClient postManClient;

	@Override
	public void doHandle(AuditMapModel event) {
		SnapQueryParams params = new SnapQueryParams();
		params.toMap().put("traceid", AppContextUtil.getTenant() + "-" + AppContextUtil.getTraceId());
		params.toMap().put("customerId", event.getCustomerId());
		params.toMap().put("clientFp", event.getClientFp());
		params.toMap().put("clientIp", event.getClientIp());
		params.toMap().put("logmap", event.toMap());

		SnapModelWrapper x = snapQueryService.process(SnapConstants.SnapQueryTemplate.CUSTOMER_LOGIN, params);

		if (!isKnownDevice(event, x, params)) {
			Customer customer = customerRepository.getActiveCustomerDetailsByCustomerId(event.getCustomerId());
			CommunicationPrefsResult prefs = communicationPrefsUtil.forCustomer(CommunicationEvents.NEW_DEVICE_LOGIN,
					customer);

			if (prefs.isEmail()) {
				params.toMap().put("name", customer.getFirstName() + " " + customer.getLastName());

				Email email = new Email();
				email.addTo(customer.getEmail());
				email.setHtml(true);
				email.setModel(params.toMap());
				email.setITemplate(TemplatesMX.NEW_DEVICE_LOGIN);
				postManClient.sendEmailAsync(email);
			}
		}

	}

	private boolean isKnownDevice(AuditMapModel event, SnapModelWrapper x, SnapQueryParams params) {
		List<Map<String, Object>> bulk = x.getBulk();
		params.toMap().put("newDevice", true);
		params.toMap().put("newLocation", true);
		if (ArgUtil.is(bulk) && bulk.size() == 0) {
			return false;
		}

		Map<String, Object> bulkItemThis = null;
		Map<String, Boolean> mapFP = new HashMap<String, Boolean>();
		Map<String, Boolean> mapIP = new HashMap<String, Boolean>();
		Map<String, Boolean> mapCountry = new HashMap<String, Boolean>();
		Map<String, Boolean> mapRegion = new HashMap<String, Boolean>();
		Map<String, Boolean> mapCity = new HashMap<String, Boolean>();

		for (Map<String, Object> bulkItem : bulk) {
			if ("this".equals(bulkItem.get("traceid"))) {
				bulkItemThis = bulkItem;
			} else {
				mapFP.put(ArgUtil.parseAsString(bulkItem.get("fp"), Constants.BLANK), true);
				mapIP.put(ArgUtil.parseAsString(bulkItem.get("ip"), Constants.BLANK), true);
				mapIP.put(ArgUtil.parseAsString(bulkItem.get("country"), Constants.BLANK), true);
				mapIP.put(ArgUtil.parseAsString(bulkItem.get("region"), Constants.BLANK), true);
				mapIP.put(ArgUtil.parseAsString(bulkItem.get("city"), Constants.BLANK), true);
			}
		}

		if (mapFP.containsKey(event.getClientFp())) {

			params.toMap().put("newDevice", false);

			if (bulkItemThis == null) {
				return true;
			}
			String country = ArgUtil.parseAsString(bulkItemThis.get("country"));
			String region = ArgUtil.parseAsString(bulkItemThis.get("region"));
			String city = ArgUtil.parseAsString(bulkItemThis.get("city"));

			if (!mapCountry.containsKey(country)
					|| !mapRegion.containsKey(region)
					|| !mapCity.containsKey(city)) {
				params.toMap().put("country", country);
				params.toMap().put("region", region);
				params.toMap().put("city", city);

				return false;
			}
		}

		if (mapIP.containsKey(event.getClientIp())) {
			return true;
		}

		return false;
	}

}
