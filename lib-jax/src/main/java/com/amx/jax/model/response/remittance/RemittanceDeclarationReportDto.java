package com.amx.jax.model.response.remittance;

import java.util.List;

public class RemittanceDeclarationReportDto {
	private Boolean waterMarkCheck ;
	private List<DeclarationReportDto> declarationList;
	public Boolean getWaterMarkCheck() {
		return waterMarkCheck;
	}
	public void setWaterMarkCheck(Boolean waterMarkCheck) {
		this.waterMarkCheck = waterMarkCheck;
	}
	public List<DeclarationReportDto> getDeclarationList() {
		return declarationList;
	}
	public void setDeclarationList(List<DeclarationReportDto> declarationList) {
		this.declarationList = declarationList;
	} 
}
