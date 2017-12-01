package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.DmsDocumentModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IDmsDocumentDao;
import com.amx.jax.userservice.repository.DmsDocumentRepository;
import com.amx.jax.util.DateUtil;

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
