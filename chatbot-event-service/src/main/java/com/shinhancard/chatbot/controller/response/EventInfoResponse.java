package com.shinhancard.chatbot.controller.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.shinhancard.chatbot.domain.EventInfo.OverLapType;
import com.shinhancard.chatbot.domain.EventInfo.RewardType;
import com.shinhancard.chatbot.domain.ResultCode;

import lombok.Data;

@Data
public class EventInfoResponse {

	private String resultCode;
	private String resultMessage;

	private String eventId;
	private String displayName;

	private LocalDateTime startDt;
	private LocalDateTime endDt;

	// 중복 신청 관련 field
	private Boolean overLapTF;
	private OverLapType overLapDateType;
	private Integer overLapDateCount;
	private Boolean includeDateTF;

	// limit 관련 field
	private Boolean rewardTF;
	private RewardType rewardType;
	private LinkedHashMap<String, Double> rewardInfo = new LinkedHashMap<>();

	// quiz 신청 관련 field
//	private Boolean quizTF;
	private List<String> quizAnswer;

	// 결과 field
	private HashMap<String, HashMap<String, String>> resultInfo = new HashMap<>();

	// 대상자 선정
	private List<String> targetClnn;
	private List<String> nonTargetClnn;

	public void setResult(ResultCode result) {
		this.resultCode = result.getResultCode();
		this.resultMessage = result.getResultMessage();
	}

	public EventInfoResponse() {
		targetClnn = new ArrayList<String>();
		nonTargetClnn = new ArrayList<String>();
	}

	// TODO :: 최소한의 필드만 넣어놨으니 더 추가할 것

}
