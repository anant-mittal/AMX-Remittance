package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.IViewArea;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.util.JaxUtil;

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

	/** Fetching shipping address **/

	public List<ShippingAddressDto> fetchShippingAddress() {
		List<ShippingAddressDto> list = new ArrayList<>();

		BigDecimal countryId = meta.getCountryId();
		BigDecimal customerId = meta.getCustomerId();
		BigDecimal companyId = meta.getCompanyId();

		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		List<ContactDetail> contactList = contactDao.getContactDetailForLocal(new Customer(customerId));
		List<ShippingAddressDetail> shippingAddressList = shippingAddressDao
				.findByFsCustomerAndActiveStatus(new Customer(customerId), ConstantDocument.Yes);

		if (!contactList.isEmpty()) {
			ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
			shippingAddressDto.setAddressId(contactList.get(0).getContactDetailId());
			if (!customerList.isEmpty()) {
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
			shippingAddressDto.setHouse(contactList.get(0).getFlat());
			shippingAddressDto.setAddressDto(
					getAddressType(contactList.get(0).getArea() == null ? "" : contactList.get(0).getArea()));
			shippingAddressDto.setAreaDesc(contactList.get(0).getArea() == null ? "" : contactList.get(0).getArea());

			List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),
					contactList.get(0).getFsCountryMaster().getCountryId());
			if (!countryMasterView.isEmpty()) {
				shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
				if (contactList.get(0).getFsStateMaster() != null) {
					List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),
							contactList.get(0).getFsStateMaster().getStateId(), new BigDecimal(1));
					if (!stateMasterView.isEmpty()) {
						shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
						shippingAddressDto.setStateDto(ResourceDTO.create(stateMasterView.get(0)));
						DistrictMaster distictMaster = contactList.get(0).getFsDistrictMaster();
						if (distictMaster != null) {
							List<ViewDistrict> districtMas = districtDao.getDistrict(
									stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),
									new BigDecimal(1));
							if (!districtMas.isEmpty()) {
								shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
								shippingAddressDto.setDistrictDto(ResourceDTO.create(districtMas.get(0)));
								List<ViewCity> cityDetails = cityDao.getCityDescription(
										districtMas.get(0).getDistrictId(),
										contactList.get(0).getFsCityMaster().getCityId(), new BigDecimal(1));
								if (!cityDetails.isEmpty()) {
									shippingAddressDto.setCityDto(ResourceDTO.create(cityDetails.get(0)));
									shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());

								}
							}
						}
					}
				}

			}
			shippingAddressDto.setAdressType("Local address");
			list.add(shippingAddressDto); // Local Address
		} // Local contact details

		/** Adding shipping Address **/

		if (!shippingAddressList.isEmpty()) {
			for (ShippingAddressDetail shippingAddressDetail : shippingAddressList) {
				ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
				shippingAddressDto.setAddressId(shippingAddressDetail.getShippingAddressDetailId());

				if (!customerList.isEmpty()) {
					shippingAddressDto.setFirstName(customerList.get(0).getFirstName());
					shippingAddressDto.setMiddleName(customerList.get(0).getMiddleName());
					shippingAddressDto.setLastName(customerList.get(0).getLastName());
				}
				shippingAddressDto.setCustomerId(shippingAddressDetail.getFsCustomer().getCustomerId());
				shippingAddressDto.setCompanyId(companyId);
				shippingAddressDto.setMobile(shippingAddressDetail.getMobile());
				shippingAddressDto.setLocalContactBuilding(shippingAddressDetail.getBuildingNo());
				shippingAddressDto.setStreet(shippingAddressDetail.getStreet());
				shippingAddressDto.setBlockNo(shippingAddressDetail.getBlock());
				shippingAddressDto.setHouse(shippingAddressDetail.getFlat());
				shippingAddressDto.setAddressDto(getAddressType(shippingAddressDetail.getAddressType()));
				shippingAddressDto.setAreaDesc(areaDao.getAreaList(shippingAddressDetail.getAreaCode()) == null ? ""
						: areaDao.getAreaList(shippingAddressDetail.getAreaCode()).getShortDesc());
				ViewAreaModel areaModel = areaDao.getAreaList(shippingAddressDetail.getAreaCode());
				if (areaModel != null) {
					shippingAddressDto.setAreaDto(ResourceDTO.create(areaModel));
				}
				List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),
						shippingAddressDetail.getFsCountryMaster().getCountryId());
				if (!countryMasterView.isEmpty()) {
					shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
					shippingAddressDto.setCountryDto(
							ResourceDTO.create(countryMasterView.get(0)));
					if (contactList.get(0).getFsStateMaster() != null) {
						List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),
								shippingAddressDetail.getFsStateMaster().getStateId(), new BigDecimal(1));
						if (!stateMasterView.isEmpty()) {
							shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
							shippingAddressDto.setStateDto(
									ResourceDTO.create(stateMasterView.get(0)));
							DistrictMaster distictMaster = shippingAddressDetail.getFsDistrictMaster();
							if (distictMaster != null) {
								List<ViewDistrict> districtMas = districtDao.getDistrict(
										stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),
										new BigDecimal(1));
								if (!districtMas.isEmpty()) {
									shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
									shippingAddressDto.setDistrictDto(
											ResourceDTO.create(districtMas.get(0)));
									List<ViewCity> cityDetails = cityDao.getCityDescription(
											districtMas.get(0).getDistrictId(),
											shippingAddressDetail.getFsCityMaster().getCityId(), new BigDecimal(1));
									if (!cityDetails.isEmpty()) {
										shippingAddressDto.setCityDto(
												ResourceDTO.create(cityDetails.get(0)));
										shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());
									}
								}
							}
						}
					}

				} else {
					throw new GlobalException("Failed", JaxError.COUNTRY_NOT_FOUND);
				}
				list.add(shippingAddressDto); // Local Address
			} // end of for Loop
		} // end

		/** fetch End of shipping address **/
		return list;
	}

	public void saveShippingAddress(CustomerShippingAddressRequestModel requestModel) {
		try {
			ShippingAddressDetail shipAdd = new ShippingAddressDetail();
			shipAdd.setFsCustomer(new Customer(meta.getCustomerId()));
			shipAdd.setCreationDate(new Date());
			shipAdd.setActiveStatus(ConstantDocument.Yes);
			shipAdd.setAreaCode(requestModel.getAreaCode());
			shipAdd.setBlock(requestModel.getBlock());
			shipAdd.setBuildingNo(requestModel.getBuildingNo());
			shipAdd.setFlat(requestModel.getFlatNo());
			shipAdd.setStreet(requestModel.getStreet());
			shipAdd.setFsCountryMaster(new CountryMaster(meta.getCountryId()));
			shipAdd.setFsStateMaster(new StateMaster(requestModel.getStateId()));
			shipAdd.setFsDistrictMaster(new DistrictMaster(requestModel.getDistrictId()));
			shipAdd.setFsCityMaster(new CityMaster(requestModel.getCityId()));
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
			// deactivateShippingAddress(new Customer(meta.getCustomerId()));
			shippingAddressDao.save(shipAdd);
		} catch (Exception e) {
			logger.error("saveShippingAddress :", e.getMessage());
			throw new GlobalException("Failed", JaxError.FS_SHIPPING_ADDRESS_CREATION_FAILED);
		}
	}

	public void deactivateShippingAddress(Customer customer) {
		List<ShippingAddressDetail> shippingAddressList = shippingAddressDao.findByFsCustomerAndActiveStatus(customer,
				ConstantDocument.Yes);
		if (!shippingAddressList.isEmpty()) {
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
			if (shippAdd != null) {
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
				throw new GlobalException("Invalid shipping address", JaxError.NO_RECORD_FOUND);
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
				if (JaxUtil.isNullZeroBigDecimalCheck(meta.getCustomerId())) {
					shipAdd.setFsCustomer(new Customer(meta.getCustomerId()));
				} else {
					throw new GlobalException("custoemr Id ", JaxError.INVALID_CUSTOMER);
				}
				shipAdd.setLastUpdated(new Date());
				shipAdd.setActiveStatus(ConstantDocument.Yes);
				if (JaxUtil.isNullZeroBigDecimalCheck(adddto.getAreaDto().resourceId())) {
					shipAdd.setAreaCode(adddto.getAreaDto().resourceId());
				} else {
					throw new GlobalException("custoemr Id ", JaxError.NULL_AREA_CODE);
				}
				shipAdd.setBlock(adddto.getBlock());
				shipAdd.setBuildingNo(adddto.getBuildingNo());
				shipAdd.setFlat(adddto.getFlat());
				shipAdd.setStreet(adddto.getStreet());
				if (JaxUtil.isNullZeroBigDecimalCheck(meta.getCountryId())) {
					shipAdd.setFsCountryMaster(new CountryMaster(meta.getCountryId()));
				} else {
					throw new GlobalException("Invalid country Id  ", JaxError.INVALID_APPLICATION_COUNTRY_ID);
				}
				if (JaxUtil.isNullZeroBigDecimalCheck(adddto.getStateDto().resourceId())) {
					shipAdd.setFsStateMaster(new StateMaster(adddto.getStateDto().resourceId()));
				} else {
					throw new GlobalException("Invalid state  ", JaxError.INVALID_STATE);
				}
				if (JaxUtil.isNullZeroBigDecimalCheck(adddto.getDistrictDto().resourceId())) {
					shipAdd.setFsDistrictMaster(new DistrictMaster(adddto.getDistrictDto().resourceId()));
				} else {
					throw new GlobalException("Invalid district  ", JaxError.INVALID_DISTRICT);
				}
				if (JaxUtil.isNullZeroBigDecimalCheck(adddto.getCityDto().resourceId())) {
					shipAdd.setFsCityMaster(new CityMaster(adddto.getCityDto().resourceId()));
				} else {
					throw new GlobalException("Invalid city  ", JaxError.INVALID_CITY);
				}
				if (!StringUtils.isBlank(adddto.getAddressDto().getAddressTypeCode())) {
					shipAdd.setAddressType(adddto.getAddressDto().getAddressTypeCode());
				} else {
					throw new GlobalException("Address type ", JaxError.INVALID_ADDRESS_TYPE);
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
				shippingAddressDao.save(shipAdd);

			} else {
				throw new GlobalException("Invalid shipping address", JaxError.NO_RECORD_FOUND);
			}

		} else {
			throw new GlobalException("Invalid shipping address", JaxError.NO_RECORD_FOUND);
		}

		List<ShippingAddressDto> list = fetchShippingAddress();
		return list;
	}

	public AddressTypeDto getAddressType(String addressTypecode) {
		List<AddressTypeDto> addtype = saleAppl.getAddressTypeList();
		AddressTypeDto dto1 = null;
		if (!addtype.isEmpty()) {
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

}
