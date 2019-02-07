package com.amx.jax.routing;

import static com.amx.jax.constant.ConstantDocument.BANK_INDICATOR_BENEFICIARY_BANK;
import static com.amx.jax.constant.ConstantDocument.BANK_INDICATOR_CORRESPONDING_BANK;
import static com.amx.jax.constant.ConstantDocument.DELIVERY_MODE_BANKING_CHANNEL;
import static com.amx.jax.constant.ConstantDocument.REMITTANCE_MODE_EFT;
import static com.amx.jax.constant.ConstantDocument.REMITTANCE_MODE_RTGS;
import static com.amx.jax.constant.ConstantDocument.Yes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dao.BankDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BankServiceRule;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.ImpsMaster;
import com.amx.jax.dbmodel.treasury.BankApplicability;
import com.amx.jax.dbmodel.treasury.BankIndicator;
import com.amx.jax.exrateservice.service.NewExchangeRateService;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.ImpsMasterService;
import com.amx.jax.services.RoutingService;

@Component
public class ImpsRoutingLogic implements IRoutingLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImpsRoutingLogic.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	ImpsMasterService impsMasterService;
	@Autowired
	BizcomponentDao bizcomponentDao;
	@Autowired
	BankMetaService bankMetaService;
	@Autowired
	BankDao bankDao;
	@Autowired
	RoutingService routingService;
	@Autowired
	NewExchangeRateService newExchangeRateService;

	@Override
	public void apply(Map<String, Object> input, Map<String, Object> output) {
		LOGGER.info("in IMPS routing logic with input, {}", input);
		Map<String, Object> inputTemp = new HashMap<String, Object>(input);
		try {
			inputTemp.put("P_SERVICE_MASTER_ID", ConstantDocument.SERVICE_MASTER_ID_TT);

			BigDecimal routingCountryId = (BigDecimal) inputTemp.get("P_ROUTING_COUNTRY_ID");
			BigDecimal beneCountryId = (BigDecimal) inputTemp.get("P_BENEFICIARY_COUNTRY_ID");
			BigDecimal beneBankId = (BigDecimal) inputTemp.get("P_BENEFICIARY_BANK_ID");
			BigDecimal fcurrencyId = (BigDecimal) inputTemp.get("P_CURRENCY_ID");
			BigDecimal fcAmount = getForeignAmount(inputTemp);
			inputTemp.put("P_FOREIGN_AMT", fcAmount);
			findRoutingBankAndBranchId(inputTemp);
			BigDecimal rouringBankIdIMPS = (BigDecimal) inputTemp.get("P_ROUTING_BANK_ID_IMPS");
			List<ImpsMaster> impsMasters = impsMasterService.getImpsMaster(new BankMasterModel(rouringBankIdIMPS),
					new BankMasterModel(beneBankId), Yes, new CountryMaster(routingCountryId));

			if (impsMasters != null && !impsMasters.isEmpty()) {
				if (routingCountryId.intValue() == 94) {
					BigDecimal toAmount = getToAmount(routingCountryId, fcurrencyId, rouringBankIdIMPS);
					if (toAmount == null) {
						LOGGER.warn("IMPS SETUP  NOT DONE FOR ROUTING");
						return;
					}
					boolean result = false;
					if (fcAmount.intValue() < toAmount.intValue()) {
						result = findRemittanceAndDeliveryModeIMPS(inputTemp, output);
						if(result) {
							output.put("P_ROUTING_BANK_ID", inputTemp.get("P_ROUTING_BANK_ID_IMPS"));
							output.put("P_ROUTING_BANK_BRANCH_ID", inputTemp.get("P_ROUTING_BANK_BRANCH_ID_IMPS"));
						}
					} else {
						result = findRemittanceAndDeliveryModeNonIMPS(output, beneBankId, beneCountryId, fcurrencyId,
								(BigDecimal) inputTemp.get("P_ROUTING_BANK_ID"));
					}
					if (result) {
						output.put("P_SERVICE_MASTER_ID", inputTemp.get("P_SERVICE_MASTER_ID"));
					}
				}
			}
			LOGGER.info("in IMPS routing logic with output, {}", output);
		} catch (Exception e) {
			LOGGER.warn("error occured in IMPS routing logic", e.getMessage());
		}
	}

	private BigDecimal getForeignAmount(Map<String, Object> inputTemp) {

		if (inputTemp.get("P_FOREIGN_AMT") != null) {
			return (BigDecimal) inputTemp.get("P_FOREIGN_AMT");
		}
		BigDecimal localAmount = (BigDecimal) inputTemp.get("P_LOCAL_AMT");
		BigDecimal toCurrencyId = (BigDecimal) inputTemp.get("P_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) inputTemp.get("P_ROUTING_BANK_ID");
		BigDecimal beneBankCountryId = (BigDecimal) inputTemp.get("P_BENEFICIARY_COUNTRY_ID");
		ExchangeRateBreakup exRateBreakup = newExchangeRateService.getExchangeRateBreakup(toCurrencyId, localAmount,
				routingBankId, beneBankCountryId);
		return exRateBreakup.getConvertedFCAmount();

	}

	private void findRoutingBankAndBranchId(Map<String, Object> inputTemp) {
		BigDecimal routingBankId = null;
		try {
			routingBankId = jdbcTemplate.queryForObject("SELECT   ROUTING_BANK_ID"
					+ "          FROM     EX_ROUTING_HEADER" + "          WHERE    SERVICE_MASTER_ID = "
					+ ConstantDocument.SERVICE_MASTER_ID_TT.intValue() + "          AND      ROUTING_COUNTRY_ID =  94 "
					+ "          AND      NVL(ISACTIVE,'')='Y' ", BigDecimal.class);
			inputTemp.put("P_ROUTING_BANK_ID_IMPS", routingBankId);

		} catch (Exception e) {
			throw new GlobalException("Duplicate Routing setup  for  Indian Bank");
		}
		try {
			BigDecimal routingBankBranchId = jdbcTemplate.queryForObject("SELECT   BANK_BRANCH_ID"
					+ "          FROM     EX_ROUTING_DETAILS" + "  WHERE  ROUTING_BANK_ID = " + routingBankId.intValue()
					+ " AND   SERVICE_MASTER_ID = " + ConstantDocument.SERVICE_MASTER_ID_TT.intValue()
					+ "          AND      ROUTING_COUNTRY_ID =  94 " + "          AND      NVL(ISACTIVE,'')='Y' ",
					BigDecimal.class);
			inputTemp.put("P_ROUTING_BANK_BRANCH_ID_IMPS", routingBankBranchId);

		} catch (Exception e) {
			throw new GlobalException("Duplicate Routing setup  for  Indian Bank Branch");
		}
	}

	/**
	 * @param output
	 * @param beneBankId
	 * @param beneCountryId
	 * @param fcurrencyId
	 * @param rouringBankId
	 * @return true when remittance and delivery mode is found else false
	 * 
	 */
	private boolean findRemittanceAndDeliveryModeNonIMPS(Map<String, Object> output, BigDecimal beneBankId,
			BigDecimal beneCountryId, BigDecimal fcurrencyId, BigDecimal rouringBankId) {
		boolean result = false;
		BankApplicability bankApplicabality = bankMetaService.getBankApplicability(beneBankId);
		if (bankApplicabality != null && bankApplicabality.getBankInd() != null) {
			BankIndicator bankInd = bankApplicabality.getBankInd();
			if (BANK_INDICATOR_CORRESPONDING_BANK.equals(bankInd.getBankIndicatorCode())) {
				List<BankServiceRule> eftServiceRule = bankDao.getBankServiceRule(rouringBankId, beneCountryId,
						fcurrencyId, REMITTANCE_MODE_EFT, DELIVERY_MODE_BANKING_CHANNEL);
				if (CollectionUtils.isNotEmpty(eftServiceRule)) {
					output.put("P_REMITTANCE_MODE_ID", REMITTANCE_MODE_EFT);
					output.put("P_DELIVERY_MODE_ID", DELIVERY_MODE_BANKING_CHANNEL);
					result = true;
				}
			}
			if (BANK_INDICATOR_BENEFICIARY_BANK.equals(bankInd.getBankIndicatorCode())) {
				List<BankServiceRule> eftServiceRule = bankDao.getBankServiceRule(rouringBankId, beneCountryId,
						fcurrencyId, REMITTANCE_MODE_RTGS, DELIVERY_MODE_BANKING_CHANNEL);
				if (CollectionUtils.isNotEmpty(eftServiceRule)) {
					output.put("P_REMITTANCE_MODE_ID", REMITTANCE_MODE_RTGS);
					output.put("P_DELIVERY_MODE_ID", DELIVERY_MODE_BANKING_CHANNEL);
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * @param input
	 * @param output
	 * @return true when remittance and delivery mode is found else false
	 * 
	 */
	private boolean findRemittanceAndDeliveryModeIMPS(Map<String, Object> input, Map<String, Object> output) {
		List<Object> args = new ArrayList<>();
		args.add(input.get("P_APPLICATION_COUNTRY_ID"));
		args.add(input.get("P_BENEFICIARY_COUNTRY_ID"));
		args.add(input.get("P_BENEFICIARY_BANK_ID"));
		args.add(input.get("P_BENEFICIARY_BRANCH_ID"));
		args.add(input.get("P_ROUTING_COUNTRY_ID"));
		args.add(input.get("P_ROUTING_BANK_ID_IMPS"));
		args.add(input.get("P_ROUTING_BANK_BRANCH_ID_IMPS"));
		args.add(input.get("P_FOREIGN_CURRENCY_ID"));
		args.add(input.get("P_SERVICE_MASTER_ID"));
		args.add(bizcomponentDao.findCustomerTypeId("I")); // Individual
		args.add(input.get("P_FOREIGN_AMT"));

		String sqlQuery = " SELECT DISTINCT A.REMITTANCE_MODE_ID as P_REMITTANCE_MODE_ID ,A.DELIVERY_MODE_ID as P_DELIVERY_MODE_ID"
				+ "  FROM   V_EX_ROUTING_DETAILS_IMPS A , EX_BANK_SERVICE_RULE B,EX_BANK_CHARGES C"
				+ "  WHERE  A.APPLICATION_COUNTRY_ID =  ?" + "  AND    A.COUNTRY_ID             =  ?"
				+ "  AND    A.BENE_BANK_ID           =  ?" + "  AND    A.BENE_BANK_BRANCH_ID    =  ?"
				+ "  AND    A.ROUTING_COUNTRY_ID     =  ?" + "  AND    A.ROUTING_BANK_ID        =  ?    "
				+ "  AND    A.BANK_BRANCH_ID         =  ?" + "  AND      A.CURRENCY_ID            =  ?"
				+ "  AND    A.SERVICE_MASTER_ID      =  ?"
				+ "  AND    A.APPLICATION_COUNTRY_ID = B.APPLICATION_COUNTRY_ID"
				+ "  AND    A.ROUTING_COUNTRY_ID = B.COUNTRY_ID" + "  AND    A.CURRENCY_ID = B.CURRENCY_ID"
				+ "  AND    A.ROUTING_BANK_ID = B.BANK_ID" + "  AND    A.REMITTANCE_MODE_ID = B.REMITTANCE_MODE_ID"
				+ "  AND    A.DELIVERY_MODE_ID = B.DELIVERY_MODE_ID" + "  AND    A.REMITTANCE_MODE_ID       =  (select REMITTANCE_MODE_ID from ex_remittance_mode where REMITTANCE_CODE=13)     "
				+ "  AND    B.APPROVED_BY IS NOT NULL" + "  AND    B.BANK_SERVICE_RULE_ID = C.BANK_SERVICE_RULE_ID"
				+ "  AND    C.CHARGES_TYPE = 'C'" + "  AND    C.CHARGES_FOR   =   ? "
				+ "  AND    ? BETWEEN C.FROM_AMOUNT AND C.TO_AMOUNT";
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sqlQuery, args.toArray());
		} catch (Exception e) {
		}
		if (map == null || map.isEmpty()) {
			args.remove(args.size() - 2);
			args.add(args.size() - 1, new BigDecimal(777));
			map = jdbcTemplate.queryForMap(sqlQuery, args.toArray());
		}
		output.putAll(map);
		if (map != null && !map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	private BigDecimal getToAmount(BigDecimal routingCountryId, BigDecimal fcurrencyId, BigDecimal routingBankId) {
		return jdbcTemplate.queryForObject(
				" SELECT  B.TO_AMOUNT   " + " FROM    EX_BANK_SERVICE_RULE A," + "         EX_BANK_CHARGES B"
						+ " WHERE   A.BANK_SERVICE_RULE_ID  =   B.BANK_SERVICE_RULE_ID"
						+ " AND     A.COUNTRY_ID            =   ?" + " AND     A.CURRENCY_ID           =   ?"
						+ " AND     A.BANK_ID=   ?      " + " AND     A.REMITTANCE_MODE_ID    =   (select REMITTANCE_MODE_ID from ex_remittance_mode where REMITTANCE_CODE=13)"
						+ " AND    A.ISACTIVE           =   'Y'" + "AND    B.ISACTIVE           =   'Y' "
						+ " AND    B.CHARGES_TYPE          =   'C'",
				BigDecimal.class, routingCountryId, fcurrencyId, routingBankId);
	}

	@Override
	public boolean isApplicable() {
		return true;
	}

}
