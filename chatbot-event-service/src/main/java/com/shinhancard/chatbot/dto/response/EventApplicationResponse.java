package com.shinhancard.chatbot.dto.response;

import com.shinhancard.chatbot.domain.EventApplicationLog;
import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.ResponseInfo;
import com.shinhancard.chatbot.domain.ResultCodeMessage;

import lombok.Data;

@Data
public class EventApplicationResponse {
//	private ResultCode resultCode;
//	private String resultMessage;
	// 결과메세지	
	private ResultCodeMessage resultCodeMessage = new ResultCodeMessage();

	private String clnn;
	private EventInfo eventInfo = new EventInfo();
	private EventApplicationLog eventApplicationLog = new EventApplicationLog();
	private ResponseInfo responseInfo = new ResponseInfo();

}