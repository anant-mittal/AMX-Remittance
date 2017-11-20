package com.amx.jax.userservice.dao;

import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.userservice.model.AbstractUserModel;

@Component
public abstract class AbstractUserDao {

	public abstract Customer saveOrUpdateUser(AbstractUserModel userModel);

	public abstract Customer getUser(Long userId);
}
