package com.shinhancard.chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhancard.chatbot.dto.request.EventApplicationInfoRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationInfoResponse;
import com.shinhancard.chatbot.service.EventApplicationInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("eventManage")
@RequiredArgsConstructor
public class EventApplicationInfoController {
	
	private final EventApplicationInfoService eventApplicationInfoService;

	@GetMapping
	public EventApplicationInfoResponse getEventApplicationInfo(
			@RequestBody EventApplicationInfoRequest eventApplicationInfoRequest) {
		return eventApplicationInfoService.getEventApplicationInfo(eventApplicationInfoRequest);
	}


}
