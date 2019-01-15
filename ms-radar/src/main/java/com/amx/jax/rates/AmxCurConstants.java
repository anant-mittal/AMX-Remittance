package com.amx.jax.rates;

public class AmxCurConstants {

	public static enum RCur {
		AFA, ALL, AZN, DZD, USD, ESP, FRF, ADP, AOA, XCD, ARS, AMD, AWG, AUD, ATS, AZM, BSD, BHD, BDT, BBD, BYB, RYR,
		BEF,
		BZD, XOF, BMD, INR, BTN, BOB, BOV, BAM, BWP, NOK, BRL, BND, BGL, BGN, BIF, KHR, XAF, CAD, CVE, KYD,
		CLP, CLF, CNY, HKD, MOP, COP, KMF, CDF, NZD, CRC, HRK, CUP, CYP, CZK, DKK, DJF, DOP,
		TPE, IDE, ECS, ECV, EGP, SVC, ERN, EEK, ETB, XEU, EUR, FKP, FJD, FIM, XPF, GMD, GEL,
		DEM, GHC, GIP, GRD, GTQ, GNF, GWP, GYD, HTG, ITL, HNL, HUF, ISK, IDR, XDR,
		IRR, IQD, IEP, ILS, JMD, JPY, JOD, KZT, KES, KPW, KRW, KWD, KGS, LAK, LVL, LBP, ZAR, LSL, LRD, LYD, CHF,
		LTL, LUF, MKD, MGF, MWK, MYR, MVR, MTL, MRO, MUR, MXN, MXV, MDL, MNT, MAD, MZM, MMK,
		NAD, NPR, ANG, NLG, NIO, NGN, OMR, PKR, PAB, PGK, PYG, PEN, PHP, PLN, PTE, QAR, ROL, RUR, RUB, RWF, SHP, WST,
		STD,
		SAR, SCR, SLL, SGD, SKK, SIT, SBD, SOS, LKR, SDP, SRG, SZL, SEK, SYP, TWD, TJR, TZS, THB, TOP, TTD, TRY,
		TND, TRL, TMM, UGX, UAH, AED, GBP, USS, USN, UYU, UZS, VUV, VEB, VND, YER,
		YUN, ZRN, ZMK, ZWD, UNKNOWN;
	}

	public static enum RType {
		BUY_CASH, SELL_CASH, SELL_TRNSFR,
	}

	public static enum RSource {
		AMANKUWAIT, BECKWT, MUZAINI, UAEXCHANGE, AMX
	}

	public static final int INTERVAL_SEC = 1000;
	public static final int INTERVAL_MIN = 60 * INTERVAL_SEC;
	public static final int INTERVAL_HRS = 60 * INTERVAL_MIN;

	public static final int INTERVAL_MIN_30 = 30 * 60 * 1000;
	public static final int INTERVAL_MIN_10 = 10 * 60 * 1000;
	public static final int INTERVAL_TEST = 2 * 1000;
	public static final int INTERVAL_TASK = 30 * 1000;
}
