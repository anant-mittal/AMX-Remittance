package com.amx.jax.model.request.customer;

import java.math.BigDecimal;

public interface ICustomerContactData {

	String getEmail();

	String getMobile();

	String getTelPrefix();

	String getWatsAppTelePrefix();

	BigDecimal getWatsAppMobileNo();

}
