package com.amx.jax.serviceprovider.venteja;

import java.util.Map;

import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;

public enum VentejaServiceProviderFlexField {

	INDIC9 {
		// prn
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			dto.getTransactionDto().setPartner_transaction_reference(value.getAmieceDescription());

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
	INDIC13 {
		// payment type
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			dto.getTransactionDto().setPartner_transaction_reference(value.getAmieceDescription());

		}
	},
	INDIC12 {
		// prn
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			INDIC9.setValue(dto, requestFlexFields);

		}
	},
	INDIC11 {
		// loan on
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			INDIC7.setValue(dto, requestFlexFields);

		}
	},
	INDIC8 {
		// member type
		@Override
		public void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields) {
			FlexFieldDto value = requestFlexFields.get(this.name());
			dto.getCustomerDto().setCustomer_type(value.getAmieceDescription());

		}
	};

	public abstract void setValue(ServiceProviderCallRequestDto dto, Map<String, FlexFieldDto> requestFlexFields);
}
