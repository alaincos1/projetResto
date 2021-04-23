package fr.ul.miage.projetResto.dao;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.model.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class InsertData {

    public static void feedData() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserEntity> users = null;
        List<BillEntity> bills = null;
        List<BookingEntity> bookings = null;
        List<MenuEntity> menus = null;
        List<OrderEntity> orders = null;
        List<PerformanceEntity> performances = null;
        List<ProductEntity> products = null;
        List<TableEntity> tables = null;
        try{
            users = objectMapper.readValue(InsertData.class.getResource("users.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, UserEntity.class));
            bills = objectMapper.readValue(InsertData.class.getResource("bills.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, BillEntity.class));
            bookings = objectMapper.readValue(InsertData.class.getResource("bookings.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, BookingEntity.class));
            menus = objectMapper.readValue(InsertData.class.getResource("dayMenu.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, MenuEntity.class));
            orders = objectMapper.readValue(InsertData.class.getResource("orders.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, OrderEntity.class));
            performances = objectMapper.readValue(InsertData.class.getResource("performances.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, PerformanceEntity.class));
            products = objectMapper.readValue(InsertData.class.getResource("products.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, ProductEntity.class));
            tables = objectMapper.readValue(InsertData.class.getResource("tables.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, TableEntity.class));
        }catch(JsonMappingException exception){
            log.error(exception.getMessage());
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
        users.forEach(Launcher.baseService::save);
        bills.forEach(Launcher.baseService::save);
        bookings.forEach(Launcher.baseService::save);
        menus.forEach(Launcher.baseService::save);
        orders.forEach(Launcher.baseService::save);
        performances.forEach(Launcher.baseService::save);
        products.forEach(Launcher.baseService::save);
        tables.forEach(Launcher.baseService::save);
    }
}
