/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.payment.model.db.Demo;

/**
 * @author Viki Sangani
 * 15-Dec-2017
 * Demo.java
 */
@Transactional
public interface DemoRepository extends CrudRepository<Demo, String> {

	@Query("select c from Demo c where id=?1")
	public List<Demo> getDemoUser(String id);
	
	@Query("select c from Demo c where id=?1")
	public List<Demo> getKnetConfig(String id);
}
