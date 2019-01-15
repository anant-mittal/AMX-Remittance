package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CityMaster;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.ShippingAddressDetail;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.dbmodel.ViewAreaModel;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.dbmodel.VwGovernateAreaModel;
import com.amx.jax.dbmodel.VwGovernateModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.response.fx.AddressTypeDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IGovernateAreaDao;
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.IViewArea;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewGovernateDao;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.CountryMetaValidation;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcSaleAddressManager extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4602595256039337910L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ICustomerRepository customerDao;

	@Autowired
	IShippingAddressRepository shippingAddressDao;

	@Autowired
	IContactDetailDao contactDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	private MetaData meta;

	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IViewStateDao stateDao;

	@Autowired
	IViewDistrictDAO districtDao;

	@Autowired
	IViewArea areaDao;

	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;

	@Autowired
	FcSaleApplicationTransactionManager saleAppl;
	
	@Autowired
	IViewGovernateDao govtDao;
	
	@Autowired
	IGovernateAreaDao govtAreaDao;
	
	@Autowired
	CountryMetaValidation countryMetaValidation;
	

	public List<ShippingAddressDto> fetchShippingAddress(){
		return fetchShippingAddress(meta.getCustomerId());
	}
	
	public ShippingAddressDto fetchShippingAddress(BigDecimal customerId, BigDecimal shippingAddressId) {
		logger.debug("customerId :"+customerId+"\t shippingAddressId :"+shippingAddressId);
		List<ShippingAddressDto> shippingAddresses = fetchShippingAddress(customerId);
		ShippingAddressDto shippingAddressDto = null;
		
		 Optional<ShippingAddressDto> shippingAddressDtoOptional = shippingAddresses.stream().filter(i -> i.getAddressId().equals(shippingAddressId)).findFirst();
		 if(shippingAddressDtoOptional.isPresent()){
			 shippingAddressDto =  shippingAddressDtoOptional.get();
		 }
		 return shippingAddressDto;
	}
	/** Fetching shipping address **/

	public List<ShippingAddressDto> fetchShippingAddress(BigDecimal customerId) {
		List<ShippingAddressDto> list = new ArrayList<>();

		BigDecimal countryId = meta.getCountryId();
		BigDecimal companyId = meta.getCompanyId();

		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		List<ContactDetail> contactList = contactDao.getContactDetailForLocal(new Customer(customerId));

		if (contactList!=null && !contactList.isEmpty()) {
			ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
			shippingAddressDto.setAddressId(contactList.get(0).getContactDetailId());
			if (customerList!=null && !customerList.isEmpty()) {
				shippingAddressDto.setFirstName(customerList.get(0).getFirstName());
				shippingAddressDto.setMiddleName(customerList.get(0).getMiddleName());
				shippingAddressDto.setLastName(customerList.get(0).getLastName());
			}
			shippingAddressDto.setCustomerId(contactList.get(0).getFsCustomer().getCustomerId());
			shippingAddressDto.setCompanyId(companyId);
			shippingAddressDto.setMobile(contactList.get(0).getMobile());
			shippingAddressDto.setLocalContactBuilding(contactList.get(0).getBuildingNo());
			shippingAddressDto.setStreet(contactList.get(0).getStreet());
			shippingAddressDto.setBlockNo(contactList.get(0).getBlock());
			shippingAddressDto.setHouse(contactList.get(0).getBuildingNo());
			shippingAddressDto.setFlat(contactList.get(0).getFlat());
			shippingAddressDto.setAddressDto(getAddressType(contactList.get(0).getArea() == null ? "" : contactList.get(0).getArea()));
			shippingAddressDto.setAreaDesc(contactList.get(0).getArea() == null ? "" : contactList.get(0).getArea());

			List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),contactList.get(0).getFsCountryMaster().getCountryId());
			if (countryMasterView!=null && !countryMasterView.isEmpty()) {
				shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
				if (contactList.get(0).getFsStateMaster() != null) {
					List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),contactList.get(0).getFsStateMaster().getStateId(), new BigDecimal(1));
					if (stateMasterView!=null && !stateMasterView.isEmpty()) {
						shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
						shippingAddressDto.setStateDto(ResourceDTO.create(stateMasterView.get(0)));
						DistrictMaster distictMaster = contactList.get(0).getFsDistrictMaster();
						if (distictMaster != null) {
							List<ViewDistrict> districtMas = districtDao.getDistrict(stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),new BigDecimal(1));
							if (districtMas!=null && !districtMas.isEmpty()) {
								shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
								shippingAddressDto.setDistrictDto(ResourceDTO.create(districtMas.get(0)));
								List<ViewCity> cityDetails = cityDao.getCityDescription(districtMas.get(0).getDistrictId(),contactList.get(0).getFsCityMaster().getCityId(), new BigDecimal(1));
								if (cityDetails!=null && !cityDetails.isEmpty()) {
									shippingAddressDto.setCityDto(ResourceDTO.create(cityDetails.get(0)));
									shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());

								}
							}
						}
					}
				}

			}
			shippingAddressDto.setAdressType("Local address");
			shippingAddressDto.setDeliveryAddress(getLocalDeliveryAddress(shippingAddressDto));
			list.add(shippingAddressDto); // Local Address
		} // Local contact details

		list.addAll(getShippingAddressDto(customerId));
		/** fetch End of shipping address **/
		return list;
	}
	
	/**
	 * get shipping addresses ONLY from shipping address table
	 * @param customer
	 * @param contactList
	 * @param companyId
	 * @return
	 */
	public List<ShippingAddressDto> getShippingAddressDto(BigDecimal customerId) {
		BigDecimal countryId = meta.getCountryId();
		BigDecimal companyId = meta.getCompanyId();
		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		Customer customer = null;
		if (customerList != null && !customerList.isEmpty()) {
			customer = customerList.get(0);
		}

		List<ContactDetail> contactList = contactDao.getContactDetailForLocal(new Customer(customerId));
		List<ShippingAddressDto> list = new ArrayList<>();
		/** Adding shipping Address **/
		List<ShippingAddressDetail> shippingAddressList = shippingAddressDao
				.findByFsCustomerAndActiveStatus(new Customer(customerId), ConstantDocument.Yes);
		if (shippingAddressList != null && !shippingAddressList.isEmpty()) {
			shippingAddressList.stream().sorted((o1, o2) -> o2.getCreationDate().compareTo(o1.getCreationDate()));
			for (ShippingAddressDetail shippingAddressDetail : shippingAddressList) {
				ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
				shippingAddressDto.setAddressId(shippingAddressDetail.getShippingAddressDetailId());

				if (customer != null) {
					shippingAddressDto.setFirstName(customer.getFirstName());
					shippingAddressDto.setMiddleName(customer.getMiddleName());
					shippingAddressDto.setLastName(customer.getLastName());
				}
				shippingAddressDto.setCustomerId(shippingAddressDetail.getFsCustomer().getCustomerId());
				shippingAddressDto.setCompanyId(companyId);
				shippingAddressDto.setMobile(shippingAddressDetail.getMobile());
				shippingAddressDto.setLocalContactBuilding(shippingAddressDetail.getBuildingNo());
				shippingAddressDto.setStreet(shippingAddressDetail.getStreet());
				shippingAddressDto.setBlockNo(shippingAddressDetail.getBlock());
				shippingAddressDto.setHouse(shippingAddressDetail.getFlat());
				shippingAddressDto.setFlat(shippingAddressDetail.getFlat());
				shippingAddressDto.setBuildingNo(shippingAddressDetail.getBuildingNo());
				shippingAddressDto.setAddressDto(getAddressType(shippingAddressDetail.getAddressType()));
				shippingAddressDto.setAreaDesc(areaDao.getAreaList(shippingAddressDetail.getAreaCode()) == null ? ""
						: areaDao.getAreaList(shippingAddressDetail.getAreaCode()).getShortDesc());
				ViewAreaModel areaModel = areaDao.getAreaList(shippingAddressDetail.getAreaCode());

				VwGovernateAreaModel govtAreaModel = govtAreaDao
						.getGovermenArea(shippingAddressDetail.getGoverAreaId());
				VwGovernateModel govtModel = govtDao.getGovermentDetails(shippingAddressDetail.getGovernateId());

				if (govtAreaModel != null) {
					shippingAddressDto
							.setGovtAreaDesc(govtAreaModel.getFullName() == null ? "" : govtAreaModel.getFullName());
					shippingAddressDto.setGovernoateAreaDto(ResourceDTO.create(govtAreaModel));
				}

				if (govtModel != null) {
					shippingAddressDto.setGovernoatesDto(ResourceDTO.create(govtModel));
				}

				if (areaModel != null) {
					shippingAddressDto.setAreaDto(ResourceDTO.create(areaModel));
				}
				List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),
						shippingAddressDetail.getFsCountryMaster().getCountryId());
				if (countryMasterView != null && !countryMasterView.isEmpty()) {
					shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
					shippingAddressDto.setCountryDto(ResourceDTO.create(countryMasterView.get(0)));
					if (shippingAddressDetail.getFsStateMaster() != null) {
						List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),
								shippingAddressDetail.getFsStateMaster().getStateId(), new BigDecimal(1));
						if (stateMasterView != null && !stateMasterView.isEmpty()) {
							shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
							shippingAddressDto.setStateDto(ResourceDTO.create(stateMasterView.get(0)));
							DistrictMaster distictMaster = shippingAddressDetail.getFsDistrictMaster();
							if (distictMaster != null) {
								List<ViewDistrict> districtMas = districtDao.getDistrict(
										stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),
										new BigDecimal(1));
								if (districtMas != null && !districtMas.isEmpty()) {
									shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
									shippingAddressDto.setDistrictDto(ResourceDTO.create(districtMas.get(0)));
									List<ViewCity> cityDetails = null;
									if (shippingAddressDetail.getFsCityMaster() != null) {
										cityDetails = cityDao.getCityDescription(districtMas.get(0).getDistrictId(),
												shippingAddressDetail.getFsCityMaster().getCityId(), new BigDecimal(1));
									}
									if (cityDetails != null && !cityDetails.isEmpty()) {
										shippingAddressDto.setCityDto(ResourceDTO.create(cityDetails.get(0)));
										shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());
									}
								}
							}
						}
					}
					shippingAddressDto.setDeliveryAddress(getLocalDeliveryAddress(shippingAddressDto));
				} else {
					throw new GlobalException(JaxError.COUNTRY_NOT_FOUND, "Failed");
				}
				list.add(shippingAddressDto); // Local Address
			} // end of for Loop
		} // end
		if (!list.isEmpty()) {
			list.get(list.size() - 1).setIsDefault(true);
		}
		return list;
	}

	public void saveShippingAddress(CustomerShippingAddressRequestModel requestModel) {
		try {
			final Pattern pattern = Pattern.compile("^[0-9]*$");

			ShippingAddressDetail shipAdd = new ShippingAddressDetail();

			if(JaxUtil.isNullZeroBigDecimalCheck(meta.getCustomerId())){
				shipAdd.setFsCustomer(new Customer(meta.getCustomerId()));
			}else{
				throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND,"Customer  id not found ");
			}

			/*
			  4703  FC Order Enhancement - Customer App 
			  if (StringUtils.isBlank(requestModel.getMobile())) {
				throw new GlobalException(JaxError.INVALID_MOBILE_NUMBER, "Invalid Mobile Number");
			}else {
				if (!pattern.matcher(requestModel.getMobile()).matches()) {
					throw new GlobalException(JaxError.INVALID_MOBILE_NUMBER, "Invalid Mobile Number");
				}
				countryMetaValidation.validateMobileNumber(meta.getCountryId(), requestModel.getMobile());
				countryMetaValidation.validateMobileNumberLength(meta.getCountryId(), requestModel.getMobile());
			}

			if (StringUtils.isBlank(requestModel.getTelPrefix())) {
				throw new GlobalException(JaxError.INVALID_MOBILE_PREFIX, "Invalid Tele Prefix");
			}else {
				if (!pattern.matcher(requestModel.getTelPrefix()).matches()) {
					throw new GlobalException(JaxError.INVALID_MOBILE_PREFIX, "Invalid Tele Prefix");
				}
			}*/

			shipAdd.setCreationDate(new Date());
			shipAdd.setActiveStatus(ConstantDocument.Yes);
			shipAdd.setAreaCode(requestModel.getAreaCode());
			shipAdd.setBlock(requestModel.getBlock());
			shipAdd.setBuildingNo(requestModel.getBuildingNo());
			shipAdd.setFlat(requestModel.getFlatNo());
			shipAdd.setStreet(requestModel.getStreet());

			shipAdd.setFsCountryMaster(new CountryMaster(meta.getCountryId()));
			
			if (JaxUtil.isNullZeroBigDecimalCheck(requestModel.getStateId())) {
				shipAdd.setFsStateMaster(new StateMaster(requestModel.getStateId()));
			}
			
			if (JaxUtil.isNullZeroBigDecimalCheck(requestModel.getDistrictId())) {
				shipAdd.setFsDistrictMaster(new DistrictMaster(requestModel.getDistrictId()));
			}
			
			if (JaxUtil.isNullZeroBigDecimalCheck(requestModel.getCityId())) {
				shipAdd.setFsCityMaster(new CityMaster(requestModel.getCityId()));
			}
			
			shipAdd.setAddressType(requestModel.getAddressTypeDto().getAddressTypeCode());

			if (!StringUtils.isBlank(meta.getReferrer())) {
				shipAdd.setCreatedBy(meta.getReferrer());
			} else {
				if (!StringUtils.isBlank(meta.getAppType())) {
					shipAdd.setCreatedBy(meta.getAppType());
				} else {
					shipAdd.setCreatedBy("WEB");
				}
			}

			shipAdd.setMobile(requestModel.getMobile());
			shipAdd.setTelephoneCode(requestModel.getTelPrefix());
			shipAdd.setTelephone(requestModel.getMobile());
			shipAdd.setGovernateId(requestModel.getGovermentId());
			shipAdd.setGoverAreaId(requestModel.getGovermentAreaId());
			shippingAddressDao.save(shipAdd);
		}catch(GlobalException e){
			logger.error("saveShippingAddress", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		} catch (Exception e) {
			logger.error("saveShippingAddress :", e.getMessage());
			throw new GlobalException(JaxError.FS_SHIPPING_ADDRESS_CREATION_FAILED, "Failed");
		}
	}

	public void deactivateShippingAddress(Customer customer) {
		List<ShippingAddressDetail> shippingAddressList = shippingAddressDao.findByFsCustomerAndActiveStatus(customer,
				ConstantDocument.Yes);
		if (shippingAddressList !=null && !shippingAddressList.isEmpty()) {
			for (ShippingAddressDetail add : shippingAddressList) {
				ShippingAddressDetail shippAdd = shippingAddressDao.findOne(add.getShippingAddressDetailId());
				shippAdd.setActiveStatus(ConstantDocument.Deleted);
				shippingAddressDao.save(shippAdd);
			}
		}
	}

	public List<ShippingAddressDto> deleteShippingAddress(BigDecimal shippingAddressDetailId) {
		if (JaxUtil.isNullZeroBigDecimalCheck(shippingAddressDetailId)) {
			ShippingAddressDetail shippAdd = shippingAddressDao.findOne(shippingAddressDetailId);
			if ( shippAdd !=null) {
				shippAdd.setShippingAddressDetailId(shippingAddressDetailId);
				shippAdd.setActiveStatus(ConstantDocument.Deleted);
				if (!StringUtils.isBlank(meta.getReferrer())) {
					shippAdd.setUpdatedBy(meta.getReferrer());
				} else {
					if (!StringUtils.isBlank(meta.getAppType())) {
						shippAdd.setUpdatedBy(meta.getAppType());
					} else {
						shippAdd.setUpdatedBy("WEB");
					}
				}
				shippAdd.setLastUpdated(new Date());
				shippingAddressDao.save(shippAdd);
			} else {
				throw new GlobalException(JaxError.NO_RECORD_FOUND, "Invalid shipping address");
			}
		}
		List<ShippingAddressDto> list = fetchShippingAddress();
		return list;
	}

	public List<ShippingAddressDto> editShippingAddress(ShippingAddressDto adddto) {
		if (adddto != null) {
			ShippingAddressDetail shippAdd = shippingAddressDao.findOne(adddto.getAddressId());
			if (shippAdd != null) {
				
				ShippingAddressDetail shipAdd = new ShippingAddressDetail();
				shipAdd.setShippingAddressDetailId(shippAdd.getShippingAddressDetailId());
				if (JaxUtil.isNullZeroBigDecimalCheck(meta.getCustomerId())) {
					shipAdd.setFsCustomer(new Customer(meta.getCustomerId()));
				} else {
					throw new GlobalException(JaxError.INVALID_CUSTOMER, "custoemr Id ");
				}
				shipAdd.setLastUpdated(new Date());
				shipAdd.setActiveStatus(ConstantDocument.Yes);
				if (adddto.getAreaDto() != null) {
					shipAdd.setAreaCode(adddto.getAreaDto().resourceId());
				}
				shipAdd.setBlock(adddto.getBlock());
				shipAdd.setBuildingNo(adddto.getBuildingNo());
				shipAdd.setFlat(adddto.getFlat());
				shipAdd.setStreet(adddto.getStreet());
				if (JaxUtil.isNullZeroBigDecimalCheck(meta.getCountryId())) {
					shipAdd.setFsCountryMaster(new CountryMaster(meta.getCountryId()));
				} else {
					throw new GlobalException(JaxError.INVALID_APPLICATION_COUNTRY_ID, "Invalid country Id  ");
				}
				if (adddto.getStateDto() != null) {
					shipAdd.setFsStateMaster(new StateMaster(adddto.getStateDto().resourceId()));
				} 
				if (adddto.getDistrictDto() !=  null) {
					shipAdd.setFsDistrictMaster(new DistrictMaster(adddto.getDistrictDto().resourceId()));
				} 
				if (adddto.getCityDto() != null) {
					shipAdd.setFsCityMaster(new CityMaster(adddto.getCityDto().resourceId()));
				}
				if (!StringUtils.isBlank(adddto.getAddressDto().getAddressTypeCode())) {
					shipAdd.setAddressType(adddto.getAddressDto().getAddressTypeCode());
				} else {
					throw new GlobalException(JaxError.INVALID_ADDRESS_TYPE, "Address type ");
				}
				if (!StringUtils.isBlank(meta.getReferrer())) {
					shipAdd.setUpdatedBy(meta.getReferrer());
				} else {
					if (!StringUtils.isBlank(meta.getAppType())) {
						shipAdd.setUpdatedBy(meta.getAppType());
					} else {
						shipAdd.setUpdatedBy("WEB");
					}
				}
				shipAdd.setMobile(adddto.getMobile());
				shipAdd.setTelephoneCode(adddto.getTelephoneCode());
				shipAdd.setTelephone(adddto.getMobile());
				
				if (adddto.getGovernoatesDto() != null) {
					shipAdd.setGovernateId(adddto.getGovernoatesDto().resourceId());
				}
				
				if (adddto.getGovernoateAreaDto() != null) {
					shipAdd.setGoverAreaId(adddto.getGovernoateAreaDto().resourceId());
				}else {
					throw new GlobalException(JaxError.NULL_AREA_CODE, "Invalid governate area");
				}
				
				
				shippingAddressDao.save(shipAdd);

			} else {
				throw new GlobalException(JaxError.NO_RECORD_FOUND, "Invalid shipping address");
			}

		} else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "Invalid shipping address");
		}

		List<ShippingAddressDto> list = fetchShippingAddress();
		return list;
	}

	public AddressTypeDto getAddressType(String addressTypecode) {
		List<AddressTypeDto> addtype = saleAppl.getAddressTypeList();
		AddressTypeDto dto1 = null;
		if (addtype !=null &&  !addtype.isEmpty()) {
			for (AddressTypeDto dto : addtype) {
				if (dto.getAddressTypeCode() != null && addressTypecode != null
						&& dto.getAddressTypeCode().equalsIgnoreCase(addressTypecode)) {
					dto1 = new AddressTypeDto();
					dto1.setAddressTypeCode(dto.getAddressTypeCode());
					dto1.setAddressTypeDesc(dto.getAddressTypeDesc());
					break;
				}
			}
		}
		return dto1;
	}
	

	public String getLocalDeliveryAddress(ShippingAddressDto shippingAddressDto){
		logger.debug("getDeliveryAddress  in FC Sale Address Manager :"+shippingAddressDto.getAddressId());
		String address ="";
		StringBuffer sb = new StringBuffer();
		String concat =",";
		if (shippingAddressDto != null) {
			sb = sb.append(shippingAddressDto.getStreet() == null ? "" : "Street "+  shippingAddressDto.getStreet() +concat)
					.append(shippingAddressDto.getBlock() == null ? "" : "Block " + shippingAddressDto.getBlockNo() + concat)
					.append(shippingAddressDto.getBuildingNo() == null ? "" : "House no. " + shippingAddressDto.getBuildingNo() + concat)
					.append(shippingAddressDto.getFlat() == null ? "" : "Flat "+  shippingAddressDto.getHouse() +concat);
					
			if (shippingAddressDto.getLocalContactCity() != null) {
				sb.append("City ").append(shippingAddressDto.getLocalContactCity() == null ? ""
						: shippingAddressDto.getLocalContactCity()).append(concat);
			}
			sb.append(
					shippingAddressDto.getGovernoatesDto() == null ? "" : shippingAddressDto.getGovernoatesDto().getResourceName() + concat);
			sb.append(
					shippingAddressDto.getGovtAreaDesc() == null ? "" : shippingAddressDto.getGovtAreaDesc() + concat);
			sb.append(shippingAddressDto.getLocalContactDistrict() == null ? ""
					: shippingAddressDto.getLocalContactDistrict() + concat);
			sb.append(shippingAddressDto.getLocalContactState() == null ? ""
					: shippingAddressDto.getLocalContactState());
			

		}
		if(sb!=null){
			address = sb.toString();
		}
		return address;
	}
	
	
	
}
