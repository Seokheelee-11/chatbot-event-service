package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;

import com.shinhancard.chatbot.domain.PropertyCode;

import lombok.Getter;

@Getter
public class EventTypeResponse {

	private String type;
	private String Name;
	private ArrayList<PropertyCode> properties = new ArrayList<PropertyCode>();
	
}
