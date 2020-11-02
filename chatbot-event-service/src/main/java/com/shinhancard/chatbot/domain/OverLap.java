package com.shinhancard.chatbot.domain;

import lombok.Data;

@Data
public class OverLap {
	private Boolean isProperty;
	private OverLapCode type;
	private Integer interval;
	private Integer limit;
	private Boolean isContinuous;
	private Boolean includesPreviousDate;
}
