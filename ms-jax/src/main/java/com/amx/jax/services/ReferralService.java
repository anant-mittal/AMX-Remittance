package com.amx.jax.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.LinkDTO;
import com.amx.amxlib.model.ReferralDTO;
import com.amx.amxlib.model.ReferralResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.db.utils.EntityDtoUtil;
import com.amx.jax.dbmodel.ReferralDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.rest.RestService;
import com.amx.jax.userservice.dao.ReferralDetailsDao;

@Service
@SuppressWarnings("rawtypes")
public class ReferralService extends AbstractService {

	@Autowired
	private ReferralDetailsDao refDao;

	/** The rest service. */
	@Autowired
	RestService restService;

	public AmxApiResponse saveReferral(ReferralDTO dto) {
		ReferralDetails referralDetails;

		referralDetails = refDao.getReferralByCustomerId();
		if (referralDetails == null) {
			referralDetails = new ReferralDetails();
			if (dto.getCustomerId() != null) {
				referralDetails.setCustomerId(dto.getCustomerId());
			}
			if (dto.getCustomerReferralCode() == null) {
				UUID uuid = UUID.randomUUID();
				referralDetails.setCustomerReferralCode(String.valueOf(uuid));
			}
			if (dto.getRefferedByCustomerId() != null) {
				referralDetails.setRefferedByCustomerId(dto.getRefferedByCustomerId());
				referralDetails.setIsConsumed(ConstantDocument.No);
			}
			dto.setCustomerRefferalCode(referralDetails.getCustomerReferralCode());
			dto.setIsConsumed(referralDetails.getIsConsumed());
			dto.setRefferedByCustomerID(referralDetails.getRefferedByCustomerId());
			refDao.saveReferralCode(referralDetails);
			EntityDtoUtil.entityToDto(referralDetails, dto);
		} else {
			dto.setCustomerRefferalCode(referralDetails.getCustomerReferralCode());
			dto.setIsConsumed(referralDetails.getIsConsumed());
			dto.setRefferedByCustomerID(referralDetails.getRefferedByCustomerId());
		}
		ReferralResponseModel referralResponseModel = new ReferralResponseModel();
		referralResponseModel.setCustomerRefferalCode(dto.getCustomerReferralCode());

		Callable<String> callable = () -> {
			Map<String, Object> androidInfo = new HashMap<String, Object>();
			androidInfo.put("androidPackageName", "com.amx.amxremit.dev");
			Map<String, Object> iosInfo = new HashMap<String, Object>();
			iosInfo.put("iosBundleId", "com.amxremit.almulla.exchange");

			Map<String, Object> dynamicLinkInfo = new HashMap<String, Object>();
			dynamicLinkInfo.put("domainUriPrefix", "amx.page.link");
			dynamicLinkInfo.put("link",
					"https://appd-kwt.amxremit.com/refer?senderId=" + referralResponseModel.getCustomerReferralCode());
			dynamicLinkInfo.put("androidInfo", androidInfo);
			dynamicLinkInfo.put("iosInfo", iosInfo);

			Map<String, Object> suffix = new HashMap<String, Object>();

			suffix.put("option", "SHORT");

			Map<String, Object> fields = new HashMap<String, Object>();

			fields.put("dynamicLinkInfo", dynamicLinkInfo);
			fields.put("suffix", suffix);
			return restService.ajax(
					"https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=AIzaSyCbSk1C_rK-0FqIWNq1rJ1HQdQFBqcvdQs")
					.header("Content-Type", "application/json").post(fields).asString();

		};
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> future = executorService.submit(callable);

		try {
			String result = future.get();
			Map<String, Object> responseMap = jsonToMap(new JSONObject(result));
			if (responseMap.containsKey("shortLink")) {
				referralResponseModel.setLink(responseMap.get("shortLink").toString());
				return AmxApiResponse.build(referralResponseModel);
			}

		} catch (InterruptedException | ExecutionException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AmxApiResponse.build(referralResponseModel);
	}

	public Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<String, Object>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public void validateReferralDto(ReferralDTO dto) {
		// both foreign and domestic amounts should not be null
		if (dto.getCustomerId() == null) {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "Customer ID can not be null");
		}

		/*
		 * if(dto.getPayAmount() != null && dto.getReceiveAmount() != null) { throw new
		 * GlobalException("Either PayAmount or ReceivedAmount should have value ",
		 * JaxError.PO_BOTH_PAY_RECEIVED_AMT_VALUE); }
		 */
	}

	public void validateContactDetails(LinkDTO linkDTO) {
		if (linkDTO.getContactType() == null) {
			throw new GlobalException(JaxError.NULL_CONTACT_TYPE, "Contact type can not be null");
		}
	}

	public void validateLinkDetails(LinkDTO linkDTO) {
		if (linkDTO.getLinkId() == null) {
			throw new GlobalException(JaxError.NULL_LINK_ID, "Link Id can not be null");
		}
	}
}
