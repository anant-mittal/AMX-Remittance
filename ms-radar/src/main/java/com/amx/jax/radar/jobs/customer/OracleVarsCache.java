package com.amx.jax.radar.jobs.customer;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.jax.radar.EsConfig;
import com.amx.utils.ArgUtil;

/**
 * The Class LoggedInUsers.
 */
@Component
public class OracleVarsCache extends CacheBox<String> {

	public static final Long START_TIME = 978287400000L;
	private static final String ASC_SEPERATOR = "-";
	private static final String DESC_SEPERATOR = "-desc-";

	public static final String DOC_VERSION = "v3";
	private static final String CUSTOMER_RESET_COUNTER = "15";
	private static final String TRANSACTION_RESET_COUNTER = "14";

	public static enum DBSyncJobs {
		CUSTOMER("oracle-" + DOC_VERSION + "-customer-v4", 15),
		TRANSACTION("oracle-" + DOC_VERSION + "-tranx-v4", 14),
		XRATE("oracle-" + DOC_VERSION + "-xrate-v4", 15);

		String indexName;
		int resetCounter;

		DBSyncJobs(String indexName, int resetCounter) {
			this.indexName = indexName;
			this.resetCounter = resetCounter;
		}

		public String getIndexName() {
			return indexName;
		}

		public int getResetCounter() {
			return resetCounter;
		}
	}

	/**
	 * Instantiates a new logged in users.
	 */
	public OracleVarsCache() {
		super("OracleVarsCache");
	}

	public String getTranxIndex() {
		return EsConfig.indexName("oracle-" + DOC_VERSION + "-tranx-v4");
	}

	public String getCustomerIndex() {
		return EsConfig.indexName("oracle-" + DOC_VERSION + "-customer-v4");
	}

	public String getIndex(DBSyncJobs job) {
		return EsConfig.indexName(job.getIndexName());
	}

	public Long getCustomerScannedStamp(boolean reverse) {
		if (reverse) {
			return ArgUtil.parseAsLong(this.get(getCustomerIndex() + DESC_SEPERATOR + CUSTOMER_RESET_COUNTER),
					System.currentTimeMillis());
		}
		return ArgUtil.parseAsLong(this.get(getCustomerIndex() + ASC_SEPERATOR + CUSTOMER_RESET_COUNTER), START_TIME);
	}

	public void setCustomerScannedStamp(Long customerScannedStamp, boolean reverse) {
		if (reverse) {
			this.put(getCustomerIndex() + DESC_SEPERATOR + CUSTOMER_RESET_COUNTER,
					ArgUtil.parseAsString(customerScannedStamp));
		} else {
			this.put(getCustomerIndex() + ASC_SEPERATOR + CUSTOMER_RESET_COUNTER,
					ArgUtil.parseAsString(customerScannedStamp));
		}
	}

	public Long getTranxScannedStamp(boolean reverse) {
		if (reverse) {
			return ArgUtil.parseAsLong(this.get(getTranxIndex() + DESC_SEPERATOR + TRANSACTION_RESET_COUNTER),
					System.currentTimeMillis());
		}
		return ArgUtil.parseAsLong(this.get(getTranxIndex() + ASC_SEPERATOR + TRANSACTION_RESET_COUNTER), START_TIME);
	}

	public void setTranxScannedStamp(Long tranxScannedStamp, boolean reverse) {
		if (reverse) {
			this.put(getTranxIndex() + DESC_SEPERATOR + TRANSACTION_RESET_COUNTER,
					ArgUtil.parseAsString(tranxScannedStamp));
		} else {
			this.put(getTranxIndex() + ASC_SEPERATOR + TRANSACTION_RESET_COUNTER,
					ArgUtil.parseAsString(tranxScannedStamp));
		}
	}

	private String getStampStart(DBSyncJobs job) {
		return this.get(getIndex(job) + ASC_SEPERATOR + job.getResetCounter());
	}

	private String getStampEnd(DBSyncJobs job) {
		return this.get(getIndex(job) + DESC_SEPERATOR + job.getResetCounter());
	}

	public Long getStampStartTime(DBSyncJobs job) {
		return ArgUtil.parseAsLong(getStampStart(job), START_TIME);
	}

	public Long getStampEndTime(DBSyncJobs job) {
		return ArgUtil.parseAsLong(getStampEnd(job), System.currentTimeMillis());
	}

	public void setStampStart(DBSyncJobs job, Object jobScannedStamp) {
		this.put(getIndex(job) + ASC_SEPERATOR + job.getResetCounter(),
				ArgUtil.parseAsString(jobScannedStamp));
	}

	public void setStampEnd(DBSyncJobs job, Object jobScannedStamp) {
		this.put(getIndex(job) + DESC_SEPERATOR + job.getResetCounter(),
				ArgUtil.parseAsString(jobScannedStamp));
	}

}
