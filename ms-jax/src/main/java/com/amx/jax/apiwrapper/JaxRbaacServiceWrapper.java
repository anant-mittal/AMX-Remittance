package com.amx.jax.apiwrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	public List<String> getEmployeeRoles() {
		AmxApiResponse<RoleMappingForEmployee, Object> response = rbaacServiceClient.getRoleMappingsForEmployee(metaData.getEmployeeId(),
				metaData.getDeviceIp(), null, Boolean.TRUE);
		Map<BigDecimal, RoleResponseDTO> roleInfoMap = response.getResult().getRoleInfoMap();
		List<String> roles = new ArrayList<String>();
		roleInfoMap.forEach((k, v) -> {
			roles.add(v.getRole());
		});
		return roles;
	}
}
