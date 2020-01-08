package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


 

@Entity
@Table(name="EX_DELIVERY_MODE")
public class DeliveryMode implements Serializable {
	private static final long serialVersionUID = 2315791709068216697L;
	private BigDecimal deliveryModeId;
	private String deliveryMode;
	/*private String  englishDeliveryModeDesc;
	private String localDeliveryModeDesc;*/
	private String createdBy;
	private Date   createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isActive;
	private List<DeliveryModeDesc> deliveryModeList=new ArrayList<DeliveryModeDesc>();
	//private List<ServiceApplicabilityRule> serviceApplicabilityRule=new ArrayList<ServiceApplicabilityRule>();
	//private List<BankServiceRule> bankServiceRule=new ArrayList<BankServiceRule>();
	//private Set<Remittance> exRemittances = new HashSet<Remittance>(0);
	private String appovedBy;
	private Date approveDate;
	private String remrks;
	
	

	public DeliveryMode(){
		
	}

	@Id
	@GeneratedValue(generator="ex_delivery_mode_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_delivery_mode_seq",sequenceName="EX_DELIVERY_MODE_SEQ",allocationSize=1)
	@Column(name ="DELIVERY_MODE_ID" , unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getDeliveryModeId() {
	return deliveryModeId;
	}
	public void setDeliveryModeId(BigDecimal deliveryModeId) {
	this.deliveryModeId = deliveryModeId;
	}

    @Column(name = "DELIVERY_CODE",unique=true)
   	public String getDeliveryMode() {
		return deliveryMode;
	}
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
	
	/*@Column(name ="DELIVERY_DESC_ENGLISH")
	public String getEnglishDeliveryModeDesc() {
		return englishDeliveryModeDesc;
	}
	public void setEnglishDeliveryModeDesc(String englishDeliveryModeDesc) {
		this.englishDeliveryModeDesc = englishDeliveryModeDesc;
	}
	@Column(name = "DELIVERY_DESC_ARABIC")
	public String getLocalDeliveryModeDesc() {
		return localDeliveryModeDesc;
	}

	public void setLocalDeliveryModeDesc(String localDeliveryModeDesc) {
		this.localDeliveryModeDesc = localDeliveryModeDesc;
	}*/
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
	
	@Column(name="ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryMode")
	public List<DeliveryModeDesc> getDeliveryModeList() {
		return deliveryModeList;
	}

	public void setDeliveryModeList(List<DeliveryModeDesc> deliveryModeList) {
		this.deliveryModeList = deliveryModeList;
	}
	
	@Column(name="APPROVED_BY")
	public String getAppovedBy() {
		return appovedBy;
	}
	public void setAppovedBy(String appovedBy) {
		this.appovedBy = appovedBy;
	}
	@Column(name="APPROVED_DATE")
	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}
	
	@Column(name="REMARKS")
	public String getRemrks() {
		return remrks;
	}

	public void setRemrks(String remrks) {
		this.remrks = remrks;
	}
	
	
	
	
 

}
