package com.amx.jax.model.response.remittance;

import java.util.List;

public class GsmPlaceOrderListDto {

	List<CountryWiseCountOrderDto> countryWiseCountList;
	List<PlaceOrderApplDto> gsmPlaceOrderList;
	
	
	public List<CountryWiseCountOrderDto> getCountryWiseCountList() {
		return countryWiseCountList;
	}
	public void setCountryWiseCountList(List<CountryWiseCountOrderDto> countryWiseCountList) {
		this.countryWiseCountList = countryWiseCountList;
	}
	public List<PlaceOrderApplDto> getGsmPlaceOrderList() {
		return gsmPlaceOrderList;
	}
	public void setGsmPlaceOrderList(List<PlaceOrderApplDto> gsmPlaceOrderList) {
		this.gsmPlaceOrderList = gsmPlaceOrderList;
	}
}
