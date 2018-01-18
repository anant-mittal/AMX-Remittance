/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.response;

import java.util.List;

/**
 * @author Viki Sangani
 * 01-Nov-2017
 * MetaModel.java
 */
public class MetaModel {

	/**
	 * 
	 */
	public MetaModel() {
		// TODO Auto-generated constructor stub
	}
	
	private String copyright;
	
	private List<String> authors;

	/**
	 * @return the copyright
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * @return the authors
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}


}
