package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.FileUploadTempModel;

public interface ServiceProviderTempUploadRepository extends CrudRepository<FileUploadTempModel, Serializable>{
		
}
