package com.amx.jax.repository.task;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.task.JaxNotificationTask;

public interface JaxNotificationTaskRepo extends CrudRepository<JaxNotificationTask, Serializable> {

}
