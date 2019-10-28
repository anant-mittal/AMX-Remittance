package com.amx.jax.radar.jobs.scrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.snap.ISnapService.RateSource;
import com.amx.jax.client.snap.ISnapService.RateType;
import com.amx.jax.dict.Currency;
import com.amx.jax.dict.Language;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.shedlock.SchedulerLock;
import com.amx.jax.mcq.shedlock.SchedulerLock.LockContext;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.radar.RadarConfig;
import com.amx.jax.radar.jobs.customer.OracleVarsCache;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncIndex;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.radar.snap.SnapQueryService.BulkRequestSnapBuilder;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
//@ConditionalOnProperty({ "jax.jobs.scrapper.rate", "elasticsearch.enabled" })
@ConditionalOnExpression(RadarConfig.CE_RATE_SCRAPPER_AND_ES_AND_ANY_TNT)
public class AMXRatesJob extends ARadarTask {

	@Autowired
	public ESRepository esRepository;

	@Autowired
	public OracleVarsCache oracleVarsCache;

	@Autowired
	private ExchangeRateClient xRateClient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	public static final Logger LOGGER = LoggerService.getLogger(AMXRatesJob.class);

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_HRS, context = LockContext.BY_CLASS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void lockedTask() {
		doTask();
	}

	public void doTask() {

		LOGGER.debug("Scrapper Task");

		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

		List<MinMaxExRateDTO> rates = xRateClient.getMinMaxExchangeRate().getResults();

		RateType type = RateType.SELL_TRNSFR;

		BulkRequestSnapBuilder builder = new BulkRequestSnapBuilder();

		for (MinMaxExRateDTO minMaxExRateDTO : rates) {

			Currency cur = ((Currency) ArgUtil.parseAsEnum(minMaxExRateDTO.getToCurrency().getQuoteName(),
					Currency.UNKNOWN));
			Currency domCur = ((Currency) ArgUtil.parseAsEnum(minMaxExRateDTO.getFromCurrency().getQuoteName(),
					Currency.UNKNOWN));
			if (!Currency.UNKNOWN.equals(cur) && !Currency.UNKNOWN.equals(domCur)) {
				BigDecimal rate = ArgUtil.parseAsBigDecimal(minMaxExRateDTO.getMaxExrate());
				if (!ArgUtil.isEmpty(rate)) {
					AmxCurRate trnsfrRate = new AmxCurRate();
					trnsfrRate.setrSrc(RateSource.AMX);
					trnsfrRate.setrDomCur(domCur);
					trnsfrRate.setrForCur(cur.toISO3());
					trnsfrRate.setrType(type);
					trnsfrRate.setrRate(rate);
					trnsfrRate.setrRate(BigDecimal.ONE.divide(rate, 12, RoundingMode.CEILING));
					// System.out.println(JsonUtil.toJson(trnsfrRate));
					builder.update(DBSyncIndex.XRATE_JOB.getIndexName(),
							new OracleViewDocument(trnsfrRate));
				}
			}
		}

		esRepository.bulk(builder.build());

	}

}
