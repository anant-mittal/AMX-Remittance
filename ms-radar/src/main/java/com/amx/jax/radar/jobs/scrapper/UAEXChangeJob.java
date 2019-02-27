package com.amx.jax.radar.jobs.scrapper;

import java.io.IOException;
import java.math.BigDecimal;

import org.jsoup.Connection;
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
import com.amx.jax.mcq.Candidate;
import com.amx.jax.mcq.MCQLocker;
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.radar.jobs.customer.OracleVarsCache;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncJobs;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurRate;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
@ConditionalOnProperty("jax.jobs.scrapper.rate")
public class UAEXChangeJob extends ARadarTask {

	private static final String UAE_XCHANGE_URL = "https://www.uaeexchange.com.kw/Rates.aspx";

	private static final String PARAM_SCRIPT_MANAGER1 = "ctl00$ScriptManager1";
	private static final String VALUE_SCRIPT_MANAGER1_AHREF_EXCHANGE = "ctl00$ctl10$updatepnl|ctl00$ctl10$ahrefExchange";
	private static final String VALUE_SCRIPT_MANAGER1_ANCHOR_FOREX = "ctl00$ctl10$updatepnl|ctl00$ctl10$anchorForex";
	private static final String PARAM_ANCHOR_FOREX = "ctl00$ctl10$anchorForex";
	private static final String PARAM_AHREF_EXCHANGE = "ctl00$ctl10$ahrefExchange";
	private static final String VALUE_EXCHANGE_RATES = "Exchange Rates";
	private static final String VALUE_FOREX_RATES = "Forex Rates";

	private static final String PARAM_NAME = "name";
	private static final String TAG_INPUT = "input";

	@Autowired
	public ESRepository esRepository;

	@Autowired
	public OracleVarsCache oracleVarsCache;

	XmlMapper xmlMapper = new XmlMapper();

	public static final Logger LOGGER = LoggerService.getLogger(UAEXChangeJob.class);

	private Candidate LOCK = new Candidate().fixedDelay(AmxCurConstants.INTERVAL_MIN_30)
			.maxAge(AmxCurConstants.INTERVAL_HRS).queue(UAEXChangeJob.class);

	@Autowired
	private MCQLocker mcq;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void lockedTask() {
		if (mcq.lead(LOCK)) {
			doTask();
			mcq.resign(LOCK);
		}
	}

	public void doTask() {
		LOGGER.info("Scrapper Task");
		try {
			Document doc0 = Jsoup.connect(UAE_XCHANGE_URL).get();

			Connection con1 = Jsoup.connect(UAE_XCHANGE_URL)
					.referrer(UAE_XCHANGE_URL);

			Elements inputs1 = doc0.select(TAG_INPUT);
			for (Element input : inputs1) {
				String key = input.attr(PARAM_NAME);
				if (!PARAM_ANCHOR_FOREX.equalsIgnoreCase(key)) {
					con1 = con1.data(key, input.val());
				}
			}
			con1 = con1.data(PARAM_SCRIPT_MANAGER1, VALUE_SCRIPT_MANAGER1_AHREF_EXCHANGE)
					.data(PARAM_AHREF_EXCHANGE, VALUE_EXCHANGE_RATES);

			Document doc1 = con1.post();
			fetchrates(doc1, RateType.SELL_TRNSFR);

			Connection con2 = Jsoup.connect(UAE_XCHANGE_URL)
					.referrer(UAE_XCHANGE_URL);

			Elements inputs2 = doc0.select(TAG_INPUT);
			for (Element input : inputs2) {
				String key = input.attr(PARAM_NAME);
				if (!PARAM_AHREF_EXCHANGE.equalsIgnoreCase(key)) {
					con2 = con2.data(key, input.val());
				}
			}
			con2 = con2.data(PARAM_SCRIPT_MANAGER1, VALUE_SCRIPT_MANAGER1_ANCHOR_FOREX)
					.data(PARAM_ANCHOR_FOREX, VALUE_FOREX_RATES);

			Document doc2 = con2.post();
			fetchrates(doc2, RateType.SELL_CASH);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void fetchrates(Document doc, RateType type) {
		Elements trs = doc.select("#ctl10_updatepnl table.table tbody tr");
		BulkRequestBuilder builder = new BulkRequestBuilder();
		for (Element tr : trs) {
			Elements tds = tr.select("td");
			Currency cur = (Currency) ArgUtil.parseAsEnum(tds.get(2).text(),
					Currency.UNKNOWN);
			if (!Currency.UNKNOWN.equals(cur) && tds.size() >= 3) {
				BigDecimal rate = ArgUtil.parseAsBigDecimal(tds.get(3).text());
				if (!ArgUtil.isEmpty(rate)) {
					AmxCurRate trnsfrRate = new AmxCurRate();
					trnsfrRate.setrSrc(RateSource.UAEXCHANGE);
					trnsfrRate.setrDomCur(Currency.KWD);
					trnsfrRate.setrForCur(cur);
					trnsfrRate.setrType(type);
					trnsfrRate.setrRate(rate);
					// System.out.println(JsonUtil.toJson(trnsfrRate));
					builder.update(oracleVarsCache.getIndex(DBSyncJobs.XRATE),
							new OracleViewDocument(trnsfrRate));
				}
			}
		}
		esRepository.bulk(builder.build());
	}

}
