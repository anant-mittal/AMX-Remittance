package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

public class GroupDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal groupId;

	private String groupName;

	private String groupType;

	private String isActive;

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

	public static class GroupDetailsComparator implements Comparator<GroupDetails> {

		@Override
		public int compare(GroupDetails arg1, GroupDetails arg2) {
			if (null != arg1.getGroupName()) {
				return arg1.getGroupName().compareTo(arg2.getGroupName());
			}
			return 0;
		}

	}
}
