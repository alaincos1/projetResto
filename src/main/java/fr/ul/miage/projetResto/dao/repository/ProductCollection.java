package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.ProductEntity;
import org.bson.Document;

public class ProductCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("products");

    public boolean save(Object productEntity) {
        return super.insert(Mapper.toDocument(productEntity), collection);
    }

    public ProductEntity getProductById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (ProductEntity) Mapper.toObject(doc, ProductEntity.class);
    }
}
