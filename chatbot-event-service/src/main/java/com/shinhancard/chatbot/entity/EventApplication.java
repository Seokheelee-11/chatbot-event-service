package com.shinhancard.chatbot.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.EventApplicationLog;

import lombok.Data;


@Data
public class EventApplication {
	@Id
	private String id;

//	@NotEmpty @NotBlank 
	private String eventId;
	
//	@NotEmpty @NotBlank
//	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;
	
	private List<EventApplicationLog> applicationHistories = new ArrayList<EventApplicationLog>();
	
	public EventApplication() {
		
	}
}
