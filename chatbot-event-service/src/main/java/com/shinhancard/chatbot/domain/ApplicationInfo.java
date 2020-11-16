package com.shinhancard.chatbot.domain;



import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.entity.EventManage;

import lombok.Data;

@Data
public class ApplicationInfo {
	
	private EventInfo eventInfo = new EventInfo();
	private List<EventApplicationLog> eventApplicationLogs = new ArrayList<>();

	public ApplicationInfo() {
		
	}
	public ApplicationInfo(EventApplication eventApplication, EventManage eventManage) {
		this.setEventInfo(new EventInfo(eventManage));
		this.setEventApplicationLogs(eventApplication.getApplicationLogs());
	}
	
	public ApplicationInfo(EventApplication eventApplication, EventManage eventManage, String channel) {
		this.setEventInfo(new EventInfo(eventManage));
		this.setEventApplicationLogs(eventApplication.getApplicationLogs(channel));
	}
	
	
}
