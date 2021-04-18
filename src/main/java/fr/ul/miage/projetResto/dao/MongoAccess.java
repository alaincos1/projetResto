package fr.ul.miage.projetResto.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public abstract class MongoAccess {
    MongoClient mongoClient = MongoClients.create();
    MongoDatabase database = mongoClient.getDatabase("bdd_restaurant");

}
