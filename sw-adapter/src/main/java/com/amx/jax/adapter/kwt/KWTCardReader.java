package com.amx.jax.adapter.kwt;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;

import com.amx.jax.adapter.SWAdapterLauncher;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;

import pacicardlibrary.PACICardAPI;
import pacicardlibrary.PaciException;

public class KWTCardReader extends CardReader {

	public static Logger LOGGER = LoggerFactory.getLogger(KWTCardReader.class);
	private static KWTCardReader READER = new KWTCardReader();
	public static PACICardAPI API = null;
	private static BlockingHashMap<String, CardData> MAP = new BlockingHashMap<String, CardData>();
	public static KWTCardReaderListner LISTNER = new KWTCardReaderListner();
	public static String CARD_READER_KEY = CardData.class.getName();

	KWTCardReader() {
	}

	public static void start() {
		try {
			if (API != null && LISTNER != null) {
				API.RemoveEventListener(LISTNER);
			}
			API = new PACICardAPI(false, 0);
			LISTNER = new KWTCardReaderListner();
			API.AddEventListener(LISTNER);
		} catch (PaciException pe) {
			LOGGER.error("AMX --- PaciException", pe);
		} catch (Exception e) {
			LOGGER.error("AMX --- Exception", e);
		}
	}

	public static CardData readerData() {
		if (READER.getData() == null) {
			READER.setData(new CardData());
		}
		return READER.getData();
	}

	public static void info(String[] readers) {
		READER.setReaders(readers);
		READER.setReaderCount(readers.length);
		READER.setReaderTime(System.currentTimeMillis());
	}

	public static void clear() {
		SWAdapterLauncher.GUI.log("Clearing...");
		MAP.remove(CARD_READER_KEY);
		READER.setData(null);
		READER.setDataTime(System.currentTimeMillis());
	}

	public static void push() {
		if (READER.getData() != null && READER.getData().isValid()) {
			READER.setDataTime(READER.getData().getTimestamp());
			MAP.put(CARD_READER_KEY, READER.getData());
		}
	}

	public static CardData poll() throws InterruptedException {
		return MAP.take(CARD_READER_KEY, 10, TimeUnit.SECONDS);
	}

	public static KWTCardReader read() throws InterruptedException {
		SWAdapterLauncher.GUI.log("Reading...");
		// start();
		CardData data = poll();
		if (data != null && data.getTimestamp() >= READER.getDataTime()) {
			SWAdapterLauncher.GUI.log("No Data Found");
		}
		push();
		return READER;
	}

	public static boolean ping() {
		if (API == null) {
			return false;
		}
		return API.GetReaders().length > 0;
	}

}
