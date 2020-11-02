package com.shinhancard.chatbot.entity;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.PropertyCode;

import lombok.Data;

@Data
public class EventType {

	@Id
	private String id;

	private String type;
	private String Name;
	private ArrayList<PropertyCode> properties = new ArrayList<PropertyCode>();
	
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
