package com.shinhancard.chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DefaultController {

	

	@GetMapping
	public String defaultApi() {
		return "chatbot-event-service";
	}

	@GetMapping("test")
	public void probTest() {
		
	}
	//control
	
}
