package com.shinhancard.chatbot.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.shinhancard.chatbot.config.EventException;
import com.shinhancard.chatbot.domain.ResultCodeMessage;

@RestControllerAdvice(basePackages = "com.shinhancard.chatbot.controller")
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler(EventException.class)
	protected ResultCodeMessage eventException(EventException e) {
		return new ResultCodeMessage(e.getResultCode());
	}
	
}
