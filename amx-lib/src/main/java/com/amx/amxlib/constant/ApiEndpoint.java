package com.amx.amxlib.constant;

public class ApiEndpoint {
    
    private ApiEndpoint() {
        
    }

    public static final String USER_API_ENDPOINT = "/user";

    public static final String CUSTOMER_ENDPOINT = "/customer";
    
    public static final String CUSTOMER_REG_ENDPOINT = "/customer-reg";

    public static final String EXCHANGE_RATE_ENDPOINT = "/exchange-rate";

    public static final String ONLINE_CUSTOMER_VALIDATE_URL = "/nationality-id/{nationality-id}/validate";

    public static final String META_API_ENDPOINT = "/meta";

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

}
