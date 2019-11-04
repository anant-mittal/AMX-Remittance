package com.amx.jax.repository;
/**
 * @author rabil 
 * @date 10/29/2019
 */
import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RatePlaceOrder;

public interface IRatePlaceOrderRepository  extends CrudRepository<RatePlaceOrder, Serializable>{

}
