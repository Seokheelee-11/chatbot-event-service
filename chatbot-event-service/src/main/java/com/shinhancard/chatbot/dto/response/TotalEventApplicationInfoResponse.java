package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.ApplicationInfo;

import lombok.Data;

@Data
public class TotalEventApplicationInfoResponse {
	private String clnn;
	private String channel;
	private List<ApplicationInfo> applicationInfoes = new ArrayList<>();

	public TotalEventApplicationInfoResponse() {
		
	}
	
	public void addApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfoes.add(applicationInfo);
	}
	
	
}