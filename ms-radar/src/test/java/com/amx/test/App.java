package com.amx.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.amx.jax.client.snap.ISnapService.RateSource;
import com.amx.jax.client.snap.ISnapService.RateType;
import com.amx.jax.dict.Currency;
import com.amx.jax.radar.jobs.scrapper.AmanKuwaitModels;
import com.amx.jax.rates.AmxCurRate;
import com.amx.utils.ArgUtil;
import com.amx.utils.FileUtil;
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
		String json = FileUtil
				.readFile("file://" + System.getProperty("user.dir")
						+ "/src/test/java/com/amx/test/amankuwaitratesample.json");
		AmanKuwaitModels.Rates rates2 = JsonUtil.getMapper().readValue(json, AmanKuwaitModels.RatesJson.class);
		rates2.getCurRates();
	}

	public static void main2(String[] args) throws URISyntaxException, IOException {
		System.out.println("Strat ============");
		Document doc0 = Jsoup.connect("https://www.uaeexchange.com.kw/Rates.aspx").get();

		Connection con1 = Jsoup.connect("https://www.uaeexchange.com.kw/Rates.aspx")
				.referrer("https://www.uaeexchange.com.kw/Rates.aspx");
		Elements inputs1 = doc0.select("input");
		for (Element input : inputs1) {
			String key = input.attr("name");
			if (!"ctl00$ctl10$anchorForex".equalsIgnoreCase(key)) {
				con1 = con1.data(key, input.val());
			}
		}
		con1 = con1.data("ctl00$ScriptManager1", "ctl00$ctl10$updatepnl|ctl00$ctl10$ahrefExchange")
				.data("ctl00$ctl10$ahrefExchange", "Exchange Rates");

		Document doc1 = con1.post();
		printUAERates(doc1, RateType.SELL_TRNSFR);

		Connection con2 = Jsoup.connect("https://www.uaeexchange.com.kw/Rates.aspx")
				.referrer("https://www.uaeexchange.com.kw/Rates.aspx");

		Elements inputs2 = doc0.select("input");
		for (Element input : inputs2) {
			String key = input.attr("name");
			if (!"ctl00$ctl10$ahrefExchange".equalsIgnoreCase(key)) {
				con2 = con2.data(key, input.val());
			}
		}
		con2 = con2.data("ctl00$ScriptManager1", "ctl00$ctl10$updatepnl|ctl00$ctl10$anchorForex")
				.data("ctl00$ctl10$anchorForex", "Forex Rates");

		System.out.println("RAte ============");
		Document doc2 = con2.post();
		printUAERates(doc2, RateType.SELL_CASH);
		System.out.println("Ends ============");
	}

	public static void printUAERates(Document doc, RateType type) {
		Elements trs = doc.select("#ctl10_updatepnl table.table tbody tr");
		for (Element tr : trs) {
			Elements tds = tr.select("td");
			Currency cur = (Currency) ArgUtil.parseAsEnum(tds.get(2).text(),
					Currency.UNKNOWN);
			if (!Currency.UNKNOWN.equals(cur) && tds.size() >= 3) {
				BigDecimal rate = ArgUtil.parseAsBigDecimal(tds.get(3).text());
				if (!ArgUtil.isEmpty(rate)) {
					AmxCurRate trnsfrRate = new AmxCurRate();
					trnsfrRate.setrSrc(RateSource.MUZAINI);
					trnsfrRate.setrDomCur(Currency.KWD);
					trnsfrRate.setrForCur(cur);
					trnsfrRate.setrType(type);
					trnsfrRate.setrRate(rate);
					System.out.println(JsonUtil.toJson(trnsfrRate));
				}
			}
		}
	}

}
