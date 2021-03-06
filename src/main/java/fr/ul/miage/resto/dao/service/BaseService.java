package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.MealType;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.dao.repository.*;
import fr.ul.miage.resto.model.entity.*;
import fr.ul.miage.resto.utils.DateDto;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseService {
    private final BillCollection billCollection;
    private final BookingCollection bookingCollection;
    private final DishCollection dishCollection;
    private final CategoryCollection categoryCollection;
    private final OrderCollection orderCollection;
    private final PerformanceCollection performanceCollection;
    private final ProductCollection productCollection;
    private final TableCollection tableCollection;
    private final UserCollection userCollection;
    Map<? extends Class, MongoAccess> map;

    public BaseService(BillCollection billCollection,
                       BookingCollection bookingCollection,
                       DishCollection dishCollection,
                       CategoryCollection categoryCollection,
                       OrderCollection orderCollection,
                       PerformanceCollection performanceCollection,
                       ProductCollection productCollection,
                       TableCollection tableCollection,
                       UserCollection userCollection) {

        this.billCollection = billCollection;
        this.bookingCollection = bookingCollection;
        this.dishCollection = dishCollection;
        this.categoryCollection = categoryCollection;
        this.orderCollection = orderCollection;
        this.performanceCollection = performanceCollection;
        this.productCollection = productCollection;
        this.tableCollection = tableCollection;
        this.userCollection = userCollection;

        map = Map.of(
                BillEntity.class, billCollection,
                BookingEntity.class, bookingCollection,
                DishEntity.class, dishCollection,
                CategoryEntity.class, categoryCollection,
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

    public boolean update(Object o) {
        if (map.containsKey(o.getClass())) {
            if (o instanceof UserEntity) {
                return updateUser((UserEntity) o);
            }
            return map.get(o.getClass()).update(o);
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

    public CategoryEntity getCategoryById(String id) {
        return categoryCollection.getCategoryById(id);
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

    public List<OrderEntity> getNotPreparedOrders() {
        return orderCollection.getNotPreparedOrders();
    }

    public List<TableEntity> getAllTables() {
        return tableCollection.getAll();
    }

    public List<BookingEntity> getAllBooking() {
        return bookingCollection.getAll();
    }

    public List<OrderEntity> getAllNotCheckedOrder() {
        return orderCollection.getAllNotChecked();
    }

    public List<UserEntity> getAllUsers() {
        return userCollection.getAll();
    }

    public void deletePastBooking(String date) {
        bookingCollection.deletePastBookings(date);
    }

    public List<CategoryEntity> getCategoriesByDishType(DishType dishType) {
        return categoryCollection.getCategoriesByDishType(dishType);
    }

    public List<ProductEntity> getAllProducts() {
        return productCollection.getAllProducts();
    }

    public List<TableEntity> getAllTableByServerOrHelper(String user) {
        return tableCollection.getAllTableByServerOrHelper(user);
    }

    public List<TableEntity> getAllTableByServerOrHelperAndState(String user, TableState state) {
        return tableCollection.getAllTableByServerOrHelperAndState(user, state);
    }

    public List<TableEntity> getAllTableByState(TableState state) {
        return tableCollection.getAllTableByState(state);
    }

    public List<OrderEntity> getPreparedOrders() {
        return orderCollection.getPreparedOrders();
    }

    public List<OrderEntity> getServedOrders() {
        return orderCollection.getServedOrders();
    }

    public boolean deleteTable(String idtable) {
        return tableCollection.delete(idtable);
    }

    public List<TableEntity> getAllRemovableTables() {
        return tableCollection.getAllRemovableTables();
    }

    public boolean safeDeleteUser(UserEntity user) {
        if (userCollection.delete(user, false)) {
            removeUserFromAllTable(user);
            return true;
        }
        return false;
    }

    public boolean promoteUser(UserEntity user, Role role) {
        if (user.getRole() == role || user.getRole() == Role.DIRECTOR || role != Role.DIRECTOR && role != Role.BUTLER) {
            return false;
        }

        UserEntity previous = userCollection.getDirectionUser(role);

        if (previous != null) {
            userCollection.delete(previous, true);
        }

        removeUserFromAllTable(user);
        user.setRole(role);

        return update(user);
    }

    public List<DishEntity> getAllDishesNotOnTheMenuOrdered() {
        return dishCollection.getAllDishesNotOnMenu()
                .stream()
                .sorted(DishEntity::orderDishByType)
                .collect(Collectors.toList());
    }

    public List<DishEntity> getAllDishesOntheMenuOrdered() {
        return dishCollection.getAllDishesOnTheMenu()
                .stream()
                .sorted(DishEntity::orderDishByType)
                .collect(Collectors.toList());
    }

    public String getTakingByPeriod(DateDto period, MealType mealType) {
        List<BillEntity> billEntities = billCollection.getBillsByPeriodAndMealType(period, mealType);

        BigInteger taking = BigInteger.valueOf(billEntities.stream()
                .map(BillEntity::getTotalPrice)
                .reduce(0, Integer::sum));

        return "recette: " + taking + "???";
    }

    public Map.Entry<String, Integer> getMostFamousDish() {
        List<OrderEntity> orderEntities = orderCollection.getAllChecked();
        return orderEntities.stream()
                .map(OrderEntity::getIdsDish)
                .flatMap(List::stream)
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElse(null);
    }

    public List<TableEntity> getTablesReadyToOrderByServer(String userId) {
        if (getUserById(userId).getRole().equals(Role.DIRECTOR)) {
            return tableCollection.getTablesReadyToOrderByServer(StringUtils.EMPTY);
        }
        return tableCollection.getTablesReadyToOrderByServer(userId);
    }

    public List<DishEntity> getDestockableDishesByDishType(DishType dishType) {
        List<DishEntity> rawDishes = dishCollection.getDishOnTheMenuByDishType(dishType);
        List<DishEntity> result = new ArrayList<>();

        for (DishEntity dish : rawDishes) {
            if (dish.checkStock(this)) {
                result.add(dish);
            }
        }
        return result;
    }

    public List<PerformanceEntity> getWeekPerformance() {
        return performanceCollection.getWeekPerformance();
    }

    public List<PerformanceEntity> getAllPerformance() {
        return performanceCollection.getAllPerformance();
    }

    protected boolean updateUser(UserEntity user) {
        if (userCollection.update(user)) {
            removeUserFromAllTable(user);
            return true;
        }
        return false;
    }

    protected void removeUserFromAllTable(UserEntity user) {
        if (user.getRole() != Role.SERVER && user.getRole() != Role.HELPER) {
            return;
        }

        List<TableEntity> tables = getAllTableByServerOrHelper(user.getId());

        tables.forEach(table -> {
            if (user.getRole() == Role.SERVER) {
                table.setIdServer("");
            } else {
                table.setIdHelper("");
            }
            save(table);
        });
    }
}
