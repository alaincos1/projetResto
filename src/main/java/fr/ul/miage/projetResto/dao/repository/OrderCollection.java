package fr.ul.miage.projetResto.dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

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

    public List<OrderEntity> getNotPreparedOrders() {
        List<Document> childOrdersDocuments = collection.find(
                and(eq("orderState", OrderState.Ordered.toString()), eq("childOrder", true)))
                .sort(Sorts.ascending("rank"))
                .into(new ArrayList<>());
        List<Document> orderDocuments = collection.find(
                and(eq("orderState", OrderState.Ordered.toString()), eq("childOrder", false)))
                .sort(Sorts.ascending("rank"))
                .into(new ArrayList<>());
        return getOrdersEntityListChildrenOrderFirst(childOrdersDocuments, orderDocuments);
    }

    public List<OrderEntity> getOrdersEntityListChildrenOrderFirst(List<Document> childOrdersDocuments, List<Document> orderDocuments) {
        List<OrderEntity> orders = new ArrayList<>();
        for (Document order : childOrdersDocuments) {
            orders.add((OrderEntity) Mapper.toObject(order, OrderEntity.class));
        }
        for (Document order : orderDocuments) {
            orders.add((OrderEntity) Mapper.toObject(order, OrderEntity.class));
        }
        return orders;
    }

    public List<OrderEntity> getAllNotChecked() {
        Bson doc = Filters.ne("orderState", OrderState.Checked.toString());
        return collection.find(doc).into(new ArrayList<Document>()).stream()
                .map(document -> (OrderEntity) Mapper.toObject(document, OrderEntity.class))
                .collect(Collectors.toList());
    }
}
