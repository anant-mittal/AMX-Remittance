package com.amx.jax.client;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.REMIT_API_ENDPOINT;

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

import com.amx.amxlib.exception.AbstractException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.util.ConverterUtility;

@Component
public class BeneClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(BeneClient.class);

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
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;
			MultiValueMap<String, String> headers = getHeader();
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/beneList/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<BeneCountryDTO> getBeneficiaryCountryList(BigDecimal beneCountryId) {
		try {
			ResponseEntity<ApiResponse<BeneCountryDTO>> response = null;
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/benecountry/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneCountryDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<RemittancePageDto> defaultBeneficiary(BigDecimal beneRealtionId, BigDecimal transactionId) {
		try {
			ResponseEntity<ApiResponse<RemittancePageDto>> response = null;

			log.info("Default Beneficiary");

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
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<BeneficiaryListDTO> beneDisable(BigDecimal beneMasSeqId, String remarks) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;

			log.info("Transaction Histroy");

			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId).append("&remarks=").append(remarks);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/disable/" + sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<BeneficiaryListDTO> beneFavoriteUpdate(BigDecimal beneMasSeqId) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;

			log.info("bene client beneFavoriteUpdate ");

			StringBuffer sb = new StringBuffer();
			sb.append("?beneMasSeqId=").append(beneMasSeqId);
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favoritebeneupdate/" + sb.toString();
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	/** favouritebene **/
	public ApiResponse<BeneficiaryListDTO> beneFavoriteList() {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;
			MultiValueMap<String, String> headers = getHeader();
			log.info("beneFavList  Clinet to get bene list Input String :");
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/favouritebenelist/";
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<BeneficiaryListDTO> beneUpdate(BeneficiaryListDTO beneficiarydto) {
		try {
			ResponseEntity<ApiResponse<BeneficiaryListDTO>> response = null;

			log.info("Bene update Client :" + beneficiarydto.getCustomerId() + "\t customerId :"
					+ beneficiarydto.getBeneficiaryRelationShipSeqId());
			HttpEntity<String> requestEntity = new HttpEntity<String>(util.marshall(beneficiarydto), getHeader());
			String url = this.getBaseUrl() + REMIT_API_ENDPOINT + "/beneupdate/";
			response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
					new ParameterizedTypeReference<ApiResponse<BeneficiaryListDTO>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

	public ApiResponse<AccountTypeDto> getBeneficiaryAccountType(BigDecimal beneCountryId) {
		try {
			ResponseEntity<ApiResponse<AccountTypeDto>> response = null;

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			StringBuffer sb = new StringBuffer();
			sb.append("?beneCountryId=").append(beneCountryId);
			log.info("Bene Clinet to get bene list Input String :" + sb.toString());
			String url = this.getBaseUrl() + BENE_API_ENDPOINT + "/accounttype/" + sb.toString();
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					new ParameterizedTypeReference<ApiResponse<AccountTypeDto>>() {
					});

			return response.getBody();
		} catch (Exception e) {
			if (e instanceof AbstractException) {
				throw e;
			} else {
				throw new JaxSystemError();
			}
		} // end of try-catch
	}

}
