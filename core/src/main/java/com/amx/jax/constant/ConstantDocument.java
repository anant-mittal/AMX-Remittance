package com.amx.jax.constant;

import java.math.BigDecimal;

public class ConstantDocument {

	public static final BigDecimal DOCUMENT_CODE_FOR_COLLECT_TRANSACTION = new BigDecimal(2);
	public static final BigDecimal DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION = new BigDecimal(1);
	public static final String No = "N";
	public static final String Yes = "Y";
	public static final String Deleted = "D";
	public static final String Update = "U";
	// document id
	public static final BigDecimal APPLICATION_DOCUMENT_ID = new BigDecimal(1);
	public static final BigDecimal COLLECTION_DOCUMENT_ID = new BigDecimal(2);
	public static final BigDecimal REMITTANCE_DOCUMENT_ID = new BigDecimal(3);
	// document codes
	public static final BigDecimal DOCUMENT_CODE_CUSTOMER_SERIAL_NUMBER = new BigDecimal(8);
	// peronsl remittancew
	public static final String Individual = "I";
	public static final String Online = "O";
	public static final String INDIVIDUAL_STRING = "Individual";
	public static final String NON_INDIVIDUAL_STRING = "Non-Individual";

	public static final String VOCHERCODE = new String("V");
	public static final String CR = "\u062C.\u0635.";
	public static final String Share_Capital = "\u0631\u0623\u0633 \u0627\u0644\u0645\u0627\u0644";
	// REMIITANCE AMIEC CODE
	public static final String AMIEC_CODE = "9999999";
	public static final String INDIC1 = new String("INDIC1");
	public static final String INDIC2 = new String("INDIC2");
	public static final String INDIC3 = new String("INDIC3");
	public static final String INDIC4 = new String("INDIC4");
	public static final String INDIC5 = new String("INDIC5");
	public static final String CAPTURED = "CAPTURED";
	public static final String APPROVED = "APPROVED";
	public static final String WU = "WU";
	// Money Gram
	public static final String MONEY = "MONEY";
	public static final BigDecimal CONTACT_TYPE_FOR_LOCAL = new BigDecimal(49);
	public static final BigDecimal CONTACT_TYPE_FOR_HOME = new BigDecimal(50);
	public static final BigDecimal REMITTANCE_DOCUMENT_CODE = new BigDecimal(3);

	public static final String JOAMX_USER = "JOAMX_USER";

	public static final String BANGLADESH_ALPHA3_CODE = "BGD";
	public static final String SERVICE_GROUP_CODE_CASH = "C";
	public static final String SERVICE_GROUP_CODE_BANK = "B";

	public static final String CONSTANT_ALL = "ALL";

	public static final String BANK_INDICATOR_CORRESPONDING_BANK = "CB";
	public static final String BANK_INDICATOR_BENEFICIARY_BANK = "BE";
	public static final BigDecimal REMITTANCE_MODE_EFT = new BigDecimal(3);
	public static final BigDecimal REMITTANCE_MODE_RTGS = new BigDecimal(4);

	public static final BigDecimal DELIVERY_MODE_BANKING_CHANNEL = new BigDecimal(105);
	public static final String MM_DD_YYYY_DATE_FORMAT = "MM/dd/yyyy";
	
	public static final BigDecimal SERVICE_MASTER_ID_TT = new BigDecimal(102);
	
	public static final String VOUCHER_ONLINE_PROMOTION_STR = "CHICKEN KING SAGAR VOUCHER";
	public static final BigDecimal IDENTITY_FOR_ID_PROOF = new BigDecimal(48);
	public static final BigDecimal BIZ_COMPONENT_ID_CIVIL_ID = new BigDecimal(198);
	/** Added by Rabil on 06/11/2018  FX_CD-Currency Denomination and FX_DC Delivery Charges **/ 
	public static final String FX_CD = "FXCD";
	public static final String FX_DC = "FXDC";	
	public static final String FX_AD = "FXAD";
	/** LOA -Local Office Address ,LHA -Local home Address **/
	public static final String FX_LHA = "LHA";
	public static final String S = "S";	
	public static final BigDecimal DOCUMENT_CODE_FOR_FCSALE_APPLICATION =new BigDecimal(1);
	public static final BigDecimal DOCUMENT_CODE_FOR_FCSALE = new BigDecimal(74);
	public static final String PR = "PR";
	public static final String FS = "FS";
	public static final String FP = "FP";
	public static final BigDecimal ONLINE_BRANCH_LOC_CODE = new BigDecimal(90);
	public static final String FC_SALE_RECEIPT_TYPE="01";
	public static final String COLLECTION_RECEIPT_TYPE="70";
	public static final String KNET_CODE="K";
	public static final String T="T";
	// ORD for --Ordered
	public static final String ORD="ORD";
	// DVD for --Delivered
	public static final String DVD="DVD";
	// RTD for --returned
	public static final String RTD="RTD";
	// ACP for --Accepted
	public static final String ACP="ACP";
	public static final String RTD_ACK="RTD_ACK";
	// CND for --cancelled
	public static final String CND="CND";
	// OFD for --out for delivery
	public static final String OFD="OFD";
	
	// OFD_ACK for --Out for delivery pending acknowledgment
	public static final String OFD_ACK="OFD_ACK";
	// PCK for --PACKED
	public static final String PCK="PCK";
	public static final String OFD_CNF="OFD_CNF";
	public static final String CND_ACK="CND_ACK";
	
	/** end Here **/
	public static final String P = "P";	
	public static final String FC_SALE = "FC Sale";
	public static final String USER_TYPE_DRIVER = "D";

	public static final BigDecimal BIZ_COMPONENT_ID_GCC_ID = new BigDecimal(201);
	public static final BigDecimal BIZ_COMPONENT_ID_BEDOUIN_ID = new BigDecimal(197);

}
