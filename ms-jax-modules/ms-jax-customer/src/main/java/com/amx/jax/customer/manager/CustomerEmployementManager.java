package com.amx.jax.customer.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.customer.service.OffsitCustRegService;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCoreDetailsView;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.EmployeeDetails;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.model.request.LocalAddressDetails;
import com.amx.jax.model.request.UpdateCustomerEmploymentDetailsReq;
import com.amx.jax.repository.CustomerCoreDetailsRepository;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.userservice.service.ContactDetailService;

@Component
public class CustomerEmployementManager {

	@Autowired
	CustomerEmployeeDetailsRepository customerEmployeeDetailsRepository;
	@Autowired
	ArticleDao articleDao;
	@Autowired
	BizcomponentDao bizcomponentDao;
	@Autowired
	CustomerCoreDetailsRepository customerCoreDetailsRepositroy;
	@Autowired
	OffsitCustRegService offsitCustRegService;
	@Autowired
	ContactDetailService contactDetailService;

	private static final Logger log = LoggerFactory.getLogger(CustomerEmployementManager.class);

	public CustomerEmploymentDetails createCustomerEmploymentDetail(Customer customer) {
		// --- Customer Employment Data
		CustomerEmploymentDetails employmentDetails = new CustomerEmploymentDetails();
		EmployeeDetails employmentData = customerEmployeeDetailsRepository.getCustomerEmploymentData(customer);
		String articleDesc = null;

		if (null != customer.getFsArticleDetails()) {
			if (null != customer.getFsArticleDetails().getArticleDetailId()) {
				articleDesc = articleDao.getArticleDesc(customer);
			}
		}
		CustomerCoreDetailsView customercoreView = customerCoreDetailsRepositroy.findByCustomerID(customer.getCustomerId());
		if (employmentData != null) {
			employmentDetails.setEmployer(employmentData.getEmployerName());
			if (employmentData.getFsBizComponentDataByEmploymentTypeId() != null) {
				employmentDetails.setEmploymentTypeId(employmentData.getFsBizComponentDataByEmploymentTypeId().getComponentDataId());
			}
			if (employmentData.getFsBizComponentDataByOccupationId() != null) {
				employmentDetails.setProfessionId(employmentData.getFsBizComponentDataByOccupationId().getComponentDataId());
			}
			employmentDetails.setStateId(employmentData.getFsStateMaster());
			employmentDetails.setDistrictId(employmentData.getFsDistrictMaster());
			if (employmentData.getFsCountryMaster() != null) {
				employmentDetails.setCountryId(employmentData.getFsCountryMaster().getCountryId());
			}
			employmentDetails.setArticleDetailsId(customer.getFsArticleDetails().getArticleDetailId());
			employmentDetails.setArticleId(customer.getFsArticleDetails().getFsArticleMaster().getArticleId());
			employmentDetails.setIncomeRangeId(customer.getFsIncomeRangeMaster().getIncomeRangeId());
			if (customercoreView != null) {
				employmentDetails.setDesignation(customercoreView.getDesignation());
			}
			if (articleDesc != null) {
				employmentDetails.setArticleDesc(articleDesc);
			}
		}
		return employmentDetails;
	}

	public void updateCustomerEmploymentInfo(Customer customer, UpdateCustomerEmploymentDetailsReq req) {

		log.debug("in updateCustomerEmploymentInfo");
		EmployeeDetails employeeModel = customerEmployeeDetailsRepository.getCustomerEmploymentData(customer);
		if (employeeModel == null) {
			// create emplyee info record
			CustomerEmploymentDetails customerEmployementDetail = new CustomerEmploymentDetails(req.getEmploymentTypeId(), req.getProfessionId(),
					req.getEmployer(), req.getArticleDetailsId(), req.getIncomeRangeId());
			ContactDetail localContactDetail = contactDetailService.getContactsForLocal(customer);
			LocalAddressDetails localAddressDetail = new LocalAddressDetails();
			if (localContactDetail != null) {
				CountryMaster countryMaster = localContactDetail.getFsCountryMaster();
				StateMaster stateMaster = localContactDetail.getFsStateMaster();
				DistrictMaster districtMaster = localContactDetail.getFsDistrictMaster();
				if (countryMaster != null) {
					localAddressDetail.setCountryId(countryMaster.getCountryId());
				}
				if (stateMaster != null) {
					localAddressDetail.setStateId(stateMaster.getStateId());
				}
				if (districtMaster != null) {
					localAddressDetail.setDistrictId(districtMaster.getDistrictId());
				}
			}
			offsitCustRegService.commitEmploymentDetails(customerEmployementDetail, customer, localAddressDetail);
			return;
		}
		// need docs
		if (req.getEmployer() != null) {
			employeeModel.setEmployerName(req.getEmployer());
		}
		if (req.getArticleDetailsId() != null) {
			customer.setFsArticleDetails(articleDao.getArticleDetailsByArticleDetailId(req.getArticleDetailsId()));
		}
		if (req.getIncomeRangeId() != null) {
			customer.setFsIncomeRangeMaster(articleDao.getIncomeRangeMasterByIncomeRangeId(req.getIncomeRangeId()));
		}
		// no need of doc upload
		if (req.getEmploymentTypeId() != null) {
			employeeModel.setFsBizComponentDataByEmploymentTypeId(bizcomponentDao.getBizComponentDataByComponmentDataId(req.getEmploymentTypeId()));
		}
		if (req.getProfessionId() != null) {
			employeeModel.setFsBizComponentDataByOccupationId(bizcomponentDao.getBizComponentDataByComponmentDataId(req.getProfessionId()));
		}
		customerEmployeeDetailsRepository.save(employeeModel);
	}
}
