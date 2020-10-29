package com.shinhancard.chatbot.entity;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.PropertyCode;

import lombok.Getter;

@Getter
public class EventType {

	@Id
	private String id;

	private String type;
	private String Name;
	private PropertyCode[] properties;
	
	
}
