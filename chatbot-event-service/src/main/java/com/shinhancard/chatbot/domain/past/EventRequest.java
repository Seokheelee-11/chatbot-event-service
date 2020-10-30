package com.shinhancard.chatbot.domain.past;

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
