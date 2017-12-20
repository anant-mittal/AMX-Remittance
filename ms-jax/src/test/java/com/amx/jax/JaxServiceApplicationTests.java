package com.amx.jax;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.dal.ApplicationProcedureDao;
import com.amx.jax.dal.CryptoDao;
import com.amx.jax.dal.LoyaltyInsuranceProDao;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.VTransferRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.WebUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxServiceApplicationTests {

	@Autowired
	CryptoDao cryptdao;

	@Autowired
	UserService userService;

	@Autowired
	LoyaltyInsuranceProDao loyaltyDao;
	
	@Autowired
	ApplicationProcedureDao applProDao;
	
	
	

	// @Test
	public void decrypt() {
		String output = cryptdao.decrypt("123123", "15BBA6D8EF5C4D660F57CB9E91C7B9D5");
		assertTrue(output.equals("amx@123"));
	}

	// @Test
	public void encrypt() {
		String output = cryptdao.encrypt("123123", "amx@123");
		assertTrue(output.equals("15BBA6D8EF5C4D660F57CB9E91C7B9D5"));
	}

	// @Test
	public void sendOtpForCivilidCheck() {
		ApiResponse apiResp = userService.sendOtpForCivilId("284052306594");
		assertTrue(apiResp.getError() == null);
	}

	@Bean
	public MetaData meta() {
		MetaData metad = new MetaData();
		metad.setCountryId(new BigDecimal(91));
		return metad;
	}

	@Autowired
	WebUtils webutil;

	// @Test
	public void testwebutil() {

		webutil.getClientIp();
	}

	/** Test for Loyalty Msg **/

	// @Test
	public void loyaltyInsuranceMsg() {
		BigDecimal cusRef = new BigDecimal("1553194");
		try {
			Map<String, Object> outputMAp = loyaltyDao.loyaltyInsuranceProcedure(cusRef, "02/12/2017");
			System.out.println(
					"Lty Points :" + outputMAp.get("P_LTY_STR1") + "\n Lyt Msg2 :" + outputMAp.get("P_LTY_STR2"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("outputMAp :"+outputMAp.toString());

	}

	
	/** Generate document seriality **/
	
	@Test
	public void getDocumentSeriality() {
		BigDecimal applCountryId = new BigDecimal("91");
		BigDecimal companyId = new BigDecimal("1");
		BigDecimal documentId = new BigDecimal("1");
		BigDecimal financialYear = new BigDecimal("2017");
		String processIn = "U";
		BigDecimal branchId = new BigDecimal("90");
		
		try {
			Map<String, Object> outputMAp = applProDao.getDocumentSeriality(applCountryId, companyId, documentId, financialYear, processIn, branchId);
			System.out.println("OUT PUT :"+outputMAp.toString());
			
		}catch(Exception e) {
			System.out.println("Message :"+e.getMessage());
		}
	}
	
	
	
	@Autowired
	PipsMasterDao pipsDao;
	
	@Autowired
	private VTransferRepository transferRepo;

	
}
