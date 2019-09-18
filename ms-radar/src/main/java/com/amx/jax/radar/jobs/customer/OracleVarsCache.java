package com.amx.jax.radar.jobs.customer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants;
import com.amx.jax.radar.AMXSharedValues;
import com.amx.jax.radar.EsConfig;
import com.amx.utils.ArgUtil;

/**
 * The Class LoggedInUsers.
 */
@Component
public class OracleVarsCache {

	private Map<String, String> fallback = Collections.synchronizedMap(new HashMap<String, String>());

	public static final Long START_TIME = 978287400000L;
	private static final String ASC_SEPERATOR = "-";
	private static final String DESC_SEPERATOR = "-desc-";

	public static final String DOC_VERSION = SnapConstants.DOC_VERSION;

	public static enum DBSyncJobs {
		CUSTOMER_JOB(SnapConstants.SnapIndexName.CUSTOMER, "v5", 20),
		TRANSACTION_JOB(SnapConstants.SnapIndexName.TRANX, "v8", 22),
		XRATE_JOB(SnapConstants.SnapIndexName.XRATE, "v5", 21);

		String indexName;
		int resetCounter;

		/**
		 * 
		 * @param indexName
		 * @param indexVersion - Change this if doc structure is changed and job is to
		 *                     be restarted again
		 * @param resetCounter - Change this if job is to be restarted again
		 */
		DBSyncJobs(String indexName, String indexVersion, int resetCounter) {
			this.indexName = String.format("%s-%s-%s-%s", SnapConstants.ORACLE, SnapConstants.DOC_VERSION, indexName,
					indexVersion);
			this.resetCounter = resetCounter;
		}

		public String getIndexName() {
			return indexName;
		}

		public int getResetCounter() {
			return resetCounter;
		}
	}

	@Autowired
	AMXSharedValues amxSharedValues;

	/**
	 * Instantiates a new logged in users.
	 */
	public OracleVarsCache() {
		// super("OracleVarsCache");
	}

	public String getTranxIndex() {
		return EsConfig.indexName(DBSyncJobs.TRANSACTION_JOB.getIndexName());
	}

	public String getCustomerIndex() {
		return EsConfig.indexName(DBSyncJobs.CUSTOMER_JOB.getIndexName());
	}

	public String get(String key) {
		return amxSharedValues.getValue(key);
	}

	private void put(String key, String value) {
		amxSharedValues.putValue(key, value);
	}

	/**
	 * Index to write data into
	 * 
	 * @param job
	 * @return
	 */
	public String getIndex(DBSyncJobs job) {
		return EsConfig.indexName(job.getIndexName());
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

	public void clearStampStart(DBSyncJobs job) {
		amxSharedValues.removeValue(getIndex(job) + ASC_SEPERATOR + job.getResetCounter());
	}

	public void clearStampEnd(DBSyncJobs job) {
		amxSharedValues.removeValue(getIndex(job) + DESC_SEPERATOR + job.getResetCounter());
	}

}
