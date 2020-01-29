package com.amx.jax.dict;

import com.amx.jax.dict.PayGCodes.CodeCategory;

public interface IResponseCode<T extends Enum<?>> {

	public CodeCategory getCodeCategoryByResponseCode(String responseCode);

	public T getResponseCodeEnumByCode(String responseCode);

}
