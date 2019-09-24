package com.amx.amxlib.model.response;

import java.util.ArrayList;
import java.util.List;

import com.amx.amxlib.meta.model.AddAdditionalBankDataDto;
import com.amx.amxlib.meta.model.PurposeTrnxAmicDescDto;
import com.amx.jax.model.AbstractModel;

public class PurposeOfTransactionModel extends AbstractModel {

	public List<AddAdditionalBankDataDto> dto;
	
	@Override
	public String getModelType() {
		return "purpose-of-txn";
	}

	public List<AddAdditionalBankDataDto> getDto() {
		return dto;
	}

	public void setDto(List<AddAdditionalBankDataDto> dto) {
		this.dto = dto;
	}

	
	
	
}
