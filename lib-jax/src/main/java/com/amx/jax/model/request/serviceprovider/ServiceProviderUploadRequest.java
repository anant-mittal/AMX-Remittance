package com.amx.jax.model.request.serviceprovider;



import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

public class ServiceProviderUploadRequest {
	private MultipartFile file;
	private Date fileDate;
	private String tpcCode;
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public Date getFileDate() {
		return fileDate;
	}
	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}
	public String getTpcCode() {
		return tpcCode;
	}
	public void setTpcCode(String tpcCode) {
		this.tpcCode = tpcCode;
	}
}
