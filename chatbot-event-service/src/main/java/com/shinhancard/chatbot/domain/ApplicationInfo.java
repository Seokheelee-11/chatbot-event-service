package com.shinhancard.chatbot.domain;



import java.time.LocalDateTime;

import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.entity.EventManage;

import lombok.Data;

@Data
public class ApplicationInfo {
	
	
	
	private EventInfo eventInfo = new EventInfo();
	private Integer lastOrder;
	private LocalDateTime lastApplyDate;
	

	public ApplicationInfo() {
		
	}
	public void setLastApplication(EventApplicationLog eventApplicationLog) {
		
	}
	
	public ApplicationInfo(EventApplication eventApplication, EventManage eventManage) {
		this.setEventInfo(new EventInfo(eventManage));
		EventApplicationLog lastEventApplicationLog = eventApplication.getLastApplicationLog();
		this.lastOrder = lastEventApplicationLog.getOrder();
		this.lastApplyDate = lastEventApplicationLog.getApplyDate();
	}
	
	public ApplicationInfo(EventApplication eventApplication, EventManage eventManage, String channel) {
		this.setEventInfo(new EventInfo(eventManage));
		EventApplicationLog lastEventApplicationLog = eventApplication.getLastApplicationLog(channel);
		this.lastOrder = lastEventApplicationLog.getOrder();
		this.lastApplyDate = lastEventApplicationLog.getApplyDate();
	}
	
	
}
