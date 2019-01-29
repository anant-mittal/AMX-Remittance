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

import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.ARadarTask;
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

import net.javacrumbs.shedlock.core.SchedulerLock;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class BECKuwaitJob extends ARadarTask {

	@Autowired
	RestService restService;

	@Autowired
	private AmxCurRateRepository curRateRepository;

	XmlMapper xmlMapper = new XmlMapper();

	public static final Logger LOGGER = LoggerService.getLogger(BECKuwaitJob.class);

	@SchedulerLock(name = "BECKuwaitJob",
			lockAtLeastFor = AmxCurConstants.INTERVAL_MIN_30,
			lockAtMostFor = AmxCurConstants.INTERVAL_HRS)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void doTask() {

		LOGGER.info("Scrapper Task");

		try {
			Document doc = Jsoup.connect("https://www.bec.com.kw/currency-exchange-rates?atype=money&continent=popular")
					.get();
			Elements transferTabs = doc.select(".currency-nav-tab-holder.transfer");
			if (transferTabs.size() > 0) {
				Elements trs = transferTabs.get(0).select("tr");
				for (Element tr : trs) {
					AmxCurConstants.RCur cur = (RCur) ArgUtil.parseAsEnum(tr.select(".bfc-country-code").text(),
							AmxCurConstants.RCur.UNKNOWN);
					if (!AmxCurConstants.RCur.UNKNOWN.equals(cur)) {
						BigDecimal rate = ArgUtil.parseAsBigDecimal(tr.select(".tg-buy .bfc-currency-rates").text());
						if (!ArgUtil.isEmpty(rate)) {
							AmxCurRate trnsfrRate = new AmxCurRate();
							trnsfrRate.setrSrc(RSource.BECKWT);
							trnsfrRate.setrDomCur(RCur.KWD);
							trnsfrRate.setrForCur(cur);
							trnsfrRate.setrType(RType.SELL_TRNSFR);
							trnsfrRate.setrRate(BigDecimal.ONE.divide(rate, 12, RoundingMode.CEILING));
							curRateRepository.insertRate(trnsfrRate);
						}
					}
				}
			}

			Elements xTabs = doc.select(".currency-nav-tab-holder.exchange");
			if (xTabs.size() > 0) {
				Elements trs = xTabs.get(0).select("tr");
				for (Element tr : trs) {
					AmxCurConstants.RCur cur = (RCur) ArgUtil.parseAsEnum(tr.select(".bfc-country-code").text(),
							AmxCurConstants.RCur.UNKNOWN);
					if (!AmxCurConstants.RCur.UNKNOWN.equals(cur)) {

						AmxCurRate buyCash = new AmxCurRate(RSource.BECKWT, RCur.KWD, cur);

						BigDecimal buyCashRate = ArgUtil
								.parseAsBigDecimal(tr.select(".tg-buy .bfc-currency-rates").text());
						if (!ArgUtil.isEmpty(buyCashRate)) {
							buyCash.setrType(RType.BUY_CASH);
							buyCash.setrRate(BigDecimal.ONE.divide(buyCashRate, 12, RoundingMode.CEILING));
							curRateRepository.insertRate(buyCash);
						}

						AmxCurRate sellCash = buyCash.clone();
						BigDecimal sellCashRate = ArgUtil
								.parseAsBigDecimal(tr.select(".tg-buy .bfc-currency-rates").text());
						if (!ArgUtil.isEmpty(sellCashRate)) {
							sellCash.setrType(RType.SELL_CASH);
							sellCash.setrRate(BigDecimal.ONE.divide(sellCashRate, 12, RoundingMode.CEILING));
							curRateRepository.insertRate(sellCash);
						}

					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
