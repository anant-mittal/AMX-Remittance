package com.amx.amxlib.meta.model;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.CurrencyMasterDTO;

public class RemittancePageDto extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BeneficiaryListDTO beneficiaryDto;
	private TransactionHistroyDTO trnxHistDto;

	CurrencyMasterDTO forCur = null;
	private PlaceOrderDTO placeOrderDTO=null;

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

}
