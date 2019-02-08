package com.amx.jax.radar.jobs.scrapper;

import java.io.IOException;
import java.math.BigDecimal;

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
import com.amx.jax.mcq.Candidate;
import com.amx.jax.mcq.MCQLock;
import com.amx.jax.radar.TestSizeApp;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.rates.AmxCurRateRepository;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class MuzainiJob {

	@Autowired
	RestService restService;

	@Autowired
	private AmxCurRateRepository curRateRepository;

	XmlMapper xmlMapper = new XmlMapper();

	public static final Logger LOGGER = LoggerService.getLogger(MuzainiJob.class);

	private Candidate LOCK = new Candidate().fixedDelay(AmxCurConstants.INTERVAL_MIN_30)
			.maxAge(AmxCurConstants.INTERVAL_HRS).queue(MuzainiJob.class);

	@Autowired
	private MCQLock mcq;

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
			Document doc = Jsoup.connect("http://www.muzaini.com/ExchangeRates.aspx")
					.data("ddlCurrency", "KWD")
					.data("ScriptManager1", "ScriptManager1|ddlCurrency")
					.referrer("http://www.muzaini.com")
					.post();

			Elements trs = doc.select("#UpdatePanel1 table.ex-table tbody tr");
			for (Element tr : trs) {
				Elements tds = tr.select("td");
				AmxCurConstants.RCur cur = (RCur) ArgUtil.parseAsEnum(tds.get(0).text(),
						AmxCurConstants.RCur.UNKNOWN);
				if (!AmxCurConstants.RCur.UNKNOWN.equals(cur) && tds.size() >= 4) {

					BigDecimal rate = ArgUtil.parseAsBigDecimal(tds.get(4).text());
					AmxCurRate sellTrnsfrRate = new AmxCurRate();
					sellTrnsfrRate.setrSrc(RSource.MUZAINI);
					sellTrnsfrRate.setrDomCur(RCur.KWD);
					sellTrnsfrRate.setrForCur(cur);
					if (!ArgUtil.isEmpty(rate)) {
						sellTrnsfrRate.setrType(RType.SELL_TRNSFR);
						sellTrnsfrRate.setrRate(rate);
						curRateRepository.insertRate(sellTrnsfrRate);
					}
					AmxCurRate sellCash = sellTrnsfrRate.clone();
					BigDecimal sellCashRate = ArgUtil.parseAsBigDecimal(tds.get(3).text());
					if (!ArgUtil.isEmpty(sellCashRate)) {
						sellCash.setrType(RType.SELL_CASH);
						sellTrnsfrRate.setrRate(sellCashRate);
						curRateRepository.insertRate(sellCash);
					}

					AmxCurRate buyCash = sellTrnsfrRate.clone();
					BigDecimal buyCashRate = ArgUtil.parseAsBigDecimal(tds.get(2).text());
					if (!ArgUtil.isEmpty(buyCashRate)) {
						buyCash.setrType(RType.BUY_CASH);
						sellTrnsfrRate.setrRate(buyCashRate);
						curRateRepository.insertRate(buyCash);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
