package com.amx.jax.partner.dto;

import java.util.Map;

public class ServiceProviderResponseDTO {
	
	private Map<String, HomeSendSrvcProviderInfo> homeSendInfoDTO;

	public Map<String, HomeSendSrvcProviderInfo> getHomeSendInfoDTO() {
		return homeSendInfoDTO;
	}
	public void setHomeSendInfoDTO(Map<String, HomeSendSrvcProviderInfo> homeSendInfoDTO) {
		this.homeSendInfoDTO = homeSendInfoDTO;
	}

}
