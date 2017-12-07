package com.amx.jax.postman.api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amx.jax.postman.Email;
import com.lowagie.text.DocumentException;

@RestController
public class PostManController {

	@Autowired
	private PostManService postManService;
	
	
	private Logger logger = Logger.getLogger(PostManController.class);

	@RequestMapping(value = "/postman/email", method = RequestMethod.GET)
	public Email sendEmail(@RequestParam(required = false) Boolean html,
			HttpServletResponse resp) throws IOException, DocumentException {

		String from = "lalit.tanwar.almulla@gmail.com";
		String to = "lalit.tanwar07@gmail.com";

		String subject = "Java Mail with Spring Boot";

		Email email = new Email(from, to, subject);

		email.getModel().put("user", "Pavan");
		email.getModel().put("today", String.valueOf(new Date()));

		if (html) {
			email.setTemplate("hello-world");
			email.setHtml(true);
		} else {
			email.setTemplate("hello-world-plain");
		}

		postManService.sendEmail(email);

		FileOutputStream os = null;
		String fileName = UUID.randomUUID().toString();
		
		
		resp.setHeader("Cache-Control","cache, must-revalidate");
		resp.addHeader("Content-disposition","attachment; filename=RemittanceReceiptReport.pdf");

		//OutputStream outputStream = resp.getOutputStream();​
		//https://tuhrig.de/generating-pdfs-with-java-flying-saucer-and-thymeleaf/
		
		try {
			// final File outputFile = File.createTempFile(fileName, ".pdf");
			// os = new FileOutputStream(outputFile);
			OutputStream outputStream = resp.getOutputStream();
			//OutputStream outputStream = new FileOutputStream("message.pdf");
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(email.getMessage());
			renderer.layout();
			renderer.createPDF(outputStream);
			outputStream.close();

			System.out.println("PDF created successfully");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}

		return email;
	}

}
