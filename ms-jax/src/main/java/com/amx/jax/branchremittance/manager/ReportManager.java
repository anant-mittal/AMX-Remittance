package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.CountryBranchMdlv1;
import com.amx.jax.dbmodel.CustomerEmployeeInfoView;
import com.amx.jax.dbmodel.PurposeOfRemittanceViewModel;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.remittance.CustomerDeclerationView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.DeclarationReportDto;
import com.amx.jax.model.response.remittance.RemittanceDeclarationReportDto;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerEmployeeInfoRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.IPurposeOfRemittance;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.remittance.ICustomerDeclarationRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.AbstractService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.JaxUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class ReportManager extends AbstractService{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	BranchRemittanceManager branchRemitManager;
	
	@Autowired
	IDocumentDao documentDao;
	
	@Autowired
	IApplicationCountryRepository applCountryRepos;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	BankMetaService bankMetaService;

	@Autowired
	FinancialService finanacialService;
	
	@Autowired
	ICurrencyDao currDao;
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;
	
	@Autowired
	RemittanceApplicationManager remitApplManager;
	
	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	BeneficiaryService beneficiaryService;
	
	@Autowired
	ICustomerDeclarationRepository customerDecRepository;

	
	@Autowired
	private CustomerDao custDao;
	
	@Autowired
	ICustomerEmployeeInfoRepository customerEmplRepossitory;
	
	@Autowired
	IRemittanceTransactionDao remitTransactRepository;
	
	
	@Autowired
	IPurposeOfRemittance purposeOfRemittance;
	
	
	 
	public RemittanceDeclarationReportDto fetchCustomerDeclarationReport(BigDecimal collectionDocNo, BigDecimal collectionDocYear,BigDecimal collectionDocCode){
		RemittanceDeclarationReportDto decalReport = new RemittanceDeclarationReportDto();
		List<DeclarationReportDto> lstDispDeclarationView = new ArrayList<>();
		if(!JaxUtil.isNullZeroBigDecimalCheck(collectionDocNo)) {
			throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document number should not be blank.");
		}
		if(!JaxUtil.isNullZeroBigDecimalCheck(collectionDocYear)) {
			throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document year should not be blank.");
		}
		if(!JaxUtil.isNullZeroBigDecimalCheck(collectionDocCode)) {
			throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document code should not be blank.");
		}
		EmployeeDetailsView employeeDetails = employeeDetailsRepository.findByEmployeeId(metaData.getEmployeeId());
		if(employeeDetails==null) {
			throw new GlobalException(JaxError.INVALID_EMPLOYEE, "Employee not found");
		}
		CountryBranchMdlv1 countryBranch = bankMetaService.getCountryBranchById(employeeDetails.getCountryBranchId()); //user branch not customer branch
		List<CustomerDeclerationView> lstDeclarationView = customerDecRepository.findByApplicationCountryIdAndCollectionDocumentFinanceYearAndCollectionDocumentIdAndCollectionDocumentNo(metaData.getCountryId(), collectionDocYear, collectionDocCode, collectionDocNo);
		
		if(lstDeclarationView!=null && !lstDeclarationView.isEmpty()) {
			for (CustomerDeclerationView customerDeclerationView : lstDeclarationView) { 
				DeclarationReportDto declerationReportBean = new DeclarationReportDto();
				//set branch name
				String branchName =new String();
				String companyName =new String();
				StringBuffer toAddress =new StringBuffer();
				StringBuffer customerName=new StringBuffer();
				StringBuffer employer = new StringBuffer();
				String countryName =new String();
				customerName.append(customerDeclerationView.getFirstName()==null?"":customerDeclerationView.getFirstName());
				customerName.append(" ");
				customerName.append(customerDeclerationView.getMiddleName()==null?"":customerDeclerationView.getMiddleName());
				customerName.append(" ");
				customerName.append(customerDeclerationView.getLastName()==null?"":customerDeclerationView.getLastName());
				
				List<CustomerEmployeeInfoView> customerEmployeeInfo =customerEmplRepossitory.findByCustomerId(metaData.getCustomerId());
				
				if(customerEmployeeInfo !=null && !customerEmployeeInfo.isEmpty()){
					CustomerEmployeeInfoView customerEmployee = customerEmployeeInfo.get(0);
					companyName = customerEmployee.getEmployeeName()==null?"":customerEmployee.getEmployeeName();
					employer.append(customerEmployee.getEmployeeProofDesc()==null?"":customerEmployee.getEmployeeProofDesc());
				}
				// to set Purpose of transcation
				String purpose ="";
				
				List<PurposeOfRemittanceViewModel>  purposeOfRemittanceList =   purposeOfRemittance.getPurposeOfRemittance(customerDeclerationView.getDocumentNo(),customerDeclerationView.getDocumentFinanceYear());
				
				if(purposeOfRemittanceList!=null && !purposeOfRemittanceList.isEmpty()) {
					for(PurposeOfRemittanceViewModel  purposeObj: purposeOfRemittanceList) {
						if(purposeObj.equals("")) {
							purpose  = purposeObj.getFlexiFieldValue();
						}else {
							purpose  = purpose+", "+purposeObj.getFlexiFieldValue();
						}
						
					}
					
				}
				if(purpose != null && !purpose.equalsIgnoreCase("")){
					declerationReportBean.setPurpose(purpose.toUpperCase());
				}

				declerationReportBean.setCustomerName(customerName==null?"":customerName.toString().toUpperCase());
				declerationReportBean.setCivilId(customerDeclerationView.getCivilId()==null?"":customerDeclerationView.getCivilId().toPlainString());
				declerationReportBean.setSourceOfIncome(customerDeclerationView.getSourceOfIncome()==null?" ":customerDeclerationView.getSourceOfIncome().toUpperCase());
				declerationReportBean.setEmployeer(employer==null?" ":employer.toString().toUpperCase());
				declerationReportBean.setCompanyName(companyName==null?" ":companyName.toUpperCase());
				declerationReportBean.setTitle(customerDeclerationView.getTitle());
				declerationReportBean.setNationality(customerDeclerationView.getNationality().toUpperCase());
				declerationReportBean.setContactNo(customerDeclerationView.getContactNo()==null?"":customerDeclerationView.getContactNo().toString());
				declerationReportBean.setToAddress(toAddress.toString());
				declerationReportBean.setSourceOfIncome(customerDeclerationView.getSourceOfIncome());
				

				StringBuffer receiptRef = new StringBuffer();
				receiptRef.append(customerDeclerationView.getCollectionDocumentFinanceYear()==null?"":customerDeclerationView.getCollectionDocumentFinanceYear());
				receiptRef.append(" / ");
				receiptRef.append(customerDeclerationView.getCollectionDocumentNo()==null?"":customerDeclerationView.getCollectionDocumentNo());
				declerationReportBean.setReceiptReferenceNo(receiptRef.toString());

				StringBuffer transactionRef = new StringBuffer();
				transactionRef.append(customerDeclerationView.getCollectionDocumentFinanceYear()==null?"":customerDeclerationView.getCollectionDocumentFinanceYear());
				transactionRef.append(" / ");
				transactionRef.append(customerDeclerationView.getDocumentNo()==null?"":customerDeclerationView.getDocumentNo());
				declerationReportBean.setTransactionRefNo(transactionRef.toString());
				if(employeeDetails!=null) {
					declerationReportBean.setCashierName(employeeDetails.getUserName()==null?"":employeeDetails.getUserName().toUpperCase());
				}

				if (customerDeclerationView.getSignatureSpecimenClob() != null) {
					try {
						declerationReportBean.setSignatutre(customerDeclerationView.getSignatureSpecimenClob().getSubString(1,(int) customerDeclerationView.getSignatureSpecimenClob().length()));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(countryBranch!=null) {
					declerationReportBean.setBranchName(countryBranch.getBranchName());
					declerationReportBean.setCustomerReference(customerDeclerationView.getCustomerReference());
					declerationReportBean.setBeneRelation(customerDeclerationView.getRelationDescription());
				}
				List<RemittanceTransactionView> remitTrnxView = remitTransactRepository.getRemittanceTransaction(customerDeclerationView.getCollectionDocumentNo(), customerDeclerationView.getCollectionDocumentFinanceYear(), customerDeclerationView.getCollectionDocumentId());
				
				if(remitTrnxView!=null && !remitTrnxView.isEmpty()) {
					declerationReportBean.setCurrencyAmount(remitTrnxView.get(0).getNetAmount()==null?"0":remitTrnxView.get(0).getNetAmount().toString());
				}
			
				lstDispDeclarationView.add(declerationReportBean);
				
			}
		}
		decalReport.setDeclarationList(lstDispDeclarationView);
		
		return decalReport; 
	}

}
