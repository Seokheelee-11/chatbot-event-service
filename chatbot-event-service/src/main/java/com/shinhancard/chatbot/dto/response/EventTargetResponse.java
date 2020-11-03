package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EventTargetResponse {
	private String name;
	private List<String> clnns = new ArrayList<String>();
}
