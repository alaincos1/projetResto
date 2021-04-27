package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCollection extends MongoAccess {
    MongoCollection<Document> collection = database.getCollection("orders");

    public boolean save(Object orderEntity) {
        return super.insert(Mapper.toDocument(orderEntity), collection);
    }

    @Override
    public boolean update(Object o) {
        return super.update(Mapper.toDocument(o), collection);
    }

    public OrderEntity getOrderById(String id) {
        Document doc = getDocumentById(id, collection);
        return doc == null ? null : (OrderEntity) Mapper.toObject(doc, OrderEntity.class);
    }
}
