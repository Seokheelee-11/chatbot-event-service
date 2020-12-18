package com.shinhancard.chatbot.config;

import com.shinhancard.chatbot.domain.ResultCode;

import lombok.Getter;


@Getter
public class EventException extends Exception{
	private ResultCode resultCode;
	
	
	public EventException() {
		// TODO Auto-generated constructor stub
	}
	
	public EventException(ResultCode resultCode) {
		this.resultCode = resultCode;
	}
}
