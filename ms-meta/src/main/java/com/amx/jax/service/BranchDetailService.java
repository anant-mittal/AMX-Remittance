package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.amx.jax.model.response.BranchSystemDetailDto;
import com.amx.jax.services.AbstractService;
import com.amx.jax.validation.BranchDetailValidation;

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

	Logger logger = LoggerFactory.getLogger(getClass());

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
		BranchSystemDetail branchSystemDetail = branchDetailDao.getBranchSystemDetail(branchSystemIp);
		if (branchSystemDetail == null) {
			throw new GlobalException("No  branch system found for given IP ", JaxError.BRANCH_SYSTEM_NOT_FOUND);
		}
		if (!ConstantDocument.Yes.equals(branchSystemDetail.getIsActive())) {
			throw new GlobalException("Given branch system is inactive ", JaxError.BRANCH_SYSTEM_NOT_ACTIVE);
		}
		return branchSystemDetail;
	}

	public BranchSystemDetail findBranchSystemByInventoryId(BigDecimal countryBranchSystemInventoryId) {
		BranchSystemDetail branchSystemDetail = branchDetailDao
				.getBranchSystemDetailByInventoryId(countryBranchSystemInventoryId);
		if (branchSystemDetail == null) {
			throw new GlobalException("No  branch system found for given inv id ", JaxError.BRANCH_SYSTEM_NOT_FOUND);
		}

		return branchSystemDetail;
	}

	public List<BranchSystemDetail> listBranchSystemDetail() {
		return branchDetailDao.listBranchSystemDetail();
	}

	public List<BranchSystemDetailDto> listBranchSystemDetailDto() {
		List<BranchSystemDetail> branchSystemDetailList = listBranchSystemDetail();
		List<BranchSystemDetailDto> dtos = branchSystemDetailList.stream().map(i -> {
			BranchSystemDetailDto dto = new BranchSystemDetailDto();
			try {
				BeanUtils.copyProperties(dto, i);
			} catch (Exception e) {
				logger.error("error in copy branchsystem detail dto , " + e.getMessage());
			}
			return dto;
		}).collect(Collectors.toList());
		return dtos;
	}

	@Override
	public String getModelType() {
		return "branch-detail";
	}

	public AmxApiResponse<BranchSystemDetailDto, Object> listBranchSystemInventory() {
		return AmxApiResponse.buildList(listBranchSystemDetailDto());
	}

}
