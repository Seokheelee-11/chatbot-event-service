package com.shinhancard.chatbot.service;

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
import com.shinhancard.chatbot.entity.EventType;
import com.shinhancard.chatbot.repository.EventApplicationRepository;
import com.shinhancard.chatbot.repository.EventManageRepository;
import com.shinhancard.chatbot.repository.EventTypeRepository;

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
	private final EventTypeRepository eventTypeRepository;

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
		List<PropertyCode> properties = getEventProperties(eventManage);
		EventApplicationLog eventApplicationLog = new EventApplicationLog(eventApplicationRequest);
		ResultCode resultCode = ResultCode.SUCCESS;		
		
		if (properties.contains(PropertyCode.DEFAULT)) {
			resultCode = canApplyDate(eventApplicationLog, resultCode);
		}
		
		if (properties.contains(PropertyCode.TARGET)) {
			resultCode = canApplyTarget(eventManage, resultCode);
		}

		if (properties.contains(PropertyCode.RESPONSE)) {

		}

		if (properties.contains(PropertyCode.OVERLAP)) {

		}

		if (properties.contains(PropertyCode.QUIZ)) {

		}

		if (properties.contains(PropertyCode.REWARD)) {

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

	
	//TODO :: 함수 만들 것
	public ResultCode canApplyDate(EventApplicationLog eventApplicationLog, ResultCode resultCode) {

		return resultCode;
	}
	
	
	public EventManage findEventManageByEventApplicationRequest(EventApplicationRequest eventApplicationRequest) {
		String eventId = eventApplicationRequest.getEventId();
		EventManage eventManage = eventManageRepository.findOneByEventId(eventId);
		return eventManage;
	}

	public List<PropertyCode> getEventProperties(EventManage eventManage) {
		String id = eventManage.getEventType().getId();
		String type = eventManage.getEventType().getType();
		EventType eventType = eventTypeRepository.findOneByIdAndType(id, type);
		return eventType.getProperties();
	}

}
