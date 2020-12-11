package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.EventApplicationLog;
import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.entity.EventApplication;

import lombok.Data;

@Data
public class OneEventApplicationInfoResponse {
	// 결과메세지
	private ResultCode resultCode;
	private String resultMessage;
	private String clnn;
	private EventInfo eventInfo = new EventInfo();
	private List<EventApplicationLog> eventApplicationLogs = new ArrayList<>();

	public OneEventApplicationInfoResponse() {
		
	}
	
	public void setEventApplicationLogs(EventApplication eventApplication) {
		this.eventApplicationLogs = eventApplication.getApplicationLogs();
	}
	public void setEventApplicationLogs(EventApplication eventApplication, String channel) {
		this.eventApplicationLogs = eventApplication.getApplicationLogs(channel);
	}
	

	
}