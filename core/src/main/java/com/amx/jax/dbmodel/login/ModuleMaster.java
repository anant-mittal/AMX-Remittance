package com.amx.jax.dbmodel.login;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_MODULE_MASTER" )
public class ModuleMaster implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal moduleId;
	private String moduleEnum;
	private String moduleName;
	private String isactive;
	private Date createdDate;
	
	@Id
	@GeneratedValue(generator="ex_module_master_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_module_master_seq",sequenceName="EX_MODULE_MASTER_SEQ",allocationSize=1)
	@Column(name = "MODULE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getModuleId() {
		return moduleId;
	}
	public void setModuleId(BigDecimal moduleId) {
		this.moduleId = moduleId;
	}
	
	@Column(name = "MODULE_ENUM")
	public String getModuleEnum() {
		return moduleEnum;
	}
	public void setModuleEnum(String moduleEnum) {
		this.moduleEnum = moduleEnum;
	}
	
	@Column(name = "MODULE_NAME")
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	@Column(name = "ISACTIVE")
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
