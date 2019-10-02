package com.amx.jax.serviceprovider;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.userservice.service.UserService;

@Component
public class ServiceProviderBeneDataManager {

	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	public void setBeneficiaryDtoDbValues(Map<String, Object> remitApplParametersMap, ServiceProviderCallRequestDto serviceProviderCallRequestDto) {
		BigDecimal beneIdNo = (BigDecimal) remitApplParametersMap.get("P_BENEFICIARY_RELASHIONSHIP_ID");
		Customer customer = userService.getCustById(metaData.getCustomerId());
		BenificiaryListView beneficiary = beneficiaryService.getBeneByIdNo(beneIdNo);
		Benificiary beneDto = serviceProviderCallRequestDto.getBeneficiaryDto();
		String lastName = beneficiary.getSecondName();
		String middleName = null;
		if (beneficiary.getThirdName() != null) {
			lastName = beneficiary.getThirdName();
			middleName = beneficiary.getSecondName();
			;
		}
		beneDto.setLast_name(lastName);
		beneDto.setFirst_name(beneficiary.getFirstName());
		beneDto.setMiddle_name(middleName);
		beneDto.setDate_of_birth(beneficiary.getDateOfBirth());
		beneDto.setEmail(customer.getEmail());
		beneDto.setContact_no(beneficiaryService.getBeneficiaryContactNumber(beneficiary.getBeneficaryMasterSeqId()));
	}

}
