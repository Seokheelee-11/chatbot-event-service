package com.shinhancard.chatbot.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.shinhancard.chatbot.domain.ApplicationInfo;
import com.shinhancard.chatbot.domain.ResultCode;

import lombok.Data;

@Data
public class TotalEventApplicationInfoResponse {
	// 결과메세지
	private ResultCode resultCode;
	private String resultMessage;
	private String clnn;
	private String channel;
	private List<ApplicationInfo> applicationInfoes = new ArrayList<>();

	public TotalEventApplicationInfoResponse() {
		
	}
	
	public void addApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfoes.add(applicationInfo);
	}
	
	
}