package com.shinhancard.chatbot.dto.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class EventTargetRequest {
	@NotEmpty @NotBlank
	private String name;
	private List<String> clnns = new ArrayList<String>();
}
