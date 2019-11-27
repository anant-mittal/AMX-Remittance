package com.amx.test;

import java.io.IOException;
import java.net.URISyntaxException;

import com.amx.jax.bot.Chatbot;

public class ChatTest { // Noncompliant

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {

		System.out.println("==="+Chatbot.response("9930104050", "hi"));
		System.out.println("==="+Chatbot.response("9930104050", "hello"));
		System.out.println("==="+Chatbot.response("9930104050", "what is your name?"));
		System.out.println("==="+Chatbot.response("9930104050", "WHERE ELSE YOU OPERATE"));
		System.out.println("==="+Chatbot.response("9930104050", "what's  Al mulla ?"));
		System.out.println("==="+Chatbot.response("9930104050", "ALREADY VERIFIED"));
		System.out.println("==="+Chatbot.response("9930104050", "RESEND LINK"));
		System.out.println("==="+Chatbot.response("9930104050", "ARE YOU A WHORE"));
		
	}

}
