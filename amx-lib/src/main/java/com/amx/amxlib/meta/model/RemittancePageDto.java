package com.amx.amxlib.meta.model;

import java.util.List;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;

public class RemittancePageDto extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BeneficiaryListDTO beneficiaryDto;
	private TransactionHistroyDTO trnxHistDto;

	CurrencyMasterDTO forCur = null;
	private PlaceOrderDTO placeOrderDTO = null;

	public CurrencyMasterDTO getForCur() {
		return forCur;
	}

	public void setForCur(CurrencyMasterDTO forCur) {
		this.forCur = forCur;
	}

	public CurrencyMasterDTO getDomCur() {
		return domCur;
	}

	public void setDomCur(CurrencyMasterDTO domCur) {
		this.domCur = domCur;
	}

	CurrencyMasterDTO domCur = null;

	private List<ParameterDetailsDto> packages;

	public BeneficiaryListDTO getBeneficiaryDto() {
		return beneficiaryDto;
	}

	public void setBeneficiaryDto(BeneficiaryListDTO beneficiaryDto) {
		this.beneficiaryDto = beneficiaryDto;
	}

	public TransactionHistroyDTO getTrnxHistDto() {
		return trnxHistDto;
	}

	public void setTrnxHistDto(TransactionHistroyDTO trnxHistDto) {
		this.trnxHistDto = trnxHistDto;
	}

	@Override
	public String getModelType() {
		return "remittance-page-dto";
	}

	public PlaceOrderDTO getPlaceOrderDTO() {
		return placeOrderDTO;
	}

	public void setPlaceOrderDTO(PlaceOrderDTO placeOrderDTO) {
		this.placeOrderDTO = placeOrderDTO;
	}

	public void setPackages(List<ParameterDetailsDto> parameterDetailsDto) {
		this.packages = parameterDetailsDto;
	}

	public List<ParameterDetailsDto> getPackages() {
		return packages;
	}

}
