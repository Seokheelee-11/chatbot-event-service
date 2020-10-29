package com.shinhancard.chatbot.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Getter;

@Getter
public class EventTarget {
	@Id
	private String id;
	private String targetId;
	private List<String> clnns;
	
}
