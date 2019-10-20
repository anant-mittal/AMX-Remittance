package com.amx.jax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.VendorFeatures;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.ApiRequestDetail;
import com.amx.jax.scope.VendorContext.VendorScoped;
import com.amx.jax.scope.VendorContext.VendorValue;
import com.amx.jax.scope.VendorProperties;
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil;

@Component
@VendorScoped
public class VendorAuthConfig {

	@VendorValue("${vendor.auth.key}")
	String basicAuthPassword;

	@VendorValue("${vendor.auth.id}")
	String basicAuthUser;

	@VendorValue("${vendor.auth.ip}")
	String basicAuthIp;

	@VendorValue("${vendor.features}")
	private String[] features;

	@VendorValue("${vendor.master.features}")
	private String[] featuresMaster;

	@Autowired
	VendorProperties vendorProperties;

	private List<String> featuresList = null;
	private Map<String, Boolean> featuresListMap = null;

	public String getBasicAuthIp() {
		return basicAuthIp;
	}

	public String getBasicAuthPassword() {
		return basicAuthPassword;
	}

	public String getBasicAuthUser() {
		return basicAuthUser;
	}

	public boolean isRequestValid(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId,
			String authToken) {
		return authToken.equals(basicAuthPassword)
				|| CryptoUtil.validateHMAC(this.basicAuthPassword, traceId, authToken);
	}

	public List<String> getFeaturesList() {
		if (null == featuresList) {
			featuresList = new ArrayList<String>();
			VendorFeatures[] allFeatures = VendorFeatures.values();
			featuresListMap = new HashMap<String, Boolean>();

			Properties vendorPropertiesLocal = vendorProperties.getProperties();

			for (int i = 0; i < allFeatures.length; i++) {
				boolean isFeature = ArgUtil.parseAsBoolean(
						vendorPropertiesLocal.getProperty("vendor.features." + allFeatures[i]), false);
				if (isFeature) {
					featuresListMap.put(allFeatures[i].name(), true);
				} else {
					isFeature = ArgUtil.parseAsBoolean(
							vendorPropertiesLocal	
									.getProperty("vendor.features." + allFeatures[i].name().toUpperCase()),
							false);
					if (isFeature) {
						featuresListMap.put(allFeatures[i].name(), true);
					}
				}
			}
			if (features != null) {
				for (int i = 0; i < features.length; i++) {
					featuresListMap.put(features[i], true);
				}
			}

			for (Entry<String, Boolean> features : featuresListMap.entrySet()) {
				featuresList.add(features.getKey());
			}
		}
		return featuresList;
	}

	public boolean hasFeature(String feature) {
		this.getFeaturesList();
		return featuresListMap.containsKey(feature) && featuresListMap.get(feature);
	}

}
