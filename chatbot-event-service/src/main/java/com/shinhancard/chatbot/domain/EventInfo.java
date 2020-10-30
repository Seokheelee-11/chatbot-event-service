package com.shinhancard.chatbot.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventInfo {
	private String EventId;
	private String name;
	private String discription;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
