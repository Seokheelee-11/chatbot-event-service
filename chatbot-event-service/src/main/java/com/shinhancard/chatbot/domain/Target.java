package com.shinhancard.chatbot.domain;

import java.util.List;
import lombok.Data;

@Data
public class Target {
	private List<String> targetClnns;
	private List<String> nonTargetClnns;
	private List<String> channels;

}
