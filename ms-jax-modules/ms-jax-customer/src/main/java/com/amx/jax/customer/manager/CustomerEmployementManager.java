package com.amx.jax.customer.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.EmployeeDetails;
import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;

@Component
public class CustomerEmployementManager {

	@Autowired
	CustomerEmployeeDetailsRepository customerEmployeeDetailsRepository;

	public CustomerEmploymentDetails createCustomerEmploymentDetail(Customer customer) {
		// --- Customer Employment Data
		CustomerEmploymentDetails employmentDetails = new CustomerEmploymentDetails();
		EmployeeDetails employmentData = customerEmployeeDetailsRepository.getCustomerEmploymentData(customer);
		if (employmentData != null) {
			employmentDetails.setEmployer(employmentData.getEmployerName());
			employmentDetails
					.setEmploymentTypeId(employmentData.getFsBizComponentDataByEmploymentTypeId().getComponentDataId());
			if (employmentData.getFsBizComponentDataByOccupationId() != null) {
				employmentDetails
						.setProfessionId(employmentData.getFsBizComponentDataByOccupationId().getComponentDataId());
			}
			employmentDetails.setStateId(employmentData.getFsStateMaster());
			employmentDetails.setDistrictId(employmentData.getFsDistrictMaster());
			employmentDetails.setCountryId(employmentData.getFsCountryMaster().getCountryId());
			employmentDetails.setArticleDetailsId(customer.getFsArticleDetails().getArticleDetailId());
			employmentDetails.setArticleId(customer.getFsArticleDetails().getFsArticleMaster().getArticleId());
			employmentDetails.setIncomeRangeId(customer.getFsIncomeRangeMaster().getIncomeRangeId());
		}
		return employmentDetails;
	}
}
