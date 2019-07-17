package com.amx.jax.insurance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.insurance.CustomerInsurance;
import com.amx.jax.dbmodel.insurance.InsuranceAction;
import com.amx.jax.dbmodel.insurance.InsuranceSetupMaster;
import com.amx.jax.dbmodel.insurance.InsurnaceClaimNominee;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.insurance.CreateOrUpdateNomineeRequest;
import com.amx.jax.model.request.insurance.OptInOutRequest;
import com.amx.jax.model.request.insurance.SaveInsuranceDetailRequest;
import com.amx.jax.model.response.insurance.GigInsuranceDetail;
import com.amx.jax.model.response.insurance.NomineeDetailDto;
import com.amx.jax.repository.insurance.CustomerInsuranceRepository;
import com.amx.jax.repository.insurance.InsuranceActionRepository;
import com.amx.jax.repository.insurance.InsurnaceClaimNomineeRepository;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.DateUtil;
import com.amx.utils.JsonUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GigInsuranceService {

	@Autowired
	MetaData metaData;
	@Autowired
	InsurnaceClaimNomineeRepository insurnaceClaimNomineeRepository;
	@Autowired
	CustomerInsuranceRepository customerInsuranceRepository;
	@Autowired
	CurrencyMasterService currencyMasterService;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	InsuranceActionRepository insuranceActionRepository;
	@Autowired
	CustomerDao customerDao;

	private static final Logger log = LoggerFactory.getLogger(GigInsuranceService.class);

	public GigInsuranceDetail fetchInsuranceDetail() {
		GigInsuranceDetail gigInsuranceDetail = new GigInsuranceDetail();
		CurrencyMasterModel currency = currencyMasterService.getCurrencyMasterById(metaData.getDefaultCurrencyId());
		gigInsuranceDetail.setCurrency(currencyMasterService.convertModel(currency));
		BigDecimal customerId = metaData.getCustomerId();
		List<InsurnaceClaimNominee> nominees = insurnaceClaimNomineeRepository.findByCustomerIdAndIsActive(customerId, ConstantDocument.Yes);
		CustomerInsurance insuranceDetail = customerInsuranceRepository.findByCustomerIdAndIsActive(customerId, ConstantDocument.Yes);
		BigDecimal policyAmount = insuranceDetail.getInsuranceSetupMaster().getCoverAmount();
		gigInsuranceDetail.setPolicyAmount(policyAmount);
		gigInsuranceDetail.setPolicyStartDate(insuranceDetail.getEffectiveDate());
		gigInsuranceDetail.setPolicyEndDate(insuranceDetail.getExpiryDate());
		List<NomineeDetailDto> nomineeDetail = new ArrayList<>();
		for (InsurnaceClaimNominee nominee : nominees) {
			NomineeDetailDto nomineeDto = new NomineeDetailDto();
			BeneficiaryListDTO beneDetail = beneficiaryService.getBeneficiaryByMasterSeqid(nominee.getCustomerId(), nominee.getBeneMasterSeqId());
			nomineeDto.setBeneficiaryListDTO(beneDetail);
			nomineeDto.setPercent(nominee.getSharePercent().intValue());
			nomineeDto.setNomineeId(nominee.getNomineeId());
			nomineeDetail.add(nomineeDto);
		}
		gigInsuranceDetail.setNomineeDetail(nomineeDetail);
		Customer customer = customerDao.getCustById(customerId);
		String isGigOptedInd = customer.getPremInsurance();
		Boolean isGigOpted = ConstantDocument.Yes.equalsIgnoreCase(isGigOptedInd);
		gigInsuranceDetail.setIsOptIn(isGigOpted);
		return gigInsuranceDetail;
	}

	@Transactional
	public void saveInsuranceDetail(SaveInsuranceDetailRequest request) {
		log.debug("saveInsuranceDetail {}", JsonUtil.toJson(request));
		validatesaveInsuranceDetailRequest(request);
		List<InsurnaceClaimNominee> customerInsuranceNominees = insurnaceClaimNomineeRepository.findByCustomerIdAndIsActive(metaData.getCustomerId(),
				ConstantDocument.Yes);

		customerInsuranceNominees.stream().forEach(i -> {
			Optional<CreateOrUpdateNomineeRequest> existing = request.getAddNomineeRequestData().stream().filter(p -> {
				if (i.getNomineeId().equals(p.getNomineeId())) {
					return true;
				}
				return false;
			}).findAny();
			// update
			if (existing.isPresent()) {
				log.debug("updating nominee id {}", i.getNomineeId());
				CreateOrUpdateNomineeRequest updateData = existing.get();
				if (updateData.getPercentage() != null) {
					i.setSharePercent(new BigDecimal(updateData.getPercentage()));
				}
				if (updateData.getBeneRelationshipSeqId() != null) {
					BigDecimal beneMasterSeqId = beneficiaryService.getBeneByIdNo(updateData.getBeneRelationshipSeqId()).getBeneficaryMasterSeqId();
					i.setBeneMasterSeqId(beneMasterSeqId);
				}
				i.setModifiedBy(getCreatedBy());
				i.setModifiedDate(new Date());
				i.setModifiedDeviceId(metaData.getDeviceId());
				i.setModifiedDeviceType(metaData.getDeviceType());
			} else {
				// delete
				log.debug("deleting nominee id {}", i.getNomineeId());
				i.setIsActive(ConstantDocument.Deleted);
			}
		});
		List<CreateOrUpdateNomineeRequest> newNominees = request.getAddNomineeRequestData().stream().filter(i -> {
			return i.getNomineeId() == null;
		}).collect(Collectors.toList());
		// create
		List<InsurnaceClaimNominee> newNomineesEntity = newNominees.stream().map(i -> {
			BigDecimal beneMasterSeqId = beneficiaryService.getBeneByIdNo(i.getBeneRelationshipSeqId()).getBeneficaryMasterSeqId();
			InsurnaceClaimNominee newNominee = new InsurnaceClaimNominee();
			newNominee.setApplicationCountryId(metaData.getCountryId());
			newNominee.setBeneMasterSeqId(beneMasterSeqId);
			newNominee.setCreatedBy(getCreatedBy());
			newNominee.setCreatedDate(new Date());
			newNominee.setCreatedDeviceId(metaData.getDeviceId());
			newNominee.setCustomerId(metaData.getCustomerId());
			newNominee.setIsActive(ConstantDocument.Yes);
			newNominee.setSharePercent(new BigDecimal(i.getPercentage()));
			return newNominee;
		}).collect(Collectors.toList());
		log.debug("adding {} nominees ", newNomineesEntity.size());
		customerInsuranceNominees.addAll(newNomineesEntity);
		insurnaceClaimNomineeRepository.save(customerInsuranceNominees);
	}

	private void validatesaveInsuranceDetailRequest(SaveInsuranceDetailRequest request) {

		List<CreateOrUpdateNomineeRequest> nominees = request.getAddNomineeRequestData();
		if (nominees.size() < 1) {
			throw new GlobalException("Minimum nominess must be 1");
		}
		if (nominees.size() > 4) {
			throw new GlobalException("Maximum nominess must be 4");
		}
		for (CreateOrUpdateNomineeRequest nominee : nominees) {
			if (nominee.getNomineeId() == null) {
				if (nominee.getBeneRelationshipSeqId() == null || nominee.getPercentage() == null) {
					throw new GlobalException("Either bene detail or percentage is blank");
				}
			} else {
				// existing nominee
				InsurnaceClaimNominee nomineeData = insurnaceClaimNomineeRepository.findOne(nominee.getNomineeId());
				if (nomineeData == null) {
					throw new GlobalException("Nominee not found with given id");
				}
				if (nominee.getPercentage() == null) {
					nominee.setPercentage(nomineeData.getSharePercent().intValue());
				}
				if (nominee.getBeneRelationshipSeqId() == null) {
					BeneficiaryListDTO beneDetail = beneficiaryService.getBeneficiaryByMasterSeqid(nomineeData.getCustomerId(),
							nomineeData.getBeneMasterSeqId());
					nominee.setBeneRelationshipSeqId(beneDetail.getIdNo());
				}
			}
		}
		validateNomineePercentage(nominees);
		validateDuplicateNominee(request);
	}

	private void validateDuplicateNominee(SaveInsuranceDetailRequest request) {

		long distinct = request.getAddNomineeRequestData().stream().filter(i -> {
			return i.getBeneRelationshipSeqId() != null;
		}).distinct().count();
		long count = request.getAddNomineeRequestData().size();
		if (distinct != count) {
			throw new GlobalException("Duplicate nominees found. please choose distict nominees");
		}
		List<BigDecimal> newNomineesBeneRelSeqIdList = request.getAddNomineeRequestData().stream().filter(i -> {
			return (i.getBeneRelationshipSeqId() != null) && (i.getNomineeId() == null);
		}).map(i -> i.getBeneRelationshipSeqId()).collect(Collectors.toList());
		List<BenificiaryListView> nomineeBeneRelationships = beneficiaryService.getBeneByIdNos(newNomineesBeneRelSeqIdList);
		List<InsurnaceClaimNominee> existingNominees = insurnaceClaimNomineeRepository.findByCustomerIdAndIsActive(metaData.getCustomerId(),
				ConstantDocument.Yes);
		Set<BigDecimal> newNomineesBeneMasterSeqIdSet = nomineeBeneRelationships.stream().map(i -> i.getBeneficaryMasterSeqId())
				.collect(Collectors.toSet());
		for (InsurnaceClaimNominee nominee : existingNominees) {
			if (newNomineesBeneMasterSeqIdSet.contains(nominee.getBeneMasterSeqId())) {
				throw new GlobalException("Nominee already added, can not add as new nominee. Use update to modify.");
			}
		}
	}

	private void validateNomineePercentage(List<CreateOrUpdateNomineeRequest> nominees) {

		int totalPercentage = nominees.stream().mapToInt(i -> i.getPercentage()).sum();
		if (totalPercentage != 100) {
			throw new GlobalException("Total nominee percentage must be 100");
		}
	}

	protected String getCreatedBy() {
		JaxChannel channel = metaData.getChannel();
		String user = ConstantDocument.JOAMX_USER;
		if (JaxChannel.ONLINE.equals(channel)) {
			user = metaData.getCustomerId() == null ? ConstantDocument.JOAMX_USER : metaData.getCustomerId().toString();
		}
		return user;
	}

	public Boolean hasAddedNominee(BigDecimal customerId) {
		List<InsurnaceClaimNominee> nominees = insurnaceClaimNomineeRepository.findByCustomerIdAndIsActive(customerId, ConstantDocument.Yes);
		return nominees.size() > 0;
	}

	public void optInOutInsurance(OptInOutRequest request) {
		log.debug("optInOutInsurance request {}", JsonUtil.toJson(request));
		BigDecimal customerId = metaData.getCustomerId();
		CustomerInsurance insuranceDetail = customerInsuranceRepository.findByCustomerIdAndIsActive(customerId, ConstantDocument.Yes);
		if (insuranceDetail == null) {
			throw new GlobalException("Insurnace detail not found");
		}
		if (request.getOptIn()) {
			optIn(request, insuranceDetail);
		} else {
			optOut(request, insuranceDetail);
		}

	}

	private void optOut(OptInOutRequest request, CustomerInsurance insuranceDetail) {

		InsuranceAction currentAction = insuranceActionRepository.findByActionId(insuranceDetail.getCurrenctActionId());
		if (currentAction.getOptOutDate() != null) {
			throw new GlobalException("Already opted out this insurance");
		}
		currentAction.setOptOutDate(new Date());
		try {
			currentAction.setOptOutDateAccount(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
		} catch (ParseException e) {
			log.error("error in parsedate in optIn function");
		}
		currentAction.setModifiedBy(getCreatedBy());
		currentAction.setModifiedDate(new Date());
		currentAction.setModifiedDeviceId(metaData.getDeviceId());
		currentAction.setModifiedDeviceType(metaData.getDeviceType());
		insuranceActionRepository.save(currentAction);
		Customer customer = customerDao.getCustById(metaData.getCustomerId());
		customer.setPremInsurance(ConstantDocument.No);
		customerDao.saveCustomer(customer);

	}

	private void optIn(OptInOutRequest request, CustomerInsurance insuranceDetail) {
		InsuranceSetupMaster setup = insuranceDetail.getInsuranceSetupMaster();
		if (!ConstantDocument.Yes.equals(setup.getOptInAllowed())) {
			throw new GlobalException("Opt in not allowed");
		}
		int optInHours = setup.getOtpInHours();
		if (!ConstantDocument.Yes.equals(setup.getOptInAllowed())) {
			throw new GlobalException("Opt in not allowed");
		}
		InsuranceAction currentAction = insuranceActionRepository.findByActionId(insuranceDetail.getCurrenctActionId());
		Date newOptInDate = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(currentAction.getOptInDate());
		now.add(Calendar.HOUR, optInHours);
		Date validOptInStartDate = now.getTime();
		if (newOptInDate.before(validOptInStartDate)) {
			throw new GlobalException("You can opt in again after " + optInHours + " hours from last opt date");
		}
		if (currentAction.getOptOutDate() != null) {
			// create new action
			InsuranceAction newAction = new InsuranceAction();
			newAction.setApplicationCountryId(metaData.getCountryId());
			newAction.setCreatedBy(getCreatedBy());
			newAction.setCreatedDate(new Date());
			newAction.setCreatedDeviceId(metaData.getDeviceId());
			newAction.setCreatedDeviceType(metaData.getDeviceType());
			newAction.setInsuranceId(insuranceDetail.getInsuranceId());
			newAction.setOptInDate(new Date());
			try {
				newAction.setOptInDateAccount(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
			} catch (ParseException e) {
				log.error("error in parsedate in optIn function");
			}
			insuranceActionRepository.save(newAction);
			insuranceDetail.setCurrenctActionId(newAction.getActionId());
			customerInsuranceRepository.save(insuranceDetail);
			Customer customer = customerDao.getCustById(metaData.getCustomerId());
			customer.setPremInsurance(ConstantDocument.Yes);
			customerDao.saveCustomer(customer);
		} else {
			throw new GlobalException("Already opted this insurance");
		}
	}
}
