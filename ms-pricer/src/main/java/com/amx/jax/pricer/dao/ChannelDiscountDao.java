package com.amx.jax.pricer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dbmodel.ChannelDiscount;
import com.amx.jax.pricer.repository.ChannelDiscountRepository;

@Component
public class ChannelDiscountDao {

	@Autowired
	ChannelDiscountRepository channelDiscountRepository;

	public ChannelDiscount getDiscountByChannel(Channel channel) {
		return channelDiscountRepository.findByChannel(channel);
	}

	public ChannelDiscount saveDiscountForChannel(ChannelDiscount channelDiscount) {
		return channelDiscountRepository.save(channelDiscount);
	}

}
