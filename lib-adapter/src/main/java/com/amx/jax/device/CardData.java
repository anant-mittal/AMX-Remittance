package com.amx.jax.device;

import java.io.Serializable;

import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardData implements Serializable {

	private static final long serialVersionUID = -3850651340742417281L;
	long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	boolean valid;
	boolean expired;
	boolean genuine;
	private String title;
	private String name1;
	private String name2;
	private String name4;
	private String name3;
	private String identity;
	private String gender;
	private String nationality;
	private String dob;
	private String issueDate;
	private String expiryDate;
	private String documentNo;
	private String serialNo;
	private String moiReference;
	private String moiReferenceIndicator;
	private String district;
	private String block;
	private String street;
	private String buildNo;
	private String unitType;
	private String unitNo;
	private String floor;
	private String bloodType;
	private String guardianId;
	private String teleNo1;
	private String teleNo2;
	private String email;
	private String localFullName;
	private String localName1;
	private String localName2;
	private String localName3;
	private String localName4;
	private String localNationality;
	private String localGender;
	private String field1;
	private String field2;
	private String addressUKey;
	private String localUnitType;
	private String fullName;
	private int photoLength = 0;
	private String info;

	public CardData() {
		this.timestamp = System.currentTimeMillis();
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isGenuine() {
		return genuine;
	}

	public boolean isEmpty() {
		return ArgUtil.isEmpty(identity);
	}

	public void setGenuine(boolean genuine) {
		this.genuine = genuine;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String fName) {
		this.name1 = fName;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String mName) {
		this.name2 = mName;
	}

	public String getName4() {
		return name4;
	}

	public void setName4(String sName) {
		this.name4 = sName;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String gfName) {
		this.name3 = gfName;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String civilId) {
		this.identity = civilId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String docNo) {
		this.documentNo = docNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serilaNo) {
		this.serialNo = serilaNo;
	}

	public String getMoiReference() {
		return moiReference;
	}

	public void setMoiReference(String moiRefer) {
		this.moiReference = moiRefer;
	}

	public String getMoiReferenceIndicator() {
		return moiReferenceIndicator;
	}

	public void setMoiReferenceIndicator(String moiRefeInicator) {
		this.moiReferenceIndicator = moiRefeInicator;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getGuardianId() {
		return guardianId;
	}

	public void setGuardianId(String guardianCivilId) {
		this.guardianId = guardianCivilId;
	}

	public String getTeleNo1() {
		return teleNo1;
	}

	public void setTeleNo1(String teleNo1) {
		this.teleNo1 = teleNo1;
	}

	public String getTeleNo2() {
		return teleNo2;
	}

	public void setTeleNo2(String teleNo2) {
		this.teleNo2 = teleNo2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocalFullName() {
		return localFullName;
	}

	public void setLocalFullName(String arFullName) {
		this.localFullName = arFullName;
	}

	public String getLocalName1() {
		return localName1;
	}

	public void setLocalName1(String arFirstName) {
		this.localName1 = arFirstName;
	}

	public String getLocalName2() {
		return localName2;
	}

	public void setLocalName2(String arFatherName) {
		this.localName2 = arFatherName;
	}

	public String getLocalName3() {
		return localName3;
	}

	public void setLocalName3(String arGFName) {
		this.localName3 = arGFName;
	}

	public String getLocalName4() {
		return localName4;
	}

	public void setLocalName4(String arSName) {
		this.localName4 = arSName;
	}

	public String getLocalNationality() {
		return localNationality;
	}

	public void setLocalNationality(String arNationality) {
		this.localNationality = arNationality;
	}

	public String getLocalGender() {
		return localGender;
	}

	public void setLocalGender(String arGender) {
		this.localGender = arGender;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String addiFiled1) {
		this.field1 = addiFiled1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String addiField2) {
		this.field2 = addiField2;
	}

	public String getAddressUKey() {
		return addressUKey;
	}

	public void setAddressUKey(String addressUKey) {
		this.addressUKey = addressUKey;
	}

	public String getLocalUnitType() {
		return localUnitType;
	}

	public void setLocalUnitType(String arUnitType) {
		this.localUnitType = arUnitType;
	}

	public void setFullName(String enFullName) {
		this.fullName = enFullName;
	}

	public int getPhotoLength() {
		return photoLength;
	}

	public void setPhotoLength(int photoLength) {
		this.photoLength = photoLength;
	}

	public String toString() {
		return (new StringBuilder("Arabic Full Name=")).append(localFullName).append("#Arabic First Name=")
				.append(localName1).append("#Arabic Father Name=").append(localName2).append("#Arabic GF Name=")
				.append(localName3).append("#Arabic Surname=").append(localName4).append("#Nationality in Arabic=")
				.append(localNationality).append("#Gender in Arabic=").append(localGender).append("#Name=")
				.append(fullName).append("#CivilId=").append(identity).append("#Gender=").append(gender)
				.append("#Nationality=").append(nationality).append("#BirthDate=").append(dob).append("#IssueDate=")
				.append(issueDate).append("#ExpiryDate=").append(expiryDate).append("#Documnet No=").append(documentNo)
				.append("#Serial No=").append(serialNo).append("#MOI Reference=").append(moiReference)
				.append("#MOI Reference Indicator=").append(moiReferenceIndicator).append("#District=").append(district)
				.append("#Block=").append(block).append("#Street=").append(street).append("#Building No=")
				.append(buildNo).append("#Unit Type=").append(unitType).append("#Unit No.=").append(unitNo)
				.append("#Floor=").append(floor).append("#Blood Type=").append(bloodType).append("#Guardian Civil ID=")
				.append(guardianId).append("#Telephone1=").append(teleNo1).append("#Telephone2=").append(teleNo2)
				.append("#E-Mail address=").append(email).append("#Additional filed1=").append(field1)
				.append("#Additional field2=").append(field2).append("#Address Unique key=").append(addressUKey)
				.append("#LATIN-NAME-1=").append(name1).append("#LATIN-NAME-2=").append(name2).append("#LATIN-NAME-4=")
				.append(name3).append("#LATIN-NAME-3=").append(name4).append("#Photo Length=").append(photoLength)
				.toString();
	}

	public String getFullName() {
		return fullName;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
