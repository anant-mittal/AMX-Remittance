package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.FieldList;

public interface IFieldListRepository extends JpaRepository<FieldList, Serializable>{

	
	List<FieldList> getFieldListByTenantAndNationalityAndComponent(String tenant, String nationality, String component);
	
	List<FieldList> getFieldListByTenantAndComponent(String tenant, String component);

}
