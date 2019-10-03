package com.amx.jax.serviceprovider.venteja;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import com.amx.amxlib.model.JaxFieldDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.utils.DateUtil;

public enum VentejaServiceProviderFlexField {

	INDIC9 {
		// account number
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			INDIC10.setValue(dto, requestFlexFields);

		}
	},
	INDIC7 {
		// loan no.
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			dto.getBeneficiaryDto().setBeneficiary_account_number(value.getAmieceDescription());

		}
	},
	INDIC11 {
		// mp2
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			INDIC7.setValue(dto, requestFlexFields);

		}
	},
	INDIC12 {
		// start date
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			LocalDate fromLocalDate = DateUtil.validateDate(value.getAmieceDescription(), ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
			Date fromDate = Date.from(fromLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			dto.getTransactionDto().setCoverage_start_date(fromDate);
		}

		@Override
		public void setAdditionalParams(JaxFieldDto jaxFieldDto) {
			Map<String, Object> additionalValidations = jaxFieldDto.getAdditionalValidations();
			additionalValidations.remove("lteq");
			additionalValidations.remove("gt");
		}

	},
	INDIC13 {
		// end date
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			LocalDate toLocalDate = DateUtil.validateDate(value.getAmieceDescription(), ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
			Date toDate = Date.from(toLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			dto.getTransactionDto().setCoverage_end_date(toDate);
		}

		@Override
		public void setAdditionalParams(JaxFieldDto jaxFieldDto) {
			Map<String, Object> additionalValidations = jaxFieldDto.getAdditionalValidations();
			additionalValidations.remove("lteq");
			additionalValidations.remove("gt");
		}
	},
	INDIC8 {
		// member id
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			dto.getBeneficiaryDto().setPartner_beneficiary_id(value.getAmieceDescription());

		}
	},
	INDIC10 {
		// biller code
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			dto.getBeneficiaryDto().setBeneficiary_bank_code(value.getAmieceDescription());
		}
	};

	public abstract void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields);

	public void setAdditionalParams(JaxFieldDto jaxFieldDto) {

	};
}
