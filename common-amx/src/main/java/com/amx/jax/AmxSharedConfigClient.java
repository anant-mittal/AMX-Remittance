package com.amx.jax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;

@Component
public class AmxSharedConfigClient implements AmxSharedConfig {

	@Autowired(required = false)
	private AmxSharedConfigDB amxSharedConfigDB;

	@Autowired(required = false)
	private AmxSharedConfigApi amxSharedConfigService;

	private AmxApiResponse<CommunicationPrefs, Object> communicationPrefs;

	@Override
	public AmxApiResponse<CommunicationPrefs, Object> getCommunicationPrefs() {
		if (communicationPrefs != null) {
			return communicationPrefs;
		} else if (amxSharedConfigDB != null) {
			communicationPrefs = amxSharedConfigDB.getCommunicationPrefs();
		} else if (amxSharedConfigService != null) {
			communicationPrefs = amxSharedConfigService.getCommunicationPrefs();
		}
		return communicationPrefs;
	}

	@Override
	public void clear() {
		this.communicationPrefs = null;
	}

}
