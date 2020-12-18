package com.shinhancard.chatbot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.config.EventException;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.domain.Reward;
import com.shinhancard.chatbot.domain.RewardCode;
import com.shinhancard.chatbot.domain.RewardInfo;
import com.shinhancard.chatbot.dto.request.EventManageRequest;
import com.shinhancard.chatbot.dto.response.EventManageResponse;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.repository.EventManageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventManageService {
	@Autowired
	private ModelMapper modelMapper;
	private final EventManageRepository eventManageRepository;

	public List<EventManageResponse> getEvents() {
		List<EventManage> eventManages = eventManageRepository.findAll();
		List<EventManageResponse> eventManageResponses = new ArrayList<EventManageResponse>();
		for (EventManage eventManage : eventManages) {
			eventManageResponses.add(modelMapper.map(eventManage, EventManageResponse.class));
		}
		return eventManageResponses;
	}

	public EventManageResponse getEventById(String id) {
		EventManage eventManage = eventManageRepository.findOneById(id);
		EventManageResponse eventManageResponse = new EventManageResponse();
		if (eventManage != null) {
			eventManageResponse = modelMapper.map(eventManage, EventManageResponse.class);
		}
		return eventManageResponse;
	}

	public EventManageResponse registEvent(EventManageRequest eventManageRequest) throws EventException {
		EventManage eventManage = mappingEventManage(eventManageRequest);
		EventManage findEventManage = eventManageRepository.findOneByEventId(eventManageRequest.getEventId());
		EventManageResponse eventManageResponse = new EventManageResponse();
		if (findEventManage == null) {
			eventManageResponse = mappingResponseAndSave(eventManage);
		}

		return eventManageResponse;
	}

	public EventManageResponse updateEvent(String id, EventManageRequest eventManageRequest) {
		EventManage eventManage = mappingEventManageAndId(eventManageRequest, id);
		eventManageRepository.save(eventManage);
		EventManageResponse eventManageResponse = modelMapper.map(eventManage, EventManageResponse.class);
		return eventManageResponse;
	}

	public void deleteEvent(String id) {
		eventManageRepository.deleteById(id);
	}

	public EventManageResponse mappingResponseAndSave(EventManage eventManage) throws EventException {
		LocalDateTime startDate = eventManage.getDefaultInfo().getStartDate();
		LocalDateTime endDate = eventManage.getDefaultInfo().getEndDate();
		EventManageResponse eventManageResponse = new EventManageResponse();
		if (startDate.isBefore(endDate) && checkRewardConfig(eventManage)) {
//TODO :: 나중에 서비스가 커지면 entity를 하나 더 만들거임. 신청 별로 한줄로 받을 수 있게, 그때 reward id를 넣어서 서로 join할 수 있게 만들거임
//			eventManage = setRewardId(eventManage);
			eventManageRepository.save(eventManage);
			eventManageResponse = modelMapper.map(eventManage, EventManageResponse.class);
		} else {
			throw new EventException(ResultCode.FAILED);
		}

		return eventManageResponse;
	}

	public Boolean checkRewardConfig(EventManage eventManage) {
		Boolean result = true;
		Reward reward = eventManage.getReward();
		List<RewardInfo> rewardInfoes = reward.getInfoes();
		if (reward.getIsProperty()) {
			if (reward.getType().equals(RewardCode.DRAWROTS)) {
				for (RewardInfo rewardInfo : rewardInfoes) {
					if (rewardInfo.getProbability() <= 0.0) {
						result = false;
						break;
					}
				}
			}

			for (RewardInfo rewardInfo : rewardInfoes) {
				if (rewardInfo.getLimit() < 0) {
					result = false;
					break;
				}
			}

		}
		return result;

	}

	// TODO :: 나중에 서비스가 커지면 entity를 하나 더 만들거임. 신청 별로 한줄로 받을 수 있게, 그때 reward id를 넣어서
	// 서로 join할 수 있게 만들거임
//	public EventManage setRewardId(EventManage eventManage) {
//		for (RewardInfo reward : eventManage.getReward().getInfoes()) {
//			if (StringUtils.isEmpty(reward.getId())) {
//				UUID uid = UUID.randomUUID();
//				reward.setId(uid.toString());
//			}
//		}
//		return eventManage;
//	}

	public EventManage mappingEventManageAndId(EventManageRequest eventManageRequest, String id) {
		EventManage eventManage = modelMapper.map(eventManageRequest, EventManage.class);
		eventManage.setId(id);
		return eventManage;
	}

	public EventManage mappingEventManage(EventManageRequest eventManageRequest) {
		EventManage eventManage = modelMapper.map(eventManageRequest, EventManage.class);
		return eventManage;
	}

}
