package com.amx.jax.adapter.kwt;

public class KWTCardDetails {

	int totalReaders = 0;
	String[] readers;
	int readerCount = 0;
	String readerName = null;

	String civilid;

	public String getCivilid() {
		return civilid;
	}

	public void setCivilid(String civilid) {
		this.civilid = civilid;
	}

	public int getReaderCount() {
		return readerCount;
	}

	public void setReaderCount(int readerCount) {
		this.readerCount = readerCount;
	}

	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}

	public int getTotalReaders() {
		return totalReaders;
	}

	public void setTotalReaders(int totalReaders) {
		this.totalReaders = totalReaders;
	}

	public void setReaders(String[] readers) {
		this.readers = readers;
	}

	public String[] getReaders() {
		return readers;
	}
}
