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
		return repo.getBlackByName(name);
	}

	public List<BlackListModel> getBlackByLocalName(String localName) {
		return repo.findByarabicName(localName);
	}
}
