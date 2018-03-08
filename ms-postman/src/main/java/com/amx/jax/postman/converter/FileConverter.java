package com.amx.jax.postman.converter;

import com.amx.jax.postman.model.File;

public interface FileConverter {
	public File toPDF(File file);
}
