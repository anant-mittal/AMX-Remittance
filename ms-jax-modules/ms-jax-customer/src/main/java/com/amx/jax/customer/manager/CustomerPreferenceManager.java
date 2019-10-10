
package com.amx.jax.customer.manager;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.repository.CustomerRepository;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class CustomerPreferenceManager {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AuditService auditService;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	/**
	 * to update language by customer
	 * 
	 * @author Rabil
	 **/
	public String saveLanguage(BigDecimal customerId, BigDecimal languageId) {
		String status = null;
		// Audit Info
		CActivityEvent audit = new CActivityEvent(Type.LANG_UPDATE);
		audit.setCustomerId(customerId);
		audit.setLanguageId(languageId);
		Customer c = customerRepository.getActiveCustomerDetailsByCustomerId(customerId);
		if (c != null) {
			BigDecimal lanChanCount = c.getOnlineLanguageChangeCount() == null ? BigDecimal.ZERO
					: c.getOnlineLanguageChangeCount();
			c.setLanguageId(languageId);
			c.setOnlineLanguageChangeCount(lanChanCount.add(BigDecimal.ONE));
			customerRepository.save(c);
			status = "SUCCESS";
			auditService.log(audit.result(Result.DONE));
		} else {
			auditService.log(audit.result(Result.FAIL).message("Customer detail not found"));
		}

		return status;
	}

}
