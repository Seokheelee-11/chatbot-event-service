package com.shinhancard.chatbot.dto.response;

import com.shinhancard.chatbot.domain.EventApplicationLog;
import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.ResultCode;

import lombok.Data;

@Data
public class EventApplicationResponse {

	// 결과메세지
	private ResultCode resultCode;
	private String resultMessage;

	private String clnn;
	private EventInfo eventInfo = new EventInfo();
	private EventApplicationLog eventApplicationLog;
}