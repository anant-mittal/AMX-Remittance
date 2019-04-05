package com.amx.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FileUtil.
 */
public final class FileUtil {

	/** The Constant RESOURCE. */
	private static final String RESOURCE = "resource";

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

	/** The Constant FILE_PREFIX. */
	public static final String FILE_PREFIX = "file://";

	/** The Constant FILE_PREFIX2. */
	public static final String FILE_PREFIX2 = "file:/";

	/** The Constant CLASSPATH_PREFIX. */
	public static final String CLASSPATH_PREFIX = "classpath:";

	/**
	 * Instantiates a new file util.
	 */
	private FileUtil() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	/**
	 * Read file.
	 *
	 * @param filename the filename
	 * @return the string
	 */
	@SuppressWarnings(RESOURCE)
	public static String readFile(String filename) {
		boolean isFilesystem = filename.startsWith(FILE_PREFIX);
		filename = isFilesystem ? filename.substring(FILE_PREFIX.length()) : filename;
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		BufferedReader reader = null;
		try {
			if (isFilesystem) {
				in = new FileInputStream(new File(filename));
			} else {
				in = FileUtil.class.getResourceAsStream(filename);
			}
			if (in == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}

		} catch (IOException e) {
			LOG.error("cannot load " + (isFilesystem ? "filesystem" : "classpath") + " file " + filename, e);
		} finally {
			CloseUtil.close(reader);
			CloseUtil.close(in);
		}
		return sb.toString();
	}

	/**
	 * Write the data into file.
	 *
	 * @param fileLocation Path of file
	 * @param content      Content
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void saveToFile(String fileLocation, String content) throws IOException {
		Writer output = null;
		File file = new File(fileLocation);

		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(content);

		} catch (IOException io) {
			throw io;
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	/**
	 * Save to file.
	 *
	 * @param fileLocation the file location
	 * @param content      the content
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void saveToFile(String fileLocation, byte[] content) throws IOException {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(fileLocation));
			// create an object of BufferedOutputStream
			bos = new BufferedOutputStream(fos);
			bos.write(content);

		} catch (IOException io) {
			throw io;
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static String read(URL url) {
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		BufferedReader reader = null;

		try {
			in = url.openStream();
			if (in == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}

		} catch (IOException e) {
			LOG.error("cannot load  file " + url.toString(), e);
		} finally {
			CloseUtil.close(reader);
			CloseUtil.close(in);
		}
		return sb.toString();
	}

	/**
	 * Can be used to load file inside classpath ie: src/resources.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the resource
	 */
	public static URL getResource(String filePath, Class<?> clazz) {
		boolean isClassPath = filePath.startsWith(CLASSPATH_PREFIX);
		if (isClassPath) {
			return getResource(filePath.substring(CLASSPATH_PREFIX.length()), clazz);
		}
		if (clazz == null) {
			return getResource(filePath);
		}
		URL u = clazz.getClassLoader().getResource(CLASSPATH_PREFIX + filePath);
		if (u != null) {
			return u;
		}

		u = clazz.getClassLoader().getResource(filePath);
		if (u != null) {
			return u;
		}
		return u;
	}

	/**
	 * Gets the resource.
	 *
	 * @param filePath the file path
	 * @return the resource
	 */
	public static URL getResource(String filePath) {
		return getResource(filePath, FileUtil.class);
	}

	/**
	 * Gets the external file.
	 *
	 * @param filePath the file path
	 * @return the external file
	 */
	public static File getExternalFile(String filePath) {
		return getExternalFile(filePath, FileUtil.class);
	}

	/**
	 * Gets the external resource.
	 *
	 * @param filePath the file path
	 * @return the external resource
	 */
	public static URL getExternalResource(String filePath) {
		return getExternalResource(filePath, FileUtil.class);
	}

	/**
	 * Gets the external resource.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the external resource
	 */
	public static URL getExternalResource(String filePath, Class<?> clazz) {
		if (clazz == null) {
			return getExternalResource(filePath);
		}
		// Search in jar folder
		File jarPath = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath().split("!")[0]);
		String propertiesPath = jarPath.getParent();

		URL u = clazz.getClassLoader().getResource(propertiesPath + "/" + filePath);
		if (u != null) {
			return u;
		}

		// Search working folder
		propertiesPath = System.getProperty("user.dir");
		try {
			u = clazz.getClassLoader().getResource(propertiesPath + "/" + filePath);
		} catch (Exception e) {
			LOG.error("clazz.getClassLoader().getResource({}) : {}", propertiesPath + "/" + filePath, e.getMessage());
		}

		if (u != null) {
			return u;
		}

		// Search in target folder
		u = clazz.getClassLoader().getResource(FILE_PREFIX2 + propertiesPath + "/target/" + filePath);
		if (u != null) {
			LOG.info("Step 5 URL:{}", u.getPath());
			return u;
		}

		// Return default
		return null;
	}

	/**
	 * Is used to load file relative to project or jar.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the external file
	 */
	public static File getExternalFile(String filePath, Class<?> clazz) {

		if (clazz == null) {
			return getExternalFile(filePath);
		}

		// Search in jar folder
		File jarPath = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath().split("!")[0]);
		String propertiesPath = jarPath.getParentFile().getPath();

		propertiesPath = propertiesPath.startsWith(FILE_PREFIX2) ? propertiesPath.substring(FILE_PREFIX2.length())
				: propertiesPath;
		propertiesPath = propertiesPath.startsWith(FILE_PREFIX) ? propertiesPath.substring(FILE_PREFIX.length())
				: propertiesPath;
		propertiesPath = "/" + propertiesPath;

		File file = new File(propertiesPath + "/" + filePath);
		if (file.exists()) {
			return file;
		}

		// Search working folder
		propertiesPath = System.getProperty("user.dir");
		file = new File(propertiesPath + "/" + filePath);
		if (file.exists()) {
			return file;
		}

		// Search in target folder
		file = new File(propertiesPath + "/target/" + filePath);
		if (file.exists()) {
			return file;
		}

		// Return default
		return new File(filePath);
	}

	/**
	 * Gets the external resource as stream.
	 *
	 * @param filePath the file path
	 * @return the external resource as stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static InputStream getExternalResourceAsStream(String filePath) throws IOException {
		return getExternalResourceAsStream(filePath, FileUtil.class);
	}

	/**
	 * Gets the external resource as stream.
	 *
	 * @param filePath the file path
	 * @param clazz    the clazz
	 * @return the external resource as stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static InputStream getExternalResourceAsStream(String filePath, Class<?> clazz) throws IOException {
		if (clazz == null) {
			return getExternalResourceAsStream(filePath);
		}
		InputStream in = null;
		URL url = getExternalResource(filePath, clazz);
		if (url != null) {
			return url.openStream();
		}
		File file = getExternalFile(filePath, clazz);
		if (file.isFile()) {
			return new FileInputStream(file);
		}
		return in;
	}
}