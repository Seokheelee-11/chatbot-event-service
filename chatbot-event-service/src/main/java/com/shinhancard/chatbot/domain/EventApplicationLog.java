package com.shinhancard.chatbot.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class EventApplicationLog {
	private Integer order;
	private LocalDateTime applyDate;
	private String channel;
	private String rewardName;
	private List<String> comments = new ArrayList<String>();
}
