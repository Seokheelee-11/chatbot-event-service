package com.shinhancard.chatbot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhancard.chatbot.dto.request.EventApplicationRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationResponse;
import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.service.EventApplicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("eventApplication")
@RequiredArgsConstructor
public class EventApplicationController {

	private final EventApplicationService eventApplicationService;
	
	@PostMapping
	public EventApplicationResponse applicationEvent(@RequestBody EventApplicationRequest eventApplicationRequest) {
		log.info("regist request {}", eventApplicationRequest.toString());
		EventApplicationResponse eventApplicationResponse;
		eventApplicationResponse = eventApplicationService.applicationEvent(eventApplicationRequest);
		return eventApplicationResponse;
	}
	
	
//  신청 이외의 기능들은 다른 controller에서 대신하고 있음
//	@GetMapping
	public List<EventApplication> getEvents() {
		return eventApplicationService.getEvents();
	}
	
//	@GetMapping("{id}")
	public EventApplication getEventById(@PathVariable String id) {
		return eventApplicationService.getEventById(id);
	}

//	@PutMapping("{id}")
	public EventApplication updateEvent(@PathVariable String id,
			@RequestBody EventApplication eventApplication) {
		return eventApplicationService.updateEvent(id, eventApplication);
	}

//	@DeleteMapping("{id}")
	public void deleteEvent(@PathVariable String id) {
		eventApplicationService.deleteEvent(id);
	}
}
