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
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.TestSizeApp;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.rates.AmxCurRateRepository;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
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
	private AmxCurRateRepository curRateRepository;

	XmlMapper xmlMapper = new XmlMapper();

	Logger logger = LoggerService.getLogger(UAEXChangeJob.class);

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void doTask() {
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
			fetchrates(doc1, RType.SELL_TRNSFR);

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
			fetchrates(doc2, RType.SELL_CASH);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void fetchrates(Document doc, RType type) {
		Elements trs = doc.select("#ctl10_updatepnl table.table tbody tr");
		for (Element tr : trs) {
			Elements tds = tr.select("td");
			AmxCurConstants.RCur cur = (RCur) ArgUtil.parseAsEnum(tds.get(2).text(),
					AmxCurConstants.RCur.UNKNOWN);
			if (!AmxCurConstants.RCur.UNKNOWN.equals(cur) && tds.size() >= 3) {
				BigDecimal rate = ArgUtil.parseAsBigDecimal(tds.get(3).text());
				if (!ArgUtil.isEmpty(rate)) {
					AmxCurRate trnsfrRate = new AmxCurRate();
					trnsfrRate.setrSrc(RSource.UAEXCHANGE);
					trnsfrRate.setrDomCur(RCur.KWD);
					trnsfrRate.setrForCur(cur);
					trnsfrRate.setrType(type);
					trnsfrRate.setrRate(rate);
					// System.out.println(JsonUtil.toJson(trnsfrRate));
					curRateRepository.insertRate(trnsfrRate);
				}
			}
		}
	}

}
