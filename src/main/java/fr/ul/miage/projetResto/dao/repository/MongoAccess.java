package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public abstract class MongoAccess {
    MongoClient mongoClient = MongoClients.create();
    MongoDatabase database = mongoClient.getDatabase("bdd_restaurant");

    public abstract boolean save(Object o);

    public boolean insert(Document doc, MongoCollection<Document> collection) {
        if (alreadyExist(String.valueOf(doc.get("_id")), collection)) {
            return false;
        }
        collection.insertOne(doc);
        return true;
    }

    public Document getDocumentById(String id, MongoCollection<Document> collection) {
        Document doc = new Document("_id", id);
        return collection.find(doc).first();
    }

    public boolean alreadyExist(String id, MongoCollection<Document> collection) {
        return getDocumentById(id, collection) != null;
    }
}
