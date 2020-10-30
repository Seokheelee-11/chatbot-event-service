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

import com.shinhancard.chatbot.domain.past.EventInfo;
import com.shinhancard.chatbot.domain.past.EventInfoRequest;
import com.shinhancard.chatbot.domain.past.EventInfoResponse;
import com.shinhancard.chatbot.repository.EventInfoRepository;
import com.shinhancard.chatbot.service.EventInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("eventInfo")
@RequiredArgsConstructor
public class EventInfoController {
	
	private final EventInfoService eventInfoService;
	private final EventInfoRepository eventInfoRepository;

	@GetMapping
	public List<EventInfo> getEvents() {
		return eventInfoRepository.findAll();
	}
	
	@GetMapping("{id}")
	public EventInfo getEventById(@PathVariable String id) {
		return eventInfoService.getEventById(id);
	}
	
	@PostMapping
	public EventInfoResponse registEvent(@RequestBody EventInfoRequest eventInfoRequest) {
		log.info("apply request {}", eventInfoRequest.toString());
		return eventInfoService.registEvent(eventInfoRequest);
	}
	
	@PutMapping("{id}")
	public EventInfo updateEvent(@PathVariable String id, @RequestBody EventInfo event) {
		return eventInfoService.updateEvent(id, event);
	}
	
	@DeleteMapping("{id}")
	public void deleteEvent(@PathVariable String id) {
		eventInfoService.deleteEvent(id);
	}
}
