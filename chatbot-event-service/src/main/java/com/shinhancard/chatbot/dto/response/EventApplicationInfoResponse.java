package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.ApplicationInfo;

import lombok.Data;

@Data
public class EventApplicationInfoResponse {
	private String clnn;
	private List<ApplicationInfo> applicationInfoes = new ArrayList<>();

	public EventApplicationInfoResponse() {
		
	}
	
	public void addApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfoes.add(applicationInfo);
	}
	
	
}