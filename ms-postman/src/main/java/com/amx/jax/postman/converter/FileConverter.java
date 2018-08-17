package com.amx.jax.postman.converter;

import com.amx.jax.postman.model.File;

import net.sf.jasperreports.engine.JRException;

/**
 * The Interface FileConverter.
 */
public interface FileConverter {

	/**
	 * To PDF.
	 *
	 * @param file
	 *            the file
	 * @return the file
	 * @throws JRException
	 *             the JR exception
	 */
	public File toPDF(File file) throws JRException;
}
