package com.shinhancard.chatbot.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Target {
	private String targetClnns;
	private String nonTargetClnns;
	private List<String> channels = new ArrayList<String>();

}
