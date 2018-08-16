package com.amx.jax.constants;


import com.amx.jax.auditlog.handlers.AbstractAuditHanlder;
import com.amx.jax.auditlog.handlers.ArticleListAuditHandler;
import com.amx.jax.auditlog.handlers.CityListAuditHandler;
import com.amx.jax.auditlog.handlers.CountryListAuditHandler;
import com.amx.jax.auditlog.handlers.DesignationListAuditHandler;
import com.amx.jax.auditlog.handlers.DistrictListAuditHandler;
import com.amx.jax.auditlog.handlers.FieldListAuditHandler;
import com.amx.jax.auditlog.handlers.IdTypeAuditHandler;
import com.amx.jax.auditlog.handlers.IncomeRangeAuditHandler;
import com.amx.jax.auditlog.handlers.SendOtpAuditHandler;
import com.amx.jax.auditlog.handlers.SendOtpEmailMobileAuditHandler;
import com.amx.jax.auditlog.handlers.StateListAuditHandler;
import com.amx.jax.auditlog.handlers.ValidateOTPAuditHandler;


/**
 * @author Prashant - Represents api flow defination
 */
public enum JaxEvent {


	SEND_OTP(SendOtpAuditHandler.class),VALIDATE_OTP(ValidateOTPAuditHandler.class),
	ID_TYPE(IdTypeAuditHandler.class),COUNTRY_LIST(CountryListAuditHandler.class),
	STATE_LIST(StateListAuditHandler.class),FIELD_LIST(FieldListAuditHandler.class),
	MOBILE_EMAIL_OTP(SendOtpEmailMobileAuditHandler.class),DISTRICT_LIST(DistrictListAuditHandler.class),
	CITY_LIST(CityListAuditHandler.class),ARTICLE_LIST(ArticleListAuditHandler.class),
	DESIGNATION_LIST(DesignationListAuditHandler.class),INCOME_RANGE(IncomeRangeAuditHandler.class);
	
	Class<? extends AbstractAuditHanlder> auditHanlder;

	private JaxEvent(Class<? extends AbstractAuditHanlder> auditHanlder) {
		this.auditHanlder = auditHanlder;
	}	

	public Class<? extends AbstractAuditHanlder> getAuditHanlder() {
		return auditHanlder;
	}

	public void setAuditHanlder(Class<? extends AbstractAuditHanlder> auditHanlder) {
		this.auditHanlder = auditHanlder;
	}


}
