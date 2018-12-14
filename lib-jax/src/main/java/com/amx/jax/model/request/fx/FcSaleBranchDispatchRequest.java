package com.amx.jax.model.request.fx;

import java.math.BigDecimal;
import java.util.List;

public class FcSaleBranchDispatchRequest {
	
	List<FcSaleBranchDispatchModel> userStockDocDetails;
	private BigDecimal collectionDocumentNo;
	private BigDecimal collectionDocumentYear;
	
	public List<FcSaleBranchDispatchModel> getUserStockDocDetails() {
		return userStockDocDetails;
	}
	public void setUserStockDocDetails(List<FcSaleBranchDispatchModel> userStockDocDetails) {
		this.userStockDocDetails = userStockDocDetails;
	}
	
	public BigDecimal getCollectionDocumentNo() {
		return collectionDocumentNo;
	}
	public void setCollectionDocumentNo(BigDecimal collectionDocumentNo) {
		this.collectionDocumentNo = collectionDocumentNo;
	}
	
	public BigDecimal getCollectionDocumentYear() {
		return collectionDocumentYear;
	}
	public void setCollectionDocumentYear(BigDecimal collectionDocumentYear) {
		this.collectionDocumentYear = collectionDocumentYear;
	}
		
}
