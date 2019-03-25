package com.amx.jax.postman.model;

import com.amx.jax.ProjectConfig;
import com.amx.jax.dict.Project;
import com.amx.jax.postman.model.File.PDFConverter;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.utils.ArgUtil;

public class ITemplates {

	public interface ITemplate {

		boolean isThymleaf();

		String getSampleJSON();

		PDFConverter getConverter();

		String getJsonFile();

		String getHtmlFile();

		String getFileName();

		public default Channel getChannel() {
			return null;
		}

	}

	public static ITemplate getTemplate(String templateStr) {
		if (ProjectConfig.PROJECT == Project.IB) {
			return (ITemplate) ArgUtil.parseAsEnum(templateStr, TemplatesIB.class);
		}
		return (ITemplate) ArgUtil.parseAsEnum(templateStr, TemplatesMX.class);
	}

}
