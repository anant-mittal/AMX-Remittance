package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.FieldList;

public interface IFieldListRepository extends JpaRepository<FieldList, Serializable>{

	@Query("select f from FieldList f where f.tenant = ?1 and f.nationality = ?2")
	List<FieldList> getFieldList(String tenant, String nationality);

	@Query("select f from FieldList f where f.tenant = ?1 ")
	List<FieldList> getFieldListWithoutNationality(String tenant);

}
