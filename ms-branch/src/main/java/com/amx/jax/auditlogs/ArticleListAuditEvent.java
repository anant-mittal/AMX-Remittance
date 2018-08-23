package com.amx.jax.auditlogs;

import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.auditlogs.JaxAuditEvent.Type;
import com.amx.jax.model.request.CommonRequest;

public class ArticleListAuditEvent extends JaxAuditEvent {
	CommonRequest dataModel;

	public CommonRequest getDataModel() {
		return dataModel;
	}

	public void setDataModel(CommonRequest dataModel) {
		this.dataModel = dataModel;
	}
	
	public ArticleListAuditEvent(CommonRequest dataModel) {
		super(Type.ARTICLE_LIST);
		this.dataModel = dataModel;
		
	}
}
