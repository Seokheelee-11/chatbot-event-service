package com.shinhancard.chatbot.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.PropertyCode;

import lombok.Data;

@Data
public class EventType {

	@Id
	private String id;

	private String type;
	private String Name;
	private List<PropertyCode> properties = new ArrayList<>();
	private PropertyCode propertyCode;
	
	public void setPropertiesArray(PropertyCode[] properties) {
		this.properties = new ArrayList<>(Arrays.asList(properties));
	}
	
	public PropertyCode[] getPropertiesArray() {
		return this.properties.toArray(new PropertyCode[properties.size()]);
	}
	
	
}
