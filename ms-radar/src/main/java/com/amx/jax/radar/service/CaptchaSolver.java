package com.amx.jax.radar.service;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_OTSU;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_RECT;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.erode;
import static org.bytedeco.javacpp.opencv_imgproc.getStructuringElement;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;
import static org.bytedeco.javacpp.opencv_photo.fastNlMeansDenoising;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.FileUtil;

/**
 * Describe class <code>CaptchaSolver</code> here.
 *
 * @author <a href="mailto:coldnew.tw@gmail.com">Yen-Chin, Lee</a>
 * @version 1.0
 */
public class CaptchaSolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaSolver.class);
	private TessBaseAPI tess;
	public static CaptchaSolver INSTANCE = new CaptchaSolver("tessdata0");

	/**
	 * Creates a new <code>CaptchaSolver</code> instance.
	 *
	 */
	public CaptchaSolver(String tessdataPath, String lang, boolean oem) {
		// use builtin resources
		if (tess == null) {
			try {
				File traindata = FileUtil.getExternalFile("ext-resources/" + tessdataPath);
				LOGGER.info("Image === {}", traindata.getPath());
				tess = new TessBaseAPI();
				if (oem) {
					if (tess.Init(traindata.getPath(), lang, 1) != 0) {
						throw new IllegalStateException("Could not initialize tesseract from ");
					}
				} else {
					if (tess.Init(traindata.getPath(), lang) != 0) {
						throw new IllegalStateException("Could not initialize tesseract from ");
					}
				}

			} catch (Exception e) {
				tess = null;
				LOGGER.error("Could not initialize tesseract", e);
			}
		}
	}

	public CaptchaSolver(String tessdataPath) {
		this(tessdataPath, "eng", false);
	}

	public CaptchaSolver(String tessdataPath, String lang) {
		this(tessdataPath, lang, false);
	}

	/**
	 * Describe <code>finalize</code> method here.
	 *
	 */
	protected void finalize() {
		tess.End();
	}

	private Mat clean_captcha(String file) {

		// Load captcha captcha in grayscale
		Mat captcha = imread(file, IMREAD_GRAYSCALE);
		if (captcha.empty()) {
			System.out.println("Can't read captcha image '" + file + "'");
			return captcha;
		}

		// Convert the captcha to black and white.
		Mat captcha_bw = new Mat();
		threshold(captcha, captcha_bw, 128, 255, THRESH_BINARY | CV_THRESH_OTSU);

		// Erode the image to remove dot noise and that wierd line. I use a 3x3
		// rectengal as the kernal.
		Mat captcha_erode = new Mat();
		Mat element = getStructuringElement(MORPH_RECT, new Size(3, 3));
		erode(captcha_bw, captcha_erode, element);

		// Some cosmetic
		Mat captcha_denoise = new Mat();
		fastNlMeansDenoising(captcha_erode, captcha_denoise, 7, 7, 21);

		return captcha_denoise;
	}

	private String image_to_string(Mat img) {
		if (ArgUtil.isEmpty(tess)) {
			return Constants.BLANK;
		}

		BytePointer outText;

		tess.SetImage(img.data(), img.cols(), img.rows(), 1, img.cols());

		outText = tess.GetUTF8Text();
		String s = outText.getString();

		// Destroy used object and release memory
		outText.deallocate();

		return s.replaceAll("[^0-9-A-Z]", "");
	}

	/**
	 * Describe <code>solve</code> method here.
	 *
	 * @param file a <code>String</code> value
	 * @return a <code>String</code> value
	 */
	public String solve(String file) {
		return image_to_string(clean_captcha(file));
	}

	public static CaptchaSolver captchaSolver = new CaptchaSolver("tessdata0");
	public static CaptchaSolver captchaSolverEqu = new CaptchaSolver("tessdata0", "digits", true);
	public static CaptchaSolver captchaSolver1 = new CaptchaSolver("tessdata1");
	public static CaptchaSolver captchaSolver2 = new CaptchaSolver("tessdata2");
	public static CaptchaSolver captchaSolver3 = new CaptchaSolver("tessdata3", "engmorse", true);

	public static void maintest(String[] args) throws URISyntaxException, IOException {
		LOGGER.info(captchaSolver.solve("/Users/lalittanwar/Projects/workdoc/captcha/identity1426918192544998208.jpg"));
		LOGGER.info(
				captchaSolverEqu.solve("/Users/lalittanwar/Projects/workdoc/captcha/identity1426918192544998208.jpg"));
		LOGGER.info(
				captchaSolver1.solve("/Users/lalittanwar/Projects/workdoc/captcha/identity1426918192544998208.jpg"));
		LOGGER.info(
				captchaSolver2.solve("/Users/lalittanwar/Projects/workdoc/captcha/identity1426918192544998208.jpg"));

		LOGGER.info(
				captchaSolver3.solve("/Users/lalittanwar/Projects/workdoc/captcha/identity1426918192544998208.jpg"));
	}
}