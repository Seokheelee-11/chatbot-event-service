package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.dto.request.EventApplicationRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationResponse;
import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.repository.EventApplicationRepository;
import com.shinhancard.chatbot.repository.EventManageRepository;

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

	public List<EventApplicationResponse> getEvents() {
		List<EventApplicationResponse> eventApplicationResponses = new ArrayList<EventApplicationResponse>();
		return eventApplicationResponses;
	}

	public EventApplicationResponse getEventById(String id) {
		EventApplicationResponse eventApplicationResponse = new EventApplicationResponse();
		return eventApplicationResponse;
	}

	public EventApplicationResponse applicationEvent(EventApplicationRequest eventApplicationRequest) {
		String eventId = eventApplicationRequest.getEventId();
		EventManage eventManage = eventManageRepository.findOneByEventId(eventId);
		
		String eventType = eventManage.getEventType();
		
		
			
		
		
		EventApplication eventApplication = new EventApplication();
		eventApplicationRepository.save(eventApplication);
		EventApplicationResponse eventApplicationResponse = modelMapper.map(eventApplication, EventApplicationResponse.class);

		log.info("saved entity {}", eventApplicationResponse.toString());

		return eventApplicationResponse;
	}

	public EventApplicationResponse updateEvent(String id, EventApplicationRequest eventApplicationRequest) {

		EventApplication eventApplication = modelMapper.map(eventApplicationRequest, EventApplication.class);
		eventApplication.setId(id);
		eventApplicationRepository.save(eventApplication);
		EventApplicationResponse eventApplicationResponse = modelMapper.map(eventApplication, EventApplicationResponse.class);
		return eventApplicationResponse;
	}

	public void deleteEvent(String id) {

	}

}
