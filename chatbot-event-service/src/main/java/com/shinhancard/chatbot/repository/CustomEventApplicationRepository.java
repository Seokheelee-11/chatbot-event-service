package com.shinhancard.chatbot.repository;

import java.util.List;

import com.shinhancard.chatbot.entity.EventApplication;

public interface CustomEventApplicationRepository {
	List<EventApplication> findAndModify();
}
