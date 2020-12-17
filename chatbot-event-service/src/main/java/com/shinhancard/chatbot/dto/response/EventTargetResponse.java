package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.ResultCodeMessage;

import lombok.Data;

@Data
public class EventTargetResponse {
	// 결과메세지	
	private ResultCodeMessage resultCodeMessage = new ResultCodeMessage();
	
	private String name;
	private List<String> clnns = new ArrayList<String>();

}
