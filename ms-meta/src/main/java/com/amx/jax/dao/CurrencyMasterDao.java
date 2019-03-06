package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.repository.CurrencyRepository;

@Component
public class CurrencyMasterDao {

	@Autowired
	private CurrencyRepository repo;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CurrencyMasterModel getCurrencyMasterById(BigDecimal id) {
		return repo.findOne(id);
	}

	public CurrencyMasterModel getCurrencyMasterByQuote(String quoteName) {
		return repo.findByquoteName(quoteName).get(0);
	}

	public Map<BigDecimal, CurrencyMasterModel> getAllCurrencyMap() {
		List<CurrencyMasterModel> list = repo.findAll();
		return list.stream().collect(Collectors.toMap(CurrencyMasterModel::getCurrencyId, c -> c));
	}
	
	/**
	 * @author Chetan Pawar
	 * @param applicationCountryId
	 * @param beneCountryId
	 * @param serviceGroupId
	 * @param routingBankId
	 * @return List<BigDecimal>
	 */
	@Transactional
	public List<BigDecimal> getCashCurrencyList(BigDecimal applicationCountryId, BigDecimal beneCountryId,
			BigDecimal serviceGroupId, BigDecimal routingBankId) {
		String sql = "select DISTINCT(CURRENCY_ID) from V_EX_ROUTING_AGENTS where APPLICATION_COUNTRY_ID=? and "
				+ " ROUTING_COUNTRY_ID=? and SERVICE_GROUP_ID=? and ROUTING_BANK_ID=? ";
		List<BigDecimal> outputList = jdbcTemplate.queryForList(sql,
				new Object[] { applicationCountryId, beneCountryId, serviceGroupId, routingBankId }, BigDecimal.class);
		return outputList;
	}
	
	public Map<BigDecimal, CurrencyMasterModel> getSelectedCurrencyMap(List<String> currencyId) {
		List<CurrencyMasterModel> list = repo.fetchCurrencyMaster(currencyId);
		return list.stream().collect(Collectors.toMap(CurrencyMasterModel::getCurrencyId, c -> c));
	}

}
