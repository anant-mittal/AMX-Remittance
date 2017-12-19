package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.remittance.Document;

public interface IDocumentDao extends JpaRepository<Document, Serializable>{
	
	@Query("select d from Document d where d.documentID=?1")
	public List<Document> getDocumnetById(BigDecimal documnetId);
	
	
	@Query("select d from Document d where d.documentCode=?1")
	public List<Document> getDocumnetByCode(BigDecimal documnetcode);


}
