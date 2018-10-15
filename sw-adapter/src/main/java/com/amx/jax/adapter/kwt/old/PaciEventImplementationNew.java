package com.amx.jax.adapter.kwt.old;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import pacicardlibrary.PaciEventHandler;
import pacicardlibrary.PaciException;

public class PaciEventImplementationNew implements PaciEventHandler {

	String paciInfoAll = null;
	String paciInformation = null;
	static String fetchCardData = new String();

	@Override
	public void ReaderChangeEvent() {
		/*
		 * System.out.println(
		 * "ReaderChangeEvent Method--->No of connected Readers :" +
		 * PaciInfo.paciApi.GetNumberOfReaders()); String[] readers =
		 * PaciInfo.paciApi.GetReaders(); for (int i = 0; i < readers.length;
		 * i++) { System.out.println("Readers :" + readers[i]); }
		 */
		System.out.println("Inside ReaderChangeEvent: Doing Nothing");
	}

	@Override
	public void CardConnectionEvent(int readerIndex) {
		
		System.out.println("Card Connection Event : Triggered with PaciInfo.isCall:" + PaciInfo.isCall);
		
		if(PaciInfo.isCall)
		{
		
		System.out.println("Card Connection Event :");
		System.out.println("PaciInfo.isCall inside CardConnectionEvent :" + PaciInfo.isCall );
		String title = new String();
		String fName = new String();
		String mName = new String();
		String sName = new String();
		String gfName = new String();
		String civilId = new String();
		String gender = new String();
		String nationality = new String();
		String dob = new String();
		String issueDate = new String();
		String expiryDate = new String();
		String docNo = new String();
		String serilaNo = new String();
		String moiRefer = new String();
		String moiRefeInicator = new String();
		String district = new String();
		String block = new String();
		String street = new String();
		String buildNo = new String();
		String unitType = new String();
		String unitNo = new String();
		String floor = new String();
		String bloodType = new String();
		String guardianCivilId = new String();
		String teleNo1 = new String();
		String teleNo2 = new String();
		String email = new String();
		String arFullName = new String();
		String arFirstName = new String();
		String arFatherName = new String();
		String arGFName = new String();
		String arSName = new String();
		String arNationality = new String();
		String arGender = new String();
		String addiFiled1 = new String();
		String addiField2 = new String();
		String addressUKey = new String();
		String arUnitType = new String();
		String enName = new String();
		String paciInfoAll = new String();
		String enFullName = new String();
		StringBuffer sb = new StringBuffer();
		int noOfReaders = 0;
		int photoLength = 0;
		long startTime = System.currentTimeMillis();

		try {
			title = PaciInfo.paciApi.getA_TITLE(readerIndex, true);
			fName = PaciInfo.paciApi.getEnglishName_1(readerIndex, true);

			arFirstName = PaciInfo.paciApi.getArabicName_1(0, true);
			arFatherName = PaciInfo.paciApi.getArabicName_2(0, true);
			arGFName = PaciInfo.paciApi.getArabicName_3(0, false);
			arSName = PaciInfo.paciApi.getArabicName_4(0, false);
			arNationality = PaciInfo.paciApi.getNationalty_Arabic_Text(0, false);
			arGender = PaciInfo.paciApi.getSex_Arabic_Text(0, false);

			fName = PaciInfo.paciApi.getEnglishName_1(0, false);
			mName = PaciInfo.paciApi.getEnglishName_2(0, false);
			gfName = PaciInfo.paciApi.getEnglishName_3(0, false);
			sName = PaciInfo.paciApi.getEnglishName_4(0, false);
			civilId = PaciInfo.paciApi.getCivil_ID(0, false);
			gender = PaciInfo.paciApi.getSex_Latin_Text(0, false);
			nationality = PaciInfo.paciApi.getNationalty_Latin_Text(0, false);

			civilId = PaciInfo.paciApi.getCivil_ID(readerIndex, true);
			expiryDate = PaciInfo.paciApi.ReadCardInfo(readerIndex, pacicardlibrary.PACICardAPI.DataType.CardExpiryDate, true);
			enFullName = PaciInfo.paciApi.ReadCardInfo(readerIndex, pacicardlibrary.PACICardAPI.DataType.FullEnglishName, true);
			arFullName = PaciInfo.paciApi.ReadCardInfo(readerIndex, pacicardlibrary.PACICardAPI.DataType.FullArabicName, true);

			dob = PaciInfo.paciApi.getBirth_Date(0, false);
			issueDate = PaciInfo.paciApi.getCard_Issue_Date(0, false);
			expiryDate = PaciInfo.paciApi.getCard_Expiry_Date(0, false);
			docNo = PaciInfo.paciApi.getDocument_No(0, false);
			serilaNo = PaciInfo.paciApi.getCard_Serial_No(0, false);
			moiRefer = PaciInfo.paciApi.getMOI_Reference(0, false);
			moiRefeInicator = PaciInfo.paciApi.getMOI_Refernce_Indic(0, false);
			district = PaciInfo.paciApi.getDestrict(0, false);
			block = PaciInfo.paciApi.getBlock_No(0, false);
			street = PaciInfo.paciApi.getStreet_Name(0, false);
			buildNo = PaciInfo.paciApi.getBuilding_Plot_No(0, false);
			unitType = PaciInfo.paciApi.getUnit_type(0, false);

			unitNo = PaciInfo.paciApi.getUnit_No(0, false);
			floor = PaciInfo.paciApi.getFloor_No(0, false);
			bloodType = PaciInfo.paciApi.getBlood_Type(0, false);
			guardianCivilId = PaciInfo.paciApi.getGuardian_Civil_ID_No(0, false);
			teleNo1 = PaciInfo.paciApi.getTel_1(0, false);
			teleNo2 = PaciInfo.paciApi.getTel_2(0, false);
			email = PaciInfo.paciApi.getEmail_Address(0, false);
			addiFiled1 = PaciInfo.paciApi.getAdditional_F1(0, false);
			addiField2 = PaciInfo.paciApi.getAdditional_F2(0, false);
			addressUKey = PaciInfo.paciApi.getAddress_Unique_Key(0, false);

			if (dob != null && dob.length() > 0 && dob.length() > 4) {
				dob = dob.subSequence(0, 4) + "/" + dob.subSequence(4, 6) + "/" + dob.substring(6);
			} else {
				dob = dob;
			}

			if (issueDate != null && issueDate.length() > 0 && issueDate.length() > 4) {
				issueDate = issueDate.substring(0, 4) + "/" + issueDate.substring(4, 6) + "/" + issueDate.substring(6);
			} else {
				issueDate = issueDate;
			}

			if (expiryDate != null && expiryDate.length() > 0 && expiryDate.length() > 4) {
				expiryDate = expiryDate.substring(0, 4) + "/" + expiryDate.substring(4, 6) + "/" + expiryDate.substring(6);
			} else {
				expiryDate = expiryDate;
			}

			paciInfoAll = (new StringBuilder("Arabic Full Name=")).append(arFullName).append("#Arabic First Name=").append(arFirstName).append("#Arabic Father Name=").append(arFatherName).append("#Arabic GF Name=").append(arGFName)
					.append("#Arabic Surname=").append(arSName).append("#Nationality in Arabic=").append(arNationality).append("#Gender in Arabic=").append(arGender).append("#Name=").append(enFullName).append("#CivilId=").append(civilId)
					.append("#Gender=").append(gender).append("#Nationality=").append(nationality).append("#BirthDate=").append(dob).append("#IssueDate=").append(issueDate).append("#ExpiryDate=").append(expiryDate).append("#Documnet No=")
					.append(docNo).append("#Serial No=").append(serilaNo).append("#MOI Reference=").append(moiRefer).append("#MOI Reference Indicator=").append(moiRefeInicator).append("#District=").append(district).append("#Block=").append(block)
					.append("#Street=").append(street).append("#Building No=").append(buildNo).append("#Unit Type=").append(unitType).append("#Unit No.=").append(unitNo).append("#Floor=").append(floor).append("#Blood Type=").append(bloodType)
					.append("#Guardian Civil ID=").append(guardianCivilId).append("#Telephone1=").append(teleNo1).append("#Telephone2=").append(teleNo2).append("#E-Mail address=").append(email).append("#Additional filed1=").append(addiFiled1)
					.append("#Additional field2=").append(addiField2).append("#Address Unique key=").append(addressUKey).append("#LATIN-NAME-1=").append(fName).append("#LATIN-NAME-2=").append(mName).append("#LATIN-NAME-4=").append(gfName)
					.append("#LATIN-NAME-3=").append(sName).append("#Photo Length=").append(photoLength).toString();

			//System.out.println("paciInfoAll:" + paciInfoAll);
			long endTime = System.currentTimeMillis();
			System.out.println((new StringBuilder("Total time in for reading all Smart Card details seconds:")).append((endTime - startTime) / 1000L).toString());

			try {
				setPaciInformation(paciInfoAll);
				//writeToText(paciInfoAll, civilId);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (PaciException e) {
			e.printStackTrace();
			System.out.println("Exception :" + e.getMessage());
		} finally {
			PaciInfo.isCall = false;
			System.out.println("Inside final method :" + PaciInfo.isCall);
		}
	} // End of If statement.
   }

	@Override
	public void CardDisconnectionEvent(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("Insdie Card Disconnection Method:");
		PaciInfo.isCall = false;

	}

	public static void writeToText(String str, String civilIdNo) throws IOException {
		FileWriter fileWriter = null;

		File newTextFile = new File("C:\\SMARTCARD\\paci_details.txt");
		fileWriter = new FileWriter(newTextFile);
		fileWriter.write(str);
		fileWriter.close();

		try {
			fileWriter.close();
		} catch (Exception exception1) {
		}
		try {
			fileWriter.close();
		} catch (Exception exception2) {
		}
		return;
	}

	/**
	 * @return the paciInformation
	 */
	public String getPaciInformation() {
		return paciInformation;
	}

	/**
	 * @param paciInformation
	 *            the paciInformation to set
	 */

	public void setPaciInformation(String paciInformation) {
		this.paciInformation = paciInformation;
	}

	public String getFetchCardData() {
		return fetchCardData;
	}

	public static void setFetchCardData(String fetchCardData) {
		PaciEventImplementationNew.fetchCardData = fetchCardData;
	}
	
	
	public void readDataFromCard(){
		
		System.out.println("Before setting the value of readDataFromCard:  " + PaciInfo.isCall );
		PaciInfo.isCall = true;
		System.out.println("After setting the value of readDataFromCard:  " + PaciInfo.isCall );
	}	
/*
	{
	System.out.println("Inside readDataFromCard() Method Call:");
		
		System.out.println("Before ReaderChangeEvent() Method Call:");
		
		ReaderChangeEvent();
		
		System.out.println("After ReaderChangeEvent() Method Call:");
		
		System.out.println("Before CardConnectionEvent() Method Call:");
		
		CardConnectionEvent(0);
		
		System.out.println("After CardConnectionEvent() Method Call:");
		
		int readerIndex = 0;
		
		String title = new String();
		String fName = new String();
		String mName = new String();
		String sName = new String();
		String gfName = new String();
		String civilId = new String();
		String gender = new String();
		String nationality = new String();
		String dob = new String();
		String issueDate = new String();
		String expiryDate = new String();
		String docNo = new String();
		String serilaNo = new String();
		String moiRefer = new String();
		String moiRefeInicator = new String();
		String district = new String();
		String block = new String();
		String street = new String();
		String buildNo = new String();
		String unitType = new String();
		String unitNo = new String();
		String floor = new String();
		String bloodType = new String();
		String guardianCivilId = new String();
		String teleNo1 = new String();
		String teleNo2 = new String();
		String email = new String();
		String arFullName = new String();
		String arFirstName = new String();
		String arFatherName = new String();
		String arGFName = new String();
		String arSName = new String();
		String arNationality = new String();
		String arGender = new String();
		String addiFiled1 = new String();
		String addiField2 = new String();
		String addressUKey = new String();
		String arUnitType = new String();
		String enName = new String();
		String paciInfoAll = new String();
		String enFullName = new String();
		StringBuffer sb = new StringBuffer();
		int noOfReaders = 0;
		int photoLength = 0;
		long startTime = System.currentTimeMillis();

		try {
			PaciInfo.paciApi.GetReaders();
			
			//System.out.println("Number of readers:" + PaciInfo.paciApi.GetReaders() );
			
			String[] readers = PaciInfo.paciApi.GetReaders();
			
			for (int i = 0; i < readers.length;i++) {
				System.out.println("Readers :" + readers[i]); 
			}
			
			title = PaciInfo.paciApi.getA_TITLE(readerIndex, true);
			fName = PaciInfo.paciApi.getEnglishName_1(readerIndex, true);

			arFirstName = PaciInfo.paciApi.getArabicName_1(0, true);
			arFatherName = PaciInfo.paciApi.getArabicName_2(0, true);
			arGFName = PaciInfo.paciApi.getArabicName_3(0, false);
			arSName = PaciInfo.paciApi.getArabicName_4(0, false);
			arNationality = PaciInfo.paciApi.getNationalty_Arabic_Text(0, false);
			arGender = PaciInfo.paciApi.getSex_Arabic_Text(0, false);

			fName = PaciInfo.paciApi.getEnglishName_1(0, false);
			mName = PaciInfo.paciApi.getEnglishName_2(0, false);
			gfName = PaciInfo.paciApi.getEnglishName_3(0, false);
			sName = PaciInfo.paciApi.getEnglishName_4(0, false);
			civilId = PaciInfo.paciApi.getCivil_ID(0, false);
			gender = PaciInfo.paciApi.getSex_Latin_Text(0, false);
			nationality = PaciInfo.paciApi.getNationalty_Latin_Text(0, false);

			civilId = PaciInfo.paciApi.getCivil_ID(readerIndex, true);
			expiryDate = PaciInfo.paciApi.ReadCardInfo(readerIndex, pacicardlibrary.PACICardAPI.DataType.CardExpiryDate, true);
			enFullName = PaciInfo.paciApi.ReadCardInfo(readerIndex, pacicardlibrary.PACICardAPI.DataType.FullEnglishName, true);
			arFullName = PaciInfo.paciApi.ReadCardInfo(readerIndex, pacicardlibrary.PACICardAPI.DataType.FullArabicName, true);

			dob = PaciInfo.paciApi.getBirth_Date(0, false);
			issueDate = PaciInfo.paciApi.getCard_Issue_Date(0, false);
			expiryDate = PaciInfo.paciApi.getCard_Expiry_Date(0, false);
			docNo = PaciInfo.paciApi.getDocument_No(0, false);
			serilaNo = PaciInfo.paciApi.getCard_Serial_No(0, false);
			moiRefer = PaciInfo.paciApi.getMOI_Reference(0, false);
			moiRefeInicator = PaciInfo.paciApi.getMOI_Refernce_Indic(0, false);
			district = PaciInfo.paciApi.getDestrict(0, false);
			block = PaciInfo.paciApi.getBlock_No(0, false);
			street = PaciInfo.paciApi.getStreet_Name(0, false);
			buildNo = PaciInfo.paciApi.getBuilding_Plot_No(0, false);
			unitType = PaciInfo.paciApi.getUnit_type(0, false);

			unitNo = PaciInfo.paciApi.getUnit_No(0, false);
			floor = PaciInfo.paciApi.getFloor_No(0, false);
			bloodType = PaciInfo.paciApi.getBlood_Type(0, false);
			guardianCivilId = PaciInfo.paciApi.getGuardian_Civil_ID_No(0, false);
			teleNo1 = PaciInfo.paciApi.getTel_1(0, false);
			teleNo2 = PaciInfo.paciApi.getTel_2(0, false);
			email = PaciInfo.paciApi.getEmail_Address(0, false);
			addiFiled1 = PaciInfo.paciApi.getAdditional_F1(0, false);
			addiField2 = PaciInfo.paciApi.getAdditional_F2(0, false);
			addressUKey = PaciInfo.paciApi.getAddress_Unique_Key(0, false);

			if (dob != null && dob.length() > 0 && dob.length() > 4) {
				dob = dob.subSequence(0, 4) + "/" + dob.subSequence(4, 6) + "/" + dob.substring(6);
			} else {
				dob = dob;
			}

			if (issueDate != null && issueDate.length() > 0 && issueDate.length() > 4) {
				issueDate = issueDate.substring(0, 4) + "/" + issueDate.substring(4, 6) + "/" + issueDate.substring(6);
			} else {
				issueDate = issueDate;
			}

			if (expiryDate != null && expiryDate.length() > 0 && expiryDate.length() > 4) {
				expiryDate = expiryDate.substring(0, 4) + "/" + expiryDate.substring(4, 6) + "/" + expiryDate.substring(6);
			} else {
				expiryDate = expiryDate;
			}

			paciInfoAll = (new StringBuilder("Arabic Full Name=")).append(arFullName).append("#Arabic First Name=").append(arFirstName).append("#Arabic Father Name=").append(arFatherName).append("#Arabic GF Name=").append(arGFName)
					.append("#Arabic Surname=").append(arSName).append("#Nationality in Arabic=").append(arNationality).append("#Gender in Arabic=").append(arGender).append("#Name=").append(enFullName).append("#CivilId=").append(civilId)
					.append("#Gender=").append(gender).append("#Nationality=").append(nationality).append("#BirthDate=").append(dob).append("#IssueDate=").append(issueDate).append("#ExpiryDate=").append(expiryDate).append("#Documnet No=")
					.append(docNo).append("#Serial No=").append(serilaNo).append("#MOI Reference=").append(moiRefer).append("#MOI Reference Indicator=").append(moiRefeInicator).append("#District=").append(district).append("#Block=").append(block)
					.append("#Street=").append(street).append("#Building No=").append(buildNo).append("#Unit Type=").append(unitType).append("#Unit No.=").append(unitNo).append("#Floor=").append(floor).append("#Blood Type=").append(bloodType)
					.append("#Guardian Civil ID=").append(guardianCivilId).append("#Telephone1=").append(teleNo1).append("#Telephone2=").append(teleNo2).append("#E-Mail address=").append(email).append("#Additional filed1=").append(addiFiled1)
					.append("#Additional field2=").append(addiField2).append("#Address Unique key=").append(addressUKey).append("#LATIN-NAME-1=").append(fName).append("#LATIN-NAME-2=").append(mName).append("#LATIN-NAME-4=").append(gfName)
					.append("#LATIN-NAME-3=").append(sName).append("#Photo Length=").append(photoLength).toString();

			System.out.println("paciInfoAll:" + paciInfoAll);
			
			long endTime = System.currentTimeMillis();
			
			System.out.println((new StringBuilder("Total time in for reading all Smart Card details seconds:")).append((endTime - startTime) / 1000L).toString());

			/*try {
				//setPaciInformation(paciInfoAll);
				//writeToText(paciInfoAll, civilId);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (PaciException e) {
			e.printStackTrace();
			System.out.println("Exception :" + e.getMessage());
		} finally {
			// System.exit(1);
		}
     

		return paciInfoAll;
		
	}
  */
}