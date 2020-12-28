package com.shinhancard.chatbot.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhancard.chatbot.config.EventException;
import com.shinhancard.chatbot.dto.request.EventApplicationRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationResponse;
import com.shinhancard.chatbot.service.EventApplicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DefaultController {


	private final EventApplicationService EventApplicationService;
	
	@GetMapping
	public String defaultApi() {
		return "chatbot-event-service";
	}

	
	
	@GetMapping("test")
	public void probTest() throws EventException {
		EventApplicationRequest eventApplicationRequest = new EventApplicationRequest();
		EventApplicationResponse eventApplicationResponse = new EventApplicationResponse();
		String clnn = "P000000030";
		String eventId = "test12";
		eventApplicationRequest.setClnn(clnn);
		eventApplicationRequest.setEventId(eventId);
		Integer first = 0;
		Integer second = 0;
		Integer third = 0;
		Integer etc = 0;
		LocalDateTime time = LocalDateTime.now();
		for (int i = 0; i <500; i++) {
			eventApplicationResponse = EventApplicationService.applicationEvent(eventApplicationRequest);
			if ("1등".equals(eventApplicationResponse.getEventApplicationLog().getRewardName())) {
				first++;
			} else if ("2등".equals(eventApplicationResponse.getEventApplicationLog().getRewardName())) {
				second++;
			} else if ("3등".equals(eventApplicationResponse.getEventApplicationLog().getRewardName())) {
				third++;
			} else {
				etc++;
			}
			log.warn("{}번째 진행중", i);
		}
		log.warn("------------------------------------------------------------------");
		
		log.warn("start time : {}", time);
		time = LocalDateTime.now();
		log.warn("end time : ", time);
		log.warn("1등 : {} 2등 : {} 3등 : {}, etc : {}", first, second, third, etc);
	}
	//control
	
}
