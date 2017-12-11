package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IBeneficiaryOnlineDao;

@Component
public class RemittanceTransactionManager {

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	private BlackListDao blistDao;

	public Object validateTransactionData(RemittanceTransactionRequestModel model) {

		BigDecimal beneId = model.getBeneId();
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(beneId);
		validateBlackListedBene(beneficiary);
		return model;

	}

	private void validateBlackListedBene(BenificiaryListView beneficiary) {
		List<BlackListModel> blist = blistDao.getBlackByName(beneficiary.getBenificaryName());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Beneficiary name found matching with black list ",
					JaxError.BLACK_LISTED_CUSTOMER.getCode());
		}
		blist = blistDao.getBlackByName(beneficiary.getArbenificaryName());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Beneficiary local name found matching with black list ",
					JaxError.BLACK_LISTED_CUSTOMER.getCode());
		}
	}

}
