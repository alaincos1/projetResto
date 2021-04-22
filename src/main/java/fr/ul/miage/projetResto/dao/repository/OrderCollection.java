package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.bson.Document;

public class OrderCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("orders");

    public boolean save(Object orderEntity) {
        return super.insert(Mapper.toDocument(orderEntity), collection);
    }

    public OrderEntity getOrderById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (OrderEntity) Mapper.toObject(doc, OrderEntity.class);
    }
}
