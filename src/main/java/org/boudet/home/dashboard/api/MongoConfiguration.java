package org.boudet.home.dashboard.api;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import static java.util.Collections.singletonList;

@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Value("${mongo.host}")
    private String host;

    @Value("${mongo.port}")
    private int port;

    @Value("${mongo.user}")
    private String user;

    @Value("${mongo.password}")
    private String password;

    @Value("${mongo.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(singletonList(new ServerAddress( host, port)),
                singletonList(MongoCredential.createCredential(user, database, password.toCharArray())));
    }

    @Override
    protected String getMappingBasePackage() {
        return "org.boudet.home.dashboard.api.model.mongo";
    }

}