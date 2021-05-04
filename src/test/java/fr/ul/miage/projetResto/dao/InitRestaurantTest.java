package fr.ul.miage.projetResto.dao;

import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitRestaurantTest {
    EasyRandom easyRandom = new EasyRandom();

    @Mock
    BaseService baseService;

    @Mock
    Service service;

    @InjectMocks
    InitRestaurant initRestaurant;

    @Test
    void testInitUncheckedOrder() {
        List<OrderEntity> uncheckedOrders = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            uncheckedOrders.add(easyRandom.nextObject(OrderEntity.class));
            uncheckedOrders.get(i).setOrderState(OrderState.Served);
        }

        when(baseService.getAllNotCheckedOrder()).thenReturn(uncheckedOrders);

        initRestaurant.initUncheckedOrder();

        verify(baseService, times(uncheckedOrders.size())).update(any(OrderEntity.class));
    }

    @Test
    void testInitUncheckedOrderWithEmptyOrders() {
        List<OrderEntity> uncheckedOrders = new ArrayList<>();

        when(baseService.getAllNotCheckedOrder()).thenReturn(uncheckedOrders);

        initRestaurant.initUncheckedOrder();

        verify(baseService, times(0)).update(any(OrderEntity.class));
    }


    @Test
    void testInitTableState() {
        List<TableEntity> tableEntities = new ArrayList<>();
        List<BookingEntity> bookingEntities = new ArrayList<>();
        MealType mealType = MealType.Déjeuner;
        String date = "2021/04/25";
        String date2 = "2021/04/26";
        String idTable1 = "idTable1";
        String idTable2 = "idTable2";

        tableEntities.add(createTableEntity(idTable1));
        tableEntities.add(createTableEntity(idTable2));

        bookingEntities.add(createBookingEntity(mealType, date, idTable1));
        bookingEntities.add(createBookingEntity(mealType, date, idTable2));
        bookingEntities.add(createBookingEntity(mealType, date2, idTable1));

        when(baseService.getAllTables()).thenReturn(tableEntities);
        when(baseService.getAllBooking()).thenReturn(bookingEntities);
        when(service.getMealType()).thenReturn(mealType);
        when(service.getDate()).thenReturn(date);

        initRestaurant.initTableState();

        verify(baseService, times(2)).update(any(TableEntity.class));
    }

    @Test
    void testInitTableStateWithoutBooking() {
        List<TableEntity> tableEntities = new ArrayList<>();
        List<BookingEntity> bookingEntities = new ArrayList<>();
        String idTable1 = "idTable1";
        String idTable2 = "idTable2";

        tableEntities.add(createTableEntity(idTable1));
        tableEntities.add(createTableEntity(idTable2));

        when(baseService.getAllTables()).thenReturn(tableEntities);
        when(baseService.getAllBooking()).thenReturn(bookingEntities);

        initRestaurant.initTableState();

        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    @Test
    void testInitTableStateWithoutTable() {
        List<TableEntity> tableEntities = new ArrayList<>();
        List<BookingEntity> bookingEntities = new ArrayList<>();
        MealType mealType = MealType.Déjeuner;
        String date = "2021/04/25";
        String date2 = "2021/04/26";
        String idTable1 = "idTable1";
        String idTable2 = "idTable2";

        bookingEntities.add(createBookingEntity(mealType, date, idTable1));
        bookingEntities.add(createBookingEntity(mealType, date, idTable2));
        bookingEntities.add(createBookingEntity(mealType, date2, idTable1));

        when(baseService.getAllTables()).thenReturn(tableEntities);
        when(baseService.getAllBooking()).thenReturn(bookingEntities);

        initRestaurant.initTableState();

        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    @Test
    void testInitTableStateWithoutMatchingBookingDate() {
        List<TableEntity> tableEntities = new ArrayList<>();
        List<BookingEntity> bookingEntities = new ArrayList<>();
        MealType mealType = MealType.Déjeuner;
        String date = "2021/04/25";
        String date2 = "2021/04/26";
        String idTable1 = "idTable1";
        String idTable2 = "idTable2";

        tableEntities.add(createTableEntity(idTable1));
        tableEntities.add(createTableEntity(idTable2));

        bookingEntities.add(createBookingEntity(mealType, date2, idTable1));
        bookingEntities.add(createBookingEntity(mealType, date2, idTable2));
        bookingEntities.add(createBookingEntity(mealType, date2, idTable1));

        when(baseService.getAllTables()).thenReturn(tableEntities);
        when(baseService.getAllBooking()).thenReturn(bookingEntities);
        when(service.getDate()).thenReturn(date);

        initRestaurant.initTableState();

        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    @Test
    void testInitTableStateWithoutMatchingBookingMealType() {
        List<TableEntity> tableEntities = new ArrayList<>();
        List<BookingEntity> bookingEntities = new ArrayList<>();
        MealType mealType = MealType.Déjeuner;
        MealType mealType2 = MealType.Dîner;
        String date = "2021/04/25";
        String date2 = "2021/04/26";
        String idTable1 = "idTable1";
        String idTable2 = "idTable2";

        tableEntities.add(createTableEntity(idTable1));
        tableEntities.add(createTableEntity(idTable2));

        bookingEntities.add(createBookingEntity(mealType, date, idTable1));
        bookingEntities.add(createBookingEntity(mealType, date, idTable2));
        bookingEntities.add(createBookingEntity(mealType, date2, idTable1));

        when(baseService.getAllTables()).thenReturn(tableEntities);
        when(baseService.getAllBooking()).thenReturn(bookingEntities);
        when(service.getDate()).thenReturn(date);
        when(service.getMealType()).thenReturn(mealType2);

        initRestaurant.initTableState();

        verify(baseService, times(0)).update(any(TableEntity.class));
    }

    private BookingEntity createBookingEntity(MealType mealType, String date, String idTable) {
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.set_id(RandomStringUtils.randomAlphabetic(10));
        bookingEntity.setMealType(mealType);
        bookingEntity.setReservationName(RandomStringUtils.randomAlphabetic(10));
        bookingEntity.setDate(date);
        bookingEntity.setIdTable(idTable);

        return bookingEntity;
    }

    private TableEntity createTableEntity(String id) {
        TableEntity tableEntity = easyRandom.nextObject(TableEntity.class);
        tableEntity.set_id(id);
        tableEntity.setTableState(TableState.Free);

        return tableEntity;
    }

    @DisplayName("Aucun utilisateur en base, création de l'admin")
    @Test
    void checkInitUsers() {
        when(baseService.getAllUsers()).thenReturn(new ArrayList<>());

        initRestaurant.initUsers();

        verify(baseService, times(1)).save(any(UserEntity.class));
    }

    @DisplayName("Directeur en base, pas de création de l'admin")
    @Test
    void checkInitUsersDirectorHere() {
        List<UserEntity> users = new ArrayList<>();
        UserEntity user1 = new UserEntity();
        user1.setRole(Role.Helper);
        UserEntity user2 = new UserEntity();
        user2.setRole(Role.Director);
        users.add(user1);
        users.add(user2);
        when(baseService.getAllUsers()).thenReturn(users);

        initRestaurant.initUsers();

        verify(baseService, times(0)).save(any(UserEntity.class));
    }

    @DisplayName("Utilisateurs en base, mais pas de directeur, création de l'admin")
    @Test
    void checkInitUsersNoDirectorButUsers() {
        List<UserEntity> users = new ArrayList<>();
        UserEntity user1 = new UserEntity();
        user1.setRole(Role.Helper);
        UserEntity user2 = new UserEntity();
        user2.setRole(Role.Cook);
        users.add(user1);
        users.add(user2);
        when(baseService.getAllUsers()).thenReturn(users);

        initRestaurant.initUsers();

        verify(baseService, times(1)).save(any(UserEntity.class));
    }
}
