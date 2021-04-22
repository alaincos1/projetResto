package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.dao.repository.*;
import fr.ul.miage.projetResto.model.entity.*;

import java.util.Map;

public class Service {
    private final BillCollection billCollection;
    private final BookingCollection bookingCollection;
    private final DishCollection dishCollection;
    private final MenuCollection menuCollection;
    private final OrderCollection orderCollection;
    private final PerformanceCollection performanceCollection;
    private final ProductCollection productCollection;
    private final TableCollection tableCollection;
    private final UserCollection userCollection;
    Map<Class, MongoAccess> map;

    public Service(BillCollection billCollection,
                   BookingCollection bookingCollection,
                   DishCollection dishCollection,
                   MenuCollection menuCollection,
                   OrderCollection orderCollection,
                   PerformanceCollection performanceCollection,
                   ProductCollection productCollection,
                   TableCollection tableCollection,
                   UserCollection userCollection) {

        this.billCollection = billCollection;
        this.bookingCollection = bookingCollection;
        this.dishCollection = dishCollection;
        this.menuCollection = menuCollection;
        this.orderCollection = orderCollection;
        this.performanceCollection = performanceCollection;
        this.productCollection = productCollection;
        this.tableCollection = tableCollection;
        this.userCollection = userCollection;

        map = Map.of(
                BillEntity.class, billCollection,
                BookingEntity.class, bookingCollection,
                DishEntity.class, dishCollection,
                MenuEntity.class, menuCollection,
                OrderEntity.class, orderCollection,
                PerformanceEntity.class, performanceCollection,
                ProductEntity.class, productCollection,
                TableEntity.class, tableCollection,
                UserEntity.class, userCollection
        );
    }

    public boolean save(Object o) {
        if (map.containsKey(o.getClass())) {
            return map.get(o.getClass()).save(o);
        }
        return false;
    }

    public BillEntity getBillById(String id) {
        return billCollection.getBillById(id);
    }

    public BookingEntity getBookingById(String id) {
        return bookingCollection.getBookingById(id);
    }

    public DishEntity getDishById(String id) {
        return dishCollection.getDishById(id);
    }

    public MenuEntity getMenuById(String id) {
        return menuCollection.getMenuById(id);
    }

    public OrderEntity getOrderById(String id) {
        return orderCollection.getOrderById(id);
    }

    public PerformanceEntity getPerformanceById(String id) {
        return performanceCollection.getPerformanceById(id);
    }

    public ProductEntity getProductById(String id) {
        return productCollection.getProductById(id);
    }

    public TableEntity getTableById(String id) {
        return tableCollection.getTableById(id);
    }

    public UserEntity getUserById(String id) {
        return userCollection.getUserById(id);
    }
}
