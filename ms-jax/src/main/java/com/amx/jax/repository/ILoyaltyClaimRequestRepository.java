package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.LoyaltyClaimRequest;

public interface ILoyaltyClaimRequestRepository extends CrudRepository<LoyaltyClaimRequest, Serializable>{

}
