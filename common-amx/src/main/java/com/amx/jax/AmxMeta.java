package com.amx.jax;

import org.springframework.stereotype.Component;

import com.amx.jax.dict.Language;
import com.amx.jax.util.JaxUtil;

@Component
public class AmxMeta {

	public Language getClientLanguage(Language lang) {
		return Language.fromId(JaxUtil.languageScaleMeta(AppContextUtil.getLang(lang).getBDCode()));
	}

	public Language getClientLanguage() {
		return getClientLanguage(Language.EN);
	}

	public void setClientLanguage(Language lang) {
		AppContextUtil.setLang(lang);
	}
}
