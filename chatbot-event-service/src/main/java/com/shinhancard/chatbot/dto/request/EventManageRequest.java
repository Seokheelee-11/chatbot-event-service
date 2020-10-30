package com.shinhancard.chatbot.dto.request;

import com.shinhancard.chatbot.domain.DefaultInfo;
import com.shinhancard.chatbot.domain.OverLap;
import com.shinhancard.chatbot.domain.Quiz;
import com.shinhancard.chatbot.domain.Response;
import com.shinhancard.chatbot.domain.Reward;
import com.shinhancard.chatbot.domain.Target;

import lombok.Data;


@Data
public class EventManageRequest {

	private String eventType;
	private String eventId;
	private DefaultInfo defaultInfo = new DefaultInfo();
	private Target target = new Target();
	private Response response = new Response();
	private OverLap overLap = new OverLap();
	private Quiz quiz = new Quiz();
	private Reward reward = new Reward();
}
