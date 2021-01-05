package com.shinhancard.chatbot.domain;

import lombok.Data;

@Data
public class RewardHistory {

	private String name;
	private Double probability;
	private Integer limit;
	private Integer applyCount;
	
	public RewardHistory() {
		this.applyCount = 0;
	}
}


