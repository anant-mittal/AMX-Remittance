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
import com.amx.jax.client.snap.SnapModels.SnapQueryParams;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditHandler;
import com.amx.jax.logger.AuditMapModel;
import com.amx.jax.logger.AuditService;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.radar.snap.SnapQueryService;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.StringUtils;

@Component
@HandlerMapping("LOGIN:COMPLETED:DONE")
public class LoginAuditHandler implements AuditHandler {

	private static final String UNKNOWN = "-";

	@Autowired
	private SnapQueryService snapQueryService;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;

	@Autowired
	PostManClient postManClient;

	@Autowired
	AuditService auditServiceClient;

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

				auditServiceClient.log(new RadarAuditEvents(
						RadarAuditEvents.Type.NEW_LOGIN_DEVICE).set(Result.ALERT).loginDeviceDetails(params.toMap()));

				// Email email = new Email();
				// email.addTo(customer.getEmail());
				// email.setHtml(true);
				// email.setModel(params.toMap());
				// email.setITemplate(TemplatesMX.NEW_DEVICE_LOGIN);
				// postManClient.sendEmailAsync(email);
			}
		}

	}

	private boolean isKnownDevice(AuditMapModel event, SnapModelWrapper x, SnapQueryParams params) {
		boolean newLocation = true;
		boolean newDevice = true;
		boolean newIP = true;
		List<Map<String, Object>> bulk = x.getBulk();
		params.toMap().put("newDevice", newDevice);
		params.toMap().put("newLocation", newLocation);
		params.toMap().put("newIP", newIP);
		if (ArgUtil.is(bulk) && bulk.size() == 0) {
			return false;
		}

		Map<String, Object> bulkItemThis = null;
		Map<String, Boolean> mapFP = new HashMap<String, Boolean>();

		for (Map<String, Object> bulkItem : bulk) {
			if ("this".equals(bulkItem.get("traceid"))) {
				bulkItemThis = bulkItem;
			} else {
				String ipAdddress = ArgUtil.parseAsString(bulkItem.get("fp"), Constants.BLANK);
				if (ArgUtil.is(ipAdddress)) {
					mapFP.put(ipAdddress, true);
					mapFP.put(StringUtils.maskIpAddress(ipAdddress), true);
				}
				mapFP.put(ArgUtil.parseAsString(bulkItem.get("ip"), Constants.BLANK), true);
				mapFP.put(ArgUtil.parseAsString(bulkItem.get("country"), Constants.BLANK), true);
				mapFP.put(ArgUtil.parseAsString(bulkItem.get("region"), Constants.BLANK), true);
				mapFP.put(ArgUtil.parseAsString(bulkItem.get("city"), Constants.BLANK), true);
			}
		}

		if (bulkItemThis != null) {
			String country = ArgUtil.parseAsString(bulkItemThis.get("country"));
			String region = ArgUtil.parseAsString(bulkItemThis.get("region"));
			String city = ArgUtil.parseAsString(bulkItemThis.get("city"));

			params.toMap().put("country", country);
			params.toMap().put("region", region);
			params.toMap().put("city", city);

			if (mapFP.containsKey(country)
					&& mapFP.containsKey(region)
					&& mapFP.containsKey(city) && !UNKNOWN.equals(city)) {
				newLocation = false;
			}
		} else {
			params.toMap().put("country", UNKNOWN);
			params.toMap().put("region", UNKNOWN);
			params.toMap().put("city", UNKNOWN);
			newLocation = false;
		}

		if (mapFP.containsKey(event.getClientFp())) {
			newDevice = false;
			return true;
		}

		if (mapFP.containsKey(event.getClientIp())) {
			newIP = false;
			return true;
		}

		if (mapFP.containsKey(StringUtils.maskIpAddress(event.getClientIp()))) {
			newIP = false;
		}

		if (!newDevice && (!newLocation || !newIP)) {
			return true;
		}

		params.toMap().put("newDevice", newDevice);
		params.toMap().put("newLocation", newLocation);
		params.toMap().put("newIP", newIP);
		return false;
	}

}
