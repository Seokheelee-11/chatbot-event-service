package com.shinhancard.chatbot.domain;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ResponseInfo {

	private String ResponseMessage;
	Map<String,String> infoes = new HashMap<String, String>();
	
}
