package com.amx.jax.branch.api;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.cache.box.CustomerOnCall;
import com.amx.jax.cache.box.CustomerOnCall.CustomerCall;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCallDetails;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.repository.CustomerCallDetailsRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.EmployeeRespository;
import com.amx.jax.scope.VendorContext.ApiVendorHeaders;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;

@RestController
public class BranchUserController {

	private static final Logger LOGGER = LoggerService.getLogger(BranchUserController.class);

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	EmployeeRespository employeeRespository;

	@Autowired
	CustomerOnCall customerOnCall;

	@Autowired
	CustomerCallDetailsRepository customerCallDetailsRepository;

	@ApiVendorHeaders
	@RequestMapping(value = "/branch-user/customer-call-session", method = RequestMethod.POST)
	public AmxApiResponse<CustomerCall, Object> customerConnnected(@RequestParam BigDecimal agentId,
			@RequestParam String mobile) {
		Employee e = employeeRespository.findEmployeeById(agentId);
		if (ArgUtil.is(e)) {
			String employeeId = ArgUtil.parseAsString(e.getEmployeeId());
			Customer c = CollectionUtil.getOne(customerRepository.getCustomerByMobile(mobile));
			if (ArgUtil.is(c)) {
				CustomerCall customerCall = new CustomerCall();
				customerCall.setCustomerid(c.getCustomerId());
				customerCall.setSessionId(AppContextUtil.getTraceId());
				customerCall.setMobile(mobile);
				customerOnCall.put(employeeId, customerCall);
				return AmxApiResponse.build(customerCall);
			} else {
				customerOnCall.remove(employeeId);
			}
		}
		return AmxApiResponse.build(new CustomerCall()).statusEnum(ApiStatusCodes.FAIL);
	}

	@ApiVendorHeaders
	@RequestMapping(value = "/branch-user/customer-call-status", method = RequestMethod.POST)
	public AmxApiResponse<CustomerCall, Object> customerConnnectedStatus(@RequestParam BigDecimal agentId,
			@RequestParam(required = false) String mobile, @RequestParam(required = false) String sessionId,
			@RequestParam String status, @RequestParam String comment) {
		Employee e = employeeRespository.findEmployeeById(agentId);
		CustomerCall call = null;
		if (ArgUtil.is(e)) {
			String employeeId = ArgUtil.parseAsString(e.getEmployeeId());
			call = customerOnCall.get(employeeId);
			Customer c = null;
			if (ArgUtil.is(sessionId) && ArgUtil.is(call) && call.getSessionId().equals(sessionId)) {
				c = customerRepository.getCustomerByCustomerId(call.getCustomerid());
			} else if (ArgUtil.is(mobile) && ArgUtil.is(call) && call.getMobile().equals(mobile)) {
				c = customerRepository.getCustomerByCustomerId(call.getCustomerid());
			} else if (ArgUtil.is(mobile)) {
				c = CollectionUtil.getOne(customerRepository.getCustomerByMobile(mobile));
			}
			if (ArgUtil.is(c)) {
				CustomerCallDetails customerCallDetail = new CustomerCallDetails();
				customerCallDetail.setSession(sessionId);
				customerCallDetail.setCreatedDate(new Date());
				customerCallDetail.setEmployeeId(e.getEmployeeId());
				customerCallDetail.setCustomerId(c.getCustomerId());
				customerCallDetail.setMobile(mobile);
				customerCallDetail.setStatus(status);
				customerCallDetail.setComment(comment);
				customerCallDetailsRepository.save(customerCallDetail);
				return AmxApiResponse.build(call).statusEnum(ApiStatusCodes.SUCCESS);
			}

		}
		return AmxApiResponse.build(call).statusEnum(ApiStatusCodes.FAIL);
	}

}
