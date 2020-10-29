package com.shinhancard.chatbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shinhancard.chatbot.domain.past.EventInfo;

public interface EventInfoRepository extends MongoRepository<EventInfo, String>{
	
	EventInfo findOneById(String id);
	EventInfo findOneByEventId(String EventId);
}
