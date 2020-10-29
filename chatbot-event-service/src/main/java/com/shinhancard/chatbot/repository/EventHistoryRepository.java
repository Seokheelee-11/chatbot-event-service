package com.shinhancard.chatbot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shinhancard.chatbot.domain.EventHistory;

public interface EventHistoryRepository extends MongoRepository<EventHistory, String>{
	
	EventHistory findOneById(String id);
	List<EventHistory> findAllByEventId(String eventId);
	EventHistory findOneByEventIdAndClnn(String eventId, String clnn);
	
	
}
