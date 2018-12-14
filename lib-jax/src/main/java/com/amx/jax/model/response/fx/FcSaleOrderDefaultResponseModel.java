package com.amx.jax.model.response.fx;
/**
 * Author :Rabil
 * Puspose : FC Sale default API gto display 
 * 		1. Source of Income
 * 		2. Purpose of Transaction
 * 		3. Fc Currency list
 * 		4. Denomination type  
 */

import java.util.ArrayList;
import java.util.List;

import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.SourceOfIncomeDto;


public class FcSaleOrderDefaultResponseModel  extends AbstractModel {

	/**
	 * 
	 */
private static final long serialVersionUID = 1567631208195068085L;

@Override
public String getModelType() {
	return "fx-sale-default";
}

public List<FxExchangeRateDto>  fcExchCurrenclyList = new ArrayList<FxExchangeRateDto>();
public List<PurposeOfTransactionDto>  purposeOfTrnxList = new ArrayList<PurposeOfTransactionDto>();
public List<CurrencyDenominationTypeDto> currDenotype = new ArrayList<>();

public List<CurrencyMasterDTO> fcCurrencyList =  new ArrayList<>();
public List<SourceOfIncomeDto> sourcOfIncomeList  = new ArrayList<>();




public List<PurposeOfTransactionDto> getPurposeOfTrnxList() {
	return purposeOfTrnxList;
}
public void setPurposeOfTrnxList(List<PurposeOfTransactionDto> purposeOfTrnxList) {
	this.purposeOfTrnxList = purposeOfTrnxList;
}
public List<CurrencyDenominationTypeDto> getCurrDenotype() {
	return currDenotype;
}
public void setCurrDenotype(List<CurrencyDenominationTypeDto> currDenotype) {
	this.currDenotype = currDenotype;
}
public List<FxExchangeRateDto> getFcExchCurrenclyList() {
	return fcExchCurrenclyList;
}
public void setFcExchCurrenclyList(List<FxExchangeRateDto> fcExchCurrenclyList) {
	this.fcExchCurrenclyList = fcExchCurrenclyList;
}
public List<CurrencyMasterDTO> getFcCurrencyList() {
	return fcCurrencyList;
}
public void setFcCurrencyList(List<CurrencyMasterDTO> fcCurrencyList) {
	this.fcCurrencyList = fcCurrencyList;
}
public List<SourceOfIncomeDto> getSourcOfIncomeList() {
	return sourcOfIncomeList;
}
public void setSourcOfIncomeList(List<SourceOfIncomeDto> sourcOfIncomeList) {
	this.sourcOfIncomeList = sourcOfIncomeList;
}
	
	

}
