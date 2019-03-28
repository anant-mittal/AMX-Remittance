package com.amx.jax.dbmodel.routing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.meta.ServiceMaster;


@Entity
@Table(name = "EX_ROUTING_DETAILS")
public class RoutingDetails implements java.io.Serializable{
	
	private static final long serialVersionUID = -59155724624336535L;
	
	private BigDecimal routingDetailsId;
	private RoutingHeader exRountingHeaderId;
	private CountryMaster exApplicationCountry;
	private CountryMaster exCountryId;
	private CountryMaster exRoutingCountryId;
	private BankMasterModel exRoutingBankId;
	private CurrencyMasterModel exCurrenyId;
	private ServiceMaster exServiceId;
	private String branchApplicability;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String approvedBy;
	private Date approvedDate;
	private BigDecimal agentBranchID;
	private BigDecimal agentBranchEmosMapCode;
	private BigDecimal agentBankId;
	
	public RoutingDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(generator="ex_routing_details_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_routing_details_seq",sequenceName="EX_ROUTING_DETAILS_SEQ",allocationSize=1)
	@Column(name ="ROUTING_DETAILS_ID", unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getRoutingDetailsId() {
		return routingDetailsId;
	}

	public void setRoutingDetailsId(BigDecimal routingDetailsId) {
		this.routingDetailsId = routingDetailsId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROUTING_HEADER_ID")
	public RoutingHeader getExRountingHeaderId() {
		return exRountingHeaderId;
	}

	public void setExRountingHeaderId(RoutingHeader exRountingHeaderId) {
		this.exRountingHeaderId = exRountingHeaderId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLICATION_COUNTRY_ID")
	public CountryMaster getExApplicationCountry() {
		return exApplicationCountry;
	}

	public void setExApplicationCountry(CountryMaster exApplicationCountry) {
		this.exApplicationCountry = exApplicationCountry;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	public CountryMaster getExCountryId() {
		return exCountryId;
	}

	public void setExCountryId(CountryMaster exCountryId) {
		this.exCountryId = exCountryId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROUTING_COUNTRY_ID")
	public CountryMaster getExRoutingCountryId() {
		return exRoutingCountryId;
	}

	public void setExRoutingCountryId(CountryMaster exRoutingCountryId) {
		this.exRoutingCountryId = exRoutingCountryId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROUTING_BANK_ID")
	public BankMasterModel getExRoutingBankId() {
		return exRoutingBankId;
	}

	public void setExRoutingBankId(BankMasterModel exRoutingBankId) {
		this.exRoutingBankId = exRoutingBankId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCY_ID")
	public CurrencyMasterModel getExCurrenyId() {
		return exCurrenyId;
	}

	public void setExCurrenyId(CurrencyMasterModel exCurrenyId) {
		this.exCurrenyId = exCurrenyId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_MASTER_ID")
	public ServiceMaster getExServiceId() {
		return exServiceId;
	}

	public void setExServiceId(ServiceMaster exServiceId) {
		this.exServiceId = exServiceId;
	}
	
	@Column(name="BRANCH_APPLICABILITY")
	public String getBranchApplicability() {
		return branchApplicability;
	}

	public void setBranchApplicability(String branchApplicability) {
		this.branchApplicability = branchApplicability;
	}

	@Column(name="ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name="MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Column(name="APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	@Column(name="APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name="AGENT_BRANCH_ID")
	public BigDecimal getAgentBranchID() {
		return agentBranchID;
	}

	public void setAgentBranchID(BigDecimal agentBranchID) {
		this.agentBranchID = agentBranchID;
	}
	@Column(name="AGENT_BRCH_EMOS_MAP_CODE")
	public BigDecimal getAgentBranchEmosMapCode() {
		return agentBranchEmosMapCode;
	}

	public void setAgentBranchEmosMapCode(BigDecimal agentBranchEmosMapCode) {
		this.agentBranchEmosMapCode = agentBranchEmosMapCode;
	}
	@Column(name="AGENT_BANK_ID")
	public BigDecimal getAgentBankId() {
		return agentBankId;
	}

	public void setAgentBankId(BigDecimal agentBankId) {
		this.agentBankId = agentBankId;
	}
	
	
	
}
