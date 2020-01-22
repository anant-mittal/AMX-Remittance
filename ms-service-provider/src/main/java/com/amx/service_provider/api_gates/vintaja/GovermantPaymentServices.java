package com.amx.service_provider.api_gates.vintaja;

public enum GovermantPaymentServices
{
	PHIL_HEALTH_YEAR_WISE(106), PHIL_HEALTH_MONTH_WISE(107),
	// ----------------------------------------------------------------------------------------------
	PAG_IBIG_SAVING(109), PAG_IBIG_MP2(110), PAG_IBIG_SHORT_TERM_LOAN(111), PAG_IBIG_HOUSE_LOAN(112),
	// ----------------------------------------------------------------------------------------------
	SSS_CONTRIBUTION_NEW_PRN(113), SSS_CONTRIBUTION_EXISTING_PRN(154), SSS_SHORT_TERM_LOAN(114),
	SSS_REAL_ESTATE_LOAN(115), SSS_MISCELLANOUS(116),
	// ----------------------------------------------------------------------------------------------
	BILL_PAYMENTS(118),
	// ----------------------------------------------------------------------------------------------
	TOP_UP_WALLET(147), RESERVATION_PAYMENT(148), RESERVATION_PAYMENT_SERACH(149),
	// ----------------------------------------------------------------------------------------------
	SSS_CONTRIBUTION_SERACH_BY_MEMBER_ID(152), SSS_CONTRIBUTION_SERACH_BY_PRN(153),
	// ----------------------------------------------------------------------------------------------
	UN_DEFINED(999);

	private int service_int_value;

	GovermantPaymentServices(int service_int_value)
	{
		this.service_int_value = service_int_value;
	}

	public int getService_int_value()
	{
		return service_int_value;
	}
}
