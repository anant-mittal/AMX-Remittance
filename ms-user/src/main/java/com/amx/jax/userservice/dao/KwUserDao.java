package com.amx.jax.userservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.exception.UserNotFoundException;
import com.amx.jax.userservice.model.AbstractUserModel;
import com.amx.jax.userservice.model.UserModel;
import com.amx.jax.userservice.repository.CustomerRepository;

@Component
public class KwUserDao extends AbstractUserDao {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private MetaData metaData;

	@Override
	public Customer saveOrUpdateUser(AbstractUserModel userModel) {
		UserModel kwUserModel = (UserModel) userModel;
		Customer cust = new Customer();
		if (userModel.getId() != null) {
			cust = getUser(userModel.getId());
			if (cust == null) {
				throw new UserNotFoundException(
						"No User found with id = " + userModel.getId());
			}
		}
//		cust.setFirstName(userModel.getFirstName());
//		cust.setLastName(userModel.getLastName());
//		cust.setMiddleName(userModel.getMiddleName());
//		cust.setTitle(userModel.getTitle());
		repository.save(cust);
		return cust;
	}

	@Override
	public Customer getUser(Long userId) {
		Customer cust = repository.findOne(userId);
		if (cust == null) {
			throw new UserNotFoundException("No User found with id" + userId);
		}
		return cust;
	}

}
