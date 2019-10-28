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
import com.amx.jax.rbaac.dbmodel.CountryBranch;
import com.amx.jax.rbaac.dbmodel.FSEmployee;
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
import com.amx.jax.rbaac.dbmodel.ViewExEmpBranchSysDetails;
import com.amx.jax.rbaac.repository.AccessTypeRepository;
import com.amx.jax.rbaac.repository.EmployeeRepository;
import com.amx.jax.rbaac.repository.OldIFunctionalityTypeRepository;
import com.amx.jax.rbaac.repository.OldIModuleRepository;
import com.amx.jax.rbaac.repository.OldIPermissionRepository;
import com.amx.jax.rbaac.repository.OldIPermissionScopeRepository;
import com.amx.jax.rbaac.repository.OldIRoleDefinitionRepository;
import com.amx.jax.rbaac.repository.OldIRoleMasterRepository;
import com.amx.jax.rbaac.repository.OldIUserMasterRepository;
import com.amx.jax.rbaac.repository.PermissionRepository;
import com.amx.jax.rbaac.repository.RoleRepository;
import com.amx.jax.rbaac.repository.ScopeRepository;
import com.amx.jax.rbaac.repository.UserRoleMappingRepository;
import com.amx.jax.rbaac.repository.VwEmpBranchSysDetailsRepository;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RbaacDao {

	@Autowired
	EmployeeRepository employeeRepository;

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
	AccessTypeRepository accessTypeRepository;

	@Autowired
	ScopeRepository scopeRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRoleMappingRepository userRoleMappingRepository;

	@Autowired
	VwEmpBranchSysDetailsRepository vwEmpBranchSysDetailsRepository;

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

	public List<ViewExEmpBranchSysDetails> getEmpBranchSysDetailsByEmpIdAndIpAddr(BigDecimal empId, String ipAddress) {
		return vwEmpBranchSysDetailsRepository.findByEmployeeIdAndIpAddress(empId, ipAddress);
	}

	public List<ViewExEmpBranchSysDetails> getEmpBranchSysByEmpIdAndBranchSysInvIdAndBranchId(BigDecimal empId,
			BigDecimal sysInventoryId, BigDecimal branchId) {
		return vwEmpBranchSysDetailsRepository.findByEmployeeIdAndBranchSysInventoryIdAndBranchId(empId, sysInventoryId,
				branchId);
	}

	public FSEmployee fetchEmpDetails(String empcode, String identity, String ipAddress) {
		List<FSEmployee> empList = employeeRepository.findByEmployeeNumberAndCivilId(empcode, identity);
		if (null != empList && !empList.isEmpty()) {
			return empList.get(0);
		}

		return null;
	}

	public FSEmployee fetchEmpDetails(String identity) {
		List<FSEmployee> empList = employeeRepository.findByCivilId(identity);
		if (null != empList && !empList.isEmpty()) {
			return empList.get(0);
		}

		return null;
	}

	public List<FSEmployee> getEmployees(String empcode, String identity) {
		return employeeRepository.findByEmployeeNumberAndCivilId(empcode, identity);
	}

	public List<FSEmployee> getEmployeesByCivilId(String identity) {
		return employeeRepository.findByCivilId(identity);
	}

	public List<FSEmployee> getEmployeesByCountryBranchId(BigDecimal countryBranchId) {
		CountryBranch countryBranch = new CountryBranch();
		countryBranch.setCountryBranchId(countryBranchId);
		return employeeRepository.findByCountryBranch(countryBranch);
	}

	public FSEmployee getEmployeeByEmployeeId(BigDecimal empId) {
		return employeeRepository.findByEmployeeId(empId);
	}

	public FSEmployee fetchEmpDetails(String user, String pass) {
		return employeeRepository.findByUserNameAndPassword(user, pass);
	}

	public FSEmployee fetchEmpDetailsByECNO(String empNo) {
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
	public FSEmployee saveEmployee(FSEmployee employee) {
		return employeeRepository.saveAndFlush(employee);
	}

	@Modifying
	@Transactional
	public List<FSEmployee> saveEmployees(Iterable<FSEmployee> employeeList) {
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
