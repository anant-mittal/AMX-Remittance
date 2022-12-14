package com.amx.jax.radar.jobs.scrapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.client.snap.ISnapService.RateSource;
import com.amx.jax.client.snap.ISnapService.RateType;
import com.amx.jax.dict.Currency;
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
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
//@ConditionalOnProperty({ "jax.jobs.scrapper.rate", "elasticsearch.enabled" })
@ConditionalOnExpression(RadarConfig.CE_RATE_SCRAPPER_AND_ES_AND_KWT)
public class BECKuwaitJob extends ARadarTask {

	@Autowired
	RestService restService;

	@Autowired
	public ESRepository esRepository;

	@Autowired
	public OracleVarsCache oracleVarsCache;

	XmlMapper xmlMapper = new XmlMapper();

	public static final Logger LOGGER = LoggerService.getLogger(BECKuwaitJob.class);

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_HRS, context = LockContext.BY_CLASS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void lockedTask() {
		doTask();
	}

	public void doTask() {
		LOGGER.debug("Scrapper Task");

		try {
			Document doc = Jsoup.connect("https://www.bec.com.kw/currency-exchange-rates?atype=money&continent=popular")
					.get();
			Elements transferTabs = doc.select(".currency-nav-tab-holder.transfer");
			if (transferTabs.size() > 0) {
				Elements trs = transferTabs.get(0).select("tr");
				BulkRequestSnapBuilder builder = new BulkRequestSnapBuilder();
				for (Element tr : trs) {
					Currency cur = (Currency) ArgUtil.parseAsEnum(tr.select(".bfc-country-code").text(),
							Currency.UNKNOWN);
					if (!Currency.UNKNOWN.equals(cur)) {
						BigDecimal rate = ArgUtil.parseAsBigDecimal(tr.select(".tg-buy .bfc-currency-rates").text());
						if (!ArgUtil.isEmpty(rate)) {
							AmxCurRate trnsfrRate = new AmxCurRate();
							trnsfrRate.setrSrc(RateSource.BECKWT);
							trnsfrRate.setrDomCur(Currency.KWD);
							trnsfrRate.setrForCur(cur);
							trnsfrRate.setrType(RateType.SELL_TRNSFR);
							trnsfrRate.setrRate(BigDecimal.ONE.divide(rate, 12, RoundingMode.CEILING));
							builder.update(DBSyncIndex.XRATE_JOB.getIndexName(),
									new OracleViewDocument(trnsfrRate));
						}
					}
				}
				esRepository.bulk(builder.build());
			}

			Elements xTabs = doc.select(".currency-nav-tab-holder.exchange");
			if (xTabs.size() > 0) {
				Elements trs = xTabs.get(0).select("tr");
				BulkRequestSnapBuilder builder = new BulkRequestSnapBuilder();
				for (Element tr : trs) {
					Currency cur = (Currency) ArgUtil.parseAsEnum(tr.select(".bfc-country-code").text(),
							Currency.UNKNOWN);
					if (!Currency.UNKNOWN.equals(cur)) {

						AmxCurRate buyCash = new AmxCurRate(RateSource.BECKWT, Currency.KWD, cur);

						BigDecimal buyCashRate = ArgUtil
								.parseAsBigDecimal(tr.select(".tg-buy .bfc-currency-rates").text());
						if (!ArgUtil.isEmpty(buyCashRate)) {
							buyCash.setrType(RateType.BUY_CASH);
							buyCash.setrRate(BigDecimal.ONE.divide(buyCashRate, 12, RoundingMode.CEILING));
							builder.update(DBSyncIndex.XRATE_JOB.getIndexName(),
									new OracleViewDocument(buyCash));
						}

						AmxCurRate sellCash = buyCash.clone();
						BigDecimal sellCashRate = ArgUtil
								.parseAsBigDecimal(tr.select(".tg-buy .bfc-currency-rates").text());
						if (!ArgUtil.isEmpty(sellCashRate)) {
							sellCash.setrType(RateType.SELL_CASH);
							sellCash.setrRate(BigDecimal.ONE.divide(sellCashRate, 12, RoundingMode.CEILING));
							builder.update(DBSyncIndex.XRATE_JOB.getIndexName(),
									new OracleViewDocument(sellCash));
						}

					}
				}
				esRepository.bulk(builder.build());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
