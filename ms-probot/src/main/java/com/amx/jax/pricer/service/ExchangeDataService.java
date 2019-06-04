package com.amx.jax.pricer.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDao;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dto.AmountSlabDetails;
import com.amx.jax.pricer.dto.ChannelDetails;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.manager.DiscountManager;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

@Service
public class ExchangeDataService {

	@Autowired
	ChannelDiscountDao channelDiscountDao;

	@Autowired
	CustCatDiscountDao custCatDiscountDao;

	@Autowired
	PipsMasterDao pipsMasterDao;
	
	@Autowired
	RoutingDao routingDao;

	@Autowired
	DiscountManager discountManager;
	
	@Autowired
	CountryBranchDao countryBranchDao;
	
	private static BigDecimal OnlineCountryBranchId;

	public DiscountDetailsReqRespDTO getDiscountManagementData(DiscountMgmtReqDTO discountMgmtReqDTO) {

		DiscountDetailsReqRespDTO discountMgmtRespDTO = new DiscountDetailsReqRespDTO();

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CHANNEL)) {
			List<ChannelDiscount> channelDiscount = channelDiscountDao.getDiscountForAllChannel();
			List<ChannelDetails> channelData = discountManager.convertChannelData(channelDiscount);
			discountMgmtRespDTO.setChannelDetails(channelData);
		}

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CUSTOMER_CATEGORY)) {
			List<CustomerCategoryDiscount> custCatDiscount = custCatDiscountDao.getDiscountForAllCustCategory();
			List<CustomerCategoryDetails> custCategoryData = discountManager.convertCustCategoryData(custCatDiscount);
			discountMgmtRespDTO.setCustomerCategoryDetails(custCategoryData);
		}

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.AMOUNT_SLAB)) {
			if (null != discountMgmtReqDTO.getCountryId() && null != discountMgmtReqDTO.getCurrencyId()) {
				CountryBranch cb = countryBranchDao.getOnlineCountryBranch();
				OnlineCountryBranchId = cb.getCountryBranchId();
				
				List<PipsMaster> pipsMasterData = pipsMasterDao.getAmountSlab(discountMgmtReqDTO.getCountryId(),
						discountMgmtReqDTO.getCurrencyId(), OnlineCountryBranchId);
				List<AmountSlabDetails> amountSlabData = discountManager.convertAmountSlabData(pipsMasterData);
				discountMgmtRespDTO.setAmountSlabDetails(amountSlabData);
			}
		}

		return discountMgmtRespDTO;
	}

	public List<RoutBanksAndServiceRespDTO> getRoutBanksAndServices(BigDecimal countryId, BigDecimal currencyId) {
		List<RoutingHeader> rountingHeaderData = routingDao.getRoutHeadersByCountryIdAndCurrenyId(countryId,
				currencyId);
		if (rountingHeaderData.isEmpty()) {
			// throw new PricerServiceException("Routing details not avaliable");

			throw new PricerServiceException(PricerServiceError.INVALID_ROUTING_BANK_IDS,
					"Routing details not avaliable");

		}
		List<RoutBanksAndServiceRespDTO> routBanksAndServiceRespDTO = discountManager
				.convertRoutBankAndService(rountingHeaderData);
		return routBanksAndServiceRespDTO;
	}

	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(DiscountDetailsReqRespDTO discountdetailsRequestDTO) {
		//DiscountDetailsReqRespDTO discountDetailsResponseDTO = new DiscountDetailsReqRespDTO();
		
		if(null != discountdetailsRequestDTO.getChannelDetails()) {
			discountManager.commitChannelDiscountModel(discountdetailsRequestDTO.getChannelDetails());
		}
		if(null != discountdetailsRequestDTO.getCustomerCategoryDetails()) {
			discountManager.commitCustomerDiscountModel(discountdetailsRequestDTO.getCustomerCategoryDetails());
		}
		if(null != discountdetailsRequestDTO.getAmountSlabDetails()) {
			discountManager.commitPipsDiscount(discountdetailsRequestDTO.getAmountSlabDetails());
		}
		
		return AmxApiResponse.build();
	}

}
