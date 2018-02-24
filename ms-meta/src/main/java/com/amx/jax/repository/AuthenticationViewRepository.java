package com.amx.jax.repository;

import java.io.Serializable;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.AuthenticationView;

@Transactional
public interface AuthenticationViewRepository extends JpaRepository<AuthenticationView, Serializable> {

}
