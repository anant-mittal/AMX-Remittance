package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.repository.GroupingMasterRepository;

@Component
public class GroupingMasterDao {

	@Autowired
	GroupingMasterRepository repository;

	public GroupingMaster getGroupById(BigDecimal groupId) {
		return repository.findOne(groupId);
	}

	public Map<BigDecimal, GroupingMaster> getGroupById(List<BigDecimal> groupIds) {

		Iterable<GroupingMaster> grpItr = repository.findAll(groupIds);

		Map<BigDecimal, GroupingMaster> grpMap = new HashMap<BigDecimal, GroupingMaster>();

		if (grpItr != null) {
			for (GroupingMaster master : grpItr) {
				grpMap.put(master.getId(), master);
			}
		}

		return grpMap;
	}

	public GroupingMaster getByGroupTypeAndGroupName(String groupType, String groupName) {
		return repository.findByGroupTypeAndGroupName(groupType, groupName);
	}

	public List<GroupingMaster> getActiveByGroupType(String groupType) {
		return repository.findByGroupTypeAndIsActive(groupType, "Y");
	}

	public List<GroupingMaster> getAllGroup() {
		return (List<GroupingMaster>) repository.findAll();
	}

	public List<GroupingMaster> getGroupForCurrency() {
		return repository.getGroupForCurrency();
	}

	public Iterable<GroupingMaster> saveAll(Iterable<GroupingMaster> groups) {
		return repository.save(groups);
	}

	public GroupingMaster save(GroupingMaster group) throws DataIntegrityViolationException {
		return repository.save(group);
	}

	public void delete(BigDecimal grpId) {
		repository.delete(grpId);
	}

}
