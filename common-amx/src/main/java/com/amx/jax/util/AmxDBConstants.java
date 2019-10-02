package com.amx.jax.util;

import java.math.BigDecimal;

public class AmxDBConstants {
	public static final BigDecimal DOCUMENT_CODE_FOR_COLLECT_TRANSACTION = new BigDecimal(2);
	public static final BigDecimal DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION = new BigDecimal(1);
	public static final String No = "N";
	public static final String Yes = "Y";
	public static final String Deleted = "D";
	public static final String Update = "U";
	public static final String Black = "B";
	public static final String Compliance = "C";

	public static enum Status {
		Y, N, D, U, B, C, V
	}

	// document id
	public static final BigDecimal APPLICATION_DOCUMENT_ID = new BigDecimal(1);
	public static final BigDecimal COLLECTION_DOCUMENT_ID = new BigDecimal(2);
	public static final BigDecimal REMITTANCE_DOCUMENT_ID = new BigDecimal(3);

	// document codes
	public static final BigDecimal DOCUMENT_CODE_CUSTOMER_SERIAL_NUMBER = new BigDecimal(8);
	// peronsl remittancew
	public static final String Individual = "I";
	public static final String Non_Individual = "C";
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
	public static final BigDecimal VOUCHER_DOCUMENT_CODE = new BigDecimal(69);

	public static final String JOAMX_USER = "JOAMX_USER";

	public static final String BANGLADESH_ALPHA3_CODE = "BGD";
	public static final String SERVICE_GROUP_CODE_CASH = "C";
	public static final String SERVICE_GROUP_CODE_BANK = "B";

	public static final String CONSTANT_ALL = "ALL";

	public static final String BANK_INDICATOR_CORRESPONDING_BANK = "CB";
	public static final String BANK_INDICATOR_BENEFICIARY_BANK = "BE";
	public static final String BANK_INDICATOR_SERVICE_PROVIDER_BANK = "SB";
	public static final BigDecimal REMITTANCE_MODE_EFT = new BigDecimal(3);
	public static final BigDecimal REMITTANCE_MODE_RTGS = new BigDecimal(4);

	public static final BigDecimal DELIVERY_MODE_BANKING_CHANNEL = new BigDecimal(105);
	public static final String MM_DD_YYYY_DATE_FORMAT = "MM/dd/yyyy";

	public static final BigDecimal SERVICE_MASTER_ID_EFT = new BigDecimal(101);
	public static final BigDecimal SERVICE_MASTER_ID_TT = new BigDecimal(102);
	public static final BigDecimal SERVICE_MASTER_ID_DD = new BigDecimal(104);
	public static final BigDecimal SERVICE_MASTER_ID_CASH = new BigDecimal(103);


	public static final String VOUCHER_ONLINE_PROMOTION_STR = "CHICKEN KING SAGAR VOUCHER";
	public static final BigDecimal IDENTITY_FOR_ID_PROOF = new BigDecimal(48);
	public static final BigDecimal BIZ_COMPONENT_ID_CIVIL_ID = new BigDecimal(198);
	/**
	 * Added by Rabil on 06/11/2018 FX_CD-Currency Denomination and FX_DC Delivery
	 * Charges
	 **/
	public static final String FX_CD = "FXCD";
	public static final String FX_DC = "FXDC";
	public static final String FX_AD = "FXAD";
	/** LOA -Local Office Address ,LHA -Local home Address **/
	public static final String FX_LHA = "LHA";
	public static final String FX_LOA = "LOA";
	public static final String S = "S";
	public static final BigDecimal DOCUMENT_CODE_FOR_FCSALE_APPLICATION = new BigDecimal(1);
	public static final BigDecimal DOCUMENT_CODE_FOR_FCSALE = new BigDecimal(74);
	public static final String PR = "PR";
	public static final String FS = "FS";
	public static final String FP = "FP";
	public static final BigDecimal ONLINE_BRANCH_LOC_CODE = new BigDecimal(90);
	public static final String FC_SALE_RECEIPT_TYPE = "01";
	public static final String COLLECTION_RECEIPT_TYPE = "70";
	public static final String KNET_CODE = "K";
	public static final String T = "T";
	// ORD for --Ordered
	public static final String ORD = "ORD";
	// DVD for --Delivered
	public static final String DVD = "DVD";
	// RTD for --returned
	public static final String RTD = "RTD";
	// ACP for --Accepted
	public static final String ACP = "ACP";
	public static final String RTD_ACK = "RTD_ACK";
	// CND for --cancelled
	public static final String CND = "CND";
	// OFD for --out for delivery
	public static final String OFD = "OFD";

	// OFD_ACK for --Out for delivery pending acknowledgment
	public static final String OFD_ACK = "OFD_ACK";
	// PCK for --PACKED
	public static final String PCK = "PCK";
	public static final String OFD_CNF = "OFD_CNF";
	public static final String CND_ACK = "CND_ACK";

	/** end Here **/
	public static final String P = "P";
	public static final String R = "R";
	public static final String FC_SALE = "FC Sale";
	public static final String USER_TYPE_DRIVER = "D";

	public static final BigDecimal BIZ_COMPONENT_ID_GCC_ID = new BigDecimal(201);
	public static final BigDecimal BIZ_COMPONENT_ID_BEDOUIN_ID = new BigDecimal(197);
	public static final BigDecimal MURQAB_FOREIGNCURRENCY = new BigDecimal(89);
	public static final BigDecimal KUWAIT_FOREIGNCURRENCY = new BigDecimal(89);
	public static final BigDecimal OMAN_FOREIGNCURRENCY = new BigDecimal(99);
	public static final BigDecimal BAHRAIN_FOREIGNCURRENCY = new BigDecimal(70);
	public static final BigDecimal BIZ_COMPONENT_ID_PASSPORT = new BigDecimal(204);
	

	public static final String ARTICLE_20_CODE = "20";
	public static final BigDecimal BIZ_COMPONENT_ID_NEW_CIVIL_ID = new BigDecimal(2000);
	public static final String CASH = "C";
	public static final String BANK_TRANSFER = "T";
	public static final String CHEQUE = "B";
	public static final String OTHER = "O";
	public static final String BRANCH = "BRANCH";
	public static final String PARAM_POS_BANK = "BPOS";
	public static final String COUNTER = "C";

	public static final String DECL_REPORT_FOR_CASH = "14";
	public static final String DECL_REPORT_FOR_TOT_AMOUNT = "15";
	public static final String FC_DECL_REPORT_FOR_TOT_AMOUNT = "39";
	public static final String FC_SALE_REMIT = "FC Sale Remmit";
	/** for Cash collection **/
	public static final String C = "C";
	/** for Cash Refund **/
	public static final String F = "F";
	public static final String FILE_CREATION = "F";
	public static final String WEB_SERVICE = "W";
	public static final String A = "A";
	public static final String CLAIM = "CLAIM";
	public static final String VOUCHER = "V";
	public static final String IMPS_CODE = "13";
	public static final String IND_COUNTRY_CODE = "004";

	/** E-Eng,A-Arabic **/
	public static final String L_ENG = "1";
	public static final String L_ARAB = "2";
	public static final String BNFBANK = "BNFBANK";
	public static final String BNFBRCH = "BNFBRCH";
	public static final String BNFBANK_SWIFT = "BNFBANK_SWIFT";
	
	public static final String HOME_SEND_PAYMENT_TYPE_CASH = "CASH";
	public static final String HOME_SEND_PAYMENT_TYPE_KNET = "CARD";
	public static final String HOME_SEND_PAYMENT_TYPE_BANK_TRANSFER = "BANK";
	public static final String HOME_SEND_PAYMENT_TYPE_CHEQUE = "CHEQUE";
	
   // Constant for Article detail id "Others"
	
	public static final BigDecimal ARTICLE_DETAIL_ID_OTHERS = new BigDecimal(16);
	public static final String  VAT_ACCOUNT_TYPE_COMM = "COMMISSION";
	public static final String  VAT_CALCULATION_TYPE_INCLUDE= "I";
	public static final String  VAT_CALCULATION_TYPE_EXCLUDE="E";
	public static final String  VAT_CATEGORY= "OUTPUT_TAX";
	public static final String  BENE_ACCT_VALID="BENE_ACCT_VALID";
	
	// Constants for Serviceprovider
	
	public static final String TOTAL_PAY_INDICATOR  = "T";

	// Constants for annual income
	public static final String ANNUAL_INCOME_RANGE = "AIR";
	
	// Constants for annual transaction limit range
	
	public static final String ANNUAL_TRANSACTION_LIMIT = "ATL";
	public static final long MILLISEC_IN_YEAR = 31540000000L;
	
	public static final String  COMM_INCLUDE= "I";
	public static final String COMM_EXCLUDE="E";
	
	public static final String BPI_GIFT="GIFT";
	
	public static final String TELEX_TRANFER = "T";
	public static final String DEMAND_DRAFT = "D";

	public static final BigDecimal EXCHANGE_RATE_DECIMAL = new BigDecimal(9);
	// Wire transfer status constants
	

	public static final String WU_PAID = "PAID";
	public static final String WU_PICK = "PICK_REMINDER";
	public static final String WU_CANC_REM = "CANC_REMINDER";
	public static final String WU_CANCELLED = "WU_CANC";

	public static final String PB_STATUS_NEW = "NEW";
	public static final String WT_STATUS_CONFIRM = "CONF";
	public static final String WT_STATUS_CANCELLED = "CANC";
	public static final String WT_STATUS_PAID  ="PAID";
	public static final String PB_PAYMENT="PB";
	
}
