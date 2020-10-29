package com.shinhancard.chatbot.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;


@Data
public class EventManage {

	@Id
	private String id;
	
	private EventType eventType;
	private String eventId;
	
	
	
	
}
