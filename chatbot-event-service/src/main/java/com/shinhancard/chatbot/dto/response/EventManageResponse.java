package com.shinhancard.chatbot.dto.response;

import com.shinhancard.chatbot.domain.DefaultInfo;
import com.shinhancard.chatbot.domain.OverLap;
import com.shinhancard.chatbot.domain.Quiz;
import com.shinhancard.chatbot.domain.Response;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.domain.Reward;
import com.shinhancard.chatbot.domain.Target;

import lombok.Data;


@Data
public class EventManageResponse {

	// 결과메세지
	private ResultCode resultCode;
	private String resultMessage;
	
	private String eventId;
	private DefaultInfo defaultInfo = new DefaultInfo();
	private Target target = new Target();
	private Response response = new Response();
	private OverLap overLap = new OverLap();
	private Quiz quiz = new Quiz();
	private Reward reward = new Reward();
}
