package com.shinhancard.chatbot.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EventRequest {

	@NotEmpty @NotBlank
	private String eventId;
	
	@NotEmpty @NotBlank
	private String clnn;
}
