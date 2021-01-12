package com.shinhancard.chatbot.dto.request;



import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.TimeClassificationCode;

import lombok.Data;


@Data
public class EventInfoRequest {
//	@NotEmpty @NotBlank 
	private List<String> eventId = new ArrayList<>();
	
//	@NotEmpty @NotBlank
//	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;
	private String channel;
	private TimeClassificationCode timeClassification;
}
