package com.amx.jax.postman.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.Language;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.DocResult;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.rest.RestService;
import com.amx.utils.CryptoUtil.HashBuilder;

@Component
public class DocServiceClient {

	@Value("${jax.doc.url}")
	private String dcoUrl;

	@Value("${jax.doc.key}")
	private String dcoKey;

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Value("${jax.postman.url}")
	private String postManUrl;

	public String generateUrl() {
		String traceId = AppContextUtil.getTraceId();
		long timestamp = System.currentTimeMillis();
		return String.format("%s/upload/%d/%s/%s", dcoUrl, timestamp, traceId,
				new HashBuilder().secret(dcoKey).message(traceId).toHMAC().output());
	}

	public AmxApiResponse<DocResult, Object> validate(String identity) {
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.DOC_VALIDATE_ID).field("id", identity)
					.postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<DocResult, Object>>() {
					});
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	public Map<String, Object> scan(MultipartFile file, Language lang) {
		return null;
	}
}
