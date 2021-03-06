package com.shinhancard.chatbot.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class EventTarget {
	@Id
	private String id;
	private String name;
	private List<String> clnns = new ArrayList<String>();
}
