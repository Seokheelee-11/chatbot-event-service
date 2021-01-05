package com.shinhancard.chatbot.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.RewardCode;
import com.shinhancard.chatbot.domain.RewardHistory;

import lombok.Data;

@Data
public class ApplyManage {
	@Id
	private String id;
	private String eventId;
	
	private Integer limitClnn;
	private Integer limitApplication;
	private Integer ClnnCount;
	private Integer ApplicationCount;
	
	private Boolean isRewardProperty;
	private RewardCode type;
	private List<RewardHistory> rewardHistories = new ArrayList<RewardHistory>();
	
	public ApplyManage() {
		this.ApplicationCount = 0;
		this.ClnnCount = 0;
		this.limitApplication = 0;
		this.limitClnn = 0;
	}

}
