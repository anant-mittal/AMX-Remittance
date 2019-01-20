package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.Date;

public class UserwiseTransactionDto {

	
	
	
	private BigDecimal refundAmount= new BigDecimal(0);
	private BigDecimal cash= new BigDecimal(0);
	private BigDecimal cheque= new BigDecimal(0);
	private BigDecimal kNet= new BigDecimal(0);
	private BigDecimal others= new BigDecimal(0);
	private BigDecimal cheqe= new BigDecimal(0);
	private BigDecimal bankTransfer= new BigDecimal(0);
	private BigDecimal lastTrnx= new BigDecimal(0);
	private BigDecimal totaltrnx=new BigDecimal(0);
	private BigDecimal employeeId;
	
	
/*	private String refundAmount;
	private BigDecimal cash ;
	private BigDecimal cheque;
	private BigDecimal kNet ;
	private BigDecimal others ;
	private BigDecimal cheqe ;
	private BigDecimal bankTransfer ;
	private BigDecimal lastTrnx ;
	private BigDecimal totaltrnx;
	private BigDecimal employeeId;*/
	
	
	
	
	public BigDecimal getCash() {
		return cash;
	}
	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}
	public BigDecimal getCheque() {
		return cheque;
	}
	public void setCheque(BigDecimal cheque) {
		this.cheque = cheque;
	}
	public BigDecimal getkNet() {
		return kNet;
	}
	public void setkNet(BigDecimal kNet) {
		this.kNet = kNet;
	}
	public BigDecimal getOthers() {
		return others;
	}
	public void setOthers(BigDecimal others) {
		this.others = others;
	}
	public BigDecimal getCheqe() {
		return cheqe;
	}
	public void setCheqe(BigDecimal cheqe) {
		this.cheqe = cheqe;
	}
	public BigDecimal getBankTransfer() {
		return bankTransfer;
	}
	public void setBankTransfer(BigDecimal bankTransfer) {
		this.bankTransfer = bankTransfer;
	}
	public BigDecimal getLastTrnx() {
		return lastTrnx;
	}
	public void setLastTrnx(BigDecimal lastTrnx) {
		this.lastTrnx = lastTrnx;
	}
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
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	
}
