package com.amx.jax.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.employee.AmgEmployee;
import com.amx.jax.dbmodel.employee.UserSession;
import com.amx.jax.repository.employee.AmgEmployeeRepository;
import com.amx.jax.repository.employee.UserSessionRepository;

@Component
public class JaxEmployeeDao {

	@Autowired
	AmgEmployeeRepository amgEmployeeRepository;
	@Autowired
	UserSessionRepository userSessionRepository;

	/**
	 * @param civilId
	 * @return AmgEmployee object which belong to table which is superset of all
	 *         employee
	 * 
	 */
	public AmgEmployee getAmgEmployee(String civilId) {
		return amgEmployeeRepository.findByCivilIdAndEmployeeInd(civilId, "AMIEC");
	}

	public boolean isAmgEmployee(String civilId) {
		if (getAmgEmployee(civilId) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * gives usersession object for branch user which is/was logged in latest
	 * 
	 * @param ipAddress
	 * @return usersession
	 * 
	 */
	public UserSession findUserSessionByIp(String ipAddress) {
		return userSessionRepository.findFirstByIpAddress(ipAddress, new Sort(Direction.DESC, "ipAddress"));
	}
}
