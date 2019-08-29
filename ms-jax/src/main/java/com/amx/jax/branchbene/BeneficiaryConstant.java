package com.amx.jax.branchbene;

import com.amx.jax.util.AmxDBConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BeneficiaryConstant {

	public enum BeneStatus {
		ENABLE(AmxDBConstants.Yes, "Active"), DISABLE(AmxDBConstants.Deleted, "Disabled"), HOLD(AmxDBConstants.Hold,
				"Deactive")/* compliance deactivated or heldby compliance */;
		@JsonIgnore
		String dbFlag;
		String description;

		private BeneStatus(String dbFlag, String description) {
			this.dbFlag = dbFlag;
			this.description = description;
		}

		public String getDbFlag() {
			return dbFlag;
		}

		public void setDbFlag(String dbFlag) {
			this.dbFlag = dbFlag;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}
}
