package com.shinhancard.chatbot.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class EventType {

	@Id
	private String id;

	private String type;
	private String Name;
	private PropertyCode[] properties;
	
	
}
