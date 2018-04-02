package com.amx.amxlib.constant;

public class ApiEndpoint {
    
    private ApiEndpoint() {
        
    }

    public static final String USER_API_ENDPOINT = "/user";

    public static final String CUSTOMER_ENDPOINT = "/customer";

    public static final String EXCHANGE_RATE_ENDPOINT = "/exchange-rate";

    public static final String ONLINE_CUSTOMER_VALIDATE_URL = "/nationality-id/{nationality-id}/validate";

    public static final String META_API_ENDPOINT = "/meta";

    public static final String UPDATE_CUSTOMER_PASSWORD_ENDPOINT = "/password/";

    public static final String BENE_API_ENDPOINT = "/bene";

    public static final String REMIT_API_ENDPOINT = "/remit";

    public static final String BANK_MASTER_BY_COUNTRY_API_ENDPOINT = "/bank/{country-id}";

    public static final String RATE_ALERT_ENDPOINT = "/rate-alert/";
    
    public static final String JAX_FIELD_ENDPOINT = "/jax-field/";

}
