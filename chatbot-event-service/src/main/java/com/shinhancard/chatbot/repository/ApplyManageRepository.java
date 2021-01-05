package com.shinhancard.chatbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shinhancard.chatbot.entity.ApplyManage;

public interface ApplyManageRepository extends MongoRepository<ApplyManage, String>{
	ApplyManage findOneByEventId(String eventId);
}
