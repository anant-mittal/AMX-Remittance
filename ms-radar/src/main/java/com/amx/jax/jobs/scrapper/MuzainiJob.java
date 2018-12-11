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
public class MuzainiJob {

	@Autowired
	RestService restService;

	@Autowired
	private AmxCurRateRepository curRateRepository;

	XmlMapper xmlMapper = new XmlMapper();

	Logger logger = LoggerService.getLogger(MuzainiJob.class);

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_5)
	public void fetchAmanKuwaitModels() {
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
					if (!ArgUtil.isEmpty(rate)) {
						AmxCurRate trnsfrRate = new AmxCurRate();
						trnsfrRate.setrSrc(RSource.MUZAINI);
						trnsfrRate.setrDomCur(RCur.KWD);
						trnsfrRate.setrForCur(cur);
						trnsfrRate.setrType(RType.SELL_TRNSFR);
						trnsfrRate.setrRate(rate);
						curRateRepository.insertRate(trnsfrRate);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
