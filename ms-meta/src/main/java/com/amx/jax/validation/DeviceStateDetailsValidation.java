package com.amx.jax.validation;

import com.amx.amxlib.exception.jax.GlobalException;

public class DeviceStateDetailsValidation  {
		
	public void validateDeviceRegIdndImageURL(Integer deviceRegId,String imageUrlStr) {
		if(deviceRegId ==null || imageUrlStr==null) {
			throw new GlobalException("DeviceRegId or imageUrl should not be blank");
				}
		
	}


}