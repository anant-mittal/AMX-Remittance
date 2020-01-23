package com.amx.jax.postman.cache;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

/**
 * The Class LoggedInUsers.
 */
@Component
public class ContactsCache extends CacheBox<Long> {

	/**
	 * Instantiates a new logged in users.
	 */
	public ContactsCache() {
		super("ContactsCache");
	}

}
