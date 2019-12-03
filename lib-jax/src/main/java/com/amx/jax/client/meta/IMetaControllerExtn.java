package com.amx.jax.client.meta;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.bene.InstitutionCategoryDto;

public interface IMetaControllerExtn {
	public static class Path {
		public static final String LIST_INSTITUTION_CATEGORY_MASTER = "/institution_category/list";
	}

	public static class Params {
		public static final String ID_NO = "idNo";
	}

	AmxApiResponse<InstitutionCategoryDto, Object> listInstitutionCategoryMaster();

}
