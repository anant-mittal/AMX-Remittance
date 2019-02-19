package com.amx.jax.pricer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.repository.CustCatDiscountRepository;

@Component
public class CustCatDiscountDao {

	@Autowired
	CustCatDiscountRepository custCatDiscountRepository;

	public CustomerCategoryDiscount getDiscountByCustomerCategory(String category) {
		return custCatDiscountRepository.findByCustomerCategory(category);
	}

	public CustomerCategoryDiscount saveDiscountForCustomerCategory(CustomerCategoryDiscount customerCategoryDiscount) {
		return custCatDiscountRepository.save(customerCategoryDiscount);
	}

	public List<CustomerCategoryDiscount> getDiscountByAllCustCategory() {
		// TODO Auto-generated method stub
		return (List<CustomerCategoryDiscount>) custCatDiscountRepository.findAll();
	}

}
