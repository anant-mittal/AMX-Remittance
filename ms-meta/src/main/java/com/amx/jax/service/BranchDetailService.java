package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dao.BranchDetailDao;
import com.amx.jax.dbmodel.BranchDetailModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.AbstractService;

/**
 * @author Subodh Bhoir
 *
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BranchDetailService extends AbstractService {

	@Autowired
	MetaData meta;
	
	@Autowired
	BranchDetailDao branchDetailDao;
	
	/**
	 * @return branch details
	 */
	public AmxApiResponse<BranchDetailModel, Object> getBracnchDetailResponse() {
		
		List<BranchDetailModel> branchDetailList = branchDetailDao.getBranchDetailList(meta.getCountryId());
		
		if (branchDetailList.isEmpty()) {
			throw new GlobalException("Branch Details are not available");
		} 
		return AmxApiResponse.buildList(branchDetailList);
	}
	
	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return "branch-detail";
	}

}
