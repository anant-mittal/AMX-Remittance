package com.amx.jax;

import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;

@Component
public class AmxMeta {

	public Language getClientLanguage(Language lang) {
		return AppContextUtil.getLang(lang);
	}

	public Language getClientLanguage() {
		return AppContextUtil.getLang(Language.EN);
	}

	public void setClientLanguage(Language lang) {
		AppContextUtil.setLang(lang);
	}
}
