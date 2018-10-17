package com.amx.jax.adapter.kwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.device.CardData;

import pacicardlibrary.PACICardAPI;
import pacicardlibrary.PaciEventHandler;
import pacicardlibrary.PaciException;

public class KWTCardReaderListner implements PaciEventHandler {

	public static Logger LOGGER = LoggerFactory.getLogger(KWTCardReaderListner.class);

	@SuppressWarnings("deprecation")
	@Override
	public void CardConnectionEvent(int readerIndex) {
		LOGGER.info("KwCardReaderListner:CardConnectionEvent {}", readerIndex);
		try {
			CardData data = KWTCardReader.readerData();

			data.setTitle(KWTCardReader.API.getA_TITLE(readerIndex, false));
			data.setIdentity(KWTCardReader.API.getCivil_ID(readerIndex, false));

			// Arabic Details
			data.setLocalFullName(
					KWTCardReader.API.ReadCardInfo(readerIndex, PACICardAPI.DataType.FullArabicName, false));
			data.setLocalName1(KWTCardReader.API.getArabicName_1(readerIndex, false));
			data.setLocalName2(KWTCardReader.API.getArabicName_2(readerIndex, false));
			data.setLocalName3(KWTCardReader.API.getArabicName_3(readerIndex, false));
			data.setLocalName4(KWTCardReader.API.getArabicName_4(readerIndex, false));
			data.setLocalNationality(KWTCardReader.API.getNationalty_Arabic_Text(readerIndex, false));
			data.setLocalGender(KWTCardReader.API.getSex_Arabic_Text(readerIndex, false));

			// English Details
			data.setFullName(KWTCardReader.API.ReadCardInfo(readerIndex, PACICardAPI.DataType.FullEnglishName, false));
			data.setName1(KWTCardReader.API.getEnglishName_1(readerIndex, false));
			data.setName2(KWTCardReader.API.getEnglishName_2(readerIndex, false));
			data.setName3(KWTCardReader.API.getEnglishName_3(readerIndex, false));
			data.setName4(KWTCardReader.API.getEnglishName_4(readerIndex, false));
			data.setGender(KWTCardReader.API.getSex_Latin_Text(readerIndex, false));
			data.setNationality(KWTCardReader.API.getNationalty_Latin_Text(readerIndex, false));

			data.setDocumentNo(KWTCardReader.API.getDocument_No(readerIndex, false));
			data.setSerialNo(KWTCardReader.API.getCard_Serial_No(readerIndex, false));

			data.setMoiReferenceIndicator(KWTCardReader.API.getMOI_Refernce_Indic(readerIndex, false));
			data.setMoiReference(KWTCardReader.API.getMOI_Reference(readerIndex, false));

			// Address
			data.setDistrict(KWTCardReader.API.getDestrict(readerIndex, false));
			data.setBlock(KWTCardReader.API.getBlock_No(readerIndex, false));
			data.setStreet(KWTCardReader.API.getStreet_Name(readerIndex, false));
			data.setBuildNo(KWTCardReader.API.getBuilding_Plot_No(readerIndex, false));
			data.setUnitType(KWTCardReader.API.getUnit_type(readerIndex, false));
			data.setUnitNo(KWTCardReader.API.getUnit_No(readerIndex, false));
			data.setFloor(KWTCardReader.API.getFloor_No(readerIndex, false));

			// Personal Details
			data.setBloodType(KWTCardReader.API.getBlood_Type(readerIndex, false));
			data.setGuardianId(KWTCardReader.API.getGuardian_Civil_ID_No(readerIndex, false));
			data.setTeleNo1(KWTCardReader.API.getTel_1(readerIndex, false));
			data.setTeleNo2(KWTCardReader.API.getTel_2(readerIndex, false));

			data.setEmail(KWTCardReader.API.getEmail_Address(readerIndex, false));
			data.setField1(KWTCardReader.API.getAdditional_F1(readerIndex, false));
			data.setField2(KWTCardReader.API.getAdditional_F2(readerIndex, false));
			data.setAddressUKey(KWTCardReader.API.getAddress_Unique_Key(readerIndex, false));

			String dob = KWTCardReader.API.getBirth_Date(readerIndex, false);
			if (dob != null && dob.length() > 0 && dob.length() > 4) {
				dob = dob.subSequence(0, 4) + "/" + dob.subSequence(4, 6) + "/" + dob.substring(6);
			}

			data.setDob(dob);

			String issueDate = KWTCardReader.API.getCard_Issue_Date(readerIndex, false);
			if (issueDate != null && issueDate.length() > 0 && issueDate.length() > 4) {
				issueDate = issueDate.substring(0, 4) + "/" + issueDate.substring(4, 6) + "/" + issueDate.substring(6);
			}
			data.setIssueDate(issueDate);

			// String expiryDate = KWTCardReader.API.ReadCardInfo(readerIndex,
			// pacicardlibrary.PACICardAPI.DataType.CardExpiryDate, true);
			String expiryDate = KWTCardReader.API.getCard_Expiry_Date(readerIndex, false);
			if (expiryDate != null && expiryDate.length() > 0 && expiryDate.length() > 4) {
				expiryDate = expiryDate.substring(0, 4) + "/" + expiryDate.substring(4, 6) + "/"
						+ expiryDate.substring(6);
			}

			data.setExpiryDate(expiryDate);
			data.setGenuine(KWTCardReader.API.ValidateCardIsGenuine(readerIndex));
			data.setValid(KWTCardReader.API.ValidateCardCertificate(readerIndex, false, true, true));

			KWTCardReader.readerData().setValid(true);
			KWTCardReader.push();

		} catch (PaciException e) {
			// e.printStackTrace();
			LOGGER.error("KwCardReaderListner:PaciException {}", e);
		} catch (Exception e2) {
			LOGGER.error("KwCardReaderListner:Exception {}", e2);
		}
	}

	@Override
	public void CardDisconnectionEvent(int arg0) {
		LOGGER.error("KwCardReaderListner:CardDisconnectionEvent  {}", arg0);
		KWTCardReader.clear();
	}

	@Override
	public void ReaderChangeEvent() {
		LOGGER.error("KwCardReaderListner:ReaderChangeEvent");
		KWTCardReader.info(KWTCardReader.API.GetReaders());
	}

}
