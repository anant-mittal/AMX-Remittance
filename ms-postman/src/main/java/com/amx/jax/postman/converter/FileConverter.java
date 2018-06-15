package com.amx.jax.postman.converter;

import com.amx.jax.postman.model.File;

import net.sf.jasperreports.engine.JRException;

public interface FileConverter {
	public File toPDF(File file) throws JRException;
}
