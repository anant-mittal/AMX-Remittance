package com.amx.jax.userservice.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserVerificationCheckListDTO;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.UserVerificationCheckListModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.userservice.repository.UserVerificationCheckListModelRepository;

@Component
public class CheckListManager {

	@Autowired
	private UserVerificationCheckListModelRepository checkListrepo;

	@Autowired
	private OnlineCustomerRepository custRepo;

	public void updateMobileAndEmailCheck(CustomerOnlineRegistration onlineCust, UserVerificationCheckListModel model) {

		if (model == null) {
			model = createNewCheckList(onlineCust);
		}
		model.setEmailVerified("Y");
		model.setMobileVerified("Y");
		checkListrepo.save(model);

	}

	private UserVerificationCheckListModel createNewCheckList(CustomerOnlineRegistration onlineCust) {
		UserVerificationCheckListModel model = new UserVerificationCheckListModel();
		model.setCreatedBy(onlineCust.getUserName());
		model.setUpdatedBy(onlineCust.getUserName());
		model.setLoginId(onlineCust.getUserName());
		model.setIsActive("N");
		model.setCreatedDate(new Date());
		return model;
	}

	public void updateCustomerChecks(CustomerOnlineRegistration onlineCust, CustomerModel model) {
		UserVerificationCheckListModel existingCheckList = checkListrepo.findOne(onlineCust.getUserName());
		if (existingCheckList == null) {
			existingCheckList = createNewCheckList(onlineCust);
		}
		if (!CollectionUtils.isEmpty(model.getSecurityquestions())) {
			existingCheckList.setSequrityQuestion("Y");
		}
		if (model.getImageUrl() != null) {
			existingCheckList.setSequriryPhishingImage("Y");
		}
		checkListrepo.save(existingCheckList);

	}

	public UserVerificationCheckListDTO getUserCheckList(String userId) {
		CustomerOnlineRegistration customer = custRepo.getOnlineCustomerByLoginIdOrUserName(userId);
		UserVerificationCheckListModel checkList = checkListrepo.findOne(customer.getUserName());
		if (checkList == null) {
			throw new GlobalException("No User verfification record found",
					JaxError.USER_VERFICATION_RECORD_NOT_FOUND);
		}
		return convert(checkList);
	}

	private UserVerificationCheckListDTO convert(UserVerificationCheckListModel model) {
		UserVerificationCheckListDTO dto = new UserVerificationCheckListDTO();
		dto.setEmailVerified(model.getEmailVerified());
		dto.setKycVerified(model.getKycVerified());
		dto.setLoginId(model.getLoginId());
		dto.setMobileVerified(model.getMobileVerified());
		dto.setSequriryPhishingImage(model.getSequriryPhishingImage());
		dto.setTermsAndCondtion(model.getTermsAndCondtion());
		return dto;
	}

}
