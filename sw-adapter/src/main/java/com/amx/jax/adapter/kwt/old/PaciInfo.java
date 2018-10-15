package com.amx.jax.adapter.kwt.old;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.adapter.kwt.KWTCardReaderListner;

import pacicardlibrary.PACICardAPI;
import pacicardlibrary.PaciEventHandler;
import pacicardlibrary.PaciException;

public abstract class PaciInfo implements PaciEventHandler {
	public static PACICardAPI paciApi;
	public static boolean isCall = false;

	public static Logger LOGGER = LoggerFactory.getLogger(KWTCardReaderListner.class);

	public static void mainNo(String[] args) throws PaciException {

		try {
			String java_ver = System.getProperty("java.version");

			System.out.println("JAVA VERSION :" + java_ver);
			long startTime = System.currentTimeMillis();
			paciApi = new PACICardAPI(false, 0);
			PaciEventImplementationNew events = new PaciEventImplementationNew();
			paciApi.AddEventListener(events);
			Thread.sleep(20000);
			long endTime = System.currentTimeMillis();
			System.out.println((new StringBuilder("Total time in for reading all details seconds:"))
					.append((endTime - startTime) / 1000L).toString());

			System.out.println("PaciInformation Method :" + events.getPaciInformation());

		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("===", ex);
			System.out.println("Message :" + ex.getMessage());
		} finally { //
			System.exit(1);
		}

	}

	public static String callData() throws PaciException, InterruptedException {

		String customerDetails = null;

		String java_ver = System.getProperty("java.version");

		System.out.println("JAVA VERSION :" + java_ver);
		long startTime = System.currentTimeMillis();
		paciApi = new PACICardAPI(false, 0);

		PaciEventImplementationNew events = new PaciEventImplementationNew();
		events.readDataFromCard();
		paciApi.AddEventListener(events);
		// Thread.sleep(10000);
		long end = startTime + 15000;// 10 seconds *1000=10000 // 15 sec *1000 =15000
		while (System.currentTimeMillis() < end) {
			System.out.println("while Loop :" + System.currentTimeMillis() + "\t end time :" + end);
			if (events.getPaciInformation() != null) {
				break;
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Paci Information Method :" + events.getPaciInformation());

		System.out.println(
				"Total time in for reading all details seconds PaciInfo class:" + (endTime - startTime) / 1000L);
		return events.getPaciInformation();

	}
}
