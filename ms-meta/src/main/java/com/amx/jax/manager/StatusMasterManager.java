package com.amx.jax.manager;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import com.amx.amxlib.meta.model.ViewStatusDto;

import com.amx.jax.dbmodel.fx.StatusMaster;

import com.amx.jax.repository.IViewStatus;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class StatusMasterManager {
	
	@Autowired
	IViewStatus statusDao;
	
	
	
	
	public List<StatusMaster> searchOrder(){
		return statusDao.getStatusList();
		}
		
		public List<ViewStatusDto> getsearchOrder(){
		List<StatusMaster> statusMaster = searchOrder();
		return convert(statusMaster);
		}
		
		
		public List<ViewStatusDto> convert(List<StatusMaster> statusMaster){
		List<ViewStatusDto> statusDto = new ArrayList<>();
		if(statusMaster != null && statusMaster.size() != 0) {
				for(StatusMaster viewStatusMaster : statusMaster) {
					ViewStatusDto dto = new ViewStatusDto();
					dto.setCreatedBy(viewStatusMaster.getCreatedBy());
					dto.setCreationDate(viewStatusMaster.getCreationDate());
					dto.setIsActive(viewStatusMaster.getIsActive());
					dto.setModifiedBy(viewStatusMaster.getModifiedBy());
					dto.setModifiedDate(viewStatusMaster.getModifiedDate());
					dto.setStatusCode(viewStatusMaster.getStatusCode());
					dto.setStatusDescription(viewStatusMaster.getStatusDescription());
					dto.setStatusMasterId(viewStatusMaster.getStatusMasterId());
					statusDto.add(dto);
				}
			}
				
				return statusDto;
	}
		
}
