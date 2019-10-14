package com.amx.jax.branchbene;

import static com.amx.amxlib.constant.NotificationConstants.BRANCH_SEARCH;
import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.RoutingBankMasterParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceImpl;
import com.amx.jax.client.bene.BeneficiaryConstant;
import com.amx.jax.client.bene.BeneficiaryConstant.BeneStatus;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryMasterDesc;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.dbmodel.bene.BeneficaryStatus;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.AbstractBeneDetailDto;
import com.amx.jax.model.request.AbtractUpdateBeneDetailDto;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.BankBranchListRequest;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.request.benebranch.ListBeneRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.AddBeneBankBranchRequestModel;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.benebranch.BeneStatusDto;
import com.amx.jax.model.response.benebranch.UpdateBeneStatus;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.BeneficaryStatusRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.trnx.BeneficiaryTrnxManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.JsonUtil;

@Service
public class BeneBranchService {

	private Logger logger = LoggerFactory.getLogger(BeneBranchService.class);

	@Autowired
	MetaData metaData;
	@Autowired
	BankService bankService;
	@Autowired
	MetaService metaService;
	@Autowired
	BeneficiaryService beneService;
	@Autowired
	BankMetaService bankMetaService;
	@Autowired
	BeneficiaryValidationService beneficiaryValidationService;
	@Autowired
	BeneficiaryTrnxManager beneficiaryTrnxManager;
	@Autowired
	CountryService countryService;
	@Autowired
	UserService userService;
	@Autowired
	JaxProperties jaxProperties;
	@Autowired
	PostManService postManService;
	@Autowired
	BeneBranchManager beneBranchManager;
	@Autowired
	ITransactionHistroyDAO iTransactionHistroyDAO;
	@Autowired
	BeneficaryStatusRepository beneficaryStatusRepository;

	// bank
	public List<BankMasterDTO> getBankByCountryAndCurrency(ListBeneBankOrCashRequest request) {
		List<BankMasterDTO> bankMasterDtolist = null;
		bankMasterDtolist = bankService.getBankByCountryAndCurrency(request.getCountryId(), request.getCurrencyId());
		return bankMasterDtolist;
	}

	// cash
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<RoutingBankMasterDTO> getServiceProviderList(ListBeneBankOrCashRequest request) {
		ServiceGroupMaster cashserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_CASH);
		RoutingBankMasterServiceImpl param = new RoutingBankMasterParam.RoutingBankMasterServiceImpl(metaData.getCountryId(), request.getCountryId(),
				cashserviceMaster.getServiceGroupId());
		ApiResponse serviceProviderResponse = beneService.getServiceProviderList(param);
		List<RoutingBankMasterDTO> serviceProviderList = serviceProviderResponse.getResults();
		return serviceProviderList;
	}

	public List<BankBranchDto> listBankBranch(BankBranchListRequest request) {
		return bankMetaService.getBankBranches(request);
	}

	public void addBeneBankorCash(AbstractBeneDetailDto request) {
		BeneficiaryTrnxModel beneficiaryTrnxModel = request.createBeneficiaryTrnxModelObject();
		beneficiaryValidationService.validateBeneficiaryTrnxModel(beneficiaryTrnxModel);
		beneficiaryTrnxManager.commit(beneficiaryTrnxModel);
		setAdditionalBranchFields(beneficiaryTrnxModel.getBeneficaryRelationSeqId(), request);
	}

	private void setAdditionalBranchFields(BigDecimal beneficaryRelationSeqId, AbstractBeneDetailDto request) {
		BenificiaryListView beneRelationship = beneService.getBeneByIdNo(beneficaryRelationSeqId);
		BeneficaryMaster beneficaryMaster = beneService.getBeneficiaryMasterBybeneficaryMasterSeqId(beneRelationship.getBeneficaryMasterSeqId());
		BeneficaryStatus beneStatus = beneficaryStatusRepository.findOne(request.getBeneficaryTypeId());
		beneficaryMaster.setBeneficaryStatus(beneStatus.getBeneficaryStatusId());
		beneficaryMaster.setBeneficaryStatusName(beneStatus.getBeneficaryStatusName());

		if (request.getAge() != null) {
			beneficaryMaster.setAge(BigDecimal.valueOf(request.getAge()));
		}
		if (request.getYearOfBirth() != null) {
			beneficaryMaster.setYearOfBrith(BigDecimal.valueOf(request.getYearOfBirth()));
		}
		beneficaryMaster.setDateOfBrith(request.getDateOfBirth());
		beneService.saveBeneMaster(beneficaryMaster);
	}

	public void addNewBankBranchRequest(AddNewBankBranchRequest request) {
		AddBeneBankBranchRequestModel model = new AddBeneBankBranchRequestModel();
		model.setBankFullName(bankService.getBankById(request.getBankId()).getBankFullName());
		model.setBranchAddress(request.getBranchAddress());
		model.setBranchName(request.getBranchName());
		ViewCityDto cityView = metaService.getCityDescription(request.getDistrictId(), metaData.getLanguageId(), request.getCityId()).getResult();
		model.setCity(cityView.getCityName());
		CountryMasterDesc countryMasterDesc = countryService.getCountryMasterDesc(request.getCountryId(), metaData.getLanguageId());
		model.setCountryName(countryMasterDesc.getCountryName());
		Customer customer = userService.getCustById(metaData.getCustomerId());
		String firstname = customer.getFirstName();
		String lastename = customer.getLastName();
		model.setCustomerName(firstname + lastename);
		model.setDistrict(metaService.getDistrictMasterById(request.getDistrictId()).getDistrictDesc());
		model.setIdentityId(customer.getIdentityInt());
		model.setIfscCode(request.getIfscCode());
		model.setState(metaService.getStateMasterById(request.getStateId()).getStateName());
		model.setSwift(request.getSwift());
		sendaddNewBankBranchRequestEmail(model);
	}

	private void sendaddNewBankBranchRequestEmail(AddBeneBankBranchRequestModel model) {
		Email email = new Email();
		String emailid = jaxProperties.getSupportSoaEmail().get(0);
		email.setSubject(BRANCH_SEARCH);
		email.addTo(jaxProperties.getSupportSoaEmail().get(0));
		email.setITemplate(TemplatesMX.BRANCH_SEARCH_EMPTY_BRANCH);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, model);

		logger.info("Email to - " + emailid + " first name : " + model.getCustomerName());
		postManService.sendEmailAsync(email);
	}

	public List<BeneStatusDto> getBeneListStatuses() {
		List<BeneStatus> list = Arrays.asList(BeneficiaryConstant.BeneStatus.values());
		List<BeneStatusDto> output = list.stream().map(i -> {
			BeneStatusDto dto = new BeneStatusDto(i.getDescription(), i.name());
			return dto;
		}).collect(Collectors.toList());
		return output;
	}

	public List<BeneficiaryListDTO> listBene(ListBeneRequest request) {
		ApiResponse beneList = beneService.getBeneficiaryListForBranch(metaData.getCustomerId(), metaData.getCountryId(),
				BigDecimal.valueOf(request.getBeneCoutryId()));
		List<BeneficiaryListDTO> beneListDto = beneList.getResults();
		beneListDto.forEach(i -> {
			String dbFlag = i.getIsActive();
			BeneStatus beneStatus = BeneficiaryConstant.BeneStatus.findBeneStatusBydbFlag(dbFlag);
			BeneStatusDto dto = new BeneStatusDto(beneStatus.getDescription(), beneStatus.name());
			i.setBeneStatusDto(dto);
		});
		addBeneUpdateFlag(beneListDto);
		return beneListDto;
	}

	private void addBeneUpdateFlag(List<BeneficiaryListDTO> beneListDto) {

		List<BigDecimal> beneRelSeqIds = beneListDto.stream().map(i -> i.getBeneficiaryRelationShipSeqId()).collect(Collectors.toList());
		List<BigDecimal> beneTrnxCounts = iTransactionHistroyDAO.getbeneRelSeqlIdsTranaction(beneRelSeqIds);
		beneListDto.forEach(i -> {
			if (beneTrnxCounts.contains(i.getBeneficiaryRelationShipSeqId())) {
				i.setUpdateBeneStatus(UpdateBeneStatus.OLD_BENE_TRASACT);
			} else {
				if (i.getBeneficiaryErrorStatus() == null || i.getBeneficiaryErrorStatus().isEmpty()) {
					i.setUpdateBeneStatus(UpdateBeneStatus.NEW_BENE_NON_TRANSACT);
				} else {
					i.setUpdateBeneStatus(UpdateBeneStatus.OLD_BENE_NON_TRANSACT);
				}
			}
		});

	}

	public void updateBeneStatus(UpdateBeneStatusRequest request) {
		beneBranchManager.updateBeneStatus(request);
	}

	@Transactional
	public void updateBeneBankorCash(AbtractUpdateBeneDetailDto request) {
		logger.info("updateBeneBankorCash request: {} ", JsonUtil.toJson(request));
		BeneficiaryTrnxModel beneficiaryTrnxModel = request.createBeneficiaryTrnxModelObject();
		BeneAccountModel beneAccountDetail = beneficiaryTrnxModel.getBeneAccountModel();
		BenePersonalDetailModel benePersonalDetail = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneficaryRelationship beneRelationship = beneService.getBeneRelationshipByIdNo(BigDecimal.valueOf(request.getIdNo()));
		beneBranchManager.updateBeneMaster(beneRelationship, benePersonalDetail, request);
		beneBranchManager.updateBeneContact(beneRelationship, benePersonalDetail);
		beneBranchManager.updateBeneAccount(beneRelationship, beneAccountDetail);
	}

	public List<BeneficiaryListDTO> getBeneByIdNo(Integer idNo) {
		BeneficiaryListDTO beneView = beneService.getBeneDtoByIdNo(BigDecimal.valueOf(idNo));
		List<BeneficiaryListDTO> beneListDto = Arrays.asList(beneView);
		beneListDto.forEach(i -> {
			String dbFlag = i.getIsActive();
			BeneStatus beneStatus = BeneficiaryConstant.BeneStatus.findBeneStatusBydbFlag(dbFlag);
			BeneStatusDto dto = new BeneStatusDto(beneStatus.getDescription(), beneStatus.name());
			i.setBeneStatusDto(dto);
		});
		addBeneUpdateFlag(beneListDto);
		return beneListDto;
	}

}
