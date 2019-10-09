package com.amx.jax.apiwrapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.response.RoleMappingForEmployee;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;

@Component
public class JaxRbaacServiceWrapper {

	@Autowired
	RbaacServiceClient rbaacServiceClient;
	@Autowired
	MetaData metaData;

	public RoleMappingForEmployee getRoleMappingsForEmployee() {
		AmxApiResponse<RoleMappingForEmployee, Object> response = rbaacServiceClient.getRoleMappingsForEmployee(metaData.getEmployeeId(),
				metaData.getDeviceIp(), null, Boolean.TRUE);
		return response.getResult();
	}

	public Map<String, Map<String, String>> getEmployeePermissions() {
		AmxApiResponse<RoleMappingForEmployee, Object> response = rbaacServiceClient.getRoleMappingsForEmployee(metaData.getEmployeeId(),
				AppContextUtil.getUserClient().getIp(), null, Boolean.TRUE);
		Map<BigDecimal, RoleResponseDTO> roleInfoMap = response.getResult().getRoleInfoMap();
		Map<String, Map<String, String>> permissions = new HashMap<>();
		roleInfoMap.forEach((k, v) -> {
			permissions.putAll(v.getPermissionMap());
		});
		return permissions;
	}
}
