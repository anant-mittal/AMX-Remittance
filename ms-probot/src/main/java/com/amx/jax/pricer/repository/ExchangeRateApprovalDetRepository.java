package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET;
import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModel;

@Transactional
public interface ExchangeRateApprovalDetRepository extends CrudRepository<ExchangeRateApprovalDetModel, BigDecimal> {

	@Query("select rate from ExchangeRateApprovalDetModel rate where  rate.currencyId=?1 and rate.countryBranchId=?2 "
			+ "and rate.bankMaster.recordStatus = 'Y' and rate.bankMaster.bankCode not in ('SCB','SCBUK', 'WU') and rate.bankMaster.bankId in (?3)")
	List<ExchangeRateApprovalDetModel> getExchangeRates(BigDecimal currencyId, BigDecimal countryBranchId,
			List<BigDecimal> bankIds);

	@Query("select rate from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 and rate.countryBranchId=?2 "
			+ "and rate.countryId=?3 and rate.applicationCountryId=?4 and rate.bankMaster.bankId=?5 and rate.serviceId=?6")
	List<ExchangeRateApprovalDetModel> getExchangeRatesForRoutingBank(BigDecimal currencyId, BigDecimal countryBranchId,
			BigDecimal countryId, BigDecimal applicationCountryId, BigDecimal routingBankId,
			BigDecimal serviceIndicatorId);

	@Query("select rate from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 and rate.bankMaster.bankId=?2 ")
	List<ExchangeRateApprovalDetModel> getExchangeRatesPlaceorder(BigDecimal currency, BigDecimal bankId);

	@Query("select rate from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 and rate.countryBranchId=?2 "
			+ "and rate.countryId=?3 and rate.applicationCountryId=?4 and rate.bankMaster.bankId in (?5)")
	List<ExchangeRateApprovalDetModel> getBranchExchangeRatesForRoutingBanks(BigDecimal currencyId,
			BigDecimal countryBranchId, BigDecimal countryId, BigDecimal applicationCountryId,
			List<BigDecimal> routingBankIds);

	@Query("select rate from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 "
			+ "and rate.countryId=?2 and rate.applicationCountryId=?3 and rate.bankMaster.bankId in (?4)")
	List<ExchangeRateApprovalDetModel> getExchangeRatesForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, List<BigDecimal> routingBankIds);

	@Query("select distinct rate.sellRateMin from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 "
			+ "and rate.countryId=?2 and rate.applicationCountryId=?3 and rate.bankMaster.bankId=?4")
	List<BigDecimal> getUniqueSellRatesMinForRoutingBank(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, BigDecimal routingBankId);

	////@formatter:off

	/*@Query("select new com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET(isActive, sellRateMin, sellRateMax, serviceId, " 
			+ "	bankMaster) from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 "
			+ " and rate.countryId=?2 and rate.applicationCountryId=?3 and rate.bankMaster.bankId in (?4) "
			+ " and rate.serviceId in (?5) "
			+ " group by rate.isActive, rate.sellRateMin, rate.sellRateMax, rate.serviceId, rate.bankMaster"
			+ " order by rate.bankMaster, rate.serviceId")
	List<ExchangeRateAPRDET> getSellRatesForRoutingBanks(BigDecimal currencyId, BigDecimal countryId,
			BigDecimal applicationCountryId, List<BigDecimal> routingBankIds, List<BigDecimal> serviceIds);*/

	/*
	 * Old Query
	 
	@Query("select new com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET(isActive, sellRateMin, sellRateMax, serviceId, "
			+ "	bankMaster) from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 "
			+ " and rate.applicationCountryId=?2 and rate.bankMaster.bankId in (?3) " + " and rate.serviceId in (?4) "
			+ " group by rate.isActive, rate.sellRateMin, rate.sellRateMax, rate.serviceId, rate.bankMaster"
			+ " order by rate.bankMaster, rate.serviceId")
	List<ExchangeRateAPRDET> getSellRatesForRoutingBanks(BigDecimal currencyId, BigDecimal applicationCountryId,
			List<BigDecimal> routingBankIds, List<BigDecimal> serviceIds);
	*/
	
	// @formatter:on

	/**
	 * New Query
	 */
	@Query("select new com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET(isActive, sellRateMin, sellRateMax, serviceId, "
			+ "	bankMaster) from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 "
			+ " and rate.applicationCountryId=?2 and rate.bankMaster.bankId in (?3) " + " and rate.serviceId in (?4) "
			+ " and rate.isActive='Y'"
			+ " and rate.countryBranchId in (select countryBranchId from CountryBranch cb where cb.isActive='Y')"
			+ " group by rate.isActive, rate.sellRateMin, rate.sellRateMax, rate.serviceId, rate.bankMaster"
			+ " order by rate.bankMaster, rate.serviceId")
	List<ExchangeRateAPRDET> getSellRatesForRoutingBanks(BigDecimal currencyId, BigDecimal applicationCountryId,
			List<BigDecimal> routingBankIds, List<BigDecimal> serviceIds);

}
