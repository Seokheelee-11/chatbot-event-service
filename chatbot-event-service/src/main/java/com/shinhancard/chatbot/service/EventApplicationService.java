package com.shinhancard.chatbot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.domain.EventApplicationLog;
import com.shinhancard.chatbot.domain.PropertyCode;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.dto.request.EventApplicationRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationResponse;
import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.entity.EventTarget;
import com.shinhancard.chatbot.repository.EventApplicationRepository;
import com.shinhancard.chatbot.repository.EventManageRepository;
import com.shinhancard.chatbot.repository.EventTargetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventApplicationService {
	@Autowired
	private ModelMapper modelMapper;
	private final EventApplicationRepository eventApplicationRepository;
	private final EventManageRepository eventManageRepository;
	private final EventTargetRepository eventTargetRepository;

	public List<EventApplicationResponse> getEvents() {
		List<EventApplicationResponse> eventApplicationResponses = new ArrayList<EventApplicationResponse>();
		return eventApplicationResponses;
	}

	public EventApplicationResponse getEventById(String id) {
		EventApplicationResponse eventApplicationResponse = new EventApplicationResponse();
		return eventApplicationResponse;
	}

	public EventApplicationResponse applicationEvent(EventApplicationRequest eventApplicationRequest) {
		EventManage eventManage = findEventManageByEventApplicationRequest(eventApplicationRequest);
		EventApplicationLog eventApplicationLog = new EventApplicationLog(eventApplicationRequest);
		List<PropertyCode> properties = eventManage.getProperties();
		ResultCode resultCode = ResultCode.SUCCESS;

		if (properties.contains(PropertyCode.DEFAULT)) {
			resultCode = canApplyDate(eventManage, eventApplicationLog, resultCode);
		} 
		
		//resultCode = properties.contains(PropertyCode.DEFAULT) ? canApplyDate(eventManage, eventApplicationLog, resultCode) : resultCode; 

		if (properties.contains(PropertyCode.TARGET)) {
			resultCode = canApplyTarget(eventManage, eventApplicationRequest, resultCode);
		}

		if (properties.contains(PropertyCode.QUIZ)) {
			resultCode = canApplyQuiz(eventManage, eventApplicationRequest, resultCode);
		}

		if (properties.contains(PropertyCode.OVERLAP)) {
			resultCode = canApplyOverLap(eventManage, eventApplicationRequest, resultCode);
			eventApplicationLog.setOrder(getOverLapOrder(eventApplicationRequest, resultCode));
		}

		if (properties.contains(PropertyCode.REWARD)) {
			resultCode = canApplyReward(eventManage, eventApplicationRequest, resultCode);
			eventApplicationLog.setRewardName(getReward(eventManage, resultCode));

		}

		EventApplication eventApplication = new EventApplication();
		eventApplicationRepository.save(eventApplication);
		EventApplicationResponse eventApplicationResponse = modelMapper.map(eventApplication,
				EventApplicationResponse.class);

		log.info("saved entity {}", eventApplicationResponse.toString());

		return eventApplicationResponse;
	}

	public EventApplicationResponse updateEvent(String id, EventApplicationRequest eventApplicationRequest) {
		EventApplication eventApplication = modelMapper.map(eventApplicationRequest, EventApplication.class);
		eventApplication.setId(id);
		eventApplicationRepository.save(eventApplication);
		EventApplicationResponse eventApplicationResponse = modelMapper.map(eventApplication,
				EventApplicationResponse.class);
		return eventApplicationResponse;
	}

	public void deleteEvent(String id) {

	}

	public ResultCode canApplyDate(EventManage eventManage, EventApplicationLog eventApplicationLog,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {
			LocalDateTime startDate = eventManage.getDefaultInfo().getStartDate();
			LocalDateTime endDate = eventManage.getDefaultInfo().getEndDate();
			LocalDateTime ApplyDate = eventApplicationLog.getApplyDate();

			if (startDate.isAfter(ApplyDate) || endDate.isBefore(ApplyDate)) {
				resultCode = ResultCode.FAILED_DATE_ORDER;
			}
		}
		return resultCode;
	}

	public ResultCode canApplyTarget(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {
			String targetName = eventManage.getTarget().getTargetName();
			String nonTargetName = eventManage.getTarget().getNonTargetName();
			EventTarget target = eventTargetRepository.findOneByName(targetName);
			EventTarget nonTarget = eventTargetRepository.findOneByName(nonTargetName);
			List<String> targetClnns = target.getClnns();
			List<String> nonTargetClnns = nonTarget.getClnns();
			List<String> channels = eventManage.getTarget().getChannels();

			if (!targetClnns.contains(eventApplicationRequest.getClnn())) {
				resultCode = ResultCode.FAILED;
			}
			if (nonTargetClnns.contains(eventApplicationRequest.getClnn())) {
				resultCode = ResultCode.FAILED;
			}
			if (channels.contains(eventApplicationRequest.getChannel())) {
				resultCode = ResultCode.FAILED;
			}
		}
		return resultCode;
	}

	// TODO :: 함수 만들 것
	public ResultCode canApplyQuiz(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {
			List<String> comments = eventApplicationRequest.getComments();
			List<String> answers = eventManage.getQuiz().getAnswers();
			if (eventManage.getQuiz().getChecksOneAnswer()) {
				for (String comment : comments) {
					if (answers.contains(comment)) {
						continue;
					} else {
						resultCode = ResultCode.FAILED;
					}
				}
			}
		}
		return resultCode;
	}

	// TODO :: 함수 만들 것
	public ResultCode canApplyOverLap(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {

		}
		return resultCode;
	}

	// TODO :: 함수 만들 것
	public ResultCode canApplyReward(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {

		}
		return resultCode;
	}

	// TODO :: 함수 만들 것
	public Integer getOverLapOrder(EventApplicationRequest eventApplicationRequest, ResultCode resultCode) {
		Integer result = 0;
		if (resultCode.isSuccess()) {

		}
		return result;
	}

	// TODO :: 함수 만들 것
	public String getReward(EventManage eventManage, ResultCode resultCode) {
		String result = "";
		if (resultCode.isSuccess()) {

		}
		return result;
	}

	public EventManage findEventManageByEventApplicationRequest(EventApplicationRequest eventApplicationRequest) {
		String eventId = eventApplicationRequest.getEventId();
		EventManage eventManage = eventManageRepository.findOneByEventId(eventId);
		return eventManage;
	}

	public List<PropertyCode> getEventProperties(EventManage eventManage) {
		List<PropertyCode> properties = new ArrayList<>();
		properties.addAll(eventManage.getProperties());
		return properties;
	}

}
