package com.amx.jax.complaince;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ReportJaxB {
	
	public static void Marshilling() {
		
		try {
			
			List<Phone> phoneList = new ArrayList<Phone>();
			Phone phone = new Phone();
			phone.setTph_contact_type("1");
			phone.setTph_communication_type("M");
			phone.setTph_number("123456");
			phoneList.add(phone);
			
			List<Phones> phonesList1 = new ArrayList<Phones>();
			Phones phones = new Phones();
			phones.setTph_contact_type("1");
			phones.setTph_communication_type("M");
			phones.setTph_country_prefix("M");
			phones.setTph_number("123456");
			phonesList1.add(phones);
			
			List<Address> addressList = new ArrayList<Address>();
			Address address = new Address();
			address.setAddress("Khaitan");
			address.setAddress_type("1");
			address.setCity("Kuwait");
			address.setCountry_code("KW");
			addressList.add(address);
			
			
			List<Phones> phonesList2 = new ArrayList<Phones>();
			Phones phones1 = new Phones();
			phones1.setTph_contact_type("1");
			phones1.setTph_communication_type("M");
			phones1.setTph_country_prefix("0091");
			phones1.setTph_number("0091");
			phonesList2.add(phones1);
			
			List<Address> addressList1 = new ArrayList<Address>();
			Address address1 = new Address();
			address1.setAddress("Tamil Nadu");
			address1.setAddress_type("1");
			address1.setCity("Chennai");
			address1.setCountry_code("IN");
			addressList1.add(address1);
			
			
			ReportIndicators repoInd =new ReportIndicators();
			List<String> indicator = new ArrayList<String>();
			indicator.add("PO-1");
			indicator.add("RS-2");			  
			repoInd.setIndicator(indicator);
			 
			//customer.getEmailAddresses().add("janed@example.com");
	       // cu//stomer.getEmailAddresses().add("jdoe@example.org");
	 
			
			
			Report repo = new Report("18", "E", "STR", "2019-05-16T00:00:00", "KWD", 
					new ReportingPerson("almulla", "admin", "KW", 
							phoneList,
							"Mohammed.Jabarullah@almullaexchange.com", "compliance officer"),
					new Location("1", "Khaitan", "Kuwait", "KW"),
					"Suspicious transaction", "Blocked the transaction",
					new Transaction("2019/123456", "Murgab Branch", "2019-05-16T00:00:00", "Kanmani", "Kanmani", "C", "150",
							new TFromMyClient("B", new FromForeignCurrency("INR", "30000", "230.25"), 
									new FromPerson("M", "Mr", "Osaba", "Bil Laden", "2019/123456", "SA", 
											phonesList1, 
											addressList, 
											new Identification("B", "2810502079628", "KW")), "KW"), 
							new Tto("B", new ToForeignCurrency("INR", "3000", "230.50"), 
									new ToPerson("M", "Mr", "Test bene", "bene", "2019/123456", "IN", 
											phonesList2, 
											addressList1, 
											new Identification("D", "123456", "IN")), "KW")), repoInd);
					JAXBContext jb =JAXBContext.newInstance(Report.class);
					Marshaller ms = jb.createMarshaller();
					ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					ms.marshal(repo, System.out);
					ms.marshal(repo, new File("src\\main\\java\\data\\report.xml"));
		}catch(Exception e) {
		System.out.println(""+e.getMessage());	
		
		}
	}
public static void UnMarshilling() {
		
		try {
			JAXBContext jb =JAXBContext.newInstance(Report.class);
			Unmarshaller ums = jb.createUnmarshaller();
			Report repo = (Report)ums.unmarshal(new File("src\\main\\java\\data\\report.xml"));
		
		}catch(Exception e) {
			System.out.println(""+e.getMessage());	
			
			}
			

}

public static void MakeZipfile() throws IOException {
	
	//File repo = new File("src\\main\\java\\data\\report.xml");
	BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\data\\report.xml"));
	StringBuilder stringBuilder = new StringBuilder();
	String line = null;
	String ls = System.getProperty("line.separator");
	while ((line = reader.readLine()) != null) {
		stringBuilder.append(line);
		stringBuilder.append(ls);
	}
	// delete the last new line separator
	stringBuilder.deleteCharAt(stringBuilder.length() - 1);
	reader.close();

	String content = stringBuilder.toString();
	
	StringBuilder sb = new StringBuilder();
	sb.append(content);

	String abc = "_Web_Report_ReportID";
	Date date = new Date();

	long time = date.getTime();

	String filenamewithoutextension = abc + time;
	File f = new File("d:\\" + filenamewithoutextension + ".zip");
	ZipOutputStream out=null;
	
		out = new ZipOutputStream(new FileOutputStream(f));
	
		
	String fileExtension = ".xml";

	String fileName = filenamewithoutextension + fileExtension;
	ZipEntry e = new ZipEntry(fileName);
	out.putNextEntry(e);

	byte[] data = sb.toString().getBytes();
	out.write(data, 0, data.length);
	out.closeEntry();

	out.close();
	
}
}