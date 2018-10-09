package com.amx.jax.postman.model;

import com.amx.jax.postman.model.File.PDFConverter;

public interface ITemplate {

	boolean isThymleafJson();

	boolean isThymleaf();

	String getSampleJSON();

	PDFConverter getConverter();

	String getJsonFileName();

	String getFileName();

}
