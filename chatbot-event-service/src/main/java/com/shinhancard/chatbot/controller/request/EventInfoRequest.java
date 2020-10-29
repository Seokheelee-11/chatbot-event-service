package com.shinhancard.chatbot.controller.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.shinhancard.chatbot.domain.past.EventInfo.OverLapType;
import com.shinhancard.chatbot.domain.past.EventInfo.RewardType;

import lombok.Data;

@Data
public class EventInfoRequest {
	
	private String eventId;	
	private String displayName;
	
	private LocalDateTime startDt;
	private LocalDateTime endDt;
	
	//중복 신청 관련 field
	private Boolean overLapTF;
	private OverLapType overLapDateType;
	private Integer overLapDateCount;
	private Boolean includeDateTF;
	
	//limit 관련 field
	private Boolean rewardTF;
	private RewardType rewardType;
	private LinkedHashMap<String, Double> rewardInfo = new LinkedHashMap<>();
	
	//quiz 신청 관련 field
//	private Boolean quizTF;
	private List<String> quizAnswer;
	
	//결과 field
	private HashMap<String,HashMap<String,String>> resultInfo = new HashMap<>();
		
	//대상자 선정
	private List<String> targetClnn;
	private List<String> nonTargetClnn;
	
	public EventInfoRequest() {
		targetClnn = new ArrayList<String>();
		nonTargetClnn = new ArrayList<String>();
	}

	
}

