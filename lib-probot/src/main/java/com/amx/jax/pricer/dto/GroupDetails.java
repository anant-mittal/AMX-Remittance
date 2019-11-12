package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.GROUP_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.GROUP_VAL_TYPE;

public class GroupDetails implements Serializable, Comparable<GroupDetails> {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Application Country Id can not be Null or Empty")
	private BigDecimal applCountryId;

	private BigDecimal groupId;

	@NotNull(message = "Group Name can not be Null or Empty")
	private String groupName;

	@NotNull(message = "Group Type can not be Null or Empty")
	private GROUP_TYPE groupType;

	private String isActive;

	@NotNull(message = "Val Type can not be Null or Empty")
	private GROUP_VAL_TYPE valType;

	private List<String> valSet;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

	public BigDecimal getApplCountryId() {
		return applCountryId;
	}

	public void setApplCountryId(BigDecimal applCountryId) {
		this.applCountryId = applCountryId;
	}

	public BigDecimal getGroupId() {
		return groupId;
	}

	public void setGroupId(BigDecimal groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public GROUP_TYPE getGroupType() {
		return groupType;
	}

	public void setGroupType(GROUP_TYPE groupType) {
		this.groupType = groupType;
	}

	public GROUP_VAL_TYPE getValType() {
		return valType;
	}

	public void setValType(GROUP_VAL_TYPE valType) {
		this.valType = valType;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public List<String> getValSet() {
		return valSet;
	}

	public void setValSet(List<String> valSet) {
		this.valSet = valSet;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public int compareTo(GroupDetails o) {

		// null is heavy
		if (null == o || null == o.getCreatedDate()) {
			return -1;
		} else if (null == this.createdDate) {
			return 1;
		} else {
			int i = this.createdDate.compareTo(o.createdDate);
			return i != 0 ? -i : compareToByName(o);

		}

	}

	public int compareToByName(GroupDetails o) {

		if (null == o || null == o.getGroupName()) {
			return 1;
		} else if (null == this.getGroupName()) {
			return -1;
		} else {
			return this.getGroupName().compareTo(o.getGroupName());
		}

	}

}
