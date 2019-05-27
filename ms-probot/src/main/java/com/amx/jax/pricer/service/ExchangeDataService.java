package com.amx.jax.pricer.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.DiscountMasterDao;
import com.amx.jax.pricer.dao.GroupingMasterDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDao;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.DiscountMaster;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dto.AmountSlabDetails;
import com.amx.jax.pricer.dto.ChannelDetails;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
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
	BankMasterDao bankMasterDao;
	
	@Autowired
	ServiceMasterDescDao serviceMasterDescDao;
	
	@Autowired
	DiscountManager discountManager;
	
	@Autowired
	CountryBranchDao countryBranchDao;
	
	@Autowired
	GroupingMasterDao groupingMasterDao;
	
	@Autowired
	DiscountMasterDao discountMasterDao;
	
	private static BigDecimal OnlineCountryBranchId;

	public DiscountDetailsReqRespDTO getDiscountManagementData(DiscountMgmtReqDTO discountMgmtReqDTO) {

		DiscountDetailsReqRespDTO discountMgmtRespDTO = new DiscountDetailsReqRespDTO();

		List<GroupingMaster> groupingMaster = groupingMasterDao.getAllGroup();
		List<GroupDetails> groupInfo = discountManager.convertGroupInfo(groupingMaster);
		discountMgmtRespDTO.setCurGroupDetails(groupInfo);
		
		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CHANNEL)) {
			for (GroupingMaster groupList : groupingMaster) {
				GroupDetails groupDetails = new GroupDetails();
				groupDetails.setGroupId(groupList.getId());
				
				List<DiscountMaster> chDiscMaster = 
						discountMasterDao.getByDiscountTypeAndGroupId(DISCOUNT_TYPE.CHANNEL.getTypeKey(), groupList.getId());
				if(null != chDiscMaster) {
					List<ChannelDetails> channelGroupingData = discountManager.convertChannelGroupingData(chDiscMaster, groupList.getId());
					if(null != channelGroupingData) {
						Map<BigDecimal, List<ChannelDetails>> curGrpChannelDetails = 
								discountManager.convertGrpChannel(groupList.getId(), channelGroupingData);
						
						discountMgmtRespDTO.setCurGrpChannelDetails(curGrpChannelDetails);
					}
				}
			}
		}

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CUSTOMER_CATEGORY)) {
			for (GroupingMaster groupList : groupingMaster) {
				GroupDetails groupDetails = new GroupDetails();
				groupDetails.setGroupId(groupList.getId());
				
				List<DiscountMaster> custCatDiscMaster = 
						discountMasterDao.getByDiscountTypeAndGroupId(DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), groupList.getId());
				if(null != custCatDiscMaster) {
					List<CustomerCategoryDetails> custCatGroupingData = discountManager.convertCustCatGroupingData(custCatDiscMaster, groupList.getId());
					if(null != custCatGroupingData) {
						Map<BigDecimal, List<CustomerCategoryDetails>> curGrpCustCatDetails = 
								discountManager.convertGrpCustCat(groupList.getId(), custCatGroupingData);
						
						discountMgmtRespDTO.setCurGrpCustCatDetails(curGrpCustCatDetails);
					}
				}
			}
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
		List<RoutingHeader> rountingHeaderData = routingDao.getRoutHeadersByCountryIdAndCurrenyId(countryId, currencyId);
		if (rountingHeaderData.isEmpty()) {
			throw new PricerServiceException("Routing details not avaliable");
		}
		List<RoutBanksAndServiceRespDTO> routBanksAndServiceRespDTO = discountManager.convertRoutBankAndService(rountingHeaderData);
		return routBanksAndServiceRespDTO;
	}

	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(DiscountDetailsReqRespDTO discountdetailsRequestDTO) {
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
