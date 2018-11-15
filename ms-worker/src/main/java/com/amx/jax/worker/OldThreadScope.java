package com.amx.jax.worker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class OldThreadScope implements Scope {
	private final ThreadLocal threadScope = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
	};

	public Object get(String name, ObjectFactory objectFactory) {
		Map scope = (Map) threadScope.get();
		Object object = scope.get(name);
		if (object == null) {
			object = objectFactory.getObject();
			scope.put(name, object);
		}
		return object;
	}

	public Object remove(String name) {
		Map scope = (Map) threadScope.get();
		return scope.remove(name);
	}

	public void registerDestructionCallback(String name, Runnable callback) {
	}

	@Override
	public Object resolveContextualObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConversationId() {
		// TODO Auto-generated method stub
		return null;
	}
}