package com.amx.jax.manager.remittance;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApplicationProcedureParam;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;

/**
 * @author Prashant
 *
 */
@Component
public class RemittanceApplicationParamManager {

	@Resource
	Map<String, Object> remitApplParametersMap;

	public void populateRemittanceApplicationParamMap(IRemittanceApplicationParams remittanceApplicationParams,
			BenificiaryListView beneficiaryView) {
		//remitApplParametersMap.put(ApplicationProcedureParam.P_ROUTING_COUNTRY_ID, remittanceApplicationParams.getLocalAmount());

	}
}
