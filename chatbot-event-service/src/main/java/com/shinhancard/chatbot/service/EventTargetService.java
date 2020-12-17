package com.shinhancard.chatbot.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.domain.ResultCodeMessage;
import com.shinhancard.chatbot.dto.request.EventTargetRequest;
import com.shinhancard.chatbot.dto.response.EventTargetResponse;
import com.shinhancard.chatbot.entity.EventTarget;
import com.shinhancard.chatbot.repository.EventTargetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventTargetService {
	@Autowired
	private ModelMapper modelMapper;
	private final EventTargetRepository eventTargetRepository;

	public List<EventTargetResponse> getTargets() {
		List<EventTarget> eventTargets = new ArrayList<EventTarget>();
		eventTargets = eventTargetRepository.findAll();
		List<EventTargetResponse> eventTargetResponses = mappingEventTargetResponses(eventTargets);
		return eventTargetResponses;
	}

	public EventTargetResponse getTargetById(String id) {
		EventTargetResponse eventTargetResponse = new EventTargetResponse();
		return eventTargetResponse;
	}

	public EventTargetResponse registTarget(EventTargetRequest eventTargetRequest) {
		EventTarget eventTarget = modelMapper.map(eventTargetRequest, EventTarget.class);
		eventTargetRepository.save(eventTarget);
		EventTargetResponse eventTargetResponse = modelMapper.map(eventTarget, EventTargetResponse.class);
		ResultCode resultCode = ResultCode.FAILED;
		resultCode = ResultCode.SUCCESS;
		eventTargetResponse.setResultCodeMessage(new ResultCodeMessage(resultCode));
		return eventTargetResponse;
	}

	public EventTargetResponse updateTarget(String id, EventTargetRequest eventTargetRequest) {
		EventTarget eventTarget = mappingEventTargetAndId(eventTargetRequest, id);
		eventTargetRepository.save(eventTarget);
		EventTargetResponse eventTargetResponse = modelMapper.map(eventTarget, EventTargetResponse.class);
		ResultCode resultCode = ResultCode.FAILED;
		resultCode = ResultCode.SUCCESS;
		eventTargetResponse.setResultCodeMessage(new ResultCodeMessage(resultCode));
		return eventTargetResponse;
	}

	public void deleteTarget(String id) {

	}

	public EventTarget mappingEventTargetAndId(EventTargetRequest eventTargetRequest, String id) {
		EventTarget eventTarget = modelMapper.map(eventTargetRequest, EventTarget.class);
		eventTarget.setId(id);
		return eventTarget;
	}

	public EventTarget mappingEventTarget(EventTargetRequest eventManageRequest) {
		EventTarget eventTarget = modelMapper.map(eventManageRequest, EventTarget.class);
		return eventTarget;
	}

	public List<EventTargetResponse> mappingEventTargetResponses(List<EventTarget> eventTargets) {
		List<EventTargetResponse> eventTargetResponses = new ArrayList<EventTargetResponse>();
		eventTargets.forEach(eventTarget -> eventTargetResponses.add(modelMapper.map(eventTarget, EventTargetResponse.class)));
		return eventTargetResponses;
	}

	public void saveEventTarget(EventTarget eventTarget) {
		eventTargetRepository.save(eventTarget);
	}
}
