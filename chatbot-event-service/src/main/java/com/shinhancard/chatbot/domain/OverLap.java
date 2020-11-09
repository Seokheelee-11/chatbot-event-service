package com.shinhancard.chatbot.domain;

import lombok.Data;

@Data
public class OverLap {
	private Boolean isProperty;
	private OverLapCode type;
	private Integer minInterval;
	private Integer maxInterval;
	private Integer limit;
	private Boolean isContinuous;
	private Boolean isStartPastDate;
}
