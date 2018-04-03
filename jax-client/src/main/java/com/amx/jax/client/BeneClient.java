package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.UPDAE_STATUS_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.VALIDATE_OTP_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.SEND_OTP_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.amx.amxlib.constant.BeneficiaryConstant.BeneStatus;
import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.UnknownJaxError;
import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.util.ConverterUtility;

@Component
public class BeneClient extends AbstractJaxServiceClient {

    private static final Logger LOGGER = Logger.getLogger(BeneClient.class);

    @Autowired
    private ConverterUtility util;

    /**
     * sdsd
     * 
     * @param beneCountryId
     * @return
     */
    public ApiResponse<BeneficiaryListDTO> getBeneficiaryList(BigDecimal beneCountryId) {
        try {
            ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;
            MultiValueMap<String, String> headers = getHeader();
            StringBuffer sb = new StringBuffer();
            sb.append("?beneCountryId=").append(beneCountryId);
            LOGGER.info("Bene Clinet to get bene list Input String :" + sb.toString());
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/beneList/" + sb.toString();
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
                    });
            
            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getBeneficiaryList : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

    public ApiResponse<BeneCountryDTO> getBeneficiaryCountryList(BigDecimal beneCountryId) {
        try {
            ResponseEntity<ApiResponse<BeneCountryDTO>> response;
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            StringBuffer sb = new StringBuffer();
            sb.append("?beneCountryId=").append(beneCountryId);
            LOGGER.info("Bene Clinet to get bene list Input String :" + sb.toString());
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/benecountry/" + sb.toString();
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>() {
                    });

            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getBeneficiaryCountryList : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

    public ApiResponse<RemittancePageDto> defaultBeneficiary(BigDecimal beneRealtionId, BigDecimal transactionId) {
        try {
            ResponseEntity<ApiResponse<RemittancePageDto>> response;

            LOGGER.info("Default Beneficiary");

            StringBuffer sb = new StringBuffer();

            if (beneRealtionId != null || transactionId != null) {
                sb.append("?");
            }

            if (beneRealtionId != null) {
                sb.append("beneRelationId=").append(beneRealtionId).append("&");
            }
            if (transactionId != null) {
                sb.append("transactionId=").append(transactionId);
            }

            HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/defaultbene/" + sb.toString();
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<RemittancePageDto>>() {
                    });

            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in defaultBeneficiary : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

    public ApiResponse<BeneficiaryListDTO> beneDisable(BigDecimal beneMasSeqId, String remarks) {
        try {
            ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;

            LOGGER.info("Transaction Histroy");

            StringBuffer sb = new StringBuffer();
            sb.append("?beneMasSeqId=").append(beneMasSeqId).append("&remarks=").append(remarks);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/disable/" + sb.toString();
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
                    });

            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in beneDisable : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

    public ApiResponse<BeneficiaryListDTO> beneFavoriteUpdate(BigDecimal beneMasSeqId) {
        try {
            ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;

            LOGGER.info("bene client beneFavoriteUpdate ");

            StringBuffer sb = new StringBuffer();
            sb.append("?beneMasSeqId=").append(beneMasSeqId);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favoritebeneupdate/" + sb.toString();
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
                    });

            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in beneFavoriteUpdate : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

    /** favouritebene **/
    public ApiResponse<BeneficiaryListDTO> beneFavoriteList() {
        try {
            ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;
            MultiValueMap<String, String> headers = getHeader();
            LOGGER.info("beneFavList  Clinet to get bene list Input String :");
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favouritebenelist/";
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
                    });
            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in beneFavoriteList : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

    public ApiResponse<BeneficiaryListDTO> beneUpdate(BeneficiaryListDTO beneficiarydto) {
        try {
            ResponseEntity<ApiResponse<BeneficiaryListDTO>> response;

            LOGGER.info("Bene update Client :" + beneficiarydto.getCustomerId() + "\t customerId :"
                    + beneficiarydto.getBeneficiaryRelationShipSeqId());
            HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(beneficiarydto), getHeader());
            String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/beneupdate/";
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
                    });

            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in beneUpdate : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

    public ApiResponse<AccountTypeDto> getBeneficiaryAccountType(BigDecimal beneCountryId) {
        try {
            ResponseEntity<ApiResponse<AccountTypeDto>> response;

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            StringBuffer sb = new StringBuffer();
            sb.append("?beneCountryId=").append(beneCountryId);
            LOGGER.info("Bene Clinet to get bene list Input String :" + sb.toString());
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/accounttype/" + sb.toString();
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
            response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<ApiResponse<AccountTypeDto>>() {
                    });

            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in getBeneficiaryAccountType : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }

	/**
	 * @return Gives the list of all beneficiary relations possible
	 */
	public ApiResponse<BeneRelationsDescriptionDto> getBeneficiaryRelations() {
		try {
			ResponseEntity<ApiResponse<BeneRelationsDescriptionDto>> response;

			LOGGER.info("in getBeneficiaryRelations");
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/relations/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneRelationsDescriptionDto>>() {
					});

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getBeneficiaryRelations : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	public ApiResponse<CivilIdOtpModel> sendOtp()
			throws InvalidInputException, CustomerValidationException, LimitExeededException {
		try {
			ResponseEntity<ApiResponse<CivilIdOtpModel>> response;
			HttpEntity<AbstractUserModel> requestEntity = new HttpEntity<AbstractUserModel>(getHeader());
			String sendOtpUrl = this.getBaseUrl() + BENE_API_ENDPOINT + SEND_OTP_ENDPOINT;
			response = restTemplate.exchange(sendOtpUrl, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CivilIdOtpModel>>() {
					});
			LOGGER.info("responce from  sendOtp api: " + util.marshall(response.getBody()));

			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
	public ApiResponse<CustomerModel> validateOtp(String mOtp, String eOtp)
			throws IncorrectInputException, CustomerValidationException, LimitExeededException, UnknownJaxError {
		try {
			ResponseEntity<ApiResponse<CustomerModel>> response;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String validateOtpUrl = this.getBaseUrl() + BENE_API_ENDPOINT + VALIDATE_OTP_ENDPOINT;
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(validateOtpUrl).queryParam("mOtp", mOtp)
					.queryParam("eOtp", eOtp);
			response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<CustomerModel>>() {
					});
			return response.getBody();
		} catch (AbstractException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in validateOtp : ", e);
			throw new JaxSystemError();
		} // end of try-catch
	}
	
    @SuppressWarnings("rawtypes")
	public ApiResponse updateStatus(BigDecimal beneMasSeqId,String remarks,BeneStatus status) {
        try {
            ResponseEntity<ApiResponse> response;

            StringBuffer sb = new StringBuffer();
            sb.append("?beneMasSeqId=").append(beneMasSeqId);
            sb.append("&remarks=").append(remarks);
            sb.append("&status=").append(status);
            HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
            String url = this.getBaseUrl() + BENE_API_ENDPOINT + UPDAE_STATUS_ENDPOINT + sb.toString();
            response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<ApiResponse>() {
                    });
            return response.getBody();
        } catch (AbstractException ae) {
            throw ae;
        } catch (Exception e) {
            LOGGER.error("exception in updateStatus : ",e);
            throw new JaxSystemError();
        } // end of try-catch
    }
}
