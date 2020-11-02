package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.dto.request.EventTypeRequest;
import com.shinhancard.chatbot.dto.response.EventTypeResponse;
import com.shinhancard.chatbot.entity.EventType;
import com.shinhancard.chatbot.repository.EventTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventTypeService {
	@Autowired
	private ModelMapper modelMapper;
	private final EventTypeRepository eventTypeRepository;

	public List<EventTypeResponse> getEvents() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		eventTypes = eventTypeRepository.findAll();
		List<EventTypeResponse> eventTypeResponses = mappingEventTypeResponses(eventTypes);
		return eventTypeResponses;
	}

	public EventTypeResponse getEventById(String id) {
		EventTypeResponse eventTypeResponse = new EventTypeResponse();
		return eventTypeResponse;
	}

	public EventTypeResponse registEvent(EventTypeRequest eventTypeRequest) {
		EventType eventType = modelMapper.map(eventTypeRequest, EventType.class);
		eventTypeRepository.save(eventType);
		EventTypeResponse eventTypeResponse = modelMapper.map(eventType, EventTypeResponse.class);
		log.info("saved entity {}", eventTypeResponse.toString());
		return eventTypeResponse;
	}

	public EventTypeResponse updateEvent(String id, EventTypeRequest eventTypeRequest) {
		EventType eventType = mappingEventTypeAndId(eventTypeRequest, id);
		eventTypeRepository.save(eventType);
		EventTypeResponse eventTypeResponse = modelMapper.map(eventType, EventTypeResponse.class);
		return eventTypeResponse;
	}

	public void deleteEvent(String id) {

	}

	public EventType mappingEventTypeAndId(EventTypeRequest eventTypeRequest, String id) {
		EventType eventType = modelMapper.map(eventTypeRequest, EventType.class);
		eventType.setId(id);
		return eventType;
	}

	public EventType mappingEventType(EventTypeRequest eventManageRequest) {
		EventType eventType = modelMapper.map(eventManageRequest, EventType.class);
		return eventType;
	}

	public List<EventTypeResponse> mappingEventTypeResponses(List<EventType> eventTypes) {
		List<EventTypeResponse> eventTypeResponses = new ArrayList<EventTypeResponse>();
		eventTypes.forEach(eventType -> eventTypeResponses.add(modelMapper.map(eventType, EventTypeResponse.class)));
		return eventTypeResponses;
	}

	public void saveEventType(EventType eventType) {
		eventTypeRepository.save(eventType);
	}
}
