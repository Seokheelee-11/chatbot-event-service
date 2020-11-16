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

import com.shinhancard.chatbot.dto.request.EventManageRequest;
import com.shinhancard.chatbot.dto.response.EventManageResponse;
import com.shinhancard.chatbot.service.EventManageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("eventManage")
@RequiredArgsConstructor
public class EventInfoController {
	
	private final EventManageService eventManageService;
	

	@GetMapping
	public List<EventManageResponse> getEvents() {
		return eventManageService.getEvents();
	}
	
	@GetMapping("{id}")
	public EventManageResponse getEventById(@PathVariable String id) {
		return eventManageService.getEventById(id);
	}
	
	@PostMapping
	public EventManageResponse registEvent(@RequestBody EventManageRequest eventManageRequest) {
		log.info("regist request {}", eventManageRequest.toString());
		return eventManageService.registEvent(eventManageRequest);
	}
	
	@PutMapping("{id}")
	public EventManageResponse updateEvent(@PathVariable String id, @RequestBody EventManageRequest eventManageRequest) {
		return eventManageService.updateEvent(id, eventManageRequest);
	}
	
	@DeleteMapping("{id}")
	public void deleteEvent(@PathVariable String id) {
		eventManageService.deleteEvent(id);
	}
}
