package com.amx.amxlib.meta.model;

import java.util.List;

import com.amx.amxlib.model.PromotionDto;

public class RemittanceReceiptSubreport {

	private List<RemittanceReportBean> remittanceApplList;
	
	private List<RemittanceReportBean> fcsaleAppList;
	
	private List<RemittanceReportBean> collectionAppList;
	
	private List<RemittanceReportBean> cities;
	
	private String waterMarkLogoPath;
	
	private Boolean waterMarkCheck;
	
	private String subReport;
	    
	private Boolean fcsaleApplicationCheck;
	
	private Boolean remittanceReceiptCheck;
	
	private PromotionDto promotionDto;
	
	public Boolean getWaterMarkCheck() {
		return waterMarkCheck;
	}

	public void setWaterMarkCheck(Boolean waterMarkCheck) {
		this.waterMarkCheck = waterMarkCheck;
	}

	public String getWaterMarkLogoPath() {
		return waterMarkLogoPath;
	}

	public void setWaterMarkLogoPath(String waterMarkLogoPath) {
		this.waterMarkLogoPath = waterMarkLogoPath;
	}
	
	
	public List<RemittanceReportBean> getCities() {
		return cities;
	}

	public void setCities(List<RemittanceReportBean> cities) {
		this.cities = cities;
	}
	
	public Boolean getRemittanceReceiptCheck() {
		return remittanceReceiptCheck;
	}

	public void setRemittanceReceiptCheck(Boolean remittanceReceiptCheck) {
		this.remittanceReceiptCheck = remittanceReceiptCheck;
	}

	public Boolean getFcsaleApplicationCheck() {
		return fcsaleApplicationCheck;
	}

	public void setFcsaleApplicationCheck(Boolean fcsaleApplicationCheck) {
		this.fcsaleApplicationCheck = fcsaleApplicationCheck;
	}

	public String getSubReport() {
		return subReport;
	}

	public void setSubReport(String subReport) {
		this.subReport = subReport;
	}

	public List<RemittanceReportBean> getRemittanceApplList() {
		return remittanceApplList;
	}

	public List<RemittanceReportBean> getFcsaleAppList() {
		return fcsaleAppList;
	}

	public void setRemittanceApplList(List<RemittanceReportBean> remittanceApplList) {
		this.remittanceApplList = remittanceApplList;
	}

	public void setFcsaleAppList(List<RemittanceReportBean> fcsaleAppList) {
		this.fcsaleAppList = fcsaleAppList;
	}



	public List<RemittanceReportBean> getCollectionAppList() {
		return collectionAppList;
	}



	public void setCollectionAppList(List<RemittanceReportBean> collectionAppList) {
		this.collectionAppList = collectionAppList;
	}

	public PromotionDto getPromotionDto() {
		return promotionDto;
	}

	public void setPromotionDto(PromotionDto promotionDto) {
		this.promotionDto = promotionDto;
	}







	
	
	
	
}
