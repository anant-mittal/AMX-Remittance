package com.amx.libjax.model;

public final class CustomerModelInterface {

	public interface ICustomerCredential {
		public String getLoginId();

		public void setLoginId(String loginId);

		public String getPassword();

		public void setPassword(String password);
	}

	public interface ICustomerCredentialDotp {
		public String getMotp();

		public void setMotp(String motp);

		public String getEotp();

		public void setEotp(String eotp);
	}

	public interface ICustomerModel extends ICustomerCredential, ICustomerCredentialDotp {
		public String getMotp();

		public void setMotp(String motp);

		public String getEotp();

		public void setEotp(String eotp);

		public String getEmail();

		public void setEmail(String email);
	}

}
