package com.shinhancard.chatbot.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RewardInfo {
	@Id
	private String Id;
	private String name;
	private Double probability;
	private Integer limit;
	private String message;
	private Map<String,String> infoes = new HashMap<String, String>();	
}
