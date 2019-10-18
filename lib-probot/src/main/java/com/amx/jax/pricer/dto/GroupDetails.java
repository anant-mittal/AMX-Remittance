package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GroupDetails implements Serializable, Comparable<GroupDetails> {

	private static final long serialVersionUID = 1L;

	private BigDecimal groupId;

	private String groupName;

	private String groupType;

	private String isActive;

	private String valType;

	private List<String> valSet;

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

	/*public static class GroupDetailsComparator implements Comparator<GroupDetails> {

		@Override
		public int compare(GroupDetails arg1, GroupDetails arg2) {
			if (null != arg1.getGroupName()) {
				return arg1.getGroupName().compareTo(arg2.getGroupName());
			}
			return 0;
		}

	}*/

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
