package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.ResultCode;

import lombok.Data;

@Data
public class EventTargetResponse {
	// 결과메세지
	private ResultCode resultCode;
	private String resultMessage;
	
	private String name;
	private List<String> clnns = new ArrayList<String>();
}
