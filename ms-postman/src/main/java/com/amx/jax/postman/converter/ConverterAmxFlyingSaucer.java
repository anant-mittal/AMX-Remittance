package com.amx.jax.postman.converter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.utils.Constants;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import gui.ava.html.Html2Image;

@Component
public class ConverterAmxFlyingSaucer implements FileConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterAmxFlyingSaucer.class);

	public ConverterAmxFlyingSaucer() {
		// com.itextpdf.licensekey.LicenseKey.loadLicenseFile(getClass().getResourceAsStream("/license/itextkey1520345049511_0.xml"));
	}

	@Override
	public File toPDF(File file) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {

			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {
				ITextRenderer renderer = new ITextRenderer();
				renderer.getFontResolver().addFont("/fonts/all/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				renderer.getFontResolver().addFont("/fonts/all/arialbd.ttf", BaseFont.IDENTITY_H,
						BaseFont.NOT_EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/arialbi.ttf",
				// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/ariali.ttf",
				// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				renderer.getFontResolver().addFont("/fonts/all/arialuni.ttf", BaseFont.IDENTITY_H,
						BaseFont.NOT_EMBEDDED);
				/*
				 * renderer.getFontResolver().addFont("/fonts/all/times.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 * renderer.getFontResolver().addFont("/fonts/all/timesbd.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 * renderer.getFontResolver().addFont("/fonts/all/timesbi.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 * renderer.getFontResolver().addFont("/fonts/all/timesi.ttf",
				 * BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				 */
				// renderer.getFontResolver().addFontDirectory("/fonts/all",
				// BaseFont.NOT_EMBEDDED);

				renderer.getFontResolver().addFont("/fonts/unhinted/NotoNaskhArabic-Regular2.ttf", BaseFont.IDENTITY_H,
						BaseFont.EMBEDDED);
				renderer.getFontResolver().addFont("/fonts/unhinted/NotoSansGurmukhi-Regular.ttf", BaseFont.IDENTITY_H,
						BaseFont.EMBEDDED);

				// renderer.getFontResolver().addFont("/fonts/all/times.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/timesbd.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/timesbi.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				// renderer.getFontResolver().addFont("/fonts/all/timesi.ttf",
				// BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

				renderer.setDocumentFromString(file.getContent());

				Document doc = renderer.getDocument();

				NodeList list = renderer.getDocument().getElementsByTagName("html2img");
				Transformer transformer = TransformerFactory.newInstance().newTransformer();

				for (int i = 0; i < list.getLength(); i++) {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					StringWriter writer = new StringWriter();
					Node node = list.item(i);
					transformer.transform(new DOMSource(node), new StreamResult(writer));
					String html = writer.toString();
					Html2Image img = Html2Image.fromHtml(html);
					BufferedImage ImageFromConvert = img.getImageRenderer().getBufferedImage();
					ImageIO.write(ImageFromConvert, "png", os);

					img.getImageRenderer().saveImage(os, true);
					Element imgtag = doc.createElement("img");
					imgtag.setAttribute("src", "data:image/png;;base64,"
							+ StringUtils.newStringUtf8(Base64.encodeBase64(os.toByteArray(), false)));
					node.replaceChild(imgtag, node.getFirstChild());
				}
				renderer.layout();
				renderer.createPDF(outputStream);
				renderer.finishPDF();
				file.setBody(outputStream.toByteArray());
				file.setType(Type.PNG);
			}
		} catch (DocumentException | IOException | TransformerException e) {
			LOGGER.error("Some Error", e);
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
		return file;
	}
}
