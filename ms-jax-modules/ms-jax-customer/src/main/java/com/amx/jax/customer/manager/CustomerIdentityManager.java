package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dbmodel.BizComponentData;

@Component
public class CustomerIdentityManager {

	@Autowired
	BizcomponentDao bizcomponentDao;

	private static final Logger log = LoggerFactory.getLogger(CustomerIdentityManager.class);

	public Date generateDob(String identityInt, BigDecimal identityTypeId) {
		BizComponentData bizComponent = bizcomponentDao.getBizComponentDataByComponmentDataId(identityTypeId);
		String identityCode = bizComponent.getComponentCode();
		Date dob = null;
		String dobStr = null;
		if ("C".equals(identityCode) || "CN".equals(identityCode) || "BD".equals(identityCode)) {
			if (identityInt.charAt(0) == '2') {
				dobStr = identityInt.substring(5, 7) + "/" + identityInt.substring(3, 5) + "/19" + identityInt.substring(1, 3);
			} else {
				dobStr = identityInt.substring(5, 7) + "/" + identityInt.substring(3, 5) + "/20" + identityInt.substring(1, 3);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				dob = formatter.parse(dobStr);
			} catch (ParseException e) {
				log.error("error in parsing date", e);
			}
		}
		return dob;
	}
}
