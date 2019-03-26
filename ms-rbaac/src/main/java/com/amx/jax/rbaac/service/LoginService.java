package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.FunctionalityTypeMaster;
import com.amx.jax.rbaac.dbmodel.ModuleMaster;
import com.amx.jax.rbaac.dbmodel.PermissionMaster;
import com.amx.jax.rbaac.dbmodel.PermissionScopeMaster;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.RoleMaster;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;
import com.amx.jax.rbaac.dto.RoleDefinitionDataTable;
import com.amx.jax.rbaac.dto.UserDetailsDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.manager.AuthLoginManager;
import com.amx.jax.rbaac.manager.AuthLoginOTPManager;
import com.amx.jax.rbaac.models.Module;
import com.amx.jax.rbaac.models.PermScope;
import com.amx.jax.rbaac.models.PermType;
import com.amx.jax.rbaac.models.Permission;
import com.amx.jax.rbaac.trnx.AuthLoginTrnxModel;
import com.amx.jax.util.JaxUtil;

/**
 * The Class LoginService.
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoginService extends AbstractService {

	/** The logger. */
	Logger logger = Logger.getLogger(LoginService.class);

	/** The login dao. */
	@Autowired
	RbaacDao rbaacDao;

	/** The auth login manager. */
	@Autowired
	AuthLoginManager authLoginManager;

	/** The auth login OTP manager. */
	@Autowired
	AuthLoginOTPManager authLoginOTPManager;

	/** The jax util. */
	@Autowired
	JaxUtil jaxUtil;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.service.AbstractService#getModelType()
	 */
	@Override
	public String getModelType() {
		return null;
	}

	/**
	 * Validate employee data.
	 *
	 * @param empcode
	 *            the empcode
	 * @param identity
	 *            the identity
	 * @param ipAddress
	 *            the ip address
	 * @return the employee
	 */
	public Employee validateEmployeeData(String empcode, String identity, String ipAddress) {
		Employee emp = rbaacDao.fetchEmpDetails(empcode, identity, ipAddress);
		return emp;
	}

	/**
	 * Fetch employee details.
	 *
	 * @param user
	 *            the user
	 * @param pass
	 *            the pass
	 * @return the employee
	 */
	public Employee fetchEmployeeDetails(String user, String pass) {
		Employee emp = rbaacDao.fetchEmpDetails(user, pass);
		return emp;
	}

	/**
	 * Fetch employee details by ECNO.
	 *
	 * @param empNo
	 *            the emp no
	 * @return the employee
	 */
	public Employee fetchEmployeeDetailsByECNO(String empNo) {
		Employee emp = rbaacDao.fetchEmpDetailsByECNO(empNo);
		return emp;
	}

	/**
	 * Fetch employee role def.
	 *
	 * @param role
	 *            the role
	 * @return the list
	 */
	public List<RoleDefinition> fetchEmployeeRoleDef(BigDecimal role) {
		List<RoleDefinition> roleDef = rbaacDao.fetchEmpRoleMenu(role);
		return roleDef;
	}

	/**
	 * Save enums.
	 *
	 * @return the api response
	 */
	// store enums
	public ApiResponse saveEnums() {
		boolean savesStatus = Boolean.FALSE;
		try {
			// fetch module enums
			boolean modulestatus = saveModule();
			System.out.println("Module : " + modulestatus);
			boolean funcTypestatus = saveFunctionalityTypeMaster();
			System.out.println("FunctionalityType : " + funcTypestatus);
			boolean permScope = savePermissionScopeMaster();
			System.out.println("PermScope : " + permScope);
			boolean perm = savePermission();
			System.out.println("perm : " + perm);

			if (modulestatus || funcTypestatus || permScope || perm) {
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "saveEnums fail ");
		}

		return getBooleanResponse(savesStatus);
	}

	/**
	 * Save module.
	 *
	 * @return true, if successful
	 */
	// save Modules
	public boolean saveModule() {

		boolean savesStatus = Boolean.FALSE;
		Set<String> moduleEnumData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> moduleDBData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		List<ModuleMaster> moduleStore = new ArrayList<ModuleMaster>();

		try {

			Module[] moduledt = Module.values();

			for (int i = 0; i < moduledt.length; i++) {
				Module module = moduledt[i];
				moduleEnumData.add(module.name());
			}

			// fetch all the data from module table
			List<ModuleMaster> moduleDB = rbaacDao.fetchModule();

			if (moduleDB != null && moduleDB.size() != 0) {
				for (ModuleMaster moduleMaster : moduleDB) {
					moduleDBData.add(moduleMaster.getModuleEnum());
				}
			}

			moduleEnumData.removeAll(moduleDBData);

			System.out.println("not available in db : " + moduleEnumData);

			for (String moduleMaster : moduleEnumData) {
				ModuleMaster module = new ModuleMaster();

				module.setModuleEnum(moduleMaster);
				module.setCreatedDate(new Date());
				module.setIsactive("Y");

				moduleStore.add(module);
			}

			if (moduleStore != null && moduleStore.size() != 0) {
				rbaacDao.saveModuleData(moduleStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "saveModule fail ");
		}

		return savesStatus;
	}

	/**
	 * Save functionality type master.
	 *
	 * @return true, if successful
	 */
	public boolean saveFunctionalityTypeMaster() {

		boolean savesStatus = Boolean.FALSE;
		Set<String> funTypeEnumData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> funTypeDBData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		List<FunctionalityTypeMaster> funTypeStore = new ArrayList<FunctionalityTypeMaster>();

		try {
			PermType[] permTypedt = PermType.values();

			for (int i = 0; i < permTypedt.length; i++) {
				PermType permType = permTypedt[i];
				funTypeEnumData.add(permType.name());
			}

			// fetch all the data from module table
			List<FunctionalityTypeMaster> permTypeDB = rbaacDao.fetchFunctionalityTypeMaster();

			if (permTypeDB != null && permTypeDB.size() != 0) {
				for (FunctionalityTypeMaster functionalityTypeMaster : permTypeDB) {
					funTypeDBData.add(functionalityTypeMaster.getFunctionalityTypeEnum());
				}
			}

			funTypeEnumData.removeAll(funTypeDBData);

			System.out.println("not available in db : " + funTypeEnumData);

			for (String funTypeMaster : funTypeEnumData) {
				FunctionalityTypeMaster funcTypeM = new FunctionalityTypeMaster();

				funcTypeM.setFunctionalityTypeEnum(funTypeMaster);
				funcTypeM.setCreatedDate(new Date());
				funcTypeM.setIsactive("Y");

				funTypeStore.add(funcTypeM);
			}

			if (funTypeStore != null && funTypeStore.size() != 0) {
				rbaacDao.saveFunctionalityTypeMaster(funTypeStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "saveFunctionalityTypeMaster fail ");
		}

		return savesStatus;
	}

	/**
	 * Save permission scope master.
	 *
	 * @return true, if successful
	 */
	public boolean savePermissionScopeMaster() {

		boolean savesStatus = Boolean.FALSE;
		Set<String> permScopeEnumData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> permScopeDBData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		List<PermissionScopeMaster> permScopeStore = new ArrayList<PermissionScopeMaster>();

		try {

			PermScope[] permScopedt = PermScope.values();

			for (int i = 0; i < permScopedt.length; i++) {
				PermScope permScope = permScopedt[i];
				permScopeEnumData.add(permScope.name());
			}

			// fetch all the data from module table
			List<PermissionScopeMaster> permScopeDB = rbaacDao.fetchPermissionScopeMaster();

			if (permScopeDB != null && permScopeDB.size() != 0) {
				for (PermissionScopeMaster permissionScopeMaster : permScopeDB) {
					permScopeDBData.add(permissionScopeMaster.getScopeEnum());
				}
			}

			permScopeEnumData.removeAll(permScopeDBData);

			System.out.println("not available in db : " + permScopeEnumData);

			for (String permScopeMaster : permScopeEnumData) {
				PermissionScopeMaster permScopeM = new PermissionScopeMaster();

				permScopeM.setScopeEnum(permScopeMaster);
				permScopeM.setCreatedDate(new Date());
				permScopeM.setIsactive("Y");

				permScopeStore.add(permScopeM);
			}

			if (permScopeStore != null && permScopeStore.size() != 0) {
				rbaacDao.savePermissionScopeMaster(permScopeStore);
				savesStatus = Boolean.TRUE;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "savePermissionScopeMaster fail ");
		}

		return savesStatus;
	}

	/**
	 * Null check.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	// to check null
	private String nullCheck(String value) {
		return value == null ? "" : value.trim().concat("|");
	}

	/**
	 * Null check WO.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	private String nullCheckWO(String value) {
		return value == null ? "" : value.trim();
	}

	/**
	 * Save permission.
	 *
	 * @return true, if successful
	 */
	// save permission
	public boolean savePermission() {

		boolean savesStatus = Boolean.FALSE;
		HashMap<String, PermissionMaster> mapPerm = new HashMap<String, PermissionMaster>();
		Set<String> permEnumData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> permDBData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		List<PermissionMaster> permStore = new ArrayList<PermissionMaster>();

		try {
			Permission[] permdt = Permission.values();
			mapPerm.clear();
			for (int i = 0; i < permdt.length; i++) {
				Permission perm = permdt[i];
				String permId = nullCheck(permdt[i].getModule().name()) + nullCheck(permdt[i].getPermType().name())
						+ nullCheckWO(permdt[i].getPermission());

				BigDecimal moduleId = fetchModuleId(permdt[i].getModule().name());
				BigDecimal functionalityTypeId = fetchFunctionalityTypeId(permdt[i].getPermType().name());
				String functionality = permdt[i].getPermission();
				BigDecimal permissionId = fetchPermissionMasterId(moduleId, functionalityTypeId, functionality);
				PermissionMaster permMast = new PermissionMaster();
				permMast.setModuleId(moduleId);
				permMast.setFunctionalityTypeId(functionalityTypeId);
				permMast.setFunctionality(functionality);
				permMast.setPermissionEnum(perm.name());
				permMast.setPermissionId(permissionId);
				permMast.setCreatedDate(new Date());
				permMast.setIsactive("Y");
				if (permissionId != null) {
					permEnumData.add(permissionId.toString());
				}
				permStore.add(permMast);
				// mapPerm.put(permId, permMast);
			}

			// fetch all the data from module table
			/*
			 * List<PermissionMaster> permDB = rbaacDao.fetchPermissionMaster();
			 * 
			 * if(permDB != null && permDB.size() != 0){ for (PermissionMaster
			 * permissionMaster : permDB) { if(permissionMaster.getPermissionId() != null){
			 * permDBData.add(permissionMaster.getPermissionId().toString()); } } }
			 * 
			 * permEnumData.removeAll(permDBData);
			 * 
			 * System.out.println("not available in db : " + permEnumData);
			 * 
			 * for (String permMaster : permEnumData) { PermissionMaster permM = new
			 * PermissionMaster();
			 * 
			 * permM.setModuleId(mapPerm.get(permMaster).getModuleId());
			 * permM.setFunctionalityTypeId(mapPerm.get(permMaster).getFunctionalityTypeId()
			 * ); permM.setFunctionality(mapPerm.get(permMaster).getFunctionality());
			 * permM.setPermissionEnum(mapPerm.get(permMaster).getPermissionEnum());
			 * permStore.add(permM); }
			 */

			if (permStore != null && permStore.size() != 0) {
				rbaacDao.savePermissionMaster(permStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "savePermission fail ");
		}

		return savesStatus;
	}

	/**
	 * Fetch module id.
	 *
	 * @param module
	 *            the module
	 * @return the big decimal
	 */
	// fetch module id by name
	public BigDecimal fetchModuleId(String module) {
		BigDecimal moduleId = null;
		ModuleMaster moduleMaster = rbaacDao.fetchModuleId(module);
		if (moduleMaster != null) {
			moduleId = moduleMaster.getModuleId();
		}
		return moduleId;
	}

	/**
	 * Fetch functionality type id.
	 *
	 * @param functionalityType
	 *            the functionality type
	 * @return the big decimal
	 */
	// fetch module id by name
	public BigDecimal fetchFunctionalityTypeId(String functionalityType) {
		BigDecimal functionalityTypeId = null;
		FunctionalityTypeMaster functionalityTypeMasterId = rbaacDao.fetchFunctionalityTypeMasterId(functionalityType);
		if (functionalityTypeMasterId != null) {
			functionalityTypeId = functionalityTypeMasterId.getFunctionalityTypeId();
		}
		return functionalityTypeId;
	}

	/**
	 * Fetch permission master id.
	 *
	 * @param moduleId
	 *            the module id
	 * @param functionalityTypeId
	 *            the functionality type id
	 * @param functionality
	 *            the functionality
	 * @return the big decimal
	 */
	// fetch permission master
	public BigDecimal fetchPermissionMasterId(BigDecimal moduleId, BigDecimal functionalityTypeId,
			String functionality) {
		BigDecimal permissionId = null;
		PermissionMaster permissionMaster = rbaacDao.fetchPermissionMasterId(moduleId, functionalityTypeId,
				functionality);
		if (permissionMaster != null) {
			permissionId = permissionMaster.getPermissionId();
		}
		return permissionId;
	}

	/**
	 * Save role master.
	 *
	 * @param roleTitle
	 *            the role title
	 * @return the api response
	 */
	// save role
	public ApiResponse saveRoleMaster(String roleTitle) {

		boolean savesStatus = Boolean.FALSE;
		Set<String> roleMasterData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> roleMasterDBData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		List<RoleMaster> roleMasterStore = new ArrayList<RoleMaster>();

		try {

			roleMasterData.add(roleTitle);

			// fetch all the data from module table
			List<RoleMaster> roleMasterDB = rbaacDao.fetchRoleMaster();

			if (roleMasterDB != null && roleMasterDB.size() != 0) {
				for (RoleMaster roleMaster : roleMasterDB) {
					roleMasterDBData.add(roleMaster.getRoleCode());
				}
			}

			roleMasterData.removeAll(roleMasterDBData);

			System.out.println("not available in db : " + roleMasterData);

			for (String roleMaster : roleMasterData) {
				RoleMaster roleM = new RoleMaster();

				// roleM.setRoleCode(roleCode);
				roleM.setCreatedDate(new Date());
				roleM.setIsactive("Y");
				roleM.setRoleDescription(roleTitle);
				roleMasterStore.add(roleM);
			}

			if (roleMasterStore != null && roleMasterStore.size() != 0) {
				rbaacDao.saveRoleMaster(roleMasterStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "saveRoleMaster fail ");
		}

		return getBooleanResponse(savesStatus);
	}

	/**
	 * Fetch user master details.
	 *
	 * @param userId
	 *            the user id
	 * @return the api response
	 */
	public ApiResponse fetchUserMasterDetails(BigDecimal userId) {
		ApiResponse apiResponse = null;
		UserRoleMaster user = rbaacDao.fetchUserMasterDetails(userId);

		UserDetailsDTO userDetail = new UserDetailsDTO();

		apiResponse = getBlackApiResponse();

		jaxUtil.convert(user, userDetail);

		apiResponse.getData().getValues().add(userDetail);
		apiResponse.getData().setType("user-detail");

		return apiResponse;
	}

	/**
	 * Save assign role to user.
	 *
	 * @param roleId
	 *            the role id
	 * @param userId
	 *            the user id
	 * @return the api response
	 */
	public ApiResponse saveAssignRoleToUser(BigDecimal roleId, BigDecimal userId) {

		boolean savesStatus = Boolean.FALSE;

		try {
			ApiResponse userMaster = fetchUserMasterDetails(userId);
			if (userMaster != null) {
				UserRoleMaster user = (UserRoleMaster) userMaster.getData().getValues().get(0);

				UserRoleMaster userM = new UserRoleMaster();

				if (user != null) {
					userM.setCreatedDate(new Date());
					userM.setIsactive("Y");
					userM.setUserRoleId(user.getUserRoleId());
					userM.setEmployeeId(user.getEmployeeId());
					if (user.getRoleId() != null && user.getRoleId().compareTo(roleId) == 0) {
						// error already exist
						throw new AuthServiceException(RbaacServiceError.ALREADY_EXIST, "saveAssignRoleToUser fail ");
					} else {
						userM.setRoleId(roleId);
					}
				} else {
					userM.setEmployeeId(userId);
					userM.setRoleId(roleId);
				}

				rbaacDao.saveRoleToUser(userM);
				savesStatus = Boolean.TRUE;
			} else {
				throw new AuthServiceException(RbaacServiceError.INVALID_USER_DETAILS, "saveAssignRoleToUser fail ");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "saveAssignRoleToUser fail ");
		}

		return getBooleanResponse(savesStatus);
	}

	/**
	 * Fetch permission scope id.
	 *
	 * @param permScope
	 *            the perm scope
	 * @return the big decimal
	 */
	// fetch permission scope Id by permission description
	public BigDecimal fetchPermissionScopeId(String permScope) {
		BigDecimal scopeId = null;
		PermissionScopeMaster permissionScopeMaster = rbaacDao.fetchPermissionScopeMasterId(permScope);
		if (permissionScopeMaster != null) {
			scopeId = permissionScopeMaster.getScopeId();
		}

		return scopeId;
	}

	/**
	 * Save assign perm to role.
	 *
	 * @param roleId
	 *            the role id
	 * @param permission
	 *            the permission
	 * @param permScope
	 *            the perm scope
	 * @param admin
	 *            the admin
	 * @return the api response
	 */
	// save Permission and scope to a role
	public ApiResponse saveAssignPermToRole(BigDecimal roleId, Permission permission, PermScope permScope,
			String admin) {
		boolean savesStatus = Boolean.FALSE;
		try {
			BigDecimal moduleId = fetchModuleId(permission.getModule().name());
			BigDecimal functionalityTypeId = fetchFunctionalityTypeId(permission.getPermType().name());
			String functionality = permission.getPermission();
			BigDecimal permissionId = fetchPermissionMasterId(moduleId, functionalityTypeId, functionality);
			System.out.println("permission" + permissionId);
			BigDecimal scopeId = fetchPermissionScopeId(permScope.name());

			RoleDefinition roleDef = rbaacDao.fetchRoleDefinitionByRolePermScope(roleId, permissionId, scopeId);
			System.out.println(roleDef);

			RoleDefinition roleDefinition = new RoleDefinition();
			if (roleDef != null) {
				roleDefinition.setRoleDefId(roleDef.getRoleDefId());
				roleDefinition.setPermissionId(permissionId);
				roleDefinition.setRoleId(roleId);
				roleDefinition.setScopeId(scopeId);
				roleDefinition.setCreatedDate(roleDef.getCreatedDate());
				roleDefinition.setIsactive(roleDef.getIsactive());
				roleDefinition.setAdmin(admin);
			} else {
				roleDefinition.setPermissionId(permissionId);
				roleDefinition.setRoleId(roleId);
				roleDefinition.setScopeId(scopeId);
				roleDefinition.setCreatedDate(new Date());
				roleDefinition.setIsactive("Y");
				roleDefinition.setAdmin(admin);
			}

			rbaacDao.saveRoleDefintionDetails(roleDefinition);
			savesStatus = Boolean.TRUE;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "saveAssignPermToRole fail ");
		}

		return getBooleanResponse(savesStatus);
	}

	/**
	 * Sends otp initiating trnx.
	 *
	 * @param emp
	 *            the emp
	 * @return the api response
	 */
	public ApiResponse sendOtp(Employee emp) {
		ApiResponse apiResponse = null;

		try {
			BeanPropertyBindingResult errors = new BeanPropertyBindingResult(emp, "emp");
			// initiate transaction
			AuthLoginTrnxModel trnxModel = authLoginManager.init(emp);
			apiResponse = getBlackApiResponse();
			SendOtpModel output = authLoginOTPManager.generateOtpTokensStaff(emp.getEmployeeNumber());
			authLoginOTPManager.sendOtpStaff();
			apiResponse.getData().getValues().add(output);
			apiResponse.getData().setType("send-otp-model");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "sendOtp fail ");
		}

		return apiResponse;
	}

	/**
	 * validates otp.
	 *
	 * @param emp
	 *            the emp
	 * @param mOtp
	 *            the m otp
	 * @return the api response
	 */
	public ApiResponse validateOtp(Employee emp, String mOtp) {
		AuthLoginTrnxModel authLoginTrnxModel = authLoginOTPManager.validateOtpStaff(emp, mOtp);
		ApiResponse apiResponse = getBlackApiResponse();

		EmployeeDetailsDTO empDetail = new EmployeeDetailsDTO();

		empDetail.setCivilId(authLoginTrnxModel.getEmpDetails().getCivilId());
		empDetail.setCountryId(authLoginTrnxModel.getEmpDetails().getCountryId());
		empDetail.setDesignation(authLoginTrnxModel.getEmpDetails().getDesignation());
		empDetail.setEmail(authLoginTrnxModel.getEmpDetails().getEmail());
		empDetail.setEmployeeId(authLoginTrnxModel.getEmpDetails().getEmployeeId());
		empDetail.setEmployeeName(authLoginTrnxModel.getEmpDetails().getEmployeeName());
		empDetail.setEmployeeNumber(authLoginTrnxModel.getEmpDetails().getEmployeeNumber());
		empDetail.setLocation(authLoginTrnxModel.getEmpDetails().getLocation());
		empDetail.setTelephoneNumber(authLoginTrnxModel.getEmpDetails().getTelephoneNumber());
		empDetail.setUserName(authLoginTrnxModel.getEmpDetails().getUserName());

		List<RoleDefinitionDataTable> lstRoleDF = new ArrayList<RoleDefinitionDataTable>();
		for (RoleDefinition roleDef : authLoginTrnxModel.getRoleDefinition()) {
			RoleDefinitionDataTable roleDefDT = new RoleDefinitionDataTable();

			roleDefDT.setModuleId(roleDef.getRoleId());
			roleDefDT.setPermissionId(roleDef.getPermissionId());
			roleDefDT.setPermScopeId(roleDef.getScopeId());
			roleDefDT.setAdmin(roleDef.getAdmin());
			lstRoleDF.add(roleDefDT);
		}

		apiResponse.getData().getValues().add(empDetail);
		apiResponse.getData().setType("employee-detail");
		return apiResponse;
	}

	/**
	 * Verify user details.
	 *
	 * @param empCode
	 *            the emp code
	 * @param identity
	 *            the identity
	 * @param ipaddress
	 *            the ipaddress
	 * @param ipAddress
	 *            the ip address
	 * @return the api response
	 */
	// Validate the ec no and then civil id number and the generate OTP and send otp
	public ApiResponse verifyUserDetails(String empCode, String identity, String ipaddress, String ipAddress) {
		ApiResponse apiResponse = null;
		try {
			if (empCode != null && identity != null) {
				Employee emp = validateEmployeeData(empCode, identity, ipAddress);
				if (emp != null) {
					apiResponse = sendOtp(emp);
				} else {
					throw new AuthServiceException(RbaacServiceError.INVALID_USER_DETAILS,
							"Employee Details not available");
				}
			} else {
				throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_DATA,
						"Employee Number and Civil Id Manadatory");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "verifyUserDetails fail ");
		}

		return apiResponse;
	}

	/**
	 * Verify user OTP details.
	 *
	 * @param empCode
	 *            the emp code
	 * @param identity
	 *            the identity
	 * @param mOtp
	 *            the m otp
	 * @param ipAddress
	 *            the ip address
	 * @return the api response
	 */
	// Validate the ec no and then civil id number and the generate OTP and send otp
	public ApiResponse verifyUserOTPDetails(String empCode, String identity, String mOtp, String ipAddress) {
		ApiResponse apiResponse = null;
		try {
			if (empCode != null && identity != null) {
				Employee emp = validateEmployeeData(empCode, identity, ipAddress);
				if (emp != null) {
					apiResponse = validateOtp(emp, mOtp);
				} else {
					throw new AuthServiceException(RbaacServiceError.INVALID_USER_DETAILS,
							"Employee Details not available");
				}
			} else {
				throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_DATA,
						"Employee Number and Civil Id Manadatory");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new AuthServiceException(e.getMessage(), "verifyUserOTPDetails fail ");
		}

		return apiResponse;
	}

}
