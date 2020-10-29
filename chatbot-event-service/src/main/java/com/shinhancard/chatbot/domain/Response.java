package com.shinhancard.chatbot.domain;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Response {

	private String successMessage;
	private String failureMessage;
	private Map<String,String> infoes = new HashMap<String, String>();	
}
