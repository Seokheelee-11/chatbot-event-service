package com.shinhancard.chatbot.dto.request;

import lombok.Data;


@Data
public class OneEventApplicationInfoRequest {
//	@NotEmpty @NotBlank 
	private String eventId;
	
//	@NotEmpty @NotBlank
//	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;
	String channel;
	
	
	
	
}
