package com.shinhancard.chatbot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhancard.chatbot.config.EventException;
import com.shinhancard.chatbot.dto.request.OneEventApplicationInfoRequest;
import com.shinhancard.chatbot.dto.request.TotalEventApplicationInfoRequest;
import com.shinhancard.chatbot.dto.response.OneEventApplicationInfoResponse;
import com.shinhancard.chatbot.dto.response.TotalEventApplicationInfoResponse;
import com.shinhancard.chatbot.service.EventApplicationInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("eventApplicationInfo")
@RequiredArgsConstructor
public class EventApplicationInfoController {
	
	private final EventApplicationInfoService eventApplicationInfoService;

	@PostMapping("/oneEvent")
	public OneEventApplicationInfoResponse getOneEventApplicationInfo(
			@RequestBody OneEventApplicationInfoRequest oneEventApplicationInfoRequest) throws EventException {
		return eventApplicationInfoService.getOneEventApplicationInfo(oneEventApplicationInfoRequest);
	}
	@PostMapping("/totalEvent")
	public TotalEventApplicationInfoResponse getTotalEventApplicationInfo(
			@RequestBody TotalEventApplicationInfoRequest totalEventApplicationInfoRequest) throws EventException {
		return eventApplicationInfoService.getTotalEventApplicationInfo(totalEventApplicationInfoRequest);
	}

}
