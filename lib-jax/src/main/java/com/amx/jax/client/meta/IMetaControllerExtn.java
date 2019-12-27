package com.amx.jax.client.meta;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.bene.InstitutionCategoryDto;
import com.amx.jax.client.bene.ReasoncodeCategory;
import com.amx.jax.model.meta.ReasonsDTO;

public interface IMetaControllerExtn {
	public static class Path {
		public static final String LIST_INSTITUTION_CATEGORY_MASTER = "/institution_category/list";
		public static final String API_REASON_CODE = "/reason_code/";
	}

	public static class Params {
		public static final String ID_NO = "idNo";
		public static final String REASON_CATEGORY = "reason_category";
	}

	AmxApiResponse<InstitutionCategoryDto, Object> listInstitutionCategoryMaster();

	AmxApiResponse<ReasonsDTO, Object> getReason(ReasoncodeCategory reasonCategory);

}
