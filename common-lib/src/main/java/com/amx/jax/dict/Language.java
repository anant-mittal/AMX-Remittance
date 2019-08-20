package com.amx.jax.dict;

import java.math.BigDecimal;

public enum Language {

	AB("abk", 1, "Abkhazian"),
	AE("ave", 1, "Avestan"),
	AF("afr", 1, "Afrikaans"),
	AK("aka", 1, "Akan"),
	AM("amh", 1, "Amharic"),
	AN("arg", 1, "Aragonese"),
	AR("ara", 2, "Arabic"),
	AS("asm", 1, "Assamese"),
	AV("ava", 1, "Avaric"),
	AY("aym", 1, "Aymara"),
	AZ("aze", 1, "Azerbaijani"),

	BA("bak", 1, "Bashkir"),
	BE("bel", 1, "Belarusian"),
	BG("bul", 1, "Bulgarian"),
	BH("bih", 1, "Bihari"),
	BI("bis", 1, "Bislama"),

	BM("bam", 1, "Bambara"),

	BN("ben", 1, "Bengali"),
	BO("bod", 1, "Tibetan"),
	BR("bre", 1, "Breton"),
	BS("bos", 1, "Bosnian"),

	CA("cat", 1, "Catalan"),
	CE("che", 1, "Chechen"),
	CH("cha", 1, "Chamorro"),
	CO("cos", 1, "Corsican"),

	CR("cre", 1, "Cree"),
	CS("ces", 1, "Czech"),
	CU("chu", 1, "Church Slavic"),
	CV("chv", 1, "Chuvash"),
	CY("cym", 1, "Welsh"),

	DA("dan", 1, "Danish"),
	DE("deu", 1, "German"),
	DV("div", 1, "Divehi"),
	DZ("dzo", 1, "Dzongkha"),

	EE("ewe", 1, "Ewe"),
	EL("ell", 1, "Greek"),
	EN("eng", 1, "English"),
	EO("epo", 1, "Esperanto"),
	ES("spa", 1, "Spanish"),

	ET("est", 1, "Estonian"),
	EU("eus", 1, "Basque"),

	FA("fas", 1, "Persian"),
	FF("ful", 1, "Fulah"),
	FI("fin", 1, "Finnish"),
	FJ("fij", 1, "Fijian"),
	FO("fao", 1, "Faroese"),

	FR("fra", 1, "French"),
	FY("fry", 1, "Frisian"),

	GA("gle", 1, "Irish"),
	GD("gla", 1, "Scottish Gaelic"),
	GL("glg", 1, "Gallegan"),
	GN("grn", 1, "Guarani"),
	GU("guj", 1, "Gujarati"),

	GV("glv", 1, "Manx"),

	HA("hau", 1, "Hausa"),
	HI("hin", 1, "Hindi"),
	HO("hmo", 1, "Hiri Motu"),
	HR("hrv", 1, "Croatian"),
	HT("hat", 1, "Haitian"),

	HU("hun", 1, "Hungarian"),
	HY("hye", 1, "Armenian"),
	HZ("her", 1, "Herero"),

	IA("ina", 1, "Interlingua"),
	IN("ind", 1, "Indonesian"),
	IE("ile", 1, "Interlingue"),
	IG("ibo", 1, "Igbo"),

	II("iii", 1, "Sichuan Yi"),
	IK("ipk", 1, "Inupiaq"),
	IO("ido", 1, "Ido"),

	IS("isl", 1, "Icelandic"),
	IT("ita", 1, "Italian"),
	IU("iku", 1, "Inuktitut"),
	IW("heb", 1, "Hebrew"),

	JA("jpn", 1, "Japanese"),
	JI("yid", 1, "Yiddish"),
	JV("jav", 1, "Javanese"),

	KA("kat", 1, "Georgian"),
	KG("kon", 1, "Kongo"),
	KI("kik", 1, "Kikuyu"),
	KJ("kua", 1, "Kwanyama"),

	KK("kaz", 1, "Kazakh"),
	KL("kal", 1, "Greenlandic"),
	KM("khm", 1, "Khmer"),
	KN("kan", 1, "Kannada"),

	KO("kor", 1, "Korean"),
	KR("kau", 1, "Kanuri"),
	KS("kas", 1, "Kashmiri"),
	KU("kur", 1, "Kurdish"),

	KV("kom", 1, "Komi"),
	KW("cor", 1, "Cornish"),
	KY("kir", 1, "Kirghiz"),

	LA("lat", 1, "Latin"),
	LB("ltz", 1, "Luxembourgish"),
	LG("lug", 1, "Ganda"),
	LI("lim", 1, "Limburgish"),

	LN("lin", 1, "Lingala"),
	LO("lao", 1, "Lao"),
	LT("lit", 1, "Lithuanian"),
	LU("lub", 1, "Luba-Katanga"),

	LV("lav", 1, "Latvian"),

	MG("mlg", 1, "Malagasy"),
	MH("mah", 1, "Marshallese"),
	MI("mri", 1, "Maori"),
	MK("mkd", 1, "Macedonian"),

	ML("mal", 1, "Malayalam"),
	MN("mon", 1, "Mongolian"),
	MO("mol", 1, "Moldavian"),
	MR("mar", 1, "Marathi"),

	MS("msa", 1, "Malay"),
	MT("mlt", 1, "Maltese"),
	MY("mya", 1, "Burmese"),

	NA("nau", 1, "Nauru"),
	NB("nob", 1, "Norwegian Bokmål"),
	ND("nde", 1, "North Ndebele"),
	NE("nep", 1, "Nepali"),

	NG("ndo", 1, "Ndonga"),
	NL("nld", 1, "Dutch"),
	NN("nno", 1, "Norwegian Nynorsk"),
	NO("nor", 1, "Norwegian"),

	NR("nbl", 1, "South Ndebele"),
	NV("nav", 1, "Navajo"),
	NY("nya", 1, "Nyanja"),

	OC("oci", 1, "Occitan"),
	OJ("oji", 1, "Ojibwa"),
	OM("orm", 1, "Oromo"),
	OR("ori", 1, "Oriya"),
	OS("oss", 1, "Ossetian"),

	PA("pan", 1, "Panjabi"),
	PI("pli", 1, "Pali"),
	PL("pol", 1, "Polish"),
	PS("pus", 1, "Pushto"),
	PT("por", 1, "Portuguese"),

	QU("que", 1, "Quechua"),
	RM("roh", 1, "Raeto-Romance"),
	RN("run", 1, "Rundi"),
	RO("ron", 1, "Romanian"),
	RU("rus", 1, "Russian"),

	RW("kin", 1, "Kinyarwanda"),

	SA("san", 1, "Sanskrit"),
	SC("srd", 1, "Sardinian"),
	SD("snd", 1, "Sindhi"),
	SE("sme", 1, "Northern Sami"),

	SG("sag", 1, "Sango"),
	SI("sin", 1, "Sinhalese"),
	SK("slk", 1, "Slovak"),
	SL("slv", 1, "Slovenian"),

	SM("smo", 1, "Samoan"),
	SN("sna", 1, "Shona"),
	SO("som", 1, "Somali"),
	SQ("sqi", 1, "Albanian"),
	SR("srp", 1, "Serbian"),

	SS("ssw", 1, "Swati"),
	ST("sot", 1, "Southern Sotho"),

	SU("sun", 1, "Sundanese"),
	SV("swe", 1, "Swedish"),
	SW("swa", 1, "Swahili"),

	TA("tam", 1, "Tamil"),
	TE("tel", 1, "Telugu"),
	TG("tgk", 1, "Tajik"),
	TH("tha", 1, "Thai"),
	TI("tir", 1, "Tigrinya"),

	TK("tuk", 1, "Turkmen"),
	TL("tgl", 1, "Tagalog"),
	TN("tsn", 1, "Tswana"),

	TO("ton", 1, "Tonga"),
	TR("tur", 1, "Turkish"),
	TS("tso", 1, "Tsonga"),
	TT("tat", 1, "Tatar"),
	TW("twi", 1, "Twi"),

	TY("tah", 1, "Tahitian"),

	UG("uig", 1, "Uighur"),
	UK("ukr", 1, "Ukrainian"),
	UR("urd", 1, "Urdu"),
	UZ("uzb", 1, "Uzbek"),

	VE("ven", 1, "Venda"),
	VI("vie", 1, "Vietnamese"),
	VO("vol", 1, "Volapük"),

	WA("wln", 1, "Walloon"),
	WO("wol", 1, "Wolof"),

	XH("xho", 1, "Xhosa"),
	YO("yor", 1, "Yoruba"),
	ZA("zha", 1, "Zhuang"),
	ZH("zho", 1, "Chinese"),
	ZU("zul", 1, "Zulu");

	public static final Language DEFAULT = EN;

	private String iso3code;
	private int id;

	Language(String iso3code, int id, String name) {
		this.id = id;
		this.iso3code = iso3code;
	}

	public String getISO3Code() {
		return iso3code;
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return this.toString();
	}

	public BigDecimal getBDCode() {
		return new BigDecimal(id);
	}

}
