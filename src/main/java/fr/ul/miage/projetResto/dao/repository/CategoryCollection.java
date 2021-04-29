package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.CategoryEntity;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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

    public List<CategoryEntity> getAllCategoriesAsList() {
        List<Document> catDocuments = collection.find()
                .into(new ArrayList<>());
        List<CategoryEntity> catEntityList = new ArrayList<>();
        for (Document cat : catDocuments) {
            catEntityList.add((CategoryEntity) Mapper.toObject(cat, CategoryEntity.class));
        }
        return catEntityList;
    }
}
