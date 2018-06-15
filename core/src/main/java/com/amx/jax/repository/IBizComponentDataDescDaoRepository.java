package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BizComponentDataDesc;
import com.amx.jax.dbmodel.LanguageType;

public interface IBizComponentDataDescDaoRepository extends JpaRepository<BizComponentDataDesc, Serializable> {
	@Query("select a from BizComponentDataDesc a where a.dataDesc= ?1 and a.fsLanguageType = ?2")
	public List<BizComponentDataDesc> getComponentId(String inputString, LanguageType langId);

	public List<BizComponentDataDesc> findByFsBizComponentDataAndFsLanguageType(BizComponentData bizComponentData,
			LanguageType langId);
}
