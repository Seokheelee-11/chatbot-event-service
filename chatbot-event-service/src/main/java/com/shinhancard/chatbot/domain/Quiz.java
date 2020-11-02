package com.shinhancard.chatbot.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Quiz {
	private Boolean isProperty;
	private List<String> amswers = new ArrayList<String>();;
	private Boolean checksOneAnswer;
	private Boolean canReapply;
}
