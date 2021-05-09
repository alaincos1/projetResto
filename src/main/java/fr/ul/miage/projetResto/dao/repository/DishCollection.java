package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import fr.ul.miage.projetResto.constants.DishType;
import fr.ul.miage.projetResto.model.entity.DishEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

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
	
	public List<DishEntity> getDishOnTheMenuByDishType(DishType dishType) {
        Bson filter = and(eq("onTheMenu",true), eq("dishType", dishType.toString()));
        return collection.find(filter).sort(Sorts.ascending("idCategory")).into(new ArrayList<Document>()).stream()
                .map(doc -> (DishEntity) Mapper.toObject(doc, DishEntity.class))
                .collect(Collectors.toList());
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
