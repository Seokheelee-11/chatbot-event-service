package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shinhancard.chatbot.domain.PropertyCode;

import lombok.Data;


@Data
public class EventTypeResponse {

	private String type;
	private String Name;
	private List<PropertyCode> properties = new ArrayList<>();
	private PropertyCode propertyCode;
	
	public void setPropertiesArray(PropertyCode[] properties) {
		this.properties = new ArrayList<>(Arrays.asList(properties));
	}
}
