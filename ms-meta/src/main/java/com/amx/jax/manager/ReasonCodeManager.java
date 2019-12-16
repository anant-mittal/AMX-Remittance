package com.amx.jax.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import com.amx.jax.dbmodel.ReasonCodeMaster;
import com.amx.jax.dbmodel.ReasonsRepository;
import com.amx.jax.model.meta.ReasonsDTO;
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ReasonCodeManager {
	@Autowired
	ReasonsRepository reasonDao;
	
	public List<ReasonsDTO> getReasonList(String reasonCodeCategory) {
		List<ReasonCodeMaster> reasonList = reasonDao.getReasonsList(reasonCodeCategory);
		return convertReasonDTO(reasonList);
	}
	
	public List<ReasonsDTO> convertReasonDTO(List<ReasonCodeMaster> reasonList) {

		List<ReasonsDTO> output = new ArrayList<>();
		for (ReasonCodeMaster model : reasonList) {
			ReasonsDTO rDto = new ReasonsDTO();
			rDto.setReasonCode(model.getReasonCode());
			rDto.setReasonCodeCategory(model.getReasonCodeCategory());
			rDto.setReasonCodeDesc(model.getReasonCodeDesc());
			rDto.setReasonCodeId(model.getReasonCodeId());
			output.add(rDto);
		}
		return output;
	}
}
