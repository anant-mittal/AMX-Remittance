package com.amx.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;

public class IoUtils {
	private static final int BUFFER_SIZE = 1024;

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

	public class DrainableOutputStream extends FilterOutputStream {
		private final ByteArrayOutputStream buffer;

		public DrainableOutputStream(OutputStream out) {
			super(out);
			this.buffer = new ByteArrayOutputStream();
		}

		@Override
		public void write(byte b[]) throws IOException {
			this.buffer.write(b);
			super.write(b);
		}

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			this.buffer.write(b, off, len);
			super.write(b, off, len);
		}

		@Override
		public void write(int b) throws IOException {
			this.buffer.write(b);
			super.write(b);
		}

		public byte[] toByteArray() {
			return this.buffer.toByteArray();
		}
	}

}
