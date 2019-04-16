package com.amx.jax.radar.jobs.scrapper;

import java.io.IOException;
import java.math.BigDecimal;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.radar.jobs.customer.OracleVarsCache;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncJobs;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
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
@ConditionalOnProperty("jax.jobs.scrapper.rate")
public class MuzainiJob {

	@Autowired
	RestService restService;

	@Autowired
	public ESRepository esRepository;

	@Autowired
	public OracleVarsCache oracleVarsCache;

	XmlMapper xmlMapper = new XmlMapper();

	public static final Logger LOGGER = LoggerService.getLogger(MuzainiJob.class);

	@SchedulerLock(lockMaxAge = AmxCurConstants.INTERVAL_HRS, context = LockContext.BY_CLASS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void lockedTask() {
		doTask();
	}

	public void doTask() {
		LOGGER.info("Scrapper Task");
		try {
			Document doc = Jsoup.connect("http://www.muzaini.com/ExchangeRates.aspx")
					.data("ddlCurrency", "KWD")
					.data("ScriptManager1", "ScriptManager1|ddlCurrency")
					.referrer("http://www.muzaini.com")
					.post();

			Elements trs = doc.select("#UpdatePanel1 table.ex-table tbody tr");
			BulkRequestBuilder builder = new BulkRequestBuilder();
			for (Element tr : trs) {
				Elements tds = tr.select("td");
				Currency cur = (Currency) ArgUtil.parseAsEnum(tds.get(0).text(),
						Currency.UNKNOWN);
				if (!Currency.UNKNOWN.equals(cur) && tds.size() >= 4) {

					BigDecimal rate = ArgUtil.parseAsBigDecimal(tds.get(4).text());
					AmxCurRate sellTrnsfrRate = new AmxCurRate();
					sellTrnsfrRate.setrSrc(RateSource.MUZAINI);
					sellTrnsfrRate.setrDomCur(Currency.KWD);
					sellTrnsfrRate.setrForCur(cur);
					if (!ArgUtil.isEmpty(rate)) {
						sellTrnsfrRate.setrType(RateType.SELL_TRNSFR);
						sellTrnsfrRate.setrRate(rate);
						builder.update(oracleVarsCache.getIndex(DBSyncJobs.XRATE_JOB),
								new OracleViewDocument(sellTrnsfrRate));
					}
					AmxCurRate sellCash = sellTrnsfrRate.clone();
					BigDecimal sellCashRate = ArgUtil.parseAsBigDecimal(tds.get(3).text());
					if (!ArgUtil.isEmpty(sellCashRate)) {
						sellCash.setrType(RateType.SELL_CASH);
						sellTrnsfrRate.setrRate(sellCashRate);
						builder.update(oracleVarsCache.getIndex(DBSyncJobs.XRATE_JOB),
								new OracleViewDocument(sellCash));
					}

					AmxCurRate buyCash = sellTrnsfrRate.clone();
					BigDecimal buyCashRate = ArgUtil.parseAsBigDecimal(tds.get(2).text());
					if (!ArgUtil.isEmpty(buyCashRate)) {
						buyCash.setrType(RateType.BUY_CASH);
						sellTrnsfrRate.setrRate(buyCashRate);
						builder.update(oracleVarsCache.getIndex(DBSyncJobs.XRATE_JOB),
								new OracleViewDocument(buyCash));
					}
				}
			}
			esRepository.bulk(builder.build());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
