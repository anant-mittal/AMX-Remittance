package com.amx.jax.complaince;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints.Paramss;
import com.amx.jax.compliance.service.ComplianceService;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.EmployeeRespository;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.utils.IoUtils;

@Component
public class ReportJaxB {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ExCbkReportLogRepo exCbkReportLogRepo;
	
	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;
	
	@Autowired
	ComplianceService complianceService;
	
	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;
	
	@Autowired
	EmployeeRespository employeeRespository;
	
	@Autowired
	CountryMasterRepository countryMasterRepository;


	public void Marshilling(jaxCbkReport cbk,BigDecimal employeeId) {

		Customer cust = customerRepository.getCustomerOneByIdentityInt(cbk.getCustSsn());
		List<ActionParamDto> actionDetailsDto=	complianceService.complainceActionData();
		List<ReasonParamDto> reasonDetailsDto=	complianceService.complainceReasonData();
		
		List<ParameterDetails> indicatorDetails = parameterDetailsRespository.fetchCodeDetails(Paramss.COMPLAINCE_INDICATOR);
		ParameterDetailsDto param = new ParameterDetailsDto();
		param.setCharUdf1(indicatorDetails.get(0).getCharField1());
		
		List<Employee> emp ;
		emp =  employeeRespository.findByEmployeeId(employeeId);		
		
		List<CountryMaster> cntryMaster ;
		cntryMaster = countryMasterRepository.getCountryAlpha2Code(emp.get(0).getEmpCountryId());
		
		Date subDate = new Date();
		
		String submissiondate = exCbkReportLogRepo.updateSubmissionDate(subDate);
		
		
		try {

			List<Phone> phoneList = new ArrayList<Phone>();
			Phone phone = new Phone();
			phone.setTph_contact_type(Paramss.COMPLAINCE_CONTACT_TYPE);
			phone.setTph_communication_type(Paramss.COMPLAINCE_COMMUNICATION_TYPE);
			phone.setTph_number(emp.get(0).getTelephoneNumber()== null ? "NA" : emp.get(0).getTelephoneNumber());
			phoneList.add(phone);

			List<Phones> phonesList1 = new ArrayList<Phones>();
			Phones phones = new Phones();
			phones.setTph_contact_type(Paramss.COMPLAINCE_CONTACT_TYPE);
			phones.setTph_communication_type(Paramss.COMPLAINCE_COMMUNICATION_TYPE);
			phones.setTph_country_prefix(cbk.getCustPhcountryPrefix()== null ? "NA" : cbk.getCustPhcountryPrefix());
			phones.setTph_number(cbk.getCustPhNumber()== null ? "NA" : cbk.getCustPhNumber());
			phonesList1.add(phones);

			List<Address> addressList = new ArrayList<Address>();
			Address address = new Address();
			address.setAddress("NA");
			address.setAddress_type(Paramss.COMPLAINCE_ADDRESS_TYPE);
			address.setCity("NA");
			address.setCountry_code(cbk.getCustcountrycode()== null ? "NA" : cbk.getCustcountrycode());
			addressList.add(address);

			List<Phones> phonesList2 = new ArrayList<Phones>();
			Phones phones1 = new Phones();
			phones1.setTph_contact_type(Paramss.COMPLAINCE_CONTACT_TYPE);
			phones1.setTph_communication_type(Paramss.COMPLAINCE_COMMUNICATION_TYPE);
			phones1.setTph_country_prefix("NA");
			phones1.setTph_number(cbk.getBenePhoneNo()== null ? "NA" : cbk.getBenePhoneNo());
			phonesList2.add(phones1);

			List<Address> addressList1 = new ArrayList<Address>();
			Address address1 = new Address();
			address1.setAddress(cbk.getBeneAddress()== null ? "NA" : cbk.getBeneAddress());
			address1.setAddress_type(Paramss.COMPLAINCE_ADDRESS_TYPE);
			address1.setCity(cbk.getBeneCity()== null ? "NA" : cbk.getBeneCity());
			address1.setCountry_code(cbk.getBeneCountrycode()== null ? "NA" : cbk.getBeneCountrycode());
			addressList1.add(address1);

			List<ReportIndicators> repoIndList = new ArrayList<ReportIndicators>();
			ReportIndicators reportIndicators= new ReportIndicators();
			reportIndicators.setIndicator(indicatorDetails.get(0).getCharField1());
			repoIndList.add(reportIndicators);
			
			Report repo = new Report(cbk.getRentityId(), cbk.getSubmissionCode(), cbk.getReportCode(),
					parseTime(submissiondate), cbk.getCurrencyCodeLocal(),
					new ReportingPerson(emp.get(0).getEmployeeName(), emp.get(0).getEmployeeName(),  cntryMaster.get(0).getCountryAlpha2Code(), phoneList,
							emp.get(0).getEmail(), "NA"),
					new Location(Paramss.COMPLAINCE_ADDRESS_TYPE, "NA", "NA", emp.get(0).getEmpCountryId().toString()), reasonDetailsDto.get(0).getReasonDesc(), actionDetailsDto.get(0).getActionDesc(),
					new Transaction(cbk.getTranxRef().toString(), cbk.getTrnxLocation(),
							parseTime(cbk.getTranxDate()), ("NA"), ("NA"),
							cbk.getTranxMode().toString(), cbk.getAmountLocal().toString(),
							new TFromMyClient(cbk.getFundsCode(),
									new FromForeignCurrency(cbk.getForeignCurrencyCode(),
											cbk.getForeignAmount().toString(), cbk.getForeignExRate().toString()),
									new FromPerson(cbk.getCustGender(), cbk.getCustTitle(), cbk.getCustFirstName(),
											cbk.getCustLastName(), cbk.getCustSsn(), cbk.getCustNationality(),
											phonesList1, addressList,
											new Identification(cbk.getCustIdentificationType().toString(),
													cbk.getCustIdentityNumber(), cbk.getCustIdIssueCountry())),
									cbk.getToCountry().toString()),
							new Tto(cbk.getFundsCode(),
									new ToForeignCurrency(cbk.getForeignCurrencyCode(),
											cbk.getForeignAmount().toString(), cbk.getForeignExRate().toString()),
									new ToPerson("NA", cbk.getBeneFirstName(), cbk.getBeneLastName(), "NA", "NA",
											phonesList2, addressList1),
									cbk.getBeneCountrycode())),
					repoIndList);
			JAXBContext jb = JAXBContext.newInstance(Report.class);
			Marshaller ms = jb.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ms.marshal(repo, System.out);
			ms.marshal(repo, new File("src\\main\\java\\data\\report.xml"));

			// writing xml in to the database table
			StringWriter sw = new StringWriter();
			ms.marshal(repo, sw);
			String xmlContent = sw.toString(); 
	      
			//Inserting data into ExCbkStrReportLOG      
			Date date = new Date();
			ExCbkStrReportLOG logtable = new ExCbkStrReportLOG();
			logtable.setReqXml(IoUtils.stringToClob(xmlContent));
			logtable.setCustomerName(cbk.getCustFirstName() + cbk.getCustLastName());
			logtable.setRemittanceTranxId(cbk.getTranxNo());
			logtable.setCreatedDate(date);
			logtable.setCustomerId(cust.getCustomerId());
			logtable.setCustomerrRef(cust.getCustomerReference());
			logtable.setIpAddress(AppContextUtil.getUserClient().getIp().toString());
			logtable.setReasonCode(reasonDetailsDto.get(0).getReasonDesc());
			logtable.setActionCode(actionDetailsDto.get(0).getActionDesc());
			logtable.setReportType(cbk.getReportCode());
			logtable.setCustIsActive(cbk.getIsActive());
	
			exCbkReportLogRepo.save(logtable);
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void UnMarshilling() {

		try {
			JAXBContext jb = JAXBContext.newInstance(Report.class);
			Unmarshaller ums = jb.createUnmarshaller();
			Report repo = (Report) ums.unmarshal(new File("src\\main\\java\\data\\report.xml"));

		} catch (Exception e) {
			System.out.println("" + e.getMessage());

		}

	}

	public File MakeZipfile(String file) throws IOException {
		
		Reader inputString = new StringReader(file);

		BufferedReader reader = new BufferedReader(inputString);
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		reader.close();

		String content = stringBuilder.toString();

		StringBuilder sb = new StringBuilder();
		sb.append(content);

		String abc = "_Web_Report_ReportID";
		Date date = new Date();

		long time = date.getTime();

		String filenamewithoutextension = abc + time;
		File f = new File(filenamewithoutextension + ".zip");
		ZipOutputStream out = null;

		out = new ZipOutputStream(new FileOutputStream(f));

		String fileExtension = ".xml";

		String fileName = filenamewithoutextension + fileExtension;
		ZipEntry e = new ZipEntry(fileName);
		out.putNextEntry(e);

		byte[] data = sb.toString().getBytes();
		out.write(data, 0, data.length);
		out.closeEntry();

		out.close();

		return f;

	}

	public String parseTime(String date) {
		try {
			String date1 = date.replace(" ", "T");
			return date1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}