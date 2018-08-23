package com.amx.jax.rbaac.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.rbaac.dbmodel.AccessType;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.FunctionalityTypeMaster;
import com.amx.jax.rbaac.dbmodel.ModuleMaster;
import com.amx.jax.rbaac.dbmodel.PermScope;
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.jax.rbaac.dbmodel.PermissionMaster;
import com.amx.jax.rbaac.dbmodel.PermissionScopeMaster;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.RoleMaster;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;
import com.amx.jax.rbaac.repository.IAccessTypeRepository;
import com.amx.jax.rbaac.repository.ILoginRepository;
import com.amx.jax.rbaac.repository.IPermissionRepository;
import com.amx.jax.rbaac.repository.IScopeRepository;
import com.amx.jax.rbaac.repository.OldIFunctionalityTypeRepository;
import com.amx.jax.rbaac.repository.OldIModuleRepository;
import com.amx.jax.rbaac.repository.OldIPermissionRepository;
import com.amx.jax.rbaac.repository.OldIPermissionScopeRepository;
import com.amx.jax.rbaac.repository.OldIRoleDefinitionRepository;
import com.amx.jax.rbaac.repository.OldIRoleMasterRepository;
import com.amx.jax.rbaac.repository.OldIUserMasterRepository;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class LoginDao {

	@Autowired
	ILoginRepository loginRepository;

	@Autowired
	OldIRoleDefinitionRepository roleDefinitionRepositoryOld;

	@Autowired
	OldIModuleRepository moduleRepositoryOld;

	@Autowired
	OldIFunctionalityTypeRepository functionalityTypeRepositoryOld;

	@Autowired
	OldIPermissionScopeRepository permissionScopeRepositoryOld;

	@Autowired
	OldIUserMasterRepository userMasterRepositoryOld;

	@Autowired
	OldIRoleMasterRepository roleMasterRepositoryOld;

	@Autowired
	OldIPermissionRepository permissionRepositoryOld;

	/******* TO BE Removed From Here ********/

	@Autowired
	IAccessTypeRepository accessTypeRepository;

	@Autowired
	IScopeRepository scopeRepository;

	@Autowired
	IPermissionRepository permissionRepository;

	public List<AccessType> getAllAccessTypes() {
		// return accessTypeRepository.findByFlags(new BigDecimal(0));
		return accessTypeRepository.findAll();
	}

	public List<PermScope> getAllScopes() {
		return scopeRepository.findAll();
	}

	public List<Permission> getAllPermissions() {
		return permissionRepository.findAll();
	}

	/******* TO BE Removed From Here End ********/

	public Employee validateEmpDetails(String empcode, String identity, String ipAddress) {
		List<Employee> empList = loginRepository.findByEmployeeNumberAndCivilIdAndIpAddress(empcode, identity,
				ipAddress);
		if (null != empList && !empList.isEmpty()) {
			return empList.get(0);
		}

		return null;
	}

	public List<Employee> getEmployees(String empcode, String identity, String ipAddress) {
		return loginRepository.findByEmployeeNumberAndCivilIdAndIpAddress(empcode, identity, ipAddress);
	}

	public List<Employee> getEmployeesByDeviceId(String empcode, String identity, String deviceId) {
		return loginRepository.findByEmployeeNumberAndCivilIdAndDeviceId(empcode, identity, deviceId);
	}

	public Employee fetchEmpDetails(String user, String pass) {
		return loginRepository.findByUserNameAndPassword(user, pass);
	}

	public Employee fetchEmpDetailsByECNO(String empNo) {
		return loginRepository.findByEmployeeNumber(empNo);
	}

	public Employee fetchEmpByEmpId(BigDecimal empId) {
		return loginRepository.findByEmployeeId(empId);
	}

	public Employee saveEmployee(Employee employee) {
		return loginRepository.save(employee);
	}

	public List<RoleDefinition> fetchEmpRoleMenu(BigDecimal roleId) {
		return roleDefinitionRepositoryOld.findByRoleId(roleId);
	}

	public List<ModuleMaster> fetchModule() {
		return moduleRepositoryOld.findAll();
	}

	public ModuleMaster fetchModuleId(String moduleName) {
		return moduleRepositoryOld.findByModuleEnum(moduleName);
	}

	public List<FunctionalityTypeMaster> fetchFunctionalityTypeMaster() {
		return functionalityTypeRepositoryOld.findAll();
	}

	public FunctionalityTypeMaster fetchFunctionalityTypeMasterId(String functionalityTypeDescription) {
		return functionalityTypeRepositoryOld.findByFunctionalityTypeEnum(functionalityTypeDescription);
	}

	public List<PermissionScopeMaster> fetchPermissionScopeMaster() {
		return permissionScopeRepositoryOld.findAll();
	}

	public PermissionScopeMaster fetchPermissionScopeMasterId(String permScope) {
		return permissionScopeRepositoryOld.findByScopeEnum(permScope);
	}

	public void saveModuleData(List<ModuleMaster> moduleMaster) {
		moduleRepositoryOld.save(moduleMaster);
	}

	public void saveFunctionalityTypeMaster(List<FunctionalityTypeMaster> functionality) {
		functionalityTypeRepositoryOld.save(functionality);
	}

	public void savePermissionScopeMaster(List<PermissionScopeMaster> permissionScopeMaster) {
		permissionScopeRepositoryOld.save(permissionScopeMaster);
	}

	public List<RoleMaster> fetchRoleMaster() {
		return roleMasterRepositoryOld.findAll();
	}

	public void saveRoleMaster(List<RoleMaster> roleMaster) {
		roleMasterRepositoryOld.save(roleMaster);
	}

	public UserRoleMaster fetchUserMasterDetails(BigDecimal empId) {
		return userMasterRepositoryOld.findByEmployeeId(empId);
	}

	public void savePermissionMaster(List<PermissionMaster> permissionMasters) {
		permissionRepositoryOld.save(permissionMasters);
	}

	public List<PermissionMaster> fetchPermissionMaster() {
		return permissionRepositoryOld.findAll();
	}

	public PermissionMaster fetchPermissionMasterId(BigDecimal moduleId, BigDecimal functionalityTypeId,
			String functionality) {
		return permissionRepositoryOld.getPermissionMaster(moduleId, functionalityTypeId, functionality);
	}

	public void saveRoleToUser(UserRoleMaster userMaster) {
		userMasterRepositoryOld.save(userMaster);
	}

	public RoleDefinition fetchRoleDefinitionByRolePerm(BigDecimal roleDefId) {
		return roleDefinitionRepositoryOld.findByRoleDefId(roleDefId);
	}

	public RoleDefinition fetchRoleDefinitionByRolePermScope(BigDecimal roleId, BigDecimal permissionId,
			BigDecimal scopeId) {
		return roleDefinitionRepositoryOld.findByRoleIdAndPermissionIdAndScopeId(roleId, permissionId, scopeId);
	}

	public void saveRoleDefintionDetails(RoleDefinition roleDefinition) {
		roleDefinitionRepositoryOld.save(roleDefinition);
	}
}
