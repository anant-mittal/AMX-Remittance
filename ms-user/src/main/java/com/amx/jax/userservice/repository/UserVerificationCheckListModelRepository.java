package com.amx.jax.userservice.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.UserVerificationCheckListModel;

@Transactional
public interface UserVerificationCheckListModelRepository extends CrudRepository<UserVerificationCheckListModel, String> {

}
