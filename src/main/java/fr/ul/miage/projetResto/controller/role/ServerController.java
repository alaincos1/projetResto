package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.controller.feature.LogInController;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;
import fr.ul.miage.projetResto.view.feature.LogInView;
import fr.ul.miage.projetResto.view.role.DirectorView;
import fr.ul.miage.projetResto.view.role.ServerView;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
                takeOrders(Launcher.getLoggedUser());
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
    }

    public void serveOrders(UserEntity user) {
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

    public List<OrderEntity> getOnlyServerOrders(List<OrderEntity> orders, List<TableEntity> tables) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            int finalI = i;
            if (tables.stream().noneMatch(tableEntity -> tableEntity.get_id().equals(orders.get(finalI).getIdTable()))) {
                orders.remove(i);
            }
        }
        return orders;
    }
}
