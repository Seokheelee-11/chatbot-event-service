package com.shinhancard.chatbot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhancard.chatbot.config.EventException;
import com.shinhancard.chatbot.dto.request.EventInfoRequest;
import com.shinhancard.chatbot.dto.response.EventInfoResponse;
import com.shinhancard.chatbot.service.EventInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("eventInfo")
@RequiredArgsConstructor
public class EventInfoController {
	
	private final EventInfoService eventInfoService;

	@PostMapping("/totalEvent")
	public EventInfoResponse getEventInfo(
			@RequestBody EventInfoRequest eventInfoRequest) throws EventException {
		return eventInfoService.getEventInfo(eventInfoRequest);
	}
}
