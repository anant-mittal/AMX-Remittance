package com.amx.jax.jobs.scrapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.rates.AmxCurRateRepository;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
public class BECKuwaitJob {

	@Autowired
	RestService restService;

	@Autowired
	private AmxCurRateRepository curRateRepository;

	XmlMapper xmlMapper = new XmlMapper();

	Logger logger = LoggerService.getLogger(BECKuwaitJob.class);

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_5)
	public void fetchAmanKuwaitModels() {
		try {
			Document doc = Jsoup.connect("https://www.bec.com.kw/currency-exchange-rates?atype=money&continent=popular")
					.get();
			Elements tabs = doc.select(".currency-nav-tab-holder.transfer");
			if (tabs.size() > 0) {
				Elements trs = tabs.get(0).select("tr");
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
