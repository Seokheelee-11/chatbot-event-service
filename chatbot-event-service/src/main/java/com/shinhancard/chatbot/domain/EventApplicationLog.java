package com.shinhancard.chatbot.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.dto.request.EventApplicationRequest;

import lombok.Data;


@Data
public class EventApplicationLog {
	private Integer order;
	private LocalDateTime applyDate;
	private String channel;
	private String rewardName;
	private List<String> comments = new ArrayList<String>();
	
	public EventApplicationLog(EventApplicationRequest eventApplicationRequest) {
		this.order = 1;
		this.applyDate = LocalDateTime.now();
		this.channel = eventApplicationRequest.getChannel();
		this.comments = eventApplicationRequest.getComments();
	}
}
