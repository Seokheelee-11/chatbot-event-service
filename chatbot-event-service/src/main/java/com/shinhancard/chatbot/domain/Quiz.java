package com.shinhancard.chatbot.domain;

import java.util.List;

import lombok.Data;

@Data
public class Quiz {

	private List<String> amswers;
	private Boolean checksOneAnswer;
	private Boolean canReapply;
}
