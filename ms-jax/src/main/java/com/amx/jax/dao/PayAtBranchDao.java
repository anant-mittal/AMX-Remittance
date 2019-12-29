package com.amx.jax.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.PayAtBranchTrnxModel;
import com.amx.jax.dbmodel.PaymentModesModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.PayAtBranchTrnxListRepository;
import com.amx.jax.repository.PaymentModesRepository;

@Component
public class PayAtBranchDao {
	@Autowired
	PaymentModesRepository paymentModesRepository;
	@Autowired
	PayAtBranchTrnxListRepository pbTrnxListRepository;
	@Autowired
	MetaData metaData;

	public List<PaymentModesModel> getPaymentModes() {
		List<PaymentModesModel> paymentModesList = paymentModesRepository.getPaymentModes();
		return paymentModesList;

	}

	public List<PayAtBranchTrnxModel> getPbTrnxList() {
		List<PayAtBranchTrnxModel> pbTrnxList = pbTrnxListRepository.getPbTrnxList(metaData.getCustomerId());
		return pbTrnxList;
	}

	public List<PayAtBranchTrnxModel> getPbTrnxListBranch() {
		List<PayAtBranchTrnxModel> pbTrnxList = pbTrnxListRepository.getPbTrnxListBranch();
		return pbTrnxList;
	}
}
