package com.amx.amxlib.model.trnx;

import java.io.Serializable;

import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;

public class BeneficiaryTrnxModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BeneAccountModel beneAccountModel;
	BenePersonalDetailModel benePersonalDetailModel;

	public BeneAccountModel getBeneAccountModel() {
		return beneAccountModel;
	}

	public void setBeneAccountModel(BeneAccountModel beneAccountModel) {
		this.beneAccountModel = beneAccountModel;
	}

	public BenePersonalDetailModel getBenePersonalDetailModel() {
		return benePersonalDetailModel;
	}

	public void setBenePersonalDetailModel(BenePersonalDetailModel benePersonalDetailModel) {
		this.benePersonalDetailModel = benePersonalDetailModel;
	}

}
