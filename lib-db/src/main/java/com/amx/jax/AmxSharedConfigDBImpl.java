package com.amx.jax;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxSharedConfig.AmxSharedConfigDB;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.repository.ICommunicationPrefsRepository;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class AmxSharedConfigDBImpl implements AmxSharedConfigDB {

	@Autowired(required=false)
	ICommunicationPrefsRepository communicationPrefsRepository;

	@Override
	public AmxApiResponse<CommunicationPrefs, Object> getCommunicationPrefs() {
		return AmxApiResponse.buildList(new ArrayList<CommunicationPrefs>(communicationPrefsRepository.findAll()));
	}

}
