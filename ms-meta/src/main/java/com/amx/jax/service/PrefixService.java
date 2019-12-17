package com.amx.jax.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.constant.PrefixEnum;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.PrefixModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.AbstractService;

/**
 * @author Subodh Bhoir
 *
 */

@Service
public class PrefixService extends AbstractService {
	@Autowired
	MetaData meta;

	/**
	 * @return returns prefix list
	 * 
	 */
	public AmxApiResponse<PrefixModel, Object> getPrefixListResponse() {
		List<PrefixModel> prefixList = new ArrayList<PrefixModel>();

		prefixList.add(new PrefixModel(PrefixEnum.MR_CODE));
		prefixList.add(new PrefixModel(PrefixEnum.MRS_CODE));
		if (prefixList.isEmpty()) {
			throw new GlobalException("Prefix list is not abaliable");
		}
		return AmxApiResponse.buildList(prefixList);
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return "prefix";
	}
	
	public List<PrefixModel> getPrefixListOffsite() {
		List<PrefixModel> prefixList = new ArrayList<PrefixModel>();

		prefixList.add(new PrefixModel(PrefixEnum.MR_CODE));
		prefixList.add(new PrefixModel(PrefixEnum.MRS_CODE));
		prefixList.add(new PrefixModel(PrefixEnum.MS));
		
		if (prefixList.isEmpty()) {
			throw new GlobalException("Prefix list is not abaliable");
		} 
		return prefixList;
	}
}
