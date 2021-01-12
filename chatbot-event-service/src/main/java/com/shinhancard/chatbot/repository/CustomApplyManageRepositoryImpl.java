package com.shinhancard.chatbot.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.shinhancard.chatbot.entity.ApplyManage;

public class CustomApplyManageRepositoryImpl implements CustomApplyManageRepository {
	private final MongoOperations operations;

	@Autowired
	public CustomApplyManageRepositoryImpl(MongoOperations operations) {

		Assert.notNull(operations, "MongoOperations must not be null!");
		this.operations = operations;
	}


	public ApplyManage incDefaultByEventId(String eventId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("eventId").is(eventId));
		Update update = new Update();
		update.inc("clnnCount", 1).inc("applicationCount", 1);
		ApplyManage applyManage = operations.findAndModify(query, update, ApplyManage.class);

		return applyManage;
	}
	

	public ApplyManage decDefaultByEventId(String eventId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("eventId").is(eventId));
		Update update = new Update();
		update.inc("clnnCount", -1).inc("applicationCount", -1);
		ApplyManage applyManage = operations.findAndModify(query, update, ApplyManage.class);

		return applyManage;
	}

}
