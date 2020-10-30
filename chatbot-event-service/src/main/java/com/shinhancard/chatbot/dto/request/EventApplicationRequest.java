package com.shinhancard.chatbot.dto.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.shinhancard.chatbot.domain.EventApplicationLog;

import lombok.Data;


@Data
public class EventApplicationRequest {
	@NotEmpty @NotBlank 
	private String eventId;
	
	@NotEmpty @NotBlank
	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;
	
	private List<EventApplicationLog> applicationHistories = new ArrayList<EventApplicationLog>();
	
	
}
