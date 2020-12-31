package com.shinhancard.chatbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.shinhancard.chatbot.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {
	@Value("${spring.data.mongodb.uri}")
	private String mongodbUri;

	@Value("${spring.data.mongodb.database}")
	private String mongodbDatabase;

	
	
	@Bean
	MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
		TransactionOptions transactionOptions = TransactionOptions.builder()
				.readConcern(ReadConcern.LINEARIZABLE)
				.writeConcern(WriteConcern.MAJORITY).build();
		return new MongoTransactionManager(dbFactory, transactionOptions);
	}

	@Override
	protected String getDatabaseName() {
		return "test";
	}

	
	@Override
	public MongoClient mongoClient() {
		final ConnectionString connectionString = new ConnectionString(mongodbUri);
		final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
				.readConcern(ReadConcern.LOCAL)
				.writeConcern(WriteConcern.ACKNOWLEDGED)
				.applyConnectionString(connectionString)
				.build();
		return MongoClients.create(mongoClientSettings);
	}
	
	
	
	@Bean
	public MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), mongodbDatabase);
	}

}
