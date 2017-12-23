package com.amx.amxlib.meta.model;

import com.amx.amxlib.model.AbstractModel;

public class RemittancePageDto extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeneficiaryListDTO beneficiaryDto;
	private TransactionHistroyDTO trnxHistDto;
	
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

}
