package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.shinhancard.chatbot.config.EventException;
import com.shinhancard.chatbot.domain.ApplicationInfo;
import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.dto.request.OneEventApplicationInfoRequest;
import com.shinhancard.chatbot.dto.request.TotalEventApplicationInfoRequest;
import com.shinhancard.chatbot.dto.response.OneEventApplicationInfoResponse;
import com.shinhancard.chatbot.dto.response.TotalEventApplicationInfoResponse;
import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.repository.EventApplicationRepository;
import com.shinhancard.chatbot.repository.EventManageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventApplicationInfoService {
	@Autowired
	private final EventManageRepository eventManageRepository;
	private final EventApplicationRepository eventApplicationRepository;

	public OneEventApplicationInfoResponse getOneEventApplicationInfo(
			OneEventApplicationInfoRequest oneEventApplicationInfoRequest) throws EventException {
		OneEventApplicationInfoResponse oneEventApplicationInfoResponse = new OneEventApplicationInfoResponse();

		oneEventApplicationInfoResponse = mappingOneEventApplicationInfo(oneEventApplicationInfoRequest);

		return oneEventApplicationInfoResponse;
	}

	public TotalEventApplicationInfoResponse getTotalEventApplicationInfo(
			TotalEventApplicationInfoRequest totalEventApplicationInfoRequest) throws EventException {
		TotalEventApplicationInfoResponse totalEventApplicationInfoResponse = new TotalEventApplicationInfoResponse();

		totalEventApplicationInfoResponse = mappingTotalEventApplicationInfo(totalEventApplicationInfoRequest);

		return totalEventApplicationInfoResponse;
	}

	public OneEventApplicationInfoResponse mappingOneEventApplicationInfo(
			OneEventApplicationInfoRequest oneEventApplicationInfoRequest) throws EventException {
		OneEventApplicationInfoResponse oneEventApplicationInfoResponse = new OneEventApplicationInfoResponse();

		String eventId = oneEventApplicationInfoRequest.getEventId();
		String clnn = oneEventApplicationInfoRequest.getClnn();
		String channel = oneEventApplicationInfoRequest.getChannel();
//		ResultCode resultCode = ResultCode.FAILED;
		EventApplication findEventApplication = new EventApplication();
		oneEventApplicationInfoResponse.setClnn(clnn);
		findEventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);
		if (findEventApplication != null) {
			EventManage findEventManage = eventManageRepository.findOneByEventId(eventId);
			oneEventApplicationInfoResponse.setEventInfo(new EventInfo(findEventManage));
			if (StringUtils.isEmpty(channel)) {
				oneEventApplicationInfoResponse.setEventApplicationLogs(findEventApplication);
			} else {
				oneEventApplicationInfoResponse.setEventApplicationLogs(findEventApplication, channel);
			}
		}
		if (CollectionUtils.isEmpty(oneEventApplicationInfoResponse.getEventApplicationLogs())) {
			throw new EventException(ResultCode.FAILED);
		}
//		oneEventApplicationInfoResponse.setResultCodeMessage(new ResultCodeMessage(resultCode));
		return oneEventApplicationInfoResponse;
	}

	public TotalEventApplicationInfoResponse mappingTotalEventApplicationInfo(
			TotalEventApplicationInfoRequest totalEventApplicationInfoRequest) throws EventException {
		TotalEventApplicationInfoResponse totalEventApplicationInfoResponse = new TotalEventApplicationInfoResponse();

		String clnn = totalEventApplicationInfoRequest.getClnn();
		String channel = totalEventApplicationInfoRequest.getChannel();

		List<EventApplication> findEventApplications = new ArrayList<>();
		totalEventApplicationInfoResponse.setClnn(clnn);
		findEventApplications = eventApplicationRepository.findAllByClnn(clnn);
		if (findEventApplications != null) {
			for (EventApplication findEventApplication : findEventApplications) {
				String findEventId = findEventApplication.getEventId();
				EventManage findEventManage = eventManageRepository.findOneByEventId(findEventId);
				if (StringUtils.isEmpty(channel)) {
					totalEventApplicationInfoResponse
							.addApplicationInfo(new ApplicationInfo(findEventApplication, findEventManage));
				} else {
					if (findEventApplication.getApplicationLogs(channel).isEmpty()) {
					} else {
						totalEventApplicationInfoResponse.addApplicationInfo(
								new ApplicationInfo(findEventApplication, findEventManage, channel));
					}
				}
			}
		}
		if (CollectionUtils.isEmpty(totalEventApplicationInfoResponse.getApplicationInfoes())) {
			throw new EventException(ResultCode.FAILED);
		}
		return totalEventApplicationInfoResponse;
	}
}
