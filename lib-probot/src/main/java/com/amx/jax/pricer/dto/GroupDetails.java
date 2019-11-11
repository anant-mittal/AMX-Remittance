package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

public class GroupDetails implements Serializable, Comparable<GroupDetails> {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Application Country Id can not be Null or Empty")
	private BigDecimal applCountryId;

	private BigDecimal groupId;

	@NotNull(message = "Group Name can not be Null or Empty")
	private String groupName;

	@NotNull(message = "Group Type can not be Null or Empty")
	private String groupType;

	private String isActive;

	@NotNull(message = "Val Type can not be Null or Empty")
	private String valType;

	private List<String> valSet;

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

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getValType() {
		return valType;
	}

	public void setValType(String valType) {
		this.valType = valType;
	}

	public List<String> getValSet() {
		return valSet;
	}

	public void setValSet(List<String> valSet) {
		this.valSet = valSet;
	}

	@Override
	public int compareTo(GroupDetails o) {

		if (null == o || null == o.getGroupName()) {
			return 1;
		} else if (null == this.getGroupName()) {
			return -1;
		} else {
			return this.getGroupName().compareTo(o.getGroupName());
		}

	}

}
