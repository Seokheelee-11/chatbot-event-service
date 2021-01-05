package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.domain.RewardHistory;
import com.shinhancard.chatbot.domain.RewardInfo;
import com.shinhancard.chatbot.entity.ApplyManage;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.repository.ApplyManageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyManageService {
	private final ApplyManageRepository rewardManageRepository;

	public void registApplyManage(EventManage eventManage) {
		ApplyManage rewardManage = setDefaultApplyManage(eventManage);
		rewardManage.setRewardHistories(getRewardHistories(eventManage));
		rewardManageRepository.save(rewardManage);
	}
	
	public ApplyManage setDefaultApplyManage(EventManage eventManage) {
		ApplyManage rewardManage = new ApplyManage();
		rewardManage.setEventId(eventManage.getEventId());
		rewardManage.setType(eventManage.getReward().getType());
		rewardManage.setLimitApplication(eventManage.getDefaultInfo().getLimitApplication());
		rewardManage.setLimitClnn(eventManage.getDefaultInfo().getLimitClnn());
		rewardManage.setIsRewardProperty(eventManage.getReward().getIsProperty());
		return rewardManage;
		
	}

	public List<RewardHistory> getRewardHistories(EventManage eventManage) {
		List<RewardHistory> rewardHistories = new ArrayList<RewardHistory>();
		for (RewardInfo rewardInfo : eventManage.getReward().getInfoes()) {
			RewardHistory rewardHistory = new RewardHistory();
			rewardHistory.setName(rewardInfo.getName());
			rewardHistory.setLimit(rewardInfo.getLimit());
			rewardHistory.setProbability(rewardInfo.getProbability());
			rewardHistory.setApplyCount(0);
			rewardHistories.add(rewardHistory);
		}
		return rewardHistories;
	}

	public List<RewardHistory> getUpdateRewardHistories(ApplyManage rewardManage, EventManage eventManage) {
		List<RewardHistory> rewardHistories = rewardManage.getRewardHistories();
		for (RewardInfo rewardInfo : eventManage.getReward().getInfoes()) {
			for (int i = 0; i < rewardHistories.size(); i++) {
				RewardHistory tempHistory = rewardHistories.get(i);
				if (StringUtils.equals(rewardInfo.getName(), tempHistory.getName())) {
					RewardHistory rewardHistory = tempHistory;
					rewardHistory.setLimit(rewardInfo.getLimit());
					rewardHistory.setProbability(rewardInfo.getProbability());
					rewardHistories.set(i, rewardHistory);
					break;
				}
			}
		}
		return rewardHistories;
	}
}