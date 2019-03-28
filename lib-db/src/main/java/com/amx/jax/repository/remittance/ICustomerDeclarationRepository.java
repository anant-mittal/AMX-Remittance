package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.CustomerDeclerationView;

public interface ICustomerDeclarationRepository extends CrudRepository<CustomerDeclerationView, Serializable> {

	public List<CustomerDeclerationView> findByApplicationCountryIdAndCollectionDocumentFinanceYearAndCollectionDocumentIdAndCollectionDocumentNo
	(BigDecimal applicationCountryId,BigDecimal documentFinanceYear,BigDecimal collectionDocumentId,BigDecimal collectionDocumentNo);
}

