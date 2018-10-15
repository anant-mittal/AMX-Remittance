package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dao.BranchDetailDao;
import com.amx.jax.dbmodel.BranchDetailModel;
import com.amx.jax.dbmodel.BranchSystemDetail;
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

	/**
	 * @return branch system details
	 */
	public AmxApiResponse<BranchSystemDetail, Object> getBranchSystemDetailResponse(BigDecimal countryBranchId) {

		List<BranchSystemDetail> branchDetailList = branchDetailDao.getBranchSystemDetail(countryBranchId);

		if (branchDetailList.isEmpty()) {
			throw new GlobalException("Branch system Details are not available");
		}
		return AmxApiResponse.buildList(branchDetailList);
	}

	@Override
	public String getModelType() {
		return "branch-detail";
	}

}
