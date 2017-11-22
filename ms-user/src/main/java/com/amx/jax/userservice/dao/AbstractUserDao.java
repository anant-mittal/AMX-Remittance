package com.amx.jax.userservice.dao;

import org.springframework.stereotype.Component;

import com.amx.amxlib.model.AbstractUserModel;
import com.amx.jax.dbmodel.Customer;

@Component
public abstract class AbstractUserDao {

	public abstract Customer saveOrUpdateUser(AbstractUserModel userModel);

	public abstract Customer getUser(Long userId);
}
