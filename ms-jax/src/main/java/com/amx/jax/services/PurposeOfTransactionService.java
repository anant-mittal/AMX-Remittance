package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.GlobalException;
import com.amx.amxlib.meta.model.AddAdditionalBankDataDto;
import com.amx.amxlib.meta.model.AddDynamicLabel;
import com.amx.amxlib.meta.model.AdditionalBankDetailsViewDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsView;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.repository.IAdditionalBankDetailsDao;
import com.amx.jax.repository.IAdditionalBankRuleMapDao;
import com.amx.jax.repository.IAdditionalDataDisplayDao;




@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PurposeOfTransactionService extends AbstractService{
	
	private Logger logger = Logger.getLogger(PurposeOfTransactionService.class);
	
	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;
	
	
	@Autowired
	IAdditionalBankDetailsDao additionalBankDetailsDao;
	
	@Autowired
	IAdditionalBankRuleMapDao additionalBankRuleMapDao;
	
	private List<AddAdditionalBankDataDto> listAdditionalBankDataTable = new ArrayList<AddAdditionalBankDataDto>();
	
	public List<AddAdditionalBankDataDto>  getPutrposeOfTransaction(BigDecimal applicationCountryId, BigDecimal countryId, BigDecimal currencyId, BigDecimal remittanceModeId, BigDecimal deliveryModeId,BigDecimal bankId) throws GlobalException{
		List<AddAdditionalBankDataDto> listAdditionalBankDataTable =null;
		List<AddDynamicLabel> listDynamicLabel = null;
		//List<AddAdditionalBankDataDto>
		ApiResponse response = getBlackApiResponse();
		try {
			listAdditionalBankDataTable = new ArrayList<>();
			listDynamicLabel = new ArrayList<>();
			List<AdditionalDataDisplayView> serviceAppRuleList = additionalDataDisplayDao.getAdditionalDataFromServiceApplicability(applicationCountryId, countryId, currencyId, remittanceModeId, deliveryModeId);
			if(!serviceAppRuleList.isEmpty()) {
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
					if (serviceRule.getIsRequired() != null && serviceRule.getIsRequired().equalsIgnoreCase("Yes")) {
						addDynamic.setMandatory("*");
						addDynamic.setRequired(true);
					}
					listDynamicLabel.add(addDynamic);
				}

			}
			listAdditionalBankDataTable = this.matchData(listDynamicLabel,countryId,currencyId, remittanceModeId, deliveryModeId,bankId);
		
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException(e.getMessage());
		}
		
		response.getData().getValues().addAll(listAdditionalBankDataTable);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType("addtionaldata");
		
		return listAdditionalBankDataTable;
		
	}

	
	public List<AddAdditionalBankDataDto> matchData(List<AddDynamicLabel> listDynamicLabel,BigDecimal routingCountry,BigDecimal currencyId, BigDecimal remittanceModeId, BigDecimal deleveryModeId,BigDecimal bankId) throws GlobalException{
		try {
			
			List<AddAdditionalBankDataDto> listAdditionalBankDataTable =new ArrayList<>();
			
			for (AddDynamicLabel dyamicLabel : listDynamicLabel) {
				AddAdditionalBankDataDto adddata = new AddAdditionalBankDataDto();
				if (dyamicLabel.getValidation() != null && dyamicLabel.getValidation().equalsIgnoreCase("Yes")) {
					List<AdditionalBankRuleMap> listAdditinalBankfield = additionalBankRuleMapDao.getDynamicLevelMatch(routingCountry, dyamicLabel.getFlexiField());
					if (!listAdditinalBankfield.isEmpty()) {
						for (AdditionalBankRuleMap listAdd : listAdditinalBankfield) {
							List<AdditionalBankDetailsView> listAdditionaView = additionalBankDetailsDao.getAdditionalBankDetails(currencyId,bankId,remittanceModeId, deleveryModeId, routingCountry, dyamicLabel.getFlexiField());
									
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
								System.out.println("listAdditionaView:"+listAdditionaView.size());

								System.out.println("listAdditionaView:"+listAdditionaView.size());
								for(AdditionalBankDetailsView lst:listAdditionaView){
									System.out.println("listAdditionaView:"+lst.getAmiecCode()+"\t Desc :"+lst.getAmieceDescription());

								}
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
						List<AdditionalBankRuleMap> listAdditinalBankfield = additionalBankRuleMapDao.getDynamicLevelMatch(routingCountry, dyamicLabel.getFlexiField());
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

				for(AddAdditionalBankDataDto lst : listAdditionalBankDataTable){
					if(lst.getAdditionalDesc()!= null && lst.getAdditionalDesc().equalsIgnoreCase("PURPOSE OF REMITTANCE")){
						List<AdditionalBankDetailsViewDto> lstAme = new ArrayList<>();
						for(AdditionalBankDetailsViewDto amiec :lst.getListadditionAmiecData()){
							if(amiec.getAmiecCode() !=null && !amiec.getAmieceDescription().contains("TRADE")){
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
		}catch (Exception exp) {
			throw new GlobalException(exp.getMessage());
		}
		return listAdditionalBankDataTable;
	}


	
	
	private List<AdditionalBankDetailsViewDto> convertViewModel(List<AdditionalBankDetailsView> listAdditionaView){
		List<AdditionalBankDetailsViewDto> listView = new ArrayList<>();
		listAdditionaView.forEach(viewModel -> listView.add(convertAddModelToDto(viewModel)));
		return listView;
	}

	private AdditionalBankDetailsViewDto convertAddModelToDto(AdditionalBankDetailsView viewModel) {
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
