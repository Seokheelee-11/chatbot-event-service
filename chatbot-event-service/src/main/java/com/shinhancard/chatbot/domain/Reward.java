package com.shinhancard.chatbot.domain;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Reward {
	private Boolean isProperty;
	private RewardCode type;
	private ArrayList<RewardInfo> infoes = new ArrayList<RewardInfo>();
	
	public Integer getTotalLimit() {
		Integer totalLimit;
		
		this.infoes.forEach(action);
		
	}
	
}
