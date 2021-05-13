package fr.ul.miage.resto.controller.role;

import fr.ul.miage.resto.Launcher;
import fr.ul.miage.resto.appinfo.Service;
import fr.ul.miage.resto.constants.DishType;
import fr.ul.miage.resto.constants.OrderState;
import fr.ul.miage.resto.constants.Role;
import fr.ul.miage.resto.constants.TableState;
import fr.ul.miage.resto.controller.feature.LogInController;
import fr.ul.miage.resto.dao.service.BaseService;
import fr.ul.miage.resto.model.entity.DishEntity;
import fr.ul.miage.resto.model.entity.OrderEntity;
import fr.ul.miage.resto.model.entity.TableEntity;
import fr.ul.miage.resto.model.entity.UserEntity;
import fr.ul.miage.resto.utils.MenuUtil;
import fr.ul.miage.resto.view.feature.LogInView;
import fr.ul.miage.resto.view.role.DirectorView;
import fr.ul.miage.resto.view.role.ServerView;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;

import java.util.*;

@AllArgsConstructor
public class ServerController extends RoleMenuController {
    private final BaseService baseService;
    private final Service service;
    private final ServerView serverView;

    @Override
    public void callAction(Integer action) {
        Role role = Launcher.getLoggedUser().getRole();
        switch (action) {
            case 0:
                if (role.equals(Role.Director)) {
                    DirectorController directorController = new DirectorController(baseService, service, new DirectorView());
                    directorController.launch(Role.Director);
                } else {
                    LogInController logInController = new LogInController(baseService, service, new LogInView());
                    logInController.disconnect();
                }
                break;
            case 1:
                viewTables(Launcher.getLoggedUser());
                break;
            case 2:
                setTablesDirty(Launcher.getLoggedUser());
                break;
            case 3:
                if (!service.isEndService()) {
                    takeOrders(Launcher.getLoggedUser());
                } else {
                    serverView.displayEndService();
                }
                break;
            case 4:
                serveOrders(Launcher.getLoggedUser());
                break;
            default:
                break;
        }
    }

    protected void viewTables(UserEntity user) {
        List<TableEntity> tables;
        if (Role.Director.equals(user.getRole())) {
            tables = baseService.getAllTables();
        } else {
            tables = baseService.getAllTableByServerOrHelper(user.get_id());
        }

        if (tables.isEmpty()) {
            serverView.displayNoTablesAffected();
        } else {
            serverView.displayTablesAffected(tables);
        }
        askMainMenu();
    }

    protected void setTablesDirty(UserEntity user) {
        List<TableEntity> tablesToDirty = new ArrayList<>();
        if (Role.Director.equals(user.getRole())) {
            tablesToDirty.addAll(baseService.getAllTableByState(TableState.Starter));
            tablesToDirty.addAll(baseService.getAllTableByState(TableState.MainCourse));
            tablesToDirty.addAll(baseService.getAllTableByState(TableState.Dessert));
        } else {
            tablesToDirty.addAll(baseService.getAllTableByServerOrHelperAndState(user.get_id(), TableState.Starter));
            tablesToDirty.addAll(baseService.getAllTableByServerOrHelperAndState(user.get_id(), TableState.MainCourse));
            tablesToDirty.addAll(baseService.getAllTableByServerOrHelperAndState(user.get_id(), TableState.Dessert));
        }

        if (tablesToDirty.isEmpty()) {
            serverView.displayNoTablesToDirty();
        } else {
            serverView.displayTablesToDirty(tablesToDirty);
            int choice = getIntegerInput(0, tablesToDirty.size()) - 1;
            if (choice != -1) {
                tablesToDirty.get(choice).setTableState(TableState.Dirty);
                if (baseService.update(tablesToDirty.get(choice))) {
                    serverView.displayTableDirtyDoAgain();
                    if (doAgain()) {
                        setTablesDirty(user);
                    }
                } else {
                    serverView.displayError();
                }
            }
        }
        launch(Role.Server);
    }

    protected void takeOrders(UserEntity user) {
        List<TableEntity> tables = baseService.getTablesReadyToOrderByServer(user.get_id());
        if (CollectionUtils.isEmpty(tables)) {
            serverView.displayNoTableToTakeOrders();
        } else {
            serverView.displayAskTableToServe();
            serverView.displayTablesAffected(tables);
            int idTableChoice = getIntegerInput(0, tables.size()) - 1;
            if (idTableChoice != -1) {
                HashMap<Integer, MenuUtil> menus = getMenusAvailable(tables.get(idTableChoice).getTableState());
                if (checkAvailabilityMenus(menus)) { //Min un type de plat contient des plats pour les choix disponibles de cette table
                    boolean childOrder = askChildOrder();
                    DishType dishType = getOrderDishType(menus);
                    if (dishType != null) { //Min un plat est disponible pour ce type de plat
                        List<String> idDishes = getDishesOrdered(dishType, menus);
                        List<String> idDrinks = getDrinksOrdered();
                        OrderEntity orderToSave = createOrderToSave(tables.get(idTableChoice).get_id(), childOrder, idDishes, idDrinks);

                        serverView.displayOrderToSave(orderToSave);
                        Integer choice = getIntegerInput(0, 1);
                        if (choice == 1) {
                            baseService.save(orderToSave);
                            serverView.displaySuccess();
                        } else {
                            serverView.displayError();
                            orderToSave.giveStockBack(baseService);
                        }
                    }
                } else {
                    serverView.displayNoDishOnTheMenu();
                }
            }
        }
        launch(Role.Server);
    }

    //Si aucun plat n'est disponible dans aucun type de plat renvoie faux
    protected boolean checkAvailabilityMenus(HashMap<Integer, MenuUtil> menus) {
        for (Map.Entry<Integer, MenuUtil> menu : menus.entrySet()) {
            if (menu.getValue().isDishesAvailable()) {
                return true;
            }
        }
        return false;
    }

    //retourne une carte des types de plats disponibles ou non pour la table choisie
    protected HashMap<Integer, MenuUtil> getMenusAvailable(TableState tableState) {
        HashMap<Integer, MenuUtil> menus = new HashMap<>();

        List<DishEntity> starterDishes = baseService.getDestockableDishesByDishType(DishType.Starter);
        List<DishEntity> mainCourseDishes = baseService.getDestockableDishesByDishType(DishType.MainCourse);
        List<DishEntity> dessertDishes = baseService.getDestockableDishesByDishType(DishType.Dessert);

        switch (tableState) {
            case Occupied:
            case Starter:
                menus.put(1, new MenuUtil(DishType.Starter, !(starterDishes == null || starterDishes.isEmpty()), starterDishes));
                menus.put(2, new MenuUtil(DishType.MainCourse, !(mainCourseDishes == null || mainCourseDishes.isEmpty()), mainCourseDishes));
                menus.put(3, new MenuUtil(DishType.Dessert, !(dessertDishes == null || dessertDishes.isEmpty()), dessertDishes));
                break;
            case MainCourse:
                menus.put(1, new MenuUtil(DishType.MainCourse, !(mainCourseDishes == null || mainCourseDishes.isEmpty()), mainCourseDishes));
                menus.put(2, new MenuUtil(DishType.Dessert, !(dessertDishes == null || dessertDishes.isEmpty()), dessertDishes));
                break;
            case Dessert:
                menus.put(1, new MenuUtil(DishType.Dessert, !(dessertDishes == null || dessertDishes.isEmpty()), dessertDishes));
                break;
            default:
                break;
        }
        return menus;
    }

    protected List<String> getDrinksOrdered() {
        if (!askDrinksOrder()) {
            return new ArrayList<>();
        }
        HashMap<Integer, MenuUtil> menu = new HashMap<>();
        List<DishEntity> drinks = baseService.getDestockableDishesByDishType(DishType.Drink);
        menu.put(1, new MenuUtil(DishType.Drink, !(drinks == null || drinks.isEmpty()), drinks));
        return getDishesOrdered(DishType.Drink, menu);
    }

    protected boolean askDrinksOrder() {
        serverView.displayAskDrinksOrder();
        return getIntegerInput(0, 1) == 1;
    }

    //récupère les choix des clients
    protected List<String> getDishesOrdered(DishType dishType, HashMap<Integer, MenuUtil> menus) {
        List<String> idDishes = new ArrayList<>();
        MenuUtil menu = menus.values().stream().filter(menu1 -> menu1.getDishType().equals(dishType)).findFirst().orElse(null);
        List<DishEntity> allDishes = menu.getDishes();

        serverView.displayMenuByCat(allDishes);
        serverView.displayOrderChoice();
        String input = getStringCommandInput(0, allDishes.size() - 1);
        while (menu.isDishesAvailable() && (!input.equals("-v") || CollectionUtils.isEmpty(idDishes))) {
            if (CollectionUtils.isEmpty(idDishes) && input.equals("-v")) {
                serverView.displayNoDishInTheOrder();
            }
            idDishes = getChosenDishes(input, allDishes, idDishes);

            //maj des plats disponibles
            menu.setDishes(baseService.getDestockableDishesByDishType(dishType));
            allDishes = menu.getDishes();
            menu.setDishesAvailable(!CollectionUtils.isEmpty(allDishes));
            if (menu.isDishesAvailable()) {
                serverView.displayOrderChoice();
                input = getStringCommandInput(0, allDishes.size() - 1);
            } else {
                serverView.displayNoDishOnTheMenu();
            }
        }
        return idDishes;
    }

    protected List<String> getChosenDishes(String input, List<DishEntity> allDishes, List<String> selection) {
        String[] listSelection = input.substring(2).replace(" ", "").split("/");
        String action = String.valueOf(input.charAt(1));
        switch (action) {
            case "a":
                return addDish(allDishes, selection, listSelection);
            case "d":
                return removeDish(allDishes, selection, listSelection);
            default:
                return selection;
        }
    }

    protected List<String> addDish(List<DishEntity> allDishes, List<String> selection, String[] listToAdd) {
        for (String id : listToAdd) {
            DishEntity dishEntity = allDishes.get(Integer.parseInt(id));
            if (dishEntity.checkStock(baseService)) {
                dishEntity.changeStock(baseService, false);
                selection.add(dishEntity.get_id());
            } else {
                serverView.displayNotEnoughStockForDish(dishEntity);
            }
        }
        return selection;
    }

    protected List<String> removeDish(List<DishEntity> allDishes, List<String> selection, String[] listToRemove) {
        if (selection.isEmpty()) {
            serverView.displayNoDishInTheOrder();
        } else {
            for (String id : listToRemove) {
                DishEntity dishEntity = allDishes.get(Integer.parseInt(id));
                if (selection.contains(dishEntity.get_id())) {
                    dishEntity.changeStock(baseService, true);
                    selection.remove(dishEntity.get_id());
                }
            }
        }
        return selection;
    }

    //Demande le type de plat qui composeront la commande, si le type de plat n'a aucun plat disponible, annule la commande
    protected DishType getOrderDishType(HashMap<Integer, MenuUtil> menus) {
        serverView.displayChoiceDishType(menus);
        Integer choice = getIntegerInput(0, menus.size());
        if (choice == 0 || !menus.get(choice).isDishesAvailable()) {
            return null;
        } else {
            return menus.get(choice).getDishType();
        }
    }

    protected Boolean askChildOrder() {
        serverView.displayAskChildOrder();
        return getIntegerInput(0, 1) == 1;
    }

    protected OrderEntity createOrderToSave(String idTable, boolean childOrder, List<String> idDishes, List<String> idDrinks) {
        OrderEntity orderToSave = new OrderEntity();
        orderToSave.set_id(new ObjectId().toString());
        orderToSave.setIdTable(idTable);
        orderToSave.setOrderState(OrderState.Ordered);
        orderToSave.setChildOrder(childOrder);
        idDishes.addAll(idDrinks);
        orderToSave.setIdsDish(idDishes);
        orderToSave.setRank(getNextRank(orderToSave.getChildOrder()));
        return orderToSave;
    }

    protected Integer getNextRank(Boolean childOrder) {
        List<OrderEntity> ordersToPrepare = baseService.getNotPreparedOrders();
        OrderEntity last = ordersToPrepare.stream().filter(order -> childOrder.equals(order.getChildOrder())).max(Comparator.comparingInt(OrderEntity::getRank)).orElse(null);
        if (last == null) {
            return 1;
        }
        return last.getRank() + 1;
    }

    protected void serveOrders(UserEntity user) {
        List<OrderEntity> orders = baseService.getPreparedOrders();
        if (!Role.Director.equals(user.getRole())) {
            List<TableEntity> tables = baseService.getAllTableByServerOrHelper(user.get_id());
            orders = getOnlyServerOrders(orders, tables);
        }

        if (orders.isEmpty()) {
            serverView.displayNoOrdersToServe();
        } else {
            serverView.displayOrdersToServe(orders);
            int choice = getIntegerInput(0, orders.size()) - 1;
            if (choice != -1) {
                orders.get(choice).setOrderState(OrderState.Served);
                TableEntity table = baseService.getTableById(orders.get(choice).getIdTable());
                table.setTableState(orders.get(choice).getDishType(baseService));

                if (baseService.update(orders.get(choice)) && baseService.update(table)) {
                    serverView.displayOrderServeAgain();
                    if (doAgain()) {
                        serveOrders(user);
                    }
                } else {
                    serverView.displayError();
                }
            }
        }
        launch(Role.Server);
    }

    protected List<OrderEntity> getOnlyServerOrders(List<OrderEntity> orders, List<TableEntity> tables) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            int finalI = i;
            if (tables.stream().noneMatch(tableEntity -> tableEntity.get_id().equals(orders.get(finalI).getIdTable()))) {
                orders.remove(i);
            }
        }
        return orders;
    }
}
