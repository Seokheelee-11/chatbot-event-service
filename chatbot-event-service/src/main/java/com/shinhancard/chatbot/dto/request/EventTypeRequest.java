package com.shinhancard.chatbot.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.PropertyCode;

import lombok.Data;

@Data
public class EventTypeRequest {

	private String type;
	private String Name;
	private List<PropertyCode> properties = new ArrayList<>();
	private PropertyCode propertyCode;
	public PropertyCode[] getPropertiesArray() {
		return this.properties.toArray(new PropertyCode[properties.size()]);
	}
}
