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
	
	//TODO :: setProperties를 바꾸자, eventManage의 isProperty 값을 T로 바꿔주는걸로
	public void setProperties(EventManage eventManage) {
		if(eventManage.getDefaultInfo().getIsProperty()) {
			properties.add(PropertyCode.DEFAULT);
		}
		if(eventManage.getTarget().getIsProperty()) {
			properties.add(PropertyCode.TARGET);
		}
		if(eventManage.getResponse().getIsProperty()) {
			properties.add(PropertyCode.RESPONSE);
		}
		if(eventManage.getOverLap().getIsProperty()) {
			properties.add(PropertyCode.OVERLAP);
		}
		if(eventManage.getQuiz().getIsProperty()) {
			properties.add(PropertyCode.QUIZ);
		}
		if(eventManage.getReward().getIsProperty()) {
			properties.add(PropertyCode.REWARD);
		}
	}	
	

}
