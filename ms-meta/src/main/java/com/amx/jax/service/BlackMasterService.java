package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.BlackListModel;
import com.amx.jax.repository.IBlackMasterRepository;

@Service
public class BlackMasterService {
	
	@Autowired
	IBlackMasterRepository  blackMasterRepository;
	
	public List<BlackListModel> getBlackList(String name){
		return blackMasterRepository.getBlackByName(name);
	}

}
