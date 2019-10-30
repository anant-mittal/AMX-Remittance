package com.amx.jax.adapter;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.LoggerService;
import com.amx.utils.CloseUtil;
import com.amx.utils.IoUtils;

@Component
public class SWDocumentScanner {

	private static final Logger LOGGER = LoggerService.getLogger(SWDocumentScanner.class);

	public static SWDocumentScanner CONTEXT = null;

	public Process process = null;
	public static File SCAN_FILE = new File("c:\\temp\\scanimage.jpg");
	public static File SCAN_DIRECTORY = new File("c:\\temp");
	// public static File SCAN_FILE = new
	// File("/Users/lalittanwar/Projects/amx-scanner/test/0.jpg");
	// public static File SCAN_DIRECTORY = new
	// File("/Users/lalittanwar/Projects/amx-scanner/test/");

	public static JLabel SCANNED_IMAGE_LABEL = null;
	public static ImageIcon SCANNED_IMAGE = null;

	public void scan() throws IOException {
		if (process != null) {
			process.destroyForcibly();
			LOGGER.info("Killed :  PlkScan.exe");
		}
		// Runtime.getRuntime().exec("cmd /c scan.bat", null, new
		// File("C:/temp"));

		process = Runtime.getRuntime().exec("cmd /c PlkScan.exe", null,
				new File("C:/ProgramData/Plustek/Software/PlkScan"));

		if (process != null && process.isAlive()) {
			LOGGER.info("Started :  PlkScan.exe");
		}
	}

	public byte[] readImage() throws InterruptedException {
		FileInputStream fin = null;
		try {
			if (SCAN_FILE.exists()) {
				LOGGER.debug("File Exists" + SCAN_FILE.getAbsolutePath());
				int len = 0;
				int lenDiff = 100;
				while (lenDiff > 0 || len == 0) {
					CloseUtil.close(fin);
					fin = new FileInputStream(SCAN_FILE.getAbsolutePath());
					byte[] out1 = IoUtils.toByteArray(fin);
					// System.out.println(out1.length);
					lenDiff = out1.length - len;
					len = out1.length;
					setScannedImage();
					if (lenDiff == 0 && len != 0) {
						return out1;
					}
					TimeUnit.SECONDS.sleep(1);
				}
				LOGGER.error("File Exists But Dont know hwat happedn");
			} else {
				LOGGER.error("File NotExists" + SCAN_FILE.getAbsolutePath());
			}
		} catch (IOException e) {
			LOGGER.error("FileInputStream", e);
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
			} catch (IOException e) {
				LOGGER.debug("FileInputStream", e);
			}
		}
		return null;
	}

	public static void setScannedImage() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(SCAN_FILE);
			Image dimg = img.getScaledInstance(SCANNED_IMAGE_LABEL.getWidth(), SCANNED_IMAGE_LABEL.getHeight(),
					Image.SCALE_SMOOTH);
			SCANNED_IMAGE.setImage(dimg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] documentScan() throws Exception {

		for (int i = 0; i < 5; i++) {
			boolean isScanDocument = false;

			try {

				if (!SWDocumentScanner.SCAN_DIRECTORY.exists()) {
					SWDocumentScanner.SCAN_DIRECTORY.mkdir();
				}

				if (isScanDocument == false) {
					if (SWDocumentScanner.SCAN_FILE.exists()) {
						LOGGER.info("Deleting : " + SWDocumentScanner.SCAN_FILE.getAbsolutePath());
						SWDocumentScanner.SCAN_FILE.delete();
					} else {
						LOGGER.info("Not Deleting : " + SWDocumentScanner.SCAN_FILE.getAbsolutePath());
					}
					scan();

					isScanDocument = true;
					// TimeUnit.SECONDS.sleep(15);

					File f = new File(SWDocumentScanner.SCAN_FILE.getAbsolutePath());
					long startTime = System.currentTimeMillis();
					long end = startTime + 5000;// 10 seconds *1000=10000 // 15 sec *1000 =15000
					while (System.currentTimeMillis() < end) {
						if (f.exists()) {
							break;
						} else {
							TimeUnit.SECONDS.sleep(1);
						}
					}
					long endTime = System.currentTimeMillis();
					LOGGER.info("ofile :" + f);
					LOGGER.info("Total time for Image reading:" + (endTime - startTime) / 1000L);
				}

				if (isScanDocument == true) {
					byte[] x = readImage();
					if(x!=null) {
						return readImage();						
					}

				}
			} catch (Exception e) {
				LOGGER.error("ERROR", e);
			}
		}
		return null;
	}
}
