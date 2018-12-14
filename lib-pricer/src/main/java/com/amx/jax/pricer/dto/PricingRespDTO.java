package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.util.List;

public class PricingRespDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3237498833216893709L;
	
	private List<BankRateDetailsDTO> bankMasterDTOList;

	public List<BankRateDetailsDTO> getBankMasterDTOList() {
		return bankMasterDTOList;
	}

	public void setBankMasterDTOList(List<BankRateDetailsDTO> bankMasterDTOList) {
		this.bankMasterDTOList = bankMasterDTOList;
	}

	
}
