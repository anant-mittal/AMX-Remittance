package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.DmsDocumentModel;

public interface DmsDocumentRepository extends JpaRepository<DmsDocumentModel, Serializable>{
	
	@Query("select dm from DmsDocumentModel dm where countryId=1 and docBlobId =?1 and docFyr=?2")
	public List<DmsDocumentModel> getDmsDocumentList(BigDecimal blobId,BigDecimal docFyr);

}
