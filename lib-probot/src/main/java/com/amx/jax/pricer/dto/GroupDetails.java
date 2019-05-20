package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class GroupDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal groupId;

	private String groupName;

	private String groupType;

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

}
