package com.shinhancard.chatbot.domain.past;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Data;

@Data
public class EventHistoryLog {

	private Integer order;
	private List<String> param;
	private LocalDateTime regDate;
	private String rewardName;
	
	public EventHistoryLog() {
		order = 1;
		param = new ArrayList<String>();
		rewardName = "default";
		regDate = LocalDateTime.now();
	}
	
	public EventHistoryLog(EventHistoryRequest eventHistoryRequest) {
		order = 1;
		param = new ArrayList<String>();
		rewardName = "default";
		regDate = LocalDateTime.now();
		// param이 requst로 입력된 경우 HistoryLog에 param 셋팅
		if (!CollectionUtils.isEmpty(eventHistoryRequest.getParam())) {
			this.setParam(eventHistoryRequest.getParam());
		}
	}
	
	
}
