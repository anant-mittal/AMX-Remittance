package com.amx.jax.serviceprovider.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dbmodel.FileUploadTempModel;
import com.amx.jax.dbmodel.ServiceProviderPartner;
import com.amx.jax.response.branchuser.ServiceProviderPartnerResponse;
import com.amx.jax.serviceprovider.dao.ServiceProviderDao;
import com.amx.jax.services.AbstractService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class ServiceProviderService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	ServiceProviderDao serviceProviderDao;
	
	public List<ServiceProviderPartnerResponse> getServiceProviderPartner() {
		List<ServiceProviderPartner> serviceProviderPartner = serviceProviderDao.getServiceProviderPartner();
		if(serviceProviderPartner.isEmpty()) {
			throw new GlobalException("Service provider partner list cannot be empty");
		}
		return convertServiceProviderPartner(serviceProviderPartner);
		
	}
	private List<ServiceProviderPartnerResponse> convertServiceProviderPartner(
			List<ServiceProviderPartner> serviceProviderPartnerList) {
		List<ServiceProviderPartnerResponse> output = new ArrayList<>();
		for (ServiceProviderPartner serviceProviderPartner : serviceProviderPartnerList) {
			ServiceProviderPartnerResponse serviceProviderPartnerResponse = new ServiceProviderPartnerResponse();
			serviceProviderPartnerResponse.setRecordId(serviceProviderPartner.getRecordId());
			serviceProviderPartnerResponse.setResourceName(serviceProviderPartner.getTpcName());
			serviceProviderPartnerResponse.setResourceCode(serviceProviderPartner.getTpcCode());
			output.add(serviceProviderPartnerResponse);
		}
		return output;
		
	}
	
	public BoolRespModel uploadServiceProviderFile(MultipartFile file,Date fileDate) throws Exception {
		InputStream in = file.getInputStream();
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		logger.info("FUll path is "+path);
	    String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
	    logger.info("File path is "+fileLocation);
	    FileOutputStream f = new FileOutputStream(fileLocation);
	    int ch = 0;
	    while ((ch = in.read()) != -1) {
	        f.write(ch);
	    }
	    f.flush();
	    f.close(); 
	    int i,j;
	    Workbook workbook = WorkbookFactory.create(new File(fileLocation));
	    LocalDate today = LocalDate.now();
	    int year = today.getYear();
	    int month = today.getMonthValue();
	    int day = today.getDayOfMonth();
	    
	    Sheet sheet = workbook.getSheetAt(0);
	    DataFormatter dataFormatter = new DataFormatter();
	    for(i=1;i<sheet.getLastRowNum();i++) {
	    	Row r = sheet.getRow(i);
	    	for(j=1;j<4;j++) {
	    		Cell cell1 = r.getCell(j);
	    		Cell cell2 = r.getCell(j+1);
	    		Cell cell3 = r.getCell(j+2);
	    		String cellValue1 = dataFormatter.formatCellValue(cell1);
	    		String cellValue2 = dataFormatter.formatCellValue(cell2);
	    		String cellValue3 = dataFormatter.formatCellValue(cell3);
	    		if(!(cellValue1.equals(String.valueOf(year))&&cellValue2.equals(String.valueOf(month))&&cellValue3.equals(String.valueOf(day)))) {
	    			throw new GlobalException("File Date is not matching with the date input");
	    		}
	    		j=4;
	    	}
	    }
	    FileUploadTempModel fileUploadTempModel = new FileUploadTempModel();
	    for(i=1;i<sheet.getLastRowNum();i++) {
	    	Row row = sheet.getRow(i);
	    	Cell cell0 = row.getCell(0);
	    	String cellValue0 = dataFormatter.formatCellValue(cell0);
	    	fileUploadTempModel.setLocalYear(new BigDecimal(cellValue0));
	    	Cell cell1 = row.getCell(1);
	    	String cellValue1 = dataFormatter.formatCellValue(cell1);
	    	fileUploadTempModel.setReportingYear(new BigDecimal(cellValue1));
	    	Cell cell2 = row.getCell(2);
	    	String cellValue2 = dataFormatter.formatCellValue(cell2);
	    	fileUploadTempModel.setReportingMonth(new BigDecimal(cellValue2));
	    	Cell cell3 = row.getCell(3);
	    	String cellValue3 = dataFormatter.formatCellValue(cell3);
	    	fileUploadTempModel.setReportingDate(new BigDecimal(cellValue3));
	    	Cell cell4 = row.getCell(4);
	    	String cellValue4 = dataFormatter.formatCellValue(cell4);
	    	fileUploadTempModel.setDirection(cellValue4);
	    	Cell cell5 = row.getCell(5);
	    	String cellValue5 = dataFormatter.formatCellValue(cell5);
	    	fileUploadTempModel.setAccountNo(cellValue5);
	    	Cell cell6 = row.getCell(6);
	    	String cellValue6 = dataFormatter.formatCellValue(cell6);
	    	fileUploadTempModel.setLocationName(cellValue6);
	    	Cell cell7 = row.getCell(7);
	    	String cellValue7 = dataFormatter.formatCellValue(cell7);
	    	fileUploadTempModel.setLocalCurrencyCode(cellValue7);
	    	Cell cell8 = row.getCell(8);
	    	String cellValue8 = dataFormatter.formatCellValue(cell8);
	    	fileUploadTempModel.setCompanyShareLocal(new BigDecimal(cellValue8));
	    	Cell cell9 = row.getCell(9);
	    	String cellValue9 = dataFormatter.formatCellValue(cell9);
	    	fileUploadTempModel.setExchangeGainLocal(new BigDecimal(cellValue9));
	    	Cell cell10 = row.getCell(10);
	    	String cellValue10 = dataFormatter.formatCellValue(cell10);
	    	fileUploadTempModel.setSendPayIndicator(cellValue10);
            Cell cell11 = row.getCell(11);
            String cellValue11 = dataFormatter.formatCellValue(cell11);
            fileUploadTempModel.setMtcnNo(cellValue11);
            serviceProviderDao.saveFileUploadTemp(fileUploadTempModel);
            logger.info("\n");
        }
	    
	    
	    
	    workbook.close();
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		return boolRespModel;
		
	}
	
	
}
