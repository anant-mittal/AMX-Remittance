package com.amx.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.amx.jax.rates.AmxCurRate;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	public static XmlMapper xmlMapper = new XmlMapper();

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		Matcher match = pattern.matcher("${app.prod}");

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
						System.out.println(JsonUtil.toJson(trnsfrRate));
					}
				}

			}
		}
	}
}
