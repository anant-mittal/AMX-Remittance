package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.h2.mvstore.ConcurrentArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.AddAdditionalBankDataDto;
import com.amx.amxlib.meta.model.AddDynamicLabel;
import com.amx.amxlib.meta.model.AdditionalBankDetailsViewDto;
import com.amx.amxlib.meta.model.PurposeTrnxAmicDescDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.LanguageCodeType;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.LanguageType;
import com.amx.jax.dbmodel.PurposeTrnxAmicDesc;
import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.IRemitTransReqPurpose;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.repository.IAdditionalBankDetailsDao;
import com.amx.jax.repository.IAdditionalBankRuleMapDao;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ILanguageTypeRepository;
import com.amx.jax.repository.IPurposeTrnxAmicDescRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PurposeOfTransactionService extends AbstractService {

	private Logger logger = LoggerFactory.getLogger(PurposeOfTransactionService.class);

	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;

	@Autowired
	IAdditionalBankDetailsDao additionalBankDetailsDao;

	@Autowired
	IAdditionalBankRuleMapDao additionalBankRuleMapDao;

	@Autowired
	private ApplicationProcedureDao applicationProcedureDao;
	
	@Autowired
	ILanguageTypeRepository languageTypeRepository;
	
	@Autowired
	IPurposeTrnxAmicDescRepository purposeTrnxAmicDescRepository;

	@Autowired
	MetaData meta;

	@Autowired
	private IBeneficiaryOnlineDao beneficiaryOnlineDao;
	@Autowired
	RemittanceTransactionManager remittanceTxnManger;
	@Resource
	public Map<String, Object> remitApplParametersMap;
	
	public List<AddAdditionalBankDataDto> getPutrposeOfTransaction(BigDecimal applicationCountryId,
			BigDecimal countryId, BigDecimal currencyId, BigDecimal remittanceModeId, BigDecimal deliveryModeId,
			BigDecimal bankId) throws GlobalException {
		logger.debug("in getPutrposeOfTransaction applicationCountryId:{}, countryId:{},currencyId:{}, remittanceModeId:{},deliveryModeId:{},bankId:{}",
				applicationCountryId, countryId, currencyId, remittanceModeId, deliveryModeId, bankId);
		List<AddAdditionalBankDataDto> listAdditionalBankDataTable = null;
		List<AddDynamicLabel> listDynamicLabel = null;
		// List<AddAdditionalBankDataDto>
		ApiResponse response = getBlackApiResponse();
		try {
			listAdditionalBankDataTable = new ArrayList<>();
			listDynamicLabel = new ArrayList<>();
			List<AdditionalDataDisplayView> serviceAppRuleList = additionalDataDisplayDao.getAdditionalDataFromServiceApplicability(applicationCountryId, countryId, currencyId,remittanceModeId, deliveryModeId,IAdditionalDataDisplayDao.flexiFieldIn);
			if (!serviceAppRuleList.isEmpty()) {
				for (AdditionalDataDisplayView serviceRule : serviceAppRuleList) {
					AddDynamicLabel addDynamic = new AddDynamicLabel();
					addDynamic.setLebelId(serviceRule.getServiceApplicabilityRuleId());
					addDynamic.setFieldLength(serviceRule.getFieldLength());
					if (serviceRule.getFieldDescription() != null) {
						addDynamic.setLebelDesc(serviceRule.getFieldDescription());
					}
					addDynamic.setFlexiField(serviceRule.getFlexField());
					addDynamic.setValidation(serviceRule.getValidationsReq());
					addDynamic.setNavicable(serviceRule.getIsRendered());
					addDynamic.setMinLenght(serviceRule.getMinLength());
					addDynamic.setMaxLenght(serviceRule.getMaxLength());
					if (serviceRule.getIsRequired() != null && serviceRule.getIsRequired().equalsIgnoreCase("Y")) {
						addDynamic.setMandatory("*");
						addDynamic.setRequired(true);
					}
					listDynamicLabel.add(addDynamic);
				}

			}
			listAdditionalBankDataTable = this.matchData(listDynamicLabel, countryId, currencyId, remittanceModeId,deliveryModeId, bankId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(e.getMessage());
		}

		response.getData().getValues().addAll(listAdditionalBankDataTable);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType("addtionaldata");

		return listAdditionalBankDataTable;

	}

	public List<AddAdditionalBankDataDto> matchData(List<AddDynamicLabel> listDynamicLabel, BigDecimal routingCountry,
			BigDecimal currencyId, BigDecimal remittanceModeId, BigDecimal deleveryModeId, BigDecimal bankId)
			throws GlobalException {
		List<AddAdditionalBankDataDto> listAdditionalBankDataTable = new ArrayList<>();
		try {

			for (AddDynamicLabel dyamicLabel : listDynamicLabel) {
				AddAdditionalBankDataDto adddata = new AddAdditionalBankDataDto();
				if (dyamicLabel.getValidation() != null && dyamicLabel.getValidation().equalsIgnoreCase("Y")) {
					logger.debug("check 1:{} {}", routingCountry, dyamicLabel.getFlexiField());
				
					List<AdditionalBankRuleMap> listAdditinalBankfield = additionalBankRuleMapDao
							.getDynamicLevelMatch(routingCountry, dyamicLabel.getFlexiField());
					if (!listAdditinalBankfield.isEmpty()) {
						for (AdditionalBankRuleMap listAdd : listAdditinalBankfield) {
							logger.debug("check 2:{} {} {} {} {} {}", currencyId, bankId, remittanceModeId, deleveryModeId,
									routingCountry, dyamicLabel.getFlexiField());
							List<AdditionalBankDetailsViewx> listAdditionaView = additionalBankDetailsDao
									.getAdditionalBankDetails(currencyId, bankId, remittanceModeId, deleveryModeId,
											routingCountry, dyamicLabel.getFlexiField());

							if (!listAdditionaView.isEmpty()) {

								// setting dynamic functionality
								adddata.setMandatory(dyamicLabel.getMandatory());
								if (dyamicLabel.getMinLenght() != null) {
									adddata.setMinLenght(dyamicLabel.getMinLenght().intValue());
								} else {
									adddata.setMinLenght(0);
								}
								if (dyamicLabel.getMaxLenght() != null) {
									adddata.setMaxLenght(dyamicLabel.getMaxLenght());
								} else {
									adddata.setMaxLenght(new BigDecimal(50));
								}
								adddata.setFieldLength(dyamicLabel.getFieldLength());
								adddata.setRequired(dyamicLabel.getRequired());
								adddata.setAdditionalBankRuleFiledId(listAdd.getAdditionalBankRuleId());

								adddata.setFlexiField(listAdd.getFlexField());
								if (listAdd.getFieldName() != null) {
									adddata.setAdditionalDesc(listAdd.getFieldName());
								}
								adddata.setRenderInputText(false);
								adddata.setRenderSelect(true);
								adddata.setRenderOneSelect(false);

								adddata.setListadditionAmiecData(convertViewModel(listAdditionaView));
							}
						}
					}
				} else {

					adddata.setMandatory(dyamicLabel.getMandatory());
					if (dyamicLabel.getMinLenght() != null) {
						adddata.setMinLenght(dyamicLabel.getMinLenght().intValue());
					} else {
						adddata.setMinLenght(0);
					}
					if (dyamicLabel.getMaxLenght() != null) {
						adddata.setMaxLenght(dyamicLabel.getMaxLenght());
					} else {
						adddata.setMaxLenght(new BigDecimal(50));
					}
					adddata.setFieldLength(dyamicLabel.getFieldLength());
					adddata.setRequired(dyamicLabel.getRequired());
					adddata.setRenderInputText(true);
					adddata.setRenderSelect(false);
					adddata.setRenderOneSelect(false);
					adddata.setFlexiField(dyamicLabel.getFlexiField());
					if (dyamicLabel.getLebelDesc() != null) {
						adddata.setAdditionalDesc(dyamicLabel.getLebelDesc());
					} else {
						List<AdditionalBankRuleMap> listAdditinalBankfield = additionalBankRuleMapDao
								.getDynamicLevelMatch(routingCountry, dyamicLabel.getFlexiField());
						if (!listAdditinalBankfield.isEmpty()) {
							if (listAdditinalBankfield.get(0).getFieldName() != null) {
								adddata.setAdditionalDesc(listAdditinalBankfield.get(0).getFieldName());
							} else {
								adddata.setExceptionMessage(dyamicLabel.getFlexiField());

							}
						} else {
							adddata.setExceptionMessage(dyamicLabel.getFlexiField());
						}
					}
				}
				listAdditionalBankDataTable.add(adddata);

				for (AddAdditionalBankDataDto lst : listAdditionalBankDataTable) {
					if (lst.getAdditionalDesc() != null
							&& lst.getAdditionalDesc().equalsIgnoreCase("PURPOSE OF REMITTANCE")) {
						List<AdditionalBankDetailsViewDto> lstAme = new ArrayList<>();
						for (AdditionalBankDetailsViewDto amiec : lst.getListadditionAmiecData()) {
							if (amiec.getAmiecCode() != null && !amiec.getAmieceDescription().contains("TRADE")) {
								lstAme.add(amiec);
							}
						}
						lst.getListadditionAmiecData().clear();
						lst.getListadditionAmiecData().addAll(lstAme);
					}

				}
			}

		} catch (NullPointerException NulExp) {
			throw new GlobalException(NulExp.getMessage());
		} catch (Exception exp) {
			throw new GlobalException(exp.getMessage());
		}
		return listAdditionalBankDataTable;
	}

	private List<AdditionalBankDetailsViewDto> convertViewModel(List<AdditionalBankDetailsViewx> listAdditionaView) {
		List<AdditionalBankDetailsViewDto> listView = new ArrayList<>();
		listAdditionaView.forEach(viewModel -> listView.add(convertAddModelToDto(viewModel)));
		Collections.sort(listView, new AdditionalBankDetailsViewDto.AdditionalBankDetailsViewDtoComparator());
		return listView;
	}

	private AdditionalBankDetailsViewDto convertAddModelToDto(AdditionalBankDetailsViewx viewModel) {
		AdditionalBankDetailsViewDto dto = new AdditionalBankDetailsViewDto();
		try {
			BeanUtils.copyProperties(dto, viewModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return dto;
	}

	@Override
	public String getModelType() {
		return "purpose-of-txn";
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("rawtypes")
	public ApiResponse getPurposeOfTransaction(IRemitTransReqPurpose request) {
		RemittanceTransactionRequestModel model = (RemittanceTransactionRequestModel) request;
		logger.debug("in getPurposeOfTransaction with params: " + model.toString());
		ApiResponse response = getBlackApiResponse();
		if (model.getLocalAmount() == null && model.getForeignAmount() == null) {
			model.setLocalAmount(BigDecimal.ONE);
		}
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(model.getBeneId());
		remittanceTxnManger.validateTransactionData(model);
		Map<String, Object> routingDetails = remitApplParametersMap;
		BigDecimal applicationCountryId = beneficiary.getApplicationCountryId();
		BigDecimal countryId = beneficiary.getCountryId();
		BigDecimal rountingCountry = (BigDecimal) routingDetails.get("P_ROUTING_COUNTRY_ID");
		BigDecimal currencyId = beneficiary.getCurrencyId();
		BigDecimal remittanceModeId = (BigDecimal) routingDetails.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) routingDetails.get("P_DELIVERY_MODE_ID");
		BigDecimal bankId = (BigDecimal) routingDetails.get("P_ROUTING_BANK_ID");
		PurposeOfTransactionModel purposeOfTxnModel = new PurposeOfTransactionModel();
		List<AddAdditionalBankDataDto> dto = this.getPutrposeOfTransaction(applicationCountryId, rountingCountry, currencyId,remittanceModeId, deliveryModeId, bankId);
		LanguageType languageType = new LanguageType();
		List<PurposeTrnxAmicDesc> PurposeTrnxAmicDesc;

		languageType = languageTypeRepository.findBylanguageId(meta.getLanguageId());
		if(languageType.getLanguageName().equals(LanguageCodeType.Arabic.toString())){
			// convert english description to arabic description
			List<AddAdditionalBankDataDto> arabicdto = getLocalAmicCodeDesc(dto, meta.getLanguageId());
			purposeOfTxnModel.setDto(arabicdto);
			response.getData().getValues().add(purposeOfTxnModel);
			response.setResponseStatus(ResponseStatus.OK);
			response.getData().setType(purposeOfTxnModel.getModelType());
		}else {
			purposeOfTxnModel.setDto(dto);
			response.getData().getValues().add(purposeOfTxnModel);
			response.setResponseStatus(ResponseStatus.OK);
			response.getData().setType(purposeOfTxnModel.getModelType());
		}
		return response;
	}
	

	private HashMap<String, Object> getBeneBankDetails(BenificiaryListView beneficiary) {

		HashMap<String, Object> beneBankDetails = new HashMap<>();
		beneBankDetails.put("P_APPLICATION_COUNTRY_ID", meta.getCountryId());
		beneBankDetails.put("P_USER_TYPE", "I");
		beneBankDetails.put("P_BENEFICIARY_COUNTRY_ID", beneficiary.getBenificaryCountry());
		beneBankDetails.put("P_BENEFICIARY_BANK_ID", beneficiary.getBankId());
		beneBankDetails.put("P_BENEFICIARY_BRANCH_ID", beneficiary.getBranchId());
		beneBankDetails.put("P_BENEFICIARY_BANK_ACCOUNT", beneficiary.getBankAccountNumber());
		beneBankDetails.put("P_CUSTOMER_ID", meta.getCustomerId());
		beneBankDetails.put("P_SERVICE_GROUP_CODE", beneficiary.getServiceGroupCode());
		beneBankDetails.put("P_CURRENCY_ID", beneficiary.getCurrencyId());
		beneBankDetails.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());
		return beneBankDetails;
	}
	
	private List<AddAdditionalBankDataDto> getLocalAmicCodeDesc(List<AddAdditionalBankDataDto> engPurposeOfTrnxDto,BigDecimal languageId){
		List<String> amiecCode = new ArrayList<>();
		HashMap<String, String> arabicPurposeOfTrnx = new HashMap<>();
		List<AdditionalBankDetailsViewDto> listadditionAmiecData = new ArrayList<>();
		List<AddAdditionalBankDataDto> arbPurposeOfTrnxDto = new ArrayList<>();
		
		for (AddAdditionalBankDataDto addAdditionalBankDataDto : engPurposeOfTrnxDto) {
			if(addAdditionalBankDataDto.getListadditionAmiecData() != null && addAdditionalBankDataDto.getListadditionAmiecData().size() != 0) {
				for (AdditionalBankDetailsViewDto additionalBankDetailsViewDto : addAdditionalBankDataDto.getListadditionAmiecData()) {
					if(!amiecCode.contains(additionalBankDetailsViewDto.getAmiecCode())) {
						amiecCode.add(additionalBankDetailsViewDto.getAmiecCode());
					}
				}
			}
		}
		
		if(amiecCode != null && amiecCode.size()!= 0) {
			List<PurposeTrnxAmicDesc> purposeTrnxAmicDescs = purposeTrnxAmicDescRepository.fetchAllAmiecDataByLanguageId(amiecCode, languageId);
			for (PurposeTrnxAmicDesc purposeTrnxAmic : purposeTrnxAmicDescs) {
				arabicPurposeOfTrnx.put(purposeTrnxAmic.getAmicCode(), purposeTrnxAmic.getLocalFulldesc());
			}
		}
		
		if(arabicPurposeOfTrnx != null && arabicPurposeOfTrnx.size() != 0) {
			for (AddAdditionalBankDataDto addAdditionalBankDataDto : engPurposeOfTrnxDto) {
				AddAdditionalBankDataDto addBankData = addAdditionalBankDataDto;
				if(addAdditionalBankDataDto.getListadditionAmiecData() != null && addAdditionalBankDataDto.getListadditionAmiecData().size() != 0) {
					for (AdditionalBankDetailsViewDto additionalBankDetailsViewDto : addAdditionalBankDataDto.getListadditionAmiecData()) {
						additionalBankDetailsViewDto.setLocalName(arabicPurposeOfTrnx.get(additionalBankDetailsViewDto.getAmiecCode()));
						listadditionAmiecData.add(additionalBankDetailsViewDto);
					}
					addBankData.setListadditionAmiecData(listadditionAmiecData);
				}
				arbPurposeOfTrnxDto.add(addBankData);
			}
		}
		
		return arbPurposeOfTrnxDto;
	}
	
	/*public List<PurposeTrnxAmicDescDto> convert(List<PurposeTrnxAmicDesc> purposeTrnxAmicDesc) {
		List<PurposeTrnxAmicDescDto> purposeTrnxAmicDescDto = new ArrayList<>();
		
		if(purposeTrnxAmicDesc!=null && purposeTrnxAmicDesc.size()!=0) {	
		for (PurposeTrnxAmicDesc purposeTrnxAmicDesccc : purposeTrnxAmicDesc) {	
			PurposeTrnxAmicDescDto dto = new PurposeTrnxAmicDescDto();
	
		dto.setAmicCode(purposeTrnxAmicDesccc.getAmicCode());
		dto.setFullDesc(purposeTrnxAmicDesccc.getFullDesc());
		dto.setId(purposeTrnxAmicDesccc.getId());
		dto.setLanguageId(purposeTrnxAmicDesccc.getLanguageId());
		dto.setLocalFulldesc(purposeTrnxAmicDesccc.getFullDesc());
		dto.setShortDesc(purposeTrnxAmicDesccc.getShortDesc());
		purposeTrnxAmicDescDto.add(dto);
}
		}
		return purposeTrnxAmicDescDto;
	}*/
	
	List<AddAdditionalBankDataDto> getlistAmicDesc(List<AdditionalBankDetailsViewDto> additionalBankDetailsViewDto,  HashMap<String, Object> localAmicDesc ){
		
		for(AdditionalBankDetailsViewDto additionalBankDetailsViewList :additionalBankDetailsViewDto) {
			additionalBankDetailsViewList.setLocalName(localAmicDesc.get("P_LOCAL_FULL_DESC").toString());
			
		}
		
		return null;
		
		
		
	}
}
