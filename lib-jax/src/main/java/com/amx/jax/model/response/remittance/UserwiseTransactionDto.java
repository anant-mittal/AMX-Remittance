package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class UserwiseTransactionDto {

	
	

	
	private BigDecimal totaltrnx=new BigDecimal(0);
	private BigDecimal employeeId;
	
	
	private String refundAmount;
	private String cash ;
	private String cheque;
	private String kNet ;
	private String others ;
	private String bankTransfer ;
	private String lastTrnx ;
	public BigDecimal getTotaltrnx() {
		return totaltrnx;
	}
	public void setTotaltrnx(BigDecimal totaltrnx) {
		this.totaltrnx = totaltrnx;
	}
	public BigDecimal getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public String getCheque() {
		return cheque;
	}
	public void setCheque(String cheque) {
		this.cheque = cheque;
	}
	public String getkNet() {
		return kNet;
	}
	public void setkNet(String kNet) {
		this.kNet = kNet;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	
	public String getBankTransfer() {
		return bankTransfer;
	}
	public void setBankTransfer(String bankTransfer) {
		this.bankTransfer = bankTransfer;
	}
	public String getLastTrnx() {
		return lastTrnx;
	}
	public void setLastTrnx(String lastTrnx) {
		this.lastTrnx = lastTrnx;
	}


	
}
