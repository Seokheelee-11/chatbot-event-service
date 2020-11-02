package com.shinhancard.chatbot.dto.request;

import java.util.ArrayList;

import com.shinhancard.chatbot.domain.PropertyCode;

import lombok.Getter;

@Getter
public class EventTypeRequest {

	private String type;
	private String Name;
	private ArrayList<PropertyCode> properties = new ArrayList<PropertyCode>();
	
}
