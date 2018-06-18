package com.amx.jax.routing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.ImpsMaster;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.service.ImpsMasterService;

@Component
public class IfscRoutingLogic implements IRoutingLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(IfscRoutingLogic.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	ImpsMasterService impsMasterService;
	@Autowired
	BizcomponentDao bizcomponentDao;

	@Override
	public void apply(Map<String, Object> input, Map<String, Object> output) {
		LOGGER.info("in ifsc routing logic with input, {}", input);
		try {
			BigDecimal rouringBankId = null;
			BigDecimal routingCountryId = (BigDecimal) input.get("P_ROUTING_COUNTRY_ID");
			BigDecimal beneBankId = (BigDecimal) input.get("P_BENEFICIARY_BANK_ID");
			BigDecimal serviceMasterid = (BigDecimal) input.get("P_SERVICE_MASTER_ID");
			BigDecimal fcurrencyId = (BigDecimal) input.get("P_CURRENCY_ID");
			BigDecimal fcAmount = (BigDecimal) input.get("P_CALCULATED_FC_AMOUNT");
			BigDecimal applicationCountryId = (BigDecimal) input.get("P_APPLICATION_COUNTRY_ID");
			try {
				rouringBankId = jdbcTemplate.queryForObject("SELECT   ROUTING_BANK_ID"
						+ "          FROM     EX_ROUTING_HEADER" + "          WHERE    SERVICE_MASTER_ID = 102"
						+ "          AND      ROUTING_COUNTRY_ID =  94 " + "          AND      NVL(ISACTIVE,'')='Y' ",
						BigDecimal.class);
			} catch (Exception e) {
				throw new GlobalException("Duplicate Routing setup  for  Indian Bank");
			}

			List<ImpsMaster> impsMasters = impsMasterService.getImpsMaster(new BankMasterModel(rouringBankId),
					new BankMasterModel(beneBankId), ConstantDocument.Yes, new CountryMaster(routingCountryId));

			if (impsMasters != null && !impsMasters.isEmpty()) {
				if (routingCountryId.intValue() == 94 && serviceMasterid.intValue() == 102) {
					BigDecimal toAmount = getToAmount(routingCountryId, fcurrencyId, rouringBankId);
					if (toAmount == null) {
						LOGGER.warn("IMPS SETUP  NOT DONE FOR ROUTING");
						return;
					}
					if (fcAmount.intValue() < toAmount.intValue()) {
						findRemittanceAndDeliveryMode(input, output);
					} else {
						
					}
				}
			}
			LOGGER.info("in ifsc routing logic with output, {}", output);
		} catch (Exception e) {
			LOGGER.warn("error occured in ifsc routing logic", e.getMessage());
		}
	}

	private void findRemittanceAndDeliveryMode(Map<String, Object> input, Map<String, Object> output) {
		List<Object> args = new ArrayList<>();
		args.add(input.get("P_APPLICATION_COUNTRY_ID"));
		args.add(input.get("P_BENE_COUNTRY_ID"));
		args.add(input.get("P_BENE_BANK_ID"));
		args.add(input.get("P_BENE_BANK_BRANCH_ID"));
		args.add(output.get("P_ROUTING_COUNTRY_ID"));
		args.add(input.get("P_ROUTING_BANK_ID"));
		args.add(input.get("P_ROUTING_BANK_BRANCH_ID"));
		args.add(input.get("P_FOREIGN_CURRENCY_ID"));
		args.add(input.get("P_SERVICE_MASTER_ID"));
		args.add(bizcomponentDao.findCustomerTypeId("I")); // Individual
		args.add(input.get("P_CALCULATED_FC_AMOUNT"));

		String sqlQuery = " SELECT DISTINCT A.REMITTANCE_MODE_ID,A.DELIVERY_MODE_ID"
				+ "  FROM   V_EX_ROUTING_DETAILS_IMPS A , EX_BANK_SERVICE_RULE B,EX_BANK_CHARGES C"
				+ "  WHERE  A.APPLICATION_COUNTRY_ID =  ?" + "  AND    A.COUNTRY_ID             =  ?"
				+ "  AND    A.BENE_BANK_ID           =  ?" + "  AND    A.BENE_BANK_BRANCH_ID    =  ?"
				+ "  AND    A.ROUTING_COUNTRY_ID     =  ?" + "  AND    A.ROUTING_BANK_ID        =  ?    "
				+ "  AND    A.BANK_BRANCH_ID         =  ?" + "  AND      A.CURRENCY_ID            =  ?"
				+ "  AND    A.SERVICE_MASTER_ID      =  ?"
				+ "  AND    A.APPLICATION_COUNTRY_ID = B.APPLICATION_COUNTRY_ID"
				+ "  AND    A.ROUTING_COUNTRY_ID = B.COUNTRY_ID" + "  AND    A.CURRENCY_ID = B.CURRENCY_ID"
				+ "  AND    A.ROUTING_BANK_ID = B.BANK_ID" + "  AND    A.REMITTANCE_MODE_ID = B.REMITTANCE_MODE_ID"
				+ "  AND    A.DELIVERY_MODE_ID = B.DELIVERY_MODE_ID" + "  AND    A.REMITTANCE_MODE_ID       =  33     "
				+ "  AND    B.APPROVED_BY IS NOT NULL" + "  AND    B.BANK_SERVICE_RULE_ID = C.BANK_SERVICE_RULE_ID"
				+ "  AND    C.CHARGES_TYPE = 'C'" + "  AND    C.CHARGES_FOR   =   ? "
				+ "  AND    ? BETWEEN C.FROM_AMOUNT AND C.TO_AMOUNT";
		Map<String, Object> map = jdbcTemplate.queryForMap(sqlQuery, args.toArray());
		if (map == null || map.isEmpty()) {
			args.remove(args.size() - 1);
			args.add(new BigDecimal(777));
			map = jdbcTemplate.queryForMap(sqlQuery, args.toArray());
		}
		output.putAll(map);
	}

	private BigDecimal getToAmount(BigDecimal routingCountryId, BigDecimal fcurrencyId, BigDecimal routingBankId) {
		return jdbcTemplate.queryForObject(
				" SELECT  B.TO_AMOUNT   " + " FROM    EX_BANK_SERVICE_RULE A," + "         EX_BANK_CHARGES B"
						+ " WHERE   A.BANK_SERVICE_RULE_ID  =   B.BANK_SERVICE_RULE_ID"
						+ " AND     A.COUNTRY_ID            =   ?" + " AND     A.CURRENCY_ID           =   ?"
						+ " AND     A.BANK_ID=   ?      " + " AND     A.REMITTANCE_MODE_ID    =   33"
						+ " AND    A.ISACTIVE           =   'Y'" + "AND    B.ISACTIVE           =   'Y' "
						+ " AND    B.CHARGES_TYPE          =   'C'",
				BigDecimal.class, routingCountryId, fcurrencyId, routingBankId);
	}

	@Override
	public boolean isApplicable() {
		return true;
	}

}
