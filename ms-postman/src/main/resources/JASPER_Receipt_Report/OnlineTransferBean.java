package com.amg.exchange.online.remittance.bean;

/**
 * Author :Rabil 
 * Date   : 21/06/2016
 */
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amg.exchange.beneficiary.model.BankBranchView;
import com.amg.exchange.beneficiary.service.IBeneficaryCreation;
import com.amg.exchange.common.model.ApplicationSetup;
import com.amg.exchange.common.model.CityMasterDesc;
import com.amg.exchange.common.model.CompanyMaster;
import com.amg.exchange.common.model.CompanyMasterDesc;
import com.amg.exchange.common.model.CountryMaster;
import com.amg.exchange.common.model.CountryMasterDesc;
import com.amg.exchange.common.model.CurrencyMaster;
import com.amg.exchange.common.model.CutomerDetailsView;
import com.amg.exchange.common.model.DistrictMasterDesc;
import com.amg.exchange.common.model.StateMasterDesc;
import com.amg.exchange.common.service.IGeneralService;
import com.amg.exchange.foreigncurrency.model.PurposeOfTransaction;
import com.amg.exchange.foreigncurrency.model.SourceOfIncomeDescription;
import com.amg.exchange.foreigncurrency.model.UserFinancialYear;
import com.amg.exchange.foreigncurrency.service.ForeignLocalCurrencyDenominationService;
import com.amg.exchange.foreigncurrency.service.IForeignCurrencyPurchaseService;
import com.amg.exchange.mail.ApplicationMailer;
import com.amg.exchange.online.bean.AccountCurrencyView;
import com.amg.exchange.online.bean.UserProfile;
import com.amg.exchange.online.common.model.CountryMasterView;
import com.amg.exchange.online.common.model.EmailNotification;
import com.amg.exchange.online.common.model.OnlineConfiguration;
import com.amg.exchange.online.remittance.service.IPersonalRemittanceService;
import com.amg.exchange.online.remittance.service.IServiceGroupMasterService;
import com.amg.exchange.registration.model.CountryBranch;
import com.amg.exchange.registration.model.Customer;
import com.amg.exchange.registration.model.Employee;
import com.amg.exchange.remittance.model.AccountTypeFromView;
import com.amg.exchange.remittance.model.AdditionalBankDetailsView;
import com.amg.exchange.remittance.model.AdditionalBankRuleMap;
import com.amg.exchange.remittance.model.AdditionalDataDisplayView;
import com.amg.exchange.remittance.model.AdditionalInstructionData;
import com.amg.exchange.remittance.model.AuthicationLimitCheckView;
import com.amg.exchange.remittance.model.BeneficaryAccount;
import com.amg.exchange.remittance.model.BeneficaryContact;
import com.amg.exchange.remittance.model.BeneficaryMaster;
//import com.amg.exchange.remittance.bean.PersonalRemmitanceBeneficaryDataTable;
//import com.amg.exchange.remittance.model.BeneficaryContact;
import com.amg.exchange.remittance.model.BenificiaryListView;
import com.amg.exchange.remittance.model.BenificiaryListViewForOnline;
import com.amg.exchange.remittance.model.CollectionDetailView;
import com.amg.exchange.remittance.model.CollectionPaymentDetailsView;
import com.amg.exchange.remittance.model.CustomerDBCardDetailsView;
import com.amg.exchange.remittance.model.PurposeOfRemittanceView;
import com.amg.exchange.remittance.model.RemittanceAppBenificiary;
import com.amg.exchange.remittance.model.RemittanceApplication;
import com.amg.exchange.remittance.model.RemittanceApplicationView;
import com.amg.exchange.remittance.model.ShoppingCartDetails;
import com.amg.exchange.remittance.model.SwiftMasterView;
import com.amg.exchange.treasury.model.BankBranch;
import com.amg.exchange.treasury.model.BankMaster;
import com.amg.exchange.treasury.model.DeliveryMode;
import com.amg.exchange.treasury.model.Document;
import com.amg.exchange.treasury.model.RemittanceModeMaster;
import com.amg.exchange.treasury.model.ServiceGroupMasterDesc;
import com.amg.exchange.treasury.model.ServiceMaster;
import com.amg.exchange.treasury.model.ServiceMasterDesc;
import com.amg.exchange.util.AMGException;
import com.amg.exchange.util.Constants;
import com.amg.exchange.util.DateUtil;
import com.amg.exchange.util.GetRound;
import com.amg.exchange.util.KnetUtils;
import com.amg.exchange.util.OmanNetUtils;
/*import com.amg.exchange.remittance.bean.PopulateData;
 import com.amg.exchange.remittance.model.BeneficaryContact;
 import com.amg.exchange.remittance.model.BeneficaryMaster;*/
import com.amg.exchange.util.SessionStateManage;
import com.amg.exchange.util.WarningHandler;

@Component("onlineRemittanceBean")
@Scope("session")
public class OnlineTransferBean<T> implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	Logger log = Logger.getLogger(OnlineTransferBean.class);
	SessionStateManage sessionStateManage = new SessionStateManage();
	// customer details variables
	private String customerName;
	private String customerCrNumber;
	private BigDecimal customerNo;
	private BigDecimal customerrefno;
	private String firstName;
	private String secondName;
	private String thirdName;
	private String customerFullName;
	private String customerLocalFullName;
	private String customerIsActive;
	private Date customerExpDate;
	private String customerIdExpDate;
	private String customerExpireDateMsg;
	private String customerType;
	private BigDecimal customerTypeId;
	private BigDecimal nationality;
	private String nationalityName;
	private Date dateOfBrith;
	private String dateOfBrithStr;
	private String countryCode;
	private String mcountryCode;
	private String occupation;
	private String sourceOfIncomes;
	private String purposeOfTransactions;
	private int purposeId;
	private int sourceId;
	private BigDecimal companyId = null;
	private BigDecimal customerId = null;
	private BigDecimal beneCountr;
	private String applicationCountryName;
	private BigDecimal languageId;
	private String currentDateTime;
	private String appCountry;
	private BigDecimal applicationCountryId;
	private String companyName;
	ResourceBundle props = null;
	private FacesContext fCtx;
	HttpSession session;
	String ipAddress;
	String engName;
	private BigDecimal beneCountryId;
	private Boolean disclaimerVisible = false;
	private Boolean beneCountryOne=false;
	private Boolean beneCountryMore=false;
	private Boolean beneOne = false;
	private Boolean beneMore = false;
	private Boolean bankOne = false;
	private Boolean bankMore = false;
	private Boolean accountOne = false;
	private Boolean accountMore = false;
	//Added by Rabil on 04 Dec 2016 for Multi currency with same account number.
	
	private Boolean currOne = false;
	private Boolean currMore = false;
	
	private String beneBankName = null;
	private String beneCountryName = null;
	private String customerEmail;
	private String customerMobile;
	private String exceptionMessage;
	private String errorMessage;
	private boolean visible = false;
	private BigDecimal documentId;
	private BigDecimal documentcode;
	private String documentNo;
	private String documentdesc;
	private BigDecimal finaceYear;
	private BigDecimal finaceYearId;
	private BigDecimal serviceCode;
	private BigDecimal routingCountry;
	private String routingCountryName;
	private BigDecimal routingBank;
	private String routingBankName;
	private BigDecimal routingBranch;
	private String routingBranchName;
	private BigDecimal remitMode;
	private String remittanceName;
	private BigDecimal deliveryMode;
	private String deliveryModeInput;
	private BigDecimal currency;
	private String specialRateRef;
	private String chargesOverseas;
	private BigDecimal foriegnCurrency;
	private String availLoyaltyPoints;
	private String spotRate = Constants.No;
	// Beneficiary details from view of data table
	private BigDecimal dataCustomerContactId;
	private String dataCustomerTelephoneNumber;
	private Boolean booRenderCustTelMandatory = false;
	private Boolean booRenderCustTelDisable = true;
	private String dataTempCustomerMobile;
	private Boolean booRenderBeneTelDisable = true;
	private Boolean booRenderBeneTelMandatory = false;
	private BigDecimal dataBeneContactId;
	private BigDecimal dataTempBeneTelNum;
	// Beneficiary details from view of data table
	private BigDecimal databenificarycountry;
	private String databenificarycountryname;
	private BigDecimal dataBankbenificarycountry;
	private String dataBankbenificarycountryname;
	private BigDecimal databenificarycurrency;
	private String databenificarycurrencyname;
	private String databenificaryservice;
	private BigDecimal databenificarydelivery;
	private BigDecimal dataserviceid;
	private String dataAccountnum;
	private String databenificarybankname;
	private String databenificarybranchname;
	private String databenificaryname;
	public String databenificaryservicegroup;
	private BigDecimal dataservicegroupid;
	private BigDecimal dataservicemasterid;
	private String benificarystatus;
	private String benificiaryryNameRemittance;
	private BigDecimal masterId;
	private BigDecimal beneficaryBankId;
	private BigDecimal beneficaryBankBranchId;
	private String benificaryTelephone;
	private BigDecimal beneficaryStatusId;
	private String procedureError;
	private BigDecimal beneficiaryAccountSeqId;
	private BigDecimal beneficiaryRelationShipSeqId;
	private BigDecimal amountToRemit;
	// fourth Panel Variables
	private boolean additionalCheck = true;
	private String exceptionMessageForReport;
	private BigDecimal exchangeRate;
	private BigDecimal Overseasamt;
	private BigDecimal commission;
	private BigDecimal grossAmountCalculated;
	private BigDecimal loyaltyAmountAvailed;
	private BigDecimal netAmountPayable;
	private BigDecimal netAmountSent;
	private BigDecimal netAmountForTransaction = BigDecimal.ZERO;
	private String localCurrencyName;
	private String forgeignCurrencyName;
	private BigDecimal newRemittanceModeId;
	private String newRemittanceModeName;
	private boolean booRenderRemit;
	private BigDecimal newDeliveryModeId;
	private String newDeliveryModeName;
	private boolean booRenderDelivery;
	private String swiftBic;
	private String beneSwiftBank1;
	private String beneSwiftBank2;
	private String beneSwiftBankAddr1;
	private String beneSwiftBankAddr2;
	private BigDecimal beneStateId;
	private BigDecimal beneDistrictId;
	private BigDecimal beneCityId;
	private String PbeneFullName;
	private String PbeneFirstName;
	private String PbeneSecondName;
	private String PbeneThirdName;
	private String PbeneFourthName;
	private String PbeneFifthName;
	private boolean booRenderInstructions;
	private boolean booRenderSwiftBank1;
	private boolean booRenderSwiftBank2;
	private String furthuerInstructions;
	private String serviceGroupCode = null;
	private BigDecimal specialDealRate;
	private BigDecimal spotExchangeRate;
	private BigDecimal spotExchangeRatePk;
	private String cashRounding;
	private String databenificaryservicecode;
	// Added by Rabil on 09/03/2016
	private BigDecimal approvalYear;
	private BigDecimal approvalNo;
	private BigDecimal equivalentRemitAmount;
	private String equivalentCurrency;
	private BigDecimal sourceOfIncome;
	private Boolean booRenderDataTablePanel = false;
	private Boolean booRenderTransferFundPanel = false;
	private Boolean booRenderAdditionalDataPanel = false;
	private Boolean booRenderDebitCardPanel = false;
	// components for third panel
	private boolean booSingleRoutingCountry = true;
	private boolean booMultipleRoutingCountry = false;
	private boolean booReadOnlyRemitAmount = false;
	// Added by Rabil 08/12/2015
	private boolean booSingleService = true;
	private boolean booMultipleService = false;
	private boolean booRenderRouting = false;
	private boolean booRenderAgent = false;
	private boolean booSingleRoutingBank = true;
	private boolean booMultipleRoutingBank = false;
	private boolean booSingleRoutingBranch = true;
	private boolean booMultipleRoutingBranch = false;
	private boolean booRenderDeliveryModeInputPanel = true;
	private boolean booRenderDeliveryModeDDPanel = false;
	private boolean booSingleRemit = true;
	private boolean booMultipleRemit = false;
	private boolean disableSpotRatePanel = false;
	private boolean marqueeRender = false;
	private boolean booSpecialCusFCCalDataTable = false;
	private boolean booRenderRemittanceServicePanel = false;
	
	// ICASH Product added on 27/04/16
	private boolean icashStateSubAgent = false;
	private boolean icashAgentPanel = false;
	private boolean icashEFT = false;
	private boolean icashTT = false;
	private String icashAgent;
	private String icashState;
	private String icashSubAgent;
	private BigDecimal icashCostRate;
	private BigDecimal loyaltyPoints;
	// Icash Product
	private String databenificarycountryalphacode;
	private String databenificarybankalphacode;
	private String dataroutingcountryalphacode;
	private String dataroutingbankalphacode;
	private String databenificarycurrencyalphacode;

	int saveCount = 0;
	Boolean checkProExp = false;
	private boolean boorenderlastpanel;
	private BigDecimal calGrossAmount;
	private BigDecimal calNetAmountPaid;
	String actionShopping;
	
	/** Added by Rabil */
	private String debitCard1;
	private String debitCard2;
	private String debitCard3;
	private String debitCard4;
	private String cardErrorMsg;
	private Boolean booRendercardErrorMsg = false;
	private BigDecimal documentNumber=null;
	private BigDecimal totalKnetAmount =new BigDecimal(0);
	
	private BigDecimal collectionDocumentNumber=null;
	private BigDecimal collectionDocumentCode = null;
	private BigDecimal collectionFinanceYear = null;
	

	private String destiCurrency =null;
	
	private BigDecimal destiCurrencyId =null;
	
	
	
	private String knetTransErrorMessage;
	private String knetTransErrorContactMessage;
	private String knetSuccessPage;
	
	private String colCurrency;
	

	private BigDecimal applicationDocNum;
	private BigDecimal shoppingcartExchangeRate;
	private boolean booRenderMultiDocNum = false;
	private boolean booRenderSingleDocNum = true;
	private BigDecimal dummiTotalNetAmount;
	private BigDecimal dummiTotalGrossAmount;
	private boolean booShowCashRoundingPanel = false;
	private boolean booRenderModifiedRoundData = false;
	
	private BigDecimal remittanceNo = new BigDecimal(0);
	private BigDecimal tempCalGrossAmount = new BigDecimal(0);
	private BigDecimal tempCalNetAmountPaid = new BigDecimal(0);
	private BigDecimal totalUamount = new BigDecimal(0);
	private BigDecimal subtractedAmount = new BigDecimal(0);
	private BigDecimal minLenght;

	//New fields added
	private String selectedSearchRadioButton;
	private Boolean booRenderBeneNameOrBank = false;
	private Boolean cashbranchOne = false;
	private Boolean cashbranchMore = false;
	private BigDecimal beneBankBranchId;
	private List<PopulateData> lstBankBranch = new ArrayList<PopulateData>();
	private String beneBankBranchName;
	private boolean booRenderAccountDialogBox = false;
	private Boolean mandatoryOptional = true;
	private Boolean booRenderAccOrBranch = false;
	 

	private String insurence1;
	private String insurence2;
	private String loyalityPointExpiring;
	private String loyalityPointExpiring1;
	private String loyalityPointExpiring2;
	private Date documentDate= new Date();
	private String promotionId;
	String helpDeskNo="";
	String remitAction;
	String paymentModeType;
	
	public String getPaymentModeType() {
		return paymentModeType;
	}

	public void setPaymentModeType(String paymentModeType) {
		this.paymentModeType = paymentModeType;
	}

		//Added by Rabil on 14 June 2017
		private String userDefExchangeRate;
		private String exchangeMinRate;
		private String exchangeMaxRate;
	
	
	private BigDecimal beneficiaryAccountType;
	private List<AccountTypeFromView> lstBankAccountTypeFromView = new ArrayList<AccountTypeFromView>();
	private BigDecimal beneficiaryStateId;
	private List<StateMasterDesc> lstStateMasterDescs=new ArrayList<StateMasterDesc>();
	private BigDecimal beneficiaryDistId;
	private List<DistrictMasterDesc> lstDistrictMasterDescs=new ArrayList<DistrictMasterDesc>();
	private BigDecimal beneficiaryCityId;
	private List<CityMasterDesc> lstCityMasterDescs=new ArrayList<CityMasterDesc>();
	private String telePhoneCode;
	private String mobileCode;
	private List<CountryMasterView> lstCountryMasterDescs=new ArrayList<CountryMasterView>();
	private String beneTelePhoneNum;
	private BigDecimal beneMobilePhoneNum;
	private BigDecimal beneficiaryContactSeqId;
	private PersonalRemmitanceBeneficaryDataTable personalRemmitanceBeneficaryDataTables;
	private boolean bankingChannelProducts = true;
	
	private boolean btnRender = false;
	
 
	
	private List<PopulateData> routingBankBranchlst = new ArrayList<PopulateData>();
	private List<PopulateData> routingbankvalues = new ArrayList<PopulateData>();
	private List<PopulateData> routingCountrylst = new ArrayList<PopulateData>();
	private List<SourceOfIncomeDescription> lstSourceofIncome = new ArrayList<SourceOfIncomeDescription>();
	private List<PurposeOfTransaction> lstPurposeOfTransaction = new ArrayList<PurposeOfTransaction>();
	private List<SourceOfIncomeDescription> lstSourceOfIncome = new ArrayList<SourceOfIncomeDescription>();
	private List<AddDynamicLabel> listDynamicLebel = new ArrayList<AddDynamicLabel>();
	private List<AddAdditionalBankData> listAdditionalBankDataTable = new ArrayList<AddAdditionalBankData>();
	private List<PopulateData> lstSwiftMasterBank1 = new ArrayList<PopulateData>();
	private List<PopulateData> lstSwiftMasterBank2 = new ArrayList<PopulateData>();
	private List<ShoppingCartDataTableBean> shoppingcartDTList = new ArrayList<ShoppingCartDataTableBean>();
	 
	private List<PopulateData> lstofCurrency = new ArrayList<PopulateData>();
	
	private List<PopulateData> lstofDestinationCurrency = new ArrayList<PopulateData>();
	private List<PopulateData> lstofRemittance = new ArrayList<PopulateData>();
	private List<PopulateData> lstofDelivery = new ArrayList<PopulateData>();
	private List<PopulateData> lstofCountry = new ArrayList<PopulateData>();
	List<BenificiaryListView> beneCountryList = new ArrayList<BenificiaryListView>();
	List<BenificiaryListView> beneNameList = new ArrayList<BenificiaryListView>();
	List<BenificiaryListView> beneBankList = new ArrayList<BenificiaryListView>();
	List<BenificiaryListView> beneAccountList = new ArrayList<BenificiaryListView>();
	private List<PopulateData> lstBeneName = new ArrayList<PopulateData>();
	private List<PopulateData> lstBank = new ArrayList<PopulateData>();
	private List<PopulateData> lstAccount = new ArrayList<PopulateData>();
	
	private List<PersonalRemmitanceBeneficaryDataTable> coustomerBeneficaryDTList = new ArrayList<PersonalRemmitanceBeneficaryDataTable>();
	
	private List<PersonalRemmitanceBeneficaryDataTable> coustomerBeneficaryListForQuick = new ArrayList<PersonalRemmitanceBeneficaryDataTable>();
	
	private List<BeneficaryMaster> beneficiaryMaster = new ArrayList<BeneficaryMaster>();
	private List<BeneficaryContact> beneficiaryTel = new ArrayList<BeneficaryContact>();
	List<PopulateData> serviceList = new ArrayList<PopulateData>();
	
	private List<CustomerDBCardDetailsView> lstofDBCards = new ArrayList<CustomerDBCardDetailsView>();
	private List<KnetSuccessPageKnetDetailsDataTable> lstKnetDetails = new ArrayList<KnetSuccessPageKnetDetailsDataTable>();
	private List<KnetSuccessPageCustomerDataTable> lstknetCustomer = new ArrayList<KnetSuccessPageCustomerDataTable>();	
	private List<KnetSuccessPageKnetTransactionDataTable> lstKnetTransDetails = new ArrayList<KnetSuccessPageKnetTransactionDataTable>();
	
	private CopyOnWriteArrayList<ShoppingCartDataTableBean> lstselectedrecord = new CopyOnWriteArrayList<ShoppingCartDataTableBean>();
	private CopyOnWriteArrayList<ShoppingCartDataTableBean> lstModifyRoundRecord = new CopyOnWriteArrayList<ShoppingCartDataTableBean>();

	private List<BigDecimal> lstApplDocNumber = new ArrayList<BigDecimal>();
	
	private List<RemittanceReceiptSubreport> remittanceReceiptSubreportList;

	// Jasper Report generation
	private JasperPrint jasperPrint;
	private List<CollectionDetailView> collectionViewList = new CopyOnWriteArrayList<CollectionDetailView>();
	
	
	private String customerNameForReport;
/*	String prLtyStr1 =null;
	String prLtyStr2 =null;
	String prInsStr1 =null;
	String prInsStr2 =null;
	String prInsStrAr1 =null;
	String prInsStrAr2=null;*/
	
	 
	private UserProfile userProfile = null;
	@Autowired
	IGeneralService<T> generalService;
	@Autowired
	IBeneficaryCreation beneficaryCreation;
	@Autowired
	IServiceGroupMasterService iServiceGroupMasterService;
	@Autowired
	IPersonalRemittanceService iPersonalRemittanceService;
	@Autowired
	ForeignLocalCurrencyDenominationService<T> foreignLocalCurrencyDenominationService;
	@Autowired
	IForeignCurrencyPurchaseService<T> foreignCurrencyPurchaseService;
	
	@Autowired
	ApplicationMailer mailService;

	public OnlineTransferBean() {
		assignNullValues();
		sessionStateManage = new SessionStateManage();
		userProfile = (UserProfile) sessionStateManage.getSessionValueAsObject("userProfile");
		languageId = new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1");
		applicationCountryId = new BigDecimal(sessionStateManage.isExists("countryId") ? sessionStateManage.getSessionValue("countryId") : "1");
		companyName = sessionStateManage.isExists("companyName") ? sessionStateManage.getSessionValue("companyName") : "";
		//companyId =userProfile.getCompanyId()==null?;
		companyId = sessionStateManage.getCompanyId();
		helpDeskNo = sessionStateManage.isExists("helpDeskNo") ? sessionStateManage.getSessionValue("helpDeskNo") : "";
		log.info("UserCreationBean :" + languageId + "\t companyName :" + companyName + "\t country :" + applicationCountryId);
		if (languageId.compareTo(new BigDecimal("1")) == 0) {
			props = ResourceBundle.getBundle("com.amg.exchange.messages.amgamx_en");
		} else if (languageId.compareTo(new BigDecimal("2")) == 0) {
			props = ResourceBundle.getBundle("com.amg.exchange.messages.amgamx_ar");
		} else {
			props = ResourceBundle.getBundle("com.amg.exchange.messages.amgamx_en");
		}
		fCtx = FacesContext.getCurrentInstance();
		session = (HttpSession) fCtx.getExternalContext().getSession(true);
		HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		ipAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = httpServletRequest.getRemoteAddr();
		}
	}

	public void pageNavigationHomePage(String act) throws IOException {
		try {
			setDisclaimerVisible(true);
			clear();
			setBeneCountr(null);
			getCustomerDetails();
			setRemitAction(act);
			if(act.equalsIgnoreCase("T")){
				FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_home.xhtml");
			}else if(act.equalsIgnoreCase("Q")){
				FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/quickRemit.xhtml");
			}
			RequestContext.getCurrentInstance().execute("openInfo.show();");
		}  catch (NullPointerException e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("sqlexception.show();");
		}catch (Exception e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("sqlexception.show();");
		}
	}
	


	 public void pageNavigation() throws IOException {
		 	clear();
			setLstBeneName(null);
			setBeneNameList(null);
			setBeneBankName(null);
			setDataAccountnum(null);
			setDatabenificaryname(null);
			setBeneBankList(null);
			setBeneAccountList(null);
			setBeneficaryBankId(null);
			setDisclaimerVisible(false);
			setBeneCountryMore(false);
			setBeneCountryOne(true);
			setBeneCountryName(null);
			setBeneOne(true);
			setBeneMore(false);
			setBankOne(true);
			setBankMore(false);
			setAccountOne(true);
			setAccountMore(false);
			setCashbranchMore(false);
			setCashbranchOne(false);
			
			setCurrOne(true);
			setCurrMore(false);
			
			setBankingChannelProducts(true);
			setBeneBankBranchId(null);
			setBeneBankBranchName(null);
			setBeneCountr(null); 
			setBooRenderBeneNameOrBank(true); 
			
			if(remitAction!=null && remitAction.equalsIgnoreCase("T")){
				popCountry();
			}else if(remitAction!=null && remitAction.equalsIgnoreCase("Q")){
				populateBeneficiaryListDetailsFromBeneRelationFromOnlineView();
			}
			setSelectedSearchRadioButton("Name");
			
	} 
	
	public void searchByType() throws IOException {
		clear();
		setLstBeneName(null);
		setBeneNameList(null);
		setBeneBankName(null);
		setDataAccountnum(null);
		setDatabenificaryname(null);
		setBeneBankList(null);
		setBeneAccountList(null);
		setBeneficaryBankId(null);
		setDisclaimerVisible(false);
		setBeneCountryMore(false);
		setBeneCountryOne(true);
		setBeneCountryName(null);
		setBeneOne(true);
		setBeneMore(false);
		setBankOne(true);
		setBankMore(false);
		setAccountOne(true);
		setAccountMore(false);
		setCashbranchMore(false);
		setCashbranchOne(false);
		setBankingChannelProducts(true);
		setBeneBankBranchId(null);
		setBeneBankBranchName(null);
		setBeneCountr(null); 
		setBooRenderBeneNameOrBank(true); 
		getCustomerDetails();
		searchBeneNameorBankRadioButton();
		setCurrOne(true);
		setCurrMore(false);
	}
	
	// select radio button to modify the search operation
		public void searchBeneNameorBankRadioButton(){

			System.out.println("Selected Radio : "+getSelectedSearchRadioButton());
			try {
				if(getSelectedSearchRadioButton() != null && getSelectedSearchRadioButton().equalsIgnoreCase("Name")){
					popCountry();
					setBooRenderBeneNameOrBank(true);
				}else if(getSelectedSearchRadioButton() != null && getSelectedSearchRadioButton().equalsIgnoreCase("Bank")){
					popBankListForBank();
					setBooRenderBeneNameOrBank(false);
				}else{
					// not selected any value
					setBooRenderBeneNameOrBank(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void popBankListForBank() throws AMGException{

			List<PopulateData> list = beneficaryCreation.getBeneBankList(getCustomerNo());
			if (list.size() > 1) {
				setLstBank(list);
				setBankOne(false);
				setBankMore(true);
			} else if (list.size() == 1) {
				setBeneficaryBankId(list.get(0).getPopulateId());
				setBeneBankName(list.get(0).getPopulateName());
				setBankOne(true);
				setBankMore(false);
				popCountryForBank();
			} else {
				setBankOne(true);
				setBankMore(false);
			}
		}
		
		public void popCountryForBank() throws AMGException{
			clearForBank();
			
			setAccountOne(false);
			setAccountMore(true);
			
			setCurrMore(true);
			setCurrOne(false);
			
			setLstofCurrency(null);
			setLstBankBranch(null);
			setCurrency(null);
			List<PopulateData> lstCountry = beneficaryCreation.getCountryList(getBeneficaryBankId() ,getCustomerNo(), sessionStateManage.getLanguageId());
			if (lstCountry != null && lstCountry.size() > 1) {
				setLstofCountry(lstCountry);
				setBeneCountryOne(false);
				setBeneCountryMore(true);
			} else if (lstCountry != null && lstCountry.size() == 1) {
				setBeneCountr(lstCountry.get(0).getPopulateId());
				setBeneCountryName(lstCountry.get(0).getPopulateName());
				setBeneCountryOne(true);
				setBeneCountryMore(false);
				popAccountListForBank();
			} else {
				setBeneCountryOne(true);
				setBeneCountryMore(false);
			}
		}
		
		public void popAccountListForBank() throws AMGException{
			setBeneNameList(null);
			setDatabenificaryname(null);
			setProcedureError(null);
			setExceptionMessage(null);
			setExceptionMessageForReport(null);
			setErrorMessage(null);
			setBeneAccountList(null);
			setDataAccountnum(null);
			setLstBankBranch(null);
			setLstofCurrency(null);
			setCurrency(null);
			setAvailLoyaltyPoints(Constants.No);
			setBooRenderAccOrBranch(false);
			List<PopulateData> list = beneficaryCreation.getBeneAccountNoList(getBeneficaryBankId(), getCustomerNo(), getBeneCountr());
			if (list != null && list.size() > 1) {
				PopulateData checkProduct = list.get(0);
				if(checkProduct.getPopulateName() != null){
					setBankingChannelProducts(true);
					setLstAccount(list);
					setAccountOne(false);
					setAccountMore(true);
					setCashbranchOne(false);
					setCashbranchMore(false);
				}else{
					setBankingChannelProducts(false);
					setAccountOne(false);
					setAccountMore(false);
					setCashbranchOne(false);
					setCashbranchMore(true);
					setLstBankBranch(list);
				//	popAccountCurrencyList();
					
				}
			} else if (list != null && list.size() == 1) {			
				if(list.get(0).getPopulateName() != null){
					setBankingChannelProducts(true);
					setDataAccountnum(list.get(0).getPopulateName());
					setAccountOne(true);
					setAccountMore(false);
					setCashbranchOne(false);
					setCashbranchMore(false);
					popNameListBank();
				}else{
					setBankingChannelProducts(false);
					setAccountOne(false);
					setAccountMore(false);
					setCashbranchOne(true);
					setCashbranchMore(false);
					setBeneBankBranchId(list.get(0).getPopulateId());
					setBeneBankBranchName(list.get(0).getPopulateCode());
					popNameListBank();
				}
			} else {
				setAccountOne(true);
				setAccountMore(false);
				setCashbranchOne(false);
				setCashbranchMore(false);
				setBankingChannelProducts(true);
			}
		}
		
		public void popNameListBank() {
			try{
				setProcedureError(null);
				setExceptionMessage(null);
				setExceptionMessageForReport(null);
				setErrorMessage(null);
				setCurrency(null);
				setAvailLoyaltyPoints(Constants.No);
				setBooRenderAccOrBranch(false);
				String  accNumber =null;


				/* adde new code on 05 april 2017*/
				if(lstAccount!=null && lstAccount.size()!=0){
					boolean checkAcc = false;
					for(PopulateData popData : lstAccount){
						if(popData.getPopulateName()!=null && popData.getPopulateName().equalsIgnoreCase(getDataAccountnum())){
							// account number Check
							setDataAccountnum(popData.getPopulateName());
							setAccountOne(true);
							setAccountMore(false);
							setCashbranchOne(false);
							setCashbranchMore(false);
							setBeneBankBranchName(null);
							setBeneBankBranchId(null);
							accNumber =getDataAccountnum();
							setBankingChannelProducts(true);
							checkAcc=true;
							break;
						}
					}

					if(!checkAcc){

						for(PopulateData popData : lstAccount){
							if(popData.getPopulateCode()!=null && popData.getPopulateCode().equalsIgnoreCase(getDataAccountnum())){
								accNumber = null;
								
								setAccountOne(false);
								setAccountMore(false);
								setCashbranchOne(true);
								setCashbranchMore(false);
								setBeneBankBranchId(popData.getPopulateId());
								setBeneBankBranchName(popData.getPopulateCode());
								
								setBankingChannelProducts(false);
								setBeneBankBranchId(popData.getPopulateId());
								break;
							}
						}
					}
				}
				/* adde new code on 05 april 2017*/
				//List<PopulateData> list = beneficaryCreation.getBenNameList(getBeneficaryBankId(), getCustomerNo(), getBeneCountr(),getDataAccountnum(),getBeneBankBranchId());
				/** Added on 05 /04/2016 */
				//List<PopulateData> list = beneficaryCreation.getBenNameList(getBeneficaryBankId(), getCustomerNo(), getBeneCountr(),accNumber,getBeneBankBranchId());
				/** Modifed on 06/04/2016 */
				List<PopulateData> list = beneficaryCreation.getBenNameList(getBeneficaryBankId(), getCustomerNo(), getBeneCountr(),accNumber,getBeneBankBranchId(),getDatabenificaryname());

				if (list!= null && list.size() > 1) {
					setBooRenderAccountDialogBox(false);
					setLstBeneName(list);
					setBeneOne(false);
					setBeneMore(true);
				} else if (list.size() == 1) {
					setDatabenificaryname(list.get(0).getPopulateName());
					setBeneOne(true);
					setBeneMore(false);
					popAccountCurrencyList();
					//populateCustomerDetailsFromBeneRelation();
				} else {
					setBeneOne(true);
					setBeneMore(false);
				}
				
			} catch (AMGException e) {
				setExceptionMessage(e.getMessage());
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
				List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.PRODUCT_SETUP);
				String currencyName = null;
				String currency = "";
				if(getCurrency()!=null){
					currencyName = generalService.getCurrencyName(getCurrency());
					currency = " ( "+currencyName+" )";
				}
				//Original
				//getMailService().sendEmailTransactionCreationFail(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
				//For Multi Country
				getMailService().getEmailTransactionCreationFailContent(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
		
			}
		}
		
		
		
		public void toFetchCityBasedOnSelectDistrict(){
			lstCityMasterDescs.clear();
			setBeneficiaryCityId(null);
			try{
				List<CityMasterDesc> cityMasterDescs=generalService.getCityList(sessionStateManage.getLanguageId(),getDatabenificarycountry(),getBeneficiaryStateId(),getBeneficiaryDistId());
				if(cityMasterDescs.size()>0){
					lstCityMasterDescs.addAll(cityMasterDescs);
				}
			}catch(NullPointerException ne){
				log.info("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
				setExceptionMessage(ne.getMessage()); 
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
				return; 
			}catch(Exception exception){
				log.info("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
				setExceptionMessage(exception.getMessage()); 
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
				return;       
			}
		}
		
		public void clearForBank() {
			setBooRenderAccountDialogBox(false);
			setDocumentNumber(null);
			setLstBeneName(null);
			setDatabenificaryname(null);
			setBeneAccountList(null);
			setLstAccount(null);
			setDataAccountnum(null);
			setLstofCurrency(null);
			setLstBankBranch(null);
			setCurrency(null);
			setBeneBankBranchId(null);
			setBeneBankBranchName(null);
			setAccountMore(true);
			setAccountOne(false);
			setBeneCountryOne(true);
			setBeneCountryMore(false);
			
			setCurrMore(true);
			setCurrOne(false);
			
			setBankingChannelProducts(true);
			setTotalKnetAmount(null);
			setCollectionDocumentCode(null);
			setCollectionDocumentNumber(null);
			setCollectionFinanceYear(null);
			setProcedureError(null);
			setExceptionMessage(null);
			setExceptionMessageForReport(null);
			setErrorMessage(null);
		}
		

			
	public void popCountry() {
		 
		List<PopulateData> lstCountry = beneficaryCreation.getCountryList(getCustomerNo(), sessionStateManage.getLanguageId());
		if(lstCountry!=null && lstCountry.size()>1){
			setLstofCountry(lstCountry);
			setBeneCountryOne(false);
			setBeneCountryMore(true);
		}else if(lstCountry!=null && lstCountry.size()==1){
			setBeneCountr(lstCountry.get(0).getPopulateId());
			setBeneCountryName(lstCountry.get(0).getPopulateName());
			setBeneCountryOne(true);
			setBeneCountryMore(false);
			popNameList();
		}else{
			setBeneCountryOne(false);
			setBeneCountryMore(true);
			setLstofCountry(lstCountry);
		}
		
	}

	public void toFetchDistrictOnSelectState(){
		try{
			setBeneficiaryDistId(null);
			setBeneficiaryCityId(null);
			lstDistrictMasterDescs.clear();
			lstCityMasterDescs.clear();
			List<DistrictMasterDesc> districtMasterDescs=generalService.getDistrictList(sessionStateManage.getLanguageId(),getDatabenificarycountry(),getBeneficiaryStateId());
			if(districtMasterDescs.size()>0){
				lstDistrictMasterDescs.addAll(districtMasterDescs);
			}
		}catch(NullPointerException ne){
			log.info("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
			setExceptionMessage(ne.getMessage()); 
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			return; 
		}catch(Exception exception){
			log.info("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
			setExceptionMessage(exception.getMessage()); 
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			return;       
		}
	}
	
	public void popNameList() {
		clear();
		setBankOne(false);
		setBankMore(true);
		setAccountOne(false);
		setAccountMore(true);
		setCurrMore(true);
		setCurrOne(false);
		setLstofCurrency(null);
		setLstBankBranch(null);
		setCurrency(null);
		List<PopulateData> list = beneficaryCreation.getBenNameList(getCustomerNo(), getBeneCountr());
		if (list.size() > 1) {
			setLstBeneName(list);
			setBeneOne(false);
			setBeneMore(true);
		} else if (list.size() == 1) {
			setDatabenificaryname(list.get(0).getPopulateName());
			setBeneOne(true);
			setBeneMore(false);
			popBankList();
		} else {
			setBeneOne(true);
			setBeneMore(false);
		}
	}

	public void popBankList() {
		setLstBank(null);
		setBeneAccountList(null);
		setBeneficaryBankId(null);
		setLstAccount(null);
		setLstBankBranch(null);
		setBeneBankName(null);
		setDataAccountnum(null);
		setCashbranchMore(false);
		setCashbranchOne(false);
		setAccountMore(true);
		setAccountOne(false);
		
		setCurrMore(true);
		setCurrOne(false);
		
		
		setBankingChannelProducts(true);
		setBeneBankBranchId(null);
		setBeneBankBranchName(null);
		setLstBankBranch(null);
		setLstofCurrency(null);
		setCurrency(null);
		setProcedureError(null);
		setExceptionMessage(null);
		setExceptionMessageForReport(null);
		setErrorMessage(null);
		List<PopulateData> list = beneficaryCreation.getBeneBankList(getCustomerNo(), getBeneCountr(), getDatabenificaryname());
		if (list.size() > 1) {
			setBooRenderAccountDialogBox(false);
			setLstBank(list);
			setBankOne(false);
			setBankMore(true);
		} else if (list.size() == 1) {
			setBeneficaryBankId(list.get(0).getPopulateId());
			setBeneBankName(list.get(0).getPopulateName());
			setBankOne(true);
			setBankMore(false);
			popAccountList();
		} else {
			setBankOne(true);
			setBankMore(false);
		}
	}

	public void popAccountList() {
		
		try{
		 
			setProcedureError(null);
			setExceptionMessage(null);
			setExceptionMessageForReport(null);
			setErrorMessage(null);
			setBeneAccountList(null);
			setDataAccountnum(null);
			setLstBankBranch(null);
			setLstofCurrency(null);
			setCurrency(null);
			setAvailLoyaltyPoints(Constants.No);
			setBooRenderAccOrBranch(false);
			List<PopulateData> list = beneficaryCreation.getBeneAccountNoList(getCustomerNo(), getBeneCountr(), getBeneficaryBankId(), getDatabenificaryname());
			if (list != null && list.size() > 1) {
				setBooRenderAccountDialogBox(false);
				PopulateData checkProduct = list.get(0);
				if(checkProduct.getPopulateName() != null){
					setBankingChannelProducts(true);
					setLstAccount(list);
					setAccountOne(false);
					setAccountMore(true);
					setCashbranchOne(false);
					setCashbranchMore(false);
				}else{
					setBankingChannelProducts(false);
					setAccountOne(false);
					setAccountMore(false);
					setCashbranchOne(false);
					setCashbranchMore(true);
					setLstBankBranch(list);
					
					//popAccountCurrencyList();
				}
			} else if (list != null && list.size() == 1) {			
				if(list.get(0).getPopulateName() != null){
					setBankingChannelProducts(true);
					setDataAccountnum(list.get(0).getPopulateName());
					setAccountOne(true);
					setAccountMore(false);
					setCashbranchOne(false);
					setCashbranchMore(false);
					
					popAccountCurrencyList();
					
					//populateCustomerDetailsFromBeneRelation();
				}else{
					setBankingChannelProducts(false);
					setAccountOne(false);
					setAccountMore(false);
					setCashbranchOne(true);
					setCashbranchMore(false);
					setBeneBankBranchId(list.get(0).getPopulateId());
					setBeneBankBranchName(list.get(0).getPopulateCode());
					
					popAccountCurrencyList();
					//populateCustomerDetailsFromBeneRelation();
				}
			} else {
				setAccountOne(true);
				setAccountMore(false);
				setCashbranchOne(false);
				setCashbranchMore(false);
				setBankingChannelProducts(true);
			}

		} catch (AMGException e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.PRODUCT_SETUP);
			String currencyName = null;
			String currency = "";
			if(getCurrency()!=null){
				currencyName = generalService.getCurrencyName(getCurrency());
				currency = " ( "+currencyName+" )";
			}
			//Original
			//getMailService().sendEmailTransactionCreationFail(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
		
			getMailService().getEmailTransactionCreationFailContent(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
		}
		
	}

	
	
public void popAccountCurrencyList() throws AMGException {
		
		try{
			setProcedureError(null);
			setExceptionMessage(null);
			setExceptionMessageForReport(null);
			setErrorMessage(null);
			setBeneAccountList(null);
			//setLstBankBranch(null);
			setLstofCurrency(null);
			setCurrency(null);
			setAvailLoyaltyPoints(Constants.No);
			setBooRenderAccOrBranch(false);
			AccountCurrencyView accountCurrencyViewParam = new AccountCurrencyView();
			accountCurrencyViewParam.setCustomerId(getCustomerNo());
			accountCurrencyViewParam.setBenificaryCountry(getBeneCountr());
			accountCurrencyViewParam.setBankId(getBeneficaryBankId());
			accountCurrencyViewParam.setBenificaryName(getDatabenificaryname());
			if(isBankingChannelProducts()){
			accountCurrencyViewParam.setBankAccountNumber(getDataAccountnum());
			}else{
				accountCurrencyViewParam.setBankAccountNumber(null);
			}
			
			
			List<PopulateData> list = beneficaryCreation.getAccountCurrencyList(accountCurrencyViewParam);
			if (list != null && list.size() > 1) {
				//setBankingChannelProducts(true);
				//setDataAccountnum(list.get(0).getPopulateName());
				//setAccountOne(true);
				//setAccountMore(false);
				setLstofDestinationCurrency(list);
				setCurrOne(false);
				setCurrMore(true);
			} else if (list != null && list.size() == 1) {			
				//setBankingChannelProducts(true);
				//setDataAccountnum(list.get(0).getPopulateName());
				//setAccountOne(true);
				//setAccountMore(false);
				setDestiCurrency(list.get(0).getPopulateName());
				setDestiCurrencyId(list.get(0).getPopulateId());
				setCurrency(list.get(0).getPopulateId());
				setCurrOne(true);
				setCurrMore(false);
				populateCustomerDetailsFromBeneRelation(null);
			} else {
				setCurrOne(true);
				setCurrMore(false);
			}
			
		}catch (AMGException e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.PRODUCT_SETUP);
			String currencyName = null;
			String currency = "";
			if(getCurrency()!=null){
				currencyName = generalService.getCurrencyName(getCurrency());
				currency = " ( "+currencyName+" )";
			}
			//getMailService().sendEmailTransactionCreationFail(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
			getMailService().getEmailTransactionCreationFailContent(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
		}
		
}



 

	
	public void populateCustomerDetailsFromBeneRelation(PersonalRemmitanceBeneficaryDataTable beneficaryDataTable) throws AMGException {
		log.info("Entering into populateCustomerDetailsFromBeneRelation method ");
		try {
			List<BenificiaryListView> isCoustomerExist = null;
			
			if (coustomerBeneficaryDTList != null || !coustomerBeneficaryDTList.isEmpty()) {
				coustomerBeneficaryDTList.clear();
			}
			
			if(destiCurrencyId!= null){
				setCurrency(destiCurrencyId);
			}
			if(beneficaryDataTable!= null){
				System.out.println("Quick beneficaryDataTable :"+beneficaryDataTable.getCustomerId()+"\t getBeneCountr() :"+beneficaryDataTable.getBenificaryCountry()+"\t getDatabenificaryname :"+beneficaryDataTable.getBenificaryName()+"\t getBeneficaryBankId :"+beneficaryDataTable.getBankId()+"\t getDataAccountnum :"+beneficaryDataTable.getBankAccountNumber()+"\t getCurrency() :"+beneficaryDataTable.getCurrencyId());
				isCoustomerExist = beneficaryCreation.getBeneficiary(sessionStateManage.getCustomerId(), beneficaryDataTable.getBenificaryCountry(), beneficaryDataTable.getBenificaryName(), beneficaryDataTable.getBankId(),beneficaryDataTable.getBankAccountNumber(), beneficaryDataTable.getCurrencyId());
			}else{
				System.out.println("isBankingChannelProducts :"+isBankingChannelProducts());
				if(!isBankingChannelProducts()){
					setDataAccountnum(null); 
				}
				//Added for CASH PRODUCT 0n 06 April 2017
				//setDataAccountnum(null); 
				isCoustomerExist = beneficaryCreation.getBeneficiary(sessionStateManage.getCustomerId(), getBeneCountr(), getDatabenificaryname(), getBeneficaryBankId(),getDataAccountnum(), getCurrency());
			}
			
			System.out.println("Transfer fund beneficaryDataTable :"+sessionStateManage.getCustomerId()+"\t getBeneCountr() :"+getBeneCountr()+"\t getDatabenificaryname :"+getDatabenificaryname()+"\t getBeneficaryBankId :"+getBeneficaryBankId()+"\t getDataAccountnum :"+getDataAccountnum()+"\t getCurrency() :"+getCurrency());
	
			
			//List<BenificiaryListView> isCoustomerExist = beneficaryCreation.getBeneficiary(sessionStateManage.getCustomerId(), getBeneCountr(), getDatabenificaryname(), getBeneficaryBankId(),getDataAccountnum(), getCurrency());
			
			
			
			log.info("=====================" + isCoustomerExist.size());
			if (isCoustomerExist != null && isCoustomerExist.size() > 0) {
				for (int i = 0; i < isCoustomerExist.size(); i++) {
					BenificiaryListView rel = new BenificiaryListView();
					rel = isCoustomerExist.get(i);
					PersonalRemmitanceBeneficaryDataTable personalRBDataTable = new PersonalRemmitanceBeneficaryDataTable();
					personalRBDataTable.setAccountStatus(rel.getAccountStatus());
					personalRBDataTable.setAge(rel.getAge());
					personalRBDataTable.setApplicationCountryId(rel.getApplicationCountryId());
					personalRBDataTable.setArbenificaryName(rel.getArbenificaryName());
					personalRBDataTable.setBankAccountNumber(rel.getBankAccountNumber());
					personalRBDataTable.setBankBranchName(rel.getBankBranchName());
					personalRBDataTable.setBankCode(rel.getBankCode());
					personalRBDataTable.setBankId(rel.getBankId());
					personalRBDataTable.setBankName(rel.getBankName());
					personalRBDataTable.setBeneficaryMasterSeqId(rel.getBeneficaryMasterSeqId());
					personalRBDataTable.setBeneficiaryAccountSeqId(rel.getBeneficiaryAccountSeqId());
					personalRBDataTable.setBeneficiaryRelationShipSeqId(rel.getBeneficiaryRelationShipSeqId());
					personalRBDataTable.setBenificaryCountry(rel.getCountryId());
					personalRBDataTable.setBenificaryCountryName(rel.getCountryName());
					personalRBDataTable.setBenificaryName(rel.getBenificaryName());
					personalRBDataTable.setBenificaryStatusId(rel.getBenificaryStatusId());
					personalRBDataTable.setBenificaryStatusName(rel.getBenificaryStatusName());
					personalRBDataTable.setBooDisabled(rel.getBankAccountNumber() != null ? true : false);
					personalRBDataTable.setBranchCode(rel.getBranchCode());
					personalRBDataTable.setBranchId(rel.getBranchId());
					personalRBDataTable.setCityName(rel.getCityName());
					personalRBDataTable.setCountryId(rel.getBenificaryCountry());
					personalRBDataTable.setCountryName(rel.getBenificaryBankCountryName());
					personalRBDataTable.setCreatedBy(rel.getCreatedBy());
					personalRBDataTable.setCreatedDate(rel.getCreatedDate());
					personalRBDataTable.setCurrencyId(rel.getCurrencyId());
					personalRBDataTable.setCurrencyName(rel.getCurrencyName());
					personalRBDataTable.setCurrencyQuoteName(rel.getCurrencyQuoteName() == null ? "" : rel.getCurrencyQuoteName());
					personalRBDataTable.setCustomerId(rel.getCustomerId());
					personalRBDataTable.setDateOfBirth(rel.getDateOfBirth());
					personalRBDataTable.setDistrictName(rel.getDistrictName());
					personalRBDataTable.setFiftheName(rel.getFiftheName());
					personalRBDataTable.setFifthNameLocal(rel.getFifthNameLocal());
					personalRBDataTable.setFirstName(rel.getFirstName());
					personalRBDataTable.setFirstNameLocal(rel.getFirstNameLocal());
					personalRBDataTable.setFourthName(rel.getFourthName());
					personalRBDataTable.setFourthNameLocal(rel.getFourthNameLocal());
					personalRBDataTable.setIsActive(rel.getIsActive());
					personalRBDataTable.setLocation(rel.getNationalityName());
					personalRBDataTable.setModifiedBy(rel.getModifiedBy());
					personalRBDataTable.setModifiedDate(rel.getModifiedDate());
					personalRBDataTable.setNationality(rel.getNationality());
					personalRBDataTable.setNationalityName(rel.getNationalityName());
					personalRBDataTable.setNoOfRemittance(rel.getNoOfRemittance());
					personalRBDataTable.setOccupation(rel.getOccupation());
					personalRBDataTable.setRelationShipId(rel.getRelationShipId());
					personalRBDataTable.setRelationShipName(rel.getRelationShipName());
					personalRBDataTable.setRemarks(rel.getRemarks());
					personalRBDataTable.setSecondNameLocal(rel.getSecondNameLocal());
					personalRBDataTable.setSecondName(rel.getSecondName());
					personalRBDataTable.setServiceGroupCode(rel.getServiceGroupCode());
					personalRBDataTable.setServiceGroupId(rel.getServiceGroupId());
					personalRBDataTable.setServiceProvider(rel.getServiceProvider());
					personalRBDataTable.setStateName(rel.getStateName());
					personalRBDataTable.setMapSequenceId(rel.getMapSequenceId());
					/*personalRBDataTable.setBeneStateId(rel.getStateId());
					personalRBDataTable.setBeneDistrictId(rel.getDistrictId());
					personalRBDataTable.setBeneCityId(rel.getCityId());*/
					personalRBDataTable.setStateId(rel.getStateId());
					personalRBDataTable.setDistrictId(rel.getDistrictId());
					personalRBDataTable.setBankAccountTypeId(rel.getBankAccountTypeId());
					personalRBDataTable.setCityId(rel.getCityId());
					//For swift  beneficiary 
					personalRBDataTable.setSwiftBic(rel.getSwiftBic());
				
					//List<ServiceGroupMasterDesc> lstServiceGroupMasterDesc = generalService.LocalServiceGroupDescription(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"), rel.getServiceGroupId());
					List<ServiceGroupMasterDesc> lstServiceGroupMasterDesc = generalService.LocalServiceGroupDescription(new BigDecimal("1"), rel.getServiceGroupId());
					if (lstServiceGroupMasterDesc!=null && lstServiceGroupMasterDesc.size() > 0) {
						personalRBDataTable.setServiceGroupName(lstServiceGroupMasterDesc.get(0).getServiceGroupDesc());
					}
					List<BeneficaryContact> telePhone = beneficaryCreation.getTelephoneDetails(rel.getBeneficaryMasterSeqId());
					if (telePhone != null && telePhone.size() != 0) {
						String telNumber = null;
						if (telePhone != null && telePhone.size() == 1) {
							if (telePhone.get(0).getTelephoneNumber() != null) {
								telNumber = telePhone.get(0).getTelephoneNumber();
								setBeneTelePhoneNum(telNumber);
							} else if (telePhone.get(0).getMobileNumber() != null) {
								telNumber = telePhone.get(0).getMobileNumber().toPlainString();
							} else {
								telNumber = null;
							}
							personalRBDataTable.setTelphoneNum(telNumber);
							personalRBDataTable.setTelphoneCode(telePhone.get(0).getCountryTelCode());
							personalRBDataTable.setBeneficiaryContactSeqId(telePhone.get(0).getBeneficaryTelephoneSeqId());
						} else {
							BeneficaryContact beneficaryContact = telePhone.get(0);
							if (beneficaryContact.getTelephoneNumber() != null) {
								telNumber = beneficaryContact.getTelephoneNumber();
							} else if (beneficaryContact.getMobileNumber() != null) {
								telNumber = beneficaryContact.getMobileNumber().toPlainString();
							} else {
								telNumber = null;
							}
							personalRBDataTable.setTelphoneNum(telNumber);
							personalRBDataTable.setTelphoneCode(beneficaryContact.getCountryTelCode());
							personalRBDataTable.setBeneficiaryContactSeqId(beneficaryContact.getBeneficaryTelephoneSeqId());
						}
					}
					personalRBDataTable.setThirdName(rel.getThirdName());
					personalRBDataTable.setThirdNameLocal(rel.getThirdNameLocal());
					personalRBDataTable.setYearOfBirth(rel.getYearOfBirth());
					coustomerBeneficaryDTList.add(personalRBDataTable);
					goNewRemmit(personalRBDataTable);
				}
			}
		}  catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
		
	}

	// call check beneficiary for validate the beneficiary details EX_CHK_BENEFICIARY
	public String checkBeneficiaryDetailsWithProc(PersonalRemmitanceBeneficaryDataTable datatabledetails){

		String errMsg = null;

		try{

			HashMap<String, Object> checkBeneDetails = new HashMap<String, Object>();

			checkBeneDetails.put("BeneBankCountryId", datatabledetails.getCountryId()); // bene bank country id
			checkBeneDetails.put("BeneServiceGroupId", datatabledetails.getServiceGroupId());
			checkBeneDetails.put("BeneBankId", datatabledetails.getBankId());
			checkBeneDetails.put("BeneBankBranchId", datatabledetails.getBranchId());
			checkBeneDetails.put("BeneCurrencyId", datatabledetails.getCurrencyId());
			checkBeneDetails.put("BeneAccountNumber", datatabledetails.getBankAccountNumber());
			checkBeneDetails.put("BeneBankServiceProvider", datatabledetails.getServiceProvider());
			checkBeneDetails.put("BeneFirstName", datatabledetails.getFirstName());
			checkBeneDetails.put("BeneSecondName", datatabledetails.getSecondName());
			checkBeneDetails.put("BeneThirdName", datatabledetails.getThirdName());
			checkBeneDetails.put("BeneFourthName", datatabledetails.getFourthName());
			checkBeneDetails.put("BeneFifthName", datatabledetails.getFiftheName());
			checkBeneDetails.put("BeneArabicFirstName", datatabledetails.getFirstNameLocal());
			checkBeneDetails.put("BeneArabicSecondName", datatabledetails.getSecondNameLocal());
			checkBeneDetails.put("BeneArabicThirdName", datatabledetails.getThirdNameLocal());
			checkBeneDetails.put("BeneArabicFourthName", datatabledetails.getFourthNameLocal());
			checkBeneDetails.put("BeneCountryId", datatabledetails.getBenificaryCountry());
			checkBeneDetails.put("BeneTelephone", getBeneTelePhoneNum());
			checkBeneDetails.put("BeneMobileNumber", getBeneMobilePhoneNum());
			checkBeneDetails.put("BeneStateId", getBeneficiaryStateId());
			checkBeneDetails.put("BeneDistrictId", getBeneficiaryDistId());
			checkBeneDetails.put("BeneCityId", getBeneficiaryCityId());

			errMsg = beneficaryCreation.checkBeneDetailsValidation(checkBeneDetails);

			return errMsg;

		}catch(Exception e){
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			return "";
		}

	}
		
		// checking mandatory Optional - state and district
		public void checkingmandatoryOptional(){
			setMandatoryOptional(true);
			String countryCode = generalService.getCountryCode(getDatabenificarycountry());
			if(countryCode != null){
				if(countryCode.equalsIgnoreCase(Constants.IND_CODE)){
					setMandatoryOptional(false);
				}
			}
		}
		
		
		
		// NEW
		public void goNewRemmit(PersonalRemmitanceBeneficaryDataTable datatabledetails)  throws AMGException {
		setBooRenderAccountDialogBox(false);
		
		try {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			setAmountToRemit(null);
			setEquivalentCurrency(null);
			setEquivalentRemitAmount(null);
			clearTransactionDetails();
			setPersonalRemmitanceBeneficaryDataTables(datatabledetails);

			//beneficiary Country id 
			setDatabenificarycountry(datatabledetails.getBenificaryCountry());
			// beneficiary Country name 
			setDatabenificarycountryname(datatabledetails.getBenificaryCountryName());

			setBeneficiaryAccountType(datatabledetails.getBankAccountTypeId());
			setBeneficiaryStateId(datatabledetails.getStateId());
			setBeneficiaryDistId(datatabledetails.getDistrictId());
			setBeneficiaryCityId(datatabledetails.getCityId());

			setDatabenificarycurrency(datatabledetails.getCurrencyId());
			setDatabenificaryservicegroup(datatabledetails.getServiceGroupName());
			setDataservicegroupid(datatabledetails.getServiceGroupId());
			System.out.println("setAccountNumberSeqId :" + datatabledetails.getBeneficiaryAccountSeqId() + "\t datatabledetails.getBenificaryMasterId() :"
					+ datatabledetails.getBeneficaryMasterSeqId());
			setDataAccountnum(datatabledetails.getBankAccountNumber());
			setDatabenificarycurrencyname(datatabledetails.getCurrencyName());
			setDatabenificarybankname(datatabledetails.getBankName());
			setDatabenificarybranchname(datatabledetails.getBankBranchName());
			setDatabenificaryname(datatabledetails.getBenificaryName());
			setBenificiaryryNameRemittance(datatabledetails.getBenificaryName());
			setMasterId(datatabledetails.getBeneficaryMasterSeqId());
			setBeneficaryBankId(datatabledetails.getBankId());
			setBeneficaryBankBranchId(datatabledetails.getBranchId());
			// beneficiary bank Country Id 
			setDataBankbenificarycountry(datatabledetails.getCountryId());
			//beneficiary bank Country Name 
			setDataBankbenificarycountryname(datatabledetails.getCountryName());
			// account and relationship id
			setBeneficiaryAccountSeqId(datatabledetails.getBeneficiaryAccountSeqId());
			setBeneficiaryRelationShipSeqId(datatabledetails.getBeneficiaryRelationShipSeqId());
			// setting contact details
			setBooRenderBeneTelDisable(true);
			setBooRenderBeneTelMandatory(false);
			setDataBeneContactId(datatabledetails.getBeneficiaryContactSeqId());
			if (datatabledetails.getTelphoneNum() != null) {
				setBenificaryTelephone(datatabledetails.getTelphoneNum());
				setBeneTelePhoneNum(datatabledetails.getTelphoneNum());
			}
			
			//set swift BIC
			if(datatabledetails.getSwiftBic()!=null){
			setSwiftBic(datatabledetails.getSwiftBic());
			}else{
				log.info("datatabledetails.getBankId() :"+datatabledetails.getBankId()+"\t Branch Id :"+datatabledetails.getBranchId());
				List<BankBranchView> bankBranchViewList =iPersonalRemittanceService.toFetchAllDetailsFromBankBranch(datatabledetails.getBankId(),datatabledetails.getBranchId());
				if(bankBranchViewList!=null && bankBranchViewList.size()==1){
					log.info("bankBranchViewList.get(0).getSwift() "+bankBranchViewList.get(0).getSwift());
					setSwiftBic(bankBranchViewList.get(0).getSwift());
				}
				
			}

			List<PopulateData> lstCurrency = new ArrayList<PopulateData>();
			System.out.println("getDatabenificarycurrency() :"+getDatabenificarycurrency()+"\t getDatabenificarycurrencyname() :"+getDatabenificarycurrencyname()+"\t Curreny Name :"+sessionStateManage.getCurrencyId());
			PopulateData populatedata = new PopulateData(getDatabenificarycurrency(), getDatabenificarycurrencyname());
			PopulateData populatedata1 = new PopulateData(new BigDecimal(sessionStateManage.getCurrencyId()), generalService.getCurrencyName(new BigDecimal(sessionStateManage.getCurrencyId())));
			lstCurrency.add(populatedata);
			lstCurrency.add(populatedata1);
			setLstofCurrency(lstCurrency);
			if (lstofCurrency.size() != 0) {
				for (PopulateData lstofcurrency : lstofCurrency) {
					if (lstofcurrency.getPopulateId().compareTo(new BigDecimal(sessionStateManage.getCurrencyId())) != 0) {
						setForiegnCurrency(lstofcurrency.getPopulateId());
					}
				}
			}
			setCurrency(new BigDecimal(sessionStateManage.getCurrencyId()));
			setAvailLoyaltyPoints(Constants.No);

			HashMap<String, String> inputRoutingBankSetUpdetails = new HashMap<String, String>();

			inputRoutingBankSetUpdetails.put("P_APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId().toPlainString());
			//beneficiary bank Country Id 
			inputRoutingBankSetUpdetails.put("P_BENE_COUNTRY_ID", datatabledetails.getCountryId().toPlainString());
			inputRoutingBankSetUpdetails.put("P_BENE_BANK_ID", datatabledetails.getBankId().toPlainString());
			inputRoutingBankSetUpdetails.put("P_BENE_BANK_BRANCH_ID", datatabledetails.getBranchId().toPlainString());
			inputRoutingBankSetUpdetails.put("P_BENE_BANK_ACCOUNT", datatabledetails.getBankAccountNumber());
			inputRoutingBankSetUpdetails.put("P_CUSTOMER_ID", datatabledetails.getCustomerId().toPlainString());
			List<ServiceGroupMasterDesc> lstServiceCode = iServiceGroupMasterService.getServiceGroupDescList(datatabledetails.getServiceGroupId());
			if (lstServiceCode != null) {
				ServiceGroupMasterDesc ServiceCode = lstServiceCode.get(0);
				inputRoutingBankSetUpdetails.put("P_SERVICE_GROUP_CODE", ServiceCode.getServiceGroupMasterId().getServiceGroupCode());
				setServiceGroupCode(ServiceCode.getServiceGroupMasterId().getServiceGroupCode());
			}
			inputRoutingBankSetUpdetails.put("P_CURRENCY_ID", datatabledetails.getCurrencyId().toPlainString());
			try {
				/**
				 * validating EX_P_VALIDATE_BENEFICIARY. added by Rabil on
				 * 03/12/2015.
				 */
				String proceValiMessage = null;
				proceValiMessage = iPersonalRemittanceService.getValidateBeneficiaryProcedure(sessionStateManage.getCountryId(), getCustomerNo(), sessionStateManage.getUserName(), getMasterId(),
						getBeneficiaryAccountSeqId());
				if (proceValiMessage != null && proceValiMessage.length() > 0) {

					checkingmandatoryOptional();

					if(getMandatoryOptional()){
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() != null && datatabledetails.getDistrictId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(" EX_P_VALIDATE_BENEFICIARY : " + proceValiMessage);
								throw new AMGException(getProcedureError());
								//RequestContext.getCurrentInstance().execute("procedureErr.show();");
							}else{
								fetchAccountDetailsBene();
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() != null && datatabledetails.getStateId() != null && datatabledetails.getDistrictId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(" EX_P_VALIDATE_BENEFICIARY : " + proceValiMessage);
								//RequestContext.getCurrentInstance().execute("procedureErr.show();");
								throw new AMGException(getProcedureError());
							}else{
								fetchAccountDetailsBene();
							}
						}
					}else{
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(" EX_P_VALIDATE_BENEFICIARY : " + proceValiMessage);
								//RequestContext.getCurrentInstance().execute("procedureErr.show();");
								throw new AMGException(getProcedureError());
							}else{
								fetchAccountDetailsBene();
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() != null && datatabledetails.getStateId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(" EX_P_VALIDATE_BENEFICIARY : " + proceValiMessage);
								//RequestContext.getCurrentInstance().execute("procedureErr.show();");
								throw new AMGException(getProcedureError());
							}else{
								fetchAccountDetailsBene();
							}
						}
					}

					return;
				}else{

					checkingmandatoryOptional();

					if(getMandatoryOptional()){
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() == null || datatabledetails.getDistrictId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() == null || datatabledetails.getStateId() == null || datatabledetails.getDistrictId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}
					}else{
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() == null || datatabledetails.getStateId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}
					}
				}

				/*
				 * As per gini m'am to stop. on 12 112016
				 * HashMap<String, String> bannedBankProcedureOut = iPersonalRemittanceService.getBannedBankCheckProcedure(sessionStateManage.getCountryId(), getBeneficaryBankId(), getMasterId());
				if (bannedBankProcedureOut.get("P_ERROR_MESSAGE") != null && !bannedBankProcedureOut.get("P_ERROR_MESSAGE").equals("")) {
					System.out.println("P_error_message :" + bannedBankProcedureOut.get("P_ERROR_MESSAGE"));
					setProcedureError("EX_P_BANNED_BANK_CHECK " + bannedBankProcedureOut.get("P_ERROR_MESSAGE"));
					RequestContext.getCurrentInstance().execute("procedureErr.show();");
					return;
				} else if (bannedBankProcedureOut.get("P_ALERT_MESSAGE") != null && !bannedBankProcedureOut.get("P_ALERT_MESSAGE").equals("")) {
					System.out.println("P_ALERT_MESSAGE :" + bannedBankProcedureOut.get("P_ALERT_MESSAGE"));
					setProcedureError("EX_P_BANNED_BANK_CHECK " + bannedBankProcedureOut.get("P_ALERT_MESSAGE"));
					RequestContext.getCurrentInstance().execute("procedureErr.show();");
				}*/
				log.info("Banch ID :" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("branchId"));
				System.out.println("sessionStateManage.getBranchId()  :" + sessionStateManage.getBranchId() + "\t FacesContext.getCurrentInstance().getExternalContext().getSessionMap() :"
						+ FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("branchId"));
					inputRoutingBankSetUpdetails.put("P_USER_TYPE", "ONLINE");
				HashMap<String, String> outputRoutingBankSetUpdetails = iPersonalRemittanceService.getRoutingBankSetupDetails(inputRoutingBankSetUpdetails);
				System.out.println("P_ERROR_MESSAGE" + outputRoutingBankSetUpdetails.toString());
				if (outputRoutingBankSetUpdetails.get("P_ERROR_MESSAGE") != null) {
					setProcedureError("EX_GET_ROUTING_SET_UP_OTH" + " : " + outputRoutingBankSetUpdetails.get("P_ERROR_MESSAGE"));
					throw new AMGException(getProcedureError());
					//RequestContext.getCurrentInstance().execute("procedureErr.show();");
					//return;
				} else {

					if (!outputRoutingBankSetUpdetails.get("P_SERVICE_MASTER_ID").equalsIgnoreCase("0")) {
						setDataserviceid(new BigDecimal(outputRoutingBankSetUpdetails.get("P_SERVICE_MASTER_ID")));
						String serviceMaster = null;
						//List<ServiceMasterDesc>  lstService = generalService.LocalServiceDescriptionFromDB(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"),getDataserviceid());
						List<ServiceMasterDesc>  lstService = generalService.LocalServiceDescriptionFromDB(new BigDecimal("1"),getDataserviceid());
						if(lstService != null && lstService.size() != 0){
							serviceMaster = lstService.get(0).getLocalServiceDescription();
						}
						setDatabenificaryservice(serviceMaster);

						System.out.println("Service Desc :" + getDatabenificaryservice() + "\t setDatabenificaryservice :" + getDatabenificaryservice());
						if (!outputRoutingBankSetUpdetails.get("P_ROUTING_COUNTRY_ID").equalsIgnoreCase("0")) {
							setRoutingCountry(new BigDecimal(outputRoutingBankSetUpdetails.get("P_ROUTING_COUNTRY_ID")));
							//String lstRcountry = generalService.getCountryName(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"),getRoutingCountry());
							
							String lstRcountry = generalService.getCountryName(new BigDecimal("1"),getRoutingCountry());
							if (lstRcountry != null) {
								setRoutingCountryName(lstRcountry);
							}
							if (!outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_ID").equalsIgnoreCase("0")) {
								setRoutingBank(new BigDecimal(outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_ID")));
								String lstRBank = generalService.getBankName(getRoutingBank());
								if (lstRBank != null) {
									setRoutingBankName(lstRBank);
								}
								if (!outputRoutingBankSetUpdetails.get("P_REMITTANCE_MODE_ID").equalsIgnoreCase("0")) {
									setRemitMode(new BigDecimal(outputRoutingBankSetUpdetails.get("P_REMITTANCE_MODE_ID")));
									//String remitName = generalService.getRemittanceDesc(getRemitMode(),new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
									String remitName = generalService.getRemittanceDesc(getRemitMode(),new BigDecimal("1"));
									if (remitName != null) {
										setRemittanceName(remitName);
									}
									if (!outputRoutingBankSetUpdetails.get("P_DELIVERY_MODE_ID").equalsIgnoreCase("0")) {
										setDeliveryMode(new BigDecimal(outputRoutingBankSetUpdetails.get("P_DELIVERY_MODE_ID")));
										//String deliveryName = generalService.getDeliveryDesc(getDeliveryMode(),new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
										String deliveryName = generalService.getDeliveryDesc(getDeliveryMode(),new BigDecimal("1"));
										if (deliveryName != null) {
											setDeliveryModeInput(deliveryName);
										}
										if (!outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_BRANCH_ID").equalsIgnoreCase("0")) {
											setRoutingBranch(new BigDecimal(outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_BRANCH_ID"))); // routing
											List<BankBranch> lstRbranch = generalService.getBankBranchByBranchID(getRoutingBranch());
											if (lstRbranch != null && lstRbranch.size() != 0) {
												BankBranch routingBranchName = lstRbranch.get(0);
												setRoutingBranchName(routingBranchName.getBranchFullName());
											}
											
											if (outputRoutingBankSetUpdetails.get("P_BENEFICIARY_SWIFT_BANK1") != null && !outputRoutingBankSetUpdetails.get("P_BENEFICIARY_SWIFT_BANK1").equalsIgnoreCase("")) {
												// Beneficiary Bank Swift Code
												setBeneSwiftBank1(outputRoutingBankSetUpdetails.get("P_BENEFICIARY_SWIFT_BANK1"));
											}else{
												setBeneSwiftBank1(null);
											}
										} 
									} 
								} 
							}
						} 
					} 
				}

			} catch (AMGException e) {
				setProcedureError(e.getMessage());
				//RequestContext.getCurrentInstance().execute("procedureErr.show();");
				throw new AMGException(getProcedureError());
			}

			beneficiaryMaster = iPersonalRemittanceService.getAllMasterValues(getMasterId());
			if (beneficiaryMaster.size() > 0) {
				setBenificarystatus(beneficiaryMaster.get(0).getBeneficaryStatusName());
			}


			setSpotRate(Constants.No);
			setSpecialRateRef(Constants.No);
			setAvailLoyaltyPoints(Constants.No);
			setChargesOverseas(Constants.No);

			// customer telephone number fetching
			HashMap<String, String> pValidatecustTelInPut = new HashMap<String, String>();
			pValidatecustTelInPut.put("APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId().toPlainString());
			pValidatecustTelInPut.put("CUSTOMER_ID", getCustomerNo().toPlainString());
			try {
				HashMap<String, String> pValidatecustTelOutPut = iPersonalRemittanceService.pValidateCustomerTelephoneDetails(pValidatecustTelInPut);
				if (pValidatecustTelOutPut.get("P_ERROR_MESSAGE") != null) {
					setProcedureError(pValidatecustTelOutPut.get("P_ERROR_MESSAGE"));
					RequestContext.getCurrentInstance().execute("procedureErr.show();");
					return;
				} else {
					if (pValidatecustTelOutPut.get("P_CONTACT_ID") != null) {
						setDataCustomerContactId(new BigDecimal(pValidatecustTelOutPut.get("P_CONTACT_ID")));
					} else {
						setDataCustomerContactId(null);
					}
					if (pValidatecustTelOutPut.get("P_TELEPHONE_NUMBER") != null) {
						setDataCustomerTelephoneNumber(pValidatecustTelOutPut.get("P_TELEPHONE_NUMBER"));
						setDataTempCustomerMobile(pValidatecustTelOutPut.get("P_TELEPHONE_NUMBER"));
						// setBooRenderCustTelMandatory(false);
						setBooRenderCustTelDisable(true);
					} else {
						setDataCustomerTelephoneNumber(null);
						setDataTempCustomerMobile(null);
						setBooRenderCustTelDisable(false);
					}
				}
			} catch (AMGException e) {
				setExceptionMessage(e.getMessage());
				//RequestContext.getCurrentInstance().execute("alertmsg.show();");
				throw new AMGException(getExceptionMessage());
			} catch (Exception e) {
				setExceptionMessage(e.getMessage());
				throw new AMGException(getExceptionMessage());
				//RequestContext.getCurrentInstance().execute("alertmsg.show();");
			}
			setBooRenderTransferFundPanel(true);
			setBooRenderAdditionalDataPanel(false);
			setBooRenderDebitCardPanel(false);
		} catch (Exception e) {
			System.out.println("Error :" + e.getMessage());
			setExceptionMessage(e.getMessage());
			//RequestContext.getCurrentInstance().execute("alertmsg.show();");
			throw new AMGException(getExceptionMessage());
		}
	}

		
	
	/**
	 * Old  Method 
	 * @throws AMGException
	 */
	/*public void goNewRemmit(PersonalRemmitanceBeneficaryDataTable datatabledetails) throws AMGException {
	 try{
		 
		 setBooRenderAccountDialogBox(false);
		String errMsg = checkBeneficiaryDetailsWithProc(datatabledetails);
		if(errMsg != null && !errMsg.equalsIgnoreCase("")){
			setProcedureError(" Please Contact Branch Manager for Modification of Beneficiary Details");
			throw new AMGException(getProcedureError());
		}else{
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			setAmountToRemit(null);
			setEquivalentCurrency(null);
			setEquivalentRemitAmount(null);
			clearTransactionDetails();
			 beneficiary Country  id
			setPersonalRemmitanceBeneficaryDataTables(datatabledetails);
			setDatabenificarycountry(datatabledetails.getBenificaryCountry()); 
			setBeneficiaryAccountType(datatabledetails.getBankAccountTypeId());
			setBeneficiaryStateId(datatabledetails.getStateId());
			setBeneficiaryDistId(datatabledetails.getDistrictId());
			setBeneficiaryCityId(datatabledetails.getCityId());
			 beneficiary Country  name
			setDatabenificarycountryname(datatabledetails.getBenificaryCountryName()); 
			setDatabenificarycurrency(datatabledetails.getCurrencyId());
			setDatabenificaryservicegroup(datatabledetails.getServiceGroupName());
			setDataservicegroupid(datatabledetails.getServiceGroupId());
			log.info("goNewRemmit  ----- > setAccountNumberSeqId :" + datatabledetails.getBeneficiaryAccountSeqId() + "\t datatabledetails.getBenificaryMasterId() :" + datatabledetails.getBeneficaryMasterSeqId());
			setDataAccountnum(datatabledetails.getBankAccountNumber());
			setDatabenificarycurrencyname(datatabledetails.getCurrencyName());
			setDatabenificarybankname(datatabledetails.getBankName());
			setDatabenificarybranchname(datatabledetails.getBankBranchName());
			setDatabenificaryname(datatabledetails.getBenificaryName());
			setBenificiaryryNameRemittance(datatabledetails.getBenificaryName());
			setMasterId(datatabledetails.getBeneficaryMasterSeqId());
			setBeneficaryBankId(datatabledetails.getBankId());
			setBeneficaryBankBranchId(datatabledetails.getBranchId());
			 beneficiary bank Country Id 
			setDataBankbenificarycountry(datatabledetails.getCountryId());
			 beneficiary bank Country Name 
			setDataBankbenificarycountryname(datatabledetails.getCountryName());
			// account and relationship id
			setBeneficiaryAccountSeqId(datatabledetails.getBeneficiaryAccountSeqId());
			setBeneficiaryRelationShipSeqId(datatabledetails.getBeneficiaryRelationShipSeqId());
			// setting contact details
			setBooRenderBeneTelDisable(true);
			setBooRenderBeneTelMandatory(false);
			setDataBeneContactId(datatabledetails.getBeneficiaryContactSeqId());
			if (datatabledetails.getTelphoneNum() != null) {
				setBenificaryTelephone(datatabledetails.getTelphoneNum());
			}
			HashMap<String, String> inputRoutingBankSetUpdetails = new HashMap<String, String>();
			inputRoutingBankSetUpdetails.put("P_APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId().toPlainString());
			inputRoutingBankSetUpdetails.put("P_BENE_COUNTRY_ID", datatabledetails.getCountryId().toPlainString());
			inputRoutingBankSetUpdetails.put("P_BENE_BANK_ID", datatabledetails.getBankId().toPlainString());
			inputRoutingBankSetUpdetails.put("P_BENE_BANK_BRANCH_ID", datatabledetails.getBranchId().toPlainString());
			List<ServiceGroupMasterDesc> lstServiceCode = iServiceGroupMasterService.getServiceGroupDescList(datatabledetails.getServiceGroupId());
			if (lstServiceCode != null) {
				ServiceGroupMasterDesc ServiceCode = lstServiceCode.get(0);
				inputRoutingBankSetUpdetails.put("P_SERVICE_GROUP_CODE", ServiceCode.getServiceGroupMasterId().getServiceGroupCode());
				setServiceGroupCode(ServiceCode.getServiceGroupMasterId().getServiceGroupCode());
			}
			inputRoutingBankSetUpdetails.put("P_CURRENCY_ID", datatabledetails.getCurrencyId().toPlainString());
			  
				String proceValiMessage = null;
				proceValiMessage = iPersonalRemittanceService.getValidateBeneficiaryProcedure(sessionStateManage.getCountryId(), getCustomerNo(), sessionStateManage.getUserName(), getMasterId(),getBeneficiaryAccountSeqId());
				
				if (proceValiMessage != null && proceValiMessage.length() > 0) {

					checkingmandatoryOptional();

					if(getMandatoryOptional()){
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() != null && datatabledetails.getDistrictId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(proceValiMessage);
								//	RequestContext.getCurrentInstance().execute("procedureErr.show();");
								 throw new AMGException(getProcedureError());
							}else{
								fetchAccountDetailsBene();
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() != null && datatabledetails.getStateId() != null && datatabledetails.getDistrictId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(proceValiMessage);
								//	RequestContext.getCurrentInstance().execute("procedureErr.show();");
									throw new AMGException(getProcedureError());
							}else{
								fetchAccountDetailsBene();
							}
						}
					}else{
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(proceValiMessage);
								//	RequestContext.getCurrentInstance().execute("procedureErr.show();");
									throw new AMGException(getProcedureError());
							}else{
								fetchAccountDetailsBene();
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() != null && datatabledetails.getStateId() != null && datatabledetails.getTelphoneNum() != null){
								setProcedureError(proceValiMessage);
								//	RequestContext.getCurrentInstance().execute("procedureErr.show();");
									throw new AMGException(getProcedureError());
							}else{
								fetchAccountDetailsBene();
							}
						}
					}

					return;
				}else{

					checkingmandatoryOptional();

					if(getMandatoryOptional()){
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() == null || datatabledetails.getDistrictId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() == null || datatabledetails.getStateId() == null || datatabledetails.getDistrictId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}
					}else{
						if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
							if(datatabledetails.getStateId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}else{
							if(datatabledetails.getBankAccountTypeId() == null || datatabledetails.getStateId() == null || datatabledetails.getTelphoneNum() == null){
								fetchAccountDetailsBene();
								return;
							}
						}
					}
				}
				
				
				HashMap<String, String> bannedBankProcedureOut = iPersonalRemittanceService.getBannedBankCheckProcedure(sessionStateManage.getCountryId(), getBeneficaryBankId(), getMasterId());
				if (bannedBankProcedureOut.get("P_ERROR_MESSAGE") != null && !bannedBankProcedureOut.get("P_ERROR_MESSAGE").equals("")) {
					log.error("P_error_message :" + bannedBankProcedureOut.get("P_ERROR_MESSAGE"));
					setProcedureError(bannedBankProcedureOut.get("P_ERROR_MESSAGE"));
					
					throw new AMGException(getProcedureError());
				} else if (bannedBankProcedureOut.get("P_ALERT_MESSAGE") != null && !bannedBankProcedureOut.get("P_ALERT_MESSAGE").equals("")) {
					log.error("P_ALERT_MESSAGE :" + bannedBankProcedureOut.get("P_ALERT_MESSAGE"));
					setProcedureError(bannedBankProcedureOut.get("P_ALERT_MESSAGE"));
					throw new AMGException(getProcedureError());
				}
				
				
				log.info("Banch ID :" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("branchId"));
				log.info ("sessionStateManage.getBranchId()  :" + sessionStateManage.getBranchId() + "\t FacesContext.getCurrentInstance().getExternalContext().getSessionMap() :"+ FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("branchId"));
				// Added by Rabil.
				if ((sessionStateManage.getBranchId() != null && sessionStateManage.getCustomerType().equals("E"))) { // &&
					inputRoutingBankSetUpdetails.put("P_USER_TYPE", "BRANCH");
				} else if (sessionStateManage.getBranchId() != null && sessionStateManage.getUserType().equalsIgnoreCase("K")) {
					inputRoutingBankSetUpdetails.put("P_USER_TYPE", "KIOSK");
				} else if (sessionStateManage.getBranchId() != null && sessionStateManage.getUserType().equalsIgnoreCase("U")) {
					inputRoutingBankSetUpdetails.put("P_USER_TYPE", "ONLINE");
				}
				if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
					List<BeneficaryAccount> lstBeneficaryAccount = iPersonalRemittanceService.getCashProductDetails(datatabledetails.getBeneficiaryAccountSeqId());
					if (lstBeneficaryAccount!=null && lstBeneficaryAccount.size() > 0) {
						BeneficaryAccount beneficaryAccount = lstBeneficaryAccount.get(0);
						// setting service Master Id
						List<ServiceMaster> lstServiceMaster = generalService.toFetchServiceRecordsServiceMaster(beneficaryAccount.getServicegropupId().getServiceGroupId());
						if (lstServiceMaster.size() != 0) {
							ServiceMaster serviceMasterId = lstServiceMaster.get(0);
							setDataserviceid(serviceMasterId.getServiceId());
							setDatabenificaryservice(generalService .LocalServiceDescriptionFromDB(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"),getDataserviceid()).get(0).getLocalServiceDescription());
							log.info ("Service Desc :" + getDatabenificaryservice() + "\t setDatabenificaryservice :" + getDatabenificaryservice());
						}
						setRoutingCountry(beneficaryAccount.getBank().getFsCountryMaster().getCountryId());
						String lstRcountry = generalService.getCountryName(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"),getRoutingCountry());
						if (lstRcountry != null) {
							setRoutingCountryName(lstRcountry);
						}
						if (beneficaryAccount.getServiceProvider() != null) {
							setRoutingBank(beneficaryAccount.getServiceProvider().getBankId());
							String lstRBank = generalService.getBankName(getRoutingBank());
							if (lstRBank != null) {
								setRoutingBankName(lstRBank);
							}
						}
						if (beneficaryAccount.getBankBranch() != null) {
							setRoutingBranch(beneficaryAccount.getServiceProviderBranchId());  
							List<BankBranch> lstRbranch = generalService.getBankBranchByBranchID(getRoutingBranch());
							if (lstRbranch != null && lstRbranch.size() != 0) {
								BankBranch routingBranchName = lstRbranch.get(0);
								setRoutingBranchName(routingBranchName.getBranchFullName());
							}
						}
						// to fetch remittance
						remittancelistByBankIdForCash();
						// to fetch remittance
					} else {
					}
				} else {
					HashMap<String, String> outputRoutingBankSetUpdetails = iPersonalRemittanceService.getRoutingBankSetupDetails(inputRoutingBankSetUpdetails);
					log.info ("P_ERROR_MESSAGE" + outputRoutingBankSetUpdetails.get("P_ERROR_MESSAGE"));
					if (outputRoutingBankSetUpdetails.get("P_ERROR_MESSAGE") != null) {
						setProcedureError(outputRoutingBankSetUpdetails.get("P_ERROR_MESSAGE"));
						throw new AMGException(getProcedureError());
					} else {
						if (!outputRoutingBankSetUpdetails.get("P_SERVICE_MASTER_ID").equalsIgnoreCase("0")) {
							setDataserviceid(new BigDecimal(outputRoutingBankSetUpdetails.get("P_SERVICE_MASTER_ID")));
							setDatabenificaryservice(generalService.LocalServiceDescriptionFromDB(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"),
											getDataserviceid()).get(0).getLocalServiceDescription());
							setBooSingleService(true);
							setBooMultipleService(false);
							log.info ("Service Desc :" + getDatabenificaryservice() + "\t setDatabenificaryservice :" + getDatabenificaryservice());
							if (!outputRoutingBankSetUpdetails.get("P_ROUTING_COUNTRY_ID").equalsIgnoreCase("0")) {
								setRoutingCountry(new BigDecimal(outputRoutingBankSetUpdetails.get("P_ROUTING_COUNTRY_ID")));
								String lstRcountry = generalService.getCountryName(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"),
										getRoutingCountry());
								if (lstRcountry != null) {
									setRoutingCountryName(lstRcountry);
								}
								if (!outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_ID").equalsIgnoreCase("0")) {
									setRoutingBank(new BigDecimal(outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_ID")));
									String lstRBank = generalService.getBankName(getRoutingBank());
									if (lstRBank != null) {
										setRoutingBankName(lstRBank);
									}
									if (!outputRoutingBankSetUpdetails.get("P_REMITTANCE_MODE_ID").equalsIgnoreCase("0")) {
										// spl pool based on routing country ,
										// routing bank
										setRemitMode(new BigDecimal(outputRoutingBankSetUpdetails.get("P_REMITTANCE_MODE_ID")));
										String remitName = generalService.getRemittanceDesc(getRemitMode(),
												new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
										if (remitName != null) {
											setRemittanceName(remitName);
										}
										if (!outputRoutingBankSetUpdetails.get("P_DELIVERY_MODE_ID").equalsIgnoreCase("0")) {
											setDeliveryMode(new BigDecimal(outputRoutingBankSetUpdetails.get("P_DELIVERY_MODE_ID")));
											String deliveryName = generalService.getDeliveryDesc(getDeliveryMode(),
													new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
											if (deliveryName != null) {
												setDeliveryModeInput(deliveryName);
											}
											if (!outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_BRANCH_ID").equalsIgnoreCase("0")) {
												// set All values in form , No
												// Need of view
												setRoutingBranch(new BigDecimal(outputRoutingBankSetUpdetails.get("P_ROUTING_BANK_BRANCH_ID"))); // routing
																																					// Bank
																																					// Branch
																																					// Id
												List<BankBranch> lstRbranch = generalService.getBankBranchByBranchID(getRoutingBranch());
												if (lstRbranch != null && lstRbranch.size() != 0) {
													BankBranch routingBranchName = lstRbranch.get(0);
													setRoutingBranchName(routingBranchName.getBranchFullName());
												}
											} else {
												// to fetch Bank Branch
												bankBranchByBankView();
											}
										} else {
											// to fetch Delivery
											deliverylistByRemitId();
										}
									} else {
										// to fetch remittance
										remittancelistByBankId();
									}
								} else {
									// setting routing bank Id from view
									bankDetailsByCountry();
								}
							} else {
								// fetch routing country from view
								List<PopulateData> lstRoutingCountry = iPersonalRemittanceService.getRoutingCountryList(sessionStateManage.getCountryId(), getBeneficaryBankId(),
										getBeneficaryBankBranchId(), datatabledetails.getBenificaryCountry(), getDatabenificarycurrency(), getDataserviceid(),
										new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
								if (lstRoutingCountry.size() == 0) {
									setBooSingleRoutingCountry(true);
									setBooMultipleRoutingCountry(false);
									setRoutingCountryName(null);
									setRoutingCountry(null);
									throw new AMGException(props.getString("lbl.routingCountryNotAvailable"));
								} else if (lstRoutingCountry.size() == 1) {
									setBooSingleRoutingCountry(true);
									setBooMultipleRoutingCountry(false);
									setRoutingCountry(lstRoutingCountry.get(0).getPopulateId());
									setRoutingCountryName(lstRoutingCountry.get(0).getPopulateName());
									// setting routing bank Id from view
									bankDetailsByCountry();
								} else {
									setRoutingCountryName(null);
									setBooSingleRoutingCountry(false);
									setBooMultipleRoutingCountry(true);
									setRoutingCountrylst(lstRoutingCountry);
								}
							}
						} else {  
									// Added by Rabil for service.
							setDataserviceid(new BigDecimal(outputRoutingBankSetUpdetails.get("P_SERVICE_MASTER_ID")));
							getServiceListDetails();
						}
					}
					setBooRenderAgent(false);
					setBooRenderRouting(true);
				}
			 
			if (getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
				setBooRenderAgent(true);
				setBooRenderRouting(false);
			} else {
				setBooRenderAgent(false);
				setBooRenderRouting(true);
			}
			// to get Beneficary status from Benificary Master
			beneficiaryMaster = iPersonalRemittanceService.getAllMasterValues(getMasterId());
			if (beneficiaryMaster.size() > 0) {
				setBenificarystatus(beneficiaryMaster.get(0).getBeneficaryStatusName());
			}
			
			setLstofCurrency(null);
			List<PopulateData> lstCurrency = new ArrayList<PopulateData>();
			PopulateData populatedata = new PopulateData(getDatabenificarycurrency(), getDatabenificarycurrencyname());
			PopulateData populatedata1 = new PopulateData(new BigDecimal(sessionStateManage.getCurrencyId()), generalService.getCurrencyName(new BigDecimal(sessionStateManage.getCurrencyId())));
			lstCurrency.add(populatedata);
			lstCurrency.add(populatedata1);
			setLstofCurrency(lstCurrency);
			if (lstofCurrency.size() != 0) {
				for (PopulateData lstofcurrency : lstofCurrency) {
					if (lstofcurrency.getPopulateId().compareTo(new BigDecimal(sessionStateManage.getCurrencyId())) != 0) {
						setForiegnCurrency(lstofcurrency.getPopulateId());
					}
				}
			}
			setCurrency(new BigDecimal(sessionStateManage.getCurrencyId()));
			setSpotRate(Constants.No);
			setSpecialRateRef(Constants.No);
			setAvailLoyaltyPoints(Constants.No);
			setChargesOverseas(Constants.No);
			// customer telephone number fetching
			HashMap<String, String> pValidatecustTelInPut = new HashMap<String, String>();
			pValidatecustTelInPut.put("APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId().toPlainString());
			pValidatecustTelInPut.put("CUSTOMER_ID", getCustomerNo().toPlainString());
			 
				HashMap<String, String> pValidatecustTelOutPut = iPersonalRemittanceService.pValidateCustomerTelephoneDetails(pValidatecustTelInPut);
				if (pValidatecustTelOutPut.get("P_ERROR_MESSAGE") != null) {
					setProcedureError(pValidatecustTelOutPut.get("P_ERROR_MESSAGE"));
					
					throw new AMGException(getProcedureError());
				} else {
					if (pValidatecustTelOutPut.get("P_CONTACT_ID") != null) {
						setDataCustomerContactId(new BigDecimal(pValidatecustTelOutPut.get("P_CONTACT_ID")));
					} else {
						setDataCustomerContactId(null);
					}
					if (pValidatecustTelOutPut.get("P_TELEPHONE_NUMBER") != null) {
						setDataCustomerTelephoneNumber(pValidatecustTelOutPut.get("P_TELEPHONE_NUMBER"));
						setDataTempCustomerMobile(pValidatecustTelOutPut.get("P_TELEPHONE_NUMBER"));
						setBooRenderCustTelDisable(true);
					} else {
						setDataCustomerTelephoneNumber(null);
						setDataTempCustomerMobile(null);
						setBooRenderCustTelDisable(false);
					}
				}
			 
			setBooRenderTransferFundPanel(true);
			setBooRenderAdditionalDataPanel(false);
			setBooRenderDebitCardPanel(false);
			
		// }	
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}*/

	 
		// account type, country , state , district , city
		public void fetchAccountDetailsBene()throws AMGException{
			
			clearAccountDetails();
			toFetchAllCountry();
			setBooRenderAccountDialogBox(true);

			if(getDataAccountnum() != null){
				toFetchAccountType();
				setBankingChannelProducts(true);
			}else{
				setBankingChannelProducts(false);
			}

			toFetchStateListBasedOnCountry();
			if(getBeneficiaryStateId() != null){
				toFetchDistrictListBeasedOnCountryAndState();
			}
			if(getBeneficiaryDistId() != null){
				toFetchCityBasedOnDist();
			}
			fetchTelMobNumberDetails();
			RequestContext.getCurrentInstance().execute("beneAccountPanel.show();");
		}
	
		// fetch bene tel , mob 
		public void fetchTelMobNumberDetails()throws AMGException{
			try{
			List<BeneficaryContact> telePhone = beneficaryCreation.getTelephoneDetails(getMasterId());
			if (telePhone != null && telePhone.size() != 0) {
				if (telePhone.size() == 1) {
					if (telePhone.get(0).getTelephoneNumber() != null) {
						setBeneTelePhoneNum(telePhone.get(0).getTelephoneNumber());
					} 
					if (telePhone.get(0).getMobileNumber() != null) {
						setBeneMobilePhoneNum(telePhone.get(0).getMobileNumber());
					}
					if(telePhone.get(0).getCountryTelCode() != null){
						setTelePhoneCode(telePhone.get(0).getCountryTelCode());
						setMobileCode(telePhone.get(0).getCountryTelCode());
					}else{
						List<CountryMasterDesc> lstCountryCode = beneficaryCreation.fetchCountryContactCode(getDatabenificarycountry());
						
						
						if(lstCountryCode != null && lstCountryCode.size() != 0){
							CountryMasterDesc countryMaster = lstCountryCode.get(0);
							if(countryMaster.getFsCountryMaster().getCountryTelCode() != null){
								setTelePhoneCode(countryMaster.getFsCountryMaster().getCountryTelCode());
								setMobileCode(countryMaster.getFsCountryMaster().getCountryTelCode());
							}
						}
						
						
					}
					if(telePhone.get(0).getBeneficaryTelephoneSeqId() != null){
						setBeneficiaryContactSeqId(telePhone.get(0).getBeneficaryTelephoneSeqId());
					}

				} else {
					BeneficaryContact beneficaryContact = telePhone.get(0);
					if (beneficaryContact.getTelephoneNumber() != null) {
						setBeneTelePhoneNum(beneficaryContact.getTelephoneNumber());
					} 
					if (beneficaryContact.getMobileNumber() != null) {
						setBeneMobilePhoneNum(beneficaryContact.getMobileNumber());
					}
					if(beneficaryContact.getCountryTelCode() != null){
						setTelePhoneCode(beneficaryContact.getCountryTelCode());
						setMobileCode(beneficaryContact.getCountryTelCode());
					}else{
						List<CountryMasterDesc> lstCountryCode = beneficaryCreation.fetchCountryContactCode(getDatabenificarycountry());
						if(lstCountryCode != null && lstCountryCode.size() != 0){
							CountryMasterDesc countryMaster = lstCountryCode.get(0);
							if(countryMaster.getFsCountryMaster().getCountryTelCode() != null){
								setTelePhoneCode(countryMaster.getFsCountryMaster().getCountryTelCode());
								setMobileCode(countryMaster.getFsCountryMaster().getCountryTelCode());
							}
						}
					}
					if(beneficaryContact.getBeneficaryTelephoneSeqId() != null){
						setBeneficiaryContactSeqId(beneficaryContact.getBeneficaryTelephoneSeqId());
					}
				}
			}else{
				List<CountryMasterDesc> lstCountryCode = beneficaryCreation.fetchCountryContactCode(getDatabenificarycountry());
				if(lstCountryCode != null && lstCountryCode.size() != 0){
					CountryMasterDesc countryMaster = lstCountryCode.get(0);
					if(countryMaster.getFsCountryMaster().getCountryTelCode() != null){
						setTelePhoneCode(countryMaster.getFsCountryMaster().getCountryTelCode());
						setMobileCode(countryMaster.getFsCountryMaster().getCountryTelCode());
					}
				}
			}
			}catch(NullPointerException ne){
				log.error("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
				setExceptionMessage(ne.getMessage()); 
				throw new AMGException(getErrorMessage());
			}catch(Exception exception){
				log.error("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
				setExceptionMessage(exception.getMessage()); 
				throw new AMGException(getErrorMessage());
			}
		}
		
		
		// clear the account details
		public void clearAccountDetails(){
			if(lstBankAccountTypeFromView != null || lstBankAccountTypeFromView.size() != 0){
				lstBankAccountTypeFromView.clear();
			}
			if(lstStateMasterDescs != null || lstStateMasterDescs.size() != 0){
				lstStateMasterDescs.clear();
			}
			if(lstDistrictMasterDescs != null || lstDistrictMasterDescs.size() != 0){
				lstDistrictMasterDescs.clear();
			}
			if(lstCityMasterDescs != null || lstCityMasterDescs.size() != 0){
				lstCityMasterDescs.clear();
			}
		}

		
		public void toFetchAllCountry() throws AMGException{
			lstCountryMasterDescs.clear();
			try{
				List<CountryMasterView> countryMasterDescs=generalService.getCountryList(sessionStateManage.getLanguageId());
				if(countryMasterDescs!=null && countryMasterDescs.size()>0){
					lstCountryMasterDescs.addAll(countryMasterDescs);
				}
			}catch(NullPointerException ne){
				log.error("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
				setErrorMessage("MethodName::toFetchAllCountry");
				throw new AMGException(getErrorMessage());
				
			}catch(Exception exception){
				log.error("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
				setErrorMessage(exception.getMessage()); 
				throw new AMGException(getErrorMessage());
			}
		}
		
		public void toFetchAccountType()throws AMGException{
			try{
				if(lstBankAccountTypeFromView != null || !lstBankAccountTypeFromView.isEmpty()){
					lstBankAccountTypeFromView.clear();
				}

				if(getDataBankbenificarycountry() != null){
					List<AccountTypeFromView> lstBankAccountTypeFromViewDB = beneficaryCreation.getAccountTypeFromView(getDataBankbenificarycountry());
					if(lstBankAccountTypeFromViewDB !=null && lstBankAccountTypeFromViewDB.size()>0){
						lstBankAccountTypeFromView.addAll(lstBankAccountTypeFromViewDB);
					}
				}

			}catch(NullPointerException ne){
				log.error("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
				setExceptionMessage(ne.getMessage()); 
				throw new AMGException(getErrorMessage());
			}catch(Exception exception){
				log.error("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
				setExceptionMessage(exception.getMessage()); 
				throw new AMGException(getErrorMessage());
			}
		}
		
		public void toFetchDistrictListBeasedOnCountryAndState()throws AMGException{
			try{
				lstDistrictMasterDescs.clear();
				lstCityMasterDescs.clear();
				//List<DistrictMasterDesc> districtMasterDescs=generalService.getDistrictList(sessionStateManage.getLanguageId(),getDatabenificarycountry(),getBeneficiaryStateId());
				List<DistrictMasterDesc> districtMasterDescs=generalService.getDistrictList(new BigDecimal("1"),getDatabenificarycountry(),getBeneficiaryStateId());
				if(districtMasterDescs!=null && districtMasterDescs.size()>0){
					lstDistrictMasterDescs.addAll(districtMasterDescs);
				}
			}catch(NullPointerException ne){
				log.error("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
				setExceptionMessage(ne.getMessage()); 
				throw new AMGException(getErrorMessage());
			}catch(Exception exception){
				log.error("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
				setExceptionMessage(exception.getMessage()); 
				throw new AMGException(getErrorMessage());
			}
		}
		
		
		public void toFetchStateListBasedOnCountry()throws AMGException{
			try{
				lstStateMasterDescs.clear();
				//List<StateMasterDesc> stateMasterDescs=generalService.getStateList(sessionStateManage.getLanguageId(),getDatabenificarycountry());
				List<StateMasterDesc> stateMasterDescs=generalService.getStateList(new BigDecimal("1"),getDatabenificarycountry());
				if(stateMasterDescs!=null && stateMasterDescs.size()>0){
					lstStateMasterDescs.addAll(stateMasterDescs);
				}
			}catch(NullPointerException ne){
				log.error("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
				setExceptionMessage(ne.getMessage());
				throw new AMGException(getErrorMessage());
			}catch(Exception exception){
				log.error("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
				setExceptionMessage(exception.getMessage()); 
				throw new AMGException(getErrorMessage());
			}
		}
		
		public void toFetchCityBasedOnDist()throws AMGException{
			lstCityMasterDescs.clear();
			try{
				//List<CityMasterDesc> cityMasterDescs=generalService.getCityList(sessionStateManage.getLanguageId(),getDatabenificarycountry(),getBeneficiaryStateId(),getBeneficiaryDistId());
				List<CityMasterDesc> cityMasterDescs=generalService.getCityList(new BigDecimal("1"),getDatabenificarycountry(),getBeneficiaryStateId(),getBeneficiaryDistId());
				if(cityMasterDescs!=null && cityMasterDescs.size()>0){
					lstCityMasterDescs.addAll(cityMasterDescs);
				}
			}catch(NullPointerException ne){
				log.error("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
				setExceptionMessage(ne.getMessage()); 
				throw new AMGException(getErrorMessage());
			}catch(Exception exception){
				log.error("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
				setExceptionMessage(exception.getMessage()); 
				throw new AMGException(getErrorMessage());
			}
		}
		
		public void fetchTelcodetoMobileCode(){
			if(getTelePhoneCode() != null){
				setMobileCode(getTelePhoneCode());
			}
		}
		
		public void fetchMobilecodetoTelCode(){
			if(getMobileCode() != null){
				setTelePhoneCode(getMobileCode());
			}
		}

		public void redirectSame(){
			try {
				
				setVisible(false);
				assignNullValues();
				clear();
				setBeneCountr(null);
				setAmountToRemit(null);
				setEquivalentCurrency(null);
				setEquivalentRemitAmount(null);
				popCountry();
				if(getRemitAction()!=null && getRemitAction().equalsIgnoreCase("T")){
				FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_home.xhtml");
				}else if(getRemitAction()!=null && getRemitAction().equalsIgnoreCase("Q")){
					FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/quickRemit.xhtml");
				}else{
					FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_home.xhtml");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// save bene account details
		public void saveBeneficiaryAccountDetails(){
			
			try {
				setExceptionMessage(null);
				setErrorMessage(null);
				// check all mandatory
				// if banking channel only
				if (!getDatabenificaryservicegroup().equalsIgnoreCase(Constants.CASHNAME)) {
					if(getBeneficiaryAccountType() == null){
						setExceptionMessage("Please Select Account type");
						redirectSame();
						return;
					}
				}

				if(getBeneficiaryStateId() == null){
					setExceptionMessage("Please Select State");
					redirectSame();
					return;
				}
				if(getBeneficiaryDistId() == null && mandatoryOptional){
					setExceptionMessage("Please Select District");
					redirectSame();
					return;
				}
				if ((getBeneTelePhoneNum() == null || getBeneTelePhoneNum().trim().equals("")) && getBeneMobilePhoneNum() == null) {
					setExceptionMessage("Please enter Telephone or Mobile");
					redirectSame();
					return;
				} else if (getBeneTelePhoneNum() != null && getTelePhoneCode() == null) {
					setExceptionMessage("Please enter Telephone Country Code");
					redirectSame();
					return;
				} else if (getBeneMobilePhoneNum() != null && getMobileCode() == null) {
					setExceptionMessage("Please enter Mobile Country Code");
					redirectSame();
					return;
				}

				HashMap<String, Object> inputValues = new HashMap<String, Object>();

				inputValues.put("BeneficiaryAccountSeqId", getBeneficiaryAccountSeqId()==null?"":getBeneficiaryAccountSeqId());
				inputValues.put("BeneficaryMasterSeqId", getMasterId() ==null?"":getMasterId());
				inputValues.put("ApplicationCountryId", sessionStateManage.getCountryId()==null?"":sessionStateManage.getCountryId());
				inputValues.put("UseId", sessionStateManage.getUserName());
				inputValues.put("BeneficiaryAccountType", getBeneficiaryAccountType() == null ? null : getBeneficiaryAccountType());

				if(getTelePhoneCode() != null){
					inputValues.put("TeleCode", getTelePhoneCode());
				}else if(getMobileCode() != null){
					inputValues.put("TeleCode", getMobileCode());
				}else{
					inputValues.put("TeleCode", null);
				}

				if(getBeneficiaryStateId()!=null)
				{
					inputValues.put("BeneficiaryStateId", getBeneficiaryStateId());
					//List<StateMasterDesc> stateName = generalService.getStateList(sessionStateManage.getLanguageId(), getDatabenificarycountry() ,getBeneficiaryStateId());
					List<StateMasterDesc> stateName = generalService.getStateList(new BigDecimal("1"), getDatabenificarycountry() ,getBeneficiaryStateId());
					if(stateName != null && stateName.size() != 0){
						inputValues.put("BeneficiaryStateName", stateName.get(0).getStateName());
					}
					inputValues.put("BeneficiaryStateIdValue", "YES");
				}else
				{
					inputValues.put("BeneficiaryStateIdValue", "NO");
				}

				if(getBeneficiaryDistId()!=null)
				{
					inputValues.put("BeneficiaryDistId", getBeneficiaryDistId());
					//List<DistrictMasterDesc> districtName = generalService.getDistrictList(sessionStateManage.getLanguageId(), getDatabenificarycountry(),getBeneficiaryStateId() ,getBeneficiaryDistId());
					List<DistrictMasterDesc> districtName = generalService.getDistrictList(new BigDecimal("1"), getDatabenificarycountry(),getBeneficiaryStateId() ,getBeneficiaryDistId());
					if(districtName != null && districtName.size() != 0){
						inputValues.put("BeneficiaryDistName", districtName.get(0).getDistrict());
					}
					inputValues.put("BeneficiaryDistIdValue", "YES");
				}else
				{
					inputValues.put("BeneficiaryDistIdValue", "NO");
				}
				if(getBeneficiaryCityId()!=null)
				{
					inputValues.put("BeneficiaryCityId", getBeneficiaryCityId());
					//List<CityMasterDesc> cityName = generalService.getCityList(sessionStateManage.getLanguageId(),getDatabenificarycountry(),getBeneficiaryStateId() ,getBeneficiaryDistId(), getBeneficiaryCityId());
					List<CityMasterDesc> cityName = generalService.getCityList(new BigDecimal("1"),getDatabenificarycountry(),getBeneficiaryStateId() ,getBeneficiaryDistId(), getBeneficiaryCityId());
					if(cityName != null && cityName.size() != 0){
						inputValues.put("BeneficiaryCityName", cityName.get(0).getCityName());
					}
					inputValues.put("BeneficiaryCityIdValue", "YES");
				}else
				{
					inputValues.put("BeneficiaryCityIdValue", "NO");
				}
				if(getBeneMobilePhoneNum()!=null)
				{
					inputValues.put("BeneficiaryCountryMobilePhoneNumber", getBeneMobilePhoneNum());
					inputValues.put("BeneficiaryCountryMobilePhoneNumberValue", "YES");
					if(getBeneficiaryContactSeqId() != null)
					{
						inputValues.put("TelePhoneSeqId", getBeneficiaryContactSeqId());
						inputValues.put("TelePhoneSeqIdValue", "YES");
					}else
					{
						inputValues.put("TelePhoneSeqIdValue", "NO");
					}

				}else
				{
					inputValues.put("BeneficiaryCountryMobilePhoneNumberValue", "NO");
				}

				if(getBeneTelePhoneNum()!=null)
				{
					inputValues.put("BeneficiaryCountryTelePhoneNumber", getBeneTelePhoneNum());
					inputValues.put("BeneficiaryCountryTelePhoneNumberValue", "YES");
					if( getBeneficiaryContactSeqId()!=null)
					{
						inputValues.put("TelePhoneSeqId", getBeneficiaryContactSeqId());
						inputValues.put("TelePhoneSeqIdValue", "YES");
					}else
					{
						inputValues.put("TelePhoneSeqIdValue", "NO");
					}
				}else
				{
					inputValues.put("BeneficiaryCountryTelePhoneNumberValue", "NO");
				}

				log.info("Hash Map to Update 6 fields : "+inputValues.toString());

				if(getPersonalRemmitanceBeneficaryDataTables() != null ){

					String errMsg = checkBeneficiaryDetailsWithProc(getPersonalRemmitanceBeneficaryDataTables());
					if(errMsg != null && !errMsg.equalsIgnoreCase("")){
						log.info("EX_CHK_BENEFICIARY:::::::errorMessage::::::::::::::::::::::::"+errMsg);
						setExceptionMessage(errMsg);
						redirectSame();
						return;
					}else{
						iPersonalRemittanceService.saveBeneficiaryContactEdit(inputValues);

						String errorMessage = null;
						if(getPersonalRemmitanceBeneficaryDataTables() != null){
							errorMessage = beneficaryCreation.getBeneDetailProce(getPersonalRemmitanceBeneficaryDataTables().getBeneficaryMasterSeqId(),getPersonalRemmitanceBeneficaryDataTables().getBankId(), getPersonalRemmitanceBeneficaryDataTables().getBranchId(),getPersonalRemmitanceBeneficaryDataTables().getBeneficiaryAccountSeqId(),getPersonalRemmitanceBeneficaryDataTables().getCurrencyId(), getPersonalRemmitanceBeneficaryDataTables().getCustomerId());
						}
						
						
						if (errorMessage == null) {
							
							PersonalRemmitanceBeneficaryDataTable beneficaryDataTable= new  PersonalRemmitanceBeneficaryDataTable();
							beneficaryDataTable.setCurrencyId(getPersonalRemmitanceBeneficaryDataTables().getCurrencyId());
							beneficaryDataTable.setBenificaryCountry(getPersonalRemmitanceBeneficaryDataTables().getBenificaryCountry());
							beneficaryDataTable.setBankId(getPersonalRemmitanceBeneficaryDataTables().getBankId());
							beneficaryDataTable.setBankAccountNumber(getPersonalRemmitanceBeneficaryDataTables().getBankAccountNumber());
							beneficaryDataTable.setBenificaryName(getPersonalRemmitanceBeneficaryDataTables().getBenificaryName());
							if(getRemitAction()!=null && getRemitAction().equalsIgnoreCase("Q")){
								populateCustomerDetailsFromBeneRelation(beneficaryDataTable);
							}else{
								populateCustomerDetailsFromBeneRelation(null);
							}
							setBooRenderAccountDialogBox(false);
							RequestContext.getCurrentInstance().execute("beneAccountPanel.hide();");
							redirectSame();
							return;
						} else {
							log.info("EX_POPULATE_BENE_DT:::::::errorMessage::::::::::::::::::::::::"+errorMessage);
							setExceptionMessage( errorMessage);
							redirectSame();
							return;
						}
					}

				}else{
					log.info("Record Doesnot Exists to Save "+errorMessage);
					setExceptionMessage("Record Doesnot Exists to Save");
					redirectSame();
					return;
				}

			} catch (Exception e) {
				setExceptionMessage(e.getMessage());
				redirectSame();
				return;
			}

		}
		
	
	public void goToAdditionalDetails() {
		try {
		
			
			if(!isBooRenderAccountDialogBox()){
				
				String errMsg = checkBeneficiaryDetailsWithProc(getPersonalRemmitanceBeneficaryDataTables());

				if(errMsg != null && !errMsg.equalsIgnoreCase("")){
					setExceptionMessage(errMsg);
					fetchAccountDetailsBene();
					//RequestContext.getCurrentInstance().execute("beneAccountPanel.show();");
					//setBooRenderAccountDialogBox(true);
					return;
				}else if (getAmountToRemit() != null) {
		  
				 	getExchangeRatevalues1();
					fetchSourceOfIncomeList();
					dynamicLevel(); 
					matchData();
					
					//BigDecimal sourceId = iPersonalRemittanceService.getSourceOfIncomeIdBasedOnName("SALARY" , sessionStateManage.getLanguageId());
					BigDecimal sourceId = iPersonalRemittanceService.getSourceOfIncomeIdBasedOnName("SALARY" , new BigDecimal("1"));
					if(sourceId !=null){
						setSourceOfIncome(sourceId);
					}
					
					fetchPurposeOfTransactionsList();
					setBooRenderTransferFundPanel(false);
					setBooRenderAdditionalDataPanel(true);
					try {
						FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_addDetails.xhtml");
					} catch (IOException e) {
					}
				// checking customer telephone number modified or not
				if (getDataCustomerTelephoneNumber() != null) {
					if (getDataTempCustomerMobile() != null && getDataTempCustomerMobile().equalsIgnoreCase(getDataCustomerTelephoneNumber())) {
						log.info("equal");
					} else {
						log.info("difference");
					}
				} else {
					setExceptionMessage(WarningHandler.showWarningMessage("lbl.custMobNumisMand", sessionStateManage.getLanguageId()));
					
					/* pushing Exception Main menthod */
					throw new AMGException(getExceptionMessage());
					
					
				}
				if (getBenificaryTelephone() != null) {
					if (getDataTempBeneTelNum() != null && getDataTempBeneTelNum().compareTo(new BigDecimal(getBenificaryTelephone())) == 0) {
						log.info("equal");
					} else {
						log.info("difference");
						// saving beneficiary telephone number
						if (getDataBeneContactId() != null) {
						}
					}
				} else {
					setExceptionMessage(WarningHandler.showWarningMessage("lbl.beneTelNumisMand", sessionStateManage.getLanguageId()));
					setBooRenderTransferFundPanel(true);
					setBooRenderAdditionalDataPanel(false);
					
					/* pushing Exception Main menthod */
					throw new AMGException(getExceptionMessage());
					
				}
			} else {
				setExceptionMessage(WarningHandler.showWarningMessage("lbl.enterremittanceamount", sessionStateManage.getLanguageId()));
				setBooRenderTransferFundPanel(true);
				setBooRenderAdditionalDataPanel(false);
				
				/* pushing Exception Main menthod */
				throw new AMGException(getExceptionMessage());
			}
			}else{
				RequestContext.getCurrentInstance().execute("beneAccountPanel.show();");
			}	
			
			
		} catch (AMGException e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.PRODUCT_SETUP);
			String currencyName = null;
			String currency = "";
			if(getCurrency()!=null){
				currencyName = generalService.getCurrencyName(getCurrency());
				currency = "( "+currencyName+" )";
			}
			//getMailService().sendEmailTransactionCreationFail(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
		}
	}

	public void backToTransferTran() {
		try {
			setVisible(false); 
			setSourceOfIncome(null);
			setAmountToRemit(null);
			setEquivalentCurrency(null);
			setEquivalentRemitAmount(null);
			lstPurposeOfTransaction.clear();
			lstSourceOfIncome.clear();
			try {
				if(getRemitAction()!=null && getRemitAction().equalsIgnoreCase("Q")){
					 FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/quickRemit.xhtml");
				}else{
				  FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_home.xhtml");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	// calling exchange rate procedure , aditional bank details , checking spot
	// rate , special rate multiple calls avaibale
	public void nextServiceSecondPanel() {
		try {
			if (getAmountToRemit() != null) {
				getExchangeRatevalues1();
				 
				fetchSourceOfIncomeList();
				fetchPurposeOfTransactionsList();
				setBooRenderTransferFundPanel(false);
				setBooRenderAdditionalDataPanel(true);
				setBooRenderDebitCardPanel(false);
				// checking customer telephone number modified or not
				if (getDataCustomerTelephoneNumber() != null) {
					if (getDataTempCustomerMobile() != null && getDataTempCustomerMobile().equalsIgnoreCase(getDataCustomerTelephoneNumber())) {
						log.info("equal");
					} else {
						log.info("difference");
					}
				} else {
					setExceptionMessage(WarningHandler.showWarningMessage("lbl.custMobNumisMand", sessionStateManage.getLanguageId()));
					RequestContext.getCurrentInstance().execute("alertmsg.show();");
					return;
				}
				if (getBenificaryTelephone() != null) {
					if (getDataTempBeneTelNum() != null && getDataTempBeneTelNum().compareTo(new BigDecimal(getBenificaryTelephone())) == 0) {
						log.info("equal");
					} else {
						log.info("difference");
						// saving beneficiary telephone number
						if (getDataBeneContactId() != null) {
						}
					}
				} else {
					setExceptionMessage(WarningHandler.showWarningMessage("lbl.beneTelNumisMand", sessionStateManage.getLanguageId()));
					RequestContext.getCurrentInstance().execute("alertmsg.show();");
					setBooRenderTransferFundPanel(true);
					setBooRenderAdditionalDataPanel(false);
					return;
				}
				
				  setAdditionalCheck(true); 
				  dynamicLevel(); 
				  matchData();
		
			} else {
				setExceptionMessage(WarningHandler.showWarningMessage("lbl.enterremittanceamount", sessionStateManage.getLanguageId()));
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
				setBooRenderTransferFundPanel(true);
				setBooRenderAdditionalDataPanel(false);
				return;
			}
		} catch (Exception e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			return;
		}
	}
	
	public void remitEquivalentCounterAmountForDisplay() throws AMGException{
		if(!isBooRenderAccountDialogBox()){
		setEquivalentRemitAmount(null);
		setEquivalentCurrency(null);
		if(getAmountToRemit() != null){
			HashMap<String, String> hmoutPutValues = getRemitEquivalentAmtForSpecialRate();
			if(hmoutPutValues!=null){
				setEquivalentRemitAmount(new BigDecimal(hmoutPutValues.get("P_EQUIV_GROSS_AMOUNT")));
				if(hmoutPutValues.get("P_EQUIV_CURRENCY_ID")!=null && !hmoutPutValues.get("P_EQUIV_CURRENCY_ID").equals("0")){
					setEquivalentCurrency("["+generalService.getCurrencyQuote(new BigDecimal(hmoutPutValues.get("P_EQUIV_CURRENCY_ID")))+"]"); 
				}
			}
		}else{
			setEquivalentRemitAmount(null);
		}
	}else{
		setAmountToRemit(null);
	    RequestContext.getCurrentInstance().execute("beneAccountPanel.show();");
	}
}
	
	
	
	public void selectCurrency(){
		setEquivalentRemitAmount(null);
		setEquivalentCurrency(null);
		setEquivalentRemitAmount(null);
		setAmountToRemit(null);
		
	}
	
	
	/*private HashMap<String, String> getRemitEquivalentAmtForSpecialRate() throws AMGException
	{
		HashMap<String, String> inputValues = new HashMap<String, String>();

		inputValues.put("P_APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId() == null ? "0" : sessionStateManage.getCountryId().toString());
		inputValues.put("P_ROUTING_COUNTRY_ID", getRoutingCountry() == null ? "0" : getRoutingCountry().toString());
		inputValues.put("P_BRANCH_ID", sessionStateManage.getBranchId());
		inputValues.put("P_COMPANY_ID", sessionStateManage.getCompanyId() == null ? "0" : sessionStateManage.getCompanyId().toString());
		inputValues.put("P_ROUTING_BANK_ID", getRoutingBank() == null ? "0" : getRoutingBank().toString());
		inputValues.put("P_SERVICE_MASTER_ID", getDataserviceid() == null ? "0" : getDataserviceid().toString());
		inputValues.put("P_DELIVERY_MODE_ID", getDeliveryMode()==null ? "0" : getDeliveryMode().toString());
		inputValues.put("P_REMITTANCE_MODE_ID", getRemitMode()==null ? "0" : getRemitMode().toString());
		inputValues.put("P_FOREIGN_CURRENCY_ID", getForiegnCurrency() == null ? "0" : getForiegnCurrency().toString());
		inputValues.put("P_SELECTED_CURRENCY_ID", getCurrency() == null ? "0" : getCurrency().toString());
		inputValues.put("P_CUSTOMER_TYPE", getCustomerType());
		inputValues.put("P_SELECTED_CURRENCY_AMOUNT", getAmountToRemit() == null ? "0": getAmountToRemit().toString());
		inputValues.put("P_USER_TYPE", sessionStateManage.getUserType() == null ? "O": sessionStateManage.getUserType());

		HashMap<String, String> outputValues = iPersonalRemittanceService.getRemitExchangeEquivalentAount(inputValues);

		return outputValues;
	}*/
	
	private HashMap<String, String> getRemitEquivalentAmtForSpecialRate() throws AMGException
	{
		HashMap<String, String> inputValues = new HashMap<String, String>();

		inputValues.put("P_APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId() == null ? "0" : sessionStateManage.getCountryId().toString());
		inputValues.put("P_ROUTING_COUNTRY_ID", getRoutingCountry() == null ? "0" : getRoutingCountry().toString());
		inputValues.put("P_BRANCH_ID", sessionStateManage.getBranchId());
		inputValues.put("P_COMPANY_ID", sessionStateManage.getCompanyId() == null ? "0" : sessionStateManage.getCompanyId().toString());
		inputValues.put("P_ROUTING_BANK_ID", getRoutingBank() == null ? "0" : getRoutingBank().toString());
		inputValues.put("P_SERVICE_MASTER_ID", getDataserviceid() == null ? "0" : getDataserviceid().toString());
		inputValues.put("P_DELIVERY_MODE_ID", getDeliveryMode()==null ? "0" : getDeliveryMode().toString());
		inputValues.put("P_REMITTANCE_MODE_ID", getRemitMode()==null ? "0" : getRemitMode().toString());
		inputValues.put("P_FOREIGN_CURRENCY_ID", getForiegnCurrency() == null ? "0" : getForiegnCurrency().toString());
		inputValues.put("P_SELECTED_CURRENCY_ID", getCurrency() == null ? "0" : getCurrency().toString());
		inputValues.put("P_CUSTOMER_TYPE", getCustomerType());
		inputValues.put("P_SELECTED_CURRENCY_AMOUNT", getAmountToRemit() == null ? "0": getAmountToRemit().toString());
		//Added by Rabil  on 15 July 2017
		inputValues.put("P_APPLICATION_TYPE",Constants.O); //C-for Counter
		inputValues.put("P_EXCHANGE_RATE_IN",getUserDefExchangeRate()==null?"0":getUserDefExchangeRate()); //C-for Counter
		HashMap<String, String> outputValues = iPersonalRemittanceService.getRemitExchangeEquivalentAount(inputValues);
		return outputValues;
		
	}
	

	public void getExchangeRatevalues1() throws AMGException {
		//boolean rtnValue = false;
		try {
			HashMap<String, String> inputValues = new HashMap<String, String>();
			inputValues.put("P_TYPE", "O");
			inputValues.put("P_APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId() == null ? "0" : sessionStateManage.getCountryId().toString());
			inputValues.put("P_ROUTING_COUNTRY_ID", getRoutingCountry() == null ? "0" : getRoutingCountry().toString());
			inputValues.put("P_BRANCH_ID", sessionStateManage.getBranchId());
			inputValues.put("P_COMPANY_ID", sessionStateManage.getCompanyId() == null ? "0" : sessionStateManage.getCompanyId().toString());
			inputValues.put("P_ROUTING_BANK_ID", getRoutingBank() == null ? "0" : getRoutingBank().toString());
			inputValues.put("P_SERVICE_MASTER_ID", getDataserviceid() == null ? "0" : getDataserviceid().toString());
			inputValues.put("P_DELIVERY_MODE_ID", getDeliveryMode() == null ? "0" : getDeliveryMode().toString());
			inputValues.put("P_REMITTANCE_MODE_ID", getRemitMode() == null ? "0" : getRemitMode().toString());
			inputValues.put("P_FOREIGN_CURRENCY_ID", getForiegnCurrency() == null ? "0" : getForiegnCurrency().toString());
			inputValues.put("P_SELECTED_CURRENCY_ID", getCurrency() == null ? "0" : getCurrency().toString());
			inputValues.put("P_CUSTOMER_ID", getCustomerNo() == null ? "0" : getCustomerNo().toString());
			inputValues.put("P_CUSTOMER_TYPE", getCustomerType());
			inputValues.put("P_LOYALTY_POINTS_IND", getAvailLoyaltyPoints());
			// special Deal Ind Changed to special Deal Rate
			inputValues.put("P_SPECIAL_DEAL_RATE", getSpecialDealRate() == null ? "0" : getSpecialDealRate().toString());
			inputValues.put("P_OVERSEAS_CHRG_IND", getChargesOverseas());
			inputValues.put("P_SELECTED_CURRENCY_AMOUNT", getAmountToRemit() == null ? "0" : getAmountToRemit().toString());
			// spot Rate Ind Changed to spot Exchange Rate
			inputValues.put("P_SPOT_RATE", getSpotExchangeRate() == null ? "0" : getSpotExchangeRate().toString());
			inputValues.put("P_CASH_ROUND_IND", getCashRounding());
			inputValues.put("P_ROUTING_BANK_BRANCH_ID", getRoutingBranch() == null ? "0" : getRoutingBranch().toString());
			inputValues.put("P_BENE_ID", getMasterId() == null ? "0" : getMasterId().toString());
			inputValues.put("P_BENE_COUNTRY_ID", getDataBankbenificarycountry() == null ? "0" : getDataBankbenificarycountry().toString());
			inputValues.put("P_BENE_BANK_ID", getBeneficaryBankId() == null ? "0" : getBeneficaryBankId().toString());
			inputValues.put("P_BENE_BANK_BRANCH_ID", getBeneficaryBankBranchId() == null ? "0" : getBeneficaryBankBranchId().toString());
			inputValues.put("P_BENE_ACCOUNT_NO", getDataAccountnum());
			// Added by Rabil on 09/03/2016
			inputValues.put("P_APPROVAL_YEAR", getApprovalYear() == null ? "0" : getApprovalYear().toString());
			inputValues.put("P_APPROVAL_NO", getApprovalNo() == null ? "0" : getApprovalNo().toString());
			inputValues.put("P_AMT_UP_DOWN_TYPE", null);
			//Added by Rabil on 24/12/2016
			inputValues.put("P_BENE_NAME",getDatabenificaryname());
			//Added by Rabil on 09/11/2016
			inputValues.put("P_APPLICATION_TYPE",Constants.O); //C-for Counter O-For Online
			//Added by Rabil on 09/03/2016
			inputValues.put("P_EXCHANGE_RATE_IN",getUserDefExchangeRate()==null?"0":getUserDefExchangeRate());
			
			
			HashMap<String, String> outputValues = iPersonalRemittanceService.getExchangeRateValues(inputValues);
			if (outputValues.size() > 0) {
			
				if (outputValues.get("P_ERROR_MESSAGE") != null && !outputValues.get("P_ERROR_MESSAGE").equalsIgnoreCase("")) {
					setProcedureError(outputValues.get("P_ERROR_MESSAGE"));
					/* pushing Exception Main menthod */
					throw new AMGException(getProcedureError());
					
				} else {
					
					int localCurrencyRound = foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()));
					int foreignCurrencyRound = foreignLocalCurrencyDenominationService.getDecimalPerCurrency(getForiegnCurrency());
					 
					String currecnyName  = generalService.getCurrencyQuote(new BigDecimal(sessionStateManage.getCurrencyId()));
					setLocalCurrencyName(currecnyName) ;
					String foreignCurencyName  = generalService.getCurrencyQuote(getForiegnCurrency());  
					setForgeignCurrencyName(foreignCurencyName); 
					
					setExchangeRate(new BigDecimal(outputValues.get("P_EXCHANGE_RATE_APPLIED")));
					setOverseasamt(GetRound.roundBigDecimal(new BigDecimal(outputValues.get("P_LOCAL_CHARGE_AMOUNT")),localCurrencyRound));
					setCommission(GetRound.roundBigDecimal(new BigDecimal(outputValues.get("P_LOCAL_COMMISION_AMOUNT")), localCurrencyRound));
					setGrossAmountCalculated(GetRound.roundBigDecimal(new BigDecimal(outputValues.get("P_LOCAL_GROSS_AMOUNT")),localCurrencyRound));
					setLoyaltyAmountAvailed(GetRound.roundBigDecimal(new BigDecimal(outputValues.get("P_LOYALTY_AMOUNT")),localCurrencyRound));
					setNetAmountPayable(GetRound.roundBigDecimal(new BigDecimal(outputValues.get("P_LOCAL_NET_PAYABLE")),localCurrencyRound));
				 	setNetAmountSent(GetRound.roundBigDecimal(new BigDecimal(outputValues.get("P_LOCAL_NET_SENT")), foreignCurrencyRound));
					if(getAvailLoyaltyPoints()!=null && !getAvailLoyaltyPoints().isEmpty() && getAvailLoyaltyPoints().equals(Constants.Yes)){
						if(getLoyaltyAmountAvailed()!=null && getLoyaltyAmountAvailed().compareTo(BigDecimal.ZERO)>0){
							BigDecimal netAmountTrns = getNetAmountPayable().subtract(getLoyaltyAmountAvailed());
							setNetAmountForTransaction(GetRound.roundBigDecimal(netAmountTrns, localCurrencyRound));
						}
					}else{
						setNetAmountForTransaction(GetRound.roundBigDecimal(new BigDecimal(outputValues.get("P_LOCAL_NET_PAYABLE")),localCurrencyRound));
					}
					log.info("GrossAmountCalculated :"+grossAmountCalculated);
					log.info("NetAmountPayable :"+netAmountPayable);
					log.info("GrossAmountCalculated :"+netAmountSent);
					log.info("getForiegnCurrency() :"+getForiegnCurrency());
					log.info("Local Currency :"+sessionStateManage.getCurrencyId());
					
					
					// Remittance Id and Delivery Id Changes
					if (getRemitMode().compareTo(new BigDecimal(outputValues.get("P_NEW_REMITTANCE_MODE_ID"))) != 0) {
						setNewRemittanceModeId(getRemitMode());
						setNewRemittanceModeName(getRemittanceName());
						setRemitMode(new BigDecimal(outputValues.get("P_NEW_REMITTANCE_MODE_ID")));
						if (new BigDecimal(outputValues.get("P_NEW_REMITTANCE_MODE_ID")).compareTo(BigDecimal.ZERO) != 0) {
							String remitName = getGeneralService().getRemittanceDesc(getRemitMode(),
									new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
							if (remitName != null) {
								setBooRenderRemit(true);
								setRemittanceName(remitName);
							} else {
								setRemittanceName(null);
								// pushing Exception Main to menthod 
								throw new AMGException(props.getString("lbl.remittanceNameNotAvailble"));
							}
						}
					} else {
						setBooRenderRemit(false);
					}
					if (getDeliveryMode().compareTo(new BigDecimal(outputValues.get("P_NEW_DELIVERY_MODE_ID"))) != 0) {
						setNewDeliveryModeId(getDeliveryMode());
						setNewDeliveryModeName(getDeliveryModeInput());
						setDeliveryMode(new BigDecimal(outputValues.get("P_NEW_DELIVERY_MODE_ID")));
						if (new BigDecimal(outputValues.get("P_NEW_DELIVERY_MODE_ID")).compareTo(BigDecimal.ZERO) != 0) {
							String deliveryName = getGeneralService().getDeliveryDesc(getDeliveryMode(),
									new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
							if (deliveryName != null) {
								setBooRenderDelivery(true);
								setDeliveryModeInput(deliveryName);
							} else {
								setDeliveryModeInput(null);
					 
								// pushing Exception Main menthod 
								throw new AMGException(props.getString("lbl.deliveryNameNotAvailable"));
							}
						}
					} else {
						setBooRenderDelivery(false);
					}
					String additionlMsg = getAdditionalCheckProcedure();
					setBeneStateId(null);
					setBeneDistrictId(null);
					setBeneCityId(null);
					setPbeneFullName(null);
					setPbeneFirstName(null);
					setPbeneSecondName(null);
					setPbeneThirdName(null);
					setPbeneFourthName(null);
					setPbeneFifthName(null);
					if (additionlMsg != null) {
						setExceptionMessage(additionlMsg);
						throw new AMGException(getExceptionMessage());
					} else {
						HashMap<String, String> lstofAdditionalBeneDetails = getAdditionalBeneDetails();
						if (lstofAdditionalBeneDetails != null && lstofAdditionalBeneDetails.size() != 0) {
							if (lstofAdditionalBeneDetails.get("P_ERROR_MESSAGE") != null) {
								setExceptionMessage("EX_GET_ADDL_BENE_DETAILS" + " : " + lstofAdditionalBeneDetails.get("P_ERROR_MESSAGE"));
								throw new AMGException(getExceptionMessage());
							} else {
								log.info(lstofAdditionalBeneDetails.toString());
								if (lstofAdditionalBeneDetails.get("P_BENE_BANK_NAME") != null) {
									setDatabenificarybankname(lstofAdditionalBeneDetails.get("P_BENE_BANK_NAME"));
								} else {
									setDatabenificarybankname(null);
								}
								if (lstofAdditionalBeneDetails.get("P_BENE_BRANCH_NAME") != null) {
									setDatabenificarybranchname(lstofAdditionalBeneDetails.get("P_BENE_BRANCH_NAME"));
								} else {
									setDatabenificarybranchname(null);
								}
								if (!lstofAdditionalBeneDetails.get("P_BENE_STATE_ID").equalsIgnoreCase("0")) {
									setBeneStateId(new BigDecimal(lstofAdditionalBeneDetails.get("P_BENE_STATE_ID")));
								} else {
									setBeneStateId(null);
								}
								if (!lstofAdditionalBeneDetails.get("P_BENE_DISTRICT_ID").equalsIgnoreCase("0")) {
									setBeneDistrictId(new BigDecimal(lstofAdditionalBeneDetails.get("P_BENE_DISTRICT_ID")));
								} else {
									setBeneDistrictId(null);
								}
								if (!lstofAdditionalBeneDetails.get("P_BENE_CITY_ID").equalsIgnoreCase("0")) {
									setBeneCityId(new BigDecimal(lstofAdditionalBeneDetails.get("P_BENE_CITY_ID")));
								} else {
									setBeneCityId(null);
								}
							
								if (lstofAdditionalBeneDetails.get("P_BENE_NAME") != null && !lstofAdditionalBeneDetails.get("P_BENE_NAME").equals("") ) {
									setPbeneFullName(lstofAdditionalBeneDetails.get("P_BENE_NAME"));
								} else {
									try {
										pageNavigationHomePage("Q");
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									//setPbeneFullName(null);
								}
								if (lstofAdditionalBeneDetails.get("P_BENEFICIARY_FIRST_NAME") != null) {
									setPbeneFirstName(lstofAdditionalBeneDetails.get("P_BENEFICIARY_FIRST_NAME"));
								} else {
									setPbeneFirstName(null);
								}
								if (lstofAdditionalBeneDetails.get("P_BENEFICIARY_SECOND_NAME") != null) {
									setPbeneSecondName(lstofAdditionalBeneDetails.get("P_BENEFICIARY_SECOND_NAME"));
								} else {
									setPbeneSecondName(null);
								}
								if (lstofAdditionalBeneDetails.get("P_BENEFICIARY_THIRD_NAME") != null) {
									setPbeneThirdName(lstofAdditionalBeneDetails.get("P_BENEFICIARY_THIRD_NAME"));
								} else {
									setPbeneThirdName(null);
								}
								if (lstofAdditionalBeneDetails.get("P_BENEFICIARY_FOURTH_NAME") != null) {
									setPbeneFourthName(lstofAdditionalBeneDetails.get("P_BENEFICIARY_FOURTH_NAME"));
								} else {
									setPbeneFourthName(null);
								}
								if (lstofAdditionalBeneDetails.get("P_BENEFICIARY_FIFTH_NAME") != null) {
									setPbeneFifthName(lstofAdditionalBeneDetails.get("P_BENEFICIARY_FIFTH_NAME"));
								} else {
									setPbeneFifthName(null);
								}
								/* to render Instrn , SwiftBank1 , SwiftBank2  conditions */
								checkingInstrnSiftBanksRequired();
								if (isBooRenderSwiftBank1() || isBooRenderSwiftBank2()) {
									// to fetch swift master list
									fetchingAllSwiftMaster();
								}
								// to fetch source of income list
								getSourceofIncomeDetails();
								
							}
						}
					}
				}
			} else {
			}
		} catch (AMGException e) {
			throw new AMGException(e.getMessage());
		}
	}

	
	public void saveApplicationTransaction(String act) {
		
		try{
			/*
			 * S-Single transaction.
	 		 * A-Add More Transaction
			 */
			tempCalGrossAmount = new BigDecimal(0) ;
			tempCalNetAmountPaid = new BigDecimal(0) ;
			log.info("Action :"+act);
	 		log.info("Action : "+act);
			setActionShopping(act);
			saveRemittanceApplication(saveCount,act);
		}catch(AMGException ex){
			System.out.println("saveApplicationTransaction :"+ex.getMessage());
			setProcedureError(ex!=null?ex.getMessage():"");
			//If Error in transaction Send Email
			List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.PRODUCT_SETUP);
			String currencyName = null;
			String currency = "";
			if(getCurrency()!=null){
				currencyName = generalService.getCurrencyName(getCurrency());
				currency = " ( "+currencyName+" )";
			}
			//getMailService().sendEmailTransactionCreationFail(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getProcedureError());
			//For Multi Country
			getMailService().getEmailTransactionCreationFailContent(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
		}

	}
	
	public void saveRemittanceApplication(int saveCount,String act) throws AMGException{
		try{
		log.info("Action : "+act);
			if (saveCount == 0) {
				// start 28/01/2015
				setDocumentNo(getDocumentSerialID(Constants.Yes));
				// End 28/01/2015verifyPassword
				RemittanceApplication remittanceApplication = new RemittanceApplication();
				// Foriegn Currency id
				CurrencyMaster forcurrencymaster = new CurrencyMaster();
				forcurrencymaster.setCurrencyId(getForiegnCurrency());
				remittanceApplication.setExCurrencyMasterByForeignCurrencyId(forcurrencymaster);
				// Local Commission Currency Id - KWD
				CurrencyMaster commisioncurrencymaster = new CurrencyMaster();
				commisioncurrencymaster.setCurrencyId(new BigDecimal(sessionStateManage.getCurrencyId()));
				if (getCommission() != null && getCommission().compareTo(BigDecimal.ZERO) != 0) {
					remittanceApplication.setExCurrencyMasterByLocalCommisionCurrencyId(commisioncurrencymaster); // mru
				} else {
					commisioncurrencymaster.setCurrencyId(new BigDecimal(sessionStateManage.getCurrencyId()));
					remittanceApplication.setExCurrencyMasterByLocalCommisionCurrencyId(commisioncurrencymaster);
				}
				// local Transaction Currency Id
				CurrencyMaster tranxcurrencymaster = new CurrencyMaster();
				tranxcurrencymaster.setCurrencyId(new BigDecimal(sessionStateManage.getCurrencyId()));
				remittanceApplication.setExCurrencyMasterByLocalTranxCurrencyId(tranxcurrencymaster);
				// local Charge Currency Id -KWD
				CurrencyMaster chargecurrencymaster = new CurrencyMaster();
				chargecurrencymaster.setCurrencyId(new BigDecimal(sessionStateManage.getCurrencyId()));
				remittanceApplication.setExCurrencyMasterByLocalChargeCurrencyId(chargecurrencymaster);
				// local Net Currency Id - kWD
				CurrencyMaster netcurrencymaster = new CurrencyMaster();
				netcurrencymaster.setCurrencyId(new BigDecimal(sessionStateManage.getCurrencyId()));
				remittanceApplication.setExCurrencyMasterByLocalNetCurrencyId(netcurrencymaster);
				// local Delivery Currency Id - kWD
				CurrencyMaster devcurrencymaster = new CurrencyMaster();
				devcurrencymaster.setCurrencyId(new BigDecimal(sessionStateManage.getCurrencyId()));
				// Modifed by Rabil
				remittanceApplication.setExCurrencyMasterByLocalDeliveryCurrencyId(null);
				// Added by Rabil
				remittanceApplication.setSpotRateInd(getSpotRate());
				remittanceApplication.setLoyaltyPointInd(getAvailLoyaltyPoints());
				
				//List<Document> lstDocument = generalService.getDocument(new BigDecimal(Constants.DOCUMENT_CODE_FOR_FC_SALE_APP), new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
				
				List<Document> lstDocument = generalService.getDocument(new BigDecimal(Constants.DOCUMENT_CODE_FOR_FC_SALE_APP), new BigDecimal("1"));
				if (lstDocument.size() != 0) {
					Document documentid = new Document();
					documentid.setDocumentID(lstDocument.get(0).getDocumentID());
					remittanceApplication.setExDocument(documentid);
					
					remittanceApplication.setDocumentCode(lstDocument.get(0).getDocumentCode());
				}
				
				// company code
				BigDecimal companyCode = null;
				List<CompanyMasterDesc> lstcompanymaster = generalService.viewById(sessionStateManage.getCompanyId());
				if(lstcompanymaster.size() != 0){
					CompanyMasterDesc companycode = lstcompanymaster.get(0);
					if(companycode.getFsCompanyMaster().getCompanyCode() != null){
						companyCode = companycode.getFsCompanyMaster().getCompanyCode();
						remittanceApplication.setCompanyCode(companyCode);
					}
				}

				// company Id
				CompanyMaster companymaster = new CompanyMaster();
				companymaster.setCompanyId(sessionStateManage.getCompanyId());
				remittanceApplication.setFsCompanyMaster(companymaster);
				// User Financial Year for Document
				UserFinancialYear docuserfinancialyear = new UserFinancialYear();
				docuserfinancialyear.setFinancialYearID(generalService.getDealYear(new Date()).get(0).getFinancialYearID());
				remittanceApplication.setExUserFinancialYearByDocumentFinanceYear(docuserfinancialyear);
				
				CountryBranch countryBranch = new CountryBranch();
				countryBranch.setCountryBranchId(new BigDecimal(sessionStateManage.getBranchId()));
				remittanceApplication.setExCountryBranch(countryBranch);
				// Application Country
				CountryMaster appcountrymaster = new CountryMaster();
				appcountrymaster.setCountryId(sessionStateManage.getCountryId());
				remittanceApplication.setFsCountryMasterByApplicationCountryId(appcountrymaster);
				// routing Country
				CountryMaster bencountrymaster = new CountryMaster();
				bencountrymaster.setCountryId(getRoutingCountry());
				remittanceApplication.setFsCountryMasterByBankCountryId(bencountrymaster);
				
				// Delivery Mode from service
				if (getDeliveryMode() != null) {
					DeliveryMode deliverymode = new DeliveryMode();
					deliverymode.setDeliveryModeId(getDeliveryMode());
					remittanceApplication.setExDeliveryMode(deliverymode);
				}

				// RemittanceModeMaster to get Remittance
				if (getRemitMode() != null) {
					RemittanceModeMaster remittancemode = new RemittanceModeMaster();
					remittancemode.setRemittanceModeId(getRemitMode());
					remittanceApplication.setExRemittanceMode(remittancemode);
				}

				// Customer id
				Customer customerid = new Customer();
				customerid.setCustomerId(getCustomerNo());
				remittanceApplication.setFsCustomer(customerid);
				// Routing Bank
				BankMaster bankmaster = new BankMaster();
				bankmaster.setBankId(getRoutingBank());
				remittanceApplication.setExBankMaster(bankmaster);
				// Routing Bank Branch
				BankBranch bankbranch = new BankBranch();
				bankbranch.setBankBranchId(getRoutingBranch());
				remittanceApplication.setExBankBranch(bankbranch);
				remittanceApplication.setDocumentDate(new Date());
				remittanceApplication.setCustomerRef(getCustomerrefno());
				if (getDataAccountnum() != null) {
					remittanceApplication.setDebitAccountNo(getDataAccountnum());
				}
				remittanceApplication.setForeignTranxAmount(getNetAmountSent());
				remittanceApplication.setLocalTranxAmount(getGrossAmountCalculated());
				remittanceApplication.setExchangeRateApplied(getExchangeRate());
				remittanceApplication.setLocalCommisionAmount(getCommission());
				remittanceApplication.setLocalChargeAmount(getOverseasamt());
				remittanceApplication.setLocalDeliveryAmount(new BigDecimal(0));
				remittanceApplication.setLocalNetTranxAmount(getNetAmountPayable());
				remittanceApplication.setLoyaltyPointsEncashed(getLoyaltyAmountAvailed());
				remittanceApplication.setDocumentFinancialyear(generalService.getDealYear(new Date()).get(0).getFinancialYear());
				remittanceApplication.setTransactionFinancialyear(generalService.getDealYear(new Date()).get(0).getFinancialYear());
				remittanceApplication.setSelectedCurrencyId(getCurrency());
				 
				try {
					remittanceApplication.setAccountMmyyyy(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				remittanceApplication.setWesternUnionMtcno(null);
				//remittanceApplication.setCreatedBy(Constants.CREATOR);
				
				if(sessionStateManage.getCreatorFromLogin()!=null && !sessionStateManage.getCreatorFromLogin().equals("")){
					remittanceApplication.setCreatedBy(sessionStateManage.getCreatorFromLogin());
				}else{
					remittanceApplication.setCreatedBy(Constants.CREATOR);
				}
			
				remittanceApplication.setCreatedDate(new Date());
				remittanceApplication.setIsactive(Constants.Yes);
				remittanceApplication.setSourceofincome(getSourceOfIncome());
				 
				remittanceApplication.setInstruction(getFurthuerInstructions());
				remittanceApplication.setApprovalYear(getApprovalYear());
				remittanceApplication.setApprovalNumber(getApprovalNo());
				// Online user
				remittanceApplication.setApplInd("O");

				HashMap<String, Object> mapAllDetailPersonalRemittanceApplSave = new HashMap<String, Object>();

				/* Save to EX_APPL_TRNX */
				mapAllDetailPersonalRemittanceApplSave.put("EX_APPL_TRNX", remittanceApplication);
				/* Save to EX_APPL_BENE */
				RemittanceAppBenificiary remitAppBene = saveRemittanceAppBenificary(remittanceApplication);
				
				if(remitAppBene.getBeneficiaryName()==null || remitAppBene.getBeneficiaryName().equals("")){
					pageNavigationHomePage("Q");
				}

				mapAllDetailPersonalRemittanceApplSave.put("EX_APPL_BENE", remitAppBene);


				List<AdditionalInstructionData> lstAddInstData = saveAdditionalInstnData(remittanceApplication);

				mapAllDetailPersonalRemittanceApplSave.put("EX_APPL_ADDL_DATA", lstAddInstData);


				mapAllDetailPersonalRemittanceApplSave.put("FinancialYear", getFinaceYear());

				 
				 documentNumber = iPersonalRemittanceService.saveAllApplTransaction(mapAllDetailPersonalRemittanceApplSave);
				checkProExp = true;
				saveCount = saveCount + 1;
				
			}

		 

			if (checkProExp) {
				setVisible(true);
				if(act.equalsIgnoreCase("S")){
				shoppingCartAndDebitCardPage();
				}else if(act.equalsIgnoreCase("A")){ // Add more transaction  for shopping cart 
					setVisible(false);
					assignNullValues();
					clear();
					setBeneCountr(null);
					setAmountToRemit(null);
					setEquivalentCurrency(null);
					setEquivalentRemitAmount(null);
					popCountry();
					if(getRemitAction()!=null && getRemitAction().equalsIgnoreCase("T")){
					FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_home.xhtml");
					}else if(getRemitAction()!=null && getRemitAction().equalsIgnoreCase("Q")){
						FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/quickRemit.xhtml");
					}else{
						FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_home.xhtml");
					}
				}
			}
		
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}

	}

	
	
	/**
	 * Document Seriality
	 */
	public String getDocumentSerialID(String processIn) {
		log.info("Start getDocumentSerialID ...." + getFinaceYear());

		String documentSerialId = "";

		log.info("process in :" + processIn);
		try {
			HashMap<String, String> outPutValues = generalService.getNextDocumentRefNumber(Integer.parseInt(sessionStateManage.getSessionValue("countryId")), Integer.parseInt(sessionStateManage.getSessionValue("companyId")),
					Integer.parseInt(Constants.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION.trim()), getFinaceYear().intValue(), processIn, sessionStateManage.getCountryBranchCode());

			String proceErrorMsg = outPutValues.get("PROCE_ERORRMSG");
			if (proceErrorMsg != null) {
				setExceptionMessage(proceErrorMsg);
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
				return null;
			} else {
				documentSerialId = outPutValues.get("DOCNO");
			}
		} catch (Exception e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
			return null;
		}
		return documentSerialId;

	}
	
	
	
	// to render to show last panel
		public void shoppingCartAndDebitCardPage() throws Exception {
			try {
				setVisible(false);
				setBooRenderTransferFundPanel(false);
				setBooRenderAdditionalDataPanel(false);
				setBoorenderlastpanel(true);
				getShoppingCartDetails(getCustomerNo());
				setCalGrossAmount(BigDecimal.ZERO);
				setCalNetAmountPaid(BigDecimal.ZERO);
				if(shoppingcartDTList!=null && shoppingcartDTList.size()>1){
					FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_shoppingCart.xhtml");
				}else{
					/** Commented by Rabil on 02 Mar 2017 because as per CBK card validation is not required  
					FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/cardDetails.xhtml");
					**/
					makePayment();
				}
			} catch (IOException e) {
				e.printStackTrace();
				setErrorMessage(e.getMessage());
				
			}

		}
		
		
		
		public void shoppingCardDebitCardPage() throws Exception {
			try {
				BigDecimal customerId = null;
				if (lstselectedrecord != null && lstselectedrecord.size() > 0) {
					/**
					 * Commented by Rabil on 02 Mar 2017 because as per CBK card
					 * validation is not required
					 */
				
					customerId = lstselectedrecord.get(0).getCustomerId();
					// trnx done by customer
					BigDecimal countTrnx = iPersonalRemittanceService.fetchCountofRemiTrnxByCustomer(customerId);
					List<AuthicationLimitCheckView> authLimitView = iPersonalRemittanceService.parameterDeTails_AUTH_View("10");
					if (authLimitView != null && authLimitView.size() > 0) {
						AuthicationLimitCheckView authLimitDt = authLimitView.get(0);
						BigDecimal authLimit = null;
						if (authLimitDt != null) {
							authLimit = authLimitDt.getAuthLimit();
						}
						BigDecimal shopSize = new BigDecimal(lstselectedrecord.size()).add(countTrnx);
						if (authLimit != null && (shopSize.compareTo(authLimit) > 0)) {
							if (countTrnx != null && countTrnx.compareTo(BigDecimal.ZERO) != 0) {
								if (countTrnx.compareTo(authLimit) >= 0) {
									setExceptionMessage(WarningHandler.showWarningMessage("lbl.cashTrnxLimitMsgForDay", sessionStateManage.getLanguageId()) + " : " + authLimit + " already " + countTrnx+ " transaction done");
								} else {
									BigDecimal lessTrnx = authLimit.subtract(countTrnx);
									setExceptionMessage(WarningHandler.showWarningMessage("lbl.cashTrnxLimitMsgForDay", sessionStateManage.getLanguageId()) + " : " + authLimit + " already " + countTrnx
											+ " Transaction done, So please select only " + lessTrnx + " application");
								}
							} else {
								setExceptionMessage(WarningHandler.showWarningMessage("lbl.cashTrnxLimitMsgForDay", sessionStateManage.getLanguageId()) + " : " + authLimit + " , So please select only "
										+ authLimit + " application");
							}
							RequestContext.getCurrentInstance().execute("alertmsg.show();");
							return;
						}
					}
					makePayment();
				} else {
					setExceptionMessage(props.getString("lbl.plsselectatleastonerecord"));
					RequestContext.getCurrentInstance().execute("alertmsg.show();");
				}
			} catch (Exception e) {
				e.printStackTrace();
				setExceptionMessage(WarningHandler.showWarningMessage(e.getMessage(),sessionStateManage.getLanguageId()));
			}
		}

		
		
		public void exitFromDailog(){
			try {
				 setBooRenderAccountDialogBox(true);
				 RequestContext.getCurrentInstance().execute("beneAccountPanel.hide();");
				FacesContext.getCurrentInstance().getExternalContext().redirect("../onlineremittance/transferfund_home.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
	public void makePayment() throws AMGException{
		
	

		String udf1 = null;
		String udf2 = null;
		String udf3 = null;
		String udf4 = null;
		String udf5 = null;
		String udf = null;
		String stackTrace = null;
		String payToken = null;
		String payMode = null;
		BigDecimal receiptNo = null;
		int i = 0;
		String payUrl = "", payId = null;
		HashMap hm = new HashMap();
		BigDecimal totalAmount = new BigDecimal(0);
		sessionStateManage = new SessionStateManage();

		HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance()).getExternalContext().getRequest();
		payToken = request.getSession().getId();
		 log.info("payToken :" + payToken+"\t getDocumentNumber :"+getDocumentNumber());
		sessionStateManage.setSessionValue("payToken", payToken);
		sessionStateManage.setSessionValue("documentNumber", getDocumentNumber().toString());
		
		
		
		/** Added by Rabil  on 29 MAy 2017 For Multi Country */
		
		
	
			List<OnlineConfiguration> onlineConfigList = generalService.getOnlineConfiguration();
			if(onlineConfigList!=null && onlineConfigList.size()==1){
				String action = onlineConfigList.get(0).getAction();
				String currency = onlineConfigList.get(0).getCurrency()==null?"512":onlineConfigList.get(0).getCurrency().toString();
				String responseUrl = onlineConfigList.get(0).getResponseUrl();
				String resourcePath =  onlineConfigList.get(0).getResourcePath();
				String aliasName = onlineConfigList.get(0).getAliasName();
				String languageCode = onlineConfigList.get(0).getLanguageCode();
				String domainUrl    = onlineConfigList.get(0).getOnlineDomUrl();
				System.out.println("Online Transfer Bean domainUrl :"+domainUrl);
				
				fCtx = FacesContext.getCurrentInstance();
				session = (HttpSession) fCtx.getExternalContext().getSession(true);
				
				
				
				session.setAttribute("action", action);
				session.setAttribute("currency", currency);
				session.setAttribute("responseUrl", responseUrl);
				session.setAttribute("resourcePath", resourcePath);
				session.setAttribute("aliasName", aliasName);
				session.setAttribute("languageCode", languageCode);
				session.setAttribute("domainUrl", domainUrl);
				
				
				
		}


		/** Saving into the DB and call the knet URL */
			try{
				log.info("netAmountPayable :"+netAmountPayable +"\t calNetAmountPaid :"+getCalNetAmountPaid());
				
				if(lstselectedrecord!=null && lstselectedrecord.size()>0){ // For Shopping Card
					totalKnetAmount = getCalNetAmountPaid(); 
				}else{
				
					totalKnetAmount = getNetAmountForTransaction();
				}
				
				log.info("totalKnetAmount :"+totalKnetAmount);
			 	if(lstselectedrecord!= null && lstselectedrecord.size()>0){
			 		udf3 = getFinaceYear()+lstselectedrecord.get(0).getDocumentNo().toPlainString();
			 	}else{
				 udf3 = getFinaceYear()+documentNumber.toPlainString();
			 	}
			 	
			    System.out.println("udf3 :"+udf3);
				String trackId = getCustomerrefno().toPlainString();
				if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.KUWAIT)){
				hm = KnetUtils.knetInitialize(trackId, udf1, udf2, udf3, udf4, udf5,totalKnetAmount.toString());
				payUrl = (String) hm.get("PayUrl");
				payId = (String) hm.get("PayId");
				}else if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.OMAN)){
					hm = OmanNetUtils.knetInitialize(trackId, udf1, udf2, udf3, udf4, udf5, totalKnetAmount.toString());
					payUrl = (String) hm.get("PayUrl");
					payId = (String) hm.get("PayId");
					if(payId==null  ||  payId.equals("")){
						payId = udf3;
					}
					
				}
				
			
				
				
				if ((payUrl != null && payUrl.length() > 0) && (payId != null && payId.length() > 0)) {
					
					KnetData onlineData = new KnetData();
					onlineData.setPayId(payId);
					onlineData.setPayToken(payToken);
					onlineData.setCustomerId(getCustomerNo());
					onlineData.setCompanyId(sessionStateManage.getCompanyId());
					onlineData.setDocumentFinanceYear(getFinaceYear());
					onlineData.setApplicationCountryId(getApplicationCountryId());
					if(lstselectedrecord!=null && lstselectedrecord.size()>0){
						for(ShoppingCartDataTableBean shopCardBean : lstselectedrecord){
							onlineData.setDcoumentNumber(shopCardBean.getDocumentNo());
							iPersonalRemittanceService.updateApplicationTable(onlineData);
						}
					}else{
						onlineData.setDcoumentNumber(getDocumentNumber());
						iPersonalRemittanceService.updateApplicationTable(onlineData);
					}
					request.setAttribute("knetRedirect","true");
					if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.KUWAIT)){
                    KnetUtils.knetPay(payUrl, payId);
					}else if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.OMAN)){
	                    OmanNetUtils.knetPay(payUrl, payId);
					}
				}
			}catch(Exception e){
				setErrorMessage(props.getString("knet.errMsg1"));
				
				//If Error in transaction Send Email
				List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.PRODUCT_SETUP);
				String currencyName = null;
				String currency = "";
				if(getCurrency()!=null){
					currencyName = generalService.getCurrencyName(getCurrency());
					currency = " ( "+currencyName+" )";
				}
				getMailService().getEmailTransactionCreationFailContent(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
				
				throw new AMGException(getErrorMessage());
			}
		}
		
		
		
	public void knetSuccess(FacesContext ctx) {
	
		String paymentId = ctx.getExternalContext().getRequestParameterMap().get("PaymentID") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("PaymentID");
		String status = ctx.getExternalContext().getRequestParameterMap().get("result") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("result");
		String trackId = ctx.getExternalContext().getRequestParameterMap().get("trackid") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("trackid");
		String auth_appNo = ctx.getExternalContext().getRequestParameterMap().get("auth") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("auth");
		String referenceId = ctx.getExternalContext().getRequestParameterMap().get("ref") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("ref");
		String tranId = ctx.getExternalContext().getRequestParameterMap().get("tranid") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("tranid");
		String postDate = ctx.getExternalContext().getRequestParameterMap().get("postdate") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("postdate");
		String udf3     =ctx.getExternalContext().getRequestParameterMap().get("udf3") == null ? "" : ctx.getExternalContext().getRequestParameterMap().get("udf3");
		
		List<RemittanceApplication> lstPayIdDetails =null;
		try {

			
			log.info("paymentId.extseq:" + paymentId + "\t trackId :"	+ trackId + "\t auth_appNo :" + auth_appNo + "\t referenceId :" + referenceId + "\t Status :" + status + "\t TranId :" + tranId);

			
			if(paymentId != null && !paymentId.equals("")){				
				if(sessionStateManage.getSessionValue("payToken") != null){
					 if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.OMAN)){
						lstPayIdDetails = iPersonalRemittanceService.fetchRemitApplTrnxRecordsByPayId(udf3); // For Oman they are not returning payment Id
						System.out.println("Bean class spaymnetId :"+paymentId+"\t udf3 :"+udf3);
						iPersonalRemittanceService.updatePaymentId(lstPayIdDetails, paymentId, udf3);
						
					}else{
						 lstPayIdDetails = iPersonalRemittanceService.fetchRemitApplTrnxRecordsByPayId(paymentId); // For KUWAIT & Bahrain
						}
					if(lstPayIdDetails!=null && lstPayIdDetails.size() != 0){
						RemittanceApplication payToken = lstPayIdDetails.get(0);
						if(sessionStateManage.getSessionValue("payToken").equalsIgnoreCase(payToken.getPayToken())){
							if (status!=null && status.equalsIgnoreCase("CAPTURED")) {
								log.info("lstSelected  : "+lstselectedrecord.size());
								log.info("Else part While not captured :"+paymentId+"\t Udf3 :"+udf3+"\t lstPayIdDetails :"+lstPayIdDetails.get(0).getPaymentId());
								//pay token to make null once success happen
								if(lstPayIdDetails != null && lstPayIdDetails.size() != 0){
									String str =saveRemittance(paymentId,auth_appNo,tranId,referenceId);
									
									if(str==null){
									successPageCustomerDetails();
									List<RemittanceApplicationView> remittanceViewlist = iPersonalRemittanceService.getRecordsForRemittanceReceiptReport(collectionDocumentNumber,finaceYear,collectionDocumentCode.toString());
									successPageKnetTransactionDetails(remittanceViewlist);
									successPageKnetPaymentDetails(paymentId , referenceId , tranId , status,totalKnetAmount,auth_appNo,udf3);
									setKnetSuccessPage(props.getString("lbl.knetsuccesspage"));
									loyaltyPoints(userProfile.getCustomerReference());
									iPersonalRemittanceService.updatePayTokenNull(lstPayIdDetails,status);
									/*To generate Jasper Report; */
									fetchRemittanceReceiptReportData(collectionDocumentNumber, collectionFinanceYear, collectionDocumentCode.toPlainString());
									remittanceReceiptReportInit();
									/** To fetch Email Details */
									List<ApplicationSetup> lstApplicationSetup = iPersonalRemittanceService.getEmailFromAppSetup(sessionStateManage.getCompanyId(), sessionStateManage.getCountryId());
									String subject = "ONLINE RECEIPT-"+collectionFinanceYear+"/"+collectionDocumentNumber;
									byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
									System.out.println("userProfile.getEmailID()  :"+userProfile.getEmailID());
									if(userProfile.getEmailID()!=null && !userProfile.getEmailID().equals("") && !userProfile.getEmailID().equals("null")){
										getMailService().sendMailToCustomerWithAttachmentForKnetUpload(lstApplicationSetup,userProfile.getEmailID(),subject,pdfasbytes,getCustomerFullName());
									}
									
									//getMailService().sendReceiptOnMail(userProfile.getEmailID(), "Successfully Initiated", userProfile.getUserName(), getCustomerFullName(), lstknetCustomer,lstKnetTransDetails,lstKnetDetails,getLoyalityPointExpiring(),getInsurence1(),getPromotionId(),str,pdfasbytes);
									}else{ /*Knet doen successfulyy but transaction Failed */
										successPageCustomerDetails();
										List<ShoppingCartDetails> shoppingCartList = new ArrayList<ShoppingCartDetails>();
										shoppingCartList = iPersonalRemittanceService.getApplicationDetails(getCustomerNo(),paymentId,getFinaceYear());
										successPageKnetTransactionDetailsWhileFail(shoppingCartList);
										successPageKnetPaymentDetails(paymentId , referenceId , tranId , status,totalKnetAmount,auth_appNo,udf3);
										setKnetSuccessPage(props.getString("lbl.knetsuccesspage"));
										loyaltyPoints(userProfile.getCustomerReference());
										iPersonalRemittanceService.updatePayTokenNull(lstPayIdDetails,status);
										//Send Email to BAck OFFICE PEOPLE.
										List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.KNET_ERROR);
										//getMailService().sendKnetSuccessButReceiptFailEMail(userProfile.getCustomerReference(),lstKnetDetails,listEmail);
										getMailService().getKnetSuccessButReceiptFailEMailcontentForMultiCountry(userProfile.getCustomerReference(),lstKnetDetails,listEmail);
									}
									
								}
								
							}else{  /*IF Transaction is declined by Knet */
								log.info("Else part While not captured :"+paymentId+"\t Udf3 :"+udf3+"\t lstPayIdDetails :"+lstPayIdDetails.get(0).getPaymentId());
								setKnetTransErrorMessage(props.getString("lbl.knetTransactionerror"));
								setKnetTransErrorContactMessage(props.getString("lbl.inqSupport")+"."+props.getString("lbl.inqSupportTime"));
								successPageKnetPaymentDetails(paymentId , referenceId , tranId , status,totalKnetAmount,auth_appNo,udf3);
								iPersonalRemittanceService.updatePayTokenNull(lstPayIdDetails,status);
								//If Error in transaction Send Email
								List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.PRODUCT_SETUP);
								String currencyName = null;
								String currency = "";
								if(getCurrency()!=null){
									currencyName = generalService.getCurrencyName(getCurrency());
									currency = " ( "+currencyName+" )";
								}
							//	getMailService().sendEmailTransactionCreationFail(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getProcedureError());
								
								getMailService().getEmailTransactionCreationFailContent(listEmail, getCustomerrefno(),getCustomerName(), getCustomerMobile(), getDatabenificaryname(), getAmountToRemit()+currency, getDatabenificarybankname(), getDatabenificarybranchname(), getDataAccountnum(), "Product could not be derived", getExceptionMessage());
								
							}
							
						}else{
							
							try {
								FacesContext.getCurrentInstance().getExternalContext().redirect("../common/sessionTimeOut.xhtml");
							} catch (IOException e) {
								e.printStackTrace();
							}
							
						}

				}
		}
	}			
	} catch (Exception e) {
		e.printStackTrace();
		log.info("Catch knetSuccess :" + e.getMessage());
		if(status!=null && !status.isEmpty() && status.equals("CAPTURED")){
			try{
			successPageCustomerDetails();
			List<ShoppingCartDetails> shoppingCartList = new ArrayList<ShoppingCartDetails>();
			shoppingCartList = iPersonalRemittanceService.getApplicationDetails(getCustomerNo(),paymentId,getFinaceYear());
			successPageKnetTransactionDetailsWhileFail(shoppingCartList);
			successPageKnetPaymentDetails(paymentId , referenceId , tranId , status,totalKnetAmount,auth_appNo,udf3);
			setKnetSuccessPage(props.getString("lbl.knetsuccesspage"));
			loyaltyPoints(userProfile.getCustomerReference());
			iPersonalRemittanceService.updatePayTokenNull(lstPayIdDetails,status);
			//Send Email to BAck OFFICE PEOPLE.
			List<EmailNotification> listEmail  =  generalService.getEmailNotificationList(getApplicationCountryId(), Constants.KNET_ERROR);
			//Rabil Original 
			//getMailService().sendKnetSuccessButReceiptFailEMail(userProfile.getCustomerReference(),lstKnetDetails,listEmail);
			
			getMailService().getKnetSuccessButReceiptFailEMailcontentForMultiCountry(userProfile.getCustomerReference(),lstKnetDetails,listEmail);
			}catch(Exception ex1){
				ex1.printStackTrace();
				setErrorMessage(ex1.getMessage());
			}
			
			
		}
		
		setErrorMessage(e.getMessage()+" "+status);
		RequestContext.getCurrentInstance().execute("alert.show();");
		 
	} finally {
		this.setTotalKnetAmount(new BigDecimal(0));
	}

 }
	
		
		public void successPageCustomerDetails() throws Exception{

			lstknetCustomer.clear();

			KnetSuccessPageCustomerDataTable custDetails = new KnetSuccessPageCustomerDataTable();

			custDetails.setCustomerId(getCustomerNo()+"/"+userProfile.getUserName());
			custDetails.setCustomerName(getCustomerFullName());
			custDetails.setHelpdeskNo(helpDeskNo);
			custDetails.setReceiptYear(generalService.getDealYear(new Date()).get(0).getFinancialYear());
			custDetails.setReceiptDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
			lstknetCustomer.add(custDetails);

		}
		
		
		// saving
		public String  saveRemittance(String paymentId,String authcode,String tranId,String refId) {
			 
			log.info("=====================CALLED SAVE ALL :"+paymentId+"\t authcode :"+authcode+"\t tranId :"+tranId+"\t refId :"+refId);
			String out = null;
			String errormsg = null;
			try {
				
				HashMap<String, String> remitanceMap = iPersonalRemittanceService.insertRemittanceOnline(getApplicationCountryId(), getCompanyId(), getCustomerNo(), userProfile.getUserName(), paymentId,authcode,tranId,refId);
				
				if(remitanceMap!=null && !remitanceMap.isEmpty()){
					collectionFinanceYear = new BigDecimal(remitanceMap.get("collectionFinanceYear"));
					collectionDocumentNumber = new BigDecimal(remitanceMap.get("collectionDocumentNumber"));
					collectionDocumentCode = new BigDecimal(remitanceMap.get("collectionDocumentCode"));
					out = remitanceMap.get("outMessage");
					
					log.info("collectionFinanceYear : " + collectionFinanceYear);
					log.info("collectionDocumentNumber : " + collectionDocumentNumber);
					log.info("collectionDocumentCode : " + collectionDocumentCode);
					
					
				}
				
				
				if (collectionDocumentNumber != null) {
					try {
						String output = iPersonalRemittanceService.insertEMOSLIVETransfer(getApplicationCountryId(),sessionStateManage.getCompanyId(), collectionDocumentCode, collectionFinanceYear, collectionDocumentNumber);
						log.info("EX_INSERT_EMOS_TRANSFER_LIVE : " + output);
						
					} catch (AMGException e) {
						log.error("Exception occurs while moving data to Old Emos:" + collectionFinanceYear + " - " + collectionDocumentNumber);
					} catch (Exception e1) {
						log.error("Exception occurs while moving data to Old Emos:" + collectionFinanceYear + " - " + collectionDocumentNumber);
					}
				}
				
				log.info("saveRemittance EX_INSERT_REMITTANCE_ONLINE out:"+out +"\t  collectionFinanceYear :"+collectionFinanceYear+"\t collectionDocumentNumber :"+collectionDocumentNumber+"\t collectionDocumentCode :"+collectionDocumentCode);
				

			} catch (Exception e) {
				out = errormsg+" "+e.getMessage();
				return out;
			} 
			return out;
		}
 
		
		public List<AdditionalInstructionData> saveAdditionalInstnData(RemittanceApplication remittanceApplication) throws AMGException{
			try{
			List<AdditionalInstructionData> lstAddInstrData = new ArrayList<AdditionalInstructionData>();
			log.info("========================CALEEDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD+++++++++++++++++++++=");
			Document document = new Document();
			//document.setDocumentID(generalService.getDocument(new BigDecimal(Constants.DOCUMENT_CODE_FOR_FC_SALE_APP), new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1")).get(0).getDocumentID());

			document.setDocumentID(generalService.getDocument(new BigDecimal(Constants.DOCUMENT_CODE_FOR_FC_SALE_APP), new BigDecimal("1")).get(0).getDocumentID());
			
			// company Id
			CompanyMaster companymaster = new CompanyMaster();
			companymaster.setCompanyId(sessionStateManage.getCompanyId());
			// Application Country
			CountryMaster countrymaster = new CountryMaster();
			countrymaster.setCountryId(sessionStateManage.getCountryId());

			for (AddAdditionalBankData dynamicList : listAdditionalBankDataTable) {

				AdditionalInstructionData additionalInsData = new AdditionalInstructionData();
				AdditionalBankRuleMap additionalBank = new AdditionalBankRuleMap();
				if (dynamicList.getAdditionalBankRuleFiledId() != null) {
					additionalBank.setAdditionalBankRuleId(dynamicList.getAdditionalBankRuleFiledId());
					additionalInsData.setAdditionalBankFieldsId(additionalBank);
				}

				log.info("dynamicList.getFlexiField() :"+dynamicList.getFlexiField()+"\t dynamicList.getAmicCode() :"+dynamicList.getAmicCode());

				additionalInsData.setFlexField(dynamicList.getFlexiField());
				if (dynamicList.getAdditionalBankRuleFiledId() != null) {
					String amiecdec = dynamicList.getVariableName();
					String amicCode = null;
					String amicDesc = null;
					if (amiecdec != null) {

						String[] amiecdecValues = amiecdec.split("-");
						if (amiecdecValues.length > 0) {
							amicCode = amiecdecValues[0];

						}
						if (amiecdecValues.length > 1) {
							amicDesc = amiecdecValues[1];

						}

					}

					additionalInsData.setAmiecCode(amicCode);
					additionalInsData.setFlexFieldValue(amicDesc);
				} else {
					additionalInsData.setAmiecCode(Constants.AMIEC_CODE);
					additionalInsData.setFlexFieldValue(dynamicList.getVariableName());
				}

				additionalInsData.setExDocument(document);
				additionalInsData.setFsCountryMaster(countrymaster);
				additionalInsData.setFsCompanyMaster(companymaster);
				additionalInsData.setExRemittanceApplication(remittanceApplication);
				additionalInsData.setExUserFinancialYear(remittanceApplication.getExUserFinancialYearByDocumentFinanceYear());
				additionalInsData.setDocumentFinanceYear(remittanceApplication.getDocumentFinancialyear());

				additionalInsData.setCreatedBy(Constants.CREATOR);
				additionalInsData.setCreatedDate(new Date());
				additionalInsData.setIsactive(Constants.Yes);
				additionalInsData.setDocumentNo(new BigDecimal(getDocumentNo()));


				lstAddInstrData.add(additionalInsData);
			}

			return lstAddInstrData;
			
			} catch (NullPointerException NulExp) {
				throw new AMGException(NulExp.getMessage());
			}catch (Exception exp) {
				throw new AMGException(exp.getMessage());
			}
		}
		
		
		public void successPageKnetPaymentDetails(String paymentId , String referenceId , String transactionId , String status,BigDecimal totalKnetAmount,String auth_appNo,String udf3){

			lstKnetDetails.clear();
			KnetSuccessPageKnetDetailsDataTable knetDetails = new KnetSuccessPageKnetDetailsDataTable();
			knetDetails.setPaymentId(paymentId);
			if(sessionStateManage.getCountryAlphaTwoCode()!= null && sessionStateManage.getCountryAlphaTwoCode().equalsIgnoreCase(Constants.KUWAIT_ALPHA_TWO_CODE)){
			knetDetails.setPaymentMode(Constants.KNET);
			}else if(sessionStateManage.getCountryAlphaTwoCode()!= null && sessionStateManage.getCountryAlphaTwoCode().equalsIgnoreCase(Constants.OMAN_ALPHA_TWO_CODE)){
				knetDetails.setPaymentMode(Constants.OMANNET);
			}
		
			knetDetails.setReceiptDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			knetDetails.setReferenceId(referenceId);
			knetDetails.setTotalAmount(totalKnetAmount);
			knetDetails.setTransactionId(transactionId);
			knetDetails.setApprovalNo(auth_appNo);
			knetDetails.setStatus(status);
			knetDetails.setUdf3(udf3);
			lstKnetDetails.add(knetDetails);
		}

		public void successPageKnetTransactionDetails(List<RemittanceApplicationView> remittanceViewlist){
			lstKnetTransDetails.clear();
			if(remittanceViewlist!=null && remittanceViewlist.size()!=0){
				for (RemittanceApplicationView lstSelect : remittanceViewlist) {
					KnetSuccessPageKnetTransactionDataTable lstTrnxDt = new KnetSuccessPageKnetTransactionDataTable();
					lstTrnxDt.setTransactionYear(lstSelect.getDocumentFinancialYear());
					lstTrnxDt.setTransactionDocNo(lstSelect.getDocumentNo());
					lstTrnxDt.setBeneficiaryName(lstSelect.getBeneficiaryName());
					log.info("lstSelect.getBenefeciaryAccountNo() :"+lstSelect.getBenefeciaryAccountNo()+"\t lstSelect "+lstSelect.getPinNo());
					if(lstSelect.getBenefeciaryAccountNo()!=null){
					lstTrnxDt.setAccountNo(lstSelect.getBenefeciaryAccountNo());
					}else{
						lstTrnxDt.setAccountNo(lstSelect.getPinNo());
					}
					lstTrnxDt.setBankName(lstSelect.getBeneficiaryBank());
					lstTrnxDt.setBranchName(lstSelect.getBenefeciaryBranch());
					lstTrnxDt.setPaymentChannel(lstSelect.getDeliveryDescription());
					lstTrnxDt.setFcamountStr("["+lstSelect.getCurrencyQuoteName()+"]"+lstSelect.getForeignTransactionAmount());
					lstTrnxDt.setCommission(lstSelect.getLocalCommissionAmount());
					lstTrnxDt.setRate(lstSelect.getExchangeRateApplied());
					if(lstSelect.getLocalTransactionAmount()!=null && lstSelect.getLocalTransactionCurrencyId()!=null){
						BigDecimal transationAmount=GetRound.roundBigDecimal((lstSelect.getLocalTransactionAmount()),foreignLocalCurrencyDenominationService.getDecimalPerCurrency(lstSelect.getLocalTransactionCurrencyId()));
						lstTrnxDt.setLocalamountStr(transationAmount.toString());
					}
					
					if(lstSelect.getLocalNetTransactionAmount()!=null && lstSelect.getLocalTransactionCurrencyId()!=null){
						BigDecimal netAmount=GetRound.roundBigDecimal((lstSelect.getLocalNetTransactionAmount()),foreignLocalCurrencyDenominationService.getDecimalPerCurrency(lstSelect.getLocalTransactionCurrencyId()));
						
						lstTrnxDt.setNetamount(lstSelect.getLocalNetTransactionAmount());
						
					}
					
					lstTrnxDt.setLocalamount(lstSelect.getLocalTransactionAmount());
					lstTrnxDt.setNetamount(lstSelect.getLocalNetTransactionAmount());
					lstKnetTransDetails.add(lstTrnxDt);
				}
			}

		}
		
		public void  successPageKnetTransactionDetailsWhileFail(List<ShoppingCartDetails> applicationViewList){
			lstKnetTransDetails.clear();
			if(applicationViewList!=null && applicationViewList.size()!=0){
				for (ShoppingCartDetails lstSelect : applicationViewList) {
					KnetSuccessPageKnetTransactionDataTable lstTrnxDt = new KnetSuccessPageKnetTransactionDataTable();
					lstTrnxDt.setTransactionDocNo(lstSelect.getDocumentNo());
					lstTrnxDt.setBeneficiaryName(lstSelect.getBeneficiaryName());
					lstTrnxDt.setAccountNo(lstSelect.getBeneficiaryAccountNo());
					lstTrnxDt.setBankName(lstSelect.getBeneficiaryBank());
					lstTrnxDt.setBranchName(lstSelect.getBeneficiaryBranch());
					lstTrnxDt.setPaymentChannel(lstSelect.getDeliveryDescription());
					lstTrnxDt.setFcamount(lstSelect.getForeignTranxAmount());
					lstTrnxDt.setCommission(lstSelect.getLocalCommisionAmount());
					lstTrnxDt.setRate(lstSelect.getExchangeRateApplied());
					if(lstSelect.getLocalTranxAmount()!=null && lstSelect.getLocalCurrency()!=null){
						BigDecimal transationAmount=GetRound.roundBigDecimal((lstSelect.getLocalTranxAmount()),foreignLocalCurrencyDenominationService.getDecimalPerCurrency(lstSelect.getLocalCurrency()));
						lstTrnxDt.setLocalamountStr(transationAmount.toString());
					}
					if(lstSelect.getLocalNextTranxAmount()!=null && lstSelect.getLocalNextTranxAmount()!=null){
						BigDecimal netAmount=GetRound.roundBigDecimal((lstSelect.getLocalNextTranxAmount()),foreignLocalCurrencyDenominationService.getDecimalPerCurrency(lstSelect.getLocalCurrency()));
						lstTrnxDt.setNetamount(lstSelect.getLocalCurrency());
					}
					lstTrnxDt.setLocalamount(lstSelect.getLocalTranxAmount());
					lstTrnxDt.setNetamount(lstSelect.getLocalNextTranxAmount());
					lstKnetTransDetails.add(lstTrnxDt);
				}
			}
		}

		
	public void loyaltyPoints(BigDecimal cusRefernce){
		HashMap<String, String> loyaltiPoints  =iPersonalRemittanceService.getloyalityPointsFromProcedure(cusRefernce, getDocumentDate());
		setLoyalityPointExpiring1(null);
		setLoyalityPointExpiring2(null);
		setInsurence1(null);
		setInsurence2(null);
		
		String prLtyStr1 =loyaltiPoints.get("P_LTY_STR1");
		String prLtyStr2 =loyaltiPoints.get("P_LTY_STR2");
		String prInsStr1 =loyaltiPoints.get("P_INS_STR1");
		String prInsStr2 =loyaltiPoints.get("P_INS_STR2");
		String prInsStrAr1 =loyaltiPoints.get("P_INS_STR_AR1");
		String prInsStrAr2 =loyaltiPoints.get("P_INS_STR_AR2");


		StringBuffer loyaltyPoint = new StringBuffer();
		if(!prLtyStr1.trim().equals("")){
			loyaltyPoint.append(prLtyStr1);
		}
		if(!prLtyStr2.trim().equals("")){
			loyaltyPoint.append("");
			loyaltyPoint.append(prLtyStr2);
		}
		
			setLoyalityPointExpiring(loyaltyPoint.toString());
	
		if(prLtyStr1!=null){
				setLoyalityPointExpiring1(prLtyStr1);
		}
		if(prLtyStr2!=null){
			setLoyalityPointExpiring2(prLtyStr2);
	      }
			

		StringBuffer insurence1 = new StringBuffer();
		if(!prInsStr1.trim().equals("")){
			insurence1.append(prInsStr1);
		}
		if(!prInsStrAr1.trim().equals("")){
			insurence1.append("");
			insurence1.append(prInsStrAr1);
		}
		setInsurence1(insurence1.toString());

		StringBuffer insurence2 = new StringBuffer();
		if(!prInsStr2.trim().equals("")){
			insurence2.append(prInsStr2);
		}
		if(!prInsStrAr2.trim().equals("")){
			insurence2.append(prInsStrAr2);
		}
		setInsurence2(insurence2.toString());


	}
		
		

		// to render to show last panel
		public void nextrenderingLastPanel() {
			setVisible(false);
			setBooRenderTransferFundPanel(false);
			setBooRenderAdditionalDataPanel(false);

			setBoorenderlastpanel(true);

			getShoppingCartDetails(getCustomerNo());

		}

		
	
	
	public void getServiceListDetails() throws AMGException{
		try{
		List<PopulateData> serviceList = iPersonalRemittanceService.getServiceList(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),
				getDataBankbenificarycountry(), getDatabenificarycurrency(), getServiceGroupCode());
		this.setServiceList(serviceList);
		if (serviceList != null && serviceList.size() > 0) {
			setBooMultipleService(true);
			setBooSingleService(false);
			setDataserviceid(serviceList.get(0).getPopulateId());
			countryNameByServiceId();
		} else {
			throw new AMGException(props.getString("lbl.serviceNotAvailble"));
		}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
		
	}

	/**
	 * Added by Rabil .
	 */
	public void countryNameByServiceId() throws AMGException{
		try{
		if (getDataserviceid() != null) {
			HashMap<BigDecimal, String> lstCountryAlphaCode = fetchAllCountryAlphaCode();
			List<ServiceMasterDesc> lstServiceMaster = generalService.LocalServiceDescriptionFromDB(
					new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"), getDataserviceid());
			if (lstServiceMaster != null && !lstServiceMaster.isEmpty()) {
				ServiceMasterDesc serviceMasterDetails = lstServiceMaster.get(0);
				setDatabenificaryservice(serviceMasterDetails.getLocalServiceDescription());
				setDatabenificaryservicecode(serviceMasterDetails.getServiceMaster().getServiceCode());
			}
			// fetch routing country from view
			List<PopulateData> lstRoutingCountry = iPersonalRemittanceService.getRoutingCountryList(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),
					getDataBankbenificarycountry(), getDatabenificarycurrency(), getDataserviceid(),
					new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
			if (lstRoutingCountry.size() == 0) {
				setBooSingleRoutingCountry(true);
				setBooMultipleRoutingCountry(false);
				setRoutingCountryName(null);
				setRoutingCountry(null);
				
				throw new AMGException(props.getString("lbl.routingCountryNotAvailable"));
				 
			} else if (lstRoutingCountry.size() == 1) {
				setBooSingleRoutingCountry(true);
				setBooMultipleRoutingCountry(false);
				setRoutingCountry(lstRoutingCountry.get(0).getPopulateId());
				setRoutingCountryName(lstRoutingCountry.get(0).getPopulateName());
				setDataroutingcountryalphacode(lstCountryAlphaCode.get(getRoutingCountry()));
				// setting routing bank Id from view
				bankDetailsByCountry();
			} else {
				setRoutingCountryName(null);
				setRoutingCountry(null);
				setDataroutingcountryalphacode(null);
				setBooSingleRoutingCountry(false);
				setBooMultipleRoutingCountry(true);
				setRoutingCountrylst(lstRoutingCountry);
			}
		}
		
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	// fetch all country alpha code
	public HashMap<BigDecimal, String> fetchAllCountryAlphaCode() {
		HashMap<BigDecimal, String> lstCountryAlphaCode = new HashMap<BigDecimal, String>();
		List<CountryMasterView> lstCountry = generalService.getCountryList(sessionStateManage.getLanguageId());
		if (lstCountry != null && !lstCountry.isEmpty()) {
			for (CountryMasterView countryMasterDesc : lstCountry) {
				lstCountryAlphaCode.put(countryMasterDesc.getCountryId(), countryMasterDesc.getCountryAlpha2Code());
			}
		} else {
			lstCountryAlphaCode.clear();
		}
		return lstCountryAlphaCode;
	}

	
	public void bankDetailsByCountry() throws AMGException{
		try{
		log.info("bankDetailsByCountry getDataserviceid bankDetailsByCountry :" + getDataserviceid());
		if (getRoutingCountry() != null) {
			log.info("Service name : " + getDatabenificaryservice());
			Boolean ttCheckSameNotAllow = false;
			if (getDatabenificaryservice().equalsIgnoreCase(Constants.TTNAME)) {
				ttCheckSameNotAllow = true;
			} else {
				ttCheckSameNotAllow = false;
			}
			List<PopulateData> lstRoutingBank = iPersonalRemittanceService.getRoutingBankList(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),getDataBankbenificarycountry(), getDatabenificarycurrency(), getDataserviceid(), getRoutingCountry(), ttCheckSameNotAllow);
			if (lstRoutingBank.size() == 0) {
				setBooSingleRoutingBank(true);
				setBooMultipleRoutingBank(false);
				setRoutingBankName(null);
				setRoutingBank(null);
				throw new AMGException(props.getString("lbl.routingBankNotAvailble"));
			} else if (lstRoutingBank.size() == 1) {
				setBooSingleRoutingBank(true);
				setBooMultipleRoutingBank(false);
				setRoutingBank(lstRoutingBank.get(0).getPopulateId());
				setRoutingBankName(lstRoutingBank.get(0).getPopulateName());
				remittancelistByBankId();
			} else {
				setRoutingBankName(null);
				setRoutingBank(null);
				setBooSingleRoutingBank(false);
				setBooMultipleRoutingBank(true);
				setRoutingbankvalues(lstRoutingBank);
			}
		}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public void checkingInstrnSiftBanksRequired() throws AMGException{
		try {
		HashMap<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("APPLICATION_COUNTRY_ID", sessionStateManage.getCountryId().toString());
		inputValues.put("ROUTING_COUNTRY_ID", getRoutingCountry().toString());
		inputValues.put("ROUTING_CURRENCY_ID", getForiegnCurrency().toString());
		inputValues.put("REMITTANCE_MODE_ID", getRemitMode().toString());
		inputValues.put("DELIVERY_MODE_ID", getDeliveryMode().toString());
		HashMap<String, String> checkData = new HashMap<String, String>();
			checkData = iPersonalRemittanceService.getSwitInstrustionFromServiceApplicability(inputValues);
			if (!checkData.isEmpty()) {
				if (checkData.get("BENEFICIARY_SWIFT_BANK1").equalsIgnoreCase(Constants.Yes)) {
					setBooRenderSwiftBank1(true);
				} else {
					setBooRenderSwiftBank1(false);
				}
				if (checkData.get("BENEFICIARY_SWIFT_BANK2").equalsIgnoreCase(Constants.Yes)) {
					setBooRenderSwiftBank2(true);
				} else {
					setBooRenderSwiftBank2(false);
				}
				if (checkData.get("INSTRUCTION").equalsIgnoreCase(Constants.Yes)) {
					setBooRenderInstructions(false);
				} else {
					setBooRenderInstructions(false);
				}
			}
		} catch (NullPointerException nullExp) {
			setExceptionMessage(nullExp.getMessage());
			log.error("checkingInstrnSiftBanksRequired :"+nullExp.getMessage());
			throw new AMGException(getExceptionMessage());
		} catch (Exception e) {
			setExceptionMessage(e.getMessage());
			log.error("checkingInstrnSiftBanksRequired :"+e.getMessage());
			throw new AMGException(getExceptionMessage());
		}
	}

	/**
	 * 
	 * @return source of income
	 */
	public void fetchSourceOfIncomeList()throws AMGException{
		
		try {
		if (lstSourceOfIncome != null || !lstSourceOfIncome.isEmpty()) {
			lstSourceOfIncome.clear();
		}
			//List<SourceOfIncomeDescription> lstSourceOfIncomesDesc = foreignCurrencyPurchaseService.getSourceofIncome(sessionStateManage.getLanguageId());
		List<SourceOfIncomeDescription> lstSourceOfIncomesDesc = foreignCurrencyPurchaseService.getSourceofIncome(new BigDecimal("1"));
			if (lstSourceOfIncomesDesc != null && lstSourceOfIncomesDesc.size() != 0) {
				lstSourceOfIncome.addAll(lstSourceOfIncomesDesc);
			}
		} catch (Exception e) {
			throw new AMGException(e==null?"":e.getMessage());
		}
	}

	/**
	 * @return purpose of transaction
	 */
	public void fetchPurposeOfTransactionsList() throws AMGException {
		if (lstPurposeOfTransaction != null || !lstPurposeOfTransaction.isEmpty()) {
			lstPurposeOfTransaction.clear();
		}
		try {
			List<PurposeOfTransaction> lstPurposeOfTransactions = getForeignCurrencyPurchaseService().getAllPurposeOfTransaction();
			if (lstPurposeOfTransactions != null && lstPurposeOfTransactions.size() != 0) {
				lstPurposeOfTransaction.addAll(lstPurposeOfTransactions);
			}
		} catch (Exception e) {
			throw new AMGException(e==null?"":e.getMessage());
		}
	}

	public void dynamicLevel() throws AMGException {
		try{
		listDynamicLebel.clear();
		setExceptionMessage("");
		List<AdditionalDataDisplayView> serviceAppRuleList = iPersonalRemittanceService.getAdditionalDataFromServiceApplicability(sessionStateManage.getCountryId(), getRoutingCountry(),
				getForiegnCurrency(), getRemitMode(), getDeliveryMode());
		if (serviceAppRuleList.size() > 0) {
			for (AdditionalDataDisplayView serviceRule : serviceAppRuleList) {
				AddDynamicLabel addDynamic = new AddDynamicLabel();
				addDynamic.setLebelId(serviceRule.getServiceApplicabilityRuleId());
				addDynamic.setFieldLength(serviceRule.getFieldLength());
				if (serviceRule.getFieldDescription() != null) {
					addDynamic.setLebelDesc(serviceRule.getFieldDescription());
				}
				addDynamic.setFlexiField(serviceRule.getFlexField());
				addDynamic.setValidation(serviceRule.getValidationsReq());
				addDynamic.setNavicable(serviceRule.getIsRendered());
				addDynamic.setMinLenght(serviceRule.getMinLength());
				addDynamic.setMaxLenght(serviceRule.getMaxLength());
				if (serviceRule.getIsRequired() != null && serviceRule.getIsRequired().equalsIgnoreCase(Constants.Yes)) {
					addDynamic.setMandatory("*");
					addDynamic.setRequired(true);
				}
				listDynamicLebel.add(addDynamic);
			}
			setAdditionalCheck(true);
		} else {
			setAdditionalCheck(false);
		}
		}catch(Exception er){
			throw new AMGException(er==null?"":er.getMessage());
		}
	}

	public void matchData() throws AMGException{
		try {
		setExceptionMessage(null);
		listAdditionalBankDataTable.clear();
			for (AddDynamicLabel dyamicLabel : listDynamicLebel) {
				AddAdditionalBankData adddata = new AddAdditionalBankData();
				if (dyamicLabel.getValidation() != null && dyamicLabel.getValidation().equalsIgnoreCase(Constants.Yes)) {
					List<AdditionalBankRuleMap> listAdditinalBankfield = iPersonalRemittanceService.getDynamicLevelMatch(getRoutingCountry(), dyamicLabel.getFlexiField());
					if (listAdditinalBankfield.size() > 0) {
						for (AdditionalBankRuleMap listAdd : listAdditinalBankfield) {
							List<AdditionalBankDetailsView> listAdditionaView = iPersonalRemittanceService.getAmiecDetails(getForiegnCurrency(), getRoutingBank(), getRemitMode(), getDeliveryMode(),
									getRoutingCountry(), listAdd.getFlexField());
							if (listAdditionaView.size() > 0) {
								// setting dynamic functionality
								adddata.setMandatory(dyamicLabel.getMandatory());
								if (dyamicLabel.getMinLenght() != null) {
									adddata.setMinLenght(dyamicLabel.getMinLenght().intValue());
								} else {
									adddata.setMinLenght(0);
								}
								if (dyamicLabel.getMaxLenght() != null) {
									adddata.setMaxLenght(dyamicLabel.getMaxLenght());
								} else {
									adddata.setMaxLenght(new BigDecimal(50));
								}
								adddata.setFieldLength(dyamicLabel.getFieldLength());
								adddata.setRequired(dyamicLabel.getRequired());
								adddata.setAdditionalBankRuleFiledId(listAdd.getAdditionalBankRuleId());
							
								adddata.setFlexiField(listAdd.getFlexField());
								if (listAdd.getFieldName() != null) {
									adddata.setAdditionalDesc(listAdd.getFieldName());
								} else {
									setExceptionMessage((getExceptionMessage().equalsIgnoreCase("") ? "" : ",") + dyamicLabel.getFlexiField());
								}
								adddata.setRenderInputText(false);
								adddata.setRenderSelect(true);
								adddata.setRenderOneSelect(false);
								System.out.println("listAdditionaView:"+listAdditionaView.size());
								
								System.out.println("listAdditionaView:"+listAdditionaView.size());
								for(AdditionalBankDetailsView lst:listAdditionaView){
									System.out.println("listAdditionaView:"+lst.getAmiecCode()+"\t Desc :"+lst.getAmieceDescription());
									
								}
								
								
								
								adddata.setListadditionAmiecData(listAdditionaView);
							}
						}
						if (getExceptionMessage() != null && !getExceptionMessage().equalsIgnoreCase("")) {
							setAdditionalCheck(false);
							setExceptionMessage(getExceptionMessage());
						//	RequestContext.getCurrentInstance().execute("dataexception.show();");
							throw new AMGException(getExceptionMessage());
						} else {
							setAdditionalCheck(true);
							setExceptionMessage(null);
						}
					}
				} else {
					 
					adddata.setMandatory(dyamicLabel.getMandatory());
					if (dyamicLabel.getMinLenght() != null) {
						adddata.setMinLenght(dyamicLabel.getMinLenght().intValue());
					} else {
						adddata.setMinLenght(0);
					}
					if (dyamicLabel.getMaxLenght() != null) {
						adddata.setMaxLenght(dyamicLabel.getMaxLenght());
					} else {
						adddata.setMaxLenght(new BigDecimal(50));
					}
					adddata.setFieldLength(dyamicLabel.getFieldLength());
					adddata.setRequired(dyamicLabel.getRequired());
					adddata.setRenderInputText(true);
					adddata.setRenderSelect(false);
					adddata.setRenderOneSelect(false);
					adddata.setFlexiField(dyamicLabel.getFlexiField());
					if (dyamicLabel.getLebelDesc() != null) {
						adddata.setAdditionalDesc(dyamicLabel.getLebelDesc());
					} else {
						List<AdditionalBankRuleMap> listAdditinalBankfield = iPersonalRemittanceService.getDynamicLevelMatch(getRoutingCountry(), dyamicLabel.getFlexiField());
						if (listAdditinalBankfield.size() > 0) {
							if (listAdditinalBankfield.get(0).getFieldName() != null) {
								adddata.setAdditionalDesc(listAdditinalBankfield.get(0).getFieldName());
							} else {
								setExceptionMessage((getExceptionMessage().equalsIgnoreCase("") ? "" : ",") + dyamicLabel.getFlexiField());
							}
						} else {
							setExceptionMessage((getExceptionMessage().equalsIgnoreCase("") ? "" : ",") + dyamicLabel.getFlexiField());
						}
					}
				}
				listAdditionalBankDataTable.add(adddata);
				
				for(AddAdditionalBankData lst : listAdditionalBankDataTable){
					//System.out.println("listAdditionaView:"+lst.getAdditionalDesc());
					if(lst.getAdditionalDesc()!= null && lst.getAdditionalDesc().equalsIgnoreCase("PURPOSE OF REMITTANCE")){
						List<AdditionalBankDetailsView> lstAme = new ArrayList<AdditionalBankDetailsView>();
						for(AdditionalBankDetailsView amiec :lst.getListadditionAmiecData()){
							if(amiec.getAmiecCode() !=null && !amiec.getAmieceDescription().contains("TRADE")){
								lstAme.add(amiec);
							}
						}
						lst.getListadditionAmiecData().clear();
						lst.getListadditionAmiecData().addAll(lstAme);
					}

				}
			}
			if (getExceptionMessage() != null && !getExceptionMessage().equalsIgnoreCase("")) {
				setAdditionalCheck(false);
				setExceptionMessage(getExceptionMessage());
				throw new AMGException(getExceptionMessage());
			} else {
				setAdditionalCheck(true);
				setExceptionMessage(null);
			}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public void fetchingAllSwiftMaster() throws AMGException{
		try{
		lstSwiftMasterBank1.clear();
		lstSwiftMasterBank2.clear();
		setBeneSwiftBank1(null);
		setBeneSwiftBank2(null);
		setBeneSwiftBankAddr1(null);
		setBeneSwiftBankAddr2(null);
			List<PopulateData> lstSwiftRecords = iPersonalRemittanceService.fetchingViewSwiftMasterByCountryId(getDataBankbenificarycountry());
			if (lstSwiftRecords.size() != 0) {
				lstSwiftMasterBank1.addAll(lstSwiftRecords);
				lstSwiftMasterBank2.addAll(lstSwiftRecords);
			}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public void getSourceofIncomeDetails() throws AMGException{
		try{
		lstSourceofIncome.clear();
		List<SourceOfIncomeDescription> lstSource = foreignCurrencyPurchaseService.getSourceofIncome(sessionStateManage.getLanguageId());
		if (lstSource.size() != 0) {
			lstSourceofIncome.addAll(lstSource);
		}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public HashMap<String, String> bankIndicatorsProcedureCheck() {
		HashMap<String, String> bankIndicatorOutValues = null;
		try {
			HashMap<String, String> bankIndicatorInValues = new HashMap<String, String>();
			bankIndicatorInValues.put("P_APPLICATION_COUNTRY_ID", (sessionStateManage.getCountryId()).toString());
			bankIndicatorInValues.put("P_ROUTING_COUNTRY_ID", getRoutingCountry().toString());
			bankIndicatorInValues.put("P_CURRENCY_ID", getForiegnCurrency().toString());
			bankIndicatorInValues.put("P_ROUTING_BANK_ID", getRoutingBank().toString());
			bankIndicatorInValues.put("P_REMITTANCE_MODE_ID", getRemitMode().toString());
			bankIndicatorInValues.put("P_DELIVERY_MODE_ID", getDeliveryMode().toString());
			bankIndicatorOutValues = iPersonalRemittanceService.exPBankIndicatorsProcedureCheck(bankIndicatorInValues, listAdditionalBankDataTable);
		} catch (AMGException e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("sqlexception.show();");
		}
		return bankIndicatorOutValues;
	}

	public HashMap<String, String> getAdditionalBeneDetails() {
		HashMap<String, String> additionalBeneDetails = null;
		try {
			additionalBeneDetails = iPersonalRemittanceService.toFetchDetilaFromAddtionalBenficiaryDetails(getMasterId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),
					getBeneficiaryAccountSeqId(), getRoutingCountry(), getRoutingBank(), getRoutingBranch(), getDataserviceid(), // getBeneficaryBankId(),
																																	// bankId
					sessionStateManage.getCountryId(), getForiegnCurrency(), // Foriegn
																				// CurrencyId
					getRemitMode(), getDeliveryMode());
		} catch (AMGException e) {
			setExceptionMessage("EX_GET_ADDL_BENE_DETAILS" + " : " + e.getMessage());
			RequestContext.getCurrentInstance().execute("sqlexception.show();");
		}
		log.info("additionalCheckMessage :" + additionalBeneDetails);
		return additionalBeneDetails;
	}

	public String getAdditionalCheckProcedure() {
		String errorMessage = "";
		String additionalCheckMessage = null;
		try {
			additionalCheckMessage = iPersonalRemittanceService.getAdditionalCheckProcedure(sessionStateManage.getCountryId(), getCustomerNo(), new BigDecimal(sessionStateManage.getBranchId()),
					getMasterId(),
					getDataBankbenificarycountry(), getBeneficaryBankId(), getBeneficaryBankBranchId(), getDataAccountnum(), getDataserviceid(), getRoutingCountry(), getRoutingBank(),
					getRoutingBranch(), getRemitMode(), getDeliveryMode(), getSourceOfIncome(), getExchangeRate(), new BigDecimal(sessionStateManage.getCurrencyId()), // localCommisionCurrencyId
					getCommission(), new BigDecimal(sessionStateManage.getCurrencyId()), // localChargeCurrencyId
					getOverseasamt(), new BigDecimal(sessionStateManage.getCurrencyId()), // localDelivCurrencyId
					getGrossAmountCalculated(), new BigDecimal(0),// serviceProvider
																	 
					getDatabenificarycurrency(), getNetAmountSent(), new BigDecimal(sessionStateManage.getCurrencyId()), // localNetCurrecnyId,
					getNetAmountPayable(), getBeneSwiftBank1(), getBeneSwiftBank2(),// beneSwiftBank1,beneSwiftBank2,
					errorMessage);
		} catch (AMGException e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("sqlexception.show();");
		}
		log.info("additionalCheckMessage :" + additionalCheckMessage);
		return additionalCheckMessage;
	}

	public BigDecimal getFinaceYear() {
		try {
			List<UserFinancialYear> financialYearList = foreignCurrencyPurchaseService.getUserFinancialYear(new Date());
			log.info("financialYearList :" + financialYearList.size());
			if (financialYearList != null) {
				finaceYear = financialYearList.get(0).getFinancialYear();
				setFinaceYear(finaceYear);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return finaceYear;
	}

	public BigDecimal getMinLenght() {
		for (AddAdditionalBankData listAdditionals : listAdditionalBankDataTable) {
			setMinLenght(new BigDecimal(listAdditionals.getMinLenght()));
			break;
		}
		return minLenght;
	}
	
	
	public void clearTransactionDetails() {
		setDatabenificarycountry(null);
		setDatabenificarycurrency(null);
		setDatabenificaryservice(null);
		setDataserviceid(null);
		setDataAccountnum(null);
		setDatabenificarycountryname(null);
		setDatabenificarycurrencyname(null);
		setDatabenificarybankname(null);
		setDatabenificarybranchname(null);
		setDatabenificaryname(null);
		setBenificiaryryNameRemittance(null);
		setMasterId(null);
		setBeneficaryBankId(null);
		setBeneficaryBankBranchId(null);
		setRoutingCountryName(null);
		setRoutingCountry(null);
		setRoutingBankName(null);
		setRoutingBank(null);
		setRoutingBranchName(null);
		setRoutingBranch(null);
		setBenificarystatus(null);
		setBenificaryTelephone(null);
		setCurrency(null);
		setSpotRate(null);
		setSpecialRateRef(null);
		setAvailLoyaltyPoints(null);
		setChargesOverseas(null);
		setForiegnCurrency(null);
		setRoutingCountrylst(null);
		if (getRoutingCountrylst() != null && routingCountrylst.size() != 0) {
			routingCountrylst.clear();
		}
		setRoutingbankvalues(null);
		if (getRoutingbankvalues() != null && routingbankvalues.size() != 0) {
			routingbankvalues.clear();
		}
		setLstofRemittance(null);
		if (getLstofRemittance() != null && lstofRemittance.size() != 0) {
			lstofRemittance.clear();
		}
		setLstofDelivery(null);
		if (getLstofDelivery() != null && lstofDelivery.size() != 0) {
			lstofDelivery.clear();
		}
		setRoutingBankBranchlst(null);
		if (getRoutingBankBranchlst() != null && routingBankBranchlst.size() != 0) {
			routingBankBranchlst.clear();
		}
		
		// new Records from procedure
		setNewDeliveryModeId(null);
		setNewDeliveryModeName(null);
		setBooRenderDelivery(false);
		setNewRemittanceModeId(null);
		setNewRemittanceModeName(null);
		setBooRenderRemit(false);
		setPbeneFifthName(null);
		setPbeneFirstName(null);
		setPbeneFourthName(null);
		setPbeneFullName(null);
		setPbeneSecondName(null);
		setPbeneThirdName(null);
		
		// clearing bank bene id
		setBeneStateId(null);
		setBeneDistrictId(null);
		setBeneCityId(null);
		
		setProcedureError(null);
		setExceptionMessage(null);
		setExceptionMessageForReport(null);
		setErrorMessage(null);
		
		setBeneficiaryDistId(null);
		setBeneficiaryStateId(null);
		setBeneficiaryCityId(null);
		setBeneficiaryAccountType(null);
		setTelePhoneCode(null);
		setBeneTelePhoneNum(null);
		setBeneMobilePhoneNum(null);
		setMobileCode(null);
		
		setDebitCard1(null);
		setDebitCard2(null);
		setDebitCard3(null);
		setDebitCard4(null);
		
		setSwiftBic(null);
		
	}

	public void remittancelistByBankIdForCash() throws AMGException{
		try{
		// spl pool based on routing country , routing bank
		List<PopulateData> lstRemitView;
			lstRemitView = iPersonalRemittanceService.getRemittanceListByCountryBankForCashProduct(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),getDataBankbenificarycountry(), getDatabenificarycurrency(), getDataserviceid(), getRoutingCountry(), getRoutingBank(), getRoutingBranch(),
					new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
			if (lstRemitView.size() == 0) {
				setBooSingleRemit(true);
				setBooMultipleRemit(false);
				setRemittanceName(null);
				setRemitMode(null);
				throw new AMGException(props.getString("lbl.remittanceNameNotAvailble"));
			} else if (lstRemitView.size() == 1) {
				setBooSingleRemit(true);
				setBooMultipleRemit(false);
				setRemittanceName(lstRemitView.get(0).getPopulateName());
				setRemitMode(lstRemitView.get(0).getPopulateId());
				deliverylistByRemitIdForCash();
			} else {
				setRemittanceName(null);
				setRemitMode(null);
				setBooSingleRemit(false);
				setBooMultipleRemit(true);
				setLstofRemittance(lstRemitView);
			}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public void deliverylistByRemitIdForCash() throws AMGException{
		try {
		List<PopulateData> lstDeliveryView;
			lstDeliveryView = iPersonalRemittanceService.getDeliveryListByCountryBankRemitForCashProduct(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),
					getDataBankbenificarycountry(), getDatabenificarycurrency(), getDataserviceid(), getRoutingCountry(), getRoutingBank(), getRoutingBranch(), getRemitMode(), new BigDecimal(
							sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
			if (lstDeliveryView.size() == 0) {
				setBooRenderDeliveryModeInputPanel(true);
				setBooRenderDeliveryModeDDPanel(false);
				setDeliveryModeInput(null);
				setDeliveryMode(null);
				throw new AMGException(props.getString("lbl.deliveryNameNotAvailable"));
				
			} else if (lstDeliveryView.size() == 1) {
				setBooRenderDeliveryModeInputPanel(true);
				setBooRenderDeliveryModeDDPanel(false);
				setDeliveryModeInput(lstDeliveryView.get(0).getPopulateName());
				setDeliveryMode(lstDeliveryView.get(0).getPopulateId());
			} else {
				setDeliveryModeInput(null);
				setDeliveryMode(null);
				setBooRenderDeliveryModeInputPanel(false);
				setBooRenderDeliveryModeDDPanel(true);
				setLstofDelivery(lstDeliveryView);
			}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	
	// to fetch shopping cart records bot remittance and fcsale
		public void getShoppingCartDetails(BigDecimal customerNo) {
			shoppingcartDTList.clear();
			List<ShoppingCartDetails> shoppingCartList = new ArrayList<ShoppingCartDetails>();
			try {
				shoppingCartList = iPersonalRemittanceService.getShoppingCartDetails(customerNo);
				if (shoppingCartList.size() > 0) {
					for (ShoppingCartDetails shoppingCartDetails : shoppingCartList) {
						ShoppingCartDataTableBean shoppingCartDataTableBean = new ShoppingCartDataTableBean();
						if (shoppingCartDetails.getRemittanceApplicationId() != null)
							shoppingCartDataTableBean.setRemittanceApplicationId(shoppingCartDetails.getRemittanceApplicationId());
						if (shoppingCartDetails.getApplicationType() != null)
							shoppingCartDataTableBean.setApplicationType(shoppingCartDetails.getApplicationType());
						if (shoppingCartDetails.getApplicationTypeDesc() != null)
							shoppingCartDataTableBean.setApplicationTypeDesc(shoppingCartDetails.getApplicationTypeDesc());
						if (shoppingCartDetails.getBeneficiaryAccountNo() != null)
							shoppingCartDataTableBean.setBeneficiaryAccountNo(shoppingCartDetails.getBeneficiaryAccountNo());
						if (shoppingCartDetails.getBeneficiaryBank() != null)
							shoppingCartDataTableBean.setBeneficiaryBank(shoppingCartDetails.getBeneficiaryBank());
						if (shoppingCartDetails.getBeneficiaryBranch() != null)
							shoppingCartDataTableBean.setBeneficiaryBranch(shoppingCartDetails.getBeneficiaryBranch());
						if (shoppingCartDetails.getBeneficiaryFirstName() != null)
							shoppingCartDataTableBean.setBeneficiaryFirstName(shoppingCartDetails.getBeneficiaryFirstName());
						if (shoppingCartDetails.getBeneficiarySecondName() != null)
							shoppingCartDataTableBean.setBeneficiarySecondName(shoppingCartDetails.getBeneficiarySecondName());
						if (shoppingCartDetails.getBeneficiaryThirdName() != null)
							shoppingCartDataTableBean.setBeneficiaryThirdName(shoppingCartDetails.getBeneficiaryThirdName());
						if (shoppingCartDetails.getBeneficiaryFourthName() != null)
							shoppingCartDataTableBean.setBeneficiaryFourthName(shoppingCartDetails.getBeneficiaryFourthName());
						if (shoppingCartDetails.getBeneficiaryId() != null)
							shoppingCartDataTableBean.setBeneficiaryId(shoppingCartDetails.getBeneficiaryId());
						if (shoppingCartDetails.getBeneficiarySwiftAddrOne() != null)
							shoppingCartDataTableBean.setBeneficiaryInterBankOne(shoppingCartDetails.getBeneficiarySwiftAddrOne());
						if (shoppingCartDetails.getBeneficiarySwiftAddrTwo() != null)
							shoppingCartDataTableBean.setBeneficiaryInterBankTwo(shoppingCartDetails.getBeneficiarySwiftAddrTwo());
						if (shoppingCartDetails.getBeneficiarySwiftBankOne() != null)
							shoppingCartDataTableBean.setBeneficiarySwiftBankOne(shoppingCartDetails.getBeneficiarySwiftBankOne());
						if (shoppingCartDetails.getBeneficiarySwiftBankTwo() != null)
							shoppingCartDataTableBean.setBeneficiarySwiftBankTwo(shoppingCartDetails.getBeneficiarySwiftBankTwo());
						if (shoppingCartDetails.getBeneficiaryName() != null)
							shoppingCartDataTableBean.setBeneficiaryName(shoppingCartDetails.getBeneficiaryName());
						if (shoppingCartDetails.getCompanyId() != null)
							shoppingCartDataTableBean.setCompanyId(shoppingCartDetails.getCompanyId());
						if (shoppingCartDetails.getDocumentFinanceYear() != null)
							shoppingCartDataTableBean.setDocumentFinanceYear(shoppingCartDetails.getDocumentFinanceYear());
						if (shoppingCartDetails.getDocumentId() != null)
							shoppingCartDataTableBean.setDocumentId(shoppingCartDetails.getDocumentId());
						if (shoppingCartDetails.getForeignTranxAmount() != null)
							shoppingCartDataTableBean.setForeignTranxAmount(GetRound.roundBigDecimal(shoppingCartDetails.getForeignTranxAmount(), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(shoppingCartDetails.getForeignCurrency())));
						if (shoppingCartDetails.getLocalTranxAmount() != null)
							shoppingCartDataTableBean.setLocalTranxAmount(GetRound.roundBigDecimal(shoppingCartDetails.getLocalTranxAmount(), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
						if (shoppingCartDetails.getLocalChargeAmount() != null)
							shoppingCartDataTableBean.setLocalChargeAmount(GetRound.roundBigDecimal(shoppingCartDetails.getLocalChargeAmount(), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
						if (shoppingCartDetails.getLocalCommisionAmount() != null)
							shoppingCartDataTableBean.setLocalCommisionAmount(GetRound.roundBigDecimal(shoppingCartDetails.getLocalCommisionAmount(), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
						if (shoppingCartDetails.getLocalDeliveryAmount() != null)
							shoppingCartDataTableBean.setLocalDeliveryAmount(shoppingCartDetails.getLocalDeliveryAmount());
						if (shoppingCartDetails.getIsActive() != null)
							shoppingCartDataTableBean.setIsActive(shoppingCartDetails.getIsActive());
						if (shoppingCartDetails.getLocalNextTranxAmount() != null)
							shoppingCartDataTableBean.setLocalNextTranxAmount(GetRound.roundBigDecimal(shoppingCartDetails.getLocalNextTranxAmount(), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
						if (shoppingCartDetails.getCustomerId() != null)
							shoppingCartDataTableBean.setCustomerId(shoppingCartDetails.getCustomerId());
						if (shoppingCartDetails.getExchangeRateApplied() != null)
							shoppingCartDataTableBean.setExchangeRateApplied(shoppingCartDetails.getExchangeRateApplied());
						if (shoppingCartDetails.getApplicationId() != null)
							shoppingCartDataTableBean.setApplicationDetailsId(shoppingCartDetails.getApplicationId());
						if (shoppingCartDetails.getDocumentNo() != null)
							shoppingCartDataTableBean.setDocumentNo(shoppingCartDetails.getDocumentNo());
						if (shoppingCartDetails.getForeignCurrency() != null)
							shoppingCartDataTableBean.setForeigncurrency(shoppingCartDetails.getForeignCurrency());
						if (shoppingCartDetails.getForeignCurrencyDesc() != null)
							shoppingCartDataTableBean.setForeignCurrencyDesc(shoppingCartDetails.getForeignCurrencyDesc());
						if (shoppingCartDetails.getLocalCurrency() != null)
							shoppingCartDataTableBean.setLocalcurrency(shoppingCartDetails.getLocalCurrency());
						if (shoppingCartDetails.getSpldeal() != null) {
							shoppingCartDataTableBean.setSpldeal(shoppingCartDetails.getSpldeal());
							shoppingCartDataTableBean.setSpldealStatus(Constants.YES);
						} else {
							shoppingCartDataTableBean.setSpldealStatus(Constants.NO);
						}
						if(shoppingCartDetails.getLoyaltsPointencahsed() != null)
							shoppingCartDataTableBean.setLoyaltsPointencahsed(shoppingCartDetails.getLoyaltsPointencahsed());

						shoppingCartDataTableBean.setSelectedrecord(Boolean.FALSE);
						shoppingCartDataTableBean.setDeleteStatus(Boolean.TRUE);

						shoppingCartDataTableBean.setBooReportEligible(true);

					
						shoppingcartDTList.add(shoppingCartDataTableBean);

					}
				}
			} catch (Exception e) {
				setExceptionMessage(e.getMessage());
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
			}
		}

	
	public void assignNullValues() {
		// clearing beneficiary lists
		if(coustomerBeneficaryDTList != null || !coustomerBeneficaryDTList.isEmpty()){
			coustomerBeneficaryDTList.clear();
		}
		setDatabenificaryname(null);
		setBenificiaryryNameRemittance(null);
		setRoutingCountry(null);
		setDataroutingcountryalphacode(null);
		setRoutingBank(null);
		setRemitMode(null);
		setDeliveryMode(null);
		setRoutingBranch(null);

		setCurrency(null);
		setAmountToRemit(null);
		setSpotRate(null);
		setAvailLoyaltyPoints(null);
		setChargesOverseas(null);
		setCashRounding(null);
		setBenificiaryryNameRemittance(null);
		setExchangeRate(null);
		setDataAccountnum(null);
		setOverseasamt(null);
		setDatabenificarycurrencyname(null);
		setCommission(null);
		setDatabenificarycountryname(null);
		setGrossAmountCalculated(null);
		setDatabenificarybankname(null);
		setDatabenificarybankalphacode(null);
		setLoyaltyAmountAvailed(null);
		setDatabenificarybranchname(null);
		setNetAmountPayable(null);
		setNetAmountSent(null);
		setIcashCostRate(null);
		setFurthuerInstructions(null);
		setSourceOfIncome(null);
		
		// clearing bank bene id 
		setBeneStateId(null);
		setBeneDistrictId(null);
		setBeneCityId(null);
		setPbeneFullName(null);
		setPbeneFirstName(null);
		setPbeneSecondName(null);
		setPbeneThirdName(null);
		setPbeneFourthName(null);
		setPbeneFifthName(null);
		
		//Clear debit card number
		setDebitCard1(null);
		setDebitCard2(null);
		setDebitCard3(null);
		setDebitCard4(null);
		
		
	}
	
	public void exit(){
		setVisible(false);
		assignNullValues();
		clear();
		setBeneCountr(null);
		setAmountToRemit(null);
		setCalGrossAmount(BigDecimal.ZERO);
		setCalNetAmountPaid(BigDecimal.ZERO);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../login/onlineHome.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
	}
	}
	
	
	public void home() throws IOException {
		log.info("Entering into home method");
		FacesContext.getCurrentInstance().getExternalContext().redirect("../login/onlineHome.xhtml");
		return;
	}
	
	
	public void bankBranchByBankView() throws AMGException{
		try{
		if (getRoutingBank() != null) {
			List<PopulateData> lstRoutingBankBranch = iPersonalRemittanceService.getRoutingBranchList(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),
					getDataBankbenificarycountry(), getDatabenificarycurrency(), getDataserviceid(), getRoutingCountry(), getRoutingBank(), getRemitMode(), getDeliveryMode());
			if (lstRoutingBankBranch.size() == 0) {
				setRoutingBranchName(null);
				setRoutingBranch(null);
			} else if (lstRoutingBankBranch.size() == 1) {
				setRoutingBranch(lstRoutingBankBranch.get(0).getPopulateId());
				setRoutingBranchName(lstRoutingBankBranch.get(0).getPopulateName());
			} else {
				setRoutingBranchName(null);
				setRoutingBranch(null);
				setRoutingBankBranchlst(lstRoutingBankBranch);
			}
		}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public void deliverylistByRemitId() throws AMGException{
		try{
		List<PopulateData> lstDeliveryView = iPersonalRemittanceService.getDeliveryListByCountryBankRemit(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),
				getDataBankbenificarycountry(), getDatabenificarycurrency(), getDataserviceid(), getRoutingCountry(), getRoutingBank(), getRemitMode(),
				new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
		if (lstDeliveryView.size() == 0) {
			setDeliveryModeInput(null);
			setDeliveryMode(null);
			RequestContext.getCurrentInstance().execute("DeliveryNoData.show();");
			return;
		} else if (lstDeliveryView.size() == 1) {
			setDeliveryModeInput(lstDeliveryView.get(0).getPopulateName());
			setDeliveryMode(lstDeliveryView.get(0).getPopulateId());
			bankBranchByBankView();
		} else {
			setDeliveryModeInput(null);
			setDeliveryMode(null);
			setLstofDelivery(lstDeliveryView);
		}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public void remittancelistByBankId() throws AMGException{
		try{
		// spl pool based on routing country , routing bank
		List<PopulateData> lstRemitView = iPersonalRemittanceService.getRemittanceListByCountryBank(sessionStateManage.getCountryId(), getBeneficaryBankId(), getBeneficaryBankBranchId(),
				getDataBankbenificarycountry(), getDatabenificarycurrency(), getDataserviceid(), getRoutingCountry(), getRoutingBank(), new BigDecimal(
						sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
		if (lstRemitView.size() == 0) {
			setRemittanceName(null);
			setRemitMode(null);
			// pushing Exception Main to menthod 
			throw new AMGException(props.getString("lbl.remittanceNameNotAvailble"));
		} else if (lstRemitView.size() == 1) {
			setRemittanceName(lstRemitView.get(0).getPopulateName());
			setRemitMode(lstRemitView.get(0).getPopulateId());
			deliverylistByRemitId();
		} else {
			setRemittanceName(null);
			setRemitMode(null);
			setLstofRemittance(lstRemitView);
		}
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}
	}

	public void getCustomerDetails() {
		sessionStateManage = new SessionStateManage();
		userProfile = (UserProfile) sessionStateManage.getSessionValueAsObject("userProfile");
		setCustomerName(userProfile.getEnglishName());
		setCustomerNo(userProfile.getCustomerId());
		log.info("userProfile.getCustomerReference() :"+userProfile.getCustomerReference());
		setCustomerrefno(userProfile.getCustomerReference());
		setFirstName(userProfile.getFirstName());
		setSecondName(userProfile.getMiddleName());
		setThirdName(userProfile.getLastName());
		setCustomerFullName(userProfile.getEnglishName());
		setCustomerLocalFullName(userProfile.getLocalFullName());
		setCustomerIsActive(userProfile.getStatus());
		setCustomerIdExpDate(userProfile.getIdExpirtyDate());
		String customerTypeString = iPersonalRemittanceService.getCustomerType(userProfile.getCustomerTypeId());
		log.info("customerTypeString :" + customerTypeString);
		if (customerTypeString != null) {
			setCustomerType(customerTypeString);
		}
		setNationality(userProfile.getNatinality());
		log.info("languageId :" + languageId + "\t userProfile.getNatinality() :" + userProfile.getNatinality());
		String natiName = generalService.getNationalityName(languageId, userProfile.getNatinality());
		setNationalityName(natiName == null ? "" : natiName);
		setDateOfBrithStr(userProfile.getDateOfBirth());
		 
		setCustomerEmail(userProfile.getEmailID());
		setCustomerMobile(userProfile.getMobileNo());
		setLoyaltyPoints(userProfile.getLoyaltyPoints());
		sessionStateManage.setSessionValue("customerId", getCustomerNo().toString());
	}

	
	// credit card checking
		public void checkingCardNumberwithDB(){
			try{
			lstofDBCards.clear();
			if(getDebitCard1()!= null && getDebitCard2() != null && getDebitCard3() != null && getDebitCard4()!= null){

				if(getDebitCard1().length() == 4 && getDebitCard2().length() == 4 && getDebitCard3().length() == 4 && getDebitCard4().length() == 4 ){
					log.info(getDebitCard1()+getDebitCard2()+getDebitCard3()+getDebitCard4());
					String cardnumber = getDebitCard1()+getDebitCard2()+getDebitCard3()+getDebitCard4();

					List<CustomerDBCardDetailsView> lstcardbankdetails = generalService.customerBanksView(getCustomerNo(),cardnumber);
					
					
					if(lstcardbankdetails!=null && lstcardbankdetails.size() != 0){
						int i = 0;
						for (CustomerDBCardDetailsView lstDebitcrd : lstcardbankdetails) {
							if(lstDebitcrd.getDebitFullCard().equalsIgnoreCase(cardnumber)){
								i = 1;
								break;
							}else{
								i = 0;
							}
						}

						if(i==1){
							setCardErrorMsg(null);
							setBooRendercardErrorMsg(false);
							makePayment();
						}else{
							setCardErrorMsg(props.getString("lbl.cardDisErrormsg"));
							setLstofDBCards(lstcardbankdetails);
							setBooRendercardErrorMsg(true);
						}
					}
				} 
			 } 
			}catch(NullPointerException nulMsg){
				setExceptionMessage(nulMsg.getMessage());
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
			}catch(Exception errMsg){
				setExceptionMessage(errMsg.getMessage());
				RequestContext.getCurrentInstance().execute("alertmsg.show();");
			}
			
		}
	
	
		public void checkFurtherSwiftBankDetails(String act) {
			checkProExp = false;
			saveCount = 0;
		
			if(isAdditionalCheck()){
				setExceptionMessage(null);
				String furtherInstrn = checkingFurtherInstructionWithProcedure();
				log.info("furtherInstrn :"+furtherInstrn+" exception Message :"+getExceptionMessage());

				if (furtherInstrn != null && furtherInstrn.length()>0) {
					setExceptionMessage(furtherInstrn);
					log.info("if booAmlCheck furtherInstrn :"+furtherInstrn+" exception Message :"+getExceptionMessage()); 
					RequestContext.getCurrentInstance().execute("sqlexception.show();");
					return;
				} else {
					setBtnRender(true);
					saveApplicationTransaction(act);
					
					log.info("else  booAmlCheck furtherInstrn :checkingSwiftCodeWithProcedure exception Message :"+getExceptionMessage());
					
					/*HashMap<String, String> beneSwiftBank1Details = checkingSwiftCodeWithProcedure(getBeneSwiftBank1(), Constants.BENEFICIARY_SWIFT_BANK1);

					if (beneSwiftBank1Details != null) {
						if (beneSwiftBank1Details.get("P_ERROR_MESSAGE") != null && !beneSwiftBank1Details.get("P_ERROR_MESSAGE").equalsIgnoreCase("")) {

							log.info("if (beneSwiftBank1Details  booAmlCheck furtherInstrn :checkingSwiftCodeWithProcedure exception Message :"+getExceptionMessage()); 
							setExceptionMessage(beneSwiftBank1Details.get("P_ERROR_MESSAGE"));
							RequestContext.getCurrentInstance().execute("sqlexception.show();");
							return;
						} else {

							HashMap<String, String> beneSwiftBank2Details = checkingSwiftCodeWithProcedure(getBeneSwiftBank2(),Constants.BENEFICIARY_SWIFT_BANK2);
							if (beneSwiftBank2Details != null) {
								if (beneSwiftBank2Details.get("P_ERROR_MESSAGE") != null && !beneSwiftBank2Details.get("P_ERROR_MESSAGE").equalsIgnoreCase("")) {

									log.info("if (beneSwiftBank2Details.  booAmlCheck furtherInstrn :checkingSwiftCodeWithProcedure exception Message :"+getExceptionMessage()); 
									setExceptionMessage(beneSwiftBank2Details.get("P_ERROR_MESSAGE"));
									RequestContext.getCurrentInstance().execute("sqlexception.show();");
									return;
								} else {
									// save application
									saveApplicationTransaction(act);
								}
							}
						}
					}*/
				}
			}else{
				if(getExceptionMessage() != null  && !getExceptionMessage().equalsIgnoreCase("")){
					setExceptionMessage(getExceptionMessage());
					RequestContext.getCurrentInstance().execute("dataexception.show();");
				}else{
					RequestContext.getCurrentInstance().execute("additionaldatanot.show();");
				}
			}
		}
		
		public HashMap<String, String> checkingSwiftCodeWithProcedure(String beneSwiftBank, String fieldName) {
			HashMap<String, String> swiftBankDetails = null;
			try {

				swiftBankDetails = iPersonalRemittanceService.toFtechSwiftBankProcedure(sessionStateManage.getCountryId(),
						getRoutingCountry(), getForiegnCurrency(), // new BigDecimal(sessionmanage.getCurrencyId()) //CurrencyID
						getRemitMode(), getDeliveryMode(), fieldName, // fieldName
						beneSwiftBank,getDataBankbenificarycountry());

			} catch (AMGException e) {
				setExceptionMessage(e.getMessage());
				RequestContext.getCurrentInstance().execute("sqlexception.show();");
			}

			return swiftBankDetails;
		}
		
		
		
		
		
		public String checkingFurtherInstructionWithProcedure() {
			String furtherInstrMsg = null;
			setFurthuerInstructions("URGENT");
			try {
				furtherInstrMsg = iPersonalRemittanceService.toFtechPurtherInstractionErrorMessaage(sessionStateManage.getCountryId(), getRoutingCountry(),getRoutingBank(),
						getForiegnCurrency(), // new BigDecimal(sessionmanage.getCurrencyId()) , //CurrencyID
						getRemitMode(), getDeliveryMode(),
						getFurthuerInstructions(),getDataBankbenificarycountry());
			} catch (AMGException e) {
				setExceptionMessage(e.getMessage());
				RequestContext.getCurrentInstance().execute("sqlexception.show();");
			}catch (Exception e) {
				setExceptionMessage(e.getMessage());
				RequestContext.getCurrentInstance().execute("sqlexception.show();");
			}

			return furtherInstrMsg;
		}

	
	
	// saving data to Remittance App Benificary Table
	public RemittanceAppBenificiary saveRemittanceAppBenificary(RemittanceApplication remittanceApplication)throws AMGException {
		
		try {
			RemittanceAppBenificiary remittanceAppBenificary = new RemittanceAppBenificiary();
			// to get the document details - Hard Code
			//List<Document> lstDocument = generalService.getDocument(new BigDecimal(Constants.DOCUMENT_CODE_FOR_FCSALE_REMITTANCE_APPLICATION), new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
			List<Document> lstDocument = generalService.getDocument(new BigDecimal(Constants.DOCUMENT_CODE_FOR_FCSALE_REMITTANCE_APPLICATION), new BigDecimal("1"));
			if (lstDocument.size() > 0) {
				for (Document lstdoc : lstDocument) {
					setDocumentId(lstdoc.getDocumentID());
					setDocumentdesc(lstdoc.getDocumentDesc());
					setDocumentcode(lstdoc.getDocumentCode());
				}
			}
			// Document Id
			Document documentid = new Document();
			documentid.setDocumentID(getDocumentId());
			remittanceAppBenificary.setExDocument(documentid);
			// company Id
			CompanyMaster companymaster = new CompanyMaster();
			companymaster.setCompanyId(sessionStateManage.getCompanyId());
			remittanceAppBenificary.setFsCompanyMaster(companymaster);

			// company code
			//List<CompanyMasterDesc> lstcompanymaster = generalService.viewById(sessionStateManage.getCompanyId(),new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"));
			
			List<CompanyMasterDesc> lstcompanymaster = generalService.viewById(sessionStateManage.getCompanyId(),new BigDecimal("1"));
			
			if (lstcompanymaster.size() != 0) {
				CompanyMasterDesc companycode = lstcompanymaster.get(0);
				remittanceAppBenificary.setCompanyCode(companycode.getFsCompanyMaster().getCompanyCode());
			}

			// User Financial Year for Transaction
			UserFinancialYear userfinancialyear = new UserFinancialYear();
			userfinancialyear.setFinancialYearID(generalService.getDealYear(new Date()).get(0).getFinancialYearID());
			remittanceAppBenificary.setExUserFinancialYear(userfinancialyear);
			// RemittanceApplication Id
			remittanceAppBenificary.setExRemittanceAppfromBenfi(remittanceApplication);
			remittanceAppBenificary.setDocumentCode(getDocumentcode());
			remittanceAppBenificary.setDocumentNo(remittanceApplication.getDocumentNo());
			remittanceAppBenificary.setBeneficiaryId(getMasterId());

			if (getDataAccountnum() != null) {
				remittanceAppBenificary.setBeneficiaryAccountNo(getDataAccountnum().toString());
			}
			
			if (getBeneSwiftBank1() != null) {
				// Beneficiary Bank Swift Code - V_EX_SWIFT_MASTER - SWIFT_BIC
				remittanceAppBenificary.setBeneficiarySwiftBank1(getBeneSwiftBank1());
				List<SwiftMasterView> lstSwiftRecords = iPersonalRemittanceService.fetchingViewSwiftMasterBySwiftBIC(getBeneSwiftBank1());
				if(lstSwiftRecords != null && lstSwiftRecords.size() != 0){
					SwiftMasterView swiftDt = lstSwiftRecords.get(0);
					
					remittanceAppBenificary.setBeneficiarySwiftBank1Id(swiftDt.getSwiftId());
					
					setBeneSwiftBankAddr1(swiftDt.getBankName() == null ? "" : swiftDt.getBankName());
					
					if (getBeneSwiftBankAddr1() != null) {
						remittanceAppBenificary.setBeneficiarySwiftAddr1(getBeneSwiftBankAddr1());
					}
				}
			}

			/*if (getBeneSwiftBank1() != null) {
				remittanceAppBenificary.setBeneficiarySwiftBank1(null);
				remittanceAppBenificary.setBeneficiarySwiftBank1Id(new BigDecimal(getBeneSwiftBank1()));

			}
			if (getBeneSwiftBank2() != null) {
				remittanceAppBenificary.setBeneficiarySwiftBank2(null);
				remittanceAppBenificary.setBeneficiarySwiftBank2Id(new BigDecimal(getBeneSwiftBank2()));
			}

			if (getBeneSwiftBankAddr1() != null) {
				remittanceAppBenificary.setBeneficiarySwiftAddr1(getBeneSwiftBankAddr1());
			}

			if (getBeneSwiftBankAddr2() != null) {
				remittanceAppBenificary.setBeneficiarySwiftAddr2(getBeneSwiftBankAddr2());
			}*/
			
			if(getSwiftBic()!=null){
				remittanceAppBenificary.setBeneficiaryBankSwift(getSwiftBic());
			}

			remittanceAppBenificary.setCreatedBy(Constants.CREATOR);
			remittanceAppBenificary.setCreatedDate(new Date());
			remittanceAppBenificary.setIsactive(Constants.Yes);
			// to get Beneficary status from Benificary Master
			if (getPbeneFullName() != null) {
				remittanceAppBenificary.setBeneficiaryName(getPbeneFullName());
			}

			if (getPbeneFirstName() != null) {
				remittanceAppBenificary.setBeneficiaryFirstName(getPbeneFirstName());
			}

			if (getPbeneSecondName() != null) {
				remittanceAppBenificary.setBeneficiarySecondName(getPbeneSecondName());
			}

			if (getPbeneThirdName() != null) {
				remittanceAppBenificary.setBeneficiaryThirdName(getPbeneThirdName());
			}

			if (getPbeneFourthName() != null) {
				remittanceAppBenificary.setBeneficiaryFourthName(getPbeneFourthName());
			}

			if (getPbeneFifthName() != null) {
				remittanceAppBenificary.setBeneficiaryFifthName(getPbeneFifthName());
			}

			remittanceAppBenificary.setBeneficiaryBankCountryId(getDataBankbenificarycountry());
			remittanceAppBenificary.setBeneficiaryBank(getDatabenificarybankname());
			remittanceAppBenificary.setBeneficiaryBranch(getDatabenificarybranchname());
			remittanceAppBenificary.setBeneficiaryBankId(getBeneficaryBankId());
			remittanceAppBenificary.setBeneficiaryBankBranchId(getBeneficaryBankBranchId());
			remittanceAppBenificary.setBeneficiaryBranchStateId(getBeneStateId());
			remittanceAppBenificary.setBeneficiaryBranchDistrictId(getBeneDistrictId());
			remittanceAppBenificary.setBeneficiaryBranchCityId(getBeneCityId());
			remittanceAppBenificary.setBeneficiaryAccountSeqId(getBeneficiaryAccountSeqId());
			remittanceAppBenificary.setBeneficiaryRelationShipSeqId(getBeneficiaryRelationShipSeqId());
			remittanceAppBenificary.setBeneficiaryTelephoneNumber(getBenificaryTelephone());

			
			return remittanceAppBenificary;
			
			
		} catch (NullPointerException NulExp) {
			throw new AMGException(NulExp.getMessage());
		}catch (Exception exp) {
			throw new AMGException(exp.getMessage());
		}

		
	}
	

	/**
	 * @return
	 */
	public void getBeneCountryListView() {
	}
	
	public void beneFicialryListPageNavigation(){
		if (coustomerBeneficaryDTList != null || !coustomerBeneficaryDTList.isEmpty()) {
			coustomerBeneficaryDTList.clear();
		}
		try {
			//populateBeneficiaryListDetailsFromBeneRelation();
			populateBeneficiaryListDetailsFromBeneRelationFromOnlineView();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../beneficiary/beneficiaryList.xhtml");
		} catch(NullPointerException ne){
			log.error("ne.getMessage():::::::::::::::::::::::::::::::"+ne.getMessage());
			setErrorMessage("MethodName::beneFicialryListPageNavigation()");
			RequestContext.getCurrentInstance().execute("nullPointerId.show();");
			return; 
		}catch(Exception exception){
			log.error("exception.getMessage():::::::::::::::::::::::::::::::"+exception.getMessage());
			setErrorMessage(exception.getMessage()); 
			RequestContext.getCurrentInstance().execute("exception.show();");
			return;       
		}
		
	}
	
	public void populateBeneficiaryListDetailsFromBeneRelation(){
		sessionStateManage = new SessionStateManage();
		userProfile = (UserProfile) sessionStateManage.getSessionValueAsObject("userProfile");
		setCustomerNo(userProfile.getCustomerId());
		log.info("Entering into populateBeneficiaryListDetailsFromBeneRelation  method "+getCustomerNo());
		try {
			if (coustomerBeneficaryDTList != null || !coustomerBeneficaryDTList.isEmpty()) {
				coustomerBeneficaryDTList.clear();
			}
			List<BenificiaryListView> isCoustomerExist = iPersonalRemittanceService.getBeneficaryList(getCustomerNo(), getCompanyId(), getApplicationCountryId());
				
			log.info("=====================" + isCoustomerExist.size());
			if (isCoustomerExist.size() > 0) {
				for (int i = 0; i < isCoustomerExist.size(); i++) {
					BenificiaryListView rel = new BenificiaryListView();
					rel = isCoustomerExist.get(i);
					PersonalRemmitanceBeneficaryDataTable personalRBDataTable = new PersonalRemmitanceBeneficaryDataTable();
					personalRBDataTable.setAccountStatus(rel.getAccountStatus());
					personalRBDataTable.setAge(rel.getAge());
					personalRBDataTable.setApplicationCountryId(rel.getApplicationCountryId());
					personalRBDataTable.setArbenificaryName(rel.getArbenificaryName());
					personalRBDataTable.setBankAccountNumber(rel.getBankAccountNumber());
					personalRBDataTable.setBankBranchName(rel.getBankBranchName());
					personalRBDataTable.setBankCode(rel.getBankCode());
					personalRBDataTable.setBankId(rel.getBankId());
					personalRBDataTable.setBankName(rel.getBankName());
					personalRBDataTable.setBeneficaryMasterSeqId(rel.getBeneficaryMasterSeqId());
					personalRBDataTable.setBeneficiaryAccountSeqId(rel.getBeneficiaryAccountSeqId());
					personalRBDataTable.setBeneficiaryRelationShipSeqId(rel.getBeneficiaryRelationShipSeqId());
					personalRBDataTable.setBenificaryCountry(rel.getCountryId());
					personalRBDataTable.setBenificaryCountryName(rel.getCountryName());
					personalRBDataTable.setBenificaryName(rel.getBenificaryName());
					personalRBDataTable.setBenificaryStatusId(rel.getBenificaryStatusId());
					personalRBDataTable.setBenificaryStatusName(rel.getBenificaryStatusName());
					personalRBDataTable.setBooDisabled(rel.getBankAccountNumber() != null ? true : false);
					personalRBDataTable.setBranchCode(rel.getBranchCode());
					personalRBDataTable.setBranchId(rel.getBranchId());
					personalRBDataTable.setCityName(rel.getCityName());
					personalRBDataTable.setCountryId(rel.getBenificaryCountry());
					personalRBDataTable.setCountryName(rel.getBenificaryBankCountryName());
					personalRBDataTable.setCreatedBy(rel.getCreatedBy());
					personalRBDataTable.setCreatedDate(rel.getCreatedDate());
					personalRBDataTable.setCurrencyId(rel.getCurrencyId());
					personalRBDataTable.setCurrencyName(rel.getCurrencyName());
					personalRBDataTable.setCurrencyQuoteName(rel.getCurrencyQuoteName() == null ? "" : rel.getCurrencyQuoteName());
					personalRBDataTable.setCustomerId(rel.getCustomerId());
					personalRBDataTable.setDateOfBirth(rel.getDateOfBirth());
					personalRBDataTable.setDistrictName(rel.getDistrictName());
					personalRBDataTable.setFiftheName(rel.getFiftheName());
					personalRBDataTable.setFifthNameLocal(rel.getFifthNameLocal());
					personalRBDataTable.setFirstName(rel.getFirstName());
					personalRBDataTable.setFirstNameLocal(rel.getFirstNameLocal());
					personalRBDataTable.setFourthName(rel.getFourthName());
					personalRBDataTable.setFourthNameLocal(rel.getFourthNameLocal());
					personalRBDataTable.setIsActive(rel.getIsActive());
					personalRBDataTable.setLocation(rel.getNationalityName());
					personalRBDataTable.setModifiedBy(rel.getModifiedBy());
					personalRBDataTable.setModifiedDate(rel.getModifiedDate());
					personalRBDataTable.setNationality(rel.getNationality());
					personalRBDataTable.setNationalityName(rel.getNationalityName());
					personalRBDataTable.setNoOfRemittance(rel.getNoOfRemittance());
					personalRBDataTable.setOccupation(rel.getOccupation());
					personalRBDataTable.setRelationShipId(rel.getRelationShipId());
					personalRBDataTable.setRelationShipName(rel.getRelationShipName());
					personalRBDataTable.setRemarks(rel.getRemarks());
					personalRBDataTable.setSecondNameLocal(rel.getSecondNameLocal());
					personalRBDataTable.setSecondName(rel.getSecondName());
					personalRBDataTable.setServiceGroupCode(rel.getServiceGroupCode());
					personalRBDataTable.setServiceGroupId(rel.getServiceGroupId());
					personalRBDataTable.setServiceProvider(rel.getServiceProvider());
					personalRBDataTable.setStateName(rel.getStateName());
					personalRBDataTable.setMapSequenceId(rel.getMapSequenceId());
					
					/** Commented by Rabil due to aabic is not there in DB  on mar 09 03 2017*/
					//List<ServiceGroupMasterDesc> lstServiceGroupMasterDesc = generalService.LocalServiceGroupDescription(new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"), rel.getServiceGroupId());
					
					List<ServiceGroupMasterDesc> lstServiceGroupMasterDesc = generalService.LocalServiceGroupDescription(new BigDecimal("1"), rel.getServiceGroupId());
					
					if (lstServiceGroupMasterDesc.size() > 0) {
						personalRBDataTable.setServiceGroupName(lstServiceGroupMasterDesc.get(0).getServiceGroupDesc());
					}
					List<BeneficaryContact> telePhone = beneficaryCreation.getTelephoneDetails(rel.getBeneficaryMasterSeqId());
					if (telePhone != null && telePhone.size() != 0) {
						String telNumber = null;
						if (telePhone.size() == 1) {
							if (telePhone.get(0).getTelephoneNumber() != null) {
								telNumber = telePhone.get(0).getTelephoneNumber();
							} else if (telePhone.get(0).getMobileNumber() != null) {
								telNumber = telePhone.get(0).getMobileNumber().toPlainString();
							} else {
								telNumber = null;
							}
							personalRBDataTable.setTelphoneNum(telNumber);
							personalRBDataTable.setTelphoneCode(telePhone.get(0).getCountryTelCode());
							personalRBDataTable.setBeneficiaryContactSeqId(telePhone.get(0).getBeneficaryTelephoneSeqId());
						} else {
							BeneficaryContact beneficaryContact = telePhone.get(0);
							if (beneficaryContact.getTelephoneNumber() != null) {
								telNumber = beneficaryContact.getTelephoneNumber();
							} else if (beneficaryContact.getMobileNumber() != null) {
								telNumber = beneficaryContact.getMobileNumber().toPlainString();
							} else {
								telNumber = null;
							}
							personalRBDataTable.setTelphoneNum(telNumber);
							personalRBDataTable.setTelphoneCode(beneficaryContact.getCountryTelCode());
							personalRBDataTable.setBeneficiaryContactSeqId(beneficaryContact.getBeneficaryTelephoneSeqId());
						}
					}
					
					
					
					personalRBDataTable.setThirdName(rel.getThirdName());
					personalRBDataTable.setThirdNameLocal(rel.getThirdNameLocal());
					personalRBDataTable.setYearOfBirth(rel.getYearOfBirth());
					
					coustomerBeneficaryDTList.add(personalRBDataTable);
					
					
				}
			}
		} catch (Exception e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
		}
		log.info("Exit into populateCustomerDetailsFromBeneRelation method ");
	
	}
	
	
	
	public void populateBeneficiaryListDetailsFromBeneRelationFromOnlineView(){
		sessionStateManage = new SessionStateManage();
		userProfile = (UserProfile) sessionStateManage.getSessionValueAsObject("userProfile");
		setCustomerNo(userProfile.getCustomerId());
		log.info("Entering into populateBeneficiaryListDetailsFromBeneRelationFromOnlineView  method "+getCustomerNo());
		try {
			if (coustomerBeneficaryDTList != null || !coustomerBeneficaryDTList.isEmpty()) {
				coustomerBeneficaryDTList.clear();
				coustomerBeneficaryListForQuick.clear();
			}
			
			List<BenificiaryListViewForOnline> isCoustomerExist = iPersonalRemittanceService.getBeneficaryListForOnline(getCustomerNo(), getCompanyId(), getApplicationCountryId());
				
			log.info("=====================" + isCoustomerExist.size());
			if (isCoustomerExist.size() > 0) {
				for (int i = 0; i < isCoustomerExist.size(); i++) {
					BenificiaryListViewForOnline rel = new BenificiaryListViewForOnline();
					rel = isCoustomerExist.get(i);
					PersonalRemmitanceBeneficaryDataTable personalRBDataTable = new PersonalRemmitanceBeneficaryDataTable();
					//personalRBDataTable.setAccountStatus(rel.getAccountStatus());
					//personalRBDataTable.setAge(rel.getAge());
					personalRBDataTable.setApplicationCountryId(rel.getApplicationCountryId());
					//personalRBDataTable.setArbenificaryName(rel.getArbenificaryName());
					personalRBDataTable.setBankAccountNumber(rel.getBankAccountNumber());
					personalRBDataTable.setBankBranchName(rel.getBankBranchName());
					//personalRBDataTable.setBankCode(rel.ge);
					personalRBDataTable.setBankId(rel.getBankId());
					personalRBDataTable.setBankName(rel.getBeneficiaryBankName());
					//personalRBDataTable.setBeneficaryMasterSeqId(rel.getBeneficaryMasterSeqId());
					//personalRBDataTable.setBeneficiaryAccountSeqId(rel.getBeneficiaryAccountSeqId());
					//personalRBDataTable.setBeneficiaryRelationShipSeqId(rel.getBeneficiaryRelationShipSeqId());
					//personalRBDataTable.setBenificaryCountry(rel.getCountryId());
					personalRBDataTable.setBenificaryCountryName(rel.getBenificaryBankCountryName());
					personalRBDataTable.setBenificaryName(rel.getBeneName());
					//personalRBDataTable.setBenificaryStatusId(rel.getBenificaryStatusId());
					//personalRBDataTable.setBenificaryStatusName(rel.getBenificaryStatusName());
					//personalRBDataTable.setBooDisabled(rel.getBankAccountNumber() != null ? true : false);
					//personalRBDataTable.setBranchCode(rel.getBranchCode());
					//personalRBDataTable.setBranchId(rel.getBranchId());
					//personalRBDataTable.setCityName(rel.getCityName());
					personalRBDataTable.setBenificaryCountry(rel.getBenificaryCountry());
					personalRBDataTable.setCountryName(rel.getBenificaryBankCountryName());
					//personalRBDataTable.setCreatedBy(rel.getCreatedBy());
					//personalRBDataTable.setCreatedDate(rel.getCreatedDate());
					personalRBDataTable.setCurrencyId(rel.getCurrencyId());
					//personalRBDataTable.setCurrencyName(rel.getCurrencyName());
					personalRBDataTable.setCurrencyQuoteName(rel.getCurrencyQuoteName() == null ? "" : rel.getCurrencyQuoteName());
					personalRBDataTable.setCustomerId(rel.getCustomerId());
					//personalRBDataTable.setDateOfBirth(rel.getDateOfBirth());
					//personalRBDataTable.setDistrictName(rel.getDistrictName());
					//personalRBDataTable.setFiftheName(rel.getFiftheName());
					//personalRBDataTable.setFifthNameLocal(rel.getFifthNameLocal());
					//personalRBDataTable.setFirstName(rel.getFirstName());
					//personalRBDataTable.setFirstNameLocal(rel.getFirstNameLocal());
					//personalRBDataTable.setFourthName(rel.getFourthName());
					//personalRBDataTable.setFourthNameLocal(rel.getFourthNameLocal());
					//personalRBDataTable.setIsActive(rel.getIsActive());
					//personalRBDataTable.setLocation(rel.getNationalityName());
					//personalRBDataTable.setModifiedBy(rel.getModifiedBy());
					//personalRBDataTable.setModifiedDate(rel.getModifiedDate());
					//personalRBDataTable.setNationality(rel.getNationality());
					//personalRBDataTable.setNationalityName(rel.getNationalityName());
					//personalRBDataTable.setNoOfRemittance(rel.getNoOfRemittance());
					//personalRBDataTable.setOccupation(rel.getOccupation());
					//personalRBDataTable.setRelationShipId(rel.getRelationShipId());
					//personalRBDataTable.setRelationShipName(rel.getRelationShipName());
					//personalRBDataTable.setRemarks(rel.getRemarks());
					//personalRBDataTable.setSecondNameLocal(rel.getSecondNameLocal());
					//personalRBDataTable.setSecondName(rel.getSecondName());
					//personalRBDataTable.setServiceGroupCode(rel.getServiceGroupCode());
					//personalRBDataTable.setServiceGroupId(rel.getServiceGroupId());
					//personalRBDataTable.setServiceProvider(rel.getServiceProvider());
					//personalRBDataTable.setStateName(rel.getStateName());
					//personalRBDataTable.setMapSequenceId(rel.getMapSequenceId());
					
					
					
					/*
					 List<ServiceGroupMasterDesc> lstServiceGroupMasterDesc = generalService.LocalServiceGroupDescription(
							new BigDecimal(sessionStateManage.isExists("languageId") ? sessionStateManage.getSessionValue("languageId") : "1"), rel.getServiceGroupId());
						
						*/	
					List<ServiceGroupMasterDesc> lstServiceGroupMasterDesc = generalService.LocalServiceGroupDescription(new BigDecimal("1"), rel.getServiceGroupId());
						
					
					
					if (lstServiceGroupMasterDesc.size() > 0) {
						personalRBDataTable.setServiceGroupName(lstServiceGroupMasterDesc.get(0).getServiceGroupDesc());
					}
					/*List<BeneficaryContact> telePhone = beneficaryCreation.getTelephoneDetails(rel.getBeneficaryMasterSeqId());
					if (telePhone != null && telePhone.size() != 0) {
						String telNumber = null;
						if (telePhone.size() == 1) {
							if (telePhone.get(0).getTelephoneNumber() != null) {
								telNumber = telePhone.get(0).getTelephoneNumber();
							} else if (telePhone.get(0).getMobileNumber() != null) {
								telNumber = telePhone.get(0).getMobileNumber().toPlainString();
							} else {
								telNumber = null;
							}
							personalRBDataTable.setTelphoneNum(telNumber);
							personalRBDataTable.setTelphoneCode(telePhone.get(0).getCountryTelCode());
							personalRBDataTable.setBeneficiaryContactSeqId(telePhone.get(0).getBeneficaryTelephoneSeqId());
						} else {
							BeneficaryContact beneficaryContact = telePhone.get(0);
							if (beneficaryContact.getTelephoneNumber() != null) {
								telNumber = beneficaryContact.getTelephoneNumber();
							} else if (beneficaryContact.getMobileNumber() != null) {
								telNumber = beneficaryContact.getMobileNumber().toPlainString();
							} else {
								telNumber = null;
							}
							personalRBDataTable.setTelphoneNum(telNumber);
							personalRBDataTable.setTelphoneCode(beneficaryContact.getCountryTelCode());
							personalRBDataTable.setBeneficiaryContactSeqId(beneficaryContact.getBeneficaryTelephoneSeqId());
						}
					}*/
					
					
					
					//personalRBDataTable.setThirdName(rel.getThirdName());
					//personalRBDataTable.setThirdNameLocal(rel.getThirdNameLocal());
					//personalRBDataTable.setYearOfBirth(rel.getYearOfBirth());
					coustomerBeneficaryListForQuick.add(personalRBDataTable);
					coustomerBeneficaryDTList.add(personalRBDataTable);
					
					
				}
			}
		} catch (Exception e) {
			setExceptionMessage(e.getMessage());
			RequestContext.getCurrentInstance().execute("alertmsg.show();");
		}
		log.info("Exit into populateCustomerDetailsFromBeneRelation method ");
	
	}
	
	
	
	public void getSelectedRecordstoRemittance(ShoppingCartDataTableBean shoppingCartDataTableBean) {
		
		
		setColCurrency(generalService.getCurrencyName(new BigDecimal(sessionStateManage.getCurrencyId())));

		List<ShoppingCartDataTableBean> lstOnlyselectedrec = new ArrayList<ShoppingCartDataTableBean>();
		lstOnlyselectedrec.clear();
		lstApplDocNumber.clear();
		lstselectedrecord.clear();
		tempCalGrossAmount = BigDecimal.ZERO;
		tempCalNetAmountPaid = BigDecimal.ZERO;
		setCalNetAmountPaid(null);

		BigDecimal custPayAmt = BigDecimal.ZERO;
		for (ShoppingCartDataTableBean lstRec : shoppingcartDTList) {

			if (lstRec.getSelectedrecord()) {
				if (lstRec.getApplicationType().equalsIgnoreCase(Constants.Remittance)) {
					remittanceNo = remittanceNo.add(new BigDecimal(1));
				}
				tempCalGrossAmount = tempCalGrossAmount.add(lstRec.getLocalTranxAmount() == null ? BigDecimal.ZERO : lstRec.getLocalTranxAmount());
				setCalGrossAmount(tempCalGrossAmount);
				tempCalNetAmountPaid = tempCalNetAmountPaid.add(lstRec.getLocalNextTranxAmount() == null ? BigDecimal.ZERO : lstRec.getLocalNextTranxAmount());
				setCalNetAmountPaid(tempCalNetAmountPaid);
				lstRec.setDeleteStatus(Boolean.FALSE);
				lstOnlyselectedrec.add(lstRec);
				lstApplDocNumber.add(lstRec.getDocumentNo());
				custPayAmt = custPayAmt.add(lstRec.getLoyaltsPointencahsed());
			}else{
				lstRec.setDeleteStatus(Boolean.TRUE);
			}

		}
		
		setCalNetAmountPaid((getCalNetAmountPaid()==null?BigDecimal.ZERO:getCalNetAmountPaid()).subtract(custPayAmt));
		
		if(lstOnlyselectedrec != null && lstOnlyselectedrec.size() != 0){
			lstselectedrecord.addAll(lstOnlyselectedrec);
		}

		if (lstselectedrecord.size() == 0) {
			setCalGrossAmount(null);
			setCalNetAmountPaid(null);
			setApplicationDocNum(null);
			lstApplDocNumber.clear();
		} else if (lstselectedrecord.size() == 1) {
			setApplicationDocNum(lstselectedrecord.get(0).getDocumentNo());
		} else {
			setShoppingcartExchangeRate(null);
			setApplicationDocNum(null);
		}
		
		

		/*setColCurrency(generalService.getCurrencyName(new BigDecimal(sessionStateManage.getCurrencyId())));
		
		if (shoppingCartDataTableBean.getSelectedrecord() && shoppingcartDTList.size() == 1) {
			if (shoppingCartDataTableBean.getApplicationType().equalsIgnoreCase(Constants.Remittance)) {
				remittanceNo = remittanceNo.add(new BigDecimal(1));
			}
			//tempCalGrossAmount = tempCalGrossAmount.add(shoppingCartDataTableBean.getLocalTranxAmount() == null ? new BigDecimal(0) : shoppingCartDataTableBean.getLocalTranxAmount());
			tempCalGrossAmount = tempCalGrossAmount.add(shoppingCartDataTableBean.getLocalTranxAmount());
			setCalGrossAmount(tempCalGrossAmount);
			tempCalNetAmountPaid = tempCalNetAmountPaid.add(shoppingCartDataTableBean.getLocalNextTranxAmount() == null ? new BigDecimal(0) : shoppingCartDataTableBean.getLocalNextTranxAmount().subtract(shoppingCartDataTableBean.getLoyaltsPointencahsed()));
			setCalNetAmountPaid(tempCalNetAmountPaid);
			lstselectedrecord.add(shoppingCartDataTableBean);
			lstApplDocNumber.add(shoppingCartDataTableBean.getDocumentNo());
		} else if (shoppingCartDataTableBean.getSelectedrecord()) {
			if (shoppingCartDataTableBean.getApplicationType().equalsIgnoreCase(Constants.Remittance)) {
				remittanceNo = remittanceNo.add(new BigDecimal(1));
			}
			tempCalGrossAmount = tempCalGrossAmount.add(shoppingCartDataTableBean.getLocalTranxAmount() == null ? new BigDecimal(0) : shoppingCartDataTableBean.getLocalTranxAmount());
			setCalGrossAmount(tempCalGrossAmount);
			tempCalNetAmountPaid = tempCalNetAmountPaid.add(shoppingCartDataTableBean.getLocalNextTranxAmount() == null ? new BigDecimal(0) : shoppingCartDataTableBean.getLocalNextTranxAmount().subtract(shoppingCartDataTableBean.getLoyaltsPointencahsed()));
			setCalNetAmountPaid(tempCalNetAmountPaid);
			shoppingCartDataTableBean.setDeleteStatus(Boolean.FALSE);
			lstselectedrecord.add(shoppingCartDataTableBean);
			lstApplDocNumber.add(shoppingCartDataTableBean.getDocumentNo());
		} else if (shoppingCartDataTableBean.getSelectedrecord() == false) {
			if (shoppingCartDataTableBean.getApplicationType().equalsIgnoreCase(Constants.Remittance)) {
				remittanceNo = remittanceNo.subtract(new BigDecimal(1));
			}
			tempCalGrossAmount = tempCalGrossAmount.subtract(shoppingCartDataTableBean.getLocalTranxAmount() == null ? new BigDecimal(0) : shoppingCartDataTableBean.getLocalTranxAmount());
			setCalGrossAmount(tempCalGrossAmount);
			tempCalNetAmountPaid = tempCalNetAmountPaid.subtract(shoppingCartDataTableBean.getLocalNextTranxAmount() == null ? new BigDecimal(0) : shoppingCartDataTableBean.getLocalNextTranxAmount().subtract(shoppingCartDataTableBean.getLoyaltsPointencahsed()));
			setCalNetAmountPaid(tempCalNetAmountPaid);
			shoppingCartDataTableBean.setDeleteStatus(Boolean.TRUE);
			for (int i = 0; i < lstselectedrecord.size(); i++) {
				ShoppingCartDataTableBean shoppingCartDataTableBean1 = lstselectedrecord.get(i);
				if (shoppingCartDataTableBean1.getApplicationDetailsId().compareTo(shoppingCartDataTableBean.getApplicationDetailsId()) == 0) {
					lstselectedrecord.remove(i);
				}
			}
			lstApplDocNumber.remove(shoppingCartDataTableBean.getDocumentNo());
		} else {
			log.info("checkbox not working");
		}

		if (lstselectedrecord.size() == 0) {
			setCalGrossAmount(null);
			setCalNetAmountPaid(null);
			setShoppingcartExchangeRate(null);
			setBooRenderMultiDocNum(false);
			setBooRenderSingleDocNum(true);
			setApplicationDocNum(null);
			lstApplDocNumber.clear();
		} else if (lstselectedrecord.size() == 1) {
			setShoppingcartExchangeRate(lstselectedrecord.get(0).getExchangeRateApplied());
			setBooRenderSingleDocNum(true);
			setBooRenderMultiDocNum(false);
			setApplicationDocNum(lstselectedrecord.get(0).getDocumentNo());
		} else {
			setShoppingcartExchangeRate(null);
			setBooRenderMultiDocNum(true);
			setBooRenderSingleDocNum(false);
			setApplicationDocNum(null);
			setCashRounding(null);
		}

		// to call round function while click
		if (getCalNetAmountPaid() != null) {
			setDummiTotalGrossAmount(getCalGrossAmount());
			setDummiTotalNetAmount(getCalNetAmountPaid());
			roundingShoppingCartList();
		} else {
			setCashRounding(null);
			setApplicationDocNum(null);
			setCalGrossAmount(null);
			setCalNetAmountPaid(null);
			setDummiTotalGrossAmount(null);
			setDummiTotalNetAmount(null);
			setBooShowCashRoundingPanel(false);
			lstApplDocNumber.clear();
		}*/
		
	}
	
	
	
	public void deleteRecordShoppingCart(ShoppingCartDataTableBean shoppingCartData) {
		log.info(shoppingCartData.getRemittanceApplicationId());
		iPersonalRemittanceService.deleteShoppingCartByApplId(shoppingCartData.getRemittanceApplicationId());
		setCalGrossAmount(null);
		setCalNetAmountPaid(null);
		tempCalGrossAmount = new BigDecimal(0);
		tempCalNetAmountPaid = new BigDecimal(0);
		getShoppingCartDetails(getCustomerNo());
	}
	
	
	
	public void roundingShoppingCartList() {

		BigDecimal differenceNetAmount = BigDecimal.ZERO;
		BigDecimal upNetAmount = BigDecimal.ZERO;
		BigDecimal downNetAmount = BigDecimal.ZERO;
		String roundtype = null;
		setBooRenderModifiedRoundData(false);
		lstModifyRoundRecord.clear();

		if (getCashRounding() == null) {
			setCashRounding(Constants.U);

			BigDecimal roundingValue;
			try {
				roundingValue = iPersonalRemittanceService.roundingTotalNetAmountbyFunction(sessionStateManage.getCountryId(),getCalNetAmountPaid(), getCashRounding());

				if (roundingValue != null) {
					if (getCalNetAmountPaid().compareTo(roundingValue) == 0) {
						setBooShowCashRoundingPanel(false);
						setCashRounding(null);
					} else {
						setBooShowCashRoundingPanel(true);
						setCashRounding(null);
					}
				} else {
					// round value null
					setCashRounding(null);
					RequestContext.getCurrentInstance().execute("roundValueErr.show();");
				}

			} catch (AMGException e) {
				setExceptionMessage(e.getMessage());
				RequestContext.getCurrentInstance().execute("sqlexception.show();");
			}

		} else {

			if (getShoppingcartExchangeRate() != null) {
				BigDecimal roundingValue;
				try {
					roundingValue = iPersonalRemittanceService.roundingTotalNetAmountbyFunction(sessionStateManage.getCountryId(),getDummiTotalNetAmount(), getCashRounding());

					if (roundingValue != null) {
						if (getDummiTotalNetAmount().compareTo(roundingValue) < 0) {
							differenceNetAmount = roundingValue.subtract(getDummiTotalNetAmount());
							int decimalvalue = foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()));
							upNetAmount = differenceNetAmount.divide(getShoppingcartExchangeRate(), decimalvalue,BigDecimal.ROUND_HALF_UP);
							setCalNetAmountPaid(GetRound.roundBigDecimal(roundingValue, decimalvalue));
							setCalGrossAmount(getDummiTotalGrossAmount().add(differenceNetAmount));
							roundtype = Constants.UP;
							lstModifyRoundRecord.clear();
							if (lstselectedrecord.size() == 1) {
								ShoppingCartDataTableBean selectedrec = lstselectedrecord.get(0);
								ShoppingCartDataTableBean lstModifiedData = changeRoundCalculationinShoppingCart(selectedrec, upNetAmount, roundtype,differenceNetAmount);
								lstModifyRoundRecord.add(lstModifiedData);
								setBooRenderModifiedRoundData(true);
							}

						} else {
							differenceNetAmount = getDummiTotalNetAmount().subtract(roundingValue);
							int decimalvalue = foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()));
							downNetAmount = differenceNetAmount.divide(getShoppingcartExchangeRate(), decimalvalue,BigDecimal.ROUND_HALF_UP);
							setCalNetAmountPaid(GetRound.roundBigDecimal(roundingValue, decimalvalue));
							setCalGrossAmount(getDummiTotalGrossAmount().subtract(differenceNetAmount));
							roundtype = Constants.DOWN;
							lstModifyRoundRecord.clear();
							if (lstselectedrecord.size() == 1) {
								ShoppingCartDataTableBean selectedrec = lstselectedrecord.get(0);
								ShoppingCartDataTableBean lstModifiedData = changeRoundCalculationinShoppingCart(selectedrec, downNetAmount, roundtype,differenceNetAmount);
								lstModifyRoundRecord.add(lstModifiedData);
								setBooRenderModifiedRoundData(true);
							}
						}
					} else {
						// round value null
						setCashRounding(null);
						RequestContext.getCurrentInstance().execute("roundValueErr.show();");
					}
				} catch (AMGException e) {
					setExceptionMessage(e.getMessage());
					RequestContext.getCurrentInstance().execute("sqlexception.show();");
				}



			} else {
				if (getApplicationDocNum() != null) {
					if (lstselectedrecord.size() != 0) {

						for (ShoppingCartDataTableBean selectedrec : lstselectedrecord) {

							if (getApplicationDocNum().compareTo(selectedrec.getDocumentNo()) == 0) {
								BigDecimal roundingValue;
								try {
									roundingValue = iPersonalRemittanceService.roundingTotalNetAmountbyFunction(sessionStateManage.getCountryId(),getDummiTotalNetAmount(),getCashRounding());

									if (roundingValue != null) {
										if (getDummiTotalNetAmount().compareTo(roundingValue) < 0) {
											differenceNetAmount = roundingValue.subtract(getDummiTotalNetAmount());
											int decimalvalue = foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()));
											upNetAmount = differenceNetAmount.divide(selectedrec.getExchangeRateApplied(),decimalvalue,BigDecimal.ROUND_HALF_UP);
											setCalNetAmountPaid(GetRound.roundBigDecimal(roundingValue,decimalvalue));
											setCalGrossAmount(getDummiTotalGrossAmount().add(differenceNetAmount));
											roundtype = Constants.UP;
											lstModifyRoundRecord.clear();
											ShoppingCartDataTableBean lstModifiedData = changeRoundCalculationinShoppingCart(selectedrec, upNetAmount,roundtype, differenceNetAmount);
											lstModifyRoundRecord.add(lstModifiedData);
											setBooRenderModifiedRoundData(true);

										} else {
											differenceNetAmount = getDummiTotalNetAmount().subtract(roundingValue);
											int decimalvalue = foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()));
											downNetAmount = differenceNetAmount.divide(selectedrec.getExchangeRateApplied(),decimalvalue,BigDecimal.ROUND_HALF_UP);
											setCalNetAmountPaid(GetRound.roundBigDecimal(roundingValue,decimalvalue));
											setCalGrossAmount(getDummiTotalGrossAmount().subtract(differenceNetAmount));
											roundtype = Constants.DOWN;
											lstModifyRoundRecord.clear();
											ShoppingCartDataTableBean lstModifiedData = changeRoundCalculationinShoppingCart(selectedrec, downNetAmount,roundtype, differenceNetAmount);
											lstModifyRoundRecord.add(lstModifiedData);
											setBooRenderModifiedRoundData(true);
										}
									} else {
										// round value null
										setCashRounding(null);
										RequestContext.getCurrentInstance().execute("roundValueErr.show();");
									}
								} catch (AMGException e) {
									setExceptionMessage(e.getMessage());
									log.error("*******Error message ********"+ getExceptionMessage());
									RequestContext.getCurrentInstance().execute("sqlexception.show();");
								}
							}
						}
					}
				}
			}
		}
	}

	
	public ShoppingCartDataTableBean changeRoundCalculationinShoppingCart(ShoppingCartDataTableBean shoppingCartDetails,BigDecimal netAmount, String roundtype,BigDecimal differenceNetAmount) {

		ShoppingCartDataTableBean shoppingCartDataTableBean = new ShoppingCartDataTableBean();

		shoppingCartDataTableBean.setRemittanceApplicationId(shoppingCartDetails.getRemittanceApplicationId());
		shoppingCartDataTableBean.setApplicationType(shoppingCartDetails.getApplicationType());
		shoppingCartDataTableBean.setApplicationTypeDesc(shoppingCartDetails.getApplicationTypeDesc());
		shoppingCartDataTableBean.setBeneficiaryAccountNo(shoppingCartDetails.getBeneficiaryAccountNo());
		shoppingCartDataTableBean.setBeneficiaryBank(shoppingCartDetails.getBeneficiaryBank());
		shoppingCartDataTableBean.setBeneficiaryBranch(shoppingCartDetails.getBeneficiaryBranch());
		shoppingCartDataTableBean.setBeneficiaryFirstName(shoppingCartDetails.getBeneficiaryFirstName());
		shoppingCartDataTableBean.setBeneficiarySecondName(shoppingCartDetails.getBeneficiarySecondName());
		shoppingCartDataTableBean.setBeneficiaryThirdName(shoppingCartDetails.getBeneficiaryThirdName());
		shoppingCartDataTableBean.setBeneficiaryFourthName(shoppingCartDetails.getBeneficiaryFourthName());
		shoppingCartDataTableBean.setBeneficiaryId(shoppingCartDetails.getBeneficiaryId());
		shoppingCartDataTableBean.setBeneficiaryInterBankOne(shoppingCartDetails.getBeneficiaryInterBankOne());
		shoppingCartDataTableBean.setBeneficiaryInterBankTwo(shoppingCartDetails.getBeneficiaryInterBankTwo());
		shoppingCartDataTableBean.setBeneficiarySwiftBankOne(shoppingCartDetails.getBeneficiarySwiftBankOne());
		shoppingCartDataTableBean.setBeneficiarySwiftBankTwo(shoppingCartDetails.getBeneficiarySwiftBankTwo());
		shoppingCartDataTableBean.setBeneficiaryName(shoppingCartDetails.getBeneficiaryName());
		shoppingCartDataTableBean.setCompanyId(shoppingCartDetails.getCompanyId());
		shoppingCartDataTableBean.setDocumentFinanceYear(shoppingCartDetails.getDocumentFinanceYear());
		shoppingCartDataTableBean.setDocumentId(shoppingCartDetails.getDocumentId());
		shoppingCartDataTableBean.setForeigncurrency(shoppingCartDetails.getForeigncurrency());
		if (roundtype.equalsIgnoreCase(Constants.UP)) {
			shoppingCartDataTableBean.setLocalTranxAmount(shoppingCartDetails.getLocalTranxAmount().add(differenceNetAmount));
			shoppingCartDataTableBean.setLocalNextTranxAmount(shoppingCartDetails.getLocalNextTranxAmount().add(differenceNetAmount));
			shoppingCartDataTableBean.setForeignTranxAmount(GetRound.roundBigDecimal(shoppingCartDetails.getForeignTranxAmount().add(netAmount),
					foreignLocalCurrencyDenominationService.getDecimalPerCurrency(shoppingCartDetails.getForeigncurrency())));
		} else {
			shoppingCartDataTableBean.setLocalTranxAmount(shoppingCartDetails.getLocalTranxAmount().subtract(differenceNetAmount));
			shoppingCartDataTableBean.setLocalNextTranxAmount(shoppingCartDetails.getLocalNextTranxAmount().subtract(differenceNetAmount));
			shoppingCartDataTableBean.setForeignTranxAmount(GetRound.roundBigDecimal(shoppingCartDetails.getForeignTranxAmount().subtract(netAmount),
					foreignLocalCurrencyDenominationService.getDecimalPerCurrency(shoppingCartDetails.getForeigncurrency())));
		}
		shoppingCartDataTableBean.setLocalChargeAmount(shoppingCartDetails.getLocalChargeAmount());
		shoppingCartDataTableBean.setLocalCommisionAmount(shoppingCartDetails.getLocalCommisionAmount());
		shoppingCartDataTableBean.setLocalDeliveryAmount(new BigDecimal(0));// shoppingCartDetails.getLocalDeliveryAmount());
		shoppingCartDataTableBean.setIsActive(shoppingCartDetails.getIsActive());
		shoppingCartDataTableBean.setCustomerId(shoppingCartDetails.getCustomerId());
		shoppingCartDataTableBean.setExchangeRateApplied(shoppingCartDetails.getExchangeRateApplied());
		shoppingCartDataTableBean.setApplicationDetailsId(shoppingCartDetails.getApplicationDetailsId());
		shoppingCartDataTableBean.setDocumentNo(shoppingCartDetails.getDocumentNo());
		shoppingCartDataTableBean.setLocalcurrency(shoppingCartDetails.getLocalcurrency());
		if (shoppingCartDetails.getSpldeal() != null) {
			shoppingCartDataTableBean.setSpldeal(shoppingCartDetails.getSpldeal());
			shoppingCartDataTableBean.setSpldealStatus(Constants.YES);
		} else {
			shoppingCartDataTableBean.setSpldealStatus(Constants.NO);
		}
		shoppingCartDataTableBean.setSelectedrecord(Boolean.FALSE);
		shoppingCartDataTableBean.setDeleteStatus(Boolean.TRUE);

		return shoppingCartDataTableBean;
	}

	 

	public void clear() {
		
		setBooRenderAccountDialogBox(false);
		setDocumentNumber(null);
		setLstBeneName(null);
		setDatabenificaryname(null);
		setBeneBankList(null);
		setBeneAccountList(null);
		setBeneficaryBankId(null);
		setLstAccount(null);
		setLstBank(null);
		setLstBeneName(null);
		setDatabenificaryname(null);
		setBeneficaryBankId(null);
		setBeneBankName(null);
		setDataAccountnum(null);
		setLstofCurrency(null);
		setLstofDestinationCurrency(null);
		setCurrency(null);
		setTotalKnetAmount(null);
		setCollectionDocumentCode(null);
		setCollectionDocumentNumber(null);
		setCollectionFinanceYear(null);
		setDebitCard1(null);
		setDebitCard2(null);
		setDebitCard3(null);
		setDebitCard4(null);
		setProcedureError(null);
		setExceptionMessage(null);
		setExceptionMessageForReport(null);
		setErrorMessage(null);
		setAmountToRemit(null);
		setEquivalentCurrency(null);
		setEquivalentRemitAmount(null);
		setDestiCurrency(null);
		setDestiCurrencyId(null);
	}
	
	public void remittanceReceiptReportInit() throws JRException {

		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(remittanceReceiptSubreportList);
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String realPath = ctx.getRealPath("//");
		String reportPath = realPath + "//reports//design//RemittanceReceiptNewReport.jasper";
		System.out.println(reportPath);		
		jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap(), beanCollectionDataSource);

	}

	
	
	
	
	private void fetchRemittanceReceiptReportData(BigDecimal documentNumber, BigDecimal finaceYear, String documentCode) throws Exception {

		collectionViewList.clear();
		remittanceReceiptSubreportList = new CopyOnWriteArrayList<RemittanceReceiptSubreport>();

		List<RemittanceApplicationView> remittanceApplicationList = new ArrayList<RemittanceApplicationView>();

		List<RemittanceApplicationView> fcsaleList = new ArrayList<RemittanceApplicationView>();

		List<RemittanceReportBean> remittanceApplList = new ArrayList<RemittanceReportBean>();

		List<RemittanceReportBean> fcsaleAppList = new ArrayList<RemittanceReportBean>();

		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String realPath = ctx.getRealPath("//");
		String subReportPath = realPath + Constants.SUB_REPORT_PATH;
		String waterMark = realPath + Constants.REPORT_WATERMARK_LOGO;
		
		
		String logoPath =null;
		if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.KUWAIT)){
			logoPath = realPath+Constants.LOGO_PATH_KWT_FOR_RCPT;
		}else if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.OMAN)){
			logoPath = realPath+Constants.LOGO_PATH_OM_FOR_RCPT;
		}else if(sessionStateManage.getCountryName().equalsIgnoreCase(Constants.BAHRAIN)){
			logoPath =realPath+Constants.LOGO_PATH_BH_FOR_RCPT;
		}else{
			logoPath =realPath+Constants.LOGO_PATH_KWT_FOR_RCPT;
		}
		

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(new Date());
		int noOfTransactions = 0;

		String currencyQuoteName = generalService.getCurrencyQuote(new BigDecimal(sessionStateManage.getCurrencyId()));

		List<Employee> employeeList = iPersonalRemittanceService.getEmployeeDetails(sessionStateManage.getUserName());

		List<RemittanceApplicationView> remittanceViewlist = iPersonalRemittanceService.getRecordsForRemittanceReceiptReport(documentNumber, finaceYear, documentCode);

		if (remittanceViewlist.size() > 0) {

			for (RemittanceApplicationView remittanceAppview : remittanceViewlist) {

				if (remittanceAppview.getApplicationTypeDesc().equalsIgnoreCase("REMITTANCE")) {
					remittanceApplicationList.add(remittanceAppview);
					noOfTransactions = noOfTransactions + 1;
				} else if (remittanceAppview.getApplicationTypeDesc().equalsIgnoreCase("FOREIGN CURRENCY SALE")) {
					fcsaleList.add(remittanceAppview);
					noOfTransactions = noOfTransactions + 1;
				}
			}
			// remittance List
			for (RemittanceApplicationView view : remittanceApplicationList) {

				RemittanceReportBean obj = new RemittanceReportBean();

				List<PurposeOfRemittanceView> purposeOfRemittanceList = iPersonalRemittanceService.getPurposeOfRemittance(view.getDocumentNo(), view.getDocumentFinancialYear());

				List<PurposeOfRemittanceReportBean> purposeOfRemitTrList1 = new ArrayList<PurposeOfRemittanceReportBean>();
				for (PurposeOfRemittanceView purposeObj : purposeOfRemittanceList) {
					PurposeOfRemittanceReportBean beanObj = new PurposeOfRemittanceReportBean();
					beanObj.setPurposeOfTrField(purposeObj.getFlexfieldName());
					beanObj.setPurposeOfTrfieldArabic(null);
					beanObj.setPurposeOfTrValue(purposeObj.getFlexiFieldValue());
					purposeOfRemitTrList1.add(beanObj);
				}

				if (purposeOfRemitTrList1.size() > 0) {
					obj.setPurposeOfRemitTrList(purposeOfRemitTrList1);
				}
				// setting customer reference
				StringBuffer customerReff = new StringBuffer();

				if (view.getCustomerReference() != null) {
					customerReff.append(view.getCustomerReference());
					customerReff.append(" / ");
				}

				StringBuffer customerName = new StringBuffer();

				if (view.getFirstName() != null) {
					customerName.append(" ");
					customerName.append(view.getFirstName());
					customerReff.append(" ");
					customerReff.append(view.getFirstName());
				}
				if (view.getMiddleName() != null) {
					customerName.append(" ");
					customerName.append(view.getMiddleName());
					customerReff.append(" ");
					customerReff.append(view.getMiddleName());
				}
				if (view.getLastName() != null) {
					customerName.append(" ");
					customerName.append(view.getLastName());
					customerReff.append(" ");
					customerReff.append(view.getLastName());
				}

				obj.setFirstName(customerReff.toString());
				setCustomerNameForReport(customerName.toString());

				if (view.getContactNumber() != null && !view.getContactNumber().trim().equalsIgnoreCase("")) {
					obj.setMobileNo(new BigDecimal(view.getContactNumber()));
				}

				obj.setCivilId(view.getIdentityInt());

				Date sysdate = view.getIdentityExpiryDate();

				if (sysdate != null) {
					obj.setIdExpiryDate(new SimpleDateFormat("dd/MM/yyy").format(sysdate));
				}

				obj.setLocation(sessionStateManage.getLocation());

				// setting receipt Number
				StringBuffer receiptNo = new StringBuffer();

				if (view.getDocumentFinancialYear() != null) {
					receiptNo.append(view.getDocumentFinancialYear());
					receiptNo.append(" / ");
				}
				if (view.getCollectionDocumentNo() != null) {
					receiptNo.append(view.getCollectionDocumentNo());
				}
				obj.setReceiptNo(receiptNo.toString());

				// setting transaction Number
				StringBuffer transactionNo = new StringBuffer();
				if (view.getDocumentFinancialYear() != null) {
					transactionNo.append(view.getDocumentFinancialYear());
					transactionNo.append(" / ");
				}
				if (view.getDocumentNo() != null) {
					transactionNo.append(view.getDocumentNo());
				}
				obj.setTransactionNo(transactionNo.toString());

				obj.setDate(currentDate);
				obj.setBeneficiaryName(view.getBeneficiaryName());
				obj.setBenefeciaryBankName(view.getBeneficiaryBank());
				obj.setBenefeciaryBranchName(view.getBenefeciaryBranch());
				obj.setBenefeciaryAccountNumber(view.getBenefeciaryAccountNo());
				obj.setNoOfTransaction(new BigDecimal(noOfTransactions));
				obj.setPhoneNumber(view.getPhoneNumber());
				obj.setUserName(sessionStateManage.getUserName());
				obj.setPinNo(view.getPinNo());
				obj.setLogoPath(logoPath);

				HashMap<String, String> loyaltiPoints = iPersonalRemittanceService.getloyalityPointsFromProcedure(view.getCustomerReference(), view.getDocumentDate());

				String prLtyStr1 = loyaltiPoints.get("P_LTY_STR1");
				String prLtyStr2 = loyaltiPoints.get("P_LTY_STR2");
				String prInsStr1 = loyaltiPoints.get("P_INS_STR1");
				String prInsStr2 = loyaltiPoints.get("P_INS_STR2");
				String prInsStrAr1 = loyaltiPoints.get("P_INS_STR_AR1");
				String prInsStrAr2 = loyaltiPoints.get("P_INS_STR_AR2");

				StringBuffer loyaltyPoint = new StringBuffer();
				if (!prLtyStr1.trim().equals("")) {
					loyaltyPoint.append(prLtyStr1);
				}
				if (!prLtyStr2.trim().equals("")) {
					loyaltyPoint.append("\n");
					loyaltyPoint.append(prLtyStr2);
				}
				obj.setLoyalityPointExpiring(loyaltyPoint.toString());

				StringBuffer insurence1 = new StringBuffer();
				if (!prInsStr1.trim().equals("")) {
					insurence1.append(prInsStr1);
				}
				if (!prInsStrAr1.trim().equals("")) {
					insurence1.append("\n");
					insurence1.append(prInsStrAr1);
				}
				obj.setInsurence1(insurence1.toString());

				StringBuffer insurence2 = new StringBuffer();
				if (!prInsStr2.trim().equals("")) {
					insurence2.append(prInsStr2);
				}
				if (!prInsStrAr2.trim().equals("")) {
					insurence2.append("\n");
					insurence2.append(prInsStrAr2);
				}
				obj.setInsurence2(insurence2.toString());

				// setting beneficiary Address
				StringBuffer address = new StringBuffer();
				if (view.getBeneStateName() != null) {
					address.append(view.getBeneStateName());
					address.append(",  ");
				}
				if (view.getBeneCityName() != null) {
					address.append(view.getBeneCityName());
					address.append(",  ");
				}
				if (view.getBeneDistrictName() != null) {
					address.append(view.getBeneDistrictName());
				}
				obj.setAddress(address.toString());

				// setting payment channel
				StringBuffer paymentChannel = new StringBuffer();
				if (view.getRemittanceDescription() != null) {
					paymentChannel.append(view.getRemittanceDescription());
					paymentChannel.append(" - ");
				}
				if (view.getDeliveryDescription() != null) {
					paymentChannel.append(view.getDeliveryDescription());
				}
				obj.setPaymentChannel(paymentChannel.toString());

				String currencyAndAmount = null;
				BigDecimal foreignTransationAmount = GetRound.roundBigDecimal((view.getForeignTransactionAmount()),
						foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getForeignCurrencyId()));
				if (view.getCurrencyQuoteName() != null && foreignTransationAmount != null) {
					currencyAndAmount = view.getCurrencyQuoteName() + "     ******" + foreignTransationAmount;
				}
				obj.setCurrencyAndAmount(currencyAndAmount);

				if (view.getCurrencyQuoteName() != null && currencyQuoteName != null && view.getExchangeRateApplied() != null) {
					obj.setExchangeRate(view.getCurrencyQuoteName() + " / " + currencyQuoteName + "     " + view.getExchangeRateApplied().toString());
				}

				if (view.getLocalTransactionAmount() != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal transationAmount = GetRound.roundBigDecimal((view.getLocalTransactionAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setLocalTransactionAmount(currencyQuoteName + "     ******" + transationAmount.toString());
				}

				if (view.getLocalCommissionAmount() != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal localCommitionAmount = GetRound.roundBigDecimal((view.getLocalCommissionAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setCommision(currencyQuoteName + "     ******" + localCommitionAmount.toString());
				}

				if (view.getLocalChargeAmount() != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal localChargeAmount = GetRound.roundBigDecimal((view.getLocalChargeAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setOtherCharges(currencyQuoteName + "     ******" + localChargeAmount.toString());
				}

				if (view.getLocalNetTransactionAmount() != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal netAmount = GetRound.roundBigDecimal((view.getLocalNetTransactionAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setTotalAmount(currencyQuoteName + "     ******" + netAmount.toString());
				}

				obj.setFutherInstructions(view.getInstructions());
				obj.setSourceOfIncome(view.getSourceOfIncomeDesc());
				obj.setIntermediataryBank(view.getBenefeciaryInterBank1());

				List<CollectionDetailView> collectionDetailList1 = iPersonalRemittanceService.getCollectionDetailForRemittanceReceipt(view.getCompanyId(), view.getCollectionDocumentNo(),
						view.getCollectionDocFinanceYear(), view.getCollectionDocCode());

				CollectionDetailView collectionDetailView = collectionDetailList1.get(0);

				if (collectionDetailView.getNetAmount() != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal collectNetAmount = GetRound.roundBigDecimal((collectionDetailView.getNetAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setNetAmount(currencyQuoteName + "     ******" + collectNetAmount);
				}

				if (collectionDetailView.getPaidAmount() != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal collectPaidAmount = GetRound.roundBigDecimal((collectionDetailView.getPaidAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setPaidAmount(currencyQuoteName + "     ******" + collectPaidAmount);
				}

				if (collectionDetailView.getRefundedAmount() != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal collectRefundAmount = GetRound.roundBigDecimal((collectionDetailView.getRefundedAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setRefundedAmount(currencyQuoteName + "     ******" + collectRefundAmount);
				}

				obj.setSubReport(subReportPath);
				obj.setCollectionDetailList(calculateCollectionMode(view));

				// addedd new column
				BigDecimal lessLoyaltyEncash = BigDecimal.ZERO;
				BigDecimal amountPayable = BigDecimal.ZERO;
				List<CollectionPaymentDetailsView> collectionPmtDetailList = iPersonalRemittanceService.getCollectionPaymentDetailForRemittanceReceipt(view.getCompanyId(),
						view.getCollectionDocumentNo(), view.getCollectionDocFinanceYear(), view.getCollectionDocCode());
				for (CollectionPaymentDetailsView collPaymentDetailsView : collectionPmtDetailList) {
					if (collPaymentDetailsView.getCollectionMode().equalsIgnoreCase(Constants.VOCHERCODE)) {
						lessLoyaltyEncash = collPaymentDetailsView.getCollectAmount();
						amountPayable = amountPayable.add(collPaymentDetailsView.getCollectAmount());
					} else {
						amountPayable = amountPayable.add(collPaymentDetailsView.getCollectAmount());
					}
				}
				if (lessLoyaltyEncash.compareTo(BigDecimal.ZERO) == 0) {
					obj.setLessLoyaltyEncasement(null);
				} else {
					BigDecimal loyaltyAmount = GetRound.roundBigDecimal((lessLoyaltyEncash), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setLessLoyaltyEncasement(currencyQuoteName + "     ******" + loyaltyAmount);
				}

				if (amountPayable != null && currencyQuoteName != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal payable = GetRound.roundBigDecimal((amountPayable), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setAmountPayable(currencyQuoteName + "     ******" + payable);
				}

				// obj.setSignature(view.getCustomerSignature());
				// Rabil

				// Added by Rabil
				try {
					if (view.getCustomerSignatureClob() != null) {
						obj.setSignature(view.getCustomerSignatureClob().getSubString(1, (int) view.getCustomerSignatureClob().length()));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				if (employeeList != null && employeeList.size() > 0) {
					try {
						Employee employee = employeeList.get(0);
						if (employee.getSignatureSpecimenClob() != null) {
							obj.setCashierSignature(employee.getSignatureSpecimenClob().getSubString(1, (int) employee.getSignatureSpecimenClob().length()));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				// For Company information ADDED BY VISWA --START
				List<CompanyMaster> companyMaster = iPersonalRemittanceService.getCompanyMaster(sessionStateManage.getCompanyId());
				StringBuffer engCompanyInfo = null;
				StringBuffer arabicCompanyInfo = null;
				if ( companyMaster != null && companyMaster.size() > 0) {
					engCompanyInfo = new StringBuffer();
					if (companyMaster.get(0).getAddress1() != null && companyMaster.get(0).getAddress1().length() > 0) {
						engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getAddress1() + ",");
					}
					if (companyMaster.get(0).getAddress2() != null && companyMaster.get(0).getAddress2().length() > 0) {
						engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getAddress2() + ",");
					}
					if (companyMaster.get(0).getAddress3() != null && companyMaster.get(0).getAddress3().length() > 0) {
						engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getAddress3() + ",");
					}
					if (companyMaster.get(0).getRegistrationNumber() != null && companyMaster.get(0).getRegistrationNumber().length() > 0) {
						engCompanyInfo = engCompanyInfo.append("C.R. " + companyMaster.get(0).getRegistrationNumber() + ",");
					}
					if (companyMaster.get(0).getCapitalAmount() != null && companyMaster.get(0).getCapitalAmount().length() > 0) {
						engCompanyInfo = engCompanyInfo.append("Share Capital-" + companyMaster.get(0).getCapitalAmount());
					}
					obj.setEngCompanyInfo(engCompanyInfo.toString());

					arabicCompanyInfo = new StringBuffer();

					if (companyMaster.get(0).getArabicAddress1() != null && companyMaster.get(0).getArabicAddress1().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress1());
					}
					if (companyMaster.get(0).getArabicAddress2() != null && companyMaster.get(0).getArabicAddress2().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress2() + ",");
					}
					if (companyMaster.get(0).getArabicAddress3() != null && companyMaster.get(0).getArabicAddress3().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress3() + ",");
					}
					if (companyMaster.get(0).getRegistrationNumber() != null && companyMaster.get(0).getRegistrationNumber().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(Constants.CR + " " + companyMaster.get(0).getRegistrationNumber() + ",");
					}
					if (companyMaster.get(0).getCapitalAmount() != null && companyMaster.get(0).getCapitalAmount().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(Constants.Share_Capital + " " + companyMaster.get(0).getCapitalAmount());
					}
					obj.setArabicCompanyInfo(arabicCompanyInfo.toString());
				}
				// For Company information ADDED BY VISWA --END

				if (obj.getFirstName() == null || obj.getFirstName().isEmpty()) {
					List<CutomerDetailsView> customerList = iPersonalRemittanceService.getCustomerDetails(view.getCustomerId());

					if (customerList.size() > 0) {
						CutomerDetailsView cust = customerList.get(0);
						obj.setFirstName(cust.getCustomerName());
						if (cust.getContactNumber() != null && !cust.getContactNumber().trim().equalsIgnoreCase("")) {
							obj.setMobileNo(new BigDecimal(cust.getContactNumber()));
						}
						obj.setCivilId(cust.getIdNumber());
						Date sysdate1 = cust.getIdExp();
						if (sysdate1 != null) {
							obj.setIdExpiryDate(new SimpleDateFormat("dd/MM/yyyy").format(sysdate1));
						}
					}
				}

				remittanceApplList.add(obj);
			}

			// for foreign currency Sale report
			for (RemittanceApplicationView view : fcsaleList) {

				RemittanceReportBean obj = new RemittanceReportBean();

				// For Company information ADDED BY VISWA --START
				List<CompanyMaster> companyMaster = iPersonalRemittanceService.getCompanyMaster(sessionStateManage.getCompanyId());
				StringBuffer engCompanyInfo = null;
				StringBuffer arabicCompanyInfo = null;
				if (companyMaster.size() > 0) {
					engCompanyInfo = new StringBuffer();
					if (companyMaster.get(0).getAddress1() != null && companyMaster.get(0).getAddress1().length() > 0) {
						engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getAddress1() + ",");
					}
					if (companyMaster.get(0).getAddress2() != null && companyMaster.get(0).getAddress2().length() > 0) {
						engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getAddress2() + ",");
					}
					if (companyMaster.get(0).getAddress3() != null && companyMaster.get(0).getAddress3().length() > 0) {
						engCompanyInfo = engCompanyInfo.append(companyMaster.get(0).getAddress3() + ",");
					}
					if (companyMaster.get(0).getRegistrationNumber() != null && companyMaster.get(0).getRegistrationNumber().length() > 0) {
						engCompanyInfo = engCompanyInfo.append("C.R. " + companyMaster.get(0).getRegistrationNumber() + ",");
					}
					if (companyMaster.get(0).getCapitalAmount() != null && companyMaster.get(0).getCapitalAmount().length() > 0) {
						engCompanyInfo = engCompanyInfo.append("Share Capital-" + companyMaster.get(0).getCapitalAmount());
					}
					obj.setEngCompanyInfo(engCompanyInfo.toString());

					arabicCompanyInfo = new StringBuffer();

					if (companyMaster.get(0).getArabicAddress1() != null && companyMaster.get(0).getArabicAddress1().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress1());
					}
					if (companyMaster.get(0).getArabicAddress2() != null && companyMaster.get(0).getArabicAddress2().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress2() + ",");
					}
					if (companyMaster.get(0).getArabicAddress3() != null && companyMaster.get(0).getArabicAddress3().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(companyMaster.get(0).getArabicAddress3() + ",");
					}
					if (companyMaster.get(0).getRegistrationNumber() != null && companyMaster.get(0).getRegistrationNumber().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(Constants.CR + " " + companyMaster.get(0).getRegistrationNumber() + ",");
					}
					if (companyMaster.get(0).getCapitalAmount() != null && companyMaster.get(0).getCapitalAmount().length() > 0) {
						arabicCompanyInfo = arabicCompanyInfo.append(Constants.Share_Capital + " " + companyMaster.get(0).getCapitalAmount());
					}
					obj.setArabicCompanyInfo(arabicCompanyInfo.toString());
				}
				// For Company information ADDED BY VISWA --END

				// setting customer reference
				StringBuffer customerReff = new StringBuffer();

				if (view.getCustomerReference() != null) {
					customerReff.append(view.getCustomerReference());
					customerReff.append(" / ");
				}

				StringBuffer customerName = new StringBuffer();

				if (view.getFirstName() != null) {
					customerName.append(" ");
					customerName.append(view.getFirstName());
					customerReff.append(" ");
					customerReff.append(view.getFirstName());
				}
				if (view.getMiddleName() != null) {
					customerName.append(" ");
					customerName.append(view.getMiddleName());
					customerReff.append(" ");
					customerReff.append(view.getMiddleName());
				}
				if (view.getLastName() != null) {
					customerName.append(" ");
					customerName.append(view.getLastName());
					customerReff.append(" ");
					customerReff.append(view.getLastName());
				}

				obj.setFirstName(customerReff.toString());
				setCustomerNameForReport(customerName.toString());

				if (view.getContactNumber() != null && !view.getContactNumber().trim().equalsIgnoreCase("")) {
					obj.setMobileNo(new BigDecimal(view.getContactNumber()));
				}
				obj.setCivilId(view.getIdentityInt());

				Date sysdate = view.getIdentityExpiryDate();
				if (sysdate != null) {
					obj.setIdExpiryDate(new SimpleDateFormat("dd/MM/yyyy").format(sysdate));
				}

				HashMap<String, String> loyaltiPoints = iPersonalRemittanceService.getloyalityPointsFromProcedure(view.getCustomerReference(), view.getDocumentDate());

				String prLtyStr1 = loyaltiPoints.get("P_LTY_STR1");
				String prLtyStr2 = loyaltiPoints.get("P_LTY_STR2");
				String prInsStr1 = loyaltiPoints.get("P_INS_STR1");
				String prInsStr2 = loyaltiPoints.get("P_INS_STR2");
				String prInsStrAr1 = loyaltiPoints.get("P_INS_STR_AR1");
				String prInsStrAr2 = loyaltiPoints.get("P_INS_STR_AR2");

				StringBuffer loyaltyPoint = new StringBuffer();

				if (!prLtyStr1.trim().equals("")) {
					loyaltyPoint.append(prLtyStr1);
				}
				if (!prLtyStr2.trim().equals("")) {
					loyaltyPoint.append("\n");
					loyaltyPoint.append(prLtyStr2);
				}
				obj.setLoyalityPointExpiring(loyaltyPoint.toString());

				StringBuffer insurence = new StringBuffer();

				if (!prInsStr1.trim().equals("")) {
					insurence.append(prInsStr1);
				}
				if (prInsStrAr1.trim().equals("")) {
					insurence.append("\n");
					insurence.append(prInsStrAr1);
				}

				if (!prInsStr2.trim().equals("")) {
					insurence.append("\n");
					insurence.append(prInsStr2);
				}
				if (!prInsStrAr2.trim().equals("")) {
					insurence.append("\n");
					insurence.append(prInsStrAr2);
				}
				obj.setInsurence1(insurence.toString());

				obj.setLocation(view.getCountryBranchName());
				obj.setPhoneNumber(view.getPhoneNumber());
				obj.setDate(currentDate);
				obj.setUserName(sessionStateManage.getUserName());

				// setting receipt Number
				StringBuffer receiptNo = new StringBuffer();
				if (view.getDocumentFinancialYear() != null) {
					receiptNo.append(view.getDocumentFinancialYear());
					receiptNo.append(" / ");
				}
				if (view.getCollectionDocumentNo() != null) {
					receiptNo.append(view.getCollectionDocumentNo());
				}
				obj.setReceiptNo(receiptNo.toString());

				// setting transaction Number
				StringBuffer transactionNo = new StringBuffer();
				if (view.getDocumentFinancialYear() != null) {
					transactionNo.append(view.getDocumentFinancialYear());
					transactionNo.append(" / ");
				}
				if (view.getDocumentNo() != null) {
					transactionNo.append(view.getDocumentNo());
				}
				obj.setTransactionNo(transactionNo.toString());

				if (view.getForeignCurrencyId() != null) {
					String saleCurrency = iPersonalRemittanceService.getCurrencyForUpdate(view.getForeignCurrencyId());
					obj.setCurrencyQuoteName(saleCurrency);
				}
				String saleCurrencyCode = null;

				if (view.getForeignCurrencyId() != null) {
					saleCurrencyCode = generalService.getCurrencyQuote(view.getForeignCurrencyId());
				}

				if (view.getForeignTransactionAmount() != null && saleCurrencyCode != null) {
					BigDecimal foreignTransationAmount = GetRound.roundBigDecimal((view.getForeignTransactionAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getForeignCurrencyId()));
					obj.setSaleAmount(saleCurrencyCode + "     ******" + foreignTransationAmount.toString());
				}

				if (view.getLocalTransactionCurrencyId() != null && currencyQuoteName != null) {
					BigDecimal transationAmount = GetRound.roundBigDecimal((view.getLocalTransactionAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setPurchageAmount(currencyQuoteName + "     ******" + transationAmount.toString());
				}

				if (saleCurrencyCode != null && currencyQuoteName != null && view.getExchangeRateApplied() != null) {
					obj.setExchangeRate(saleCurrencyCode + " / " + currencyQuoteName + "     " + view.getExchangeRateApplied().toString());
				}

				if (view.getLocalTransactionAmount() != null && view.getLocalTransactionCurrencyId() != null && currencyQuoteName != null) {
					BigDecimal transationAmount = GetRound.roundBigDecimal((view.getLocalTransactionAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setLocalTransactionAmount(currencyQuoteName + "     ******" + transationAmount.toString());
				}

				if (view.getLocalCommissionAmount() != null && view.getLocalTransactionCurrencyId() != null && currencyQuoteName != null) {
					BigDecimal localCommitionAmount = GetRound.roundBigDecimal((view.getLocalCommissionAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setCommision(currencyQuoteName + "     ******" + localCommitionAmount.toString());
				}

				/*
				 * List<PurposeOfRemittanceView> purposeOfRemittanceList =
				 * iPersonalRemittanceService
				 * .getPurposeOfRemittance(view.getDocumentNo
				 * (),view.getDocumentFinancialYear());
				 * 
				 * if(purposeOfRemittanceList!=null &&
				 * purposeOfRemittanceList.size()>0){
				 * obj.setPerposeOfRemittance(
				 * purposeOfRemittanceList.get(0).getAmiecDescription()); }
				 */
				obj.setPerposeOfRemittance(view.getPurposeOfTransaction());
				obj.setSourceOfIncome(view.getSourceOfIncomeDesc());

				List<CollectionDetailView> collectionDetailList1 = iPersonalRemittanceService.getCollectionDetailForRemittanceReceipt(view.getCompanyId(), view.getCollectionDocumentNo(),
						view.getCollectionDocFinanceYear(), view.getCollectionDocCode());
				if (collectionDetailList1.size() > 0) {
					CollectionDetailView collectionDetailView = collectionDetailList1.get(0);
					if (collectionDetailView.getNetAmount() != null && currencyQuoteName != null && view.getLocalTransactionCurrencyId() != null) {
						BigDecimal collectNetAmount = GetRound.roundBigDecimal((collectionDetailView.getNetAmount()),
								foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
						obj.setNetAmount(currencyQuoteName + "     ******" + collectNetAmount);
					}
					if (collectionDetailView.getPaidAmount() != null && currencyQuoteName != null && view.getLocalTransactionCurrencyId() != null) {
						BigDecimal collectPaidAmount = GetRound.roundBigDecimal((collectionDetailView.getPaidAmount()),
								foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
						obj.setPaidAmount(currencyQuoteName + "     ******" + collectPaidAmount);
					}
					if (collectionDetailView.getRefundedAmount() != null && currencyQuoteName != null && view.getLocalTransactionCurrencyId() != null) {
						BigDecimal collectRefundAmount = GetRound.roundBigDecimal((collectionDetailView.getRefundedAmount()),
								foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
						obj.setRefundedAmount(currencyQuoteName + "     ******" + collectRefundAmount);
					}
					obj.setCollectionDetailList(calculateCollectionMode(view));
				}

				obj.setSubReport(subReportPath);
				obj.setUserName(sessionStateManage.getUserName());

				// addedd new column
				BigDecimal lessLoyaltyEncash = BigDecimal.ZERO;
				BigDecimal amountPayable = BigDecimal.ZERO;
				List<CollectionPaymentDetailsView> collectionPmtDetailList = iPersonalRemittanceService.getCollectionPaymentDetailForRemittanceReceipt(view.getCompanyId(),
						view.getCollectionDocumentNo(), view.getCollectionDocFinanceYear(), view.getCollectionDocCode());
				for (CollectionPaymentDetailsView collPaymentDetailsView : collectionPmtDetailList) {
					if (collPaymentDetailsView.getCollectionMode().equalsIgnoreCase(Constants.VOCHERCODE)) {
						lessLoyaltyEncash = collPaymentDetailsView.getCollectAmount();
						amountPayable = amountPayable.add(collPaymentDetailsView.getCollectAmount());
					} else {
						amountPayable = amountPayable.add(collPaymentDetailsView.getCollectAmount());
					}
				}
				if (lessLoyaltyEncash.compareTo(BigDecimal.ZERO) == 0) {
					obj.setLessLoyaltyEncasement(null);
				} else {
					BigDecimal loyaltyAmount = GetRound.roundBigDecimal((lessLoyaltyEncash), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setLessLoyaltyEncasement(currencyQuoteName + "     ******" + loyaltyAmount);
				}

				if (amountPayable != null && currencyQuoteName != null && view.getLocalTransactionCurrencyId() != null) {
					BigDecimal payable = GetRound.roundBigDecimal((amountPayable), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(view.getLocalTransactionCurrencyId()));
					obj.setAmountPayable(currencyQuoteName + "     ******" + payable);
				}

				// obj.setSignature(view.getCustomerSignature());

				try {
					if (view.getCustomerSignatureClob() != null) {
						obj.setSignature(view.getCustomerSignatureClob().getSubString(1, (int) view.getCustomerSignatureClob().length()));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				if (employeeList != null && employeeList.size() > 0) {
					try {
						Employee employee = employeeList.get(0);
						if (employee.getSignatureSpecimenClob() != null) {
							obj.setCashierSignature(employee.getSignatureSpecimenClob().getSubString(1, (int) employee.getSignatureSpecimenClob().length()));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				if (obj.getFirstName() == null || obj.getFirstName().isEmpty()) {
					List<CutomerDetailsView> customerList = iPersonalRemittanceService.getCustomerDetails(view.getCustomerId());

					if (customerList.size() > 0) {
						CutomerDetailsView cust = customerList.get(0);
						obj.setFirstName(cust.getCustomerName());
						if (cust.getContactNumber() != null && !cust.getContactNumber().trim().equalsIgnoreCase("")) {
							obj.setMobileNo(new BigDecimal(cust.getContactNumber()));
						}
						obj.setCivilId(cust.getIdNumber());
						Date sysdate1 = cust.getIdExp();
						if (sysdate1 != null) {
							obj.setIdExpiryDate(new SimpleDateFormat("dd/MM/yyyy").format(sysdate1));
						}
					}
				}

				fcsaleAppList.add(obj);

			}

			// for Main Remittance Receipt report (Remittance Receipt and Fc
			// Sale Application)
			RemittanceReceiptSubreport remittanceObj = new RemittanceReceiptSubreport();

			remittanceObj.setWaterMarkLogoPath(waterMark);
			remittanceObj.setWaterMarkCheck(false);
			remittanceObj.setFcsaleAppList(fcsaleAppList);
			remittanceObj.setRemittanceApplList(remittanceApplList);
			remittanceObj.setSubReport(subReportPath);

			if (fcsaleAppList != null && fcsaleAppList.size() > 0) {
				remittanceObj.setFcsaleApplicationCheck(true);
			} /*
			 * else { remittanceObj.setFcsaleApplicationCheck(false); }
			 */
			if (remittanceApplList != null && remittanceApplList.size() > 0) {
				remittanceObj.setRemittanceReceiptCheck(true);
			}/*
			 * else{ remittanceObj.setRemittanceReceiptCheck(false); }
			 */

			remittanceReceiptSubreportList.add(remittanceObj);

		} else {
			RequestContext.getCurrentInstance().execute("noDataForReport.show();");
			return;
		}

	}


	public List<RemittanceReportBean> calculateCollectionMode(RemittanceApplicationView viewCollectionObj) {
		List<RemittanceReportBean> collectionDetailList = new ArrayList<RemittanceReportBean>();
		List<CollectionPaymentDetailsView> collectionPaymentDetailList = iPersonalRemittanceService.getCollectionPaymentDetailForRemittanceReceipt(viewCollectionObj.getCompanyId(),
				viewCollectionObj.getCollectionDocumentNo(), viewCollectionObj.getCollectionDocFinanceYear(), viewCollectionObj.getCollectionDocCode());

		int size = collectionPaymentDetailList.size();
		for (CollectionPaymentDetailsView viewObj : collectionPaymentDetailList) {
			RemittanceReportBean obj = new RemittanceReportBean();
			if (viewObj.getCollectionMode() != null && viewObj.getCollectionMode().equalsIgnoreCase("K")) {
				obj.setCollectionMode(viewObj.getCollectionModeDesc());
				setPaymentModeType(viewObj.getCollectionModeDesc());
				obj.setApprovalNo(viewObj.getApprovalNo());
				obj.setTransactionId(viewObj.getTransactionId());
				obj.setKnetreceiptDateTime(viewObj.getKnetReceiptDatenTime());
				obj.setKnetBooleanCheck(true);
				if (viewObj.getCollectAmount() != null && viewCollectionObj.getLocalTransactionCurrencyId() != null) {
					BigDecimal collectAmount = GetRound.roundBigDecimal((viewObj.getCollectAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(viewCollectionObj.getLocalTransactionCurrencyId()));
					obj.setCollectAmount(collectAmount);
				}
			} else {
				obj.setCollectionMode(viewObj.getCollectionModeDesc());
				obj.setKnetBooleanCheck(false);
				if (viewObj.getCollectAmount() != null && viewCollectionObj.getLocalTransactionCurrencyId() != null) {
					BigDecimal collectAmount = GetRound.roundBigDecimal((viewObj.getCollectAmount()),
							foreignLocalCurrencyDenominationService.getDecimalPerCurrency(viewCollectionObj.getLocalTransactionCurrencyId()));
					obj.setCollectAmount(collectAmount);
				}
			}
			if (size > 1) {
				obj.setDrawLine(true);
			} else {
				obj.setDrawLine(false);
			}
			collectionDetailList.add(obj);
			size = size - 1;
		}
		return collectionDetailList;
	}
	
	
	

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public SessionStateManage getSessionStateManage() {
		return sessionStateManage;
	}

	public void setSessionStateManage(SessionStateManage sessionStateManage) {
		this.sessionStateManage = sessionStateManage;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCrNumber() {
		return customerCrNumber;
	}

	public void setCustomerCrNumber(String customerCrNumber) {
		this.customerCrNumber = customerCrNumber;
	}

	public BigDecimal getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(BigDecimal customerNo) {
		this.customerNo = customerNo;
	}

	public BigDecimal getCustomerrefno() {
		return customerrefno;
	}

	public void setCustomerrefno(BigDecimal customerrefno) {
		this.customerrefno = customerrefno;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}

	public String getCustomerFullName() {
		return customerFullName;
	}

	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}

	public String getCustomerLocalFullName() {
		return customerLocalFullName;
	}

	public void setCustomerLocalFullName(String customerLocalFullName) {
		this.customerLocalFullName = customerLocalFullName;
	}

	public String getCustomerIsActive() {
		return customerIsActive;
	}

	public void setCustomerIsActive(String customerIsActive) {
		this.customerIsActive = customerIsActive;
	}

	public Date getCustomerExpDate() {
		return customerExpDate;
	}

	public void setCustomerExpDate(Date customerExpDate) {
		this.customerExpDate = customerExpDate;
	}

	public String getCustomerExpireDateMsg() {
		return customerExpireDateMsg;
	}

	public void setCustomerExpireDateMsg(String customerExpireDateMsg) {
		this.customerExpireDateMsg = customerExpireDateMsg;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public BigDecimal getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(BigDecimal customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public BigDecimal getNationality() {
		return nationality;
	}

	public void setNationality(BigDecimal nationality) {
		this.nationality = nationality;
	}

	public String getNationalityName() {
		return nationalityName;
	}

	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}

	public Date getDateOfBrith() {
		return dateOfBrith;
	}

	public void setDateOfBrith(Date dateOfBrith) {
		this.dateOfBrith = dateOfBrith;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMcountryCode() {
		return mcountryCode;
	}

	public void setMcountryCode(String mcountryCode) {
		this.mcountryCode = mcountryCode;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getSourceOfIncomes() {
		return sourceOfIncomes;
	}

	public void setSourceOfIncomes(String sourceOfIncomes) {
		this.sourceOfIncomes = sourceOfIncomes;
	}

	public String getPurposeOfTransactions() {
		return purposeOfTransactions;
	}

	public void setPurposeOfTransactions(String purposeOfTransactions) {
		this.purposeOfTransactions = purposeOfTransactions;
	}

	public int getPurposeId() {
		return purposeId;
	}

	public void setPurposeId(int purposeId) {
		this.purposeId = purposeId;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getApplicationCountryName() {
		return applicationCountryName;
	}

	public void setApplicationCountryName(String applicationCountryName) {
		this.applicationCountryName = applicationCountryName;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public String getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(String currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public String getAppCountry() {
		return appCountry;
	}

	public void setAppCountry(String appCountry) {
		this.appCountry = appCountry;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ResourceBundle getProps() {
		return props;
	}

	public void setProps(ResourceBundle props) {
		this.props = props;
	}

	public FacesContext getfCtx() {
		return fCtx;
	}

	public void setfCtx(FacesContext fCtx) {
		this.fCtx = fCtx;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public BigDecimal getBeneCountryId() {
		return beneCountryId;
	}

	public void setBeneCountryId(BigDecimal beneCountryId) {
		this.beneCountryId = beneCountryId;
	}

	public Boolean getDisclaimerVisible() {
		return disclaimerVisible;
	}

	public void setDisclaimerVisible(Boolean disclaimerVisible) {
		this.disclaimerVisible = disclaimerVisible;
	}

	public Boolean getBeneOne() {
		return beneOne;
	}

	public void setBeneOne(Boolean beneOne) {
		this.beneOne = beneOne;
	}

	public Boolean getBeneMore() {
		return beneMore;
	}

	public void setBeneMore(Boolean beneMore) {
		this.beneMore = beneMore;
	}

	public Boolean getBankOne() {
		return bankOne;
	}

	public void setBankOne(Boolean bankOne) {
		this.bankOne = bankOne;
	}

	public Boolean getBankMore() {
		return bankMore;
	}

	public void setBankMore(Boolean bankMore) {
		this.bankMore = bankMore;
	}

	public Boolean getAccountOne() {
		return accountOne;
	}

	public void setAccountOne(Boolean accountOne) {
		this.accountOne = accountOne;
	}

	public Boolean getAccountMore() {
		return accountMore;
	}

	public void setAccountMore(Boolean accountMore) {
		this.accountMore = accountMore;
	}

	public String getBeneBankName() {
		return beneBankName;
	}

	public void setBeneBankName(String beneBankName) {
		this.beneBankName = beneBankName;
	}

	public String getCustomerIdExpDate() {
		return customerIdExpDate;
	}

	public void setCustomerIdExpDate(String customerIdExpDate) {
		this.customerIdExpDate = customerIdExpDate;
	}

	public String getDateOfBrithStr() {
		return dateOfBrithStr;
	}

	public void setDateOfBrithStr(String dateOfBrithStr) {
		this.dateOfBrithStr = dateOfBrithStr;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<PopulateData> getRoutingBankBranchlst() {
		return routingBankBranchlst;
	}

	public void setRoutingBankBranchlst(List<PopulateData> routingBankBranchlst) {
		this.routingBankBranchlst = routingBankBranchlst;
	}

	public List<PopulateData> getRoutingbankvalues() {
		return routingbankvalues;
	}

	public void setRoutingbankvalues(List<PopulateData> routingbankvalues) {
		this.routingbankvalues = routingbankvalues;
	}

	public List<PopulateData> getRoutingCountrylst() {
		return routingCountrylst;
	}

	public void setRoutingCountrylst(List<PopulateData> routingCountrylst) {
		this.routingCountrylst = routingCountrylst;
	}

	public List<PopulateData> getLstofCurrency() {
		return lstofCurrency;
	}

	public void setLstofCurrency(List<PopulateData> lstofCurrency) {
		this.lstofCurrency = lstofCurrency;
	}

	public List<PopulateData> getLstofRemittance() {
		return lstofRemittance;
	}

	public void setLstofRemittance(List<PopulateData> lstofRemittance) {
		this.lstofRemittance = lstofRemittance;
	}

	public List<PopulateData> getLstofDelivery() {
		return lstofDelivery;
	}

	public void setLstofDelivery(List<PopulateData> lstofDelivery) {
		this.lstofDelivery = lstofDelivery;
	}

	public List<PopulateData> getLstofCountry() {
		return lstofCountry;
	}

	public void setLstofCountry(List<PopulateData> lstofCountry) {
		this.lstofCountry = lstofCountry;
	}

	public IGeneralService<T> getGeneralService() {
		return generalService;
	}

	public void setGeneralService(IGeneralService<T> generalService) {
		this.generalService = generalService;
	}

	public BigDecimal getBeneCountr() {
		return beneCountr;
	}

	public void setBeneCountr(BigDecimal beneCountr) {
		this.beneCountr = beneCountr;
	}

	public BigDecimal getDocumentId() {
		return documentId;
	}

	public void setDocumentId(BigDecimal documentId) {
		this.documentId = documentId;
	}

	public BigDecimal getDocumentcode() {
		return documentcode;
	}

	public void setDocumentcode(BigDecimal documentcode) {
		this.documentcode = documentcode;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getDocumentdesc() {
		return documentdesc;
	}

	public void setDocumentdesc(String documentdesc) {
		this.documentdesc = documentdesc;
	}

	

	public void setFinaceYear(BigDecimal finaceYear) {
		this.finaceYear = finaceYear;
	}

	public BigDecimal getFinaceYearId() {
		return finaceYearId;
	}

	public void setFinaceYearId(BigDecimal finaceYearId) {
		this.finaceYearId = finaceYearId;
	}

	public BigDecimal getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(BigDecimal serviceCode) {
		this.serviceCode = serviceCode;
	}

	public BigDecimal getRoutingCountry() {
		return routingCountry;
	}

	public void setRoutingCountry(BigDecimal routingCountry) {
		this.routingCountry = routingCountry;
	}

	public String getRoutingCountryName() {
		return routingCountryName;
	}

	public void setRoutingCountryName(String routingCountryName) {
		this.routingCountryName = routingCountryName;
	}

	public BigDecimal getRoutingBank() {
		return routingBank;
	}

	public void setRoutingBank(BigDecimal routingBank) {
		this.routingBank = routingBank;
	}

	public String getRoutingBankName() {
		return routingBankName;
	}

	public void setRoutingBankName(String routingBankName) {
		this.routingBankName = routingBankName;
	}

	public BigDecimal getRoutingBranch() {
		return routingBranch;
	}

	public void setRoutingBranch(BigDecimal routingBranch) {
		this.routingBranch = routingBranch;
	}

	public String getRoutingBranchName() {
		return routingBranchName;
	}

	public void setRoutingBranchName(String routingBranchName) {
		this.routingBranchName = routingBranchName;
	}

	public BigDecimal getRemitMode() {
		return remitMode;
	}

	public void setRemitMode(BigDecimal remitMode) {
		this.remitMode = remitMode;
	}

	public String getRemittanceName() {
		return remittanceName;
	}

	public void setRemittanceName(String remittanceName) {
		this.remittanceName = remittanceName;
	}

	public BigDecimal getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(BigDecimal deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getDeliveryModeInput() {
		return deliveryModeInput;
	}

	public void setDeliveryModeInput(String deliveryModeInput) {
		this.deliveryModeInput = deliveryModeInput;
	}

	public BigDecimal getCurrency() {
		return currency;
	}

	public void setCurrency(BigDecimal currency) {
		this.currency = currency;
	}

	public String getSpecialRateRef() {
		return specialRateRef;
	}

	public void setSpecialRateRef(String specialRateRef) {
		this.specialRateRef = specialRateRef;
	}

	public String getChargesOverseas() {
		return chargesOverseas;
	}

	public void setChargesOverseas(String chargesOverseas) {
		this.chargesOverseas = chargesOverseas;
	}

	public BigDecimal getForiegnCurrency() {
		return foriegnCurrency;
	}

	public void setForiegnCurrency(BigDecimal foriegnCurrency) {
		this.foriegnCurrency = foriegnCurrency;
	}

	public String getAvailLoyaltyPoints() {
		return availLoyaltyPoints;
	}

	public void setAvailLoyaltyPoints(String availLoyaltyPoints) {
		this.availLoyaltyPoints = availLoyaltyPoints;
	}

	public String getSpotRate() {
		return spotRate;
	}

	public void setSpotRate(String spotRate) {
		this.spotRate = spotRate;
	}

	public List<BenificiaryListView> getBeneNameList() {
		return beneNameList;
	}

	public void setBeneNameList(List<BenificiaryListView> beneNameList) {
		this.beneNameList = beneNameList;
	}

	public List<BenificiaryListView> getBeneBankList() {
		return beneBankList;
	}

	public void setBeneBankList(List<BenificiaryListView> beneBankList) {
		this.beneBankList = beneBankList;
	}

	public List<BenificiaryListView> getBeneAccountList() {
		return beneAccountList;
	}

	public void setBeneAccountList(List<BenificiaryListView> beneAccountList) {
		this.beneAccountList = beneAccountList;
	}

	public List<PopulateData> getLstBeneName() {
		return lstBeneName;
	}

	public void setLstBeneName(List<PopulateData> lstBeneName) {
		this.lstBeneName = lstBeneName;
	}

	public List<PopulateData> getLstBank() {
		return lstBank;
	}

	public void setLstBank(List<PopulateData> lstBank) {
		this.lstBank = lstBank;
	}

	public List<PopulateData> getLstAccount() {
		return lstAccount;
	}

	public void setLstAccount(List<PopulateData> lstAccount) {
		this.lstAccount = lstAccount;
	}

	public void setBeneCountryList(List<BenificiaryListView> beneCountryList) {
		this.beneCountryList = beneCountryList;
	}

	public BigDecimal getDataCustomerContactId() {
		return dataCustomerContactId;
	}

	public void setDataCustomerContactId(BigDecimal dataCustomerContactId) {
		this.dataCustomerContactId = dataCustomerContactId;
	}

	public String getDataCustomerTelephoneNumber() {
		return dataCustomerTelephoneNumber;
	}

	public void setDataCustomerTelephoneNumber(String dataCustomerTelephoneNumber) {
		this.dataCustomerTelephoneNumber = dataCustomerTelephoneNumber;
	}

	public Boolean getBooRenderCustTelMandatory() {
		return booRenderCustTelMandatory;
	}

	public void setBooRenderCustTelMandatory(Boolean booRenderCustTelMandatory) {
		this.booRenderCustTelMandatory = booRenderCustTelMandatory;
	}

	public Boolean getBooRenderCustTelDisable() {
		return booRenderCustTelDisable;
	}

	public void setBooRenderCustTelDisable(Boolean booRenderCustTelDisable) {
		this.booRenderCustTelDisable = booRenderCustTelDisable;
	}

	public String getDataTempCustomerMobile() {
		return dataTempCustomerMobile;
	}

	public void setDataTempCustomerMobile(String dataTempCustomerMobile) {
		this.dataTempCustomerMobile = dataTempCustomerMobile;
	}

	public Boolean getBooRenderBeneTelDisable() {
		return booRenderBeneTelDisable;
	}

	public void setBooRenderBeneTelDisable(Boolean booRenderBeneTelDisable) {
		this.booRenderBeneTelDisable = booRenderBeneTelDisable;
	}

	public Boolean getBooRenderBeneTelMandatory() {
		return booRenderBeneTelMandatory;
	}

	public void setBooRenderBeneTelMandatory(Boolean booRenderBeneTelMandatory) {
		this.booRenderBeneTelMandatory = booRenderBeneTelMandatory;
	}

	public BigDecimal getDataBeneContactId() {
		return dataBeneContactId;
	}

	public void setDataBeneContactId(BigDecimal dataBeneContactId) {
		this.dataBeneContactId = dataBeneContactId;
	}

	public BigDecimal getDataTempBeneTelNum() {
		return dataTempBeneTelNum;
	}

	public void setDataTempBeneTelNum(BigDecimal dataTempBeneTelNum) {
		this.dataTempBeneTelNum = dataTempBeneTelNum;
	}

	public BigDecimal getDatabenificarycountry() {
		return databenificarycountry;
	}

	public void setDatabenificarycountry(BigDecimal databenificarycountry) {
		this.databenificarycountry = databenificarycountry;
	}

	public String getDatabenificarycountryname() {
		return databenificarycountryname;
	}

	public void setDatabenificarycountryname(String databenificarycountryname) {
		this.databenificarycountryname = databenificarycountryname;
	}

	public BigDecimal getDataBankbenificarycountry() {
		return dataBankbenificarycountry;
	}

	public void setDataBankbenificarycountry(BigDecimal dataBankbenificarycountry) {
		this.dataBankbenificarycountry = dataBankbenificarycountry;
	}

	public String getDataBankbenificarycountryname() {
		return dataBankbenificarycountryname;
	}

	public void setDataBankbenificarycountryname(String dataBankbenificarycountryname) {
		this.dataBankbenificarycountryname = dataBankbenificarycountryname;
	}

	public BigDecimal getDatabenificarycurrency() {
		return databenificarycurrency;
	}

	public void setDatabenificarycurrency(BigDecimal databenificarycurrency) {
		this.databenificarycurrency = databenificarycurrency;
	}

	public String getDatabenificarycurrencyname() {
		return databenificarycurrencyname;
	}

	public void setDatabenificarycurrencyname(String databenificarycurrencyname) {
		this.databenificarycurrencyname = databenificarycurrencyname;
	}

	public String getDatabenificaryservice() {
		return databenificaryservice;
	}

	public void setDatabenificaryservice(String databenificaryservice) {
		this.databenificaryservice = databenificaryservice;
	}

	public BigDecimal getDatabenificarydelivery() {
		return databenificarydelivery;
	}

	public void setDatabenificarydelivery(BigDecimal databenificarydelivery) {
		this.databenificarydelivery = databenificarydelivery;
	}

	public BigDecimal getDataserviceid() {
		return dataserviceid;
	}

	public void setDataserviceid(BigDecimal dataserviceid) {
		this.dataserviceid = dataserviceid;
	}

	public String getDataAccountnum() {
		return dataAccountnum;
	}

	public void setDataAccountnum(String dataAccountnum) {
		this.dataAccountnum = dataAccountnum;
	}

	public String getDatabenificarybankname() {
		return databenificarybankname;
	}

	public void setDatabenificarybankname(String databenificarybankname) {
		this.databenificarybankname = databenificarybankname;
	}

	public String getDatabenificarybranchname() {
		return databenificarybranchname;
	}

	public void setDatabenificarybranchname(String databenificarybranchname) {
		this.databenificarybranchname = databenificarybranchname;
	}

	public String getDatabenificaryname() {
		return databenificaryname;
	}

	public void setDatabenificaryname(String databenificaryname) {
		this.databenificaryname = databenificaryname;
	}

	public String getDatabenificaryservicegroup() {
		return databenificaryservicegroup;
	}

	public void setDatabenificaryservicegroup(String databenificaryservicegroup) {
		this.databenificaryservicegroup = databenificaryservicegroup;
	}

	public BigDecimal getDataservicegroupid() {
		return dataservicegroupid;
	}

	public void setDataservicegroupid(BigDecimal dataservicegroupid) {
		this.dataservicegroupid = dataservicegroupid;
	}

	public BigDecimal getDataservicemasterid() {
		return dataservicemasterid;
	}

	public void setDataservicemasterid(BigDecimal dataservicemasterid) {
		this.dataservicemasterid = dataservicemasterid;
	}

	public String getBenificarystatus() {
		return benificarystatus;
	}

	public void setBenificarystatus(String benificarystatus) {
		this.benificarystatus = benificarystatus;
	}

	public String getBenificiaryryNameRemittance() {
		return benificiaryryNameRemittance;
	}

	public void setBenificiaryryNameRemittance(String benificiaryryNameRemittance) {
		this.benificiaryryNameRemittance = benificiaryryNameRemittance;
	}

	public BigDecimal getMasterId() {
		return masterId;
	}

	public void setMasterId(BigDecimal masterId) {
		this.masterId = masterId;
	}

	public BigDecimal getBeneficaryBankId() {
		return beneficaryBankId;
	}

	public void setBeneficaryBankId(BigDecimal beneficaryBankId) {
		this.beneficaryBankId = beneficaryBankId;
	}

	public BigDecimal getBeneficaryBankBranchId() {
		return beneficaryBankBranchId;
	}

	public void setBeneficaryBankBranchId(BigDecimal beneficaryBankBranchId) {
		this.beneficaryBankBranchId = beneficaryBankBranchId;
	}

	public String getBenificaryTelephone() {
		return benificaryTelephone;
	}

	public void setBenificaryTelephone(String benificaryTelephone) {
		this.benificaryTelephone = benificaryTelephone;
	}

	public BigDecimal getBeneficaryStatusId() {
		return beneficaryStatusId;
	}

	public void setBeneficaryStatusId(BigDecimal beneficaryStatusId) {
		this.beneficaryStatusId = beneficaryStatusId;
	}

	public String getProcedureError() {
		return procedureError;
	}

	public void setProcedureError(String procedureError) {
		this.procedureError = procedureError;
	}

	public BigDecimal getBeneficiaryAccountSeqId() {
		return beneficiaryAccountSeqId;
	}

	public void setBeneficiaryAccountSeqId(BigDecimal beneficiaryAccountSeqId) {
		this.beneficiaryAccountSeqId = beneficiaryAccountSeqId;
	}

	public BigDecimal getBeneficiaryRelationShipSeqId() {
		return beneficiaryRelationShipSeqId;
	}

	public void setBeneficiaryRelationShipSeqId(BigDecimal beneficiaryRelationShipSeqId) {
		this.beneficiaryRelationShipSeqId = beneficiaryRelationShipSeqId;
	}

	public BigDecimal getAmountToRemit() {
		return amountToRemit;
	}

	public void setAmountToRemit(BigDecimal amountToRemit) {
		this.amountToRemit = amountToRemit;
	}

	public boolean isAdditionalCheck() {
		return additionalCheck;
	}

	public void setAdditionalCheck(boolean additionalCheck) {
		this.additionalCheck = additionalCheck;
	}

	public String getExceptionMessageForReport() {
		return exceptionMessageForReport;
	}

	public void setExceptionMessageForReport(String exceptionMessageForReport) {
		this.exceptionMessageForReport = exceptionMessageForReport;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getOverseasamt() {
		return Overseasamt;
	}

	public void setOverseasamt(BigDecimal overseasamt) {
		Overseasamt = overseasamt;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getGrossAmountCalculated() {
		return grossAmountCalculated;
	}

	public void setGrossAmountCalculated(BigDecimal grossAmountCalculated) {
		this.grossAmountCalculated = grossAmountCalculated;
	}

	public BigDecimal getLoyaltyAmountAvailed() {
		return loyaltyAmountAvailed;
	}

	public void setLoyaltyAmountAvailed(BigDecimal loyaltyAmountAvailed) {
		this.loyaltyAmountAvailed = loyaltyAmountAvailed;
	}

	public BigDecimal getNetAmountPayable() {
		return netAmountPayable;
	}

	public void setNetAmountPayable(BigDecimal netAmountPayable) {
		this.netAmountPayable = netAmountPayable;
	}

	public BigDecimal getNetAmountSent() {
		return netAmountSent;
	}

	public void setNetAmountSent(BigDecimal netAmountSent) {
		this.netAmountSent = netAmountSent;
	}

	public BigDecimal getNewRemittanceModeId() {
		return newRemittanceModeId;
	}

	public void setNewRemittanceModeId(BigDecimal newRemittanceModeId) {
		this.newRemittanceModeId = newRemittanceModeId;
	}

	public String getNewRemittanceModeName() {
		return newRemittanceModeName;
	}

	public void setNewRemittanceModeName(String newRemittanceModeName) {
		this.newRemittanceModeName = newRemittanceModeName;
	}

	public boolean isBooRenderRemit() {
		return booRenderRemit;
	}

	public void setBooRenderRemit(boolean booRenderRemit) {
		this.booRenderRemit = booRenderRemit;
	}

	public BigDecimal getNewDeliveryModeId() {
		return newDeliveryModeId;
	}

	public void setNewDeliveryModeId(BigDecimal newDeliveryModeId) {
		this.newDeliveryModeId = newDeliveryModeId;
	}

	public String getNewDeliveryModeName() {
		return newDeliveryModeName;
	}

	public void setNewDeliveryModeName(String newDeliveryModeName) {
		this.newDeliveryModeName = newDeliveryModeName;
	}

	public boolean isBooRenderDelivery() {
		return booRenderDelivery;
	}

	public void setBooRenderDelivery(boolean booRenderDelivery) {
		this.booRenderDelivery = booRenderDelivery;
	}
	
	public String getSwiftBic() {
		return swiftBic;
	}

	public void setSwiftBic(String swiftBic) {
		this.swiftBic = swiftBic;
	}

	public String getBeneSwiftBank1() {
		return beneSwiftBank1;
	}

	public void setBeneSwiftBank1(String beneSwiftBank1) {
		this.beneSwiftBank1 = beneSwiftBank1;
	}

	public String getBeneSwiftBank2() {
		return beneSwiftBank2;
	}

	public void setBeneSwiftBank2(String beneSwiftBank2) {
		this.beneSwiftBank2 = beneSwiftBank2;
	}

	public String getBeneSwiftBankAddr1() {
		return beneSwiftBankAddr1;
	}

	public void setBeneSwiftBankAddr1(String beneSwiftBankAddr1) {
		this.beneSwiftBankAddr1 = beneSwiftBankAddr1;
	}

	public String getBeneSwiftBankAddr2() {
		return beneSwiftBankAddr2;
	}

	public void setBeneSwiftBankAddr2(String beneSwiftBankAddr2) {
		this.beneSwiftBankAddr2 = beneSwiftBankAddr2;
	}

	public BigDecimal getBeneStateId() {
		return beneStateId;
	}

	public void setBeneStateId(BigDecimal beneStateId) {
		this.beneStateId = beneStateId;
	}

	public BigDecimal getBeneDistrictId() {
		return beneDistrictId;
	}

	public void setBeneDistrictId(BigDecimal beneDistrictId) {
		this.beneDistrictId = beneDistrictId;
	}

	public BigDecimal getBeneCityId() {
		return beneCityId;
	}

	public void setBeneCityId(BigDecimal beneCityId) {
		this.beneCityId = beneCityId;
	}

	public String getPbeneFullName() {
		return PbeneFullName;
	}

	public void setPbeneFullName(String pbeneFullName) {
		PbeneFullName = pbeneFullName;
	}

	public String getPbeneFirstName() {
		return PbeneFirstName;
	}

	public void setPbeneFirstName(String pbeneFirstName) {
		PbeneFirstName = pbeneFirstName;
	}

	public String getPbeneSecondName() {
		return PbeneSecondName;
	}

	public void setPbeneSecondName(String pbeneSecondName) {
		PbeneSecondName = pbeneSecondName;
	}

	public String getPbeneThirdName() {
		return PbeneThirdName;
	}

	public void setPbeneThirdName(String pbeneThirdName) {
		PbeneThirdName = pbeneThirdName;
	}

	public String getPbeneFourthName() {
		return PbeneFourthName;
	}

	public void setPbeneFourthName(String pbeneFourthName) {
		PbeneFourthName = pbeneFourthName;
	}

	public String getPbeneFifthName() {
		return PbeneFifthName;
	}

	public void setPbeneFifthName(String pbeneFifthName) {
		PbeneFifthName = pbeneFifthName;
	}

	public boolean isBooRenderInstructions() {
		return booRenderInstructions;
	}

	public void setBooRenderInstructions(boolean booRenderInstructions) {
		this.booRenderInstructions = booRenderInstructions;
	}

	public boolean isBooRenderSwiftBank1() {
		return booRenderSwiftBank1;
	}

	public void setBooRenderSwiftBank1(boolean booRenderSwiftBank1) {
		this.booRenderSwiftBank1 = booRenderSwiftBank1;
	}

	public boolean isBooRenderSwiftBank2() {
		return booRenderSwiftBank2;
	}

	public void setBooRenderSwiftBank2(boolean booRenderSwiftBank2) {
		this.booRenderSwiftBank2 = booRenderSwiftBank2;
	}

	public String getFurthuerInstructions() {
		return furthuerInstructions;
	}

	public void setFurthuerInstructions(String furthuerInstructions) {
		this.furthuerInstructions = furthuerInstructions;
	}

	public List<PersonalRemmitanceBeneficaryDataTable> getCoustomerBeneficaryDTList() {
		return coustomerBeneficaryDTList;
	}

	public void setCoustomerBeneficaryDTList(List<PersonalRemmitanceBeneficaryDataTable> coustomerBeneficaryDTList) {
		this.coustomerBeneficaryDTList = coustomerBeneficaryDTList;
	}

	public String getServiceGroupCode() {
		return serviceGroupCode;
	}

	public void setServiceGroupCode(String serviceGroupCode) {
		this.serviceGroupCode = serviceGroupCode;
	}

	public BigDecimal getSpecialDealRate() {
		return specialDealRate;
	}

	public void setSpecialDealRate(BigDecimal specialDealRate) {
		this.specialDealRate = specialDealRate;
	}

	public BigDecimal getSpotExchangeRate() {
		return spotExchangeRate;
	}

	public void setSpotExchangeRate(BigDecimal spotExchangeRate) {
		this.spotExchangeRate = spotExchangeRate;
	}

	public BigDecimal getSpotExchangeRatePk() {
		return spotExchangeRatePk;
	}

	public void setSpotExchangeRatePk(BigDecimal spotExchangeRatePk) {
		this.spotExchangeRatePk = spotExchangeRatePk;
	}

	public String getCashRounding() {
		return cashRounding;
	}

	public void setCashRounding(String cashRounding) {
		this.cashRounding = cashRounding;
	}

	public BigDecimal getApprovalYear() {
		return approvalYear;
	}

	public void setApprovalYear(BigDecimal approvalYear) {
		this.approvalYear = approvalYear;
	}

	public BigDecimal getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(BigDecimal approvalNo) {
		this.approvalNo = approvalNo;
	}

	public BigDecimal getEquivalentRemitAmount() {
		return equivalentRemitAmount;
	}

	public void setEquivalentRemitAmount(BigDecimal equivalentRemitAmount) {
		this.equivalentRemitAmount = equivalentRemitAmount;
	}

	public String getEquivalentCurrency() {
		return equivalentCurrency;
	}

	public void setEquivalentCurrency(String equivalentCurrency) {
		this.equivalentCurrency = equivalentCurrency;
	}

	public ForeignLocalCurrencyDenominationService<T> getForeignLocalCurrencyDenominationService() {
		return foreignLocalCurrencyDenominationService;
	}

	public void setForeignLocalCurrencyDenominationService(ForeignLocalCurrencyDenominationService<T> foreignLocalCurrencyDenominationService) {
		this.foreignLocalCurrencyDenominationService = foreignLocalCurrencyDenominationService;
	}

	public List<SourceOfIncomeDescription> getLstSourceofIncome() {
		return lstSourceofIncome;
	}

	public void setLstSourceofIncome(List<SourceOfIncomeDescription> lstSourceofIncome) {
		this.lstSourceofIncome = lstSourceofIncome;
	}

	public List<PurposeOfTransaction> getLstPurposeOfTransaction() {
		return lstPurposeOfTransaction;
	}

	public void setLstPurposeOfTransaction(List<PurposeOfTransaction> lstPurposeOfTransaction) {
		this.lstPurposeOfTransaction = lstPurposeOfTransaction;
	}

	public List<SourceOfIncomeDescription> getLstSourceOfIncome() {
		return lstSourceOfIncome;
	}

	public void setLstSourceOfIncome(List<SourceOfIncomeDescription> lstSourceOfIncome) {
		this.lstSourceOfIncome = lstSourceOfIncome;
	}

	public IForeignCurrencyPurchaseService<T> getForeignCurrencyPurchaseService() {
		return foreignCurrencyPurchaseService;
	}

	public void setForeignCurrencyPurchaseService(IForeignCurrencyPurchaseService<T> foreignCurrencyPurchaseService) {
		this.foreignCurrencyPurchaseService = foreignCurrencyPurchaseService;
	}

	public List<PopulateData> getLstSwiftMasterBank1() {
		return lstSwiftMasterBank1;
	}

	public void setLstSwiftMasterBank1(List<PopulateData> lstSwiftMasterBank1) {
		this.lstSwiftMasterBank1 = lstSwiftMasterBank1;
	}

	public List<PopulateData> getLstSwiftMasterBank2() {
		return lstSwiftMasterBank2;
	}

	public void setLstSwiftMasterBank2(List<PopulateData> lstSwiftMasterBank2) {
		this.lstSwiftMasterBank2 = lstSwiftMasterBank2;
	}

	public BigDecimal getSourceOfIncome() {
		return sourceOfIncome;
	}

	public void setSourceOfIncome(BigDecimal sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}

	public Boolean getBooRenderDataTablePanel() {
		return booRenderDataTablePanel;
	}

	public void setBooRenderDataTablePanel(Boolean booRenderDataTablePanel) {
		this.booRenderDataTablePanel = booRenderDataTablePanel;
	}

	public Boolean getBooRenderTransferFundPanel() {
		return booRenderTransferFundPanel;
	}

	public void setBooRenderTransferFundPanel(Boolean booRenderTransferFundPanel) {
		this.booRenderTransferFundPanel = booRenderTransferFundPanel;
	}

	public Boolean getBooRenderAdditionalDataPanel() {
		return booRenderAdditionalDataPanel;
	}

	public void setBooRenderAdditionalDataPanel(Boolean booRenderAdditionalDataPanel) {
		this.booRenderAdditionalDataPanel = booRenderAdditionalDataPanel;
	}

	public Boolean getBooRenderDebitCardPanel() {
		return booRenderDebitCardPanel;
	}

	public void setBooRenderDebitCardPanel(Boolean booRenderDebitCardPanel) {
		this.booRenderDebitCardPanel = booRenderDebitCardPanel;
	}

	public boolean isBooSingleRoutingCountry() {
		return booSingleRoutingCountry;
	}

	public void setBooSingleRoutingCountry(boolean booSingleRoutingCountry) {
		this.booSingleRoutingCountry = booSingleRoutingCountry;
	}

	public boolean isBooMultipleRoutingCountry() {
		return booMultipleRoutingCountry;
	}

	public void setBooMultipleRoutingCountry(boolean booMultipleRoutingCountry) {
		this.booMultipleRoutingCountry = booMultipleRoutingCountry;
	}

	public boolean isBooReadOnlyRemitAmount() {
		return booReadOnlyRemitAmount;
	}

	public void setBooReadOnlyRemitAmount(boolean booReadOnlyRemitAmount) {
		this.booReadOnlyRemitAmount = booReadOnlyRemitAmount;
	}

	public boolean isBooSingleService() {
		return booSingleService;
	}

	public void setBooSingleService(boolean booSingleService) {
		this.booSingleService = booSingleService;
	}

	public boolean isBooMultipleService() {
		return booMultipleService;
	}

	public void setBooMultipleService(boolean booMultipleService) {
		this.booMultipleService = booMultipleService;
	}

	public boolean isBooRenderRouting() {
		return booRenderRouting;
	}

	public void setBooRenderRouting(boolean booRenderRouting) {
		this.booRenderRouting = booRenderRouting;
	}

	public boolean isBooRenderAgent() {
		return booRenderAgent;
	}

	public void setBooRenderAgent(boolean booRenderAgent) {
		this.booRenderAgent = booRenderAgent;
	}

	public boolean isBooSingleRoutingBank() {
		return booSingleRoutingBank;
	}

	public void setBooSingleRoutingBank(boolean booSingleRoutingBank) {
		this.booSingleRoutingBank = booSingleRoutingBank;
	}

	public boolean isBooMultipleRoutingBank() {
		return booMultipleRoutingBank;
	}

	public void setBooMultipleRoutingBank(boolean booMultipleRoutingBank) {
		this.booMultipleRoutingBank = booMultipleRoutingBank;
	}

	public boolean isBooSingleRoutingBranch() {
		return booSingleRoutingBranch;
	}

	public void setBooSingleRoutingBranch(boolean booSingleRoutingBranch) {
		this.booSingleRoutingBranch = booSingleRoutingBranch;
	}

	public boolean isBooMultipleRoutingBranch() {
		return booMultipleRoutingBranch;
	}

	public void setBooMultipleRoutingBranch(boolean booMultipleRoutingBranch) {
		this.booMultipleRoutingBranch = booMultipleRoutingBranch;
	}

	public boolean isBooRenderDeliveryModeInputPanel() {
		return booRenderDeliveryModeInputPanel;
	}

	public void setBooRenderDeliveryModeInputPanel(boolean booRenderDeliveryModeInputPanel) {
		this.booRenderDeliveryModeInputPanel = booRenderDeliveryModeInputPanel;
	}

	public boolean isBooRenderDeliveryModeDDPanel() {
		return booRenderDeliveryModeDDPanel;
	}

	public void setBooRenderDeliveryModeDDPanel(boolean booRenderDeliveryModeDDPanel) {
		this.booRenderDeliveryModeDDPanel = booRenderDeliveryModeDDPanel;
	}

	public boolean isBooSingleRemit() {
		return booSingleRemit;
	}

	public void setBooSingleRemit(boolean booSingleRemit) {
		this.booSingleRemit = booSingleRemit;
	}

	public boolean isBooMultipleRemit() {
		return booMultipleRemit;
	}

	public void setBooMultipleRemit(boolean booMultipleRemit) {
		this.booMultipleRemit = booMultipleRemit;
	}

	public boolean isDisableSpotRatePanel() {
		return disableSpotRatePanel;
	}

	public void setDisableSpotRatePanel(boolean disableSpotRatePanel) {
		this.disableSpotRatePanel = disableSpotRatePanel;
	}

	public boolean isMarqueeRender() {
		return marqueeRender;
	}

	public void setMarqueeRender(boolean marqueeRender) {
		this.marqueeRender = marqueeRender;
	}

	public boolean isBooSpecialCusFCCalDataTable() {
		return booSpecialCusFCCalDataTable;
	}

	public void setBooSpecialCusFCCalDataTable(boolean booSpecialCusFCCalDataTable) {
		this.booSpecialCusFCCalDataTable = booSpecialCusFCCalDataTable;
	}

	public boolean isBooRenderRemittanceServicePanel() {
		return booRenderRemittanceServicePanel;
	}

	public void setBooRenderRemittanceServicePanel(boolean booRenderRemittanceServicePanel) {
		this.booRenderRemittanceServicePanel = booRenderRemittanceServicePanel;
	}

	public boolean isIcashStateSubAgent() {
		return icashStateSubAgent;
	}

	public void setIcashStateSubAgent(boolean icashStateSubAgent) {
		this.icashStateSubAgent = icashStateSubAgent;
	}

	public boolean isIcashAgentPanel() {
		return icashAgentPanel;
	}

	public void setIcashAgentPanel(boolean icashAgentPanel) {
		this.icashAgentPanel = icashAgentPanel;
	}

	public boolean isIcashEFT() {
		return icashEFT;
	}

	public void setIcashEFT(boolean icashEFT) {
		this.icashEFT = icashEFT;
	}

	public boolean isIcashTT() {
		return icashTT;
	}

	public void setIcashTT(boolean icashTT) {
		this.icashTT = icashTT;
	}

	public String getIcashAgent() {
		return icashAgent;
	}

	public void setIcashAgent(String icashAgent) {
		this.icashAgent = icashAgent;
	}

	public String getIcashState() {
		return icashState;
	}

	public void setIcashState(String icashState) {
		this.icashState = icashState;
	}

	public String getIcashSubAgent() {
		return icashSubAgent;
	}

	public void setIcashSubAgent(String icashSubAgent) {
		this.icashSubAgent = icashSubAgent;
	}

	public BigDecimal getIcashCostRate() {
		return icashCostRate;
	}

	public void setIcashCostRate(BigDecimal icashCostRate) {
		this.icashCostRate = icashCostRate;
	}

	public List<AddDynamicLabel> getListDynamicLebel() {
		return listDynamicLebel;
	}

	public void setListDynamicLebel(List<AddDynamicLabel> listDynamicLebel) {
		this.listDynamicLebel = listDynamicLebel;
	}

	public List<AddAdditionalBankData> getListAdditionalBankDataTable() {
		return listAdditionalBankDataTable;
	}

	public void setListAdditionalBankDataTable(List<AddAdditionalBankData> listAdditionalBankDataTable) {
		this.listAdditionalBankDataTable = listAdditionalBankDataTable;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public IBeneficaryCreation getBeneficaryCreation() {
		return beneficaryCreation;
	}

	public void setBeneficaryCreation(IBeneficaryCreation beneficaryCreation) {
		this.beneficaryCreation = beneficaryCreation;
	}

	public IServiceGroupMasterService getiServiceGroupMasterService() {
		return iServiceGroupMasterService;
	}

	public void setiServiceGroupMasterService(IServiceGroupMasterService iServiceGroupMasterService) {
		this.iServiceGroupMasterService = iServiceGroupMasterService;
	}

	public IPersonalRemittanceService getiPersonalRemittanceService() {
		return iPersonalRemittanceService;
	}

	public void setiPersonalRemittanceService(IPersonalRemittanceService iPersonalRemittanceService) {
		this.iPersonalRemittanceService = iPersonalRemittanceService;
	}

	public List<BeneficaryMaster> getBeneficiaryMaster() {
		return beneficiaryMaster;
	}

	public void setBeneficiaryMaster(List<BeneficaryMaster> beneficiaryMaster) {
		this.beneficiaryMaster = beneficiaryMaster;
	}

	public List<BeneficaryContact> getBeneficiaryTel() {
		return beneficiaryTel;
	}

	public void setBeneficiaryTel(List<BeneficaryContact> beneficiaryTel) {
		this.beneficiaryTel = beneficiaryTel;
	}

	public List<PopulateData> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<PopulateData> serviceList) {
		this.serviceList = serviceList;
	}

	public String getDatabenificarycountryalphacode() {
		return databenificarycountryalphacode;
	}

	public void setDatabenificarycountryalphacode(String databenificarycountryalphacode) {
		this.databenificarycountryalphacode = databenificarycountryalphacode;
	}

	public String getDatabenificarybankalphacode() {
		return databenificarybankalphacode;
	}

	public void setDatabenificarybankalphacode(String databenificarybankalphacode) {
		this.databenificarybankalphacode = databenificarybankalphacode;
	}

	public String getDataroutingcountryalphacode() {
		return dataroutingcountryalphacode;
	}

	public void setDataroutingcountryalphacode(String dataroutingcountryalphacode) {
		this.dataroutingcountryalphacode = dataroutingcountryalphacode;
	}

	public String getDataroutingbankalphacode() {
		return dataroutingbankalphacode;
	}

	public void setDataroutingbankalphacode(String dataroutingbankalphacode) {
		this.dataroutingbankalphacode = dataroutingbankalphacode;
	}

	public String getDatabenificarycurrencyalphacode() {
		return databenificarycurrencyalphacode;
	}

	public void setDatabenificarycurrencyalphacode(String databenificarycurrencyalphacode) {
		this.databenificarycurrencyalphacode = databenificarycurrencyalphacode;
	}

	public String getDatabenificaryservicecode() {
		return databenificaryservicecode;
	}

	public void setDatabenificaryservicecode(String databenificaryservicecode) {
		this.databenificaryservicecode = databenificaryservicecode;
	}

	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}

	public int getSaveCount() {
		return saveCount;
	}

	public void setSaveCount(int saveCount) {
		this.saveCount = saveCount;
	}

	public Boolean getCheckProExp() {
		return checkProExp;
	}

	public void setCheckProExp(Boolean checkProExp) {
		this.checkProExp = checkProExp;
	}

	public List<ShoppingCartDataTableBean> getShoppingcartDTList() {
		return shoppingcartDTList;
	}

	public void setShoppingcartDTList(List<ShoppingCartDataTableBean> shoppingcartDTList) {
		this.shoppingcartDTList = shoppingcartDTList;
	}

	public boolean isBoorenderlastpanel() {
		return boorenderlastpanel;
	}

	public void setBoorenderlastpanel(boolean boorenderlastpanel) {
		this.boorenderlastpanel = boorenderlastpanel;
	}

	public BigDecimal getCalGrossAmount() {
		return calGrossAmount;
	}

	public void setCalGrossAmount(BigDecimal calGrossAmount) {
		this.calGrossAmount = calGrossAmount;
	}

	public BigDecimal getCalNetAmountPaid() {
		return calNetAmountPaid;
	}

	public void setCalNetAmountPaid(BigDecimal calNetAmountPaid) {
		this.calNetAmountPaid = calNetAmountPaid;
	}

	public String getActionShopping() {
		return actionShopping;
	}

	public void setActionShopping(String actionShopping) {
		this.actionShopping = actionShopping;
	}

	public String getDebitCard1() {
		return debitCard1;
	}

	public void setDebitCard1(String debitCard1) {
		this.debitCard1 = debitCard1;
	}

	public String getDebitCard2() {
		return debitCard2;
	}

	public void setDebitCard2(String debitCard2) {
		this.debitCard2 = debitCard2;
	}

	public String getDebitCard3() {
		return debitCard3;
	}

	public void setDebitCard3(String debitCard3) {
		this.debitCard3 = debitCard3;
	}

	public String getDebitCard4() {
		return debitCard4;
	}

	public void setDebitCard4(String debitCard4) {
		this.debitCard4 = debitCard4;
	}

	public String getCardErrorMsg() {
		return cardErrorMsg;
	}

	public void setCardErrorMsg(String cardErrorMsg) {
		this.cardErrorMsg = cardErrorMsg;
	}

	public Boolean getBooRendercardErrorMsg() {
		return booRendercardErrorMsg;
	}

	public void setBooRendercardErrorMsg(Boolean booRendercardErrorMsg) {
		this.booRendercardErrorMsg = booRendercardErrorMsg;
	}

	public List<CustomerDBCardDetailsView> getLstofDBCards() {
		return lstofDBCards;
	}

	public void setLstofDBCards(List<CustomerDBCardDetailsView> lstofDBCards) {
		this.lstofDBCards = lstofDBCards;
	}

	public BigDecimal getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(BigDecimal documentNumber) {
		this.documentNumber = documentNumber;
	}

	public BigDecimal getTotalKnetAmount() {
		return totalKnetAmount;
	}

	public void setTotalKnetAmount(BigDecimal totalKnetAmount) {
		this.totalKnetAmount = totalKnetAmount;
	}

	public String getKnetTransErrorMessage() {
		return knetTransErrorMessage;
	}

	public void setKnetTransErrorMessage(String knetTransErrorMessage) {
		this.knetTransErrorMessage = knetTransErrorMessage;
	}

	public String getKnetTransErrorContactMessage() {
		return knetTransErrorContactMessage;
	}

	public void setKnetTransErrorContactMessage(String knetTransErrorContactMessage) {
		this.knetTransErrorContactMessage = knetTransErrorContactMessage;
	}

	public String getKnetSuccessPage() {
		return knetSuccessPage;
	}

	public void setKnetSuccessPage(String knetSuccessPage) {
		this.knetSuccessPage = knetSuccessPage;
	}

	public List<KnetSuccessPageKnetDetailsDataTable> getLstKnetDetails() {
		return lstKnetDetails;
	}

	public void setLstKnetDetails(List<KnetSuccessPageKnetDetailsDataTable> lstKnetDetails) {
		this.lstKnetDetails = lstKnetDetails;
	}

	public List<KnetSuccessPageCustomerDataTable> getLstknetCustomer() {
		return lstknetCustomer;
	}

	public void setLstknetCustomer(List<KnetSuccessPageCustomerDataTable> lstknetCustomer) {
		this.lstknetCustomer = lstknetCustomer;
	}

	public List<KnetSuccessPageKnetTransactionDataTable> getLstKnetTransDetails() {
		return lstKnetTransDetails;
	}

	public void setLstKnetTransDetails(List<KnetSuccessPageKnetTransactionDataTable> lstKnetTransDetails) {
		this.lstKnetTransDetails = lstKnetTransDetails;
	}

	public BigDecimal getApplicationDocNum() {
		return applicationDocNum;
	}

	public void setApplicationDocNum(BigDecimal applicationDocNum) {
		this.applicationDocNum = applicationDocNum;
	}

	public BigDecimal getShoppingcartExchangeRate() {
		return shoppingcartExchangeRate;
	}

	public void setShoppingcartExchangeRate(BigDecimal shoppingcartExchangeRate) {
		this.shoppingcartExchangeRate = shoppingcartExchangeRate;
	}

	public boolean isBooRenderMultiDocNum() {
		return booRenderMultiDocNum;
	}

	public void setBooRenderMultiDocNum(boolean booRenderMultiDocNum) {
		this.booRenderMultiDocNum = booRenderMultiDocNum;
	}

	public boolean isBooRenderSingleDocNum() {
		return booRenderSingleDocNum;
	}

	public void setBooRenderSingleDocNum(boolean booRenderSingleDocNum) {
		this.booRenderSingleDocNum = booRenderSingleDocNum;
	}

	public BigDecimal getDummiTotalNetAmount() {
		return dummiTotalNetAmount;
	}

	public void setDummiTotalNetAmount(BigDecimal dummiTotalNetAmount) {
		this.dummiTotalNetAmount = dummiTotalNetAmount;
	}

	public BigDecimal getDummiTotalGrossAmount() {
		return dummiTotalGrossAmount;
	}

	public void setDummiTotalGrossAmount(BigDecimal dummiTotalGrossAmount) {
		this.dummiTotalGrossAmount = dummiTotalGrossAmount;
	}

	public boolean isBooShowCashRoundingPanel() {
		return booShowCashRoundingPanel;
	}

	public void setBooShowCashRoundingPanel(boolean booShowCashRoundingPanel) {
		this.booShowCashRoundingPanel = booShowCashRoundingPanel;
	}

	public boolean isBooRenderModifiedRoundData() {
		return booRenderModifiedRoundData;
	}

	public void setBooRenderModifiedRoundData(boolean booRenderModifiedRoundData) {
		this.booRenderModifiedRoundData = booRenderModifiedRoundData;
	}

	public CopyOnWriteArrayList<ShoppingCartDataTableBean> getLstselectedrecord() {
		return lstselectedrecord;
	}

	public void setLstselectedrecord(CopyOnWriteArrayList<ShoppingCartDataTableBean> lstselectedrecord) {
		this.lstselectedrecord = lstselectedrecord;
	}

	public List<BenificiaryListView> getBeneCountryList() {
		return beneCountryList;
	}

	public BigDecimal getRemittanceNo() {
		return remittanceNo;
	}

	public void setRemittanceNo(BigDecimal remittanceNo) {
		this.remittanceNo = remittanceNo;
	}

	public BigDecimal getTempCalGrossAmount() {
		return tempCalGrossAmount;
	}

	public void setTempCalGrossAmount(BigDecimal tempCalGrossAmount) {
		this.tempCalGrossAmount = tempCalGrossAmount;
	}

	public BigDecimal getTempCalNetAmountPaid() {
		return tempCalNetAmountPaid;
	}

	public void setTempCalNetAmountPaid(BigDecimal tempCalNetAmountPaid) {
		this.tempCalNetAmountPaid = tempCalNetAmountPaid;
	}

	public BigDecimal getTotalUamount() {
		return totalUamount;
	}

	public void setTotalUamount(BigDecimal totalUamount) {
		this.totalUamount = totalUamount;
	}

	public BigDecimal getSubtractedAmount() {
		return subtractedAmount;
	}

	public void setSubtractedAmount(BigDecimal subtractedAmount) {
		this.subtractedAmount = subtractedAmount;
	}

	public List<BigDecimal> getLstApplDocNumber() {
		return lstApplDocNumber;
	}

	public void setLstApplDocNumber(List<BigDecimal> lstApplDocNumber) {
		this.lstApplDocNumber = lstApplDocNumber;
	}

	public String getColCurrency() {
		return colCurrency;
	}

	public void setColCurrency(String colCurrency) {
		this.colCurrency = colCurrency;
	}

	public CopyOnWriteArrayList<ShoppingCartDataTableBean> getLstModifyRoundRecord() {
		return lstModifyRoundRecord;
	}

	public void setLstModifyRoundRecord(CopyOnWriteArrayList<ShoppingCartDataTableBean> lstModifyRoundRecord) {
		this.lstModifyRoundRecord = lstModifyRoundRecord;
	}

	public String getInsurence1() {
		return insurence1;
	}

	public void setInsurence1(String insurence1) {
		this.insurence1 = insurence1;
	}

	public String getInsurence2() {
		return insurence2;
	}

	public void setInsurence2(String insurence2) {
		this.insurence2 = insurence2;
	}

	public String getLoyalityPointExpiring() {
		return loyalityPointExpiring;
	}

	public void setLoyalityPointExpiring(String loyalityPointExpiring) {
		this.loyalityPointExpiring = loyalityPointExpiring;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public ApplicationMailer getMailService() {
		return mailService;
	}

	public void setMailService(ApplicationMailer mailService) {
		this.mailService = mailService;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getHelpDeskNo() {
		return helpDeskNo;
	}

	public void setHelpDeskNo(String helpDeskNo) {
		this.helpDeskNo = helpDeskNo;
	}

	public BigDecimal getCollectionDocumentNumber() {
		return collectionDocumentNumber;
	}

	public void setCollectionDocumentNumber(BigDecimal collectionDocumentNumber) {
		this.collectionDocumentNumber = collectionDocumentNumber;
	}

	public BigDecimal getCollectionDocumentCode() {
		return collectionDocumentCode;
	}

	public void setCollectionDocumentCode(BigDecimal collectionDocumentCode) {
		this.collectionDocumentCode = collectionDocumentCode;
	}

	public BigDecimal getCollectionFinanceYear() {
		return collectionFinanceYear;
	}

	public void setCollectionFinanceYear(BigDecimal collectionFinanceYear) {
		collectionFinanceYear = collectionFinanceYear;
	}

	public Boolean getBeneCountryOne() {
		return beneCountryOne;
	}

	public void setBeneCountryOne(Boolean beneCountryOne) {
		this.beneCountryOne = beneCountryOne;
	}

	public Boolean getBeneCountryMore() {
		return beneCountryMore;
	}

	public void setBeneCountryMore(Boolean beneCountryMore) {
		this.beneCountryMore = beneCountryMore;
	}

	public String getBeneCountryName() {
		return beneCountryName;
	}

	public void setBeneCountryName(String beneCountryName) {
		this.beneCountryName = beneCountryName;
	}

	public void setMinLenght(BigDecimal minLenght) {
		this.minLenght = minLenght;
	}

	public BigDecimal getBeneficiaryAccountType() {
		return beneficiaryAccountType;
	}

	public void setBeneficiaryAccountType(BigDecimal beneficiaryAccountType) {
		this.beneficiaryAccountType = beneficiaryAccountType;
	}

	public List<AccountTypeFromView> getLstBankAccountTypeFromView() {
		return lstBankAccountTypeFromView;
	}

	public void setLstBankAccountTypeFromView(
			List<AccountTypeFromView> lstBankAccountTypeFromView) {
		this.lstBankAccountTypeFromView = lstBankAccountTypeFromView;
	}

	public BigDecimal getBeneficiaryStateId() {
		return beneficiaryStateId;
	}

	public void setBeneficiaryStateId(BigDecimal beneficiaryStateId) {
		this.beneficiaryStateId = beneficiaryStateId;
	}

	public List<StateMasterDesc> getLstStateMasterDescs() {
		return lstStateMasterDescs;
	}

	public void setLstStateMasterDescs(List<StateMasterDesc> lstStateMasterDescs) {
		this.lstStateMasterDescs = lstStateMasterDescs;
	}

	public BigDecimal getBeneficiaryDistId() {
		return beneficiaryDistId;
	}

	public void setBeneficiaryDistId(BigDecimal beneficiaryDistId) {
		this.beneficiaryDistId = beneficiaryDistId;
	}

	public List<DistrictMasterDesc> getLstDistrictMasterDescs() {
		return lstDistrictMasterDescs;
	}

	public void setLstDistrictMasterDescs(
			List<DistrictMasterDesc> lstDistrictMasterDescs) {
		this.lstDistrictMasterDescs = lstDistrictMasterDescs;
	}

	public BigDecimal getBeneficiaryCityId() {
		return beneficiaryCityId;
	}

	public void setBeneficiaryCityId(BigDecimal beneficiaryCityId) {
		this.beneficiaryCityId = beneficiaryCityId;
	}

	public List<CityMasterDesc> getLstCityMasterDescs() {
		return lstCityMasterDescs;
	}

	public void setLstCityMasterDescs(List<CityMasterDesc> lstCityMasterDescs) {
		this.lstCityMasterDescs = lstCityMasterDescs;
	}

	public String getTelePhoneCode() {
		return telePhoneCode;
	}

	public void setTelePhoneCode(String telePhoneCode) {
		this.telePhoneCode = telePhoneCode;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	public List<CountryMasterView> getLstCountryMasterDescs() {
		return lstCountryMasterDescs;
	}

	public void setLstCountryMasterDescs(
			List<CountryMasterView> lstCountryMasterDescs) {
		this.lstCountryMasterDescs = lstCountryMasterDescs;
	}

	public String getBeneTelePhoneNum() {
		return beneTelePhoneNum;
	}

	public void setBeneTelePhoneNum(String beneTelePhoneNum) {
		this.beneTelePhoneNum = beneTelePhoneNum;
	}

	public BigDecimal getBeneMobilePhoneNum() {
		return beneMobilePhoneNum;
	}

	public void setBeneMobilePhoneNum(BigDecimal beneMobilePhoneNum) {
		this.beneMobilePhoneNum = beneMobilePhoneNum;
	}

	public BigDecimal getBeneficiaryContactSeqId() {
		return beneficiaryContactSeqId;
	}

	public void setBeneficiaryContactSeqId(BigDecimal beneficiaryContactSeqId) {
		this.beneficiaryContactSeqId = beneficiaryContactSeqId;
	}

	public PersonalRemmitanceBeneficaryDataTable getPersonalRemmitanceBeneficaryDataTables() {
		return personalRemmitanceBeneficaryDataTables;
	}

	public void setPersonalRemmitanceBeneficaryDataTables(
			PersonalRemmitanceBeneficaryDataTable personalRemmitanceBeneficaryDataTables) {
		this.personalRemmitanceBeneficaryDataTables = personalRemmitanceBeneficaryDataTables;
	}

	public boolean isBankingChannelProducts() {
		return bankingChannelProducts;
	}

	public void setBankingChannelProducts(boolean bankingChannelProducts) {
		this.bankingChannelProducts = bankingChannelProducts;
	}

	public BigDecimal getNetAmountForTransaction() {
		return netAmountForTransaction;
	}

	public void setNetAmountForTransaction(BigDecimal netAmountForTransaction) {
		this.netAmountForTransaction = netAmountForTransaction;
	}

	public String getLocalCurrencyName() {
		return localCurrencyName;
	}

	public void setLocalCurrencyName(String localCurrencyName) {
		this.localCurrencyName = localCurrencyName;
	}

	public String getForgeignCurrencyName() {
		return forgeignCurrencyName;
	}

	public void setForgeignCurrencyName(String forgeignCurrencyName) {
		this.forgeignCurrencyName = forgeignCurrencyName;
	}

	public String getSelectedSearchRadioButton() {
		return selectedSearchRadioButton;
	}

	public void setSelectedSearchRadioButton(String selectedSearchRadioButton) {
		this.selectedSearchRadioButton = selectedSearchRadioButton;
	}

	public Boolean getBooRenderBeneNameOrBank() {
		return booRenderBeneNameOrBank;
	}

	public void setBooRenderBeneNameOrBank(Boolean booRenderBeneNameOrBank) {
		this.booRenderBeneNameOrBank = booRenderBeneNameOrBank;
	}

	public boolean isBooRenderAccountDialogBox() {
		return booRenderAccountDialogBox;
	}

	public void setBooRenderAccountDialogBox(boolean booRenderAccountDialogBox) {
		this.booRenderAccountDialogBox = booRenderAccountDialogBox;
	}

	public Boolean getCashbranchOne() {
		return cashbranchOne;
	}

	public void setCashbranchOne(Boolean cashbranchOne) {
		this.cashbranchOne = cashbranchOne;
	}

	public Boolean getCashbranchMore() {
		return cashbranchMore;
	}

	public void setCashbranchMore(Boolean cashbranchMore) {
		this.cashbranchMore = cashbranchMore;
	}

	public BigDecimal getBeneBankBranchId() {
		return beneBankBranchId;
	}

	public void setBeneBankBranchId(BigDecimal beneBankBranchId) {
		this.beneBankBranchId = beneBankBranchId;
	}

	public List<PopulateData> getLstBankBranch() {
		return lstBankBranch;
	}

	public void setLstBankBranch(List<PopulateData> lstBankBranch) {
		this.lstBankBranch = lstBankBranch;
	}

	public String getBeneBankBranchName() {
		return beneBankBranchName;
	}

	public void setBeneBankBranchName(String beneBankBranchName) {
		this.beneBankBranchName = beneBankBranchName;
	}

	public Boolean getMandatoryOptional() {
		return mandatoryOptional;
	}

	public void setMandatoryOptional(Boolean mandatoryOptional) {
		this.mandatoryOptional = mandatoryOptional;
	}

	public Boolean getBooRenderAccOrBranch() {
		return booRenderAccOrBranch;
	}

	public void setBooRenderAccOrBranch(Boolean booRenderAccOrBranch) {
		this.booRenderAccOrBranch = booRenderAccOrBranch;
	}

	public String getLoyalityPointExpiring2() {
		return loyalityPointExpiring2;
	}

	public void setLoyalityPointExpiring2(String loyalityPointExpiring2) {
		this.loyalityPointExpiring2 = loyalityPointExpiring2;
	}

	public String getLoyalityPointExpiring1() {
		return loyalityPointExpiring1;
	}

	public void setLoyalityPointExpiring1(String loyalityPointExpiring1) {
		this.loyalityPointExpiring1 = loyalityPointExpiring1;
	}

	public Boolean getCurrOne() {
		return currOne;
	}

	public void setCurrOne(Boolean currOne) {
		this.currOne = currOne;
	}

	public Boolean getCurrMore() {
		return currMore;
	}

	public void setCurrMore(Boolean currMore) {
		this.currMore = currMore;
	}

	public String getDestiCurrency() {
		return destiCurrency;
	}

	public void setDestiCurrency(String destiCurrency) {
		this.destiCurrency = destiCurrency;
	}

	public List<PopulateData> getLstofDestinationCurrency() {
		return lstofDestinationCurrency;
	}

	public void setLstofDestinationCurrency(List<PopulateData> lstofDestinationCurrency) {
		this.lstofDestinationCurrency = lstofDestinationCurrency;
	}

	public BigDecimal getDestiCurrencyId() {
		return destiCurrencyId;
	}

	public void setDestiCurrencyId(BigDecimal destiCurrencyId) {
		this.destiCurrencyId = destiCurrencyId;
	}

	public String getRemitAction() {
		return remitAction;
	}

	public void setRemitAction(String remitAction) {
		this.remitAction = remitAction;
	}

	public List<PersonalRemmitanceBeneficaryDataTable> getCoustomerBeneficaryListForQuick() {
		return coustomerBeneficaryListForQuick;
	}

	public void setCoustomerBeneficaryListForQuick(List<PersonalRemmitanceBeneficaryDataTable> coustomerBeneficaryListForQuick) {
		this.coustomerBeneficaryListForQuick = coustomerBeneficaryListForQuick;
	}

	public List<RemittanceReceiptSubreport> getRemittanceReceiptSubreportList() {
		return remittanceReceiptSubreportList;
	}

	public void setRemittanceReceiptSubreportList(List<RemittanceReceiptSubreport> remittanceReceiptSubreportList) {
		this.remittanceReceiptSubreportList = remittanceReceiptSubreportList;
	}

	public List<CollectionDetailView> getCollectionViewList() {
		return collectionViewList;
	}

	public void setCollectionViewList(List<CollectionDetailView> collectionViewList) {
		this.collectionViewList = collectionViewList;
	}

	public String getCustomerNameForReport() {
		return customerNameForReport;
	}

	public void setCustomerNameForReport(String customerNameForReport) {
		this.customerNameForReport = customerNameForReport;
	}

	public String getUserDefExchangeRate() {
		return userDefExchangeRate;
	}

	public void setUserDefExchangeRate(String userDefExchangeRate) {
		this.userDefExchangeRate = userDefExchangeRate;
	}

	public String getExchangeMinRate() {
		return exchangeMinRate;
	}

	public void setExchangeMinRate(String exchangeMinRate) {
		this.exchangeMinRate = exchangeMinRate;
	}

	public String getExchangeMaxRate() {
		return exchangeMaxRate;
	}

	public void setExchangeMaxRate(String exchangeMaxRate) {
		this.exchangeMaxRate = exchangeMaxRate;
	}

	public boolean isBtnRender() {
		return btnRender;
	}

	public void setBtnRender(boolean btnRender) {
		this.btnRender = btnRender;
	}

	

	
	
	 
	
	
	
}
