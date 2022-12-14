package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.BlackListModel;

public interface IBlackMasterRepository extends JpaRepository<BlackListModel, Serializable> {

	@Query(value = "select bm  from BlackListModel  bm where cAName=?1")
	public List<BlackListModel> findByarabicName(String localName);

	@Query(value = "select bm  from BlackListModel  bm where upper(cName)=?1")
	public List<BlackListModel> findByCANameIgnoreCase(String name);

}
