package com.amx.jax.postman.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

//import org.apache.fop.apps.FOPException;
//import org.apache.fop.apps.FOUserAgent;
//import org.apache.fop.apps.Fop;
//import org.apache.fop.apps.FopFactory;
//import org.apache.fop.apps.FopFactoryBuilder;
//import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.utils.Constants;

@Component
public class ConverterFOP implements FileConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterFOP.class);

	@Override
	public File toPDF(File file) {
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		try {
//
//			// LicenseKey.loadLicenseFile("/license/itextkey1520345049511_0.xml");
//			if (file.getContent() != null && !Constants.BLANK.equals(file.getContent())) {
//
//				StringReader reader = new StringReader(file.getContent());
//				Source sourceTemplate = new StreamSource(reader);
//
//				Source xmlSource = new StreamSource();
//
//				// version 1.0 of getting factory
//				// final FopFactory fopFactory = FopFactory.newInstance();
//
//				// version 2.1 of getting factory
//				FopFactoryBuilder builder = new FopFactoryBuilder(new URI("http://google.com"));
//				builder.setSourceResolution(96);
//				FopFactory fopFactory = builder.build();
//
//				FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
//				Fop fop;
//				fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputStream);
//
//				TransformerFactory factory = TransformerFactory.newInstance();
//				Transformer transformer = factory.newTransformer(sourceTemplate);
//
//				Result result = new SAXResult(fop.getDefaultHandler());
//				transformer.transform(xmlSource, result);
//
//				outputStream.flush();
//
//				file.setBody(outputStream.toByteArray());
//				file.setType(Type.PDF);
//				LOGGER.info("Created");
//
//			}
//		} catch (IOException | FOPException | URISyntaxException | TransformerException e) {
//			LOGGER.error("Some Error", e);
//		} finally {
//			if (outputStream != null) {
//				try {
//					outputStream.close();
//				} catch (IOException e) {
//					/* ignore */
//				}
//			}
//		}
		return file;
	}

}
