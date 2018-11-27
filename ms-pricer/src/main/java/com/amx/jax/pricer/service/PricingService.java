/**
 * 
 */
package com.amx.jax.pricer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.pricer.dao.CustomerDao;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dto.BankMasterDTO;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.dto.PricingRespDTO;
import com.amx.jax.pricer.manager.CustomerDiscountManager;
import com.amx.jax.pricer.manager.RemitPriceManager;
import com.amx.jax.pricer.repository.ExchangeRateApprovalDetRepository;

/**
 * @author abhijeet
 *
 */
@Service
public class PricingService {

	@Autowired
	CustomerDao customerDao;

	@Autowired
	RemitPriceManager remitPriceManager;

	@Autowired
	CustomerDiscountManager customerDiscountManager;

	@Autowired
	ExchangeRateApprovalDetRepository repository;

	public PricingRespDTO fetchRemitPricesForCustomer(PricingReqDTO pricingReqDTO) {

		Customer customer = customerDao.getCustById(pricingReqDTO.getCustomerId());

		// System.out.println(" Customer ==> " + customer.toString());

		List<BankMasterDTO> bankMasterDTOs = remitPriceManager.fetchPricesForRoutingBanks(pricingReqDTO);

		PricingRespDTO pricingRespDTO = new PricingRespDTO();
		pricingRespDTO.setBankMasterDTOList(bankMasterDTOs);

		return pricingRespDTO;
	}

}
