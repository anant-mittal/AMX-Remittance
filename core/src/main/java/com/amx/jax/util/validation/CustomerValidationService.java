package com.amx.jax.util.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.util.CountryUtil;

@Component
public class CustomerValidationService {

	@Autowired
	private CountryUtil util;

	public boolean validateIdentityInt(String civilId, String countryCode, BigDecimal identityType) {
		if(identityType.equals(ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID) || 
				identityType.equals(ConstantDocument.BIZ_COMPONENT_ID_NEW_CIVIL_ID)){
			return validateCivilId(civilId, countryCode);
		}
		if(identityType.equals(ConstantDocument.BIZ_COMPONENT_ID_GCC_ID)){
			return validateGccId(civilId, countryCode);
		}
		if(identityType.equals(ConstantDocument.BIZ_COMPONENT_ID_BEDOUIN_ID)){
			return validateBedouinId(civilId, countryCode);
		}
		
		return true;
	}
	
	public boolean validateCivilId(String civilId, String countryCode) {
		if (StringUtils.isEmpty(civilId)) {
			return false;
		}
		if (util.isKuwait(countryCode)) {
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

		} else if (util.isBahrain(countryCode)) {
			if (civilId.length() != 9) {
				return false;
			}
		}
		return true;
	}

	public String validateContact(String countryCode, String contactNo, char contactType) {
		if (util.isKuwait(countryCode)) {
			return validateKuwaitContact(contactNo, contactType);
		} else if (util.isOman(countryCode)) {
			return validateOmanContact(contactNo, contactType);
		} else if (util.isBahrain(countryCode)) {
			return validateBahrainContract(contactNo, contactType);
		}
		return "Wrong Paramter passed";
	}

	private String validateBahrainContract(String contactNo, char contactType) {
		String message = "Y";
		char firstChar = contactNo.charAt(0);

		if (contactType == 'R' && firstChar != '1') {
			message = "Residence Number should start with 1";
		} else if (contactType == 'M') {
			char[] charArr = { '3', '6', '7' };
			boolean startswith = false;
			for (char c : charArr) {
				if (c == firstChar) {
					startswith = true;
				}
			}
			if (!startswith) {
				message = "Mobile Number should start with 3,6,7";
			}
		}
		return message;

	}

	private String validateOmanContact(String contactNo, char contactType) {
		String message = "Y";
		int length = contactNo.trim().length();
		char firstChar = contactNo.charAt(0);
		if (length != 8) {
			message = "CONTACT NUMBER MUST BE IN 8 DIGIT";
		}
		if (contactType == 'R' && firstChar != '2') {
			message = "Residence Number should start with 2";
		} else if (contactType == 'M' && firstChar != '9') {
			message = "Mobile Number should start with 9";
		}
		return message;
	}

	private String validateKuwaitContact(String contactNo, char contactType) {
		String message = "Y";
		int length = contactNo.trim().length();
		if (length != 8) {
			return "CONTACT NUMBER MUST BE IN 8 DIGIT";
		}
		if (contactType == 'R') {
			char secondChar = contactNo.charAt(1);
			char firstChar = contactNo.charAt(0);
			char[] charArr = { '6', '7', '9' };
			for (char c : charArr) {
				if (c == secondChar) {
					message = "Residence second Number should not contain 6,7,9";
				}
			}
			if (firstChar != '2') {
				message = "Residence Number should start with 2";
			}
		} else if (contactType == 'M') {
			char firstChar = contactNo.charAt(0);
			char[] charArr = { '5', '6', '7', '9' };
			boolean startswith = false;
			for (char c : charArr) {
				if (c == firstChar) {
					startswith = true;
					break;
				}
			}
			if (!startswith) {
				message = "Mobile Number should start with 5,6,7,9";
			}
		}
		return message;
	}
	
	public boolean validateGccId(String civilId, String countryCode) {
		if (StringUtils.isEmpty(civilId)) {
			return false;
		}
		
		if (util.isKuwait(countryCode)) {
			if (civilId.length() != 15) {
				return false;
			}
		}
		return true;
	}
	
	public boolean validateBedouinId(String civilId, String countryCode) {
		if (StringUtils.isEmpty(civilId)) {
			return false;
		}
		
		if (util.isKuwait(countryCode)) {
			if (civilId.length() != 12) {
				return false;
			}
		}
		return true;
	}

}
