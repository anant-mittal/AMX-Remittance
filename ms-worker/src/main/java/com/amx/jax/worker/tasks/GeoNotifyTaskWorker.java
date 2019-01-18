package com.amx.jax.worker.tasks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.task.events.GeoNotifyTask;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;

@TunnelEventMapping(byEvent = GeoNotifyTask.class, scheme = TunnelEventXchange.TASK_WORKER)
public class GeoNotifyTaskWorker implements ITunnelSubscriber<GeoNotifyTask> {

	@Autowired
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExchangeRateClient xRateClient;

	@Autowired
	private BeneClient beneClient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private AuditService auditService;

	@Override
	public void onMessage(String channel, GeoNotifyTask task) {

		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

		jaxMetaInfo.setCustomerId(task.getCustomerId());

		List<String> messages = new ArrayList<>();
		List<MinMaxExRateDTO> rates = xRateClient.getMinMaxExchangeRate().getResults();
		List<BeneficiaryListDTO> benes = beneClient.getBeneficiaryList(new BigDecimal(0)).getResults();

		PushMessage pushMessage = new PushMessage();
		String customerNotificationTitle = String.format("Special rate @ %s", task.getAppTitle());
		for (MinMaxExRateDTO minMaxExRateDTO : rates) {
			boolean toAdd = false;
			for (BeneficiaryListDTO beneficiaryListDTO : benes) {
				if (ArgUtil.areEqual(minMaxExRateDTO.getToCurrency().getCurrencyId(),
						beneficiaryListDTO.getCurrencyId())) {
					toAdd = true;
					continue;
				}
			}
			if (toAdd) {
				String messageStr = String.format(
						"Get more %s for your %s at %s. %s-%s Special rate in the "
								+ "range of %.4f – %.4f for %s online and App users.",
						minMaxExRateDTO.getToCurrency().getCurrencyName(),
						minMaxExRateDTO.getFromCurrency().getCurrencyName(), task.getAppTitle(),
						minMaxExRateDTO.getFromCurrency().getQuoteName(),
						minMaxExRateDTO.getToCurrency().getQuoteName(), minMaxExRateDTO.getMinExrate(),
						minMaxExRateDTO.getMaxExrate(), task.getAppTitle());
				messages.add(messageStr);
			}
		}
		CActivityEvent event = new CActivityEvent(CActivityEvent.Type.GEO_LOCATION);
		event.setCustomer(task.getCustomerId());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("hotpoint", task.getGeoPoint());
		data.put("messages", messages);
		event.setData(data);

		pushMessage.setSubject(customerNotificationTitle);
		pushMessage.setLines(messages);
		pushMessage.addToUser(task.getCustomerId());

		auditService.log(event);
		pushNotifyClient.sendDirect(pushMessage);

	}
}