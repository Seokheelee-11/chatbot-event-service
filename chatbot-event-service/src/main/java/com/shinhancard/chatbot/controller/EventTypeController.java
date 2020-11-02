package com.shinhancard.chatbot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhancard.chatbot.dto.request.EventTypeRequest;
import com.shinhancard.chatbot.dto.response.EventTypeResponse;
import com.shinhancard.chatbot.service.EventTypeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("eventType")
@RequiredArgsConstructor
public class EventTypeController {
	
	private final EventTypeService eventTypeService;
	

	@GetMapping
	public List<EventTypeResponse> getEvents() {
		return eventTypeService.getEvents();
	}
	
	@GetMapping("{id}")
	public EventTypeResponse getEventById(@PathVariable String id) {
		return eventTypeService.getEventById(id);
	}
	
	@PostMapping
	public EventTypeResponse registEvent(@RequestBody EventTypeRequest eventTypeRequest) {
		log.info("regist request {}", eventTypeRequest.toString());
		return eventTypeService.registEvent(eventTypeRequest);
	}
	
	@PutMapping("{id}")
	public EventTypeResponse updateEvent(@PathVariable String id, @RequestBody EventTypeRequest eventTypeRequest) {
		return eventTypeService.updateEvent(id, eventTypeRequest);
	}
	
	@DeleteMapping("{id}")
	public void deleteEvent(@PathVariable String id) {
		eventTypeService.deleteEvent(id);
	}
}
