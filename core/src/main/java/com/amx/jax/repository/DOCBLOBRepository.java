package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.DocBlobUpload;

public interface DOCBLOBRepository extends JpaRepository<DocBlobUpload, Serializable>{

}
