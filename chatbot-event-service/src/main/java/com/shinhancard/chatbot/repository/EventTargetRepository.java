package com.shinhancard.chatbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shinhancard.chatbot.entity.EventTarget;

public interface EventTargetRepository extends MongoRepository<EventTarget, String>{
	
	
	EventTarget findOneByName(String name);
}
