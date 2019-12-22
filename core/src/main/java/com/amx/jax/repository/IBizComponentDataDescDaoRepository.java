package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
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
	
	@Query(value = "select a from BizComponentDataDesc a where a.fsBizComponentData in (198,201,204,197) and a.fsLanguageType = ?1")
	public List<BizComponentDataDesc> findByFsBizComponentDataDesc(LanguageType langId);
	
	@Query(value="SELECT a  FROM FS_BIZ_COMPONENT_DATA_DESC WHERE COMPONENT_DATA_ID =?1 AND LANGUAGE_ID = 1",nativeQuery=true)
	public BizComponentDataDesc getCustomrIdType(BigDecimal  idType);

}
