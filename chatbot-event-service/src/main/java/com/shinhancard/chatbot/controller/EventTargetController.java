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

import com.shinhancard.chatbot.dto.request.EventTargetRequest;
import com.shinhancard.chatbot.dto.response.EventTargetResponse;
import com.shinhancard.chatbot.service.EventTargetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("eventTarget")
@RequiredArgsConstructor
public class EventTargetController {
	
	private final EventTargetService eventTargetService;
	

	@GetMapping
	public List<EventTargetResponse> getTargets() {
		return eventTargetService.getTargets();
	}
	
	@GetMapping("{id}")
	public EventTargetResponse getTargetById(@PathVariable String id) {
		return eventTargetService.getTargetById(id);
	}
	
	@PostMapping
	public EventTargetResponse registTarget(@RequestBody EventTargetRequest eventTargetRequest) {
		log.info("regist request {}", eventTargetRequest.toString());
		return eventTargetService.registTarget(eventTargetRequest);
	}
	
	@PutMapping("{id}")
	public EventTargetResponse updateTarget(@PathVariable String id, @RequestBody EventTargetRequest eventTargetRequest) {
		return eventTargetService.updateTarget(id, eventTargetRequest);
	}
	
	@DeleteMapping("{id}")
	public void deleteTarget(@PathVariable String id) {
		eventTargetService.deleteTarget(id);
	}
}
