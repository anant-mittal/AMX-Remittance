package com.amx.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
