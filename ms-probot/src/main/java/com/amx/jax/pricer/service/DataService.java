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
import com.amx.jax.pricer.manager.DiscountManager;
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
	
	@Autowired
	DiscountManager discountManager;

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
