package com.amx.jax;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.repository.JaxConfigRepository;
import com.amx.jax.util.ConverterUtil;

@Component
public class JaxServiceStartUpInit {

	@Autowired
	ICompanyDAO companyDao;

	@Autowired
	JaxConfigRepository jaxConfigRepository;

	@Autowired
	ConverterUtil converterutil;

	@Autowired
	OtpSettings otpSettings;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	public void initializeJaxApplication() {
		initializeConfigs();
	}

	private void initializeConfigs() {
		JaxConfig jaxConfig = jaxConfigRepository.findByType("OTP_SETTINGS");
		final int WRONG_OTP_ATTEMPTS_ALLOWED = 3;

		if (jaxConfig != null) {
			OtpSettings setting = converterutil.readValue(jaxConfig.getValue(), OtpSettings.class);
			try {
				BeanUtils.copyProperties(otpSettings, setting);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("error in initializeConfigs", e);
			}
		} else {
			// default config
			otpSettings.setMaxValidateOtpAttempts(WRONG_OTP_ATTEMPTS_ALLOWED);
			otpSettings.setMaxSendOtpAttempts(3);
			otpSettings.setOtpValidityTime(3); // minutes
		}

	}
}
