package com.shinhancard.chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

import com.mongodb.ReadConcern;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;

@Configuration
public class MongoTransactionManagerConfig {

	@Bean
	MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory) {
		TransactionOptions transactionOptions = TransactionOptions.builder().readConcern(ReadConcern.LOCAL)
				.writeConcern(WriteConcern.W1).build();
		return new MongoTransactionManager(dbFactory, transactionOptions);
	}
}
