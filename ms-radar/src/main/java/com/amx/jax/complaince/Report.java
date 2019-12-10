package com.amx.jax.complaince;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.txw2.annotation.XmlElement;
	
@XmlRootElement(name = "report")
@XmlType(propOrder={"rentity_id", "submission_code", "report_code","submission_date", "currency_code_local", "reporting_person","location", "reason", "action", "transaction", "report_indicators"})	
		public class Report 
		{
			
			  private String rentity_id;
			  private String submission_code;
			  private String report_code;
			  private String submission_date;
			  private String currency_code_local;
			  private ReportingPerson reporting_person;
			  private Location location;
			  private String reason;
			  private String action;
			  private Transaction transaction;
			  private ReportIndicators report_indicators;
			  
			@XmlElement  
			public String getRentity_id() {
				return rentity_id;
			}
			public void setRentity_id(String rentity_id) {
				this.rentity_id = rentity_id;
			}
			@XmlElement 
			public String getSubmission_code() {
				return submission_code;
			}
			public void setSubmission_code(String submission_code) {
				this.submission_code = submission_code;
			}
			@XmlElement 
			public String getReport_code() {
				return report_code;
			}
			public void setReport_code(String report_code) {
				this.report_code = report_code;
			}
			@XmlElement 
			public String getCurrency_code_local() {
				return currency_code_local;
			}
			public void setCurrency_code_local(String currency_code_local) {
				this.currency_code_local = currency_code_local;
			}
			@XmlElement 
			public String getSubmission_date() {
				return submission_date;
			}
			public void setSubmission_date(String submission_date) {
				this.submission_date = submission_date;
			}
			@XmlElement
			public ReportingPerson getReporting_person() {
				return reporting_person;
			}
			public void setReporting_person(ReportingPerson reporting_person) {
				this.reporting_person = reporting_person;
			}
			@XmlElement 
			public Location getLocation() {
				return location;
			}
			public void setLocation(Location location) {
				this.location = location;
			}
			@XmlElement 
			public String getReason() {
				return reason;
			}
			public void setReason(String reason) {
				this.reason = reason;
			}
			@XmlElement 
			public String getAction() {
				return action;
			}
			public void setAction(String action) {
				this.action = action;
			}
			@XmlElement 
			public Transaction getTransaction() {
				return transaction;
			}
			public void setTransaction(Transaction transaction) {
				this.transaction = transaction;
			}		
			
			@XmlElement 
			public ReportIndicators getReport_indicators() {
				return report_indicators;
			}
			public void setReport_indicators(ReportIndicators report_indicators) {
				this.report_indicators = report_indicators;
			}
			
			public Report(String rentity_id, String submission_code, String report_code, String submission_date,
					String currency_code_local, ReportingPerson reporting_person, Location location, String reason,
					String action, Transaction transaction, ReportIndicators report_indicators) {
				super();
				this.rentity_id = rentity_id;
				this.submission_code = submission_code;
				this.report_code = report_code;
				this.submission_date = submission_date;
				this.currency_code_local = currency_code_local;
				this.reporting_person = reporting_person;
				this.location = location;
				this.reason = reason;
				this.action = action;
				this.transaction = transaction;
				this.report_indicators = report_indicators;
			}
			public Report() {
				super();
				
			}
			
			
			
			 
				 
			 
		}