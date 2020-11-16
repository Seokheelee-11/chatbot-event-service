package com.shinhancard.chatbot.domain;

import java.time.LocalDateTime;

import com.shinhancard.chatbot.entity.EventManage;

import lombok.Data;

@Data
public class EventInfo {
	private String EventId;
	private String name;
	private String discription;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	
	public EventInfo() {
		
	}
	
	public EventInfo(EventManage eventManage) {
		DefaultInfo defaultInfo = eventManage.getDefaultInfo();

		this.setEventId(eventManage.getEventId());
		this.setName(defaultInfo.getName());
		this.setDiscription(defaultInfo.getDescription());
		this.setStartDate(defaultInfo.getStartDate());
		this.setEndDate(defaultInfo.getEndDate());
		
	}
	

}
