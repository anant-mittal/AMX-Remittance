package com.amx.jax.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dao.BeneficiaryDao;
import com.amx.jax.dbmodel.BeneficiaryCountryView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.dbmodel.SwiftMasterView;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryCountryDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.Util;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BeneficiaryService extends AbstractService {

	Logger logger = Logger.getLogger(BeneficiaryService.class);

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	IBeneficiaryCountryDao beneficiaryCountryDao;

	@Autowired
	BeneficiaryCheckService beneCheck;

	@Autowired
	IBeneficiaryRelationshipDao beneRelationShipDao;

	@Autowired
	IBeneficaryContactDao beneficiaryContactDao;

	@Autowired
	CustomerDao custDao;
	
	@Autowired
	BeneficiaryDao beneDao;
	
	@Autowired
	ITransactionHistroyDAO tranxHistDao;

	
	public ApiResponse getBeneficiaryListForOnline(BigDecimal customerId, BigDecimal applicationCountryId,
			BigDecimal beneCountryId) {
		List<BenificiaryListView> beneList = null;
		if (beneCountryId != null && beneCountryId.compareTo(BigDecimal.ZERO) != 0) {
			beneList = beneficiaryOnlineDao.getOnlineBeneListFromViewForCountry(customerId, applicationCountryId,
					beneCountryId);
		} else {
			beneList = beneficiaryOnlineDao.getOnlineBeneListFromView(customerId, applicationCountryId);
		}
		BigDecimal nationalityId = custDao.getCustById(customerId).getNationalityId();
		BenificiaryListViewOnlineComparator comparator = new BenificiaryListViewOnlineComparator(nationalityId);
		Collections.sort(beneList, comparator);
		ApiResponse response = getBlackApiResponse();
		if (beneList.isEmpty()) {
			throw new GlobalException("Beneficiary list is not found");
		} else {
			response.getData().getValues().addAll(convertBeneList(beneList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("beneList");
		return response;
	}
	
	
	
	
	
	

	public class BenificiaryListViewOnlineComparator implements Comparator<BenificiaryListView> {

		private BigDecimal countryId;

		public BenificiaryListViewOnlineComparator(BigDecimal nationalityId) {
			this.countryId = nationalityId;
		}

		@Override
		public int compare(BenificiaryListView o1, BenificiaryListView o2) {

			if (o1.getCountryId() != o2.getCountryId() && o1.getCountryId() != null
					&& o1.getCountryId().equals(this.countryId)) {
				return -1;
			}
			return 0;
		}

		public BigDecimal getCountryId() {
			return countryId;
		}

		public void setCountryId(BigDecimal countryId) {
			this.countryId = countryId;
		}

	}

	public ApiResponse getBeneficiaryListForBranch(BigDecimal customerId, BigDecimal applicationCountryId,
			BigDecimal beneCountryId) {

		List<BenificiaryListView> beneList = null;
		if (beneCountryId != null && beneCountryId.compareTo(BigDecimal.ZERO) != 0) {
			beneList = beneficiaryOnlineDao.getBeneListFromViewForCountry(customerId, applicationCountryId,
					beneCountryId);
		} else {
			beneList = beneficiaryOnlineDao.getBeneListFromView(customerId, applicationCountryId);
		}

		ApiResponse response = getBlackApiResponse();
		if (beneList.isEmpty()) {
			throw new GlobalException("Beneficiary list is not found");
		} else {
			response.getData().getValues().addAll(convertBeneList(beneList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("beneList");
		return response;
	}

	public ApiResponse getBeneficiaryCountryListForOnline(BigDecimal customerId) {
		List<BeneficiaryCountryView> beneocountryList = beneficiaryCountryDao.getBeneCountryForOnline(customerId);
		ApiResponse response = getBlackApiResponse();
		if (beneocountryList.isEmpty()) {
			throw new GlobalException("Beneficiary country list is not found");
		} else {
			response.getData().getValues().addAll(convert(beneocountryList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("benecountry");
		return response;
	}

	public ApiResponse getBeneficiaryCountryListForBranch(BigDecimal customerId) {
		List<BeneficiaryCountryView> beneocountryList = beneficiaryCountryDao.getBeneCountryForBranch(customerId);
		ApiResponse response = getBlackApiResponse();
		if (beneocountryList.isEmpty()) {
			throw new GlobalException("Beneficiary country list is not found");
		} else {
			response.getData().getValues().addAll(convert(beneocountryList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("benecountry");
		return response;
	}

	public ApiResponse disableBeneficiary(BeneficiaryListDTO beneDetails) {
		ApiResponse response = getBlackApiResponse();
		try {
			List<BeneficaryRelationship> beneRelationList = null;

			BeneficaryRelationship beneRelation = null;

			// beneRelation =
			// beneRelationShipDao.findOne(beneDetails.getBeneficiaryRelationShipSeqId());

			beneRelationList = beneRelationShipDao.getBeneRelationshipByBeneMasterIdForDisable(
					beneDetails.getBeneficaryMasterSeqId(), beneDetails.getCustomerId());

			if (!beneRelationList.isEmpty()) {
				BeneficaryRelationship beneRelationModel = beneRelationShipDao
						.findOne((beneRelationList.get(0).getBeneficaryRelationshipId()));
				beneRelationModel.setIsActive("D");
				beneRelationModel.setModifiedBy(beneDetails.getCustomerId().toString());
				beneRelationModel.setModifiedDate(new Date());
				beneRelationModel.setRemarks(beneDetails.getRemarks());
				beneRelationShipDao.save(beneRelationModel);
				response.setResponseStatus(ResponseStatus.OK);
			} else {
				throw new GlobalException("No record found");
			}

			return response;
		} catch (Exception e) {
			throw new GlobalException("Error while update");
		}

	}

	public ApiResponse beneficiaryUpdate(BeneficiaryListDTO beneDetails) {
		ApiResponse response = getBlackApiResponse();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalException("Error while update Custoemr Id" + beneDetails.getCustomerId()
					+ "\t Bene Ralation :" + beneDetails.getBeneficiaryRelationShipSeqId());
		}

		return response;
	}
	
	
	
	/**
	 * to get default beneficiary.
	 * @param beneocountryList
	 * @return
	 * Transaction Id: idno
	 * And Beneficiary Id :idNo
	 */
	
	public ApiResponse getDefaultBeneficiary(BigDecimal customerId, BigDecimal applicationCountryId,
			BigDecimal beneRealtionId,BigDecimal transactionId) {
		ApiResponse response = getBlackApiResponse();
		try {
		BenificiaryListView beneList = null;
		BeneficiaryListDTO beneDto =null;
		CustomerRemittanceTransactionView trnxView = null;
		RemittancePageDto remitPageDto = new RemittancePageDto();
		
		if (beneRealtionId != null && beneRealtionId.compareTo(BigDecimal.ZERO) != 0) {
			beneList = beneficiaryOnlineDao.getBeneficiaryByRelationshipId(customerId, applicationCountryId, beneRealtionId);
		} else {
			beneList = beneficiaryOnlineDao.getDefaultBeneficiary(customerId, applicationCountryId);
		
		}
		
		if(beneList ==null) {
			throw new GlobalException("Not found");
		}else {
			beneDto = beneCheck.beneCheck(convertBeneModelToDto((beneList)));
			if(beneDto!=null && !Util.isNullZeroBigDecimalCheck(transactionId) && (Util.isNullZeroBigDecimalCheck(beneRealtionId) || Util.isNullZeroBigDecimalCheck(beneDto.getBeneficiaryRelationShipSeqId()))) {
				trnxView = tranxHistDao.getDefaultTrnxHist(customerId, beneDto.getBeneficiaryRelationShipSeqId());
			}else if(beneDto!=null && Util.isNullZeroBigDecimalCheck(transactionId) && Util.isNullZeroBigDecimalCheck(beneRealtionId)) {
				trnxView = tranxHistDao.getTrnxHistByBeneIdAndTranId(customerId, beneRealtionId,transactionId);
			}else if(beneDto!=null && Util.isNullZeroBigDecimalCheck(transactionId)){
				trnxView = tranxHistDao.getTrnxHistTranId(customerId, transactionId);
			}
			
		}
		
		remitPageDto.setBeneficiaryDto(beneDto);
		remitPageDto.setTrnxHistDto(convertTranHistDto(trnxView));
		response.getData().getValues().add(remitPageDto);
		response.getData().setType(remitPageDto.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		}catch (Exception e) {
			throw new GlobalException("Default bene not found"+e.getMessage());
		}
		return response;
	}
	
	

	private List<BeneCountryDTO> convert(List<BeneficiaryCountryView> beneocountryList) {
		List<BeneCountryDTO> list = new ArrayList<BeneCountryDTO>();
		for (BeneficiaryCountryView beneCountry : beneocountryList) {
			BeneCountryDTO model = new BeneCountryDTO();
			model.setApplicationCountry(beneCountry.getApplicationCountry());
			model.setBeneCountry(beneCountry.getBeneCountry());
			model.setCountryCode(beneCountry.getCountryCode());
			model.setCountryName(beneCountry.getCountryName());
			model.setCustomerId(beneCountry.getCustomerId());
			model.setIdNo(beneCountry.getIdNo());
			model.setOrsStatus(beneCountry.getOrsStatus());
			list.add(model);
		}
		return list;
	}

	private List<BeneficiaryListDTO> convertBeneList(List<BenificiaryListView> beneList) {
		List<BeneficiaryListDTO> output = new ArrayList<>();
		beneList.forEach(beneModel -> output.add(beneCheck.beneCheck(convertBeneModelToDto(beneModel))));
		return output;
	}

	private BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return dto;
	}

	
	private TransactionHistroyDTO convertTranHistDto(CustomerRemittanceTransactionView tranView) {
		TransactionHistroyDTO tranDto = new TransactionHistroyDTO();
		try {
			BeanUtils.copyProperties(tranDto, tranView);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return tranDto;
		
	}
	
	
	public String getBeneficiaryContactNumber(BigDecimal beneMasterId) {
		List<BeneficaryContact> beneContactList = beneficiaryContactDao.getBeneContact(beneMasterId);
		BeneficaryContact beneContact = beneContactList.get(0);
		String contactNumber = null;
		if (beneContact != null) {
			if (beneContact.getMobileNumber() != null) {
				contactNumber = beneContact.getMobileNumber().toString();
			} else {
				contactNumber = beneContact.getTelephoneNumber();
			}
		}
		return contactNumber;
	}
	
	public SwiftMasterView getSwiftMasterBySwiftBic(String swiftBic) {
		return beneDao.getSwiftMasterBySwiftBic(swiftBic);
	}
	
	
	
	public Integer getTodaysTransactionForBene(BigDecimal customerId, BigDecimal benRelationId) {
		return tranxHistDao.getTodaysTransactionForBeneficiary(customerId, benRelationId);
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
