package com.amx.jax.model.response.remittance;

import java.util.ArrayList;
import java.util.List;

/**
 * auth : MRU
 * 
 */
public class RoutingResponseDto {
	List<RoutingServiceDto> serviceList = new ArrayList<>();
	List<RoutingCountryDto> routingCountrydto = new ArrayList<>();
	List<RoutingBankDto> routingBankDto = new ArrayList<>();
	List<RoutingBranchDto> routingBankBranchDto = new ArrayList<>();
	List<RemittanceModeDto> remittanceModeList = new ArrayList<>();
	List<DeliveryModeDto> deliveryModeList = new ArrayList<>();
	
	
	

	public List<RoutingServiceDto> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<RoutingServiceDto> serviceList) {
		this.serviceList = serviceList;
	}

	public List<RoutingCountryDto> getRoutingCountrydto() {
		return routingCountrydto;
	}

	public void setRoutingCountrydto(List<RoutingCountryDto> routingCountrydto) {
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

	
	
}
