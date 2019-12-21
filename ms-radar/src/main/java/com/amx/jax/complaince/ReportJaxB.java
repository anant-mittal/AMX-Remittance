package com.amx.jax.complaince;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import org.springframework.mock.web.MockMultipartFile;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints.Paramss;
import com.amx.jax.compliance.service.ComplianceService;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
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
	JaxMetaInfo metaData;
	
	@Autowired
	ComplianceService complianceService;
	
	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;
	
	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;


	public void Marshilling(CBK_Report cbk) {

		Customer cust = customerRepository.getCustomerOneByIdentityInt(cbk.getCustSsn());
		List<ActionParamDto> actionDetailsDto=	complianceService.complainceActionData();
		List<ReasonParamDto> reasonDetailsDto=	complianceService.complainceReasonData();
		
		List<ParameterDetails> indicatorDetails = parameterDetailsRespository.fetchCodeDetails(Paramss.COMPLAINCE_INDICATOR);
		ParameterDetailsDto param = new ParameterDetailsDto();
		param.setCharUdf1(indicatorDetails.get(0).getCharField1());
		
		
		
		try {

			List<Phone> phoneList = new ArrayList<Phone>();
			Phone phone = new Phone();
			phone.setTph_contact_type(cbk.getEmpPhcontacttype()== null ? "NA" : cbk.getEmpPhcontacttype());
			phone.setTph_communication_type(cbk.getEmpPhoneCommunicationtype()== null ? "NA" : cbk.getEmpPhoneCommunicationtype());
			phone.setTph_number(cbk.getEmpPhoneNo()== null ? "NA" : cbk.getEmpPhoneNo());
			phoneList.add(phone);

			List<Phones> phonesList1 = new ArrayList<Phones>();
			Phones phones = new Phones();
			phones.setTph_contact_type(cbk.getCustPhContacttype()== null ? "NA" : cbk.getCustPhContacttype());
			phones.setTph_communication_type(cbk.getCustPhCommunicationType()== null ? "NA" : cbk.getCustPhCommunicationType());
			phones.setTph_country_prefix(cbk.getCustPhCountryPrefix()== null ? "NA" : cbk.getCustPhCountryPrefix());
			phones.setTph_number(cbk.getCustPhNo()== null ? "NA" : cbk.getCustPhNo());
			phonesList1.add(phones);

			List<Address> addressList = new ArrayList<Address>();
			Address address = new Address();
			address.setAddress("NA");
			address.setAddress_type(cbk.getEmpaddresstype()== null ? "NA" : cbk.getEmpaddresstype());
			address.setCity("NA");
			address.setCountry_code(cbk.getEmpcountrycode()== null ? "NA" : cbk.getEmpcountrycode());
			addressList.add(address);

			List<Phones> phonesList2 = new ArrayList<Phones>();
			Phones phones1 = new Phones();
			phones1.setTph_contact_type(cbk.getBenePhoneContacttype()== null ? "NA" : cbk.getBenePhoneContacttype());
			phones1.setTph_communication_type(cbk.getBenePhoneCommunicationType()== null ? "NA" : cbk.getBenePhoneCommunicationType());
			phones1.setTph_country_prefix("NA");
			phones1.setTph_number(cbk.getBenePhoneNumber()== null ? "NA" : cbk.getBenePhoneNumber());
			phonesList2.add(phones1);

			List<Address> addressList1 = new ArrayList<Address>();
			Address address1 = new Address();
			address1.setAddress(cbk.getBeneAddress()== null ? "NA" : cbk.getBeneAddress());
			address1.setAddress_type(cbk.getBeneAddressType()== null ? "NA" : cbk.getBeneAddressType());
			address1.setCity(cbk.getBeneCity()== null ? "NA" : cbk.getBeneCity());
			address1.setCountry_code(cbk.getBeneCountryCode().toString()== null ? "NA" : cbk.getBeneCountryCode().toString());
			addressList1.add(address1);

			List<ReportIndicators> repoIndList = new ArrayList<ReportIndicators>();
			ReportIndicators reportIndicators= new ReportIndicators();
			reportIndicators.setIndicator(indicatorDetails.get(0).getCharField1());
			repoIndList.add(reportIndicators);
			

			Report repo = new Report("18", cbk.getSubmissionCode(), cbk.getReportcode(),
					parseTime(cbk.getSubmissionDate()), cbk.getCurrencyCodeLocal(),
					new ReportingPerson(cbk.getEmpFullName(), cbk.getEmpFullName(), cbk.getEmpcountrycode(), phoneList,
							cbk.getEmail(), "NA"),
					new Location(cbk.getEmpaddresstype(), "NA", "NA", cbk.getEmpcountrycode()), reasonDetailsDto.get(0).getReasonDesc(), actionDetailsDto.get(0).getActionDesc(),
					new Transaction(cbk.getTransactionRefNo().toString(), cbk.getTrnxLocal(),
							parseTime(cbk.getTrnxDate()), (cbk.getTeller()== null ? "NA" : cbk.getTeller()), (cbk.getAuthorized()== null ? "NA" : cbk.getAuthorized()),
							cbk.getTransmodeCode().toString(), cbk.getAmountLocal().toString(),
							new TFromMyClient("B",
									new FromForeignCurrency(cbk.getForeignCurrencycode(),
											cbk.getForeignAmount().toString(), cbk.getForeignExchangerate().toString()),
									new FromPerson(cbk.getCustGender(), cbk.getCustTitle(), cbk.getCustFirstName(),
											cbk.getCustLastName(), cbk.getCustSsn(), cbk.getCustNationality(),
											phonesList1, addressList,
											new Identification(cbk.getCustIdentificationType().toString(),
													cbk.getCustIdentityNumber(), cbk.getCustIdIssueCountry())),
									cbk.getToCountry().toString()),
							new Tto("B",
									new ToForeignCurrency(cbk.getForeignCurrencycode(),
											cbk.getForeignAmount().toString(), cbk.getForeignExchangerate().toString()),
									new ToPerson("NA", cbk.getBeneFirstName(), cbk.getBeneLastName(), "NA", "NA",
											phonesList2, addressList1),
									cbk.getBeneCountryCode().toString())),
					repoIndList);
			JAXBContext jb = JAXBContext.newInstance(Report.class);
			Marshaller ms = jb.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ms.marshal(repo, System.out);
			ms.marshal(repo, new File("src\\main\\java\\data\\report.xml"));

			System.out.println("ms:" + ms.toString());

			// writing xml in to the database table
			StringWriter sw = new StringWriter();
			ms.marshal(repo, sw);
			String xmlContent = sw.toString(); 
	      
			//Inserting data into ExCbkStrReportLOG      
			Date date = new Date();
			ExCbkStrReportLOG logtable = new ExCbkStrReportLOG();
			logtable.setReqXml(IoUtils.stringToClob(xmlContent));
			logtable.setCustomerName(cbk.getCustFirstName() + cbk.getCustLastName());
			logtable.setRemittanceTranxId(cbk.getRemitTrnxId());
			logtable.setCreatedDate(date);
			logtable.setCreatedBy(cbk.getAuthorized());
			logtable.setCustomerId(cust.getCustomerId());
			logtable.setCustomerrRef(cust.getCustomerReference());
			logtable.setIpAddress(AppContextUtil.getUserClient().getIp().toString());
			logtable.setReasonCode(reasonDetailsDto.get(0).getReasonCode());
			logtable.setActionCode(actionDetailsDto.get(0).getActionCode());
	
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