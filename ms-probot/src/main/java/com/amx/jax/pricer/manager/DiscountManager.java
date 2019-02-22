package com.amx.jax.pricer.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.ChannelDiscountDao;
import com.amx.jax.pricer.dao.CustCatDiscountDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDao;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dto.AmountSlabDetails;
import com.amx.jax.pricer.dto.ChannelDetails;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.service.BankService;
import com.amx.jax.pricer.service.ServiceMasterDescService;

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
	BankService bankService;
	
	@Autowired
	ServiceMasterDescService serviceMasterDescService;

	public void commitChannelDiscountModel(List<ChannelDetails> channelDetails) {

		List<ChannelDetails> channelData = convertchannelRequest(channelDetails);
		List<ChannelDiscount> list = new ArrayList<>();
		for(ChannelDetails channelUpdate : channelData) {
			ChannelDiscount channelById = channelDiscountDao.getDiscountById(channelUpdate.getId());
			
			if(null != channelUpdate.getDiscountPips()) {
				if(channelUpdate.getDiscountPips().doubleValue() >= channelById.getMinDiscountPips().doubleValue() && 
						channelUpdate.getDiscountPips().doubleValue() <= channelById.getMaxDiscountPips().doubleValue()) {
					channelById.setDiscountPips(channelUpdate.getDiscountPips());
					}
				else {
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

	public void commitCustomerDiscountModel(List<CustomerCategoryDetails> customerCategoryDetails) {
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

	public void commitPipsDiscount(List<AmountSlabDetails> amountSlabDetails) {
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
