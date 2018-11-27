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
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IShippingAddressRepository;
import com.amx.jax.repository.IViewArea;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class FcSaleAddressManager  extends AbstractModel{
	
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

	/** Fetching shipping address **/
	
	public List<ShippingAddressDto> fetchShippingAddress(){
		List<ShippingAddressDto> list = new ArrayList<>();
		
		BigDecimal countryId = meta.getCountryId();
		BigDecimal customerId = meta.getCustomerId();
		BigDecimal companyId = meta.getCompanyId();
		
		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		List<ContactDetail> contactList = contactDao.getContactDetailForLocal(new Customer(customerId));
		List<ShippingAddressDetail> shippingAddressList = shippingAddressDao.findByFsCustomerAndActiveStatus(new Customer(customerId), ConstantDocument.Yes);
		
		if (!contactList.isEmpty()) {
			ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
			shippingAddressDto.setAddressId(contactList.get(0).getContactDetailId());
			if(!customerList.isEmpty()){
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

			List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),contactList.get(0).getFsCountryMaster().getCountryId());
			if (!countryMasterView.isEmpty()) {
				shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
				if (contactList.get(0).getFsStateMaster() != null) {
					List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),contactList.get(0).getFsStateMaster().getStateId(), new BigDecimal(1));
					if (!stateMasterView.isEmpty()) {
						shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
						DistrictMaster distictMaster = contactList.get(0).getFsDistrictMaster();
						if (distictMaster != null) {
							List<ViewDistrict> districtMas = districtDao.getDistrict(stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),new BigDecimal(1));
							if (!districtMas.isEmpty()) {
								shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
								List<ViewCity> cityDetails = cityDao.getCityDescription(districtMas.get(0).getDistrictId(),contactList.get(0).getFsCityMaster().getCityId(), new BigDecimal(1));
								if (!cityDetails.isEmpty()) {
									shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());
								}
							}
						}
					}
				}

			}
			shippingAddressDto.setAdressType("local_address");
			list.add(shippingAddressDto); // Local Address
		} //Local contact details
		
		
		
		/** Adding shipping Address **/
		
		
		if (!shippingAddressList.isEmpty()) {
			for (ShippingAddressDetail shippingAddressDetail : shippingAddressList) {
				ShippingAddressDto shippingAddressDto = new ShippingAddressDto();
				shippingAddressDto.setAddressId(shippingAddressDetail.getShippingAddressDetailId());
				
				if(!customerList.isEmpty()){
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
			shippingAddressDto.setAdressType(shippingAddressDetail.getAddressType());
			shippingAddressDto.setAreaDesc(areaDao.getAreaList(shippingAddressDetail.getAreaCode())==null?"":areaDao.getAreaList(shippingAddressDetail.getAreaCode()).getShortDesc());
			List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),shippingAddressDetail.getFsCountryMaster().getCountryId());
			if (!countryMasterView.isEmpty()) {
				shippingAddressDto.setLocalContactCountry(countryMasterView.get(0).getCountryName());
				if (contactList.get(0).getFsStateMaster() != null) {
					List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),shippingAddressDetail.getFsStateMaster().getStateId(), new BigDecimal(1));
					if (!stateMasterView.isEmpty()) {
						shippingAddressDto.setLocalContactState(stateMasterView.get(0).getStateName());
						DistrictMaster distictMaster = shippingAddressDetail.getFsDistrictMaster();
						if (distictMaster != null) {
							List<ViewDistrict> districtMas = districtDao.getDistrict(stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),new BigDecimal(1));
							if (!districtMas.isEmpty()) {
								shippingAddressDto.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
								List<ViewCity> cityDetails = cityDao.getCityDescription(districtMas.get(0).getDistrictId(),shippingAddressDetail.getFsCityMaster().getCityId(), new BigDecimal(1));
								if (!cityDetails.isEmpty()) {
									shippingAddressDto.setLocalContactCity(cityDetails.get(0).getCityName());
								}
							}
						}
					}
				}

			}
			list.add(shippingAddressDto); // Local Address
		} //end of for Loop
	} //end 
		
		/** fetch End of shipping address **/
		return list;
	}
	
	
	public void saveShippingAddress(CustomerShippingAddressRequestModel requestModel){
		try{
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
		shipAdd.setAddressType(requestModel.getAddressType());
		if(!StringUtils.isBlank(meta.getReferrer())){
			shipAdd.setCreatedBy(meta.getReferrer());
		}else{
			if(!StringUtils.isBlank(meta.getAppType())){				
				shipAdd.setCreatedBy(meta.getAppType());
			}else{
				shipAdd.setCreatedBy("WEB");
			 }
		}
		shipAdd.setMobile(requestModel.getMobile());
		shipAdd.setTelephoneCode(requestModel.getTelPrefix());
		shipAdd.setTelephone(requestModel.getMobile());
		deactivateShippingAddress(new Customer(meta.getCustomerId()));
		shippingAddressDao.save(shipAdd);
		}catch(Exception e){
			logger.error("saveShippingAddress :", e.getMessage());
			throw new GlobalException("Failed" ,JaxError.FS_SHIPPING_ADDRESS_CREATION_FAILED);
		}
	}

	public void deactivateShippingAddress(Customer customer){
     List<ShippingAddressDetail> shippingAddressList = shippingAddressDao.findByFsCustomerAndActiveStatus(customer, ConstantDocument.Yes);
     if(!shippingAddressList.isEmpty()){
    	 for( ShippingAddressDetail add: shippingAddressList){
    		 ShippingAddressDetail shippAdd = shippingAddressDao.findOne(add.getShippingAddressDetailId());
    		 shippAdd.setActiveStatus(ConstantDocument.Deleted);
    		 shippingAddressDao.save(shippAdd);
    	 }
     }
	}
	
}
