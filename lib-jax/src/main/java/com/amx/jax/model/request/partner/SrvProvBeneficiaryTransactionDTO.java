package com.amx.jax.model.request.partner;

import com.amx.jax.partner.dto.BeneficiaryDetailsDTO;

public class SrvProvBeneficiaryTransactionDTO {
	
	private BeneficiaryDetailsDTO beneficiaryDetailsDTO;
	private RemittanceTransactionPartnerDTO remittanceTransactionPartnerDTO;
	
	public BeneficiaryDetailsDTO getBeneficiaryDetailsDTO() {
		return beneficiaryDetailsDTO;
	}
	public void setBeneficiaryDetailsDTO(BeneficiaryDetailsDTO beneficiaryDetailsDTO) {
		this.beneficiaryDetailsDTO = beneficiaryDetailsDTO;
	}
	
	public RemittanceTransactionPartnerDTO getRemittanceTransactionPartnerDTO() {
		return remittanceTransactionPartnerDTO;
	}
	public void setRemittanceTransactionPartnerDTO(RemittanceTransactionPartnerDTO remittanceTransactionPartnerDTO) {
		this.remittanceTransactionPartnerDTO = remittanceTransactionPartnerDTO;
	}
	
}
