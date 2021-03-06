package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shinhancard.chatbot.domain.ApplicationInfo;
import com.shinhancard.chatbot.dto.request.EventApplicationInfoRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationInfoResponse;
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

	public EventApplicationInfoResponse getEventApplicationInfo(
			EventApplicationInfoRequest eventApplicationInfoRequest) {
		EventApplicationInfoResponse eventApplicationInfoResponse = new EventApplicationInfoResponse();

		eventApplicationInfoResponse = mappingEventApplicationInfo(eventApplicationInfoRequest);

		return eventApplicationInfoResponse;
	}

	public EventApplicationInfoResponse mappingEventApplicationInfo(
			EventApplicationInfoRequest eventApplicationInfoRequest) {
		EventApplicationInfoResponse eventApplicationInfoResponse = new EventApplicationInfoResponse();

		String eventId = eventApplicationInfoRequest.getEventId();
		String clnn = eventApplicationInfoRequest.getClnn();
		String channel = eventApplicationInfoRequest.getChannel();

		List<EventApplication> findEventApplications = new ArrayList<>();
		eventApplicationInfoResponse.setClnn(clnn);

		if (StringUtils.isEmpty(eventId)) {
			findEventApplications = eventApplicationRepository.findAllByClnn(clnn);
		} else {
			findEventApplications = eventApplicationRepository.findAllByEventIdAndClnn(eventId, clnn);
		}

		if (findEventApplications != null) {
			for (EventApplication findEventApplication : findEventApplications) {
				String findEventId = findEventApplication.getEventId();
				EventManage findEventManage = eventManageRepository.findOneByEventId(findEventId);

				if (StringUtils.isEmpty(channel)) {
					eventApplicationInfoResponse
							.addApplicationInfo(new ApplicationInfo(findEventApplication, findEventManage));
				} else {
					if (findEventApplication.getApplicationLogs(channel).isEmpty()) {
					} else {
						eventApplicationInfoResponse.addApplicationInfo(
								new ApplicationInfo(findEventApplication, findEventManage, channel));
					}
				}
			}
		}

		return eventApplicationInfoResponse;
	}
}
