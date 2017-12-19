package com.amx.jax.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class TenantScope implements Scope {

	private Map<String, Object> scopedObjects = Collections.synchronizedMap(new HashMap<String, Object>());
	private Map<String, Runnable> destructionCallbacks = Collections.synchronizedMap(new HashMap<String, Runnable>());

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		String nameKey = getNameKey(name);
		if (!scopedObjects.containsKey(nameKey)) {
			scopedObjects.put(nameKey, objectFactory.getObject());
		}
		return scopedObjects.get(nameKey);
	}

	@Override
	public Object remove(String name) {
		String nameKey = getNameKey(name);
		destructionCallbacks.remove(nameKey);
		return scopedObjects.remove(nameKey);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		String nameKey = getNameKey(name);
		destructionCallbacks.put(nameKey, callback);
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	private String getNameKey(String name) {
		return getConversationId() + name;
	}

	@Override
	public String getConversationId() {
		if (TenantContextHolder.currentSite() == null) {
			return null;
		} else {
			return TenantContextHolder.currentSite().getId();
		}
	}

}