package com.amx.jax.routing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
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

	@Override
	public void apply(Map<String, Object> input, Map<String, Object> output) {
		LOGGER.info("in ifsc routing logic with input, {}", input);
		try {
			BigDecimal rouringBankId = null;
			BigDecimal routingCountryId = (BigDecimal) input.get("P_ROUTING_COUNTRY_ID");
			BigDecimal beneBankId = (BigDecimal) input.get("P_BENEFICIARY_BANK_ID");
			BigDecimal serviceMasterid = (BigDecimal) input.get("P_SERVICE_MASTER_ID");

			try {
				rouringBankId = jdbcTemplate.queryForObject("SELECT   ROUTING_BANK_ID"
						+ "          FROM     EX_ROUTING_HEADER" + "          WHERE    SERVICE_MASTER_ID = 102"
						+ "          AND      ROUTING_COUNTRY_ID =  94 " + "          AND      NVL(ISACTIVE,'')='Y' ;",
						BigDecimal.class);
			} catch (Exception e) {
				throw new GlobalException("Duplicate Routing setup  for  Indian Bank");
			}

			List<ImpsMaster> impsMasters = impsMasterService.getImpsMaster(new BankMasterModel(rouringBankId),
					new BankMasterModel(beneBankId), ConstantDocument.Yes, new CountryMaster(routingCountryId));

			if (impsMasters != null && !impsMasters.isEmpty()) {
				if (routingCountryId.intValue() == 94 && serviceMasterid.intValue() == 102) {

				} else {
					// AMOUNT HIGH
				}
			}
			LOGGER.info("in ifsc routing logic with output, {}", output);
		} catch (Exception e) {
			LOGGER.warn("error occured in ifsc routing logic", e.getMessage());
		}
	}

	@Override
	public boolean isApplicable() {
		return false;
	}

}
