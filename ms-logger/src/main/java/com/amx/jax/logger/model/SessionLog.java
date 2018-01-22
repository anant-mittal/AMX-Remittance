package com.amx.jax.logger.model;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.amx.jax.logger.events.SessionEvent;

/**
 * 
 */
@Document
public class SessionLog extends AbstractLogMessage {

	@TextIndexed
	private String customerId;

	@TextIndexed(weight = 2)
	private String searchTerm;

	@TextIndexed
	private String currentPage;

	public SessionLog(SessionEvent sessionEvent) {
		super(sessionEvent);
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	@Override
	public String toString() {
		return "CustomerLog{" + "customerId='" + customerId + '\'' + ", moduleName='" + this.getModuleName() + '\''
				+ ", searchTerm='" + searchTerm + '\'' + ", currentPage='" + currentPage + '\'' + "} "
				+ super.toString();
	}
}
