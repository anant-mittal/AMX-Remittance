package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;

@Transactional
public interface ChannelDiscountRepository extends CrudRepository<ChannelDiscount, BigDecimal> {

	ChannelDiscount findByChannel(Channel channel);

}
