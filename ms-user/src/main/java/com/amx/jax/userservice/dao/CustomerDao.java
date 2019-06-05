package com.amx.jax.userservice.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.placeorder.PlaceOrderCustomer;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.ApplicationCoreProcedureDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.UserVerificationCheckListModel;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.ViewOnlineCustomerCheck;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.service.CompanyService;
import com.amx.jax.userservice.repository.LoyaltyPointRepository;
import com.amx.jax.userservice.repository.OnlineCustomerRepository;
import com.amx.jax.userservice.repository.UserVerificationCheckListModelRepository;
import com.amx.jax.userservice.repository.ViewOnlineCustomerCheckRepository;
import com.amx.jax.util.AmxDBConstants;
import com.amx.jax.util.CryptoUtil;
import com.google.common.collect.Lists;

@Component
public class CustomerDao {

	@Autowired
	private CustomerRepository repo;
	@Autowired
	private OnlineCustomerRepository onlineCustRepo;
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private UserVerificationCheckListModelRepository checkListrepo;
	@Autowired
	private ViewOnlineCustomerCheckRepository onlineCustViewRepo;
	@Autowired
	private MetaData meta;
	@Autowired
	private CryptoUtil cryptoUtil;
	@Autowired
	private LoyaltyPointRepository loyaltyPointRepo;
	@Autowired
	private ApplicationCoreProcedureDao applicationCoreProcedureDao;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	Logger LOGGER = LoggerFactory.getLogger(CustomerDao.class);

	public Customer getCustomerByCivilId(String civilId) {
		Customer cust = null;
		BigDecimal countryId = meta.getCountryId();
		List<Customer> customers = repo.getCustomerbyuser(countryId, civilId);
		if (customers != null && !customers.isEmpty()) {
			cust = customers.get(0);
		}
		return cust;
	}
	
	public List<Customer> findActiveCustomers(String identityInt) {
		return repo.findActiveCustomers(identityInt);
	}
	
	public List<Customer> getCustomerByIdentityInt(String identityInt) {
		return repo.getCustomerByIdentityInt(identityInt);
	}
	
	public Customer getActiveCustomerByIndentityIntAndType(String identityInt, BigDecimal identityType) {
		return repo.getActiveCustomerByIndentityIntAndType(identityInt, identityType);
	}

	public Customer getCustById(BigDecimal id) {
		return repo.findOne(id);
	}
	
	
	
	
	public List<PlaceOrderCustomer> getPersonInfoById(List<BigDecimal> customerIds) {
		List<PlaceOrderCustomer> poCustomers = new ArrayList<>();
		List<List<BigDecimal>> partitions = Lists.partition(customerIds, 999);
		for (List<BigDecimal> partition : partitions) {
			poCustomers.addAll(repo.findPOCustomersByIds(partition));
		}

		return poCustomers;
	}

	public CustomerOnlineRegistration getOnlineCustByCustomerId(BigDecimal customerId) {
		CustomerOnlineRegistration onlineCust = null;
		if (customerId != null) {
			List<CustomerOnlineRegistration> list = onlineCustRepo.getOnlineCustomersById(customerId);
			if (!CollectionUtils.isEmpty(list) && list.size() >= 1) {
				onlineCust = list.get(0);
			}
		}
		return onlineCust;
	}

	public CustomerOnlineRegistration getOnlineCustByUserId(String userName) {
		BigDecimal countryId = meta.getCountryId();
		CustomerOnlineRegistration customer = null;
		List<CustomerOnlineRegistration> customers = onlineCustRepo.getOnlineCustomerList(countryId, userName);
		if (!CollectionUtils.isEmpty(customers)) {
			customer = customers.get(0);
		}
		return customer;
	}

	@Transactional
	public CustomerOnlineRegistration saveOrUpdateOnlineCustomer(CustomerOnlineRegistration onlineCust,
			CustomerModel model) {
		String userId = onlineCust.getUserName();

		List<SecurityQuestionModel> secQuestions = model.getSecurityquestions();
		if (!CollectionUtils.isEmpty(secQuestions)) {
			setSecurityQuestions(secQuestions, onlineCust);
		}
		if (model.getCaption() != null) {
			onlineCust.setCaption(cryptoUtil.encrypt(userId, model.getCaption()));
		}
		if (model.getImageUrl() != null) {
			onlineCust.setImageUrl(model.getImageUrl());
		}

		if (model.getLoginId() != null) {
			onlineCust.setLoginId(model.getLoginId());
		}
		
		if (model.getPassword() != null) {
			onlineCust.setPassword(cryptoUtil.getHash(userId, model.getPassword()));
		}
		
		//update new email id
		if (model.getEmail() != null) {
			onlineCust.setEmail(model.getEmail());
		}
		
		//update new mobile number
		if (model.getMobile() != null) {
			onlineCust.setMobileNumber(model.getMobile());
		}
		onlineCustRepo.save(onlineCust);
		
		if (model.getEmail() != null || model.getMobile() != null) {
			Customer cust = getCustomerByCountryAndUserId(onlineCust.getCountryId(), userId);
			if (model.getEmail() != null) {
				cust.setEmail(model.getEmail());
			}else {
				cust.setMobile(model.getMobile());
			}
			if(cust.getUpdatedBy() == null)
				cust.setUpdatedBy(cust.getCreatedBy());		

			customerRepo.save(cust);
		}
		
		return onlineCust;
	}

	public void setSecurityQuestions(List<SecurityQuestionModel> secQuestions, CustomerOnlineRegistration onlineCust) {
		String userId = onlineCust.getUserName();
		if (secQuestions.get(0).getAnswer() != null) {
			onlineCust.setSecurityQuestion1(secQuestions.get(0).getQuestionSrNo());
			onlineCust.setSecurityAnswer1(cryptoUtil.getHash(userId, secQuestions.get(0).getAnswer()));
		}

		if (secQuestions.get(1).getAnswer() != null) {
			onlineCust.setSecurityQuestion2(secQuestions.get(1).getQuestionSrNo());
			onlineCust.setSecurityAnswer2(cryptoUtil.getHash(userId, secQuestions.get(1).getAnswer()));
		}

		if (secQuestions.get(2).getAnswer() != null) {
			onlineCust.setSecurityQuestion3(secQuestions.get(2).getQuestionSrNo());
			onlineCust.setSecurityAnswer3(cryptoUtil.getHash(userId, secQuestions.get(2).getAnswer()));
		}
		if (secQuestions.get(3).getAnswer() != null) {
			onlineCust.setSecurityQuestion4(secQuestions.get(3).getQuestionSrNo());
			onlineCust.setSecurityAnswer4(cryptoUtil.getHash(userId, secQuestions.get(3).getAnswer()));
		}
		if (secQuestions.get(4).getAnswer() != null) {
			onlineCust.setSecurityQuestion5(secQuestions.get(4).getQuestionSrNo());
			onlineCust.setSecurityAnswer5(cryptoUtil.getHash(userId, secQuestions.get(4).getAnswer()));
		}
	}

	@Transactional
	public void saveOnlineCustomer(CustomerOnlineRegistration onlineCust) {
		onlineCustRepo.save(onlineCust);
	}

	public CustomerOnlineRegistration getCustomerByLoginId(String loginId) {
		return onlineCustRepo.getOnlineCustomersByLoginId(loginId);
	}

	public CustomerOnlineRegistration getOnlineCustomerByCustomerId(BigDecimal customerId) {
		List<CustomerOnlineRegistration> list = onlineCustRepo.getOnlineCustomersById(customerId);
		CustomerOnlineRegistration onlinecust = null;
		if (list != null) {
			onlinecust = list.get(0);
		}
		return onlinecust;
	}

	public CustomerOnlineRegistration getOnlineCustomerByLoginIdOrUserName(String loginId) {
		return onlineCustRepo.getOnlineCustomerByLoginIdOrUserName(loginId);
	}
	
	public CustomerOnlineRegistration getCustomerIDByuserId(String loginId) {
		return onlineCustRepo.getCustomerIDByuserId(loginId);
	}
	

	public List<CustomerOnlineRegistration> getOnlineCustomerWithStatusByLoginIdOrUserName(String loginId) {
		return onlineCustRepo.getOnlineCustomerWithStatusByLoginIdOrUserName(loginId);
	}
	
	public UserVerificationCheckListModel getCheckListForUserId(String civilId) {
		return checkListrepo.findOne(civilId);
	}

	public ViewOnlineCustomerCheck getOnlineCustomerview(BigDecimal countryId, String civilId) {
		ViewOnlineCustomerCheck view = null;
		List<ViewOnlineCustomerCheck> custViewRepoList = onlineCustViewRepo.civilIdCheckForOnlineUser(countryId,
				civilId);
		if (!CollectionUtils.isEmpty(custViewRepoList)) {
			view = custViewRepoList.get(0);
		}
		return view;
	}

	public ViewOnlineCustomerCheck getOnlineCustomerview(BigDecimal custId) {
		return onlineCustViewRepo.findOne(custId);
	}

	@Transactional
	public void updatetLoyaltyPoint(BigDecimal custId) {
		Customer customer = getCustById(custId);
		BigDecimal loyalityPoints = loyaltyPointRepo.getLoyaltyPoints(customer.getCustomerReference());
		customer.setLoyaltyPoints(loyalityPoints);
		repo.save(customer);
	}
	
	public Customer getCustomerByCountryAndUserId(BigDecimal countryId,String userId) {
		List<Customer> list = customerRepo.getCustomerbyuser(countryId, userId);
		Customer customer = null;
		if (list != null) {
			customer = list.get(0);
		}
		return customer;
	}
	
	public Customer getCustomerByMobile(String mobile) {
		List<Customer> list = customerRepo.getCustomerByMobile(mobile);
		Customer customer = null;
		if (list != null && list.size()!=0) {
			customer = list.get(0);
		}
		return customer;
	}
	
	public void saveCustomer(Customer c) {
		customerRepo.save(c);
	}
	
	public BigDecimal generateCustomerReference() {
		ViewCompanyDetails company = companyService.getCompanyDetail();
		BigDecimal docFinYear = new BigDecimal(2001);

		Map<String, Object> output = applicationCoreProcedureDao.callProcedureCustReferenceNumber(
				company.getCompanyCode(), ConstantDocument.DOCUMENT_CODE_CUSTOMER_SERIAL_NUMBER, docFinYear);
		return (BigDecimal) output.get("P_DOCNO");
	}

	public List<Customer> getCustomerByIdentityInt(String identityInt, BigDecimal identityType) {
		return repo.getCustomerByIdentityInt(identityInt, identityType);
	}

	public List<Customer> findActiveCustomers(String identityInt, BigDecimal identityType) {
		return repo.findActiveCustomers(identityInt, identityType);
	}
	
	public List<Customer> getActiveCustomerByIndentityIntAndTypeAndIsActive(String identityInt, BigDecimal identityType, List<String> status){
		return repo.getCustomerByIndentityIntAndTypeAndIsactive(identityInt, identityType, status);
	}

	/**
	 * moves customer data to cusmas table from fs_customer table
	 * @param customerId
	 * @return
	 */
	public Map<String, Object> callProcedurePopulateCusmas(BigDecimal customerId) {

		LOGGER.info("callProcedurePopulateCusmas customerid: " + customerId);
		Map<String, Object> output = null;
		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
				new SqlOutParameter("P_ERROR_IND", Types.VARCHAR), // 2
				new SqlOutParameter("P_ERROR_MSG", Types.VARCHAR) // 3
		);
		CallableStatementCreator callableStatement = (Connection con) -> {
			String proc = " { call EX_POPULATE_CUSMAS (?, ?, ?) } ";
			CallableStatement cs = con.prepareCall(proc);
			cs.setBigDecimal(1, customerId);
			cs.registerOutParameter(2, java.sql.Types.VARCHAR);
			cs.registerOutParameter(3, java.sql.Types.VARCHAR);
			return cs;

		};
		output = jdbcTemplate.call(callableStatement, declareInAndOutputParameters);
		if (!AmxDBConstants.No.equals(output.get("P_ERROR_IND")) || output.get("P_ERROR_MSG") != null) {
			LOGGER.error("Error in callProcedurePopulateCusmas, P_ERROR_IND: " + output.get("P_ERROR_IND")
					+ " P_ERROR_MSG: " + output.get("P_ERROR_MSG"));
		}
		return output;
	}

	public List<Customer> findDuplicateCustomerRecords(BigDecimal nationality, String mobile, String email,
			String firstName) {
		return repo.getCustomerForDuplicateCheck(nationality, mobile, email, firstName);
	}
}
