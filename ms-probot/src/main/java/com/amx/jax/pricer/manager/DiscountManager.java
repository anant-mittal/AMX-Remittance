package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.BankMasterDao;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.DiscountMasterDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDao;
import com.amx.jax.pricer.dao.ServiceMasterDescDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.DiscountMaster;
import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.dto.AmountSlabDetails;
import com.amx.jax.pricer.dto.ChannelDetails;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;

@Component
public class DiscountManager {

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
	DiscountMasterDao discountMasterDao;

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

			BankMasterModel bankName = bankMasterDao.getBankById(routingData.getRoutingBankId());
			if (null != bankName) {
				routBanksAndServiceRespData.setRoutingBankName(bankName.getBankFullName());
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
		List<ChannelDiscount> list = new ArrayList<>();
		for (ChannelDetails channelUpdate : channelData) {
			ChannelDiscount channelById = channelDiscountDao.getDiscountById(channelUpdate.getChannelId());

			/*if (null != channelUpdate.getDiscountPips()) {
				if (channelUpdate.getDiscountPips().doubleValue() >= channelById.getMinDiscountPips().doubleValue()
						&& channelUpdate.getDiscountPips().doubleValue() <= channelById.getMaxDiscountPips()
								.doubleValue()) {
					channelById.setDiscountPips(channelUpdate.getDiscountPips());
				} else {
					throw new PricerServiceException(PricerServiceError.INVALID_CHANNEL_DISC_PIPS,
							"Channel Discount Pips must be In between Min-Max Discount Pips");
				}
			}*/

			if (null != channelUpdate.getIsActive()) {
				channelById.setIsActive(channelUpdate.getIsActive());
			}

			list.add(channelById);
		}
		channelDiscountDao.saveDiscountForChannel(list);

	}

	private List<ChannelDetails> convertchannelRequest(List<ChannelDetails> channelDetails) {
		List<ChannelDetails> list = new ArrayList<>();

		for (ChannelDetails dto : channelDetails) {
			ChannelDetails channelData = new ChannelDetails();
			channelData.setDiscountPips(dto.getDiscountPips());
			channelData.setIsActive(dto.getIsActive());
			channelData.setChannelId(dto.getChannelId());

			list.add(channelData);
		}
		return list;
	}

	public void commitCustomerDiscountModel(List<CustomerCategoryDetails> customerCategoryDetails) {
		List<CustomerCategoryDetails> customerDiscountData = convertCustomerDiscountRequest(customerCategoryDetails);
		List<CustomerCategoryDiscount> list = new ArrayList<>();
		for (CustomerCategoryDetails custCatUpdate : customerDiscountData) {
			CustomerCategoryDiscount custCatById = custCatDiscountDao.getCustCatDiscountById(custCatUpdate.getCustCatId());
			/*if (null != custCatUpdate.getDiscountPips()) {
				if (custCatUpdate.getDiscountPips().doubleValue() >= custCatById.getMinDiscountPips().doubleValue()
						&& custCatUpdate.getDiscountPips().doubleValue() <= custCatById.getMaxDiscountPips()
								.doubleValue()) {
					custCatById.setDiscountPips(custCatUpdate.getDiscountPips());
				} else {
					throw new PricerServiceException(PricerServiceError.INVALID_CUST_CAT_DISC_PIPS,
							"Customer Category Discount Pips must be In between Min-Max Discount Pips");
				}
			}*/
			if (null != custCatUpdate.getIsActive()) {
				custCatById.setIsActive(custCatUpdate.getIsActive());
			}

			list.add(custCatById);
		}
		custCatDiscountDao.saveDiscountForCustomer(list);
	}

	private List<CustomerCategoryDetails> convertCustomerDiscountRequest(
			List<CustomerCategoryDetails> customerCategoryDetails) {
		List<CustomerCategoryDetails> list = new ArrayList<>();
		for (CustomerCategoryDetails dto : customerCategoryDetails) {
			CustomerCategoryDetails customerCategoryData = new CustomerCategoryDetails();
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
			channelDetail.setDisountId(discChannelList.getId());
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
			customerCategoryDetails.setDisountId(discCustCatList.getId());
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
			
			list.add(groupDetails);
		}
		return list;
	}
}
