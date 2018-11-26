package com.amx.jax.rbaac.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.rbaac.dbmodel.AccessType;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.FunctionalityTypeMaster;
import com.amx.jax.rbaac.dbmodel.ModuleMaster;
import com.amx.jax.rbaac.dbmodel.PermScope;
import com.amx.jax.rbaac.dbmodel.Permission;
import com.amx.jax.rbaac.dbmodel.PermissionMaster;
import com.amx.jax.rbaac.dbmodel.PermissionScopeMaster;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.RoleMaster;
import com.amx.jax.rbaac.dbmodel.UserRoleMapping;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;
import com.amx.jax.rbaac.repository.IAccessTypeRepository;
import com.amx.jax.rbaac.repository.IEmployeeRepository;
import com.amx.jax.rbaac.repository.IPermissionRepository;
import com.amx.jax.rbaac.repository.IRoleRepository;
import com.amx.jax.rbaac.repository.IScopeRepository;
import com.amx.jax.rbaac.repository.IUserRoleMappingRepository;
import com.amx.jax.rbaac.repository.OldIFunctionalityTypeRepository;
import com.amx.jax.rbaac.repository.OldIModuleRepository;
import com.amx.jax.rbaac.repository.OldIPermissionRepository;
import com.amx.jax.rbaac.repository.OldIPermissionScopeRepository;
import com.amx.jax.rbaac.repository.OldIRoleDefinitionRepository;
import com.amx.jax.rbaac.repository.OldIRoleMasterRepository;
import com.amx.jax.rbaac.repository.OldIUserMasterRepository;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RbaacDao {

	@Autowired
	IEmployeeRepository employeeRepository;

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

	@Autowired
	IAccessTypeRepository accessTypeRepository;

	@Autowired
	IScopeRepository scopeRepository;

	@Autowired
	IPermissionRepository permissionRepository;

	@Autowired
	IRoleRepository roleRepository;

	@Autowired
	IUserRoleMappingRepository userRoleMappingRepository;

	public List<AccessType> getAllAccessTypes() {
		return accessTypeRepository.findAll();
	}

	public List<PermScope> getAllScopes() {
		return scopeRepository.findAll();
	}

	public List<Permission> getAllPermissions() {
		return permissionRepository.findAll();
	}

	public Role getRoleById(BigDecimal id) {
		return roleRepository.findById(id);
	}

	public Role getRoleByRoleName(String roleName) {
		return roleRepository.findByRole(roleName);
	}

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public Role saveRole(Role role) {
		return roleRepository.saveAndFlush(role);
	}

	public UserRoleMapping getUserRoleMappingById(BigDecimal id) {
		return userRoleMappingRepository.findById(id);
	}

	public List<UserRoleMapping> getUserRoleMappingsByIds(List<BigDecimal> ids) {
		return userRoleMappingRepository.findByIdIn(ids);
	}

	public List<UserRoleMapping> getUserRoleMappingsByEmployeeIds(List<BigDecimal> employeeIdList) {
		return userRoleMappingRepository.findByEmployeeIdIn(employeeIdList);
	}

	public UserRoleMapping getUserRoleMappingByEmployeeId(BigDecimal employeeId) {
		return userRoleMappingRepository.findByEmployeeId(employeeId);
	}

	/**
	 * Saves Single Entity of User Role Mapping
	 * 
	 * @param urm
	 * @return
	 */
	@Modifying
	@Transactional
	public UserRoleMapping saveUserRoleMapping(UserRoleMapping urm) {
		return userRoleMappingRepository.save(urm);
	}

	/**
	 * Saves Batch Entities of User Role Mapping
	 * 
	 * @param urMappings
	 * @return
	 */
	@Modifying
	@Transactional
	public List<UserRoleMapping> saveUserRoleMappings(List<UserRoleMapping> urMappings) {
		return userRoleMappingRepository.save(urMappings);
	}

	/**
	 * Deletes given user role mappings in the list
	 * 
	 * @param urMappings
	 */
	@Modifying
	@Transactional
	public void deleteUserRoleMappings(List<UserRoleMapping> urMappings) {
		userRoleMappingRepository.deleteInBatch(urMappings);
	}

	public Employee fetchEmpDetails(String empcode, String identity, String ipAddress) {
		List<Employee> empList = employeeRepository.findByEmployeeNumberAndCivilIdAndIpAddress(empcode, identity,
				ipAddress);
		if (null != empList && !empList.isEmpty()) {
			return empList.get(0);
		}

		return null;
	}
	
	public Employee fetchEmpDetails(String identity) {
		List<Employee> empList = employeeRepository.findByCivilId(identity);
		if (null != empList && !empList.isEmpty()) {
			return empList.get(0);
		}

		return null;
	}

	public List<Employee> getEmployees(String empcode, String identity, String ipAddress) {
		return employeeRepository.findByEmployeeNumberAndCivilIdAndIpAddress(empcode, identity, ipAddress);
	}

	public List<Employee> getEmployeesByDeviceId(String empcode, String identity, String deviceId) {
		return employeeRepository.findByEmployeeNumberAndCivilIdAndDeviceId(empcode, identity, deviceId);
	}

	public List<Employee> getEmployeesByCountryBranchId(BigDecimal countryBranchId) {
		return employeeRepository.findByFsCountryBranch(countryBranchId);
	}

	public Employee getEmployeeByEmployeeId(BigDecimal empId) {
		return employeeRepository.findByEmployeeId(empId);
	}

	public Employee fetchEmpDetails(String user, String pass) {
		return employeeRepository.findByUserNameAndPassword(user, pass);
	}

	public Employee fetchEmpDetailsByECNO(String empNo) {
		return employeeRepository.findByEmployeeNumber(empNo);
	}

	/**
	 * Saves Employee Data and Flushes for immediate Push
	 * 
	 * @param employee
	 * @return
	 */
	@Modifying
	@Transactional
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.saveAndFlush(employee);
	}

	@Modifying
	@Transactional
	public List<Employee> saveEmployees(Iterable<Employee> employeeList) {
		return employeeRepository.save(employeeList);
	}

	/** UnUsed APIs **/

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

	public UserRoleMapping getUserRoleMappingsByEmployeeId(BigDecimal employeeId) {
		return userRoleMappingRepository.findByEmployeeId(employeeId);
	}
}
