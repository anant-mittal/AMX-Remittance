package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class CommonRequest {
BigDecimal countryId;
BigDecimal stateId;
BigDecimal districtId;
BigDecimal cityId;
BigDecimal articleId;
BigDecimal articleDetailsId;

public BigDecimal getCountryId() {
	return countryId;
}
public void setCountryId(BigDecimal countryId) {
	this.countryId = countryId;
}
public BigDecimal getStateId() {
	return stateId;
}
public void setStateId(BigDecimal stateId) {
	this.stateId = stateId;
}
public BigDecimal getDistrictId() {
	return districtId;
}
public void setDistrictId(BigDecimal districtId) {
	this.districtId = districtId;
}
public BigDecimal getCityId() {
	return cityId;
}
public void setCityId(BigDecimal cityId) {
	this.cityId = cityId;
}
public BigDecimal getArticleId() {
	return articleId;
}
public void setArticleId(BigDecimal articleId) {
	this.articleId = articleId;
}
public BigDecimal getArticleDetailsId() {
	return articleDetailsId;
}
public void setArticleDetailsId(BigDecimal articleDetailsId) {
	this.articleDetailsId = articleDetailsId;
}
@Override
public String toString() {
	return "CommonRequest [countryId=" + countryId + ", stateId=" + stateId + ", districtId=" + districtId + ", cityId="
			+ cityId + ", articleId=" + articleId + ", articleDetailsId=" + articleDetailsId + "]";
}

}
