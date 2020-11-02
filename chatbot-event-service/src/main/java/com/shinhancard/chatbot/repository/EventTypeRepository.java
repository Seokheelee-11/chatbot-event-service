package com.shinhancard.chatbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shinhancard.chatbot.entity.EventType;

public interface EventTypeRepository extends MongoRepository<EventType, String>{
	
//	EventType findOneByType(String type);
	EventType findOneByIdAndType(String id, String type);
	
}
