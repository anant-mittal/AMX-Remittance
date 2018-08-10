package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="FS_FIELD_LIST")
public class FieldList {

	private BigDecimal fieldId;
	private String tenant;
	private String nationality;
	private String remitCountry;
	private String key;
	private String value;
	private String details;	
	
	public FieldList() {
		super();
	}

	public FieldList(BigDecimal fieldId, String tenant, String nationality, String remitCountry, String key,
			String value, String details) {
		super();
		this.fieldId = fieldId;
		this.tenant = tenant;
		this.nationality = nationality;
		this.remitCountry = remitCountry;
		this.key = key;
		this.value = value;
		this.details = details;
	}

	@Id
	@GeneratedValue(generator="fs_field_list_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="fs_field_list_seq",sequenceName="FS_FIELD_LIST_SEQ",allocationSize=1)
	@Column(name = "FIELD_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getFieldId() {
		return fieldId;
	}

	public void setFieldId(BigDecimal fieldId) {
		this.fieldId = fieldId;
	}

	@Column(name = "TENANT")
	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	@Column(name = "NATIONALITY")
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Column(name = "REMIT_COUNTRY")
	public String getRemitCountry() {
		return remitCountry;
	}

	public void setRemitCountry(String remitCountry) {
		this.remitCountry = remitCountry;
	}

	@Column(name = "KEY")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "DETAILS")
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
	
	
}
