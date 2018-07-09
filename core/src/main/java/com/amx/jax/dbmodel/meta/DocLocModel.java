package com.amx.jax.dbmodel.meta;

import java.math.BigDecimal;
import java.util.Date;

public class DocLocModel {

	BigDecimal comCod;
	BigDecimal docFyr;
	BigDecimal docCode;
	BigDecimal locCod;
	Date createdDate;
	Date updatedDate;

	public BigDecimal getComCod() {
		return comCod;
	}

	public void setComCod(BigDecimal comCod) {
		this.comCod = comCod;
	}

	public BigDecimal getDocFyr() {
		return docFyr;
	}

	public void setDocFyr(BigDecimal docFyr) {
		this.docFyr = docFyr;
	}

	public BigDecimal getDocCode() {
		return docCode;
	}

	public void setDocCode(BigDecimal docCode) {
		this.docCode = docCode;
	}

	public BigDecimal getLocCod() {
		return locCod;
	}

	public void setLocCod(BigDecimal locCod) {
		this.locCod = locCod;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
