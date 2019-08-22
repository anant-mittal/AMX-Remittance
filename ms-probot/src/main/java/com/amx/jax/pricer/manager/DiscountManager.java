package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CurrencyMasterDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.DiscountMasterDao;
import com.amx.jax.pricer.dao.MarginMarkupDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDaoAlt;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.DiscountMaster;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.dto.AmountSlabDetails;
import com.amx.jax.pricer.dto.ChannelDetails;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.meta.ProbotMetaInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

@Component
public class DiscountManager {

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
	DiscountMasterDao discountMasterDao;
	
	@Autowired
	CurrencyMasterDao currencyMasterDao;
	
	@Autowired
	MarginMarkupDao marginMarkupDao;
	
	@Autowired
	private ProbotMetaInfo metaInfo;

	// ------ To get Discount details Start here ------
	public List<ChannelDetails> convertChannelData(List<ChannelDiscount> channelDiscount, BigDecimal groupId) {
		List<ChannelDetails> list = new ArrayList<>();
		
		for (ChannelDiscount discList : channelDiscount) {
			ChannelDetails channelDetail = new ChannelDetails();
			channelDetail.setChannelId(discList.getId());
			channelDetail.setChannel(discList.getChannel());
			channelDetail.setIsActive(discList.getIsActive());

			list.add(channelDetail);
		}

		return list;
	}

	public List<CustomerCategoryDetails> convertCustCategoryData(List<CustomerCategoryDiscount> custCatDiscount) {
		List<CustomerCategoryDetails> list = new ArrayList<>();

		for (CustomerCategoryDiscount custDiscList : custCatDiscount) {
			CustomerCategoryDetails custCategoryDetail = new CustomerCategoryDetails();
			custCategoryDetail.setCustCatId(custDiscList.getId());
			custCategoryDetail.setCustomerCategory(custDiscList.getCustomerCategory());
			custCategoryDetail.setIsActive(custDiscList.getIsActive());

			list.add(custCategoryDetail);
		}
		Collections.sort(list);
		return list;
	}

	public List<AmountSlabDetails> convertAmountSlabData(List<PipsMaster> pipsMasterData) {
		List<AmountSlabDetails> list = new ArrayList<>();
		
		for (PipsMaster pipsMasterList : pipsMasterData) {
			
			AmountSlabDetails amountSlabDetail = new AmountSlabDetails();
			amountSlabDetail.setPipsMasterId(pipsMasterList.getPipsMasterId());
			amountSlabDetail.setFromAmount(pipsMasterList.getFromAmount());
			amountSlabDetail.setToAmount(pipsMasterList.getToAmount());
			amountSlabDetail.setDiscountPips(pipsMasterList.getPipsNo());
			if (null != pipsMasterList.getCurrencyMaster()) {
				amountSlabDetail.setCurrencyId(pipsMasterList.getCurrencyMaster().getCurrencyId());
				amountSlabDetail.setCurrencyName(pipsMasterList.getCurrencyMaster().getQuoteName());
			}
			if (null != pipsMasterList.getBankMaster()) {
				amountSlabDetail.setBankId(pipsMasterList.getBankMaster().getBankId());
				amountSlabDetail.setBankName(pipsMasterList.getBankMaster().getBankShortName());
			}
			if (null != pipsMasterList.getCountryMaster()) {
				if (null != pipsMasterList.getCountryMaster().getFsCountryMasterDescs()) {
					amountSlabDetail.setCountryId(pipsMasterList.getCountryMaster().getCountryId());
					amountSlabDetail.setCountryName(
							pipsMasterList.getCountryMaster().getFsCountryMasterDescs().get(0).getCountryName());
				}
			}
			amountSlabDetail.setMinDiscountPips(pipsMasterList.getMinDiscountPips());
			amountSlabDetail.setMaxDiscountPips(pipsMasterList.getMaxDiscountPips());
			list.add(amountSlabDetail);
		
		}
		return list;
	}

	// ------ To get Routing Bank and Services Start here ------
	public List<RoutBanksAndServiceRespDTO> convertRoutBankAndService(List<RoutingHeader> rountingHeaderData) {
		List<RoutBanksAndServiceRespDTO> list = new ArrayList<>();
		for (RoutingHeader routingData : rountingHeaderData) {
			RoutBanksAndServiceRespDTO routBanksAndServiceRespData = new RoutBanksAndServiceRespDTO();
			routBanksAndServiceRespData.setRoutingBankId(routingData.getRoutingBankId());

			BankMasterModel bankModel = bankMasterDao.getBankById(routingData.getRoutingBankId());
			if (null != bankModel) {
				routBanksAndServiceRespData.setRoutingBankName(bankModel.getBankFullName());
			}

			routBanksAndServiceRespData.setServiceId(routingData.getServiceMasterId());

			ServiceMasterDesc serviceName = serviceMasterDescDao.getServiceById(routingData.getServiceMasterId());
			if (null != serviceName) {
				routBanksAndServiceRespData.setServiceDesc(serviceName.getServiceDesc());
			}

			list.add(routBanksAndServiceRespData);
		}
		return list;
	}

	// ------ To save all discount pips data Start here ------
	public void commitChannelDiscountModel(List<ChannelDetails> channelDetails) {

		List<ChannelDetails> channelData = convertchannelRequest(channelDetails);
		List<DiscountMaster> list = new ArrayList<>();
		for (ChannelDetails channelUpdate : channelData) {
			if(null != channelUpdate.getDiscountId() && null != channelUpdate.getGroupId()) {
				DiscountMaster discChannelByIdAndGroupId = 
						discountMasterDao.getByDiscountIdAndGroupId(channelUpdate.getDiscountId(), channelUpdate.getGroupId(), DISCOUNT_TYPE.CHANNEL.getTypeKey());

				if(null != discChannelByIdAndGroupId) {
					if (null != channelUpdate.getDiscountPips()) {
						if (channelUpdate.getDiscountPips().doubleValue() >= discChannelByIdAndGroupId.getMinDiscountPips().doubleValue()
								&& channelUpdate.getDiscountPips().doubleValue() <= discChannelByIdAndGroupId.getMaxDiscountPips()
										.doubleValue()) {
							discChannelByIdAndGroupId.setDiscountPips(channelUpdate.getDiscountPips());
						} else {
							throw new PricerServiceException(PricerServiceError.INVALID_CHANNEL_DISC_PIPS,
									"Channel Discount Pips must be In between Min-Max Discount Pips");
						}
					}

					if (null != channelUpdate.getIsActive()) {
						discChannelByIdAndGroupId.setIsActive(channelUpdate.getIsActive());
					}

					list.add(discChannelByIdAndGroupId);
				}
			}else {
				throw new PricerServiceException(PricerServiceError.MISSING_DISCOUNT_OR_GROUP_ID,
						"Either DiscountId or GroupId is missing for Channel");
			}
		}
		discountMasterDao.saveDiscountForChannel(list);
	}

	private List<ChannelDetails> convertchannelRequest(List<ChannelDetails> channelDetails) {
		List<ChannelDetails> list = new ArrayList<>();

		for (ChannelDetails dto : channelDetails) {
			ChannelDetails channelData = new ChannelDetails();
			channelData.setGroupId(dto.getGroupId());
			channelData.setDiscountId(dto.getDiscountId());
			channelData.setDiscountPips(dto.getDiscountPips());
			channelData.setIsActive(dto.getIsActive());
			channelData.setChannelId(dto.getChannelId());

			list.add(channelData);
		}
		return list;
	}

	public void commitCustomerDiscountModel(List<CustomerCategoryDetails> customerCategoryDetails) {
		List<CustomerCategoryDetails> customerDiscountData = convertCustomerDiscountRequest(customerCategoryDetails);
		List<DiscountMaster> list = new ArrayList<>();
		for (CustomerCategoryDetails custCatUpdate : customerDiscountData) {
			if(null != custCatUpdate.getDiscountId() && null != custCatUpdate.getGroupId()) {
				DiscountMaster discCustCatByIdAndGroupId = 
						discountMasterDao.getByDiscountIdAndGroupId(custCatUpdate.getDiscountId(), custCatUpdate.getGroupId(), DISCOUNT_TYPE.CUSTOMER_CATEGORY.getTypeKey()); 
				if(null != discCustCatByIdAndGroupId) {
					if (null != custCatUpdate.getDiscountPips()) {
						if (custCatUpdate.getDiscountPips().doubleValue() >= discCustCatByIdAndGroupId.getMinDiscountPips().doubleValue()
								&& custCatUpdate.getDiscountPips().doubleValue() <= discCustCatByIdAndGroupId.getMaxDiscountPips()
										.doubleValue()) {
							discCustCatByIdAndGroupId.setDiscountPips(custCatUpdate.getDiscountPips());
						} else {
							throw new PricerServiceException(PricerServiceError.INVALID_CUST_CAT_DISC_PIPS,
									"Customer Category Discount Pips must be In between Min-Max Discount Pips");
						}
					}
					if (null != custCatUpdate.getIsActive()) {
						discCustCatByIdAndGroupId.setIsActive(custCatUpdate.getIsActive());
					}

					list.add(discCustCatByIdAndGroupId);
				}
			
			}else {
				throw new PricerServiceException(PricerServiceError.MISSING_DISCOUNT_OR_GROUP_ID,
						"Either DiscountId or GroupId is missing for Customer Category");
			}
		}
		discountMasterDao.saveDiscountForCustomerCategory(list);
	}

	private List<CustomerCategoryDetails> convertCustomerDiscountRequest(List<CustomerCategoryDetails> customerCategoryDetails) {
		List<CustomerCategoryDetails> list = new ArrayList<>();
		for (CustomerCategoryDetails dto : customerCategoryDetails) {
			CustomerCategoryDetails customerCategoryData = new CustomerCategoryDetails();
			customerCategoryData.setGroupId(dto.getGroupId());
			customerCategoryData.setDiscountId(dto.getDiscountId());
			customerCategoryData.setDiscountPips(dto.getDiscountPips());
			customerCategoryData.setIsActive(dto.getIsActive());
			customerCategoryData.setCustCatId(dto.getCustCatId());

			list.add(customerCategoryData);
		}
		return list;
	}

	public void commitPipsDiscount(List<AmountSlabDetails> amountSlabDetails) {
		List<AmountSlabDetails> pipsData = convertPipsRequest(amountSlabDetails);
		List<PipsMaster> list = new ArrayList<>();
		for (AmountSlabDetails pipsUpdate : pipsData) {
			PipsMaster pipsByMasterId = pipsMasterDao.getPipsById(pipsUpdate.getPipsMasterId());
			if (null != pipsUpdate.getDiscountPips()) {
				if (pipsUpdate.getDiscountPips().doubleValue() >= pipsByMasterId.getMinDiscountPips().doubleValue()
						&& pipsUpdate.getDiscountPips().doubleValue() <= pipsByMasterId.getMaxDiscountPips()
								.doubleValue()) {
					pipsByMasterId.setPipsNo(pipsUpdate.getDiscountPips());
				} else {
					throw new PricerServiceException(PricerServiceError.INVALID_AMT_SLAB_DISC_PIPS,
							"Amount slab Discount Pips must be In between Min-Max Discount Pips");
				}
			}
			list.add(pipsByMasterId);
		}
		pipsMasterDao.savePipsForDiscount(list);
	}

	private List<AmountSlabDetails> convertPipsRequest(List<AmountSlabDetails> amountSlabDetails) {
		List<AmountSlabDetails> list = new ArrayList<>();
		for (AmountSlabDetails dto : amountSlabDetails) {
			AmountSlabDetails pipsData = new AmountSlabDetails();
			pipsData.setDiscountPips(dto.getDiscountPips());
			pipsData.setPipsMasterId(dto.getPipsMasterId());

			list.add(pipsData);
		}
		return list;
	}

	// ------ Currency Grouping changes ------
	public List<ChannelDetails> convertChannelGroupingData(List<DiscountMaster> chDiscMaster, BigDecimal groupId) {
		List<ChannelDetails> list = new ArrayList<>();
		
		for (DiscountMaster discChannelList : chDiscMaster) {
			
			ChannelDiscount channelData = channelDiscountDao.getDiscountById(discChannelList.getDiscountTypeId());
						
			ChannelDetails channelDetail = new ChannelDetails();
			channelDetail.setChannelId(channelData.getId());
			channelDetail.setChannel(channelData.getChannel());
			channelDetail.setDiscountId(discChannelList.getId());
			channelDetail.setGroupId(discChannelList.getGroupId());
			channelDetail.setDiscountTypeId(discChannelList.getDiscountTypeId());
			channelDetail.setDiscountPips(discChannelList.getDiscountPips());
			channelDetail.setMinDiscountPips(discChannelList.getMinDiscountPips());
			channelDetail.setMaxDiscountPips(discChannelList.getMaxDiscountPips());
			channelDetail.setIsActive(discChannelList.getIsActive());

			list.add(channelDetail);
		}

		return list;
	}
	
	public List<CustomerCategoryDetails> convertCustCatGroupingData(List<DiscountMaster> custCatDiscMaster, BigDecimal groupId) {
		List <CustomerCategoryDetails> list = new ArrayList<>();
		
		for(DiscountMaster discCustCatList : custCatDiscMaster) {
			CustomerCategoryDiscount custCatData = custCatDiscountDao.getCustCatDiscountById(discCustCatList.getDiscountTypeId());
			
			CustomerCategoryDetails customerCategoryDetails = new CustomerCategoryDetails();
			customerCategoryDetails.setCustCatId(custCatData.getId());
			customerCategoryDetails.setCustomerCategory(custCatData.getCustomerCategory());
			customerCategoryDetails.setDiscountId(discCustCatList.getId());
			customerCategoryDetails.setGroupId(discCustCatList.getGroupId());
			customerCategoryDetails.setDiscountTypeId(discCustCatList.getDiscountTypeId());
			customerCategoryDetails.setDiscountPips(discCustCatList.getDiscountPips());
			customerCategoryDetails.setMinDiscountPips(discCustCatList.getMinDiscountPips());
			customerCategoryDetails.setMaxDiscountPips(discCustCatList.getMaxDiscountPips());
			customerCategoryDetails.setIsActive(discCustCatList.getIsActive());
			
			list.add(customerCategoryDetails);
		}
		
		return list;
	}

	public Map<BigDecimal, List<ChannelDetails>> convertGrpChannel(BigDecimal groupId,
			List<ChannelDetails> channelData) {
		
		Map<BigDecimal, List<ChannelDetails>> grpChannelData = new HashMap<BigDecimal, List<ChannelDetails>>();
		grpChannelData.put(groupId, channelData);
		
		return grpChannelData;
	}
	
	public Map<BigDecimal, List<CustomerCategoryDetails>> convertGrpCustCat(BigDecimal groupId,
			List<CustomerCategoryDetails> custCatData) {
		
		Map<BigDecimal, List<CustomerCategoryDetails>> grpCustCatData = new HashMap<BigDecimal, List<CustomerCategoryDetails>>();
		grpCustCatData.put(groupId, custCatData);
		
		return grpCustCatData;
	}

	public List<GroupDetails> convertGroupInfo(List<GroupingMaster> groupingMaster) {
		List<GroupDetails> list = new ArrayList<>();
		
		for (GroupingMaster groupList : groupingMaster) {
			GroupDetails groupDetails = new GroupDetails();
			groupDetails.setGroupId(groupList.getId());
			groupDetails.setGroupName(groupList.getGroupName());
			groupDetails.setGroupType(groupList.getGroupType());
			groupDetails.setIsActive(groupList.getIsActive());
			
			list.add(groupDetails);
		}
		return list;
	}

	public void commitCurrencyGroupId(BigDecimal groupId, BigDecimal currencyId) {
		CurrencyMasterModel currencyById = currencyMasterDao.getByCurrencyId(currencyId);
		
		currencyById.setCurrGroupId(groupId);
		currencyMasterDao.updateCurrencyGroupId(currencyById);
	}

	public List<CurrencyMasterDTO> convertCurrencyData(List<CurrencyMasterModel> currencyByGrId) {
		List<CurrencyMasterDTO> list = new ArrayList<>();
		
		for(CurrencyMasterModel currencyList : currencyByGrId) {
			CurrencyMasterDTO currencyMasterDTO = new CurrencyMasterDTO();
			currencyMasterDTO.setCurrencyId(currencyList.getCurrencyId());
			currencyMasterDTO.setCurrencyCode(currencyList.getCurrencyCode());
			currencyMasterDTO.setQuoteName(currencyList.getQuoteName());
			currencyMasterDTO.setCurrencyName(currencyList.getCurrencyName());
			currencyMasterDTO.setCurrGroupId(currencyList.getCurrGroupId());
			
			list.add(currencyMasterDTO);
		}
		return list;
	}
	
	public OnlineMarginMarkupInfo convertMarkup(OnlineMarginMarkup onlineMarginMarkup ) {
		OnlineMarginMarkupInfo markupDetails=new OnlineMarginMarkupInfo();
			markupDetails.setBankId(onlineMarginMarkup.getBankId());
			markupDetails.setMarginMarkup(onlineMarginMarkup.getMarginMarkup());
			markupDetails.setCountryId(onlineMarginMarkup.getCountryId());
			markupDetails.setCurrencyId(onlineMarginMarkup.getCurrencyId());
			markupDetails.setOnlineMarginMarkupId(onlineMarginMarkup.getOnlineMarginMarkupId());
			markupDetails.setEmpName(onlineMarginMarkup.getCreatedBy());
			return markupDetails;
		
		
	}
	public Boolean commitMarkup(OnlineMarginMarkup marginMarkupData,OnlineMarginMarkupInfo request) {
		if (marginMarkupData.getOnlineMarginMarkupId()!=null) 
		 {
			marginMarkupData.setMarginMarkup(request.getMarginMarkup());
			marginMarkupData.setModifiedDate(new Date());
			marginMarkupData.setModifiedBy(request.getEmpName());
			marginMarkupDao.saveOnlineMarginMarkup(marginMarkupData);
			return true;

		 }
		 else {
			 OnlineMarginMarkup onlineMarginMarkup=new OnlineMarginMarkup();
			 onlineMarginMarkup.setCountryId(request.getCountryId());
			 onlineMarginMarkup.setCurrencyId(request.getCurrencyId());
			 onlineMarginMarkup.setIsActive("Y");
			 onlineMarginMarkup.setCreatedDate(new Date());
			 onlineMarginMarkup.setModifiedDate(new Date());
			 onlineMarginMarkup.setModifiedBy(request.getEmpName());
			 onlineMarginMarkup.setCreatedBy(request.getEmpName());
			 onlineMarginMarkup.setApplicationCountryId(metaInfo.getCountryId());
			marginMarkupDao.saveOnlineMarginMarkup(onlineMarginMarkup);
			return true;

		 }
	}
	
}
