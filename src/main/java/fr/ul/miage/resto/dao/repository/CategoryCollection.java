package fr.ul.miage.resto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.model.entity.CategoryEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class CategoryCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("categories");

    public boolean save(Object categoryEntity) {
        return super.insert(Mapper.toDocument(categoryEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public CategoryEntity getCategoryById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (CategoryEntity) Mapper.toObject(doc, CategoryEntity.class);
    }

    public List<CategoryEntity> getCategoriesByDishType(DishType dishType) {
        return collection.find(eq("dishType", dishType.toString())).sort(Sorts.ascending("_id"))
                .into(new ArrayList<Document>()).stream().map(doc -> (CategoryEntity) Mapper.toObject(doc, CategoryEntity.class))
                .collect(Collectors.toList());
    }
}
