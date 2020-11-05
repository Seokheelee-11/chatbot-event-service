package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		List<EventManageResponse> eventManageResponses = new ArrayList<EventManageResponse>();
		return eventManageResponses;
	}

	public EventManageResponse getEventById(String id) {
		EventManageResponse eventManageResponse = new EventManageResponse();
		return eventManageResponse;
	}

	public EventManageResponse registEvent(EventManageRequest eventManageRequest) {
		EventManage eventManage = mappingEventManage(eventManageRequest);
		eventManageRepository.save(eventManage);
		EventManageResponse eventManageResponse = modelMapper.map(eventManage, EventManageResponse.class);
		
		log.info("saved entity {}", eventManageResponse.toString());

		return eventManageResponse;
	}

	public EventManageResponse updateEvent(String id, EventManageRequest eventManageRequest) {

		EventManage eventManage =  mappingEventManageAndId(eventManageRequest, id);
		eventManageRepository.save(eventManage);
		EventManageResponse eventManageResponse = modelMapper.map(eventManage, EventManageResponse.class);
		return eventManageResponse;
	}

	public void deleteEvent(String id) {

	}
	
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
