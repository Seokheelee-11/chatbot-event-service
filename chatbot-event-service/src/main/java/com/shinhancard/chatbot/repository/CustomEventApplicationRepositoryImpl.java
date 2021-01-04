package com.shinhancard.chatbot.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import com.shinhancard.chatbot.entity.EventApplication;

public class CustomEventApplicationRepositoryImpl implements CustomEventApplicationRepository {
	  private final MongoOperations operations;

	  @Autowired
	  public CustomEventApplicationRepositoryImpl(MongoOperations operations) {

	    Assert.notNull(operations, "MongoOperations must not be null!");
	    this.operations = operations;
	  }

	  @Override
	  public List<EventApplication> findAndModify(){
		  List<EventApplication> result = new ArrayList<>();
		  return result;
	  }
	  
}
