package com.amx.jax.radar.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.LoggerService;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Component
public class CivilIdValidationService {

	private static final String UAE_XCHANGE_URL = "https://www.e.gov.kw/sites/kgoEnglish/Pages/eServices/PACI/CivilIDStatus.aspx";

	private static final String PARAM_NAME = "name";
	private static final String TAG_INPUT = "input";

	XmlMapper xmlMapper = new XmlMapper();

	CaptchaSolver captchaSolver = new CaptchaSolver("tessdata0");

	public static final Logger LOGGER = LoggerService.getLogger(CivilIdValidationService.class);

	public void validate(String identity) throws IOException {

		Document doc0 = Jsoup.connect(UAE_XCHANGE_URL).get();

		Connection con1 = Jsoup.connect(UAE_XCHANGE_URL)
				.referrer(UAE_XCHANGE_URL);

		Elements inputs1 = doc0.select(TAG_INPUT);
		for (Element input : inputs1) {
			String key = input.attr(PARAM_NAME);
			if (!ArgUtil.isEmpty(key)) {
				if (key.endsWith("txtCivilID")) {
					con1 = con1.data(key, identity);
				} else {
					con1 = con1.data(key, input.val());
				}
			}

		}

		Document doc1 = con1.post();

		Elements trs = doc1.select(".section.group .alert .labelText");
		for (Element divs : trs) {
			String key = divs.id();
			if (!ArgUtil.isEmpty(key) && key.endsWith("_lblResult")) {
				LOGGER.info("{} ==  {}", key, divs.text());
			}

		}
	}

	private static final String PACI_URL = "https://www.paci.gov.kw/Default.aspx";

	public void validateCaptcha(String identity) throws IOException {

		Connection con0 = Jsoup.connect(PACI_URL)
				.header("Accept-Encoding", "gzip, deflate")
				.userAgent(
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
				// .referrer(PACI_URL)
				.cookie("ASP.NET_SessionId", "001nvimnicv0j32oz5cjejzk")
				.cookie("PaciSite", "en")
				.cookie("TermsAndConditions", "1")
				.maxBodySize(0)
				.timeout(600000);
		Connection.Response response = con0.execute();

		Document doc0 = response.parse();

		// Image Reading Start
		Elements img = doc0.select("#ContentPlaceHolder1_MyCaptchaBot_ImgCaptcha");
		String imageSrc = img.attr("src");
		Connection.Response resultImageResponse = Jsoup.connect("https://www.paci.gov.kw/" + imageSrc)
				.cookies(response.cookies())
				.ignoreContentType(true)
				.method(Connection.Method.GET).timeout(30000).execute();

		File temp = File.createTempFile(identity, ".jpg");
		FileOutputStream out = (new FileOutputStream(temp));
		out.write(resultImageResponse.bodyAsBytes());
		out.close();
		LOGGER.info("Captch form {} is {}", temp.getAbsolutePath(),
				captchaSolver.solve(temp.getAbsolutePath()));
		// Image Reading End

		Connection con1 = con0
				.referrer(PACI_URL);

		Elements inputs1 = doc0.select(TAG_INPUT);
		for (Element input : inputs1) {
			String key = input.attr(PARAM_NAME);
			if (!ArgUtil.isEmpty(key)) {
				if (key.endsWith("CivilNoTxt")) {
					con1 = con1.data(key, identity);
				} else {
					con1 = con1.data(key, input.val());
				}
				// LOGGER.info("INPUT {} {} ", key,input.val() );
			}

		}

		con1.cookie("PaciSite", "en");
		con1.cookie("TermsAndConditions", "1");
		con1.cookies(response.cookies());

		Document doc1 = con1.post();

		Elements trs = doc1.select("#ContentPlaceHolder1_alrtDiv #ContentPlaceHolder1_spanMsg");
		for (Element divs : trs) {
			LOGGER.info("Result+ {}", divs.text());
		}
	}
}
