package com.shinhancard.chatbot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shinhancard.chatbot.entity.EventManage;

public interface EventManageRepository extends MongoRepository<EventManage, String>{
	
	EventManage findOneById(String id);
	EventManage findOneByEventId(String eventId);
	List<EventManage> findAll();
	List<EventManage> findAllByEventId(String eventId);
	

}
