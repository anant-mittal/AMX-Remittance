package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.multitenant.MultiTenantConnectionProviderImpl;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceProcedureDao {

	private static final Logger LOGGER = LoggerService.getLogger(RemittanceProcedureDao.class);

	@Autowired
	MultiTenantConnectionProviderImpl connectionProvider;

	@Transactional
	public Map<String, Object> insertRemittanceForOnline(Map<String, Object> inputValues) {

		BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPL_CNTY_ID");
		BigDecimal companyId = (BigDecimal) inputValues.get("P_COMPANY_ID");
		BigDecimal customerNo = (BigDecimal) inputValues.get("P_CUSTOMER_ID");
		String userName = inputValues.get("P_USER_NAME") == null ? null : inputValues.get("P_USER_NAME").toString();
		String paymentId = inputValues.get("P_PAYMENT_ID") == null ? "" : inputValues.get("P_PAYMENT_ID").toString();
		String authcode = inputValues.get("P_AUTHCOD") == null ? "" : inputValues.get("P_AUTHCOD").toString();
		String tranId = inputValues.get("P_TRANID") == null ? "" : inputValues.get("P_TRANID").toString();
		String refId = inputValues.get("P_REFID") == null ? "" : inputValues.get("P_REFID").toString();

		LOGGER.info("EX_INSERT_REMITTANCE_ONLINE INPUT :" + inputValues.toString());

		Map<String, Object> output = new HashMap<>();

		Connection connection = null;

		try {
			connection = connectionProvider.getDataSource().getConnection();
			CallableStatement cs = connection
					.prepareCall("{call EX_INSERT_REMITTANCE_ONLINE(?,?,?,?,?,?,?,?,?,?,?,?)}");

			cs.setBigDecimal(1, applicationCountryId);
			cs.setBigDecimal(2, companyId);
			cs.setBigDecimal(3, customerNo);
			cs.setString(4, userName);
			cs.setString(5, paymentId);
			cs.setString(6, authcode);
			cs.setString(7, tranId);
			cs.setString(8, refId);
			cs.registerOutParameter(9, java.sql.Types.BIGINT);
			cs.registerOutParameter(10, java.sql.Types.BIGINT);
			cs.registerOutParameter(11, java.sql.Types.BIGINT);
			cs.registerOutParameter(12, java.sql.Types.VARCHAR);
			cs.executeUpdate();

			BigDecimal collectionFinanceYear = cs.getBigDecimal(9);
			BigDecimal collectionDocumentNumber = cs.getBigDecimal(10);
			BigDecimal collectionDocumentCode = cs.getBigDecimal(11);
			String outMessage = cs.getString(12);

			output.put("P_COLLECT_FINYR", collectionFinanceYear);
			output.put("P_COLLECTION_NO", collectionDocumentNumber);
			output.put("P_COLLECTION_DOCUMENT_CODE", collectionDocumentCode);
			output.put("P_ERROR_MESG", outMessage);
			LOGGER.info("EX_INSERT_REMITTANCE_ONLINE output :" + output.toString());
			cs.close();

		} catch (Exception e) {
			LOGGER.debug("Error while calling EX_INSERT_REMITTANCE_ONLINE : ", e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception ee) {
				LOGGER.debug("Error while closing connection. : ", ee);
			}
		}
		return output;
	}

	@Transactional
	public Map<String, Object> insertEMOSLIVETransfer(Map<String, Object> inputValues) {

		BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPL_CNTY_ID");
		BigDecimal companyId = (BigDecimal) inputValues.get("P_COMPANY_ID");
		BigDecimal documentId = (BigDecimal) inputValues.get("P_DOCUMENT_ID");
		BigDecimal financialYr = (BigDecimal) inputValues.get("P_DOC_FINYR");
		BigDecimal documentNo = (BigDecimal) inputValues.get("P_DOCUMENT_NO");

		LOGGER.info("EX_INSERT_EMOS_TRANSFER_LIVE INPUT :" + inputValues.toString());

		Map<String, Object> output = new HashMap<>();

		Connection connection = null;

		try {
			connection = connectionProvider.getDataSource().getConnection();
			CallableStatement cs = connection.prepareCall("{call EX_INSERT_EMOS_TRANSFER_LIVE(?, ?, ?, ?, ?,?)}");
			cs.setBigDecimal(1, applicationCountryId);
			cs.setBigDecimal(2, companyId);
			cs.setBigDecimal(3, documentId);
			cs.setBigDecimal(4, financialYr);
			cs.setBigDecimal(5, documentNo);
			cs.registerOutParameter(6, java.sql.Types.VARCHAR);
			cs.execute();
			output.put("P_ERROR_MESSAGE", cs.getString(6));
			LOGGER.info("EX_INSERT_EMOS_TRANSFER_LIVE OUTPUT :" + output.toString());
			cs.close();

		} catch (Exception e) {
			LOGGER.debug("Error while calling EX_INSERT_EMOS_TRANSFER_LIVE : ", e);
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception ee) {
				LOGGER.debug("Error while closing connection. : ", ee);
			}
		}
		return output;
	}

}
