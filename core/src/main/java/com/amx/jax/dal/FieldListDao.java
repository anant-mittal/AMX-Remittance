package com.amx.jax.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.FieldList;
import com.amx.jax.repository.IFieldListRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FieldListDao {

	@Autowired
	IFieldListRepository iFielListRepository;

	public List<FieldList> getFieldList(String tenant, String nationality) {
		
		return iFielListRepository.getFieldList(tenant,nationality);
	}

	public List<FieldList> getFieldListWithoutNationality(String tenant) {		
		return iFielListRepository.getFieldListWithoutNationality(tenant);
	}
	
	

}
