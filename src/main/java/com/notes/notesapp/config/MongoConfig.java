package com.notes.notesapp.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

// wire mongodb client and template configs
@Configuration
public class MongoConfig {

    @Value("${mongodb.uri:mongodb://localhost:27017/notesDB}")
    private String mongoUri;

    // build mongo client from uri
    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

    // expose mongo template for queries
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        return new MongoTemplate(mongoClient, connectionString.getDatabase());
    }
}
