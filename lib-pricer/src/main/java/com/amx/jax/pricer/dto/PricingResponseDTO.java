package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PricingResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3237498833216893709L;

	private List<BankRateDetailsDTO> bankMasterDTOList;
	
	private Map<String, Object> info;

	public List<BankRateDetailsDTO> getBankMasterDTOList() {
		return bankMasterDTOList;
	}

	public void setBankMasterDTOList(List<BankRateDetailsDTO> bankMasterDTOList) {
		this.bankMasterDTOList = bankMasterDTOList;
	}

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}


}
