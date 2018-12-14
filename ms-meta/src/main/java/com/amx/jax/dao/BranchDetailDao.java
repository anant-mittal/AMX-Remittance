package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.rowmapper.BranchDetailRowMapper;
import com.amx.jax.dbmodel.BranchDetailModel;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.repository.BranchSystemDetailRepository;

/**
 * @author Subodh Bhoir
 *
 */
@Component
public class BranchDetailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BranchSystemDetailRepository branchSystemDetailRepository;

	public static final String QUERY_BRANCH_LIST = "SELECT * from EX_COUNTRY_BRANCH  WHERE ISACTIVE='Y' AND "
			+ "COUNTRY_ID=?  and CORPORATE_STATUS='N' and branch_name != 'ONLINE' and HEAD_OFFICE_INDICATOR=0  and "
			+ " ADDRESS IS NOT NULL AND LATITUDE IS NOT NULL AND LONGITUDE IS NOT NULL ORDER BY BRANCH_NAME ASC";

	/**
	 * @param countryId
	 * @return get branch detail
	 */
	public List<BranchDetailModel> getBranchDetailList(BigDecimal countryId) {

		List<BranchDetailModel> branchDetail = jdbcTemplate.query(QUERY_BRANCH_LIST, new BranchDetailRowMapper(),countryId);
		return branchDetail;
	}

	public List<BranchSystemDetail> getBranchSystemDetail(BigDecimal countryBranchId) {
		return branchSystemDetailRepository.findByIsActiveAndCountryBranchId(ConstantDocument.Yes, countryBranchId);
	}

	public BranchSystemDetail getBranchSystemDetail(String ipAddress) {
		return branchSystemDetailRepository.findByIpAddress(ipAddress);
	}

	public BranchSystemDetail getBranchSystemDetailByInventoryId(BigDecimal countryBranchSystemInventoryId) {
		return branchSystemDetailRepository.findOne(countryBranchSystemInventoryId);
	}

	public List<BranchSystemDetail> listBranchSystemDetail() {
		return branchSystemDetailRepository.findByIsActive(ConstantDocument.Yes);
	}
}