package com.amx.jax.serviceprovider;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.serviceprovider.venteja.VentajaManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VentejaApiTest {

	@Autowired
	VentajaManager ventajaManager;
	@Autowired
	MetaData metaData;

	@Test
	public void testcallVentajaPartnerApi() {
		RemittanceResponseDto responseDto = new RemittanceResponseDto();
		metaData.setCustomerId(BigDecimal.valueOf(1321971));
		responseDto.setCollectionDocumentCode(BigDecimal.valueOf(2));
		responseDto.setCollectionDocumentFYear(BigDecimal.valueOf(2019));
		responseDto.setCollectionDocumentNo(BigDecimal.valueOf(37004743));
		ventajaManager.callVentajaPartnerApi(responseDto);
	}
}
