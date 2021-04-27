package fr.ul.miage.projetResto.dao.repository;

import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderCollectionTest {
    private OrderCollection orderCollection = new OrderCollection();

    @Test
    public void getOrdersEntityListChildrenOrderFirstTest(){
        List<Document> childOrdersDocuments = new ArrayList<>();
        childOrdersDocuments.add(Document.parse("{\n" +
                "    \"_id\" : \"2\",\n" +
                "    \"orderState\" : \"Ordered\",\n" +
                "    \"childOrder\" : true,\n" +
                "    \"rank\" : 5,\n" +
                "    \"idsDish\" : [ \n" +
                "        \"Patates sautées et légumes\", \n" +
                "        \"Salade caesar\"\n" +
                "    ],\n" +
                "    \"idTable\" : \"1\"\n" +
                "}"));
        List<Document> orderDocuments = new ArrayList<>();
        orderDocuments.add(Document.parse("{\n" +
                "    \"_id\" : \"3\",\n" +
                "    \"orderState\" : \"Ordered\",\n" +
                "    \"childOrder\" : false,\n" +
                "    \"rank\" : 3,\n" +
                "    \"idsDish\" : [ \n" +
                "        \"Boeuf et légumes\", \n" +
                "        \"Banana Split\"\n" +
                "    ],\n" +
                "    \"idTable\" : \"5\"\n" +
                "}"));
        orderDocuments.add(Document.parse("{\n" +
                "    \"_id\" : \"1\",\n" +
                "    \"orderState\" : \"Ordered\",\n" +
                "    \"childOrder\" : false,\n" +
                "    \"rank\" : 4,\n" +
                "    \"idsDish\" : [ \n" +
                "        \"Salade de sardines\", \n" +
                "        \"Beignet de banane\"\n" +
                "    ],\n" +
                "    \"idTable\" : \"1\"\n" +
                "}"));

        OrderEntity order1 = (OrderEntity) Mapper.toObject(childOrdersDocuments.get(0),OrderEntity.class);
        OrderEntity order2 = (OrderEntity) Mapper.toObject(orderDocuments.get(0),OrderEntity.class);
        OrderEntity order3 = (OrderEntity) Mapper.toObject(orderDocuments.get(1),OrderEntity.class);
        List<OrderEntity> orderActual = new ArrayList<>();
        orderActual.add(order1);
        orderActual.add(order2);
        orderActual.add(order3);

        List<OrderEntity> ordersTest =  orderCollection.getOrdersEntityListChildrenOrderFirst(childOrdersDocuments, orderDocuments);

        Assertions.assertEquals(ordersTest, orderActual);
    }
}
