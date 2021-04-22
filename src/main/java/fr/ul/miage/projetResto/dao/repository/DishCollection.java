package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import org.bson.Document;

public class DishCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("dishs");

    public boolean save(Object dishEntity) {
        return super.insert(Mapper.toDocument(dishEntity), collection);
    }

    public DishEntity getDishById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (DishEntity) Mapper.toObject(doc, DishEntity.class);
    }
}
