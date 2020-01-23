package com.amx.jax.postman.model;

import java.util.ArrayList;
import java.util.List;

public class MessageBox {

	private List<Email> emailBucket;
	private List<SMS> smsBucket;
	private List<WAMessage> waBucket;
	private List<TGMessage> tgBucket;
	private List<PushMessage> pushBucket;

	public MessageBox() {
		this.emailBucket = new ArrayList<Email>();
		this.smsBucket = new ArrayList<SMS>();
		this.waBucket = new ArrayList<WAMessage>();
		this.tgBucket = new ArrayList<TGMessage>();
		this.pushBucket = new ArrayList<PushMessage>();
	}

	public List<Email> getEmailBucket() {
		return emailBucket;
	}

	public void setEmailBucket(List<Email> emailBucket) {
		this.emailBucket = emailBucket;
	}

	public List<SMS> getSmsBucket() {
		return smsBucket;
	}

	public void setSmsBucket(List<SMS> smsBucket) {
		this.smsBucket = smsBucket;
	}

	public List<WAMessage> getWaBucket() {
		return waBucket;
	}

	public void setWaBucket(List<WAMessage> waBucket) {
		this.waBucket = waBucket;
	}

	public List<PushMessage> getPushBucket() {
		return pushBucket;
	}

	public void setPushBucket(List<PushMessage> pushBucket) {
		this.pushBucket = pushBucket;
	}

	public MessageBox push(Email m) {
		this.emailBucket.add(m);
		return this;
	}

	public MessageBox push(SMS m) {
		this.smsBucket.add(m);
		return this;
	}

	public MessageBox push(WAMessage m) {
		this.waBucket.add(m);
		return this;
	}

	public MessageBox push(TGMessage m) {
		this.tgBucket.add(m);
		return this;
	}

	public MessageBox push(PushMessage m) {
		this.pushBucket.add(m);
		return this;
	}

	public List<TGMessage> getTgBucket() {
		return tgBucket;
	}

	public void setTgBucket(List<TGMessage> tgBucket) {
		this.tgBucket = tgBucket;
	}
}
