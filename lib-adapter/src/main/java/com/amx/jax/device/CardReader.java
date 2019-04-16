package com.amx.jax.device;

import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardReader {

	private CardData data = null;
	int totalReaders = 0;
	String reader;
	long deviceActiveTime;
	long cardActiveTime;

	public boolean hasCard() {
		return !ArgUtil.isEmpty(data) && !data.isEmpty();
	}

	public CardReader() {
		this.cardActiveTime = System.currentTimeMillis();
	}

	public CardData getData() {
		return data;
	}

	public void setData(CardData data) {
		this.data = data;
	}

	public int getTotalReaders() {
		return totalReaders;
	}

	public void setTotalReaders(int totalReaders) {
		this.totalReaders = totalReaders;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getReader() {
		return reader;
	}

	public long getDeviceActiveTime() {
		return deviceActiveTime;
	}

	public void setDeviceActiveTime(long deviceActiveTime) {
		this.deviceActiveTime = deviceActiveTime;
	}

	public long getCardActiveTime() {
		return cardActiveTime;
	}

	public void setCardActiveTime(long cardActiveTime) {
		this.cardActiveTime = cardActiveTime;
	}

}
