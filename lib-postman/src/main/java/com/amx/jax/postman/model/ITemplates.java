package com.amx.jax.postman.model;

import com.amx.jax.AppConfig;
import com.amx.jax.dict.Project;
import com.amx.jax.postman.model.File.PDFConverter;

public class ITemplates {

	public interface ITemplate {

		boolean isThymleafJson();

		boolean isThymleaf();

		String getSampleJSON();

		PDFConverter getConverter();

		String getJsonFileName();

		String getFileName();

	}

	public static ITemplate getTemplate(String templateStr) {
		if (AppConfig.PROJECT == Project.IB) {
			return TemplatesIB.valueOf(templateStr);
		}
		return TemplatesMX.valueOf(templateStr);
	}

}
