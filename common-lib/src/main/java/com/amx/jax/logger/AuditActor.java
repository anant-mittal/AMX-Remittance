package com.amx.jax.logger;

import com.amx.utils.ArgUtil;

public class AuditActor {

	public static enum ActorType {

		GUEST("G"), CUSTOMER("C"), EMPLOYEE("E");
		String shortName;

		ActorType(String shortName) {
			this.shortName = shortName;
		}

		@Override
		public String toString() {
			return this.shortName;
		}
	}

	ActorType actorType;
	String actorId;

	public AuditActor(ActorType actorType, Object actorId) {
		this.actorType = actorType;
		this.actorId = ArgUtil.parseAsString(actorId);
	}

	public ActorType getActorType() {
		return actorType;
	}

	public void setActorType(ActorType actorType) {
		this.actorType = actorType;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String toString() {
		return String.format("%s:%s", this.actorType, this.actorId);
	}
}
