package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DishCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("dishes");

    public boolean save(Object dishEntity) {
        return super.insert(Mapper.toDocument(dishEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public DishEntity getDishById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (DishEntity) Mapper.toObject(doc, DishEntity.class);
    }

    public List<DishEntity> getAllDishsNotOnMenu() {
        return collection.find(new Document("onTheMenu", false)).into(new ArrayList<>()).stream()
                .map(doc -> (DishEntity) Mapper.toObject(doc, DishEntity.class))
                .collect(Collectors.toList());
    }

    public List<DishEntity> getAllDishsOnTheMenu() {
        return collection.find(new Document("onTheMenu", true)).into(new ArrayList<>()).stream()
                .map(doc -> (DishEntity) Mapper.toObject(doc, DishEntity.class))
                .collect(Collectors.toList());
    }
}
