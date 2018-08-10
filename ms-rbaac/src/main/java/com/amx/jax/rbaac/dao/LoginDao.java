package com.amx.jax.rbaac.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.FunctionalityTypeMaster;
import com.amx.jax.rbaac.dbmodel.ModuleMaster;
import com.amx.jax.rbaac.dbmodel.PermissionMaster;
import com.amx.jax.rbaac.dbmodel.PermissionScopeMaster;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.RoleMaster;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;
import com.amx.jax.rbaac.repository.IFunctionalityTypeRepository;
import com.amx.jax.rbaac.repository.ILoginRepository;
import com.amx.jax.rbaac.repository.IModuleRepository;
import com.amx.jax.rbaac.repository.IPermissionRepository;
import com.amx.jax.rbaac.repository.IPermissionScopeRepository;
import com.amx.jax.rbaac.repository.IRoleDefinitionRepository;
import com.amx.jax.rbaac.repository.IRoleMasterRepository;
import com.amx.jax.rbaac.repository.IUserMasterRepository;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class LoginDao {

	@Autowired
	ILoginRepository loginRepository;

	@Autowired
	IRoleDefinitionRepository roleDefinitionRepository;

	@Autowired
	IModuleRepository moduleRepository;

	@Autowired
	IFunctionalityTypeRepository functionalityTypeRepository;

	@Autowired
	IPermissionScopeRepository permissionScopeRepository;

	@Autowired
	IUserMasterRepository userMasterRepository;

	@Autowired
	IRoleMasterRepository roleMasterRepository;

	@Autowired
	IPermissionRepository permissionRepository;

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
		return roleDefinitionRepository.findByRoleId(roleId);
	}

	public List<ModuleMaster> fetchModule() {
		return moduleRepository.findAll();
	}

	public ModuleMaster fetchModuleId(String moduleName) {
		return moduleRepository.findByModuleEnum(moduleName);
	}

	public List<FunctionalityTypeMaster> fetchFunctionalityTypeMaster() {
		return functionalityTypeRepository.findAll();
	}

	public FunctionalityTypeMaster fetchFunctionalityTypeMasterId(String functionalityTypeDescription) {
		return functionalityTypeRepository.findByFunctionalityTypeEnum(functionalityTypeDescription);
	}

	public List<PermissionScopeMaster> fetchPermissionScopeMaster() {
		return permissionScopeRepository.findAll();
	}

	public PermissionScopeMaster fetchPermissionScopeMasterId(String permScope) {
		return permissionScopeRepository.findByScopeEnum(permScope);
	}

	public void saveModuleData(List<ModuleMaster> moduleMaster) {
		moduleRepository.save(moduleMaster);
	}

	public void saveFunctionalityTypeMaster(List<FunctionalityTypeMaster> functionality) {
		functionalityTypeRepository.save(functionality);
	}

	public void savePermissionScopeMaster(List<PermissionScopeMaster> permissionScopeMaster) {
		permissionScopeRepository.save(permissionScopeMaster);
	}

	public List<RoleMaster> fetchRoleMaster() {
		return roleMasterRepository.findAll();
	}

	public void saveRoleMaster(List<RoleMaster> roleMaster) {
		roleMasterRepository.save(roleMaster);
	}

	public UserRoleMaster fetchUserMasterDetails(BigDecimal empId) {
		return userMasterRepository.findByEmployeeId(empId);
	}

	public void savePermissionMaster(List<PermissionMaster> permissionMasters) {
		permissionRepository.save(permissionMasters);
	}

	public List<PermissionMaster> fetchPermissionMaster() {
		return permissionRepository.findAll();
	}

	public PermissionMaster fetchPermissionMasterId(BigDecimal moduleId, BigDecimal functionalityTypeId,
			String functionality) {
		return permissionRepository.getPermissionMaster(moduleId, functionalityTypeId, functionality);
	}

	public void saveRoleToUser(UserRoleMaster userMaster) {
		userMasterRepository.save(userMaster);
	}

	public RoleDefinition fetchRoleDefinitionByRolePerm(BigDecimal roleDefId) {
		return roleDefinitionRepository.findByRoleDefId(roleDefId);
	}

	public RoleDefinition fetchRoleDefinitionByRolePermScope(BigDecimal roleId, BigDecimal permissionId,
			BigDecimal scopeId) {
		return roleDefinitionRepository.findByRoleIdAndPermissionIdAndScopeId(roleId, permissionId, scopeId);
	}

	public void saveRoleDefintionDetails(RoleDefinition roleDefinition) {
		roleDefinitionRepository.save(roleDefinition);
	}
}
