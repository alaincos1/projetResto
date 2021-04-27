package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.PerformanceEntity;
import org.bson.Document;

public class PerformanceCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("performances");

    public boolean save(Object performanceEntity) {
        return super.insert(Mapper.toDocument(performanceEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public PerformanceEntity getPerformanceById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (PerformanceEntity) Mapper.toObject(doc, PerformanceEntity.class);
    }
}
