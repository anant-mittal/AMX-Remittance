package com.amx.service_provider.dbmodel.webservice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

public class OwsTransferLogKey implements Serializable 
{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "COMCOD")
	Integer company_code;

	@Column(name = "DOCCOD")
	Integer document_code;

	@Column(name = "DOCFYR")
	Integer document_finance_year;

	@Column(name = "DOCNO")
	Integer document_number;

	@Column(name = "BNKCOD")
	String route_bank_code;
	
	@Column(name = "RSP_CODE")
	String response_code;

	@Column(name = "RSP_DESC")
	String response_desc;

	@Column(name = "WS_CALL_TYPE")
	String web_service_call_type;
	
	@Column(name = "CRTDAT")
	Date creation_date;

	public OwsTransferLogKey()
	{
		super();
	}

	public OwsTransferLogKey(Integer company_code, Integer document_code, Integer document_finance_year,
			Integer document_number, String route_bank_code, String response_code, String response_desc,
			String web_service_call_type, Date creation_date)
	{
		super();
		this.company_code = company_code;
		this.document_code = document_code;
		this.document_finance_year = document_finance_year;
		this.document_number = document_number;
		this.route_bank_code = route_bank_code;
		this.response_code = response_code;
		this.response_desc = response_desc;
		this.web_service_call_type = web_service_call_type;
		this.creation_date = creation_date;
	}

	public Integer getCompany_code()
	{
		return company_code;
	}

	public void setCompany_code(Integer company_code)
	{
		this.company_code = company_code;
	}

	public Integer getDocument_code()
	{
		return document_code;
	}

	public void setDocument_code(Integer document_code)
	{
		this.document_code = document_code;
	}

	public Integer getDocument_finance_year()
	{
		return document_finance_year;
	}

	public void setDocument_finance_year(Integer document_finance_year)
	{
		this.document_finance_year = document_finance_year;
	}

	public Integer getDocument_number()
	{
		return document_number;
	}

	public void setDocument_number(Integer document_number)
	{
		this.document_number = document_number;
	}

	public String getRoute_bank_code()
	{
		return route_bank_code;
	}

	public void setRoute_bank_code(String route_bank_code)
	{
		this.route_bank_code = route_bank_code;
	}

	public String getResponse_code()
	{
		return response_code;
	}

	public void setResponse_code(String response_code)
	{
		this.response_code = response_code;
	}

	public String getResponse_desc()
	{
		return response_desc;
	}

	public void setResponse_desc(String response_desc)
	{
		this.response_desc = response_desc;
	}

	public String getWeb_service_call_type()
	{
		return web_service_call_type;
	}

	public void setWeb_service_call_type(String web_service_call_type)
	{
		this.web_service_call_type = web_service_call_type;
	}

	public Date getCreation_date()
	{
		return creation_date;
	}

	public void setCreation_date(Date creation_date)
	{
		this.creation_date = creation_date;
	}
}
