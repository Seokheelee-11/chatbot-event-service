package com.shinhancard.chatbot.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Target {
	private Boolean isProperty;
	private String targetName;
	private String nonTargetName;
	private List<String> channels = new ArrayList<String>();

}
