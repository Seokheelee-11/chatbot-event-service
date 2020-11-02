package com.shinhancard.chatbot.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DefaultInfo {
	private Boolean isProperty;
	private String name;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String description;

}
