package com.shinhancard.chatbot.dto.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EventTargetRequest {
	private String name;
	private List<String> clnns = new ArrayList<String>();
}
