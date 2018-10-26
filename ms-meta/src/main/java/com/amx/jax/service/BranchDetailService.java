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
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.BranchDetailDao;
import com.amx.jax.dbmodel.BranchDetailModel;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.AbstractService;
import com.amx.jax.validation.BranchDetailValidation;
import com.amx.utils.NumberUtil;

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

	@Autowired
	BranchDetailValidation branchDetailValidation;

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
		branchDetailValidation.validateCountryBranchId(countryBranchId);
		List<BranchSystemDetail> branchDetailList = branchDetailDao.getBranchSystemDetail(countryBranchId);

		if (branchDetailList.isEmpty()) {
			throw new GlobalException("Branch system Details are not available");
		}
		return AmxApiResponse.buildList(branchDetailList);
	}

	public BranchSystemDetail findBranchSystemByIp(String branchSystemIp) {
		BranchSystemDetail branchSystemDetail = branchDetailDao.getBranchSystemDetail( branchSystemIp);
		if (branchSystemDetail == null) {
			throw new GlobalException("No  branch system found for given IP ", JaxError.BRANCH_SYSTEM_NOT_FOUND);
		}
		if (!ConstantDocument.Yes.equals(branchSystemDetail.getIsActive())) {
			throw new GlobalException("Given branch system is inactive ", JaxError.BRANCH_SYSTEM_NOT_ACTIVE);
		}
		return branchSystemDetail;
	}

	@Override
	public String getModelType() {
		return "branch-detail";
	}

}
