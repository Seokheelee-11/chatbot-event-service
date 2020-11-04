package com.shinhancard.chatbot.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Quiz {
	private Boolean isProperty;
	private List<String> answers = new ArrayList<String>();;
	private Boolean checksOneAnswer;
	//private Boolean canReapply;
}
