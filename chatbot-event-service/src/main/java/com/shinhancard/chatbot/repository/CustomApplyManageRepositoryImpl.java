package com.shinhancard.chatbot.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import com.shinhancard.chatbot.entity.ApplyManage;

public class CustomApplyManageRepositoryImpl implements CustomApplyManageRepository {
	  private final MongoOperations operations;

	  @Autowired
	  public CustomApplyManageRepositoryImpl(MongoOperations operations) {

	    Assert.notNull(operations, "MongoOperations must not be null!");
	    this.operations = operations;
	  }

	  @Override
	  public ApplyManage updateByEventId(String eventId){
		  
		  
		  
		  w
		  return result;
	  }
	  
}
