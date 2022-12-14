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
import com.amx.jax.branchuser.service.BranchUserService;
import com.amx.jax.cache.box.CustomerOnCall;
import com.amx.jax.cache.box.CustomerOnCall.CustomerCall;
import com.amx.jax.client.JaxStompClient;
import com.amx.jax.client.branch.IBranchService;
import com.amx.jax.dbmodel.CustomerTeleMarketingDetails;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.repository.CustomerCallDetailsRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.CustomerTeleMarketingDetailsRepository;
import com.amx.jax.repository.EmployeeRespository;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;

@RestController
public class BranchUserController implements IBranchService {

	private static final Logger LOGGER = LoggerService.getLogger(BranchUserController.class);

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	EmployeeRespository employeeRespository;

	@Autowired
	CustomerOnCall customerOnCall;

	@Autowired
	JaxStompClient jaxStompClient;

	@Autowired
	CustomerCallDetailsRepository customerCallDetailsRepository;

	@Autowired
	CustomerTeleMarketingDetailsRepository customerTeleMarketingDetailsRepository;

	@Override
	@RequestMapping(value = Path.BRANCH_USER_CUSTOMER_CALL_SESSION, method = RequestMethod.POST)
	public AmxApiResponse<CustomerCall, Object> customerCallSession(
			@RequestParam BigDecimal agentId,
			@RequestParam(required = false) BigDecimal customerId,
			@RequestParam(required = true) BigDecimal leadId) {
		Employee e = employeeRespository.findEmployeeById(agentId);

		if (ArgUtil.is(e)) {
			String employeeId = ArgUtil.parseAsString(e.getEmployeeId());
			CustomerTeleMarketingDetails custTMDetails = null;
			if (ArgUtil.is(leadId)) {
				custTMDetails = CollectionUtil
						.getOne(customerTeleMarketingDetailsRepository.getCustomerTeleMarketingDetailsByLeadId(leadId));
			} else if (ArgUtil.is(customerId)) {
				custTMDetails = CollectionUtil
						.getOne(customerTeleMarketingDetailsRepository
								.getCustomerTeleMarketingDetailsByCustomerId(customerId));
			}

			if (ArgUtil.is(custTMDetails)) {
				CustomerCall customerCall = new CustomerCall();
				customerCall.setCustomerid(custTMDetails.getCustomerId());
				customerCall.setLeadId(custTMDetails.getLeadId());
				customerCall.setSessionId(AppContextUtil.getTraceId());

				custTMDetails.setEmployeeId(e.getEmployeeId());
				custTMDetails.setModifiedDate(new Date());
				customerTeleMarketingDetailsRepository.save(custTMDetails);

				customerOnCall.put(employeeId, customerCall);
				jaxStompClient.publishOnCallCustomerStatus(e.getEmployeeId(), custTMDetails.getCustomerId());
				return AmxApiResponse.build(customerCall);
			} else {
				customerOnCall.remove(employeeId);
				return AmxApiResponse.build(new CustomerCall()).statusEnum(ApiStatusCodes.FAIL)
						.message("Invalid Lead Id or Customer Id");
			}
		}
		return AmxApiResponse.build(new CustomerCall()).statusEnum(ApiStatusCodes.FAIL).message("Invalid Agent Id");
	}

	@Override
	@RequestMapping(value = Path.BRANCH_USER_CUSTOMER_CALL_STATUS, method = RequestMethod.POST)
	public AmxApiResponse<CustomerCall, Object> customerCallStatus(
			@RequestParam BigDecimal agentId,
			@RequestParam(required = false) BigDecimal customerId,
			@RequestParam(required = true) BigDecimal leadId,
			@RequestParam String followUpCode, @RequestParam String remark,
			@RequestParam(required = false) String sessionId) {
		Employee e = employeeRespository.findEmployeeById(agentId);
		CustomerCall call = new CustomerCall();
		if (ArgUtil.is(e)) {
			CustomerTeleMarketingDetails custTMDetails = null;
			if (ArgUtil.is(leadId)) {
				custTMDetails = CollectionUtil
						.getOne(customerTeleMarketingDetailsRepository.getCustomerTeleMarketingDetailsByLeadId(leadId));
			}

			if (ArgUtil.is(custTMDetails)) {
				custTMDetails.setRemark(remark);
				custTMDetails.setFollowUpCode(followUpCode);
				custTMDetails.setModifiedDate(new Date());
				custTMDetails.setEmployeeId(e.getEmployeeId());
				customerTeleMarketingDetailsRepository.save(custTMDetails);

				call.setCustomerid(custTMDetails.getCustomerId());
				call.setLeadId(custTMDetails.getLeadId());
				return AmxApiResponse.build(call).statusEnum(ApiStatusCodes.SUCCESS);
			}
			return AmxApiResponse.build(new CustomerCall()).statusEnum(ApiStatusCodes.FAIL)
					.message("Invalid Lead Id or Customer Id");
		}
		return AmxApiResponse.build(call).statusEnum(ApiStatusCodes.FAIL).message("Invalid Agent Id");
	}

	@Autowired
	MetaData metaData;

	@Autowired
	BranchUserService branchUserService;

	@RequestMapping(value = Path.BR_REMITTANCE_USER_WISE_COUNT, method = RequestMethod.GET)
	public AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(
			@RequestParam(value = Params.TRNX_DATE, required = false) String transactiondate) {
		LOGGER.info("user wise total getTotalCount " + transactiondate);
		return branchUserService.getTotalCount(transactiondate);
	}

}
