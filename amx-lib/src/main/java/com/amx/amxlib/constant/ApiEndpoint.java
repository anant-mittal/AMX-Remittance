package com.amx.amxlib.constant;

import java.util.Date;
import java.util.List;

import com.amx.jax.model.customer.SecurityQuestionModel;

public class ApiEndpoint {

	private ApiEndpoint() {

	}

	public static final String USER_API_ENDPOINT = "/user";

	public static final String CUSTOMER_ENDPOINT = "/customer";
	
	public static final String EKYC_ENDPOINT = "/eKyc";

	public static final String CUSTOMER_REG_ENDPOINT = "/customer-reg";

	public static final String EXCHANGE_RATE_ENDPOINT = "/exchange-rate";

	public static final String ONLINE_CUSTOMER_VALIDATE_URL = "/nationality-id/{nationality-id}/validate";
	
	public static final String REVENUE_REPORT_ENDPOINT = "/revenue-report";

	public final class MetaApi {
		// Prefix
		public static final String PREFIX = "/meta";

		// Params
		public static final String PARAM_QUEST_ID = "questId";
		public static final String PARAM_EMAIL_ID = "emailId";
		public static final String PARAM_BENE_COUNTRY_ID = "beneficiaryCountryId";
		public static final String PARAM_SERVICE_GROUP_ID = "serviceGroupId";
		public static final String PARAM_ROUTING_BANK_ID = "routingBankId";
		public static final String PARAM_IND = "ind";
		public static final String PARAM_DISTRICT_ID = "districtId";
		public static final String PARAM_CITY_ID = "cityId";
		public static final String PARAM_COUNTRY_ID = "countryId";
		public static final String PARAM_STATE_ID = "stateId";
		public static final String PARAM_GOVERONATE_ID = "governateId";

		// Paths
		public static final String SEQ_QUEST_LIST = "/quest/list";
		public static final String SEQ_QUEST_BY_ID = "/quest/{questId}";
		public static final String APPL_COUNTRY = "/applcountry/";
		public static final String HELP_DESK_TIME = "/helpdtime/";
		public static final String APPL_COUNTRY_COMP = "/applcountrycomp/";
		public static final String API_COUNTRY = "/country/";
		public static final String API_COUNTRY_BY_lANG_ID = "/country/lang";
		public static final String API_COUNTRY_BY_lANG_COUNTRY_ID = "/country/langcountry";
		public static final String API_COUNTRY_BC = "/country/bc/";
		public static final String API_TERMS_BY_lANG_ID = "/terms/lang";
		public static final String API_TERMS_BY_lANG_COUNTRY_ID = "/terms/langcountry";
		public static final String API_TERMS_BY_lANG_COUNTRY_ID_FOR_FX = "/terms/langcountry/fx";
		public static final String API_WHY = "/why/";
		public static final String EMAIL_CHECK = "/emailcheck/{emailId}";
		public static final String API_FYEAR = "/fyear";
		public static final String API_HELP_NO = "/helpdno/";
		public static final String API_PREFIX = "/prefix/";
		public static final String SERVICE_GROUP = "/service-group/";
		public static final String API_BRANCH_DETAIL = "/branchdetail/";
		public static final String API_BRANCH_SYSTEM_INV_LIST = "/branchsysteminventory/list/";
		public static final String META_PARAMETER = "/meta-parameter/";
		public static final String CURRENCY_BENE_SERVICE = "/currency/beneservice/";
		public static final String API_ONLINE_CONFIG = "/onlineconfig/";
		public static final String API_CITY_DESC = "/citydesc/{stateId}/{districtId}/";
		public static final String API_CITY_LIST = "/citylist/{districtId}/";
		public static final String API_STATE_LIST = "/statelist/{countryId}/";
		public static final String API_STATE_DESC = "/statedesc/{countryId}/{stateId}/";
		public static final String API_DISTRICTLIST = "/districtlist/{stateId}/";
		public static final String API_DISTRICTDESC = "/districtdesc/{stateId}/{districtId}/";
		public static final String MULTI_COUNTRY = "/multicountry/";
		public static final String EXCHANGE_RATE_CURRENCY_LIST = "/exchange-rate-currency/list/";
		public static final String CURRENCY_ONLINE = "/currency/online/";
		public static final String API_BANK_BRANCH_GET = "/bankbranch/get/";
		public static final String API_AREA_LIST = "/arealist/";
		public static final String API_GOVERNATE_LIST = "/governatelist/";
		public static final String API_GOVERNATE_AREA_LIST = "/governatearealist/";
		public static final String API_STATUS_LIST = "/statuslist/";
		public static final String API_COUNTRY_BRANCH_LIST = "/countrybranchlist/";
		public static final String API_DESIGNATION = "/designationslist/";
		public static final String API_DECLARATION = "/declaration/";
	}

	public static final String UPDATE_CUSTOMER_PASSWORD_ENDPOINT = "/password/";

	public static final String BENE_API_ENDPOINT = "/bene";

	public static final String REMIT_API_ENDPOINT = "/remit";

	public static final String BANK_MASTER_BY_COUNTRY_API_ENDPOINT = "/bank/{country-id}";

	public static final String RATE_ALERT_ENDPOINT = "/rate-alert/";

	public static final String JAX_FIELD_ENDPOINT = "/jax-field/";

	public static final String VALIDATE_OTP_ENDPOINT = "/validate-otp/";

	public static final String SEND_OTP_ENDPOINT = "/send-otp/";

	public static final String UPDAE_STATUS_ENDPOINT = "/update-status/";

	public static final String GET_SERVICE_PROVIDER_ENDPOINT = "/service-provider/";

	public static final String GET_AGENT_MASTER_ENDPOINT = "/agent-master/";

	public static final String GET_AGENT_BRANCH_ENDPOINT = "/agent-branch/";

	public static final String ACCOUNT_TYPE_ENDPOINT = "/accounttype/";

	public static final String PLACE_ORDER_ENDPOINT = "/place-order/";

	public static final String OFFSITE_CUSTOMER_REG = "/offsite-cust-reg";
	public static final String JAX_NOTIFICATION_ENDPOINT = "/jax-notification";
	public static final String JAX_CUSTOMER_NOTIFICATION = "/jax/notifications";

	public static final String LOGIN_ENDPOINT = "/login";

	public static final String VALIDATE_USER_ENDPOINT = "/validate-user/";

	public static final String VALIDATE_EMPNO_ENDPOINT = "/validate-ecno/";

	public static final String GET_USER_MENU_ENDPOINT = "/user-menu/";

	public static final String VALIDATE_USER_MENU_ENDPOINT = "/validate-user-menu/";

	public static final String SYNC_ENUMS_ENDPOINT = "/api/sync/perms/";

	public static final String VALIDATE_AUTH_USER_ENDPOINT = "/api/user/validate/";

	public static final String VALIDATE_USER_DETAIL_ENDPOINT = "/api/user/auth/";

	public static final String SAVE_ROLE_ENDPOINT = "/api/role/";

	public static final String SAVE_PERM_ENDPOINT = "/api/role/perm/";

	public static final String SAVE_USER_ROLE_ENDPOINT = "/api/user/role/";

	public static final String SAVE_USER_PERM_ENDPOINT = "/api/user/perms/";
	
	public final class CustomerApi{
		//Prefix
		public static final String PREFIX = CUSTOMER_ENDPOINT;
		
		//Paths
		public static final String GET_ANNUAL_INCOME_RANGE = "/getAnnualIncome/";
		public static final String SAVE_ANNUAL_INCOME = "/saveAnnualIncome/";
		public static final String GET_ANNUAL_INCOME_DETAILS = "/getAnnualIncomeDetails/";
		public static final String SAVE_SECURITY_QUESTIONS = "/save-security-questions/";
		//params
		public static final String PARAM_INCOMEDTO = "incomeDto";
		
	}

	public static final String LINK_DEVICEID = "/link-deviceid/";

	// public static final String LINK_DEVICE_LOGGEDIN_CUSTOMER =
	// "/link-device-loggedin-customer/";

	// public static final String LOGIN_CUSTOMER_BY_FINGERPRINT =
	// "/login-customer-by-fingerprint/";

	public final class UserApi {
		public static final String PREFIX = USER_API_ENDPOINT;

		// Params
		public static final String IDENTITYINT = "identityInt";

		public static final String PASSWORD = "password";

		public static final String FINGERPRINTDEVICEID = "fingerprintDeviceId";
		
	

		// Paths
		public static final String LINK_DEVICE_LOGGEDIN_USER = "/link-device-loggedin-user/";

		public static final String LOGIN_CUSTOMER_BY_FINGERPRINT = "/login-customer-by-fingerprint/";

		public static final String DELINK_FINGERPRINT = "/delink-fingerprint";
		public static final String RESET_FINGERPRINT = "/reset-fingerprint";

	}
	
	public final class EKyc{
		
		
		public static final String PREFIX = EKYC_ENDPOINT;
		

		// Paths
		public static final String EKYC_SAVE_CUSTOMER = "/ekyc-save-customer/";
		
	}
	
	public final class ServiceProvider{
		public static final String PREFIX = REVENUE_REPORT_ENDPOINT;
		
		//Paths
		
		public static final String REVENUE_REPORT_PARTNER = "/revenue-report-partner";
		
		public static final String REVENUE_REPORT_UPLOAD_FILE="/revenue-report-upload-file";
		
		public static final String REVENUE_REPORT_CONFIRMATION="/revenue-report-confirmation";
		
		public static final String REVENUE_REPORT_DEFAULT_DATE = "/revenue-report-default-date";
	}

}
