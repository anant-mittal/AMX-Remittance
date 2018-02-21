package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.DmsDocumentModel;
import com.amx.jax.userservice.repository.DmsDocumentRepository;

/**
 * 
 * @author : Rabil Date :
 *
 */
@Component
public class DmsDocumentDao {

	private Logger logger = Logger.getLogger(DmsDocumentDao.class);

	@Autowired
	DmsDocumentRepository DmsDocumentRepo;

	public List<DmsDocumentModel> getDmsDocument(BigDecimal blobId, BigDecimal docFyr) {
		List<DmsDocumentModel> dmsDocumentList = DmsDocumentRepo.getDmsDocumentList(blobId, docFyr);
		return dmsDocumentList;
	}

}
