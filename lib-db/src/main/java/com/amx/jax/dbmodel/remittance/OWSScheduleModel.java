package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
* Added By Amr Shokr
*/
@Entity
@Table(name="OWS_SCHEDULE")
public class OWSScheduleModel implements Serializable{
	
	private static final long serialVersionUID = 2315791709068216697L;
	
	private String corBank;
	private BigDecimal liveInd;
	private BigDecimal repeatInterval;
	private Date creationDate;
	private String creator;
	private Date update;
	private String modifier;
	private BigDecimal neftInd; 
	private BigDecimal rigsInd;
	private BigDecimal neftSTTime;
	private BigDecimal neftEDTime;
	private BigDecimal rigsSTTime;
	private BigDecimal rigsEDTime;
	private BigDecimal ruleId;
	private Date lastRunTime;
	private BigDecimal wkendSTTime;
	private BigDecimal wkendEDTime;
	private BigDecimal accRepeatInterval;
	private Date pinGenLastRunTime;
	private Date agentLastRunTime;
	private BigDecimal agentRepeatInterval;
	private String owsReportFlag;
	private BigDecimal eftInd;
	private String balanceCheckInd;
	private String flexField1;
	private String beneAccountCheckInd;
	private String ttbeneAccountCheckInd;
	
	
	public OWSScheduleModel() {
		super();
	}


	@Id
	@Column(name="CORSBNK")
	public String getCorBank() {
		return corBank;
	}


	public void setCorBank(String corBank) {
		this.corBank = corBank;
	}

	@Column(name="LIVE_IND")
	public BigDecimal getLiveInd() {
		return liveInd;
	}


	public void setLiveInd(BigDecimal liveInd) {
		this.liveInd = liveInd;
	}

	@Column(name="REPEAT_INTERVAL")
	public BigDecimal getRepeatInterval() {
		return repeatInterval;
	}


	public void setRepeatInterval(BigDecimal repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	
	@Column(name="CRTDAT")
	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name="CREATOR")
	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name="UPDDAT")
	public Date getUpdate() {
		return update;
	}


	public void setUpdate(Date update) {
		this.update = update;
	}

	@Column(name="MODIFIER")
	public String getModifier() {
		return modifier;
	}


	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Column(name="NEFT_IND")
	public BigDecimal getNeftInd() {
		return neftInd;
	}


	public void setNeftInd(BigDecimal neftInd) {
		this.neftInd = neftInd;
	}

	@Column(name="RTGS_IND")
	public BigDecimal getRigsInd() {
		return rigsInd;
	}


	public void setRigsInd(BigDecimal rigsInd) {
		this.rigsInd = rigsInd;
	}

	@Column(name="NEFT_ST_TIME")
	public BigDecimal getNeftSTTime() {
		return neftSTTime;
	}


	public void setNeftSTTime(BigDecimal neftSTTime) {
		this.neftSTTime = neftSTTime;
	}

	@Column(name="NEFT_ED_TIME")
	public BigDecimal getNeftEDTime() {
		return neftEDTime;
	}


	public void setNeftEDTime(BigDecimal neftEDTime) {
		this.neftEDTime = neftEDTime;
	}

	@Column(name="RTGS_ST_TIME")
	public BigDecimal getRigsSTTime() {
		return rigsSTTime;
	}


	public void setRigsSTTime(BigDecimal rigsSTTime) {
		this.rigsSTTime = rigsSTTime;
	}

	@Column(name="RTGS_ED_TIME")
	public BigDecimal getRigsEDTime() {
		return rigsEDTime;
	}


	public void setRigsEDTime(BigDecimal rigsEDTime) {
		this.rigsEDTime = rigsEDTime;
	}

	@Column(name="RULE_IND")
	public BigDecimal getRuleId() {
		return ruleId;
	}

	
	public void setRuleId(BigDecimal ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name="LAST_RUN_TIME")
	public Date getLastRunTime() {
		return lastRunTime;
	}

	
	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	@Column(name="WKEND_ST_TIME")
	public BigDecimal getWkendSTTime() {
		return wkendSTTime;
	}


	public void setWkendSTTime(BigDecimal wkendSTTime) {
		this.wkendSTTime = wkendSTTime;
	}

	@Column(name="WKEND_ED_TIME")
	public BigDecimal getWkendEDTime() {
		return wkendEDTime;
	}


	public void setWkendEDTime(BigDecimal wkendEDTime) {
		this.wkendEDTime = wkendEDTime;
	}

	@Column(name="ACC_REPEAT_INTERVAL")
	public BigDecimal getAccRepeatInterval() {
		return accRepeatInterval;
	}


	public void setAccRepeatInterval(BigDecimal accRepeatInterval) {
		this.accRepeatInterval = accRepeatInterval;
	}

	@Column(name="PIN_GEN_LAST_RUN_TIME")
	public Date getPinGenLastRunTime() {
		return pinGenLastRunTime;
	}


	public void setPinGenLastRunTime(Date pinGenLastRunTime) {
		this.pinGenLastRunTime = pinGenLastRunTime;
	}

	@Column(name="AGENT_LAST_RUN_TIME")
	public Date getAgentLastRunTime() {
		return agentLastRunTime;
	}


	public void setAgentLastRunTime(Date agentLastRunTime) {
		this.agentLastRunTime = agentLastRunTime;
	}

	@Column(name="AGENT_REPEAT_INTERVAL")
	public BigDecimal getAgentRepeatInterval() {
		return agentRepeatInterval;
	}


	public void setAgentRepeatInterval(BigDecimal agentRepeatInterval) {
		this.agentRepeatInterval = agentRepeatInterval;
	}

	@Column(name="OWS_REPORT_FLAG")
	public String getOwsReportFlag() {
		return owsReportFlag;
	}


	public void setOwsReportFlag(String owsReportFlag) {
		this.owsReportFlag = owsReportFlag;
	}

	@Column(name="EFT_IND")
	public BigDecimal getEftInd() {
		return eftInd;
	}


	public void setEftInd(BigDecimal eftInd) {
		this.eftInd = eftInd;
	}

	@Column(name="BALANCE_CHECK_IND")
	public String getBalanceCheckInd() {
		return balanceCheckInd;
	}


	public void setBalanceCheckInd(String balanceCheckInd) {
		this.balanceCheckInd = balanceCheckInd;
	}

	@Column(name="FLEXIFIELD1")
	public String getFlexField1() {
		return flexField1;
	}


	public void setFlexField1(String flexField1) {
		this.flexField1 = flexField1;
	}

	@Column(name="BENE_ACCT_CHECK_IND")
	public String getBeneAccountCheckInd() {
		return beneAccountCheckInd;
	}


	public void setBeneAccountCheckInd(String beneAccountCheckInd) {
		this.beneAccountCheckInd = beneAccountCheckInd;
	}


	@Column(name="TT_BENE_ACCT_CHECK_IND")
	public String getTtbeneAccountCheckInd() {
		return ttbeneAccountCheckInd;
	}


	public void setTtbeneAccountCheckInd(String ttbeneAccountCheckInd) {
		this.ttbeneAccountCheckInd = ttbeneAccountCheckInd;
	}
	
	
	
	

}
