package com.amx.jax.logger;

import com.amx.utils.ArgUtil;
import com.amx.utils.EnumType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditActor {

	public static enum ActorType implements EnumType {

		G, C, E,
		GUEST(G), CUSTOMER(C), EMP(E);

		ActorType shortName;

		ActorType(ActorType shortName) {
			this.shortName = shortName;
		}

		ActorType() {
			this.shortName = this;
		}

		@Override
		public String toString() {
			return this.name();
		}

		@Override
		public String stringValue() {
			return this.shortName.toString();
		}

		public ActorType enunValue() {
			return this.shortName;
		}
	}

	@JsonProperty("type")
	ActorType actorType;

	@JsonProperty("id")
	String actorId;

	public AuditActor(ActorType actorType, Object actorId) {
		this.actorType = actorType;
		this.actorId = ArgUtil.parseAsString(actorId);
	}

	public ActorType getActorType() {
		return actorType.enunValue();
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
