package com.amx.jax.model.response.remittance;

import java.util.ArrayList;
import java.util.List;

import com.amx.jax.model.ResourceDTO;

/**
 * auth : MRU
 * 
 */
public class RoutingResponseDto {
	List<RoutingServiceDto> serviceList = new ArrayList<>();
	List<ResourceDTO> routingCountrydto = new ArrayList<>();
	List<RoutingBankDto> routingBankDto = new ArrayList<>();
	List<RoutingBranchDto> routingBankBranchDto = new ArrayList<>();
	List<RemittanceModeDto> remittanceModeList = new ArrayList<>();
	List<DeliveryModeDto> deliveryModeList = new ArrayList<>();
	String warnigMsg;

	public List<RoutingServiceDto> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<RoutingServiceDto> serviceList) {
		this.serviceList = serviceList;
	}

	public List<ResourceDTO> getRoutingCountrydto() {
		return routingCountrydto;
	}

	public void setRoutingCountrydto(List<ResourceDTO> routingCountrydto) {
		this.routingCountrydto = routingCountrydto;
	}

	public List<RoutingBankDto> getRoutingBankDto() {
		return routingBankDto;
	}

	public void setRoutingBankDto(List<RoutingBankDto> routingBankDto) {
		this.routingBankDto = routingBankDto;
	}

	public List<RoutingBranchDto> getRoutingBankBranchDto() {
		return routingBankBranchDto;
	}

	public void setRoutingBankBranchDto(List<RoutingBranchDto> routingBankBranchDto) {
		this.routingBankBranchDto = routingBankBranchDto;
	}

	public List<RemittanceModeDto> getRemittanceModeList() {
		return remittanceModeList;
	}

	public void setRemittanceModeList(List<RemittanceModeDto> remittanceModeList) {
		this.remittanceModeList = remittanceModeList;
	}

	public List<DeliveryModeDto> getDeliveryModeList() {
		return deliveryModeList;
	}

	public void setDeliveryModeList(List<DeliveryModeDto> deliveryModeList) {
		this.deliveryModeList = deliveryModeList;
	}

	public String getWarnigMsg() {
		return warnigMsg;
	}

	public void setWarnigMsg(String warnigMsg) {
		this.warnigMsg = warnigMsg;
	}

}
