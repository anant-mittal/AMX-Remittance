package com.amx.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.util.StreamUtils;

/**
 * The Class IoUtils.
 */
public class IoUtils {

	/** The Constant BUFFER_SIZE. */
	private static final int BUFFER_SIZE = 1024;

	/**
	 * To byte array.
	 *
	 * @param is the is
	 * @return the byte[]
	 */
	public static byte[] toByteArray(InputStream is) {
		BufferedInputStream bis = new BufferedInputStream(is);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int readBytes;
		try {
			while ((readBytes = bis.read(buffer)) > 0) {
				baos.write(buffer, 0, readBytes);
			}
			return baos.toByteArray();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Inputstream to string.
	 *
	 * @param in the in
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String inputstream_to_string(InputStream in) throws IOException {
		Reader reader = new InputStreamReader(in);
		StringWriter writer = new StringWriter();
		char[] buf = new char[1000];
		while (true) {
			int n = reader.read(buf, 0, 1000);
			if (n == -1) {
				break;
			}
			writer.write(buf, 0, n);
		}
		return writer.toString();
	}

	public static class StreamWrapper {
		InputStream in;
		private byte[] body;

		public StreamWrapper(InputStream in) {
			this.in = in;
		}

		public InputStream toStream() throws IOException {
			if (this.body == null) {
				this.body = StreamUtils.copyToByteArray(in);
			}
			return new ByteArrayInputStream(this.body);
		}
	}

	/**
	 * The Class DrainableOutputStream.
	 */
	public class DrainableOutputStream extends FilterOutputStream {

		/** The buffer. */
		private final ByteArrayOutputStream buffer;

		/**
		 * Instantiates a new drainable output stream.
		 *
		 * @param out the out
		 */
		public DrainableOutputStream(OutputStream out) {
			super(out);
			this.buffer = new ByteArrayOutputStream();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FilterOutputStream#write(byte[])
		 */
		@Override
		public void write(byte b[]) throws IOException {
			this.buffer.write(b);
			super.write(b);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FilterOutputStream#write(byte[], int, int)
		 */
		@Override
		public void write(byte b[], int off, int len) throws IOException {
			this.buffer.write(b, off, len);
			super.write(b, off, len);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.io.FilterOutputStream#write(int)
		 */
		@Override
		public void write(int b) throws IOException {
			this.buffer.write(b);
			super.write(b);
		}

		/**
		 * To byte array.
		 *
		 * @return the byte[]
		 */
		public byte[] toByteArray() {
			return this.buffer.toByteArray();
		}
	}

	public static java.sql.Clob stringToClob(String source) throws SerialException, SQLException {
		return new javax.sql.rowset.serial.SerialClob(source.toCharArray());
	}

}
