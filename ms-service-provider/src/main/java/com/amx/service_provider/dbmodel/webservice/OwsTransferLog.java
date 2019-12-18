package com.amx.service_provider.dbmodel.webservice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "OWS_TRANSFER_LOG")
public class OwsTransferLog implements Serializable
{

	
	public OwsTransferLog()
	{
		super();
	}

	public OwsTransferLog(Integer company_code, Integer document_code, Integer document_finance_year,
			Integer document_number, String route_bank_code, String route_bank_short_name, String response_code,
			String response_desc, String web_service_call_type, String request_xml, String response_xml, String creator,
			Date creation_date)
	{
		super();
		owsTransferLogKey =
				new OwsTransferLogKey(company_code, document_code, document_finance_year, document_number,
						route_bank_code, response_code, response_desc, web_service_call_type, creation_date);

		this.route_bank_short_name = route_bank_short_name;
		this.request_xml = request_xml;
		this.response_xml = response_xml;
		this.creator = creator;

	}

	private static final long serialVersionUID = 7805653137109249539L;

	@EmbeddedId
	OwsTransferLogKey owsTransferLogKey;

	@Column(name = "FUDESC")
	String route_bank_short_name;

	@Column(name = "REQ_XML")
	String request_xml;

	@Column(name = "RESP_XML")
	String response_xml;

	@Column(name = "CREATOR")
	String creator;

	public OwsTransferLogKey getOwsTransferLogKey()
	{
		return owsTransferLogKey;
	}

	public void setOwsTransferLogKey(OwsTransferLogKey owsTransferLogKey)
	{
		this.owsTransferLogKey = owsTransferLogKey;
	}

	public String getRoute_bank_short_name()
	{
		return route_bank_short_name;
	}

	public void setRoute_bank_short_name(String route_bank_short_name)
	{
		this.route_bank_short_name = route_bank_short_name;
	}

	public String getRequest_xml()
	{
		return request_xml;
	}

	public void setRequest_xml(String request_xml)
	{
		this.request_xml = request_xml;
	}

	public String getResponse_xml()
	{
		return response_xml;
	}

	public void setResponse_xml(String response_xml)
	{
		this.response_xml = response_xml;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}
}