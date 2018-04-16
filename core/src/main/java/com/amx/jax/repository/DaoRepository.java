package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface DaoRepository<T, K extends Serializable> extends JpaRepository<T, K>, QueryDslPredicateExecutor<T> {
}