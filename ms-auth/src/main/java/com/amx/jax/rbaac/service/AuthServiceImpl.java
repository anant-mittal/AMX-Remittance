package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.rbaac.AuthService;
import com.amx.jax.rbaac.dao.LoginDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.FunctionalityTypeMaster;
import com.amx.jax.rbaac.dbmodel.ModuleMaster;
import com.amx.jax.rbaac.dbmodel.PermissionMaster;
import com.amx.jax.rbaac.dbmodel.PermissionScopeMaster;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.RoleMaster;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;
import com.amx.jax.rbaac.dto.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.UserDetailsDTO;
import com.amx.jax.rbaac.error.AuthServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.manager.AuthLoginManager;
import com.amx.jax.rbaac.manager.AuthLoginOTPManager;
import com.amx.jax.rbaac.models.Module;
import com.amx.jax.rbaac.models.PermScope;
import com.amx.jax.rbaac.models.PermType;
import com.amx.jax.rbaac.models.Permission;
import com.amx.jax.rbaac.trnx.AuthLoginTrnxModel;
import com.amx.jax.rbaac.trnx.UserOtpData;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.JsonUtil;

@Component
public class AuthServiceImpl implements AuthService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceImpl.class);

	@Autowired
	LoginDao loginDao;

	@Autowired
	AuthLoginManager authLoginManager;

	@Autowired
	AuthLoginOTPManager authLoginOTPManager;

	@Autowired
	JaxUtil jaxUtil;

	@Autowired
	UserOtpData userOtpData;

	public Employee validateEmployeeData(String empcode, String identity, String ipAddress) {
		Employee emp = loginDao.validateEmpDetails(empcode, identity, ipAddress);
		return emp;
	}

	public Employee fetchEmployeeDetails(String user, String pass) {
		Employee emp = loginDao.fetchEmpDetails(user, pass);
		return emp;
	}

	public Employee fetchEmployeeDetailsByECNO(String empNo) {
		Employee emp = loginDao.fetchEmpDetailsByECNO(empNo);
		return emp;
	}

	public List<RoleDefinition> fetchEmployeeRoleDef(BigDecimal role) {
		List<RoleDefinition> roleDef = loginDao.fetchEmpRoleMenu(role);
		return roleDef;
	}

	// store enums
	public AmxApiResponse<BoolRespModel, Object> saveEnums() {
		boolean savesStatus = Boolean.FALSE;
		try {
			// fetch module enums
			boolean modulestatus = saveModule();
			LOGGER.info("Module : " + modulestatus);
			boolean funcTypestatus = saveFunctionalityTypeMaster();
			LOGGER.info("FunctionalityType : " + funcTypestatus);
			boolean permScope = savePermissionScopeMaster();
			LOGGER.info("PermScope : " + permScope);
			boolean perm = savePermission();
			LOGGER.info("perm : " + perm);

			if (modulestatus || funcTypestatus || permScope || perm) {
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("saveEnums fail ", e.getMessage());
		}
		return AmxApiResponse.build(new BoolRespModel(savesStatus));
	}

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
			List<ModuleMaster> moduleDB = loginDao.fetchModule();

			if (moduleDB != null && moduleDB.size() != 0) {
				for (ModuleMaster moduleMaster : moduleDB) {
					moduleDBData.add(moduleMaster.getModuleEnum());
				}
			}

			moduleEnumData.removeAll(moduleDBData);

			LOGGER.info("not available in db : " + moduleEnumData);

			for (String moduleMaster : moduleEnumData) {
				ModuleMaster module = new ModuleMaster();

				module.setModuleEnum(moduleMaster);
				module.setCreatedDate(new Date());
				module.setIsactive("Y");

				moduleStore.add(module);
			}

			if (moduleStore != null && moduleStore.size() != 0) {
				loginDao.saveModuleData(moduleStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("saveModule fail ", e.getMessage());
		}

		return savesStatus;
	}

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
			List<FunctionalityTypeMaster> permTypeDB = loginDao.fetchFunctionalityTypeMaster();

			if (permTypeDB != null && permTypeDB.size() != 0) {
				for (FunctionalityTypeMaster functionalityTypeMaster : permTypeDB) {
					funTypeDBData.add(functionalityTypeMaster.getFunctionalityTypeEnum());
				}
			}

			funTypeEnumData.removeAll(funTypeDBData);

			LOGGER.info("not available in db : " + funTypeEnumData);

			for (String funTypeMaster : funTypeEnumData) {
				FunctionalityTypeMaster funcTypeM = new FunctionalityTypeMaster();

				funcTypeM.setFunctionalityTypeEnum(funTypeMaster);
				funcTypeM.setCreatedDate(new Date());
				funcTypeM.setIsactive("Y");

				funTypeStore.add(funcTypeM);
			}

			if (funTypeStore != null && funTypeStore.size() != 0) {
				loginDao.saveFunctionalityTypeMaster(funTypeStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("saveFunctionalityTypeMaster fail ", e.getMessage());
		}

		return savesStatus;
	}

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
			List<PermissionScopeMaster> permScopeDB = loginDao.fetchPermissionScopeMaster();

			if (permScopeDB != null && permScopeDB.size() != 0) {
				for (PermissionScopeMaster permissionScopeMaster : permScopeDB) {
					permScopeDBData.add(permissionScopeMaster.getScopeEnum());
				}
			}

			permScopeEnumData.removeAll(permScopeDBData);

			LOGGER.info("not available in db : " + permScopeEnumData);

			for (String permScopeMaster : permScopeEnumData) {
				PermissionScopeMaster permScopeM = new PermissionScopeMaster();

				permScopeM.setScopeEnum(permScopeMaster);
				permScopeM.setCreatedDate(new Date());
				permScopeM.setIsactive("Y");

				permScopeStore.add(permScopeM);
			}

			if (permScopeStore != null && permScopeStore.size() != 0) {
				loginDao.savePermissionScopeMaster(permScopeStore);
				savesStatus = Boolean.TRUE;
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("savePermissionScopeMaster fail ", e.getMessage());
		}

		return savesStatus;
	}

	// to check null
	private String nullCheck(String value) {
		return value == null ? "" : value.trim().concat("|");
	}

	private String nullCheckWO(String value) {
		return value == null ? "" : value.trim();
	}

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
			 * List<PermissionMaster> permDB = loginDao.fetchPermissionMaster();
			 * 
			 * if(permDB != null && permDB.size() != 0){ for (PermissionMaster
			 * permissionMaster : permDB) { if(permissionMaster.getPermissionId() != null){
			 * permDBData.add(permissionMaster.getPermissionId().toString()); } } }
			 * 
			 * permEnumData.removeAll(permDBData);
			 * 
			 * LOGGER.info("not available in db : " + permEnumData);
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
				loginDao.savePermissionMaster(permStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("savePermission fail ", e.getMessage());
		}

		return savesStatus;
	}

	// fetch module id by name
	public BigDecimal fetchModuleId(String module) {
		BigDecimal moduleId = null;
		ModuleMaster moduleMaster = loginDao.fetchModuleId(module);
		if (moduleMaster != null) {
			moduleId = moduleMaster.getModuleId();
		}
		return moduleId;
	}

	// fetch module id by name
	public BigDecimal fetchFunctionalityTypeId(String functionalityType) {
		BigDecimal functionalityTypeId = null;
		FunctionalityTypeMaster functionalityTypeMasterId = loginDao.fetchFunctionalityTypeMasterId(functionalityType);
		if (functionalityTypeMasterId != null) {
			functionalityTypeId = functionalityTypeMasterId.getFunctionalityTypeId();
		}
		return functionalityTypeId;
	}

	// fetch permission master
	public BigDecimal fetchPermissionMasterId(BigDecimal moduleId, BigDecimal functionalityTypeId,
			String functionality) {
		BigDecimal permissionId = null;
		PermissionMaster permissionMaster = loginDao.fetchPermissionMasterId(moduleId, functionalityTypeId,
				functionality);
		if (permissionMaster != null) {
			permissionId = permissionMaster.getPermissionId();
		}
		return permissionId;
	}

	// save role
	public AmxApiResponse<BoolRespModel, Object> saveRoleMaster(String roleTitle) {

		boolean savesStatus = Boolean.FALSE;
		Set<String> roleMasterData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		Set<String> roleMasterDBData = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		List<RoleMaster> roleMasterStore = new ArrayList<RoleMaster>();

		try {

			roleMasterData.add(roleTitle);

			// fetch all the data from module table
			List<RoleMaster> roleMasterDB = loginDao.fetchRoleMaster();

			if (roleMasterDB != null && roleMasterDB.size() != 0) {
				for (RoleMaster roleMaster : roleMasterDB) {
					roleMasterDBData.add(roleMaster.getRoleCode());
				}
			}

			roleMasterData.removeAll(roleMasterDBData);

			LOGGER.info("not available in db : " + roleMasterData);

			for (String roleMaster : roleMasterData) {
				RoleMaster roleM = new RoleMaster();

				// roleM.setRoleCode(roleCode);
				roleM.setCreatedDate(new Date());
				roleM.setIsactive("Y");
				roleM.setRoleDescription(roleTitle);
				roleMasterStore.add(roleM);
			}

			if (roleMasterStore != null && roleMasterStore.size() != 0) {
				loginDao.saveRoleMaster(roleMasterStore);
				savesStatus = Boolean.TRUE;
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("saveRoleMaster fail ", e.getMessage());
		}
		AmxApiResponse<BoolRespModel, Object> response = new AmxApiResponse<BoolRespModel, Object>();
		response.addResult(new BoolRespModel(savesStatus));
		return response;
	}

	public AmxApiResponse<UserDetailsDTO, Object> fetchUserMasterDetails(BigDecimal userId) {
		UserRoleMaster user = loginDao.fetchUserMasterDetails(userId);

		// ????
		UserDetailsDTO userDetail = new UserDetailsDTO();
		jaxUtil.convert(user, userDetail);

		return AmxApiResponse.build(userDetail);
	}

	public AmxApiResponse<BoolRespModel, Object> saveAssignRoleToUser(BigDecimal roleId, BigDecimal userId) {

		boolean savesStatus = Boolean.FALSE;

		try {
			UserRoleMaster user = loginDao.fetchUserMasterDetails(userId);

			UserRoleMaster userM = new UserRoleMaster();

			if (user != null) {
				userM.setCreatedDate(new Date());
				userM.setIsactive("Y");
				userM.setUserRoleId(user.getUserRoleId());
				userM.setEmployeeId(user.getEmployeeId());
				if (user.getRoleId() != null && user.getRoleId().compareTo(roleId) == 0) {
					// error already exist
					throw new AuthServiceException("saveAssignRoleToUser fail ", AuthServiceError.ALREADY_EXIST);
				} else {
					userM.setRoleId(roleId);
				}
			} else {
				userM.setEmployeeId(userId);
				userM.setRoleId(roleId);
			}

			loginDao.saveRoleToUser(userM);
			savesStatus = Boolean.TRUE;
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("saveAssignRoleToUser fail ", e.getMessage());
		}

		return AmxApiResponse.build(new BoolRespModel(savesStatus));
	}

	// fetch permission scope Id by permission description
	public BigDecimal fetchPermissionScopeId(String permScope) {
		BigDecimal scopeId = null;
		PermissionScopeMaster permissionScopeMaster = loginDao.fetchPermissionScopeMasterId(permScope);
		if (permissionScopeMaster != null) {
			scopeId = permissionScopeMaster.getScopeId();
		}

		return scopeId;
	}

	// save Permission and scope to a role
	public AmxApiResponse<BoolRespModel, Object> saveAssignPermToRole(BigDecimal roleId, Permission permission,
			PermScope permScope, String admin) {
		boolean savesStatus = Boolean.FALSE;
		try {
			BigDecimal moduleId = fetchModuleId(permission.getModule().name());
			BigDecimal functionalityTypeId = fetchFunctionalityTypeId(permission.getPermType().name());
			String functionality = permission.getPermission();
			BigDecimal permissionId = fetchPermissionMasterId(moduleId, functionalityTypeId, functionality);
			LOGGER.info("permission" + permissionId);
			BigDecimal scopeId = fetchPermissionScopeId(permScope.name());

			RoleDefinition roleDef = loginDao.fetchRoleDefinitionByRolePermScope(roleId, permissionId, scopeId);
			LOGGER.info(JsonUtil.toJson(roleDef));

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

			loginDao.saveRoleDefintionDetails(roleDefinition);
			savesStatus = Boolean.TRUE;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new AuthServiceException("saveAssignPermToRole fail ", e.getMessage());
		}

		return AmxApiResponse.build(new BoolRespModel(savesStatus));
	}

	/**
	 * Sends otp initiating trnx
	 */
	public AmxApiResponse<SendOtpModel, Object> sendOtp(Employee emp) {
		try {
			BeanPropertyBindingResult errors = new BeanPropertyBindingResult(emp, "emp");
			// initiate transaction
			AuthLoginTrnxModel trnxModel = authLoginManager.init(emp);

			/**
			 * Save OTP to cache As Well
			 */

			SendOtpModel output = authLoginOTPManager.generateOtpTokensStaff(emp.getEmployeeNumber());

			userOtpData.fastPut(emp.getEmployeeNumber(), output.getmOtpPrefix() + "-" + output.getmOtp());

			authLoginOTPManager.sendOtpStaff();

			AmxApiResponse<SendOtpModel, Object> amxResp = AmxApiResponse.build(output);

			return amxResp;
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("sendOtp fail ", e.getMessage());
		}
	}

	/**
	 * validates otp
	 */
	public AmxApiResponse<EmployeeDetailsDTO, Object> validateOtp(Employee emp, String mOtp) {
		// AuthLoginTrnxModel authLoginTrnxModel =
		// authLoginOTPManager.validateOtpStaff(emp, mOtp);

		String origOtp = userOtpData.remove(emp.getEmployeeNumber());

		if (null == origOtp || !origOtp.equalsIgnoreCase(mOtp)) {
			throw new AuthServiceException("Invalid OTP", AuthServiceError.INVALID_OTP);
		}

		EmployeeDetailsDTO empDetail = new EmployeeDetailsDTO();

		empDetail.setCivilId(emp.getCivilId());
		empDetail.setCountryId(emp.getCountryId());
		empDetail.setDesignation(emp.getDesignation());
		empDetail.setEmail(emp.getEmail());
		empDetail.setEmployeeId(emp.getEmployeeId());
		empDetail.setEmployeeName(emp.getEmployeeName());
		empDetail.setEmployeeNumber(emp.getEmployeeNumber());
		empDetail.setLocation(emp.getLocation());
		empDetail.setTelephoneNumber(emp.getTelephoneNumber());
		empDetail.setUserName(emp.getUserName());
		empDetail.setRoleId(new BigDecimal("1"));

		/*
		 * List<RoleDefinitionDataTable> lstRoleDF = new
		 * ArrayList<RoleDefinitionDataTable>(); for (RoleDefinition roleDef :
		 * authLoginTrnxModel.getRoleDefinition()) { RoleDefinitionDataTable roleDefDT =
		 * new RoleDefinitionDataTable();
		 * 
		 * roleDefDT.setModuleId(roleDef.getRoleId());
		 * roleDefDT.setPermissionId(roleDef.getPermissionId());
		 * roleDefDT.setPermScopeId(roleDef.getScopeId());
		 * roleDefDT.setAdmin(roleDef.getAdmin()); lstRoleDF.add(roleDefDT); }
		 * empDetail.setRoleDef(lstRoleDF);
		 */

		return AmxApiResponse.build(empDetail);
	}

	// Validate the ec no and then civil id number and the generate OTP and send otp
	public AmxApiResponse<SendOtpModel, Object> verifyUserDetails(String empCode, String identity, String ipAddress) {
		try {
			if (empCode != null && identity != null) {
				Employee emp = validateEmployeeData(empCode, identity, ipAddress);
				if (emp != null) {

					return sendOtp(emp);
				} else {
					throw new AuthServiceException("Employee Details not available",
							AuthServiceError.INVALID_USER_DETAILS);
				}
			} else {
				throw new AuthServiceException("Employee Number and Civil Id Manadatory",
						AuthServiceError.INVALID_OR_MISSING_DATA);
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new AuthServiceException("verifyUserDetails fail ", e.getMessage());
		}
	}

	// Validate the ec no and then civil id number and the generate OTP and send otp
	public AmxApiResponse<EmployeeDetailsDTO, Object> verifyUserOTPDetails(String empCode, String identity, String mOtp,
			String ipAddress) {
		if (empCode != null && identity != null) {
			Employee emp = validateEmployeeData(empCode, identity, ipAddress);
			if (emp != null) {
				return validateOtp(emp, mOtp);
			} else {
				throw new AuthServiceException("Employee Details not available",
						AuthServiceError.INVALID_USER_DETAILS);
			}
		} else {
			throw new AuthServiceException("Employee Number and Civil Id Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}

	}

}
