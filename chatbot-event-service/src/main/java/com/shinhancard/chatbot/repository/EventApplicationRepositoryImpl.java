package com.shinhancard.chatbot.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

public class EventApplicationRepositoryImpl implements EventApplicationRepository {
	  private final MongoOperations operations;

	  @Autowired
	  public EventApplicationRepositoryImpl(MongoOperations operations) {

	    Assert.notNull(operations, "MongoOperations must not be null!");
	    this.operations = operations;
	  }

	  
}
