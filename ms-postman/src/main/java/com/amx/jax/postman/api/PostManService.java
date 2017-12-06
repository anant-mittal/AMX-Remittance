package com.amx.jax.postman.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.postman.EmailService;

@Service
public class PostManService {
	
	@Autowired
	private EmailService emailService;

}
