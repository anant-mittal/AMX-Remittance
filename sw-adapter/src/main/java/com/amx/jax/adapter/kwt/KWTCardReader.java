package com.amx.jax.adapter.kwt;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;

import pacicardlibrary.PACICardAPI;
import pacicardlibrary.PaciException;

public class KWTCardReader {

	public static Logger LOGGER = LoggerFactory.getLogger(KWTCardReader.class);

	public static PACICardAPI API = null;
	private static KWTCardDetails DETAILS = null;

	private static BlockingHashMap<String, KWTCardDetails> MAP = new BlockingHashMap<String, KWTCardDetails>();

	public static KWTCardReaderListner listner = new KWTCardReaderListner();

	KWTCardReader() {
	}

	public static void start() {
		try {
			if (API != null && listner != null) {
				API.RemoveEventListener(listner);
			}
			API = new PACICardAPI(false, 0);
			listner = new KWTCardReaderListner();
			API.AddEventListener(listner);
		} catch (PaciException pe) {
			LOGGER.error("PaciException", pe);
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
	}

	public static KWTCardDetails getDetails() {
		if (DETAILS == null) {
			DETAILS = new KWTCardDetails();
		}
		return DETAILS;
	}

	public static void clearDetails() {
		MAP.remove(KWTCardReader.class.getName());
		DETAILS = null;
	}

	public static void push() {
		MAP.put(KWTCardReader.class.getName(), KWTCardReader.DETAILS);
	}

	public static KWTCardDetails poll() throws InterruptedException {
		return MAP.take(KWTCardDetails.class.getName(), 15, TimeUnit.SECONDS);
	}

}
