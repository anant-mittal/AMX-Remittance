package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.fx.StatusMaster;

public interface IViewStatus extends CrudRepository<StatusMaster, Serializable>{

	@Query("select s from StatusMaster s where s.isActive ='Y'")
	public List<StatusMaster> getStatusList();
	
	@Query("select s from StatusMaster s where s.statusDescription =?1")
	public List<StatusMaster> getOrderStatusList(String statusDescription);
	
}
