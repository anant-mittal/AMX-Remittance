package com.amx.jax.sso;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;


@Component
public class SSOPermissionEvaluator implements PermissionEvaluator {
	private static Logger logger = LoggerFactory.getLogger(SSOPermissionEvaluator.class);
	
	@Autowired
	private SSOUser ssoUser;
	
	@Override
	public boolean hasPermission(Authentication authentication , Object permissionKey, Object permission) {
		EmployeeDetailsDTO userDetails = ssoUser.getUserDetails();
		
		if(userDetails != null) {
			Map<String, Map<String, String>> permissionMap = userDetails.getUserRole().getPermissionMap();
			if(permissionMap.get(permissionKey) != null && permissionMap.get(permissionKey).get(permission) != null) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return false;
	}

}
