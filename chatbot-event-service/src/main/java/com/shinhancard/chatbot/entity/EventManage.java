package com.shinhancard.chatbot.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.DefaultInfo;
import com.shinhancard.chatbot.domain.OverLap;
import com.shinhancard.chatbot.domain.PropertyCode;
import com.shinhancard.chatbot.domain.Quiz;
import com.shinhancard.chatbot.domain.Response;
import com.shinhancard.chatbot.domain.Reward;
import com.shinhancard.chatbot.domain.Target;

import lombok.Data;


@Data
public class EventManage {

	@Id
	private String id;

	private String eventId;
	private DefaultInfo defaultInfo = new DefaultInfo();
	private Target target = new Target();
	private Response response = new Response();
	private OverLap overLap = new OverLap();
	private Quiz quiz = new Quiz();
	private Reward reward = new Reward();
	
	
	public List<PropertyCode> getProperties(){
		List<PropertyCode> properties = new ArrayList<PropertyCode>();
		
//		if(this.defaultInfo.getIsProperty()) {
//			properties.add(PropertyCode.DEFAULT);
//		}
		if(this.target.getIsProperty()) {
			properties.add(PropertyCode.TARGET);
		}
//		if(this.response.getIsProperty()) {
//			properties.add(PropertyCode.RESPONSE);
//		}
		if(this.overLap.getIsProperty()) {
			properties.add(PropertyCode.OVERLAP);
		}
		if(this.quiz.getIsProperty()) {
			properties.add(PropertyCode.QUIZ);
		}
		if(this.reward.getIsProperty()) {
			properties.add(PropertyCode.REWARD);
		}
		
		return properties;
	}

}
