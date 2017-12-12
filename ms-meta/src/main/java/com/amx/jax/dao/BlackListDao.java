package com.amx.jax.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.repository.IBlackMasterRepository;

@Component
public class BlackListDao {

	@Autowired
	private IBlackMasterRepository repo;

	public List<BlackListModel> getBlackByName(String name) {
		name = name.replaceAll("\\s+", "");
		return repo.getBlackByName(name);
	}

	public List<BlackListModel> getBlackByLocalName(String localName) {
		localName = localName.replaceAll("\\s+", "");
		return repo.findByarabicName(localName);
	}
}
