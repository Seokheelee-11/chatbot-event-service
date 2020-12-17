package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shinhancard.chatbot.domain.ApplicationInfo;
import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.domain.ResultCodeMessage;
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
			OneEventApplicationInfoRequest oneEventApplicationInfoRequest) {
		OneEventApplicationInfoResponse oneEventApplicationInfoResponse = new OneEventApplicationInfoResponse();

		oneEventApplicationInfoResponse = mappingOneEventApplicationInfo(oneEventApplicationInfoRequest);

		return oneEventApplicationInfoResponse;
	}
	
	public TotalEventApplicationInfoResponse getTotalEventApplicationInfo(
			TotalEventApplicationInfoRequest totalEventApplicationInfoRequest) {
		TotalEventApplicationInfoResponse totalEventApplicationInfoResponse = new TotalEventApplicationInfoResponse();
		
		totalEventApplicationInfoResponse = mappingTotalEventApplicationInfo(totalEventApplicationInfoRequest);
		
		return totalEventApplicationInfoResponse;
	}


	public OneEventApplicationInfoResponse mappingOneEventApplicationInfo(
			OneEventApplicationInfoRequest oneEventApplicationInfoRequest) {
		OneEventApplicationInfoResponse oneEventApplicationInfoResponse = new OneEventApplicationInfoResponse();

		String eventId = oneEventApplicationInfoRequest.getEventId();
		String clnn = oneEventApplicationInfoRequest.getClnn();
		String channel = oneEventApplicationInfoRequest.getChannel();
		ResultCode resultCode = ResultCode.FAILED;
		EventApplication findEventApplication = new EventApplication();
		oneEventApplicationInfoResponse.setClnn(clnn);
		findEventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);
		if (findEventApplication != null) {
			resultCode = ResultCode.SUCCESS;			
			EventManage findEventManage = eventManageRepository.findOneByEventId(eventId);
			oneEventApplicationInfoResponse.setEventInfo(new EventInfo(findEventManage));
			if (StringUtils.isEmpty(channel)) {
				oneEventApplicationInfoResponse.setEventApplicationLogs(findEventApplication);
			} else {
				oneEventApplicationInfoResponse.setEventApplicationLogs(findEventApplication,channel);
			}
		}
		oneEventApplicationInfoResponse.setResultCodeMessage(new ResultCodeMessage(resultCode));
		return oneEventApplicationInfoResponse;
	}

	
	public TotalEventApplicationInfoResponse mappingTotalEventApplicationInfo(
			TotalEventApplicationInfoRequest totalEventApplicationInfoRequest) {
		TotalEventApplicationInfoResponse totalEventApplicationInfoResponse = new TotalEventApplicationInfoResponse();
		
		String clnn = totalEventApplicationInfoRequest.getClnn();
		String channel = totalEventApplicationInfoRequest.getChannel();
		ResultCode resultCode = ResultCode.FAILED;
		List<EventApplication> findEventApplications = new ArrayList<>();
		totalEventApplicationInfoResponse.setClnn(clnn);
		findEventApplications = eventApplicationRepository.findAllByClnn(clnn);
		if (findEventApplications != null) {
			resultCode = ResultCode.SUCCESS;
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
		totalEventApplicationInfoResponse.setResultCodeMessage(new ResultCodeMessage(resultCode));
		return totalEventApplicationInfoResponse;
	}

	
	
}
