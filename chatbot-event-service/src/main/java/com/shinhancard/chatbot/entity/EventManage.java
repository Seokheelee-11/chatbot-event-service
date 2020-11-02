package com.shinhancard.chatbot.entity;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.DefaultInfo;
import com.shinhancard.chatbot.domain.OverLap;
import com.shinhancard.chatbot.domain.Quiz;
import com.shinhancard.chatbot.domain.Response;
import com.shinhancard.chatbot.domain.Reward;
import com.shinhancard.chatbot.domain.Target;

import lombok.Data;


@Data
public class EventManage {

	@Id
	private String id;
	
	private Type eventType;
	private String eventId;
	private DefaultInfo defaultInfo = new DefaultInfo();
	private Target target = new Target();
	private Response response = new Response();
	private OverLap overLap = new OverLap();
	private Quiz quiz = new Quiz();
	private Reward reward = new Reward();
	
	@Data
	public class Type{
		private String id;
		private String type;
	}
	

}
