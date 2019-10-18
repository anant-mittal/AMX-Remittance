package com.amx.jax.pricer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.DiscountMasterDao;
import com.amx.jax.pricer.dao.GroupingMasterDao;
import com.amx.jax.pricer.dao.MarginMarkupDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDaoAlt;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.DiscountMaster;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dto.AmountSlabDetails;
import com.amx.jax.pricer.dto.ChannelDetails;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
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
	RoutingDaoAlt routingDaoAlt;

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

	@Autowired
	CurrencyMasterDao currencyMasterDao;

	@Autowired
	MarginMarkupDao marginMarkupDao;

	// @Autowired
	// private ProbotMetaInfo metaInfo;

	private static BigDecimal OnlineCountryBranchId;

	public DiscountDetailsReqRespDTO getDiscountManagementData(DiscountMgmtReqDTO discountMgmtReqDTO) {

		DiscountDetailsReqRespDTO discountMgmtRespDTO = new DiscountDetailsReqRespDTO();

		List<GroupingMaster> groupingMaster = groupingMasterDao.getAllGroup();
		List<GroupDetails> groupInfo = discountManager.convertGroupInfo(groupingMaster);
		discountMgmtRespDTO.setCurGroupDetails(groupInfo);

		Map<BigDecimal, List<ChannelDetails>> curGrpChannelDetails = new HashMap<BigDecimal, List<ChannelDetails>>();
		Map<BigDecimal, List<CustomerCategoryDetails>> curGrpCustCatDetails = new HashMap<BigDecimal, List<CustomerCategoryDetails>>();

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CHANNEL)) {
			for (GroupingMaster groupList : groupingMaster) {
				GroupDetails groupDetails = new GroupDetails();
				groupDetails.setGroupId(groupList.getId());

				List<DiscountMaster> chDiscMaster = discountMasterDao
						.getByDiscountTypeAndGroupId(DISCOUNT_TYPE.CHANNEL.getTypeKey(), groupList.getId());
				if (null != chDiscMaster) {
					List<ChannelDetails> channelGroupingData = discountManager.convertChannelGroupingData(chDiscMaster,
							groupList.getId());
					if (null != channelGroupingData) {
						curGrpChannelDetails.put(groupList.getId(), channelGroupingData);

						discountMgmtRespDTO.setCurGrpChannelDetails(curGrpChannelDetails);
					}
				}
			}
		}

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CUSTOMER_CATEGORY)) {
			for (GroupingMaster groupList : groupingMaster) {
				GroupDetails groupDetails = new GroupDetails();
				groupDetails.setGroupId(groupList.getId());

				List<DiscountMaster> custCatDiscMaster = discountMasterDao
						.getByDiscountTypeAndGroupId(DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey(), groupList.getId());
				if (null != custCatDiscMaster) {
					List<CustomerCategoryDetails> custCatGroupingData = discountManager
							.convertCustCatGroupingData(custCatDiscMaster, groupList.getId());
					if (null != custCatGroupingData) {
						curGrpCustCatDetails.put(groupList.getId(), custCatGroupingData);

						discountMgmtRespDTO.setCurGrpCustCatDetails(curGrpCustCatDetails);
					}
				}
			}
		}

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.AMOUNT_SLAB)) {
			if (null != discountMgmtReqDTO.getCountryId() && null != discountMgmtReqDTO.getCurrencyId()
					&& null != discountMgmtReqDTO.getBankId()) {

				if (OnlineCountryBranchId == null) {
					CountryBranch cb = countryBranchDao.getOnlineCountryBranch();
					OnlineCountryBranchId = cb.getCountryBranchId();
				}

				List<PipsMaster> pipsMasterData = pipsMasterDao.getAmountSlab(discountMgmtReqDTO.getCountryId(),
						discountMgmtReqDTO.getCurrencyId(), OnlineCountryBranchId, discountMgmtReqDTO.getBankId());
				List<AmountSlabDetails> amountSlabData = discountManager.convertAmountSlabData(pipsMasterData);

				discountMgmtRespDTO.setAmountSlabDetails(amountSlabData);
			}

		}

		return discountMgmtRespDTO;
	}

	public List<RoutBanksAndServiceRespDTO> getRoutBanksAndServices(BigDecimal countryId, BigDecimal currencyId) {
		List<RoutingHeader> allRountingHeaderData = routingDaoAlt.getRoutHeadersByCountryIdAndCurrenyId(countryId,
				currencyId);

		List<RoutingHeader> rountingHeaderData = new ArrayList<RoutingHeader>();

		BigDecimal oneOOne = new BigDecimal(101);
		BigDecimal oneOTwo = new BigDecimal(102);

		for (RoutingHeader routingHeader : allRountingHeaderData) {

			BigDecimal serviceId = routingHeader.getServiceMasterId();

			if (null != serviceId && (oneOOne.compareTo(serviceId) == 0 || oneOTwo.compareTo(serviceId) == 0)) {
				rountingHeaderData.add(routingHeader);
			}
		}

		if (rountingHeaderData.isEmpty()) {
			throw new PricerServiceException(PricerServiceError.MISSING_ROUTING_BANK_IDS,
					"Invalid or Missing Routing Banks");
		}

		List<RoutBanksAndServiceRespDTO> routBanksAndServiceRespDTO = discountManager
				.convertRoutBankAndService(rountingHeaderData);
		return routBanksAndServiceRespDTO;
	}

	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(
			DiscountDetailsReqRespDTO discountdetailsRequestDTO) {
		if (null != discountdetailsRequestDTO.getChannelDetails()) {
			discountManager.commitChannelDiscountModel(discountdetailsRequestDTO.getChannelDetails());
		}

		if (null != discountdetailsRequestDTO.getCustomerCategoryDetails()) {
			discountManager.commitCustomerDiscountModel(discountdetailsRequestDTO.getCustomerCategoryDetails());
		}

		if (null != discountdetailsRequestDTO.getAmountSlabDetails()) {
			discountManager.commitPipsDiscount(discountdetailsRequestDTO.getAmountSlabDetails());

		}

		return AmxApiResponse.build();
	}

	public List<GroupDetails> getGroupInfoForCurrency() {

		List<GroupingMaster> groupingMaster = groupingMasterDao.getGroupForCurrency();
		List<GroupDetails> groupInfo = discountManager.convertGroupInfo(groupingMaster);
		Collections.sort(groupInfo);

		return groupInfo;
	}

	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(BigDecimal groupId, BigDecimal currencyId) {
		discountManager.commitCurrencyGroupId(groupId, currencyId);

		return AmxApiResponse.build();
	}

	public List<CurrencyMasterDTO> getCurrencyByGroupId(BigDecimal groupId) {
		List<CurrencyMasterModel> currencyByGrId = currencyMasterDao.getCurrencyByGroupId(groupId);
		List<CurrencyMasterDTO> currencyData = discountManager.convertCurrencyData(currencyByGrId);
		return currencyData;
	}

	public OnlineMarginMarkupInfo getOnlineMarginMarkupData(OnlineMarginMarkupReq request) {
		OnlineMarginMarkup marginMarkupData = marginMarkupDao.getMarkupData(request.getApplicationCountryId(),
				request.getCountryId(), request.getCurrencyId(), request.getBankId());
		OnlineMarginMarkupInfo marginMarkupInfo;
		if (marginMarkupData != null) {
			marginMarkupInfo = discountManager.convertMarkup(marginMarkupData);
		} else {
			marginMarkupInfo = new OnlineMarginMarkupInfo();
		}

		return marginMarkupInfo;
	}

	public BoolRespModel saveOnlineMarginMarkupData(OnlineMarginMarkupInfo request) {
		OnlineMarginMarkup marginMarkupData = marginMarkupDao.getMarkupData(request.getApplicationCountryId(),
				request.getCountryId(), request.getCurrencyId(), request.getBankId());
		Boolean resp = discountManager.commitMarkup(marginMarkupData, request);
		if (resp == true)
			return new BoolRespModel(Boolean.TRUE);
		else
			return new BoolRespModel(Boolean.FALSE);

	}

}
