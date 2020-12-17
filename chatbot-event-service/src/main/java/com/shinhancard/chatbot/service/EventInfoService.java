package com.shinhancard.chatbot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.PropertyCode;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.domain.ResultCodeMessage;
import com.shinhancard.chatbot.domain.TimeClassificationCode;
import com.shinhancard.chatbot.dto.request.EventInfoRequest;
import com.shinhancard.chatbot.dto.response.EventInfoResponse;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.entity.EventTarget;
import com.shinhancard.chatbot.repository.EventManageRepository;
import com.shinhancard.chatbot.repository.EventTargetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventInfoService {
	@Autowired
	private final EventManageRepository eventManageRepository;
	private final EventTargetRepository eventTargetRepository;

	public EventInfoResponse getEventInfo(EventInfoRequest eventInfoRequest) {
		EventInfoResponse eventInfoResponse = new EventInfoResponse();

		eventInfoResponse = mappingEventInfo(eventInfoRequest);

		return eventInfoResponse;
	}

	public EventInfoResponse mappingEventInfo(EventInfoRequest eventInfoRequest) {
		EventInfoResponse eventInfoResponse = new EventInfoResponse();
		ResultCode resultCode = ResultCode.FAILED;
		String eventId = eventInfoRequest.getEventId();
		String clnn = eventInfoRequest.getClnn();
		String channel = eventInfoRequest.getChannel();
		TimeClassificationCode timeClassification = eventInfoRequest.getTimeClassification();

		List<EventManage> eventManages = new ArrayList<>();
		eventInfoResponse.setClnn(clnn);
		eventInfoResponse.setTimeClassification(timeClassification);
		eventInfoResponse.setChannel(channel);

		if (StringUtils.isEmpty(eventId)) {
			eventManages = eventManageRepository.findAll();
		} else {
			eventManages = eventManageRepository.findAllByEventId(eventId);
		}

		for (EventManage eventManage : eventManages) {
			if (isTimeRight(eventManage, timeClassification) && isTarget(eventManage, clnn) && isChannelRight(eventManage, channel)) {
				resultCode = ResultCode.SUCCESS;
				eventInfoResponse.addEventInfo(new EventInfo(eventManage));
			}
		}
		eventInfoResponse.setResultCodeMessage(new ResultCodeMessage(resultCode));
		return eventInfoResponse;
	}
	
	public Boolean isChannelRight(EventManage eventManage, String channel) {
		Boolean result = false;
		if (StringUtils.isEmpty(channel)) {
			result =  true;
		}

		if (eventManage.getProperties().contains(PropertyCode.TARGET)) {
			List<String> channels = eventManage.getTarget().getChannels();
			if (CollectionUtils.isEmpty(channels)) {
				result = true;
			} else if (channels.contains(channel)) {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}

	public Boolean isTarget(EventManage eventManage, String clnn) {
		if (eventManage.getTarget().getIsProperty()) {
			String targetName = eventManage.getTarget().getTargetName();
			String nonTargetName = eventManage.getTarget().getNonTargetName();

			EventTarget target = eventTargetRepository.findOneByName(targetName);
			EventTarget nonTarget = eventTargetRepository.findOneByName(nonTargetName);

			if (target != null) {
				if (target.getClnns().contains(clnn)) {
				} else {
					return false;
				}
			}

			if (nonTarget != null) {
				if (nonTarget.getClnns().contains(clnn)) {
					return false;
				}
			}
		}

		return true;
	}

	public Boolean isTimeRight(EventManage eventManage, TimeClassificationCode timeClassification) {
		LocalDateTime startDate = eventManage.getDefaultInfo().getStartDate();
		LocalDateTime endDate = eventManage.getDefaultInfo().getEndDate();
		LocalDateTime nowDate = LocalDateTime.now();
		if (timeClassification == TimeClassificationCode.PRESENT) {
			if (startDate.isBefore(nowDate) && endDate.isAfter(nowDate)) {
				return true;
			} else {
				return false;
			}
		} else if (timeClassification == TimeClassificationCode.FUTURE) {
			if (startDate.isAfter(nowDate) && endDate.isAfter(nowDate)) {
				return true;
			} else {
				return false;
			}
		} else if (timeClassification == TimeClassificationCode.FUTURE_FROM_NOW) {
			if (endDate.isAfter(nowDate)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
