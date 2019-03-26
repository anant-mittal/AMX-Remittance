package com.amx.jax.userservice.manager.kwt;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.InvalidCivilIdException;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DmsDocumentModel;
import com.amx.jax.dict.Tenant;
import com.amx.jax.error.JaxError;
import com.amx.jax.scope.TenantSpecific;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.dao.DmsDocumentDao;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.utils.Constants;

@Component
@TenantSpecific(value = { Tenant.KWT, Tenant.KWT2 })
public class UserValidationKwt implements CustomerValidation {
	Logger logger = Logger.getLogger(UserValidationKwt.class);
	@Autowired
	private CustomerIdProofDao idproofDao;

	@Autowired
	private ImageCheckDao imageCheckDao;

	@Autowired
	private DmsDocumentDao dmsDocDao;
	
	@Autowired
	private CustomerRepository customerRepo;

	private DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public void validateCustIdProofs(BigDecimal custId) {
		List<CustomerIdProof> idProofs = idproofDao.getCustomerIdProofs(custId);
		for (CustomerIdProof idProof : idProofs) {
			validateIdProof(idProof);
		}
		if (idProofs.isEmpty()) {
			throw new GlobalException(JaxError.NO_ID_PROOFS_AVAILABLE, "ID proofs not available, contact branch");
		}
	}

	private void validateIdProof(CustomerIdProof idProof) {

		String scanSystem = idProof.getScanSystem();
		if (idProof.getIdentityExpiryDate() != null && idProof.getIdentityExpiryDate().compareTo(new Date()) < 0) {
			throw new GlobalException(JaxError.ID_PROOF_EXPIRED, "Identity proof are expired");
		}
		if ("A".equals(scanSystem)) {
			List<CustomerIdProof> validIds = idproofDao
					.getCustomerImageValidation(idProof.getFsCustomer().getCustomerId(), idProof.getIdentityTypeId());
			if (validIds == null || validIds.isEmpty()) {
				throw new GlobalException(JaxError.ID_PROOFS_NOT_VALID, "Identity proof are expired or invalid");
			}
			for (CustomerIdProof id : validIds) {
				if (id.getIdentityExpiryDate() != null && id.getIdentityExpiryDate().compareTo(new Date()) < 0) {
					throw new GlobalException(JaxError.ID_PROOF_EXPIRED, "Identity proof are expired");
				}
			}

		} else if ("D".equals(scanSystem)) {
			Map<String, Object> imageChecks = imageCheckDao.dmsImageCheck(idProof.getIdentityTypeId(),
					idProof.getIdentityInt(), sdf.format(idProof.getIdentityExpiryDate()));
			if (imageChecks.get("docBlobId") != null && imageChecks.get("docFinYr") != null) {
				Integer docBlobIdInt = (Integer) imageChecks.get("docBlobId");
				Integer docFinYrInt = (Integer) imageChecks.get("docFinYr");
				List<DmsDocumentModel> dmsDocs = dmsDocDao.getDmsDocument(new BigDecimal(docBlobIdInt),
						new BigDecimal(docFinYrInt));
				if (dmsDocs == null || dmsDocs.isEmpty()) {
					throw new GlobalException(JaxError.ID_PROOFS_IMAGES_NOT_FOUND, "Identity proof images not found");
				}
			}
		} else {
			throw new GlobalException(JaxError.ID_PROOFS_SCAN_NOT_FOUND, "Identity proof scans not found");
		}
	}

	@Override
	public void validateCivilId(String civilId) {

		boolean isValid = isValid(civilId);
		if (!isValid) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " is not valid.");
		}
	}

	public boolean isValid(String civilId) {
		if (StringUtils.isEmpty(civilId)) {
			return false;
		}
		if (civilId.length() != 12) {
			return false;
		}
		int idcheckdigit = Character.getNumericValue(civilId.charAt(11));
		int[] multiFactor = { 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int cal = 0;
		for (int i = 0; i < 11; i++) {
			cal += multiFactor[i] * Character.getNumericValue(civilId.charAt(i));
		}
		int rem = cal % 11;
		int calcheckdigit = 11 - rem;
		if (calcheckdigit == 0 || calcheckdigit == 10) {
			return false;
		} else {
			if (calcheckdigit != idcheckdigit) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void validateEmailId(String emailId) {
		List<Customer> list = customerRepo.getCustomerByEmailId(emailId);	
		if (list != null && list.size()!=0) {
			throw new GlobalException(JaxError.ALREADY_EXIST_EMAIL, "Email Id already exist");
		}
		
	}

	@Override
	public void validateDuplicateMobile(String mobileNo) {
		List<Customer> list = customerRepo.getCustomerByMobileCheck(mobileNo);
		if (list != null && list.size()!=0) {
			throw new GlobalException(JaxError.ALREADY_EXIST_MOBILE, "Mobile Number already exist");
		}
		
	}

}
