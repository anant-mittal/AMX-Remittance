package com.amx.jax.util.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class PatternValidator {
	private Pattern userNamepattern;
	private Matcher matcher;

	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{8,15}$";

	public PatternValidator() {
		userNamepattern = Pattern.compile(USERNAME_PATTERN);
	}

	public boolean validateUserName(final String username) {

		matcher = userNamepattern.matcher(username);
		return matcher.matches();

	}
}
