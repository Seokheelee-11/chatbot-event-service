package com.shinhancard.chatbot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shinhancard.chatbot.entity.EventApplication;

public interface EventApplicationRepository extends MongoRepository<EventApplication, String>{
	
	EventApplication findOneById(String id);
	List<EventApplication> findAllByEventId(String eventId);
	List<EventApplication> findAllByClnn(String clnn);
	List<EventApplication> findAllByEventIdAndClnn(String eventId, String clnn);
	EventApplication findOneByEventIdAndClnn(String eventId, String clnn);
	List<EventApplication> findAndModify();
	
	
}
