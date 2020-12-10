package com.shinhancard.chatbot.domain;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class Reward {
	private Boolean isProperty;
	private RewardCode type;
	private ArrayList<RewardInfo> infoes = new ArrayList<RewardInfo>();
	
	public Integer getTotalLimit() {
		Integer totalLimit=0;
		for(RewardInfo info : this.infoes) {
			totalLimit += info.getLimit();
		}
		return totalLimit;
	}
	public RewardInfo getInfoByRewardName(String rewardName) {
		RewardInfo resultRewardInfo = new RewardInfo();
		for(RewardInfo rewardInfo : this.infoes) {
			if(StringUtils.equals(rewardName, rewardInfo.getName())) {
				resultRewardInfo = rewardInfo;
				break;
			}
		}
		return resultRewardInfo;
	}
	
	
}
