/**
 * 
 */
package com.amx.jax.repository.auth;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.auth.AuthFailureLog;

/**
 * @author prashant
 *
 */
public interface AuthFailureLogRepository extends CrudRepository<AuthFailureLog, Serializable> {

	@Query("select count(af) from AuthFailureLog af where af.ipAddress=?1 and af.eventDate >= ?2")
	Long getFailedRecordCountForIp(String ipAddress, Date date);
}
