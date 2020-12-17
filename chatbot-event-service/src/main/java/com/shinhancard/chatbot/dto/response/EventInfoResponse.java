package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.ResultCodeMessage;
import com.shinhancard.chatbot.domain.TimeClassificationCode;

import lombok.Data;

@Data
public class EventInfoResponse {

	// 결과메세지	
	private ResultCodeMessage resultCodeMessage = new ResultCodeMessage();

	private String eventId;
	private String clnn;
	private String channel;
	private TimeClassificationCode timeClassification;
	private List<EventInfo> eventInfoes = new ArrayList<>();
	
	public void addEventInfo(EventInfo eventInfo) {
		this.eventInfoes.add(eventInfo);
	}

}