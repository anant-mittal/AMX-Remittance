package com.amx.jax.adapter.kwt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pacicardlibrary.PACICardAPI;
import pacicardlibrary.PaciEventHandler;
import pacicardlibrary.PaciException;

public class KWTCardReaderListner implements PaciEventHandler {

	public static boolean connected = false;

	public static Logger LOGGER = LoggerFactory.getLogger(KWTCardReaderListner.class);

	@SuppressWarnings("deprecation")
	@Override
	public void CardConnectionEvent(int readerIndex) {
		connected = true;
		System.out.println("\n**** KwCardReaderListner:PaciException" + readerIndex);
		LOGGER.info("KwCardReaderListner:CardConnectionEvent {}", readerIndex);

		try {
			LOGGER.info("Latin Nationality {}", KWTCardReader.API.getNationalty_Latin_Text(readerIndex, true));
			LOGGER.info("English Name {}", KWTCardReader.API.getEnglish_Name(readerIndex, true));
			LOGGER.info("Arabic Name {}", KWTCardReader.API.getArabic_Name(readerIndex, true));
			LOGGER.info("DOB {}", KWTCardReader.API.getBirth_Date(readerIndex, true));
			LOGGER.info("Blood {}", KWTCardReader.API.getBlood_Type(readerIndex, true));
			LOGGER.info("Nationality Arabic {}", KWTCardReader.API.getNationalty_Arabic_Text(readerIndex, true));
			LOGGER.info("Latin Arabic {}", KWTCardReader.API.getSex_Latin_Text(readerIndex, true));
			LOGGER.info("Exp Date {}",
					KWTCardReader.API.ReadCardInfo(readerIndex, PACICardAPI.DataType.CardExpiryDate, true));

			LOGGER.info("Full Arabic Name {}",
					KWTCardReader.API.ReadCardInfo(readerIndex, PACICardAPI.DataType.FullArabicName, true));

			LOGGER.info("Full Eng Name {}",
					KWTCardReader.API.ReadCardInfo(readerIndex, PACICardAPI.DataType.FullEnglishName, true));

			X509Certificate cert = KWTCardReader.API.GetCertificate(readerIndex);

			LOGGER.info("Cert Sunj Name {}", cert.getSubjectDN());

			LOGGER.info("is Car Valid {}", KWTCardReader.API.ValidateCardIsGenuine(readerIndex));

			LOGGER.info("is Car  Cert Valid {}",
					KWTCardReader.API.ValidateCardCertificate(readerIndex, false, true, true));

			// Reading Photo
			byte[] image = KWTCardReader.API.ReadPhoto(readerIndex);
			String civilid = KWTCardReader.API.getCivil_ID(0, true);

			try {
				Files.write(Paths.get("civil_photos/" + civilid + ".jp2"), image);
			} catch (IOException e) {
				LOGGER.error("is Car  Cert Valid {}", e);
			}

			KWTCardReader.getDetails().setCivilid(civilid);
			KWTCardReader.push();

		} catch (PaciException e) {
			// e.printStackTrace();
			System.out.println("KwCardReaderListner:PaciException" + e.getMessage());
		} catch (Exception e2) {
			System.out.println("KwCardReaderListner:Exception " + e2.getMessage());
		}
	}

	@Override
	public void CardDisconnectionEvent(int arg0) {
		System.out.println("\n**** KwCardReaderListner:CardDisconnectionEvent " + arg0);
		connected = false;
		KWTCardReader.clearDetails();
	}

	@Override
	public void ReaderChangeEvent() {
		System.out.println("\n**** KwCardReaderListner:ReaderChangeEvent");
		KWTCardReader.getDetails().setTotalReaders(KWTCardReader.API.GetNumberOfReaders());
		KWTCardReader.getDetails().setReaders(KWTCardReader.API.GetReaders());
	}

}
