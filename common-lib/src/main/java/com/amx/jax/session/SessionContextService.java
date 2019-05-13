package com.amx.jax.session;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.model.MapModel;
import com.amx.utils.JsonUtil;

@Component
public class SessionContextService {

	@Autowired(required = false)
	private SessionContextCache sessionContextCache;

	public MapModel getContext() {
		if (sessionContextCache != null) {
			return new MapModel(sessionContextCache.getContext());
		}
		return new MapModel();
	}

	public <T extends MapModel> MapModel getContext(T map) {
		if (sessionContextCache != null) {
			map.fromMap(sessionContextCache.getContext());
		}
		return map;
	}

	public <T> T getContext(Class<T> toValueType) {
		if (sessionContextCache != null) {
			return JsonUtil.toObject(sessionContextCache.getContext(), toValueType);
		}
		return JsonUtil.toObject(new HashMap<String, Object>(), toValueType);
	}

	public void setContext(Object model) {
		if (sessionContextCache != null) {
			sessionContextCache.setContext(JsonUtil.toMap(model));
		}
	}

	public void setContext(MapModel model) {
		if (sessionContextCache != null) {
			sessionContextCache.setContext(model.toMap());
		}
	}
}
