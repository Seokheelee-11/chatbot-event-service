package com.shinhancard.chatbot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.ApplicationHistory;

public class EventApplication {
	@Id
	private String id;

	@NotEmpty @NotBlank 
	private String eventId;
	
	@NotEmpty @NotBlank
	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;
	
	private List<ApplicationHistory> applicationHistories = new ArrayList<ApplicationHistory>();
	
	public EventApplication() {
		
	}
}
