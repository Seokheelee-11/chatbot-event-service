package com.shinhancard.chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhancard.chatbot.controller.request.EventHistoryRequest;
import com.shinhancard.chatbot.controller.response.EventHistoryResponse;
import com.shinhancard.chatbot.service.EventHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DefaultController {

	private final EventHistoryService eventHistoryService;

	@GetMapping
	public String defaultApi() {
		return "chatbot-event-service";
	}

	@GetMapping("test")
	public void probTest() {
		EventHistoryRequest eventHistoryRequest = new EventHistoryRequest();
		EventHistoryResponse eventHistoryResponse = new EventHistoryResponse();
		String clnn = "P000000001";
		String eventId = "testevent16";
		eventHistoryRequest.setClnn(clnn);
		eventHistoryRequest.setEventId(eventId);
		Integer starbucks = 0;
		Integer tumbler = 0;
		Integer mask = 0;
		Integer etc = 0;
		for (int i = 0; i < 1000; i++) {
			eventHistoryResponse = eventHistoryService.registEventHistory(eventHistoryRequest);
			if ("starbucks".equals(eventHistoryResponse.getRewardName())) {
				starbucks++;
			} else if ("tumbler".equals(eventHistoryResponse.getRewardName())) {
				tumbler++;
			} else if ("mask".equals(eventHistoryResponse.getRewardName())) {
				mask++;
			} else {
				etc++;
			}
		}
		log.error("------------------------------------------------------------------");
		log.error("starbucks : {} tumbler : {} mask : {}, etc : {}", starbucks, tumbler, mask, etc);
	}
	//control
	
}
