package fr.ul.miage.resto.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class InsertData {
    private InsertData() {
        throw new IllegalStateException("Utility class");
    }

    public static void feedData(BaseService baseService) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserEntity> users;
        List<BookingEntity> bookings;
        List<CategoryEntity> categories;
        List<PerformanceEntity> performances;
        List<ProductEntity> products;
        List<TableEntity> tables;
        List<DishEntity> dishes;
        try {
            users = objectMapper.readValue(InsertData.class.getResource("users.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, UserEntity.class));
            bookings = objectMapper.readValue(InsertData.class.getResource("bookings.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, BookingEntity.class));
            categories = objectMapper.readValue(InsertData.class.getResource("categories.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, CategoryEntity.class));
            performances = objectMapper.readValue(InsertData.class.getResource("performances.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, PerformanceEntity.class));
            products = objectMapper.readValue(InsertData.class.getResource("products.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, ProductEntity.class));
            tables = objectMapper.readValue(InsertData.class.getResource("tables.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, TableEntity.class));
            dishes = objectMapper.readValue(InsertData.class.getResource("dishes.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, DishEntity.class));
            users.forEach(baseService::save);
            bookings.forEach(baseService::save);
            categories.forEach(baseService::save);
            performances.forEach(baseService::save);
            products.forEach(baseService::save);
            tables.forEach(baseService::save);
            dishes.forEach(baseService::save);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
