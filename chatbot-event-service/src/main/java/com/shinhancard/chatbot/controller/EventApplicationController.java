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

import com.mongodb.MongoCommandException;
import com.shinhancard.chatbot.dto.request.EventApplicationRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationResponse;
import com.shinhancard.chatbot.service.EventApplicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("eventApplication")
@RequiredArgsConstructor
public class EventApplicationController {

	private final EventApplicationService eventApplicationService;

	@GetMapping
	public List<EventApplicationResponse> getEvents() {
		return eventApplicationService.getEvents();
	}

	@GetMapping("{id}")
	public EventApplicationResponse getEventById(@PathVariable String id) {
		return eventApplicationService.getEventById(id);
	}

	@PostMapping
	public EventApplicationResponse applicationEvent(@RequestBody EventApplicationRequest eventApplicationRequest) {
		log.info("regist request {}", eventApplicationRequest.toString());
		EventApplicationResponse eventApplicationResponse;
//		try {
//			eventApplicationResponse = eventApplicationService.applicationEvent(eventApplicationRequest);
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//			eventApplicationResponse = applicationEvent(eventApplicationRequest);
//			throw e;
//		}
		
		eventApplicationResponse = eventApplicationService.applicationEvent(eventApplicationRequest);
		return eventApplicationResponse;
	}


	public EventApplicationResponse applicationEvent(@RequestBody EventApplicationRequest eventApplicationRequest,
			Integer count) {
		log.info("regist request {}", eventApplicationRequest.toString());
		EventApplicationResponse eventApplicationResponse;
		try {
			eventApplicationResponse = eventApplicationService.applicationEvent(eventApplicationRequest);
		} catch (MongoCommandException e) {
//			e.printStackTrace();
			if (count < 50) {
				count++;
				log.info("재시도 {}", count);
				eventApplicationResponse = applicationEvent(eventApplicationRequest,count);
			}
			throw e;
			
		}
		return eventApplicationResponse;
	}

	@PutMapping("{id}")
	public EventApplicationResponse updateEvent(@PathVariable String id,
			@RequestBody EventApplicationRequest eventApplicationRequest) {
		return eventApplicationService.updateEvent(id, eventApplicationRequest);
	}

	@DeleteMapping("{id}")
	public void deleteEvent(@PathVariable String id) {
		eventApplicationService.deleteEvent(id);
	}
}
