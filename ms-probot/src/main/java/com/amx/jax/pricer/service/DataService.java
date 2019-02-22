package com.amx.jax.pricer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.dto.AmountSlabDetails;
import com.amx.jax.pricer.dto.ChannelDetails;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

@Service
public class DataService {

	@Autowired
	ChannelDiscountDao channelDiscountDao;

	@Autowired
	CustCatDiscountDao custCatDiscountDao;

	@Autowired
	PipsMasterDao pipsMasterDao;
	
	@Autowired
	RoutingDao routingDao;
	
	@Autowired
	BankService bankService;
	
	@Autowired
	ServiceMasterDescService serviceMasterDescService;

	public DiscountDetailsReqRespDTO getDiscountManagementData(DiscountMgmtReqDTO discountMgmtReqDTO) {

		DiscountDetailsReqRespDTO discountMgmtRespDTO = new DiscountDetailsReqRespDTO();

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CHANNEL)) {
			List<ChannelDiscount> channelDiscount = channelDiscountDao.getDiscountForAllChannel();
			List<ChannelDetails> channelData = convertChannelData(channelDiscount);
			discountMgmtRespDTO.setChannelDetails(channelData);
		}

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.CUSTOMER_CATEGORY)) {
			List<CustomerCategoryDiscount> custCatDiscount = custCatDiscountDao.getDiscountForAllCustCategory();
			List<CustomerCategoryDetails> custCategoryData = convertCustCategoryData(custCatDiscount);
			discountMgmtRespDTO.setCustomerCategoryDetails(custCategoryData);
		}

		if (discountMgmtReqDTO.getDiscountType().contains(DISCOUNT_TYPE.AMOUNT_SLAB)) {
			if (null != discountMgmtReqDTO.getCountryId() && null != discountMgmtReqDTO.getCurrencyId()) {
				List<PipsMaster> pipsMasterData = pipsMasterDao.getAmountSlab(discountMgmtReqDTO.getCountryId(),
						discountMgmtReqDTO.getCurrencyId());
				List<AmountSlabDetails> amountSlabData = convertAmountSlabData(pipsMasterData);
				discountMgmtRespDTO.setAmountSlabDetails(amountSlabData);
			}
		}

		return discountMgmtRespDTO;
	}

	private List<ChannelDetails> convertChannelData(List<ChannelDiscount> channelDiscount) {
		List<ChannelDetails> list = new ArrayList<>();

		for (ChannelDiscount discList : channelDiscount) {
			ChannelDetails channelDetail = new ChannelDetails();
			channelDetail.setId(discList.getId());
			channelDetail.setChannel(discList.getChannel());
			channelDetail.setDiscountPips(discList.getDiscountPips());
			channelDetail.setIsActive(discList.getIsActive());
			channelDetail.setMinDiscountPips(discList.getMinDiscountPips());
			channelDetail.setMaxDiscountPips(discList.getMaxDiscountPips());

			list.add(channelDetail);
		}

		return list;
	}

	private List<CustomerCategoryDetails> convertCustCategoryData(List<CustomerCategoryDiscount> custCatDiscount) {
		List<CustomerCategoryDetails> list = new ArrayList<>();

		for (CustomerCategoryDiscount custDiscList : custCatDiscount) {
			CustomerCategoryDetails custCategoryDetail = new CustomerCategoryDetails();
			custCategoryDetail.setId(custDiscList.getId());
			custCategoryDetail.setCustomerCategory(custDiscList.getCustomerCategory());
			custCategoryDetail.setDiscountPips(custDiscList.getDiscountPips());
			custCategoryDetail.setIsActive(custDiscList.getIsActive());
			custCategoryDetail.setMinDiscountPips(custDiscList.getMinDiscountPips());
			custCategoryDetail.setMaxDiscountPips(custDiscList.getMaxDiscountPips());

			list.add(custCategoryDetail);
		}

		return list;
	}

	private List<AmountSlabDetails> convertAmountSlabData(List<PipsMaster> pipsMasterData) {
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
					amountSlabDetail.setCountryName(pipsMasterList.getCountryMaster().getFsCountryMasterDescs().get(0).getCountryName());
				}
			}
			amountSlabDetail.setMinDiscountPips(pipsMasterList.getMinDiscountPips());
			amountSlabDetail.setMaxDiscountPips(pipsMasterList.getMaxDiscountPips());			

			list.add(amountSlabDetail);
		}
		return list;
	}

	public List<RoutBanksAndServiceRespDTO> getRoutBanksAndServices(BigDecimal countryId, BigDecimal currencyId) {
		List<RoutingHeader> rountingHeaderData = routingDao.getRoutHeadersByCountryIdAndCurrenyId(countryId, currencyId);
		List<RoutBanksAndServiceRespDTO> routBanksAndServiceRespDTO = convertRoutBankAndService(rountingHeaderData);
		return routBanksAndServiceRespDTO;
	}

	private List<RoutBanksAndServiceRespDTO> convertRoutBankAndService(List<RoutingHeader> rountingHeaderData) {
		List<RoutBanksAndServiceRespDTO> list = new ArrayList<>();
		for(RoutingHeader routingData : rountingHeaderData) {
			RoutBanksAndServiceRespDTO routBanksAndServiceRespData = new RoutBanksAndServiceRespDTO();
			routBanksAndServiceRespData.setRoutingBankId(routingData.getRoutingBankId());
			
			BankMasterModel bankName = bankService.getBankById(routingData.getRoutingBankId());
			if(null != bankName) {
				routBanksAndServiceRespData.setRoutingBankName(bankName.getBankFullName());
			}
			
			routBanksAndServiceRespData.setServiceId(routingData.getServiceMasterId());
			
			ServiceMasterDesc serviceName = serviceMasterDescService.getServiceById(routingData.getServiceMasterId());
			if(null != serviceName) {
				routBanksAndServiceRespData.setServiceDesc(serviceName.getServiceDesc());
			}
			
			list.add(routBanksAndServiceRespData);
		}
		return list;
	}

	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(DiscountDetailsReqRespDTO discountdetailsRequestDTO) {
		//DiscountDetailsReqRespDTO discountDetailsResponseDTO = new DiscountDetailsReqRespDTO();
		
		if(null != discountdetailsRequestDTO.getChannelDetails()) {
			commitChannelDiscountModel(discountdetailsRequestDTO.getChannelDetails());
		}
		if(null != discountdetailsRequestDTO.getCustomerCategoryDetails()) {
			commitCustomerDiscountModel(discountdetailsRequestDTO.getCustomerCategoryDetails());
		}
		if(null != discountdetailsRequestDTO.getAmountSlabDetails()) {
			commitPipsDiscount(discountdetailsRequestDTO.getAmountSlabDetails());
		}
		
		return AmxApiResponse.build();
	}

	
	private void commitChannelDiscountModel(List<ChannelDetails> channelDetails) {
		List<ChannelDetails> channelData = convertchannelRequest(channelDetails);
		List<ChannelDiscount> list = new ArrayList<>();
		for(ChannelDetails channelUpdate : channelData) {
			ChannelDiscount channelById = channelDiscountDao.getDiscountById(channelUpdate.getId());
			
			if(null != channelUpdate.getDiscountPips()) {
				if(channelUpdate.getDiscountPips().doubleValue() >= channelById.getMinDiscountPips().doubleValue() && 
						channelUpdate.getDiscountPips().doubleValue() <= channelById.getMaxDiscountPips().doubleValue()) {
					channelById.setDiscountPips(channelUpdate.getDiscountPips());
					}
				else{
					throw new PricerServiceException(PricerServiceError.INVALID_CHANNEL_DISC_PIPS, 
							"Channel Discount Pips must be In between Min-Max Discount Pips");
				}
			}
			
			if(null != channelUpdate.getIsActive()) {
				channelById.setIsActive(channelUpdate.getIsActive());
			}
			
			list.add(channelById);
		}
		channelDiscountDao.saveDiscountForChannel(list);
	}

	private List<ChannelDetails> convertchannelRequest(List<ChannelDetails> channelDetails) {
		List<ChannelDetails> list = new ArrayList<>();
		
		for(ChannelDetails dto:channelDetails) {
			ChannelDetails channelData = new ChannelDetails();
			channelData.setDiscountPips(dto.getDiscountPips());
			channelData.setIsActive(dto.getIsActive());
			channelData.setId(dto.getId());
			
			list.add(channelData);
		}
		return list;
	}
	
	private void commitCustomerDiscountModel(List<CustomerCategoryDetails> customerCategoryDetails) {
		List<CustomerCategoryDetails> customerDiscountData = convertCustomerDiscountRequest(customerCategoryDetails);
		List<CustomerCategoryDiscount> list = new ArrayList<>();
		for(CustomerCategoryDetails custCatUpdate : customerDiscountData) {
			CustomerCategoryDiscount custCatById = custCatDiscountDao.getCustCatDiscountById(custCatUpdate.getId());
			if(null != custCatUpdate.getDiscountPips()) {
				if(custCatUpdate.getDiscountPips().doubleValue() >= custCatById.getMinDiscountPips().doubleValue() && 
						custCatUpdate.getDiscountPips().doubleValue() <= custCatById.getMaxDiscountPips().doubleValue()) {
					custCatById.setDiscountPips(custCatUpdate.getDiscountPips());
					}
				else{
					throw new PricerServiceException(PricerServiceError.INVALID_CUST_CAT_DISC_PIPS, 
							"Customer Category Discount Pips must be In between Min-Max Discount Pips");
				}
			}
			if(null != custCatUpdate.getIsActive()) {
				custCatById.setIsActive(custCatUpdate.getIsActive());
			}
			
			list.add(custCatById);
		}
		custCatDiscountDao.saveDiscountForCustomer(list);
	}

	private List<CustomerCategoryDetails> convertCustomerDiscountRequest(List<CustomerCategoryDetails> customerCategoryDetails) {
		List<CustomerCategoryDetails> list = new ArrayList<>();
		for(CustomerCategoryDetails dto:customerCategoryDetails) {
			CustomerCategoryDetails customerCategoryData = new CustomerCategoryDetails();
			customerCategoryData.setDiscountPips(dto.getDiscountPips());
			customerCategoryData.setIsActive(dto.getIsActive());
			customerCategoryData.setId(dto.getId());
			
			list.add(customerCategoryData);
		}
		return list;
	}
	
	private void commitPipsDiscount(List<AmountSlabDetails> amountSlabDetails) {
		List<AmountSlabDetails> pipsData = convertPipsRequest(amountSlabDetails);
		List<PipsMaster> list = new ArrayList<>();
		for(AmountSlabDetails pipsUpdate : pipsData) {
			PipsMaster pipsByMasterId = pipsMasterDao.getPipsById(pipsUpdate.getPipsMasterId());
			if(null != pipsUpdate.getDiscountPips()) {
				if(pipsUpdate.getDiscountPips().doubleValue() >= pipsByMasterId.getMinDiscountPips().doubleValue() && 
						pipsUpdate.getDiscountPips().doubleValue() <= pipsByMasterId.getMaxDiscountPips().doubleValue()) {
					pipsByMasterId.setPipsNo(pipsUpdate.getDiscountPips());
					}
				else{
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
		for(AmountSlabDetails dto:amountSlabDetails) {
			AmountSlabDetails pipsData = new AmountSlabDetails();
			pipsData.setDiscountPips(dto.getDiscountPips());
			pipsData.setPipsMasterId(dto.getPipsMasterId());
			
			list.add(pipsData);
		}
		return list;
	}

}
