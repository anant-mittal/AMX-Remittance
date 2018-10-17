package com.amx.jax.device;

public class CardReader {

	private CardData data = null;
	int totalReaders = 0;
	String[] readers;
	int readerCount = 0;
	long readerTime;
	long dataTime;

	public CardData getData() {
		return data;
	}

	public void setData(CardData data) {
		this.data = data;
	}

	public int getReaderCount() {
		return readerCount;
	}

	public void setReaderCount(int readerCount) {
		this.readerCount = readerCount;
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

	public long getReaderTime() {
		return readerTime;
	}

	public void setReaderTime(long readerTime) {
		this.readerTime = readerTime;
	}

	public long getDataTime() {
		return dataTime;
	}

	public void setDataTime(long dataTime) {
		this.dataTime = dataTime;
	}

}
