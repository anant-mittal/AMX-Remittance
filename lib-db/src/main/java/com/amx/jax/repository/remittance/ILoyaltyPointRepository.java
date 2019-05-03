package com.amx.jax.repository.remittance;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.LoyaltyPointsModel;

public interface ILoyaltyPointRepository extends CrudRepository<LoyaltyPointsModel, Serializable>{

}
