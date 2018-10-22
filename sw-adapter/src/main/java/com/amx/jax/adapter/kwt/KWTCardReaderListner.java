package com.amx.jax.adapter.kwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.adapter.ACardReaderService.CardStatus;
import com.amx.jax.adapter.ACardReaderService.DataStatus;
import com.amx.jax.device.CardData;
import com.amx.utils.ArgUtil;

import pacicardlibrary.PACICardAPI;
import pacicardlibrary.PaciEventHandler;
import pacicardlibrary.PaciException;

public class KWTCardReaderListner implements PaciEventHandler {

	public static Logger LOGGER = LoggerFactory.getLogger(KWTCardReaderListner.class);

	Thread.UncaughtExceptionHandler h = null;
	private boolean enabled = false;
	private boolean reading = false;
	private Object lock = new Object();
	private String readername = null;

	public boolean ping() {
		if (!enabled) {
			return false;
		}
		if (reading) {
			return true;
		}
		synchronized (lock) {
			try {
				return !ArgUtil.isEmpty(KWTCardReaderService.API.ValidateCardCertificate(0, false, true, true));
			} catch (PaciException pe) {
				LOGGER.error("AMX --- PaciException", pe);
				return false;
			} catch (Exception e) {
				LOGGER.error("AMX --- Exception", e);
				return false;
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void CardConnectionEvent(int readerIndex) {
		if (!enabled) {
			return;
		}
		KWTCardReaderService.CONTEXT.status(CardStatus.FOUND);
		synchronized (lock) {
			try {
				reading = true;
				KWTCardReaderService.CONTEXT.status(CardStatus.READING);
				LOGGER.debug("KWTCardReaderServiceListner:CardConnectionEvent");
				CardData data = new CardData();

				data.setTitle(KWTCardReaderService.API.getA_TITLE(readerIndex, false));
				data.setIdentity(KWTCardReaderService.API.getCivil_ID(readerIndex, false));

				// Arabic Details
				data.setLocalFullName(
						KWTCardReaderService.API.ReadCardInfo(readerIndex, PACICardAPI.DataType.FullArabicName, false));
				data.setLocalName1(KWTCardReaderService.API.getArabicName_1(readerIndex, false));
				data.setLocalName2(KWTCardReaderService.API.getArabicName_2(readerIndex, false));
				data.setLocalName3(KWTCardReaderService.API.getArabicName_3(readerIndex, false));
				data.setLocalName4(KWTCardReaderService.API.getArabicName_4(readerIndex, false));
				data.setLocalNationality(KWTCardReaderService.API.getNationalty_Arabic_Text(readerIndex, false));
				data.setLocalGender(KWTCardReaderService.API.getSex_Arabic_Text(readerIndex, false));

				// English Details
				data.setFullName(KWTCardReaderService.API.ReadCardInfo(readerIndex,
						PACICardAPI.DataType.FullEnglishName, false));
				data.setName1(KWTCardReaderService.API.getEnglishName_1(readerIndex, false));
				data.setName2(KWTCardReaderService.API.getEnglishName_2(readerIndex, false));
				data.setName3(KWTCardReaderService.API.getEnglishName_3(readerIndex, false));
				data.setName4(KWTCardReaderService.API.getEnglishName_4(readerIndex, false));
				data.setGender(KWTCardReaderService.API.getSex_Latin_Text(readerIndex, false));
				data.setNationality(KWTCardReaderService.API.getNationalty_Latin_Text(readerIndex, false));

				data.setDocumentNo(KWTCardReaderService.API.getDocument_No(readerIndex, false));
				data.setSerialNo(KWTCardReaderService.API.getCard_Serial_No(readerIndex, false));

				data.setMoiReferenceIndicator(KWTCardReaderService.API.getMOI_Refernce_Indic(readerIndex, false));
				data.setMoiReference(KWTCardReaderService.API.getMOI_Reference(readerIndex, false));

				// Address
				data.setDistrict(KWTCardReaderService.API.getDestrict(readerIndex, false));
				data.setBlock(KWTCardReaderService.API.getBlock_No(readerIndex, false));
				data.setStreet(KWTCardReaderService.API.getStreet_Name(readerIndex, false));
				data.setBuildNo(KWTCardReaderService.API.getBuilding_Plot_No(readerIndex, false));
				data.setUnitType(KWTCardReaderService.API.getUnit_type(readerIndex, false));
				data.setUnitNo(KWTCardReaderService.API.getUnit_No(readerIndex, false));
				data.setFloor(KWTCardReaderService.API.getFloor_No(readerIndex, false));

				// Personal Details
				data.setBloodType(KWTCardReaderService.API.getBlood_Type(readerIndex, false));
				data.setGuardianId(KWTCardReaderService.API.getGuardian_Civil_ID_No(readerIndex, false));
				data.setTeleNo1(KWTCardReaderService.API.getTel_1(readerIndex, false));
				data.setTeleNo2(KWTCardReaderService.API.getTel_2(readerIndex, false));

				data.setEmail(KWTCardReaderService.API.getEmail_Address(readerIndex, false));
				data.setField1(KWTCardReaderService.API.getAdditional_F1(readerIndex, false));
				data.setField2(KWTCardReaderService.API.getAdditional_F2(readerIndex, false));
				data.setAddressUKey(KWTCardReaderService.API.getAddress_Unique_Key(readerIndex, false));

				String dob = KWTCardReaderService.API.getBirth_Date(readerIndex, false);
				if (dob != null && dob.length() > 0 && dob.length() > 4) {
					dob = dob.subSequence(0, 4) + "/" + dob.subSequence(4, 6) + "/" + dob.substring(6);
				}

				data.setDob(dob);

				String issueDate = KWTCardReaderService.API.getCard_Issue_Date(readerIndex, false);
				if (issueDate != null && issueDate.length() > 0 && issueDate.length() > 4) {
					issueDate = issueDate.substring(0, 4) + "/" + issueDate.substring(4, 6) + "/"
							+ issueDate.substring(6);
				}
				data.setIssueDate(issueDate);

				// String expiryDate = KWTCardReaderService.API.ReadCardInfo(readerIndex,
				// pacicardlibrary.PACICardAPI.DataType.CardExpiryDate, true);
				String expiryDate = KWTCardReaderService.API.getCard_Expiry_Date(readerIndex, false);
				if (expiryDate != null && expiryDate.length() > 0 && expiryDate.length() > 4) {
					expiryDate = expiryDate.substring(0, 4) + "/" + expiryDate.substring(4, 6) + "/"
							+ expiryDate.substring(6);
				}

				data.setExpiryDate(expiryDate);
				data.setGenuine(KWTCardReaderService.API.ValidateCardIsGenuine(readerIndex));
				data.setValid(KWTCardReaderService.API.ValidateCardCertificate(readerIndex, false, true, true));
				KWTCardReaderService.CONTEXT.push(data);
				KWTCardReaderService.CONTEXT.status(CardStatus.SCANNED);
			} catch (PaciException e) {
				LOGGER.error("KwCardReaderListner:CardConnectionEvent:PaciException {}", e);
				KWTCardReaderService.CONTEXT.status(DataStatus.READ_ERROR);
			} catch (Exception e2) {
				LOGGER.error("KwCardReaderListner:CardConnectionEvent:Exception {}", e2);
				KWTCardReaderService.CONTEXT.status(DataStatus.READ_ERROR);
			}
			reading = false;
		}

	}

	@Override
	public void CardDisconnectionEvent(int arg0) {
		if (!enabled) {
			return;
		}
		LOGGER.debug("KWTCardReaderServiceListner:CardDisconnectionEvent");
		KWTCardReaderService.CONTEXT.status(CardStatus.REMOVED);
	}

	@Override
	public void ReaderChangeEvent() {
		if (!enabled) {
			return;
		}
		LOGGER.debug("KWTCardReaderServiceListner:ReaderChangeEvent");
		if (!reading && readername == null) {
			readername = KWTCardReaderService.API.GetReaders()[0];
		}
		KWTCardReaderService.CONTEXT.pong(readername);
		if (h == null) {
			h = new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread th, Throwable ex) {
					LOGGER.error("Uncaught exception: ", ex);
					KWTCardReaderService.CONTEXT.status(CardStatus.ERROR);
				}
			};
			Thread.currentThread().setUncaughtExceptionHandler(h);
		}

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
