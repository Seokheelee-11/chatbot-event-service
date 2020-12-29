package com.shinhancard.chatbot.dto.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;


@Data
public class EventApplicationRequest {
	@NotEmpty @NotBlank
	private String eventId;
	
	@NotEmpty @NotBlank
//	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;
	
	private String channel;
	private List<String> comments = new ArrayList<String>();
}
