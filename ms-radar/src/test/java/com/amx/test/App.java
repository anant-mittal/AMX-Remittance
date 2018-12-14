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
					System.out.println(JsonUtil.toJson(trnsfrRate));
				}
			}
		}
	}
}
