package com.amx.jax.proto.tpc.api;

import java.math.BigDecimal;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.TpcClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.request.tpc.TpcCustomerLoginRequest;
import com.amx.jax.model.response.customer.CustomerModelResponse;
import com.amx.jax.model.response.tpc.TpcValidateClientSecretResponse;
import com.amx.jax.proto.tpc.api.TPCSession.TPCSessionModel;
import com.amx.jax.proto.tpc.api.TPCStatus.TPCServerCodes;
import com.amx.jax.proto.tpc.api.TPCStatus.TPCServerError;
import com.amx.jax.proto.tpc.models.ClientAuth.ClientAuthRequest;
import com.amx.jax.proto.tpc.models.ClientAuth.ClientAuthResponse;
import com.amx.jax.proto.tpc.models.CustomerAuth.CustomerAuthRequest;
import com.amx.jax.proto.tpc.models.CustomerAuth.CustomerAuthResponse;
import com.amx.utils.ArgUtil;
import com.amx.utils.UniqueID;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

@Component
public class TPCApiAuthService {

	public static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	public static String TEXT_PASSWORD = "dummysecret";
	public static String SESSION_SECRET = "sessionsecret";
	public static String CUSDOMTER_IDENTITY = "284052306594";

	static {
		textEncryptor.setPasswordCharArray(TEXT_PASSWORD.toCharArray());
	}

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	TpcClient tpcClient;

	@Autowired
	private UserClient userclient;

	@Autowired
	JaxMetaInfo jaxMetaInfoContext;

	@Autowired
	TPCSession tpcSession;

	public boolean validate(String input, String target) {
		try {
			if (target.equals(textEncryptor.decrypt(input))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public String encrypt(String input, String salt) {
		try {
			if (ArgUtil.isEmpty(salt)) {
				return textEncryptor.encrypt(input);
			}
			return textEncryptor.encrypt(salt + ":" + input);
		} catch (Exception e) {
			return null;
		}
	}

	public String decrypt(String input, String salt) {
		try {
			String op = textEncryptor.decrypt(input);
			if (!ArgUtil.isEmpty(salt)) {
				return op.replace(salt + ":", "");
			}
			return op;
		} catch (Exception e) {
			return null;
		}
	}

	public ClientAuthResponse authClient(ClientAuthRequest req) {
		try {
			TpcValidateClientSecretResponse resp = tpcClient.validateSecret(req.clientId,
					req.clientSecret).getResult();
			ClientAuthResponse x = new ClientAuthResponse();
			x.sessionToken = encrypt(req.clientId, null);
			tpcSession.setContextId(req.clientId);
			TPCSessionModel session = tpcSession.getOrDefault();
			session.setSessionToken(x.sessionToken);
			session.setSessionSalt(UniqueID.generateString());
			session.setBranchId(resp.getCountryBranchId());
			tpcSession.put(session);
			return x;
		} catch (AbstractJaxException e) {
			throw new TPCServerError(TPCServerCodes.INVALID_CLIENT_CREDS, "Client Credentials do not match");
		}
	}

	public TPCSessionModel validateClient() {
		String sessionToken = commonHttpRequest.get(TPCApiConstants.Keys.CLIENT_SESSION_TOKEN_XKEY);
		String clientId = decrypt(sessionToken, null);
		if (!ArgUtil.isEmpty(clientId)) {
			TPCSessionModel session = tpcSession.get();
			if (sessionToken.equals(sessionToken)) {
				jaxMetaInfoContext.setCountryBranchId(session.getBranchId());
				return session;
			}
		}
		throw new TPCServerError(TPCServerCodes.INVALID_SESSION_TOKEN, "Invalid Session Token passed in API");
	}

	public CustomerAuthResponse authCustomer(CustomerAuthRequest req) {
		TPCSessionModel session = validateClient();
		TpcCustomerLoginRequest tpcCustomerLoginRequest = new TpcCustomerLoginRequest();
		PhoneNumber swissNumberProto;
		try {
			swissNumberProto = phoneUtil.parse(req.mobile, "IN");
			tpcCustomerLoginRequest.setIdentityInt(req.identity);
			tpcCustomerLoginRequest.setPrefix(ArgUtil.parseAsString(swissNumberProto.getCountryCode()));
			tpcCustomerLoginRequest.setMobileNumber(ArgUtil.parseAsString(swissNumberProto.getNationalNumber()));
			AmxApiResponse<CustomerModelResponse, Object> resp = tpcClient.loginCustomer(tpcCustomerLoginRequest);

			CustomerModelResponse customer = userclient.getCustomerModelResponse(req.identity).getResult();
			CustomerAuthResponse authResp = new CustomerAuthResponse();
			authResp.customerToken = encrypt(ArgUtil.parseAsString(customer.getCustomerId()), session.getSessionSalt());
			return authResp;
		} catch (NumberParseException e) {
			// Do Nothing
		}
		throw new TPCServerError(TPCServerCodes.NO_DATA_FOUND, "Customer Not Found in Records");
	}

	public boolean validateCustomer() {
		TPCSessionModel session = validateClient();
		try {
			String customerToken = commonHttpRequest.get(TPCApiConstants.Keys.CUSTOMER_SESSION_TOKEN_XKEY);
			BigDecimal customerId = ArgUtil.parseAsBigDecimal(decrypt(customerToken, session.getSessionSalt()));
			jaxMetaInfoContext.setCustomerId(customerId);
			return true;
		} catch (Exception e) {
			// Do Nothing
		}
		throw new TPCServerError(TPCServerCodes.INVALID_CUSTOMER_TOKEN, "Invalid Customer Token passed in API");
	}

}
