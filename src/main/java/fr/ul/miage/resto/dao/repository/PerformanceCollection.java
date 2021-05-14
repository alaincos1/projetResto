package fr.ul.miage.resto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import fr.ul.miage.resto.model.entity.PerformanceEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<PerformanceEntity> getWeekPerformance() {
        return collection.find().sort(Sorts.descending("mealType")).sort(Sorts.ascending("_id")).limit(6)
                .into(new ArrayList<>()).stream().map(doc -> (PerformanceEntity) Mapper.toObject(doc, PerformanceEntity.class))
                .collect(Collectors.toList());
    }

    public List<PerformanceEntity> getAllPerformance() {
        return collection.find()
                .into(new ArrayList<>()).stream().map(doc -> (PerformanceEntity) Mapper.toObject(doc, PerformanceEntity.class))
                .collect(Collectors.toList());
    }
}
